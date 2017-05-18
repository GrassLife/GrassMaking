package life.grass.grasscooking.manager;

import life.grass.grasscooking.food.Cuisine;
import life.grass.grasscooking.food.Food;
import life.grass.grasscooking.food.ingredient.Ingredient;
import life.grass.grasscooking.food.seasoning.Seasoning;
import life.grass.grasscooking.table.Cooker;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Kitchen {

    public static Cuisine cook(Cooker cooker, List<Ingredient> ingredientList, List<Seasoning> seasoningList) {
        return Food.fromItemStack(cooker.getBlock().getWorld(), new ItemStack(Material.COOKED_BEEF), Cuisine.class);
    }
}