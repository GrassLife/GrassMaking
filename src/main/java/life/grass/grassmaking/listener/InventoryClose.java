package life.grass.grassmaking.listener;

import life.grass.grassmaking.table.BlockTable;
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

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        List<ItemStack> dropList = new ArrayList<>();

        if (holder instanceof BlockTable) {
            BlockTable blockTable = (BlockTable) holder;
            Block block = blockTable.getBlock();

            if (blockTable.canKeepInventory()) return;

            blockTable.getSlotPartMap().forEach((slot, slotPart) -> {
                if (slotPart.canMove()) dropList.add(inventory.getItem(slot));
            });

            Location dropLocation = block.getRelative(BlockFace.UP).getLocation().add(0.5, 0.1, 0.5);
            dropList.forEach(item -> {
                if (item != null) block.getWorld().dropItem(dropLocation, item);
            });
        }
    }
}
