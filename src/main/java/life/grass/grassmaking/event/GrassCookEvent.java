package life.grass.grassmaking.event;

import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.cooking.CookingType;
import life.grass.grassmaking.cooking.FoodEffect;
import life.grass.grassmaking.cooking.FoodElement;
import life.grass.grassmaking.table.Cooker;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GrassCookEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private Cooker cooker;
    private CookingType cookingType;
    private List<ItemStack> ingredientList;
    private List<ItemStack> seasoningList;
    private boolean isCancelled = false;

    public GrassCookEvent(Cooker cooker, CookingType cookingType, List<ItemStack> ingredientList, List<ItemStack> seasoningList) {
        this.cooker = cooker;
        this.cookingType = cookingType;
        this.ingredientList = ingredientList;
        this.seasoningList = seasoningList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    public Cooker getCooker() {
        return cooker;
    }

    public CookingType getCookingType() {
        return cookingType;
    }

    public List<ItemStack> getIngredientList() {
        return ingredientList;
    }

    public List<ItemStack> getSeasoningList() {
        return seasoningList;
    }

    public ItemStack getResult() {
        if (ingredientList.isEmpty()) return null;

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

        int totalCalorie = (int) (ingredientList.stream().mapToInt(ingredient -> JsonHandler.getGrassJson(ingredient)
                .getDynamicValue("Calorie")
                .getAsMaskedInteger().orElse(0))
                .sum() * seasoningList.stream().mapToDouble(seasoning -> JsonHandler.getGrassJson(seasoning)
                .getDynamicValue("CalorieMultiple")
                .getAsMaskedDouble().orElse(1.0))
                .reduce(1, (n, m) -> n * m));
        int calorie = totalCalorie / amount;

        int totalOily = (int) (ingredientList.stream().mapToInt(ingredient -> JsonHandler.getGrassJson(ingredient)
                .getDynamicValue("Oily")
                .getAsMaskedInteger().orElse(0))
                .sum() * cookingType.getOilyMultiple());
        int oily = totalOily * 4 / amount;

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
        result = JsonHandler.putDynamicData(result, "CustomDisplayName", cooker.namesCuisine(mainIngredient, mainSeasoning));
        result = JsonHandler.putDynamicData(result, "Calorie", "+" + calorie);

        // TODO: add effect to cuisine from food element

        if (0 < oily / 10)
            result = JsonHandler.putDynamicData(result, "FoodEffect/" + FoodEffect.HEAVY_STOMACH.toString(), oily / 10);

        return result;
    }
}
