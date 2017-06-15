package life.grass.grassmaking.manager;

import life.grass.grassitem.GrassItem;
import life.grass.grassmaking.food.*;
import life.grass.grassmaking.table.Cooker;
import life.grass.grassmaking.tag.CookingTag;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kitchen {

    public static Cuisine cook(Cooker cooker, List<Ingredient> ingredientList, List<Seasoning> seasoningList) {
        // TODO: change
        Cuisine cuisine = new Cuisine(new ItemStack(Material.COOKED_BEEF));
        cuisine.setElement(FoodElement.SALTY, (int) (Math.random() * 2));
        cuisine.setElement(FoodElement.UMAMI, (int) (Math.random() * 3));
        return cuisine;
    }

    public static Food generateFoodFromItemStack(ItemStack item) {
        GrassItem grassItem = new GrassItem(item);
        FoodType foodType = grassItem.hasNBT(CookingTag.FOOD_TYPE) ? FoodType.valueOf((String) grassItem.getNBT(CookingTag.FOOD_TYPE).get()) : FoodType.UNKNOWN;

        if (foodType == FoodType.UNKNOWN) {
            switch (item.getType()) {
                case RAW_BEEF:
                case RAW_CHICKEN:
                case RABBIT:
                case MUTTON:
                    foodType = FoodType.INGREDIENT_MEAT;
                    break;
                default:
                    foodType = FoodType.UNKNOWN;
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