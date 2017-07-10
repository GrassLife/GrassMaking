package life.grass.grassmaking.table.enchant;

import life.grass.grassmaking.event.GrassBookBindEvent;
import life.grass.grassmaking.operation.Operation;
import life.grass.grassmaking.operation.enchant.BookBindingOperation;
import life.grass.grassmaking.table.Maker;
import life.grass.grassmaking.ui.enchant.BookBindingInterface;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class BookBindingTable extends Maker implements BookBindingInterface {
    private static final ItemStack MAKING_ICON;
    private static final ItemStack LEATHER_ICON;

    private BookBindingOperation operation;

    static {
        MAKING_ICON = createIcon(Material.BOOK_AND_QUILL, 0, ChatColor.GREEN + "製本する", null);
        LEATHER_ICON = createIcon(Material.LEATHER, 0, ChatColor.GOLD + "皮入れ", null);
    }

    public BookBindingTable(Block block, BookBindingOperation operation) {
        super(block);

        this.operation = operation;
    }

    @Override
    public Operation getOperation() {
        return operation;
    }

    @Override
    public ItemStack getMakingIcon() {
        return MAKING_ICON;
    }

    @Override
    public int getMakingIconPosition() {
        return 15;
    }

    @Override
    public List<Integer> getIngredientSpacePositionList() {
        return Arrays.asList(29, 30, 31, 32, 33, 38, 39, 40, 41, 42);
    }

    @Override
    public void onPressMaking() {
        GrassBookBindEvent event = new GrassBookBindEvent();
        Bukkit.getServer().getPluginManager().callEvent(event);

        ItemStack result = operation.getResult();
        if (result != null) {
            operation.setResult(result);
            operation.start(5 * 16 /* seconds */);
        }
    }

    @Override
    public Inventory initInventory() {
        Inventory inventory = super.initInventory();

        inventory.setItem(getLeatherIconPosition(), getLeatherIcon());
        inventory.setItem(getLeatherSpacePosition(), null);

        return inventory;
    }

    @Override
    public String getTitle() {
        return ChatColor.DARK_GREEN + "製本";
    }

    @Override
    public boolean canOpen(Block block) {
        return true;
    }

    @Override
    public ItemStack getLeatherIcon() {
        return LEATHER_ICON;
    }

    @Override
    public int getLeatherIconPosition() {
        return 11;
    }

    @Override
    public int getLeatherSpacePosition() {
        return 12;
    }
}
