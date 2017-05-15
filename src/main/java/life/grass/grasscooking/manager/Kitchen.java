package life.grass.grasscooking.manager;

import life.grass.grasscooking.food.Cuisine;
import life.grass.grasscooking.food.Ingredient;
import life.grass.grasscooking.food.Seasoning;
import life.grass.grasscooking.table.Cooker;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Kitchen {

    public static Cuisine cook(Cooker cooker, List<Ingredient> ingredientList, List<Seasoning> seasoningList) {
        // TODO: change
        ItemStack cuisine = new ItemStack(Material.COOKED_CHICKEN);
        cuisine.setAmount((int) (Math.random() * 3 + 1));
        return Cuisine.fromItemStack(cuisine);
    }
}