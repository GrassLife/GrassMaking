package life.grass.grasscooking.listener;

import life.grass.grasscooking.GrassCooking;
import life.grass.grasscooking.manager.TableManager;
import life.grass.grasscooking.operation.Operable;
import life.grass.grasscooking.table.Pot;
import life.grass.grasscooking.table.Table;
import org.bukkit.block.Block;
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

        TableManager tableManager = GrassCooking.getTableManager();

        // TODO: change
        Table table = null;
        switch (block.getType()) {
            case CAULDRON:
                table = tableManager.findTable(block).orElseGet(() -> tableManager.createTable(block, Pot.class));
                break;
        }

        if (table != null) {
            if (table instanceof Operable) {
                if (((Operable) table).getOperation().isOperating()) return;
            }

            player.openInventory(table.getInventory());
        }
    }
}
