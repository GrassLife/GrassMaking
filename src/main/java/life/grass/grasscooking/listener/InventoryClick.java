package life.grass.grasscooking.listener;

import life.grass.grasscooking.table.Cooker;
import life.grass.grasscooking.table.Maker;
import life.grass.grasscooking.table.Table;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null
                || !(event.getClickedInventory().getHolder() instanceof Table)) return;

        Table table = (Table) event.getClickedInventory().getHolder();
        ItemStack clicked = event.getCurrentItem();
        int slot = event.getSlot();

        if (table.getPaddingIcon(slot).equals(clicked)) {
            event.setCancelled(true);
            return;
        }

        if (table instanceof Maker) {
            Maker maker = (Maker) table;

            if (maker.getMakingIconPosition() == slot) {
                event.setCancelled(true);
                maker.onPressedMaking();
                return;
            }

            if (table instanceof Cooker) {
                Cooker cooker = (Cooker) table;

                if (cooker.getSeasoningIconPosition() == slot) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
