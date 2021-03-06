package life.grass.grassmaking.listener;

import life.grass.grassmaking.manager.BlockTableHolder;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

import java.util.Arrays;

public class BlockPistonExtend implements Listener {
    private static BlockTableHolder tableManager;

    static {
        tableManager = BlockTableHolder.getInstance();
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        event.getBlocks().forEach(pushed ->
                Arrays.asList(
                        pushed,
                        pushed.getRelative(BlockFace.UP)
                ).forEach(block -> tableManager.findTable(block).ifPresent(table -> event.setCancelled(true)))
        );
    }
}
