package life.grass.grassmaking.listener;

import life.grass.grassmaking.GrassMaking;
import life.grass.grassmaking.manager.TableManager;
import life.grass.grassmaking.operation.Operable;
import life.grass.grassmaking.table.IronPlate;
import life.grass.grassmaking.table.Table;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK
                || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        TableManager tableManager = GrassMaking.getTableManager();
        Table table = null;
        switch (block.getType()) {
            case STONE_PLATE:
                if (block.getRelative(BlockFace.DOWN).getType() == Material.MAGMA)
                    table = tableManager.findTable(block).orElseGet(() -> tableManager.createTable(block, IronPlate.class));
                break;
        }

        if (table instanceof Operable) {
            if (((Operable) table).getOperation().isOperating()) return;

            player.openInventory(table.getInventory());
        }
    }
}
