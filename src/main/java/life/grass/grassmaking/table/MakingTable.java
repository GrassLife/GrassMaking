package life.grass.grassmaking.table;

import org.bukkit.block.Block;

public abstract class MakingTable extends BlockTable {
    public static final String MAKING_TAG = "Making";

    public MakingTable(Block block) {
        super(block);
    }

    public abstract void onPressMaking();
}
