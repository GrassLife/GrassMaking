package life.grass.grassmaking.manager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import life.grass.grassmaking.GrassMaking;
import life.grass.grassmaking.handcrafting.Recipe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class RecipeShelf {
    private static final String RECIPE_DIR_PATH;
    private static Gson gson;
    private static RecipeShelf instance;

    private Map<String, Recipe> recipeMap;

    static {
        RECIPE_DIR_PATH = GrassMaking.getInstance().getDataFolder().getPath() + File.separator + "recipes";
        gson = new Gson();
        instance = new RecipeShelf();
    }

    private RecipeShelf() {
        this.recipeMap = new HashMap<>();

        File folder = new File(RECIPE_DIR_PATH);
        if (!folder.exists()) folder.mkdirs();

        Arrays.stream(folder.listFiles()).filter(file -> file.getName().endsWith(".json"))
                .forEach(file -> loadJsonFromFile(file).ifPresent(json -> {
                            try {
                                Recipe recipe = new Recipe(json);
                                recipeMap.put(recipe.getRecipeName(), recipe);
                                System.out.println("Loaded json: " + file.getName());
                            } catch (Exception ex) {
                                System.out.println("Could not load json: " + file.getName() + " / " + ex.getMessage());
                            }
                        })
                );
    }

    public static RecipeShelf getInstance() {
        return instance;
    }

    public Optional<Recipe> findRecipe(String recipeName) {
        return Optional.ofNullable(recipeMap.get(recipeName));
    }

    public List<Recipe> getRecipeList() {
        return (List<Recipe>) recipeMap.values();
    }

    private static Optional<JsonObject> loadJsonFromFile(File file) {
        JsonObject root;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            root = gson.fromJson(br, JsonObject.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            root = null;
        }

        return Optional.ofNullable(root);
    }
}
