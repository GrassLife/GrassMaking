package life.grass.grasscooking.listener;

import life.grass.grasscooking.food.Cuisine;
import life.grass.grasscooking.food.Food;
import life.grass.grasscooking.food.ingredient.Meat;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class ItemSpawn implements Listener {

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        ItemStack item = event.getEntity().getItemStack();

        if (!true /* check it does not have any GrassItem types */) {
            return;
        }

        switch (item.getType()) {
            case RAW_CHICKEN:
                item = Food.fromItemStack(event.getEntity().getWorld(), new ItemStack(Material.COOKED_BEEF), Meat.class).getItem();
                break;
            case COOKED_CHICKEN:
                item = Food.fromItemStack(event.getEntity().getWorld(), new ItemStack(Material.COOKED_BEEF), Meat.class).getItem();
                break;
        }

        event.getEntity().setItemStack(item);
    }
}
