package life.grass.grassmaking.generator;

import life.grass.grassmaking.food.FoodElement;
import life.grass.grassmaking.food.Meat;
import life.grass.grassmaking.manager.Kitchen;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;

public class RawBeefGenerator extends Generator {

    @Override
    public ItemStack generate(ItemStack item) {
        Meat meat = (Meat) Kitchen.generateFoodFromItemStack(item);
        assert meat != null;
        meat.setRestoreAmount(2);
        meat.setExpireDate(LocalDateTime.now().plusDays(3));
        meat.increaseElement(FoodElement.UMAMI, (int) (Math.random() * 4) - 2);

        return meat.getItem();
    }
}
