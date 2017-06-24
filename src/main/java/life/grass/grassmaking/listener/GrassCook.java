package life.grass.grassmaking.listener;

import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.cooking.CookingType;
import life.grass.grassmaking.cooking.FoodEffect;
import life.grass.grassmaking.cooking.FoodElement;
import life.grass.grassmaking.cooking.IngredientType;
import life.grass.grassmaking.event.GrassCookEvent;
import life.grass.grassmaking.table.Cooker;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GrassCook implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onGrassCook(GrassCookEvent event) {
        Cooker cooker = event.getCooker();
        CookingType cookingType = event.getCookingType();
        List<ItemStack> ingredientList = event.getIngredientList();
        List<ItemStack> seasoningList = event.getSeasoningList();

        if (cookingType == null || ingredientList.isEmpty()) return;

        ItemStack mainIngredient = ingredientList.stream()
                .sorted(Comparator.comparingInt(ingredient -> JsonHandler.getGrassJson((ItemStack) ingredient)
                        .getDynamicValue("Weight")
                        .getAsMaskedInteger().orElse(10))
                        .reversed())
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        ItemStack mainSeasoning = seasoningList.stream()
                .sorted(Comparator.comparingInt(seasoning -> JsonHandler.getGrassJson((ItemStack) seasoning)
                        .getDynamicValue("Weight")
                        .getAsMaskedInteger().orElse(10))
                        .reversed())
                .findFirst()
                .orElse(null);

        int totalWeight = ingredientList.stream().mapToInt(ingredient -> JsonHandler.getGrassJson(ingredient)
                .getDynamicValue("Weight")
                .getAsMaskedInteger().orElse(10))
                .sum();
        int amount = totalWeight / cookingType.getWeightDivider() + 1;

        int oily = ingredientList.stream()
                .mapToInt(ingredient -> {
                    GrassJson grassJson = JsonHandler.getGrassJson(ingredient);

                    IngredientType ingredientType;
                    if (grassJson.hasItemTag("Meat")) {
                        ingredientType = IngredientType.MEAT;
                    } else if (grassJson.hasItemTag("Fish")) {
                        ingredientType = IngredientType.FISH;
                    } else if (grassJson.hasItemTag("Vegetable")) {
                        ingredientType = IngredientType.VEGETABLE;
                    } else {
                        ingredientType = null;
                    }

                    if (ingredientType == null) return 0;
                    else {
                        return (int) (grassJson.getDynamicValue("Calorie").getAsMaskedInteger().orElse(0) * ingredientType.getOilyMultiple());
                    }
                }).sum() / amount;

        int totalCalorie = (int) (ingredientList.stream().mapToInt(ingredient -> JsonHandler.getGrassJson(ingredient)
                .getDynamicValue("Calorie")
                .getAsMaskedInteger().orElse(0))
                .sum() * seasoningList.stream().mapToDouble(seasoning -> JsonHandler.getGrassJson(seasoning)
                .getDynamicValue("CalorieMultiple")
                .getAsMaskedDouble().orElse(1.0))
                .reduce(1, (n, m) -> n * m));
        int calorie = totalCalorie / amount;

        Map<FoodElement, Integer> foodElementMap = new HashMap<>();
        Arrays.stream(FoodElement.values()).forEach(foodElement -> {
            ingredientList.stream().mapToInt(ingredient -> JsonHandler.getGrassJson(ingredient)
                    .getDynamicValue("FoodElement/" + foodElement.toString())
                    .getAsMaskedInteger()
                    .orElse(0))
                    .filter(elementValue -> elementValue / 2 != 0)
                    .forEach(elementValue -> foodElementMap.put(foodElement, elementValue / 2));
            seasoningList.stream().mapToInt(seasoning -> JsonHandler.getGrassJson(seasoning)
                    .getDynamicValue("FoodElement/" + foodElement.toString())
                    .getAsMaskedInteger().orElse(0))
                    .filter(elementValue -> elementValue != 0)
                    .forEach(elementValue -> foodElementMap.put(foodElement, elementValue * 2));
            foodElementMap.put(
                    foodElement,
                    (int) (foodElementMap.getOrDefault(foodElement, 0) * cookingType.getFoodElementMultiple())
            );
        });

        GrassJson mainIngredientJson = JsonHandler.getGrassJson(mainIngredient);
        Material cuisineMaterial = Material.valueOf(mainIngredientJson.getStaticValue("AfterMaterial")
                .getAsOriginalString()
                .orElse("COOKED_BEEF"));

        ItemStack result = JsonHandler.putUniqueName(new ItemStack(cuisineMaterial, amount), "Cuisine");
        result = JsonHandler.putDynamicData(result, "CustomMaterial", cuisineMaterial);
        result = JsonHandler.putDynamicData(result, "CustomName", cooker.namesCuisine(mainIngredient, mainSeasoning));
        result = JsonHandler.putDynamicData(result, "Calorie", "+" + calorie);

        // TODO: add effect to cuisine from food element

        if (oily == 0) oily = 1;
        if (0 < oily / 10) {
            result = JsonHandler.putDynamicData(result, "FoodEffect/" + FoodEffect.HEAVY_STOMACH.toString(), oily / 10);
        }

        event.setResult(result);
    }
}
