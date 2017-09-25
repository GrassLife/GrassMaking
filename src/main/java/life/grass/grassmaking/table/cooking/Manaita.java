package life.grass.grassmaking.table.cooking;

import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.cooking.CookingType;
import life.grass.grassmaking.operation.Operation;
import life.grass.grassmaking.operation.cooking.ManaitaOperation;
import life.grass.grassmaking.ui.SlotPart;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Manaita extends Cooker {
    private static final SlotPart MAKING_SLOT_PART = new SlotPart(false, false, MAKING_TAG, Material.WOOD_PLATE, 0, ChatColor.YELLOW + "切る", null);

    private Operation operation;

    public Manaita(Block block) {
        super(block);
        this.operation = new ManaitaOperation(block);

        addSlotPart(37, MAKING_SLOT_PART);
        addSlotPart(10, SEASONING_SLOT_PART);
        Arrays.asList(21, 22, 23, 24, 25, 30, 31, 32, 33, 34, 39, 40, 41, 42, 43).forEach(slot -> addSlotPart(slot, INGREDIENT_SPACE_SLOT_PART));
        addSlotPart(19, SEASONING_SPACE_SLOT_PART);
    }

    @Override
    public String namesCuisine(ItemStack mainIngredient, ItemStack accompaniment, ItemStack mainSeasoning) {
        return "切った" + JsonHandler.getGrassJson(mainIngredient).getDisplayName() + (accompaniment != null ? "と" + JsonHandler.getGrassJson(accompaniment).getDisplayName() : "");
    }

    @Override
    public ItemStack getExtendExpireDate(ItemStack item) {
        return JsonHandler.putExpireDateHours(item, 24);
    }

    @Override
    public int getMaxCuisineAmount() {
        return 64;
    }

    @Override
    protected CookingType getCookingType() {
        return CookingType.CUTTING;
    }

    @Override
    protected boolean canCook(List<ItemStack> ingredientList, List<ItemStack> seasoningList) {
        return true;
    }

    @Override
    public String getTitle() {
        return ChatColor.GOLD + "まな板";
    }

    @Override
    public boolean canOpen(Block block) {
        return block != null
                && block.getType() == Material.WOOD_PLATE
                && (block.getRelative(BlockFace.DOWN).getType() == Material.LOG || block.getRelative(BlockFace.DOWN).getType() == Material.LOG_2);
    }

    @Override
    public int getCookingTick() {
        return 20 * 4;
    }

    @Override
    public Operation getOperation() {
        return operation;
    }
}
