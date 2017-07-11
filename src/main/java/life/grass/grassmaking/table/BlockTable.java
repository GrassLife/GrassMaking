package life.grass.grassmaking.table;

import life.grass.grassmaking.operation.Operable;
import org.bukkit.block.Block;

public abstract class BlockTable extends Table implements Operable {
    private Block block;

    public BlockTable(Block block) {
        super();

        this.block = block;
    }

    public abstract boolean canKeepInventory();

    public abstract boolean canOpen(Block block);

    public Block getBlock() {
        return block;
    }
}
