package life.grass.grassmaking.food;

import org.bukkit.inventory.ItemStack;

public abstract class Ingredient extends Food {

    protected Ingredient(ItemStack item) {
        super(item);
    }
}