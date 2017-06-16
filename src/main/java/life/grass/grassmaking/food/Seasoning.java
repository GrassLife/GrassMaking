package life.grass.grassmaking.food;

import life.grass.grassmaking.operation.CookingOperation;
import life.grass.grassmaking.table.Cooker;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public abstract class Seasoning extends Food {
    protected static final String TASTED_NAME_COLOR_PREFIX;

    static {
        TASTED_NAME_COLOR_PREFIX = ChatColor.GRAY.toString();
    }

    public Seasoning(ItemStack item) {
        super(item);
    }

    public String createTastedName(CookingOperation operation) {
        Ingredient mainIngredient = operation.getMainIngredient();
        Seasoning mainSeasoning = operation.getMainSeasoning();
        Cooker cooker = operation.getCooker();
        return cooker.getCookingPrefix() + mainIngredient.getFoodName() + "と" + mainSeasoning.getFoodType();
    }
}
