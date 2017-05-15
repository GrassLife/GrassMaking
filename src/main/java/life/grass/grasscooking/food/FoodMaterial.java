package life.grass.grasscooking.food;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class FoodMaterial {
    protected static final String ELEMENT_PREFIX_LORE
            = ChatColor.DARK_GRAY + "  * " + ChatColor.BLUE;
    protected static final String ELEMENT_SEPARATOR_LORE
            = ChatColor.GRAY + ": " + ChatColor.BLUE;

    protected ItemStack item;
    protected Map<Element, Integer> elementMap;

    protected FoodMaterial(ItemStack item, Map<Element, Integer> elementMap) {
        this.item = item;
        this.elementMap = elementMap;

        updateItem();
    }

    public Map<Element, Integer> getElementMap() {
        return elementMap;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setElementMap(Map<Element, Integer> elementMap) {
        this.elementMap = elementMap;
        updateItem();
    }

    protected void updateItem() {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        elementMap.forEach((key, value) -> lore.add(ELEMENT_PREFIX_LORE + key + ELEMENT_SEPARATOR_LORE + value));
        meta.setLore(lore);

        item.setItemMeta(meta);
    }
}