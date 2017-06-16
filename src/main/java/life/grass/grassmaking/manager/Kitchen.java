package life.grass.grassmaking.manager;

import life.grass.grassitem.GrassItem;
import life.grass.grassmaking.food.*;
import life.grass.grassmaking.operation.CookingOperation;
import life.grass.grassmaking.table.Cooker;
import life.grass.grassmaking.tag.CookingTag;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kitchen {

    public static Cuisine cook(Cooker cooker, List<Ingredient> ingredientList, List<Seasoning> seasoningList) {
        CookingOperation operation = cooker.getOperation();

        Ingredient mainIngredient = operation.getMainIngredient();
        Seasoning mainSeasoning = operation.getMainSeasoning();

        int amount = ingredientList.size();

        Cuisine cuisine = new Cuisine(new ItemStack(mainIngredient.getAfterMaterial(), amount));
        cuisine.setFoodName(mainSeasoning != null ? mainSeasoning.createTastedName(operation) : cooker.getCookingPrefix() + mainIngredient.getFoodName());

        int grossWeight = 0;
        int[] ingredientsGrossWeight = ingredientList.stream().mapToInt(ingredient -> ingredient.getWeight() * ingredient.getItem().getAmount()).toArray();
        for (int i = 0; i < ingredientsGrossWeight.length; i++) grossWeight += ingredientsGrossWeight[i];

        cuisine.setWeight(grossWeight / amount);

        cuisine.setExpireDate(LocalDateTime.now().plusHours(mainIngredient.getExpireHours()));

        Map<FoodElement, Integer> foodElementMap = new HashMap<>();
        ingredientList.stream()
                .filter(ingredient -> Math.random() < 0.6d)
                .forEach(ingredient
                        -> ingredient.getElementMap().forEach((element, level)
                        -> foodElementMap.put(element, level + foodElementMap.getOrDefault(element, 0))));
        seasoningList.forEach(seasoning
                -> seasoning.getElementMap().forEach((element, level)
                -> foodElementMap.put(element, level * 4 + foodElementMap.getOrDefault(element, 0))));
        foodElementMap.forEach((element, level) -> foodElementMap.put(element, level / amount));

        cuisine.setRestoreAmount(grossWeight / 10);

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

        Food food;
        switch (foodType) {
            case CUISINE:
                food = new Cuisine(item);
                break;
            case INGREDIENT_MEAT:
                food = new Meat(item);
                break;
            case SEASONING_SALT:
                food = new Salt(item);
                break;
            default:
                food = null;
                break;
        }

        return food;
    }
}