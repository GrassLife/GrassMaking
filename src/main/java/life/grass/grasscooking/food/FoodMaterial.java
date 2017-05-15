package life.grass.grasscooking.food;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class FoodMaterial {
    protected static final String FOOD_TYPE_LORE
            = ChatColor.GREEN + "食材種" + ChatColor.GRAY + ": " + ChatColor.GREEN;
    protected static final String ELEMENT_PREFIX_LORE
            = ChatColor.DARK_GRAY + "  * " + ChatColor.BLUE;
    protected static final String ELEMENT_SEPARATOR_LORE
            = ChatColor.GRAY + ": " + ChatColor.BLUE;

    protected ItemStack item;
    protected FoodType foodType;
    protected Map<FoodElement, Integer> elementMap;
    protected int size;

    protected FoodMaterial(ItemStack item, FoodType foodType, Map<FoodElement, Integer> elementMap, int size) {
        this.item = item;
        this.foodType = foodType;
        this.elementMap = elementMap;
        this.size = size;

        updateItem();
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public Map<FoodElement, Integer> getElementMap() {
        return elementMap;
    }

    public int getSize() {
        return size;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setElementMap(Map<FoodElement, Integer> elementMap) {
        this.elementMap = elementMap;
        updateItem();
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
        updateItem();
    }

    public void setSize(int size) {
        this.size = size;
        updateItem();
    }

    protected void updateItem() {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(FOOD_TYPE_LORE + foodType.toString());
        elementMap.forEach((key, value) -> lore.add(ELEMENT_PREFIX_LORE + key + ELEMENT_SEPARATOR_LORE + value));
        meta.setLore(lore);

        item.setItemMeta(meta);
    }
}