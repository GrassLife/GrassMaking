package life.grass.grassmaking.ui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface SelectorInterface extends TableInterface {

    ItemStack getSelectedItem(int position);

    void onPressSelectedItem(Player player, int position);
}
