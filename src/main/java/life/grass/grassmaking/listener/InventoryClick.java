package life.grass.grassmaking.listener;

import life.grass.grassmaking.table.MakingTable;
import life.grass.grassmaking.table.Selector;
import life.grass.grassmaking.table.Table;
import life.grass.grassmaking.ui.SlotPart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory == null) return;

        InventoryHolder holder = inventory.getHolder();
        int slot = event.getRawSlot();

        if (holder instanceof Table) {
            Table table = (Table) holder;
            if (slot < 0 || table.getTableSize() <= slot) return;

            SlotPart slotPart = table.getSlotPart(slot);
            String tag = slotPart.getTag().orElse("EMPTY");

            if (!slotPart.canMove()) event.setCancelled(true);

            if (holder instanceof MakingTable) {
                MakingTable makingTable = (MakingTable) holder;
                if (tag.equalsIgnoreCase(MakingTable.MAKING_TAG)) makingTable.onPressMaking();
            }

            if (holder instanceof Selector) {
                Selector selector = (Selector) holder;
                if (tag.equalsIgnoreCase(Selector.SELECTING_TAG)) selector.onPressSelecting(slot);
            }
        }
    }
}
