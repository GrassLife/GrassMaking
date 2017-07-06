package life.grass.grassmaking.listener;

import life.grass.grassmaking.table.Maker;
import life.grass.grassmaking.table.enchant.EnchantTable;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryClose implements Listener {
    private static final String ENCHANT_PACKAGE = "life.grass.grassmaking.table.enchant";

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (holder.getClass().getName().contains(ENCHANT_PACKAGE)) {
            List<ItemStack> dropList = new ArrayList<>();

            if (holder instanceof Maker) {
                Maker maker = (Maker) holder;
                Block block = maker.getBlock();

                maker.getIngredientSpacePositionList().forEach(position -> dropList.add(inventory.getItem(position)));

                if (holder instanceof EnchantTable) {
                    EnchantTable enchantTable = ((EnchantTable) holder);
                    dropList.add(inventory.getItem(enchantTable.getRedstoneSpacePosition()));
                    dropList.add(inventory.getItem(enchantTable.getGlowstoneSpacePosition()));
                }

                Location dropLocation = block.getRelative(BlockFace.UP).getLocation().add(0.5, 0.1, 0.5);
                dropList.forEach(item -> {
                    if (item != null) block.getWorld().dropItem(dropLocation, item);
                });
            }
        }
    }
}
