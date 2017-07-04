package life.grass.grassmaking.table;

import life.grass.grassmaking.ui.SelectorInterface;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class HandyTable extends Table implements SelectorInterface {
    private Inventory inventory;

    public HandyTable() {
        super();

        this.inventory = initInventory();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Inventory initInventory() {
        Inventory inv = super.initInventory();

        for (int i = 0; i < Table.TABLE_SIZE; i++) {
            ItemStack selectedItem = getSelectedItem(i);

            if (selectedItem != null && selectedItem.getType() != Material.AIR) inv.setItem(i, selectedItem);
        }

        return inv;
    }
}
