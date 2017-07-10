package life.grass.grassmaking.ui.enchant;

import life.grass.grassmaking.ui.MakerInterface;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public interface EnchantInterface extends MakerInterface {

    @Override
    default List<Integer> getIngredientSpacePositionList() {
        return Arrays.asList(getRedstoneSpacePosition(), getGlowstoneSpacePosition(), getEnchantedBookSpacePosition(), getTargetSpacePosition());
    }

    ItemStack getRedstoneIcon();

    int getRedstoneIconPosition();

    int getRedstoneSpacePosition();

    ItemStack getGlowstoneIcon();

    int getGlowstoneIconPosition();

    int getGlowstoneSpacePosition();

    ItemStack getEnchantedBookIcon();

    int getEnchantedBookIconPosition();

    int getEnchantedBookSpacePosition();

    ItemStack getTargetIcon();

    int getTargetIconPosition();

    int getTargetSpacePosition();
}
