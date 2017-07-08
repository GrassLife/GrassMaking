package life.grass.grassmaking.table.cooking;

import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.cooking.CookingType;
import life.grass.grassmaking.operation.cooking.PotOperation;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Cauldron;

import java.util.Arrays;
import java.util.List;

public class Pot extends Cooker {
    private static final ItemStack PADDING_ICON_FENCE;
    private static final ItemStack PADDING_ICON_FIRE;
    private static final ItemStack MAKING_ICON;

    static {
        PADDING_ICON_FENCE = createIcon(Material.IRON_FENCE, 0, null, null);
        PADDING_ICON_FIRE = createIcon(Material.STAINED_GLASS_PANE, 14, null, null);
        MAKING_ICON = createIcon(Material.CAULDRON_ITEM, 0, ChatColor.AQUA + "茹でる", null);
    }

    public Pot(Block block) {
        super(block, new PotOperation(block));
    }

    @Override
    public String namesCuisine(ItemStack mainIngredient, ItemStack accompaniment, ItemStack mainSeasoning) {
        return "茹でた" + JsonHandler.getGrassJson(mainIngredient).getDisplayName() + (accompaniment != null ? "と" + JsonHandler.getGrassJson(accompaniment).getDisplayName() : "");
    }

    @Override
    public ItemStack extendExpireDate(ItemStack item) {
        return JsonHandler.putExpireDateHours(item, 6);
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
        return block != null
                && block.getType() == Material.CAULDRON
                && ((Cauldron) block.getState().getData()).isFull()
                && (block.getRelative(BlockFace.DOWN).getType() == Material.MAGMA || block.getRelative(BlockFace.DOWN).getType() == Material.BURNING_FURNACE);
    }

    @Override
    public ItemStack getPaddingIcon(int position) {
        ItemStack icon = super.getPaddingIcon(position);

        switch (position) {
            case 9:
            case 14:
            case 18:
            case 23:
            case 28:
            case 31:
            case 38:
            case 39:
                icon = PADDING_ICON_FENCE;
                break;
            case 47:
            case 48:
                icon = PADDING_ICON_FIRE;
                break;
        }

        return icon;
    }

    @Override
    public ItemStack getMakingIcon() {
        return MAKING_ICON;
    }

    @Override
    public int getSeasoningIconPosition() {
        return 34;
    }

    @Override
    public List<Integer> getSeasoningSpacePositionList() {
        return Arrays.asList(41, 42, 43);
    }

    @Override
    public int getCookingTick() {
        return 20 * 7;
    }

    @Override
    public int getMakingIconPosition() {
        return 16;
    }

    @Override
    public List<Integer> getIngredientSpacePositionList() {
        return Arrays.asList(10, 11, 12, 13, 19, 20, 21, 22, 29, 30);
    }
}
