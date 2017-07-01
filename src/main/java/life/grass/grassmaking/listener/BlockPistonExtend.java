package life.grass.grassmaking.listener;

import life.grass.grassmaking.GrassMaking;
import life.grass.grassmaking.manager.TableManager;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

import java.util.Arrays;

public class BlockPistonExtend implements Listener {
    private static TableManager tableManager;

    static {
        tableManager = GrassMaking.getTableManager();
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (event.getBlocks().size() < 1) return;
        Block pushed = event.getBlocks().get(0);

        Arrays.asList(
                pushed,
                pushed.getRelative(BlockFace.UP)
        ).forEach(block -> tableManager.findTable(block).ifPresent(table -> event.setCancelled(true)));
    }
}
