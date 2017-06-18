package life.grass.grassmaking.food;

import life.grass.grassitem.GrassItemHandler;
import life.grass.grassitem.GrassJson;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Food {
    private ItemStack item;
    private GrassJson grassJson;
    private int restoreAmount;
    private int weight;
    private LocalDateTime expireDate;
    private Map<FoodElement, Integer> elementMap;

    private Food(ItemStack item) {
        this.item = item;

        String uniqueName = GrassItemHandler.findUniqueNameFromItemStack(item).orElseThrow(IllegalArgumentException::new);
        grassJson = GrassJson.findGrassJson(uniqueName, item).orElseThrow(IllegalArgumentException::new);

        weight = grassJson.getDynamicDataAsMaskedInteger("Weight").orElse(1);

        restoreAmount = grassJson.getDynamicDataAsMaskedInteger("RestoreAmount").orElse(1);

        expireDate = LocalDateTime.parse(grassJson.getDynamicDataAsString("ExpireDate").orElse(LocalDateTime.now().toString()));

        elementMap = new HashMap<>();
        Arrays.stream(FoodElement.values())
                .forEach(element -> elementMap.put(element, grassJson.getDynamicDataAsMaskedInteger("Element" + element.toString()).orElse(null)));
    }

    public static Optional<Food> findFood(ItemStack item) {
        String uniqueName = null;
        GrassJson grassJson = null;
        try {
            uniqueName = GrassItemHandler.findUniqueNameFromItemStack(item).orElse(null);
            grassJson = GrassJson.findGrassJson(uniqueName, item).orElse(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return uniqueName != null && grassJson != null && grassJson.hasItemTag("Food") ? Optional.of(new Food(item)) : Optional.empty();
    }

    public ItemStack getItem() {
        GrassItemHandler.putUniqueNameToItemStack(item, "Cuisine");
        GrassItemHandler.putDynamicDataToItemStack(item, "Weight", "+" + weight);
        GrassItemHandler.putDynamicDataToItemStack(item, "RestoreAmount", "+" + restoreAmount);
        GrassItemHandler.putDynamicDataToItemStack(item, "ExpireDate", expireDate.toString());
        elementMap.forEach((key, value) -> GrassItemHandler.putDynamicDataToItemStack(item, "Element" + key.toString(), String.valueOf(value)));

        return item;
    }

    public GrassJson getGrassJson() {
        return grassJson;
    }

    public Material getAfterMaterial() {
        return Material.getMaterial(grassJson.getStaticDataAsString("AfterMaterial").orElse("COOKED_BEEF"));
    }

    public int getWeight() {
        return weight;
    }

    public int getRestoreAmount() {
        return restoreAmount;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public Map<FoodElement, Integer> getElementMap() {
        return elementMap;
    }

    public boolean isUneatable() {
        return LocalDateTime.now().isAfter(expireDate);
    }

    public boolean isCuisine() {
        return grassJson.hasItemTag("Cuisine") && !isIngredient() && !isSeasoning();
    }

    public boolean isIngredient() {
        return isMeat() || isFish() || isVegetable();
    }

    public boolean isMeat() {
        return grassJson.hasItemTag("Meat");
    }

    public boolean isFish() {
        return grassJson.hasItemTag("Fish");
    }

    public boolean isVegetable() {
        return grassJson.hasItemTag("Vegetable");
    }

    public boolean isSeasoning() {
        return isSalt();
    }

    public boolean isSalt() {
        return grassJson.hasItemTag("Salt");
    }
}
