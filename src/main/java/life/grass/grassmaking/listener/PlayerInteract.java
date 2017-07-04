package life.grass.grassmaking.listener;

import life.grass.grassmaking.manager.StationaryTableHolder;
import life.grass.grassmaking.operation.Operable;
import life.grass.grassmaking.table.StationaryTable;
import life.grass.grassmaking.table.cooking.IronPlate;
import life.grass.grassmaking.table.cooking.Manaita;
import life.grass.grassmaking.table.cooking.Pot;
import life.grass.grassmaking.table.handcrafting.HandyCraftingTable;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

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
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getHand() != EquipmentSlot.HAND) return;

        switch (event.getAction()) {
            case RIGHT_CLICK_BLOCK:
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
                break;
            case RIGHT_CLICK_AIR:
                if (item == null || item.getType() != Material.BREWING_STAND_ITEM) return;

                HandyCraftingTable handyCraftingTable = new HandyCraftingTable();
                player.openInventory(handyCraftingTable.getInventory());
                break;
        }
    }
}
