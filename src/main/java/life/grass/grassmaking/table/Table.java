package life.grass.grassmaking.table;

import life.grass.grassmaking.ui.TableInterface;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public abstract class Table implements TableInterface {
    private static final ItemStack PADDING_ICON;

    static {
        PADDING_ICON = createIcon(Material.STAINED_GLASS_PANE, 15, null, null);
    }

    @Override
    public ItemStack getPaddingIcon(int position) {
        return PADDING_ICON;
    }

    protected static ItemStack createIcon(Material material, int data, String name, List<String> lore) {
        ItemStack icon = new ItemStack(material);
        ItemMeta meta = icon.getItemMeta();

        meta.setDisplayName(name != null ? name : " ");
        meta.setLore(lore != null ? lore : Collections.emptyList());

        icon.setDurability((short) data);
        icon.setItemMeta(meta);

        return icon;
    }

    protected Inventory initInventory() {
        Inventory inv = Bukkit.createInventory(this, TABLE_SIZE, getTitle());
        for (int i = 0; i < TABLE_SIZE; i++) inv.setItem(i, getPaddingIcon(i));

        return inv;
    }
}
