package life.grass.grasscooking.listener;

import life.grass.grasscooking.food.FoodElement;
import life.grass.grasscooking.food.ingredient.Meat;
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
                Meat meat = new Meat(item);
                meat.setElement(FoodElement.SWEET, -1);
                meat.setElement(FoodElement.SALTY, 3);
                meat.setElement(FoodElement.UMAMI, 2);
                item = meat.getItem();
                break;
            case COOKED_CHICKEN:
                item = new Meat(item).getItem();
                break;
        }

        event.getEntity().setItemStack(item);
    }
}
