package life.grass.grasscooking.manager;

import life.grass.grasscooking.food.Cuisine;
import life.grass.grasscooking.food.FoodElement;
import life.grass.grasscooking.food.ingredient.Ingredient;
import life.grass.grasscooking.food.seasoning.Seasoning;
import life.grass.grasscooking.table.Cooker;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kitchen {

    public static Cuisine cook(Cooker cooker, List<Ingredient> ingredientList, List<Seasoning> seasoningList) {
        Cuisine cuisine = new Cuisine(new ItemStack(ingredientList.get(0).getItem()));

        Map<FoodElement, Integer> elementMap = new HashMap<>();
        ingredientList.forEach(ingredient -> ingredient.getElementMap().forEach((key, value) -> {
            elementMap.put(key, elementMap.get(key) + value);
        }));

        elementMap.forEach((key, value) -> cuisine.setElement(key, value / cuisine.getItem().getAmount()));

        return cuisine;
    }
}