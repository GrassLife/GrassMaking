package net.unicroak.grasscooking.table;

import net.unicroak.grasscooking.ui.MakerInterface;
import org.bukkit.inventory.Inventory;

public abstract class Maker extends Table implements MakerInterface {

    @Override
    public Inventory getInventory() {
        Inventory inv = super.getInventory();

        getIngredientSpacePositionList().forEach(position -> inv.setItem(position, null));
        inv.setItem(getMakingIconPosition(), getMakingIcon());

        return inv;
    }
}