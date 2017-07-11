package life.grass.grassmaking.table.enchant;

import life.grass.grassmaking.operation.enchant.BookBindingOperation;
import life.grass.grassmaking.operation.enchant.EnchantOperation;
import life.grass.grassmaking.table.Selector;
import life.grass.grassmaking.table.Table;
import life.grass.grassmaking.ui.SlotPart;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;

import java.util.Iterator;
import java.util.function.Consumer;

public class EnchantWindowSelector extends Selector {
    private static final SlotPart BINDING_BOOK_SLOT_PART = new SlotPart(false, SELECTING_TAG, Material.KNOWLEDGE_BOOK, 0, ChatColor.GREEN + "製本する", null);
    private static final SlotPart ENCHANT_SLOT_PART = new SlotPart(false, SELECTING_TAG, Material.ENCHANTED_BOOK, 0, ChatColor.BLUE + "エンチャントする", null);

    private Block block;

    public EnchantWindowSelector(Block block) {
        this.block = block;

        addSlotPart(11, BINDING_BOOK_SLOT_PART);
        addSlotPart(15, ENCHANT_SLOT_PART);
    }

    @Override
    public String getTitle() {
        return ChatColor.DARK_RED + "エンチャント台";
    }

    @Override
    public int getTableSize() {
        return 27;
    }

    @Override
    public void onPressSelecting(int slot) {
        Consumer<Table> openInventory = table -> {
            Iterator<HumanEntity> viewerIterator = this.getInventory().getViewers().iterator();
            while (viewerIterator.hasNext()) {
                HumanEntity viewer = viewerIterator.next();
                viewerIterator.remove();
                viewer.openInventory(table.getInventory());
            }
        };

        switch (slot) {
            case 11:
                openInventory.accept(new BookBindingTable(block, new BookBindingOperation(block)));
                break;
            case 15:
                openInventory.accept(new EnchantTable(block, new EnchantOperation(block)));
                break;
        }
    }
}
