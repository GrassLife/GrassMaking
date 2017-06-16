package life.grass.grassmaking.food;

import org.bukkit.inventory.ItemStack;

public class Meat extends Ingredient {

    public Meat(ItemStack item) {
        super(item);
        setFoodType(FoodType.INGREDIENT_MEAT);
    }

    @Override
    public int getExpireHours() {
        return 18;
    }
}
