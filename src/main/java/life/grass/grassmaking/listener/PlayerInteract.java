package life.grass.grassmaking.listener;

import life.grass.grassmaking.manager.StationaryTableHolder;
import life.grass.grassmaking.operation.Operable;
import life.grass.grassmaking.table.StationaryTable;
import life.grass.grassmaking.table.cooking.IronPlate;
import life.grass.grassmaking.table.cooking.Manaita;
import life.grass.grassmaking.table.cooking.Pot;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashSet;
import java.util.Set;

public class PlayerInteract implements Listener {
    private static Set<Class<? extends StationaryTable>> tableClassSet;

    static {
        tableClassSet = new HashSet<>();

        tableClassSet.add(IronPlate.class);
        tableClassSet.add(Manaita.class);
        tableClassSet.add(Pot.class);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK
                || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        StationaryTableHolder tableManager = StationaryTableHolder.getInstance();

        StationaryTable stationaryTable;
        Class<? extends StationaryTable> clazz = tableClassSet.stream()
                .filter(tableClass -> {
                    try {
                        return tableClass.getConstructor(Block.class).newInstance(block).canOpen(block);
                    } catch (Exception ex) {
                        return false;
                    }
                })
                .findFirst().orElse(null);

        if (clazz == null) {
            stationaryTable = null;
        } else {
            stationaryTable = tableManager.findTable(block).orElseGet(() -> {
                try {
                    return tableManager.createTable(block, clazz.getConstructor(Block.class).newInstance(block));
                } catch (Exception ex) {
                    return null;
                }
            });
        }

        if (stationaryTable instanceof Operable && stationaryTable.canOpen(block)) {
            if (((Operable) stationaryTable).getOperation().isOperating()) return;

            event.setCancelled(true);
            player.openInventory(stationaryTable.getInventory());
        }
    }
}
