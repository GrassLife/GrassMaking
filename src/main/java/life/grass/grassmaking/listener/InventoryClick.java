package life.grass.grassmaking.listener;

import life.grass.grassmaking.table.Cooker;
import life.grass.grassmaking.table.Maker;
import life.grass.grassmaking.table.Table;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() == null
                || !(event.getInventory().getHolder() instanceof Table)) return;

        Table table = (Table) event.getInventory().getHolder();
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
