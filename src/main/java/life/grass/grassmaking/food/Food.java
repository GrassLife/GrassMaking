package life.grass.grassmaking.food;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import life.grass.grassitem.GrassItem;
import life.grass.grassitem.tag.UnityTag;
import life.grass.grassmaking.GrassMaking;
import life.grass.grassmaking.tag.FoodTag;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Food {
    private static final String JSON_DIR_PATH;

    private static Gson gson;

    private ItemStack item;
    private String jsonName;
    private String name;
    private FoodType type;
    private int baseWeight, additionalWeight;
    private int baseRestoreAmount, additionalRestoreAmount;
    private Material afterMaterial;
    private LocalDateTime expireDate;
    private Map<FoodElement, Integer> baseElementMap, additionalElementMap;

    static {
        JSON_DIR_PATH = GrassMaking.getInstance().getDataFolder().getPath() + File.separator + "json" + File.separator;
        gson = new Gson();
    }

    private Food(ItemStack item, String jsonName) {
        try (BufferedReader br = new BufferedReader(new FileReader(JSON_DIR_PATH + jsonName + ".json"))) {
            JsonObject root = gson.fromJson(br, JsonObject.class);

            this.item = item;
            this.jsonName = jsonName;
            this.name = root.get("FoodName").getAsString();
            this.type = FoodType.valueOf(root.get("FoodType").getAsString());
            this.baseWeight = root.get("Weight").getAsInt();
            this.afterMaterial = Material.valueOf(root.get("AfterMaterial").getAsString());

            this.baseRestoreAmount = root.get("RestoreAmount").getAsInt();

            JsonObject expireJsonObject = root.get("Expire").getAsJsonObject();
            this.expireDate = LocalDateTime.now()
                    .plusDays(expireJsonObject.get("Days").getAsLong())
                    .plusHours(expireJsonObject.get("Hours").getAsLong())
                    .plusMinutes(expireJsonObject.get("Minutes").getAsLong())
                    .plusSeconds(expireJsonObject.get("Seconds").getAsLong());

            this.additionalElementMap = new HashMap<>();
            this.baseElementMap = new HashMap<>();
            Stream.of(root.getAsJsonObject("Element"))
                    .map(element -> gson.fromJson(element, Map.class))
                    .forEach(map -> map.forEach((key, value) -> {
                        FoodElement element = FoodElement.valueOf((String) key);
                        baseElementMap.put(element, (baseElementMap.getOrDefault(element, 0) + (int) (double) value));
                    }));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Food fromItemStack(ItemStack item) {
        if (item == null) return null;

        GrassItem grassItem = new GrassItem(item);

        final Food food;
        if (grassItem.hasNBT(UnityTag.JSON_NAME)) {
            food = new Food(item, (String) grassItem.getNBT(UnityTag.JSON_NAME).get());
        } else {
            switch (item.getType()) {
                case RAW_BEEF:
                    food = new Food(item, "RawBeef");
                    break;
                default:
                    food = null;
                    break;
            }
        }

        if (food != null) {
            if (grassItem.hasNBT(FoodTag.RESTORE_AMOUNT)) {
                String restoreAmount = (String) grassItem.getNBT(FoodTag.RESTORE_AMOUNT).get();
                if (restoreAmount.startsWith("+"))
                    food.setAdditionalRestoreAmount(Integer.parseInt(restoreAmount.substring(1)));
                else if (restoreAmount.startsWith("-"))
                    food.setAdditionalRestoreAmount(-1 * Integer.parseInt(restoreAmount.substring(1)));
            }

            if (grassItem.hasNBT(FoodTag.WEIGHT)) {
                String weight = (String) grassItem.getNBT(FoodTag.WEIGHT).get();
                if (weight.startsWith("+"))
                    food.setAdditionalWeight(Integer.parseInt(weight.substring(1)));
                else if (weight.startsWith("-"))
                    food.setAdditionalWeight(-1 * Integer.parseInt(weight.substring(1)));
            }

            if (grassItem.hasNBT(FoodTag.EXPIRE_DATE))
                food.setExpireDate(LocalDateTime.parse((String) grassItem.getNBT(FoodTag.EXPIRE_DATE).get()));

            if (grassItem.getNBT(FoodTag.ELEMENT) != null)
                ((Map<String, String>) grassItem.getNBT(FoodTag.ELEMENT).get())
                        .forEach((key, value) -> food.increaseAdditionalElement(FoodElement.valueOf(key), Integer.valueOf(value)));
        }

        return food;
    }

    public static Food makeCuisine(ItemStack item) {
        Food food = new Food(item, "Cuisine");
        return food;
    }

    public ItemStack getItem() {
        GrassItem grassItem = new GrassItem(item);
        grassItem.setNBT(UnityTag.JSON_NAME, jsonName);
        grassItem.setNBT(FoodTag.EXPIRE_DATE, expireDate.toString());
        grassItem.setNBT(FoodTag.RESTORE_AMOUNT, "+" + additionalRestoreAmount);
        grassItem.setNBT(FoodTag.WEIGHT, "+" + additionalWeight);
        grassItem.setNBT(FoodTag.ELEMENT, additionalElementMap);

        return grassItem.toItemStack();
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String getJsonName() {
        return jsonName;
    }

    public void setJsonName(String jsonName) {
        this.jsonName = jsonName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodType getType() {
        return type;
    }

    public void setType(FoodType type) {
        this.type = type;
    }

    public int getAdditionalWeight() {
        return additionalWeight;
    }

    public int getBaseWeight() {
        return baseWeight;
    }

    public int getTotalWeight() {
        return baseWeight + additionalWeight;
    }

    public int getBaseRestoreAmount() {
        return baseRestoreAmount;
    }

    public int getAdditionalRestoreAmount() {
        return additionalRestoreAmount;
    }

    public int getTotalRestoreAmount() {
        return baseRestoreAmount + additionalRestoreAmount;
    }

    public void setAdditionalWeight(int additionalWeight) {
        if (10 <= additionalWeight) {
            additionalWeight /= 10;
            additionalWeight *= 10;
            this.additionalWeight = additionalWeight;
        }
    }

    public void setAdditionalRestoreAmount(int additionalRestoreAmount) {
        this.additionalRestoreAmount = additionalRestoreAmount;
    }

    public Material getAfterMaterial() {
        return afterMaterial;
    }

    public void setAfterMaterial(Material afterMaterial) {
        this.afterMaterial = afterMaterial;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate.minusMinutes(expireDate.getMinute() % 10).truncatedTo(ChronoUnit.MINUTES);
    }

    public Map<FoodElement, Integer> getAdditionalElementMap() {
        return additionalElementMap;
    }

    public void setAdditionalElementMap(Map<FoodElement, Integer> additionalElementMap) {
        this.additionalElementMap = additionalElementMap;
    }

    public int getElementLevel(FoodElement element) {
        return this.additionalElementMap.getOrDefault(element, 0) + this.baseElementMap.getOrDefault(element, 0);
    }

    public void increaseAdditionalElement(FoodElement element, int level) {
        this.additionalElementMap.put(element, additionalElementMap.getOrDefault(element, 0) + level);
    }

    public void removeAdditionalElement(FoodElement element) {
        this.additionalElementMap.remove(element);
    }

    public Map<FoodElement, Integer> getTotalElementMap() {
        Map<FoodElement, Integer> totalElementMap = new HashMap<>();

        Arrays.stream(FoodElement.values()).forEach(element -> {
            totalElementMap.put(element, baseElementMap.getOrDefault(element, 0));
            totalElementMap.put(element, totalElementMap.getOrDefault(element, 0) + additionalElementMap.getOrDefault(element, 0));
        });

        return totalElementMap;
    }
}