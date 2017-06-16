package life.grass.grassmaking.food;

import life.grass.grassmaking.operation.CookingOperation;
import life.grass.grassmaking.table.Cooker;
import life.grass.grassmaking.table.Pot;
import org.bukkit.inventory.ItemStack;

public class Salt extends Seasoning {

    public Salt(ItemStack item) {
        super(item);
        setFoodType(FoodType.SEASONING_SALT);
    }

    @Override
    public String createTastedName(CookingOperation operation) {
        Ingredient mainIngredient = operation.getMainIngredient();
        Cooker cooker = operation.getCooker();
        String tastedName = super.createTastedName(operation);

        if (cooker instanceof Pot) {
            tastedName = TASTED_NAME_COLOR_PREFIX + mainIngredient.getFoodName() + "の塩焼き";
        }
        return tastedName;
    }
}
