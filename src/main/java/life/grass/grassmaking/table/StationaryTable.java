package life.grass.grassmaking.table;

import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

public abstract class StationaryTable extends Table {
    private Block block;
    private Inventory inventory;

    public StationaryTable(Block block) {
        this.block = block;
        this.inventory = initInventory();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public Block getBlock() {
        return block;
    }

    public abstract boolean canOpen(Block block);
}
