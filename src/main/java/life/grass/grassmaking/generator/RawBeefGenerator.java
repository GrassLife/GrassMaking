package life.grass.grassmaking.generator;

import life.grass.grassmaking.food.FoodElement;
import life.grass.grassmaking.food.Meat;
import life.grass.grassmaking.manager.Kitchen;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;

public class RawBeefGenerator extends Generator {

    @Override
    public ItemStack generate(ItemStack item) {
        Meat meat = (Meat) Kitchen.generateFoodFromItemStack(item);
        assert meat != null;
        meat.setRestoreAmount(5);
        meat.setFoodName("牛肉");
        meat.setWeight((int) (200 + (100 * Math.random() - 50)));
        meat.setAfterMaterial(Material.COOKED_BEEF);
        meat.setExpireDate(LocalDateTime.now().plusDays(1).plusHours(12));
        meat.increaseElement(FoodElement.UMAMI, (int) (Math.random() * 4) - 2);

        return meat.getItem();
    }
}
