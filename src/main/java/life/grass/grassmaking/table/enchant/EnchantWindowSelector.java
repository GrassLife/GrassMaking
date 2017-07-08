package life.grass.grassmaking.table.enchant;

import life.grass.grassmaking.operation.enchant.BookBindingOperation;
import life.grass.grassmaking.operation.enchant.EnchantOperation;
import life.grass.grassmaking.table.Selector;
import life.grass.grassmaking.table.Table;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.function.Consumer;

public class EnchantWindowSelector extends Selector {
    private static final ItemStack BINDING_ICON;
    private static final ItemStack ENCHANTING_ICON;

    private Inventory inventory;
    private Block block;

    static {
        BINDING_ICON = createIcon(Material.KNOWLEDGE_BOOK, 0, ChatColor.GREEN + "製本する", null);
        ENCHANTING_ICON = createIcon(Material.ENCHANTED_BOOK, 0, ChatColor.BLUE + "エンチャントする", null);
    }

    public EnchantWindowSelector(Block block) {
        this.inventory = initInventory();
        this.block = block;
    }

    @Override
    public ItemStack getSelectedItem(int position) {
        switch (position) {
            case 11:
                return BINDING_ICON;
            case 15:
                return ENCHANTING_ICON;
        }

        return null;
    }

    @Override
    public void onPressSelectedItem(int position) {
        Consumer<Table> openInventory = table -> {
            Iterator<HumanEntity> viewerIterator = this.getInventory().getViewers().iterator();
            while (viewerIterator.hasNext()) {
                HumanEntity viewer = viewerIterator.next();
                viewerIterator.remove();
                viewer.openInventory(table.getInventory());
            }
        };

        switch (position) {
            case 11:
                openInventory.accept(new BookBindingTable(block, new BookBindingOperation(block)));
                break;
            case 15:
                openInventory.accept(new EnchantTable(block, new EnchantOperation(block)));
                break;
        }
    }

    @Override
    public String getTitle() {
        return ChatColor.DARK_RED + "エンチャント台";
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public int getTableSize() {
        return 27;
    }
}
