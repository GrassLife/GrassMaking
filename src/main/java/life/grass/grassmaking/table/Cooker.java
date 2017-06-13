package life.grass.grassmaking.table;

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

        operation = new CookingOperation(block, this);
    }

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
                .filter(item -> item != null && item.getType() != Material.AIR)
                .forEach(ingredientList::add);

        List<ItemStack> seasoningList = new ArrayList<>();
        getSeasoningSpacePositionList().stream()
                .map(position -> getInventory().getItem(position))
                .filter(item -> item != null && item.getType() != Material.AIR)
                .forEach(seasoningList::add);

        if (operation.precook(ingredientList, seasoningList)) operation.start(getCookingTick());
    }

    public CookingOperation getOperation() {
        return operation;
    }

    public int getCookingTick() {
        return 5 * 4;
    }
}