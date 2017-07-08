package life.grass.grassmaking.table.cooking;

import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.cooking.CookingType;
import life.grass.grassmaking.event.GrassCookEvent;
import life.grass.grassmaking.operation.ResultOperation;
import life.grass.grassmaking.operation.cooking.CookingOperation;
import life.grass.grassmaking.table.Maker;
import life.grass.grassmaking.ui.cooking.CookerInterface;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public abstract class Cooker extends Maker implements CookerInterface {
    private static final ItemStack SEASONING_ICON;

    private CookingOperation operation;

    static {
        SEASONING_ICON = createIcon(Material.NAME_TAG, 0, ChatColor.RED + "調味料", null);
    }

    public Cooker(Block block, CookingOperation operation) {
        super(block);

        this.operation = operation;
    }

    public abstract String namesCuisine(ItemStack mainIngredient, ItemStack accompaniment, ItemStack mainSeasoning);

    public abstract ItemStack extendExpireDate(ItemStack item);

    protected abstract CookingType getCookingType();

    protected abstract boolean canCook(List<ItemStack> ingredientList, List<ItemStack> seasoningList);

    @Override
    public ItemStack getSeasoningIcon() {
        return SEASONING_ICON;
    }

    @Override
    public Inventory initInventory() {
        Inventory inv = super.initInventory();

        getSeasoningSpacePositionList().forEach(position -> inv.setItem(position, null));
        inv.setItem(getSeasoningIconPosition(), getSeasoningIcon());

        return inv;
    }

    @Override
    public void onPressMaking() {
        List<ItemStack> ingredientList = new ArrayList<>();
        getIngredientSpacePositionList().stream()
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
        getSeasoningSpacePositionList().stream()
                .map(position -> getInventory().getItem(position))
                .filter(seasoning -> seasoning != null
                        && JsonHandler.getGrassJson(seasoning) != null
                        && JsonHandler.getGrassJson(seasoning).hasItemTag("Seasoning"))
                .forEach(seasoningList::add);

        ItemStack result = cook(ingredientList, seasoningList);
        if (canCook(ingredientList, seasoningList) && result != null) {
            Inventory inventory = getInventory();

            Consumer<List<ItemStack>> consumeItemStackInInventory = itemStackList ->
                    itemStackList.forEach(item -> {
                        int position = inventory.first(item);
                        if (position == -1) return;

                        ItemStack slotItem = inventory.getItem(position);
                        slotItem.setAmount(slotItem.getAmount() - 1);
                        inventory.setItem(position, slotItem);
                    });
            consumeItemStackInInventory.accept(ingredientList);
            consumeItemStackInInventory.accept(seasoningList);

            Iterator<HumanEntity> viewerIterator = this.getInventory().getViewers().iterator();
            while (viewerIterator.hasNext()) {
                HumanEntity viewer = viewerIterator.next();
                viewerIterator.remove();
                viewer.closeInventory();
            }

            operation.setResult(result);
            operation.start(getCookingTick());
        }
    }

    public ItemStack cook(List<ItemStack> ingredientList, List<ItemStack> seasoningList) {
        GrassCookEvent event = new GrassCookEvent(this, getCookingType(), ingredientList, seasoningList);
        Bukkit.getServer().getPluginManager().callEvent(event);
        return event.getResult();
    }

    public ResultOperation getOperation() {
        return operation;
    }

    public int getCookingTick() {
        return 20 * 10;
    }
}
