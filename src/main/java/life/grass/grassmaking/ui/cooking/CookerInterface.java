package life.grass.grassmaking.ui.cooking;

import life.grass.grassmaking.ui.MakerInterface;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface CookerInterface extends MakerInterface {

    ItemStack getSeasoningIcon();

    int getSeasoningIconPosition();

    List<Integer> getSeasoningSpacePositionList();
}
