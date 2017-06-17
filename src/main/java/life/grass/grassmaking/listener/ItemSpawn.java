package life.grass.grassmaking.listener;

import life.grass.grassitem.GrassItem;
import life.grass.grassitem.tag.UnityTag;
import life.grass.grassmaking.food.Food;
import life.grass.grassmaking.food.FoodElement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class ItemSpawn implements Listener {

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        ItemStack item = event.getEntity().getItemStack();
        GrassItem grassItem = new GrassItem(item);
        if (grassItem.hasNBT(UnityTag.JSON_NAME)) return;

        Food food = null;
        switch (item.getType()) {
            case RAW_BEEF:
                food = Food.fromItemStack(item);
                food.setAdditionalWeight((int) (Math.random() * 80.0) - 40);
                food.increaseAdditionalElement(FoodElement.SACHI, (int) (Math.random() + 0.5));
        }

        if (food != null) event.getEntity().setItemStack(food.getItem());
    }
}
