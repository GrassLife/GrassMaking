package life.grass.grassmaking.listener;

import life.grass.grassmaking.table.Maker;
import life.grass.grassmaking.table.cooking.Cooker;
import life.grass.grassmaking.table.enchant.EnchantTable;
import life.grass.grassmaking.ui.MakerInterface;
import life.grass.grassmaking.ui.SelectorInterface;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        int slot = event.getSlot();

        if (inventory == null) return;

        if (inventory.getHolder() instanceof MakerInterface) {
            MakerInterface maker = (MakerInterface) inventory.getHolder();
            ItemStack clicked = event.getCurrentItem();

            if (maker.getPaddingIcon(slot).equals(clicked)) {
                event.setCancelled(true);
                return;
            }

            if (maker instanceof EnchantTable) {
                EnchantTable enchantTable = (EnchantTable) maker;
                if (enchantTable.getGlowstoneIconPosition() == slot
                        || enchantTable.getRedstoneIconPosition() == slot) {
                    event.setCancelled(true);
                }
            }

            if (maker instanceof Maker && event.getRawSlot() < inventory.getSize()) {
                if (maker.getMakingIconPosition() == slot) {
                    event.setCancelled(true);
                    maker.onPressMaking();
                    return;
                }

                if (maker instanceof Cooker) {
                    Cooker cooker = (Cooker) maker;

                    if (cooker.getSeasoningIconPosition() == slot) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        } else if (inventory.getHolder() instanceof SelectorInterface) {
            event.setCancelled(true);
            SelectorInterface selector = ((SelectorInterface) inventory.getHolder());

            if (selector.getSelectedItem(slot) != null) {
                selector.onPressSelectedItem(slot);
            }
        }
    }
}
