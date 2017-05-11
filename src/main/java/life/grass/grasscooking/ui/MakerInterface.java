package life.grass.grasscooking.ui;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface MakerInterface extends TableInterface {

    ItemStack getMakingIcon();

    int getMakingIconPosition();

    List<Integer> getIngredientSpacePositionList();

    void onPressedMaking();
}
