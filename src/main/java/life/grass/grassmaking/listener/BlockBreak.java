package life.grass.grassmaking.listener;

import life.grass.grassmaking.manager.BlockTableHolder;
import life.grass.grassmaking.table.cooking.Cooker;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;

public class BlockBreak implements Listener {
    private static BlockTableHolder tableManager;

    static {
        tableManager = BlockTableHolder.getInstance();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Arrays.asList(
                event.getBlock(),
                event.getBlock().getRelative(BlockFace.UP)
        ).forEach(block ->
                tableManager.findTable(block).ifPresent(table -> {
                    if (table instanceof Cooker) {
                        if (((Cooker) table).getOperation().isOperating()) event.setCancelled(true);
                        else tableManager.remove(block);
                    }
                })
        );
    }
}
