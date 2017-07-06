package life.grass.grassmaking.ui;

import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface TableInterface extends InventoryHolder {

    String getTitle();

    ItemStack getPaddingIcon(int position);

    default int getTableSize() {
        return 54;
    }
}
