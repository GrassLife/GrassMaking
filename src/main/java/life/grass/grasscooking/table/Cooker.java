package life.grass.grasscooking.table;

import life.grass.grasscooking.ui.CookerInterface;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class Cooker extends Maker implements CookerInterface {
    private static final ItemStack SEASONING_ICON;

    static {
        SEASONING_ICON = createIcon(Material.NAME_TAG, 0, ChatColor.RED + "調味料", null);
    }

    public Cooker(Block block) {
        super(block);
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
}
