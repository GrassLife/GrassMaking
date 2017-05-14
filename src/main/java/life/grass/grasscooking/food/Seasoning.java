package life.grass.grasscooking.food;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Seasoning extends FoodMaterial {

    public Seasoning(ItemStack item, Map<Element, Integer> elementMap) {
        super(item, elementMap);
    }

    public static Seasoning fromItemStack(ItemStack item) {
        // TODO: read and write NBT
        Map<Element, Integer> elementMap = new HashMap<>();

        return new Seasoning(item, elementMap);
    }

    public static Seasoning fromItemStack(ItemStack item, Map<Element, Integer> elementMap) {
        return new Seasoning(item, elementMap);
    }
}
