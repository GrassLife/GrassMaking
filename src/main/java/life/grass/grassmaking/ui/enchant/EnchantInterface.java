package life.grass.grassmaking.ui.enchant;

import life.grass.grassmaking.ui.MakerInterface;
import org.bukkit.inventory.ItemStack;

public interface EnchantInterface extends MakerInterface {

    ItemStack getRedstoneIcon();

    int getRedstoneIconPosition();

    int getRedstoneSpacePosition();

    ItemStack getGlowstoneIcon();

    int getGlowstoneIconPosition();

    int getGlowstoneSpacePosition();
}
