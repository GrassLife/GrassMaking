package life.grass.grassmaking.table.cooking;

import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.cooking.CookingType;
import life.grass.grassmaking.event.GrassCookEvent;
import life.grass.grassmaking.operation.Operation;
import life.grass.grassmaking.table.MakingTable;
import life.grass.grassmaking.ui.SlotPart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class Cooker extends MakingTable {
    private static final String INGREDIENT_TAG = "Ingredient";
    private static final String SEASONING_TAG = "Seasoning";
    protected static final SlotPart INGREDIENT_SPACE_SLOT_PART = new SlotPart(true, true, INGREDIENT_TAG);
    protected static final SlotPart SEASONING_SPACE_SLOT_PART = new SlotPart(true, true, SEASONING_TAG);
    protected static final SlotPart SEASONING_SLOT_PART = new SlotPart(false, false, null, Material.NAME_TAG, 0, ChatColor.RED + "調味料", null);

    public Cooker(Block block) {
        super(block);
    }

    public abstract String namesCuisine(ItemStack mainIngredient, ItemStack accompaniment, ItemStack mainSeasoning);

    public abstract ItemStack getExtendExpireDate(ItemStack item);

    public abstract int getCookingTick();

    public abstract int getMaxCuisineAmount();

    protected abstract CookingType getCookingType();

    protected abstract boolean canCook(List<ItemStack> ingredientList, List<ItemStack> seasoningList);

    @Override
    public boolean canKeepInventory() {
        return true;
    }

    @Override
    public void onPressMaking() {
        List<ItemStack> ingredientList = new ArrayList<>();
        collectTagSlotList(INGREDIENT_TAG).stream()
                .map(position -> getInventory().getItem(position))
                .filter(ingredient -> {
                    GrassJson grassJson = JsonHandler.getGrassJson(ingredient);
                    if (ingredient == null || grassJson == null || !grassJson.hasItemTag("Ingredient")
                            || !grassJson.hasStaticValue("AfterMaterial/" + getCookingType().toString())) {
                        return false;
                    }

                    GrassJson ingredientJson = JsonHandler.getGrassJson(ingredient);
                    LocalDateTime expireDate = LocalDateTime.parse(ingredientJson
                            .getDynamicValue("ExpireDate")
                            .getAsOverwritedString().orElse(LocalDateTime.now().minusSeconds(1).toString()));
                    return ingredientJson.hasItemTag("Ingredient") && expireDate.isAfter(LocalDateTime.now());
                })
                .forEach(ingredientList::add);

        List<ItemStack> seasoningList = new ArrayList<>();
        collectTagSlotList(SEASONING_TAG).stream()
                .map(position -> getInventory().getItem(position))
                .filter(seasoning -> seasoning != null
                        && JsonHandler.getGrassJson(seasoning) != null
                        && JsonHandler.getGrassJson(seasoning).hasItemTag("Seasoning"))
                .forEach(seasoningList::add);

        ItemStack result = cook(ingredientList, seasoningList);
        if (canCook(ingredientList, seasoningList) && result != null) {
            Inventory inventory = getInventory();

            BiConsumer<List<ItemStack>, List<Integer>> consumeItemStackInInventory = (itemStackList, slotList) ->
                    itemStackList.forEach(item -> {
                        int position = slotList.stream().filter(slot -> {
                            ItemStack compared = inventory.getItem(slot);
                            return compared != null && compared.isSimilar(item);
                        }).findFirst().orElse(-1);
                        if (position == -1) return;

                        ItemStack slotItem = inventory.getItem(position);
                        slotItem.setAmount(slotItem.getAmount() - 1);
                        inventory.setItem(position, slotItem);
                    });
            consumeItemStackInInventory.accept(ingredientList, collectTagSlotList(INGREDIENT_TAG));
            consumeItemStackInInventory.accept(seasoningList, collectTagSlotList(SEASONING_TAG));

            List<HumanEntity> viewerList = new ArrayList<>(this.getInventory().getViewers());
            this.getInventory().getViewers().removeIf(viewer -> true);
            viewerList.forEach(HumanEntity::closeInventory);

            Operation operation = getOperation();
            operation.setResult(result);
            operation.start(getCookingTick());
        }
    }

    private ItemStack cook(List<ItemStack> ingredientList, List<ItemStack> seasoningList) {
        GrassCookEvent event = new GrassCookEvent(this, getCookingType(), ingredientList, seasoningList);
        Bukkit.getServer().getPluginManager().callEvent(event);
        return event.getResult();
    }
}
