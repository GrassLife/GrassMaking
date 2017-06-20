package life.grass.grassmaking.listener;

import life.grass.grassmaking.food.Food;
import life.grass.grassmaking.food.FoodElement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EntityDeath implements Listener {

    // TODO: write generator
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        List<ItemStack> dropList = new ArrayList<>();
        event.getDrops().stream()
                .filter(drop -> Food.makeFood(drop).isPresent())
                .forEach(drop -> {
                    Food food = Food.makeFood(drop).get();
                    switch (drop.getType()) {
                        case RAW_BEEF:
                            food.setAdditionalWeight((int) (Math.random() * 80.0));
                            food.putElement(FoodElement.SACHI, (int) (Math.random() * 3.0));
                            food.setExpireDate(LocalDateTime.now().plusHours(12));
                            break;
                    }
                    dropList.add(food.getItem());
                });
        event.getDrops().clear();
        event.getDrops().addAll(dropList);
    }
}
