package life.grass.grassmaking.table;

import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.cooking.CookingType;
import life.grass.grassmaking.event.GrassCookEvent;
import life.grass.grassmaking.operation.CookingOperation;
import life.grass.grassmaking.ui.CookerInterface;
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

public abstract class Cooker extends Maker implements CookerInterface {
    private static final ItemStack SEASONING_ICON;

    private CookingOperation operation;

    static {
        SEASONING_ICON = createIcon(Material.NAME_TAG, 0, ChatColor.RED + "調味料", null);
    }

    public Cooker(Block block) {
        super(block);

        operation = new CookingOperation(block);
    }

    public abstract String namesCuisine(ItemStack mainIngredient, ItemStack mainSeasoning);

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
    public void onPressedMaking() {
        List<ItemStack> ingredientList = new ArrayList<>();
        getIngredientSpacePositionList().stream()
                .map(position -> getInventory().getItem(position))
                .filter(ingredient -> {
                    if (ingredient == null || JsonHandler.getGrassJson(ingredient).hasItemTag("ingredient")) {
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
                .filter(seasoning -> seasoning != null && JsonHandler.getGrassJson(seasoning).hasItemTag("Seasoning"))
                .forEach(seasoningList::add);

        ItemStack result = cook(ingredientList, seasoningList);
        if (canCook(ingredientList, seasoningList) && result != null) {
            Iterator<HumanEntity> viewerIterator = this.getInventory().getViewers().iterator();
            while (viewerIterator.hasNext()) {
                HumanEntity viewer = viewerIterator.next();
                viewerIterator.remove();
                viewer.closeInventory();
            }

            operation.setCuisine(result);
            operation.start(getCookingTick());
        }
    }

    public ItemStack cook(List<ItemStack> ingredientList, List<ItemStack> seasoningList) {
        GrassCookEvent event = new GrassCookEvent(this, getCookingType(), ingredientList, seasoningList);
        Bukkit.getServer().getPluginManager().callEvent(event);
        return event.getResult();
    }

    public CookingOperation getOperation() {
        return operation;
    }

    public int getCookingTick() {
        return 5 * 4;
    }
}
