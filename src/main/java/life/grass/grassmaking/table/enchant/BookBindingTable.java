package life.grass.grassmaking.table.enchant;

import life.grass.grassmaking.event.GrassBookBindEvent;
import life.grass.grassmaking.operation.Operation;
import life.grass.grassmaking.operation.ResultOperation;
import life.grass.grassmaking.table.Maker;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class BookBindingTable extends Maker {
    private static final ItemStack MAKING_ICON;

    private ResultOperation operation;

    static {
        MAKING_ICON = createIcon(Material.BOOK_AND_QUILL, 0, ChatColor.GREEN + "製本する", null);
    }

    public BookBindingTable(Block block, ResultOperation resultOperation) {
        super(block);

        this.operation = resultOperation;
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
        // TODO: something...
    }

    @Override
    public String getTitle() {
        return ChatColor.DARK_GREEN + "製本";
    }

    @Override
    public boolean canOpen(Block block) {
        return true;
    }
}
