package life.grass.grasscooking.food.seasoning;

import life.grass.grasscooking.food.Food;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public abstract class Seasoning extends Food {

    protected Seasoning(World world, ItemStack item) {
        super(world, item);
    }
}
