package life.grass.grassmaking.listener;

import life.grass.grassmaking.manager.TableManager;
import life.grass.grassmaking.operation.Operable;
import life.grass.grassmaking.table.Table;
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
    private static Set<Class<? extends Table>> tableClassSet;

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

        TableManager tableManager = TableManager.getInstance();

        Table table;
        Class<? extends Table> clazz = tableClassSet.stream()
                .filter(tableClass -> {
                    try {
                        return tableClass.getConstructor(Block.class).newInstance(block).canOpen(block);
                    } catch (Exception ex) {
                        return false;
                    }
                })
                .findFirst().orElse(null);

        if (clazz == null) {
            table = null;
        } else {
            table = tableManager.findTable(block).orElseGet(() -> {
                try {
                    return tableManager.createTable(block, clazz.getConstructor(Block.class).newInstance(block));
                } catch (Exception ex) {
                    return null;
                }
            });
        }

        if (table instanceof Operable && table.canOpen(block)) {
            if (((Operable) table).getOperation().isOperating()) return;

            event.setCancelled(true);
            player.openInventory(table.getInventory());
        }
    }
}
