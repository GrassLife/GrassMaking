package life.grass.grassmaking.listener;

import life.grass.grassmaking.GrassMaking;
import life.grass.grassmaking.manager.TableManager;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;

public class BlockBreak implements Listener {
    private static TableManager tableManager;

    static {
        tableManager = GrassMaking.getTableManager();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Arrays.asList(
                event.getBlock(),
                event.getBlock().getRelative(BlockFace.UP)
        ).forEach(block ->
                tableManager.findTable(block).ifPresent(table -> {
                    if (table.canOpen(block)) tableManager.remove(block);
                    else event.setCancelled(true);
                })
        );
    }
}
