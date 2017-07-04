package life.grass.grassmaking.ui;

import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface TableInterface extends InventoryHolder {
    int TABLE_SIZE = 54;

    String getTitle();

    ItemStack getPaddingIcon(int position);
}
