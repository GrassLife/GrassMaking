package life.grass.grasscooking.food.ingredient;

import life.grass.grasscooking.food.Food;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public abstract class Ingredient extends Food {

    protected Ingredient(World world, ItemStack item) {
        super(world, item);
    }
}
