package life.grass.grassmaking.ui.enchant;

import life.grass.grassmaking.ui.MakerInterface;
import org.bukkit.inventory.ItemStack;

public interface BookBindingInterface extends MakerInterface {

    ItemStack getLeatherIcon();

    int getLeatherIconPosition();

    int getLeatherSpacePosition();
}
