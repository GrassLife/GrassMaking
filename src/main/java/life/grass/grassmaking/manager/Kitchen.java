package life.grass.grassmaking.manager;

import life.grass.grassitem.GrassItem;
import life.grass.grassmaking.food.*;
import life.grass.grassmaking.table.Cooker;
import life.grass.grassmaking.tag.CookingTag;
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

    public static Food generateFoodFromItemStack(ItemStack item) {
        GrassItem grassItem = new GrassItem(item);
        FoodType foodType = grassItem.hasNBT(CookingTag.FOOD_TYPE) ? FoodType.valueOf((String) grassItem.getNBT(CookingTag.FOOD_TYPE).get()) : null;

        if (foodType == null) {
            switch (item.getType()) {
                case RAW_BEEF:
                case RAW_CHICKEN:
                case RABBIT:
                case MUTTON:
                    foodType = FoodType.INGREDIENT_MEAT;
                    break;
            }
        }

        switch (foodType) {
            case CUISINE:
                return new Cuisine(item);
            case INGREDIENT_MEAT:
                return new Meat(item);
            case SEASONING_SALT:
                return new Salt(item);
        }

        return null;
    }
}