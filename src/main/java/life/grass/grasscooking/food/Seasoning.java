package life.grass.grasscooking.food;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Seasoning extends FoodMaterial {

    public Seasoning(ItemStack item, FoodType foodType, Map<FoodElement, Integer> elementMap, int size) {
        super(item, foodType, elementMap, size);
    }

    public static Seasoning fromItemStack(ItemStack item) {
        // TODO: read and write NBT
        FoodType foodType = null;
        Map<FoodElement, Integer> elementMap = new HashMap<>();
        int size = 1;

        return new Seasoning(item, foodType, elementMap, size);
    }

    public static Seasoning fromItemStack(ItemStack item, FoodType foodType, Map<FoodElement, Integer> elementMap, int size) {
        return new Seasoning(item, foodType, elementMap, size);
    }

    public static boolean verifySeasoning(ItemStack item) {
        // TODO: change
        return true;
    }
}
