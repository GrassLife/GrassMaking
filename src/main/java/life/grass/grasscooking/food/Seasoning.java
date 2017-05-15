package life.grass.grasscooking.food;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Seasoning extends FoodMaterial {

    public Seasoning(ItemStack item, Map<FoodElement, Integer> elementMap) {
        super(item, elementMap);
    }

    public static Seasoning fromItemStack(ItemStack item) {
        // TODO: read and write NBT
        Map<FoodElement, Integer> elementMap = new HashMap<>();

        return new Seasoning(item, elementMap);
    }

    public static Seasoning fromItemStack(ItemStack item, Map<FoodElement, Integer> elementMap) {
        return new Seasoning(item, elementMap);
    }

    public static boolean verifySeasoning(ItemStack item) {
        // TODO: change
        return true;
    }
}
