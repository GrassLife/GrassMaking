package life.grass.grassmaking.listener;

import life.grass.grassmaking.table.Maker;
import life.grass.grassmaking.table.StationaryTable;
import life.grass.grassmaking.table.cooking.Cooker;
import life.grass.grassmaking.table.handcrafting.HandyCraftingTable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = ((Player) event.getWhoClicked());
        Inventory inventory = event.getInventory();
        int slot = event.getSlot();

        if (inventory == null) return;

        if (inventory.getHolder() instanceof StationaryTable) {
            StationaryTable stationaryTable = (StationaryTable) inventory.getHolder();
            ItemStack clicked = event.getCurrentItem();

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
        } else if (inventory.getHolder() instanceof HandyCraftingTable) {
            event.setCancelled(true);
            HandyCraftingTable HandyCraftingTable = ((HandyCraftingTable) inventory.getHolder());

            if (HandyCraftingTable.getSelectedItem(slot) != null) {
                HandyCraftingTable.onPressSelectedItem(slot);
            }
        }
    }
}
