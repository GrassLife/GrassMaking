package life.grass.grassmaking.manager;

import life.grass.grassmaking.food.Cuisine;
import life.grass.grassmaking.food.FoodElement;
import life.grass.grassmaking.food.Ingredient;
import life.grass.grassmaking.food.Seasoning;
import life.grass.grassmaking.table.Cooker;
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