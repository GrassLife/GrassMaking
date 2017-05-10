package net.unicroak.grasscooking.table;

import net.unicroak.grasscooking.ui.CookerInterface;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class Cooker extends Maker implements CookerInterface {
    private static final ItemStack SEASONING_ICON;

    static {
        SEASONING_ICON = initIcon(Material.NAME_TAG, 0, ChatColor.RED + "調味料", null);
    }

    @Override
    public ItemStack getSeasoningIcon() {
        return SEASONING_ICON;
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = super.getInventory();

        getSeasoningSpacePositionList().forEach(position -> inv.setItem(position, null));
        inv.setItem(getSeasoningIconPosition(), getSeasoningIcon());

        return inv;
    }
}
