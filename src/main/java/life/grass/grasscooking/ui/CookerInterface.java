package life.grass.grasscooking.ui;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface CookerInterface extends MakerInterface {

    ItemStack getSeasoningIcon();

    int getSeasoningIconPosition();

    List<Integer> getSeasoningSpacePositionList();
}
