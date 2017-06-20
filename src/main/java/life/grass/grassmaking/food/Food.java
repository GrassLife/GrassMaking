package life.grass.grassmaking.food;

import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonHandler;
import life.grass.grassitem.JsonHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Food {
    protected ItemStack item;

    private String name;
    private int restoreAmount;
    private int weight;
    private LocalDateTime expireDate;
    private Map<FoodElement, Integer> elementMap;
    private Material afterMaterial;

    protected Food(ItemStack item) {
        this.item = item;

        GrassJson grassJson = JsonHandler.getGrassJson(item);
        this.name = grassJson.getDisplayName();
        this.restoreAmount = grassJson.getDynamicValue("RestoreAmount").getAsOverwritedInteger().orElse(1);
        this.weight = grassJson.getDynamicValue("Weight").getAsOverwritedInteger().orElse(10);
        this.expireDate = LocalDateTime.parse(grassJson.getDynamicValue("ExpireDate").getAsOverwritedString().orElse(LocalDateTime.now().toString()));
        this.elementMap = new HashMap<>();
        Arrays.stream(FoodElement.values()).forEach(element -> {
            String elementKey = "Element" + element.toString();
            grassJson.getDynamicValue(elementKey)
                    .getAsOverwritedInteger()
                    .ifPresent(value -> {
                        if (value != 0) this.elementMap.put(element, value);
                    });
        });
        this.afterMaterial = Material.valueOf(grassJson.getStaticValue("AfterMaterial").getAsOriginalString().orElse("BREAD"));
    }

    public static Optional<Food> makeFood(ItemStack item) {
        GrassJson grassJson = JsonHandler.getGrassJson(item);
        if (grassJson == null) return Optional.empty();

        if (grassJson.hasItemTag("Meat")) {
            return Optional.of(new Meat(item));
        } else if (grassJson.hasItemTag("Cuisine")) {
            return Optional.of(Cuisine.makeCuisine(item));
        }

        return Optional.empty();
    }

    public ItemStack getItem() {
        item = JsonHandler.putDynamicData(item, "RestoreAmount", restoreAmount < 0 ? "-" + Math.abs(restoreAmount) : "+" + restoreAmount);
        item = JsonHandler.putDynamicData(item, "Weight", weight < 0 ? "-" + Math.abs(weight) : "+" + weight);
        item = JsonHandler.putDynamicData(item, "ExpireDate", expireDate);
        elementMap.forEach((key, value) -> item = JsonHandler.putDynamicData(item, "Element" + key.toString(), value));

        return item;
    }

    public String getName() {
        return ChatColor.translateAlternateColorCodes('&', name);
    }

    public int getRestoreAmount() {
        return JsonHandler.getGrassJson(item).getDynamicValue("RestoreAmount").getAsOriginalInteger().orElse(0) + restoreAmount;
    }

    public int getWeight() {
        return JsonHandler.getGrassJson(item).getDynamicValue("Weight").getAsOriginalInteger().orElse(0) + weight;
    }

    public LocalDateTime getExpireDate() {
        return expireDate.minusMinutes(expireDate.getMinute() % 10).truncatedTo(ChronoUnit.MINUTES);
    }

    public Map<FoodElement, Integer> getElementMap() {
        return elementMap;
    }

    public int getElementLevel(FoodElement element) {
        return elementMap.getOrDefault(element, 0);
    }

    public Material getAfterMaterial() {
        return afterMaterial;
    }

    public void setAdditionalRestoreAmount(int restoreAmount) {
        this.restoreAmount = restoreAmount;
    }

    public void setAdditionalWeight(int weight) {
        this.weight = weight;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public void putElement(FoodElement element, int level) {
        this.elementMap.put(element, level);
    }

    public void removeElement(FoodElement element) {
        this.elementMap.remove(element);
    }
}
