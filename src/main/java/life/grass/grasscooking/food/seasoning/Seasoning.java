package life.grass.grasscooking.food.seasoning;

import life.grass.grasscooking.food.Food;
import org.bukkit.inventory.ItemStack;

public abstract class Seasoning extends Food {

    protected Seasoning(ItemStack item) {
        super(item);
    }
}
