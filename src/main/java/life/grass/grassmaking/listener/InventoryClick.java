package life.grass.grassmaking.listener;

import life.grass.grassmaking.table.cooking.Cooker;
import life.grass.grassmaking.table.Maker;
import life.grass.grassmaking.table.StationaryTable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory == null || !(inventory.getHolder() instanceof StationaryTable)) return;

        StationaryTable stationaryTable = (StationaryTable) inventory.getHolder();
        ItemStack clicked = event.getCurrentItem();
        int slot = event.getSlot();

        if (stationaryTable.getPaddingIcon(slot).equals(clicked)) {
            event.setCancelled(true);
            return;
        }

        if (stationaryTable instanceof Maker && event.getRawSlot() < inventory.getSize()) {
            Maker maker = (Maker) stationaryTable;

            if (maker.getMakingIconPosition() == slot) {
                event.setCancelled(true);
                maker.onPressMaking();
                return;
            }

            if (stationaryTable instanceof Cooker) {
                Cooker cooker = (Cooker) stationaryTable;

                if (cooker.getSeasoningIconPosition() == slot) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
