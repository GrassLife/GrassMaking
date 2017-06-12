package life.grass.grassmaking.manager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import life.grass.grassmaking.GrassMaking;
import life.grass.grassmaking.food.Food;
import life.grass.grassmaking.food.FoodEffect;
import life.grass.grassmaking.food.FoodElement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

public class JsonFoodHamper {
    private static final String FOOD_PACKAGE;

    private Map<String, Food> jsonFoodMap;
    private File folder;
    private Gson gson;

    static {
        FOOD_PACKAGE = "life.grass.grasscooking.food";
    }

    public JsonFoodHamper() {
        jsonFoodMap = new HashMap<>();
        folder = new File(GrassMaking.getInstance().getDataFolder() + File.separator + "json");
        gson = new Gson();

        reload();
    }

    public Optional<Food> getFood(String fileName) {
        return Optional.ofNullable(jsonFoodMap.get(fileName));
    }

    public void reload() {
        if (!folder.exists()) folder.mkdirs();
        loadAll();
    }

    private void loadAll() {
        Arrays.stream(folder.listFiles((dir, name) -> name.endsWith(".json"))).forEach(json -> {
            Food food = makeFoodFromJson(json);
            if (food == null) {
                System.out.println("Failed in loading " + json.getName());
            } else {
                jsonFoodMap.put(json.getName().replace(".json", ""), food);
                System.out.println("Loaded " + json.getName());
            }
        });
    }

    private Food makeFoodFromJson(File json) {
        try (BufferedReader br = new BufferedReader(new FileReader(json))) {
            JsonObject root = gson.fromJson(br, JsonObject.class);

            Class<? extends Food> foodClass = (Class<? extends Food>) Class.forName(FOOD_PACKAGE + "." + root.get("FoodType").getAsString());

            JsonObject itemStack = root.getAsJsonObject("ItemStack");
            String name = ChatColor.translateAlternateColorCodes('&', itemStack.get("Name").getAsString());
            List<String> lore = new ArrayList<>();
            itemStack.get("Lore").getAsJsonArray().forEach(jsonElement ->
                    lore.add(ChatColor.translateAlternateColorCodes('&', jsonElement.getAsString()))
            );
            Material material = Material.valueOf(itemStack.get("Material").getAsString());
            short data = itemStack.get("Data").getAsShort();

            JsonObject expireDate = root.getAsJsonObject("Expire");
            int expireDays = expireDate.get("Days").getAsInt();
            int expireHours = expireDate.get("Hours").getAsInt();
            int expireMinutes = expireDate.get("Minutes").getAsInt();
            int expireSeconds = expireDate.get("Seconds").getAsInt();

            int restoreAmount = root.get("RestoreAmount").getAsInt();

            Map<FoodElement, Integer> elementMap = new HashMap<>();
            Stream.of(root.getAsJsonArray("Element"))
                    .map(element -> gson.fromJson(element, Map.class))
                    .forEach(map -> map.forEach((key, value) -> {
                        FoodElement element = FoodElement.valueOf((String) key);
                        elementMap.put(element, (elementMap.getOrDefault(element, 0) + (int) (double) value));
                    }));

            Map<FoodEffect, Integer> effectMap = new HashMap<>();
            Stream.of(root.getAsJsonArray("Effect"))
                    .map(element -> gson.fromJson(element, Map.class))
                    .forEach(map -> map.forEach((key, value) -> {
                        FoodElement element = FoodElement.valueOf((String) key);
                        elementMap.put(element, elementMap.getOrDefault(element, 0) + (int) (double) value);
                    }));

            ItemStack item = new ItemStack(material, 1, data);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);

            Food food = foodClass.getConstructor(ItemStack.class).newInstance(item);
            food.setExpireDate(LocalDateTime.now()
                    .plusDays(expireDays)
                    .plusHours(expireHours)
                    .plusMinutes(expireMinutes)
                    .plusSeconds(expireSeconds)
            );
            food.setRestoreAmount(restoreAmount);
            elementMap.forEach(food::setElement);
            effectMap.forEach(food::setEffect);

            return food;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
