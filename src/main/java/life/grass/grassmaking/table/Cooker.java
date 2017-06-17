package life.grass.grassmaking.table;

import life.grass.grassmaking.food.Food;
import life.grass.grassmaking.operation.CookingOperation;
import life.grass.grassmaking.ui.CookerInterface;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
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

    protected abstract Food cook(List<Food> ingredientList, List<Food> seasoningList);

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
        List<Food> ingredientList = new ArrayList<>();
        getIngredientSpacePositionList().stream()
                .map(position -> getInventory().getItem(position))
                .filter(item -> item != null && item.getType() != Material.AIR)
                .map(Food::fromItemStack)
                .filter(food -> food != null && food.getType().toString().startsWith("INGREDIENT_"))
                .forEach(ingredientList::add);

        List<Food> seasoningList = new ArrayList<>();
        getSeasoningSpacePositionList().stream()
                .map(position -> getInventory().getItem(position))
                .filter(item -> item != null && item.getType() != Material.AIR)
                .map(Food::fromItemStack)
                .filter(food -> food != null && food.getType().toString().startsWith("SEASONING_"))
                .forEach(seasoningList::add);

        Food result = cook(ingredientList, seasoningList);
        if (result != null) {
            operation.setCuisine(result);
            operation.start(getCookingTick());
        }
    }

    public CookingOperation getOperation() {
        return operation;
    }

    public int getCookingTick() {
        return 5 * 4;
    }

    protected void subtractOneItem(Inventory inventory, ItemStack item) {
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack slotItem = inventory.getItem(slot);


            if (slotItem != null && slotItem.isSimilar(item)) {
                inventory.setItem(slot, null);
                return;
            }
        }
    }
}
