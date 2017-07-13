package life.grass.grassmaking.table.cooking;

import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.cooking.CookingType;
import life.grass.grassmaking.operation.Operation;
import life.grass.grassmaking.operation.cooking.PotOperation;
import life.grass.grassmaking.ui.SlotPart;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Cauldron;

import java.util.Arrays;
import java.util.List;

public class Pot extends Cooker {
    private static final SlotPart MAKING_SLOT_PART = new SlotPart(false, false, MAKING_TAG, Material.CAULDRON_ITEM, 0, ChatColor.AQUA + "茹でる", null);
    private static final SlotPart FENCE_SLOT_PART = new SlotPart(false, false, null, Material.FENCE, 0, null, null);
    private static final SlotPart FIRE_SLOT_PART = new SlotPart(false, false, null, Material.STAINED_GLASS_PANE, 14, null, null);

    private PotOperation operation;

    public Pot(Block block) {
        super(block);
        operation = new PotOperation(block);

        Arrays.asList(9, 14, 18, 23, 28, 31, 38, 39).forEach(slot -> addSlotPart(slot, FENCE_SLOT_PART));
        Arrays.asList(47, 48).forEach(slot -> addSlotPart(slot, FIRE_SLOT_PART));
        addSlotPart(16, MAKING_SLOT_PART);
        addSlotPart(34, SEASONING_SLOT_PART);
        Arrays.asList(10, 11, 12, 13, 19, 20, 21, 22, 29, 30).forEach(slot -> addSlotPart(slot, INGREDIENT_SPACE_SLOT_PART));
        Arrays.asList(41, 42, 43).forEach(slot -> addSlotPart(slot, SEASONING_SPACE_SLOT_PART));
    }

    @Override
    public String namesCuisine(ItemStack mainIngredient, ItemStack accompaniment, ItemStack mainSeasoning) {
        return "茹でた" + JsonHandler.getGrassJson(mainIngredient).getDisplayName() + (accompaniment != null ? "と" + JsonHandler.getGrassJson(accompaniment).getDisplayName() : "");
    }

    @Override
    public ItemStack getExtendExpireDate(ItemStack item) {
        return JsonHandler.putExpireDateHours(item, 6);
    }


    @Override
    public int getMaxCuisineAmount() {
        return 32;
    }

    @Override
    protected CookingType getCookingType() {
        return CookingType.BOIL;
    }

    @Override
    protected boolean canCook(List<ItemStack> ingredientList, List<ItemStack> seasoningList) {
        return true;
    }

    @Override
    public String getTitle() {
        return ChatColor.DARK_RED + "鍋";
    }

    @Override
    public boolean canOpen(Block block) {
        if (block.getType() != Material.CAULDRON || !((Cauldron) block.getState().getData()).isFull()) return false;

        switch (block.getRelative(BlockFace.DOWN).getType()) {
            case STATIONARY_LAVA:
            case LAVA:
            case FIRE:
            case MAGMA:
            case BURNING_FURNACE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public int getCookingTick() {
        switch (getBlock().getRelative(BlockFace.DOWN).getType()) {
            case STATIONARY_LAVA:
            case BURNING_FURNACE:
            case FIRE:
                return 20 * 7;
            default:
                return 20 * 21;
        }
    }

    @Override
    public Operation getOperation() {
        return operation;
    }
}
