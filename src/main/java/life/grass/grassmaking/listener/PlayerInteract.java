package life.grass.grassmaking.listener;

import life.grass.grassitem.GrassItem;
import life.grass.grassmaking.GrassMaking;
import life.grass.grassmaking.manager.TableManager;
import life.grass.grassmaking.operation.Operable;
import life.grass.grassmaking.table.Pot;
import life.grass.grassmaking.table.Table;
import life.grass.grassmaking.tag.CookingTag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Arrays;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        // debugging
        if (event.getItem() != null && event.getAction() == Action.LEFT_CLICK_AIR) {
            GrassItem grassItem = new GrassItem(event.getItem());
            Arrays.stream(CookingTag.values()).forEach(tag -> {
                player.sendMessage(tag.getKey() + ": " + (grassItem.hasNBT(tag) ? grassItem.getNBT(tag).toString() : "NULL"));
            });
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK
                || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        TableManager tableManager = GrassMaking.getTableManager();

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
