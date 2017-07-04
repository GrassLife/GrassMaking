package life.grass.grassmaking.handcrafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import life.grass.grassmaking.exception.IllegalJsonException;
import life.grass.grassmaking.manager.RecipeShelf;

import java.util.HashMap;
import java.util.Map;

public class Recipe {
    private String recipeName;
    private Map<String, Integer> craftingMaterialMap;
    private String resultUniqueName;
    private int resultAmount;
    private RecipeType recipeType;

    public Recipe(JsonObject jsonObject) throws Exception {
        String recipeName = jsonObject.get("RecipeName").getAsString();
        if (recipeName == null || recipeName.isEmpty() || RecipeShelf.getInstance().findRecipe(recipeName).isPresent())
            throw new IllegalJsonException("\"RecipeName\" is wrong");
        else {
            this.recipeName = recipeName;
        }

        Map<String, Integer> craftingMaterialMap = new HashMap<>();
        JsonArray jsonArrayCraftingMaterial = jsonObject.getAsJsonArray("CraftingMaterial");
        jsonArrayCraftingMaterial.forEach(jsonElement -> {
            String material = jsonElement.getAsString().split(":")[0];
            int amount = Integer.valueOf(jsonElement.getAsString().split(":")[1]);
            if (material != null && 0 < amount) craftingMaterialMap.put(material, amount);
        });
        if (craftingMaterialMap.isEmpty()) throw new IllegalJsonException("\"CraftingMaterial\" must not be null");
        else this.craftingMaterialMap = craftingMaterialMap;

        String result = jsonObject.get("Result").getAsString();
        this.resultUniqueName = result.split(":")[0];
        this.resultAmount = Integer.valueOf(result.split(":")[1]);

        this.recipeType = RecipeType.valueOf(jsonObject.get("RecipeType").getAsString().toUpperCase());
    }

    public String getRecipeName() {
        return recipeName;
    }

    public Map<String, Integer> getCraftingMaterialMap() {
        return craftingMaterialMap;
    }

    public String getResultUniqueName() {
        return resultUniqueName;
    }

    public int getResultAmount() {
        return resultAmount;
    }

    public RecipeType getRecipeType() {
        return recipeType;
    }

    public enum RecipeType {
        DEFAULT, ADVANCED;
    }
}
