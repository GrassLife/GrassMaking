package life.grass.grassmaking.table.cooking;

import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.cooking.CookingType;
import life.grass.grassmaking.operation.Operation;
import life.grass.grassmaking.operation.cooking.IronPlateOperation;
import life.grass.grassmaking.ui.SlotPart;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class IronPlate extends Cooker {
    private static final SlotPart MAKING_SLOT_PART = new SlotPart(false, MAKING_TAG, Material.STONE_PLATE, 0, ChatColor.RED + "焼く", null);
    private static final SlotPart FENCE_SLOT_PART = new SlotPart(false, null, Material.IRON_FENCE, 0, null, null);
    private static final SlotPart FIRE_SLOT_PART = new SlotPart(false, null, Material.STAINED_GLASS_PANE, 14, null, null);

    private IronPlateOperation operation;

    public IronPlate(Block block) {
        super(block);
        operation = new IronPlateOperation(block);

        Arrays.asList(37, 38, 39, 40, 41).forEach(slot -> addSlotPart(slot, FENCE_SLOT_PART));
        Arrays.asList(46, 47, 48, 49, 50).forEach(slot -> addSlotPart(slot, FIRE_SLOT_PART));
        addSlotPart(43, MAKING_SLOT_PART);
        addSlotPart(16, SEASONING_SLOT_PART);
        Arrays.asList(10, 11, 12, 13, 14, 19, 20, 21, 22, 23, 28, 29, 30, 31, 32).forEach(slot -> addSlotPart(slot, INGREDIENT_SPACE_SLOT_PART));
        addSlotPart(25, SEASONING_SPACE_SLOT_PART);
    }

    @Override
    public String namesCuisine(ItemStack mainIngredient, ItemStack accompaniment, ItemStack mainSeasoning) {
        return "焼いた" + JsonHandler.getGrassJson(mainIngredient).getDisplayName() + (accompaniment != null ? "の" + JsonHandler.getGrassJson(accompaniment).getDisplayName() + "添え" : "");
    }

    @Override
    public ItemStack getExtendExpireDate(ItemStack item) {
        return JsonHandler.putExpireDateHours(item, 12);
    }

    @Override
    protected CookingType getCookingType() {
        return CookingType.GRILL;
    }

    @Override
    protected boolean canCook(List<ItemStack> ingredientList, List<ItemStack> seasoningList) {
        return true;
    }

    @Override
    protected int getMaxCuisineAmount() {
        return 16;
    }

    @Override
    public String getTitle() {
        return ChatColor.DARK_RED + "鉄板";
    }

    @Override
    public boolean canOpen(Block block) {
        return block != null
                && block.getType() == Material.STONE_PLATE
                && (block.getRelative(BlockFace.DOWN).getType() == Material.MAGMA || block.getRelative(BlockFace.DOWN).getType() == Material.BURNING_FURNACE);
    }

    @Override
    public int getCookingTick() {
        switch (getBlock().getRelative(BlockFace.DOWN).getType()) {
            case BURNING_FURNACE:
                return 20 * 9;
            default:
                return 20 * 36;
        }
    }

    @Override
    public Operation getOperation() {
        return operation;
    }
}
