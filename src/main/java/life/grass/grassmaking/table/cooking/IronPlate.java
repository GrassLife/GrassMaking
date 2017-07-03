package life.grass.grassmaking.table.cooking;

import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.cooking.CookingType;
import life.grass.grassmaking.operation.cooking.IronPlateOperation;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class IronPlate extends Cooker {
    private static final ItemStack PADDING_ICON_FENCE;
    private static final ItemStack PADDING_ICON_FIRE;
    private static final ItemStack MAKING_ICON;

    static {
        PADDING_ICON_FENCE = createIcon(Material.IRON_FENCE, 0, null, null);
        PADDING_ICON_FIRE = createIcon(Material.STAINED_GLASS_PANE, 14, null, null);
        MAKING_ICON = createIcon(Material.CAULDRON_ITEM, 0, ChatColor.RED + "調理する", null);
    }

    public IronPlate(Block block) {
        super(block, new IronPlateOperation(block));
    }

    @Override
    public String namesCuisine(ItemStack mainIngredient, ItemStack accompaniment, ItemStack mainSeasoning) {
        return "焼いた" + JsonHandler.getGrassJson(mainIngredient).getDisplayName() + (accompaniment != null ? "の" + JsonHandler.getGrassJson(accompaniment).getDisplayName() + "添え" : "");
    }

    @Override
    public ItemStack extendExpireDate(ItemStack item) {
        return JsonHandler.putExpireDateHours(item, 12);
    }

    @Override
    protected CookingType getCookingType() {
        return CookingType.GRILL;
    }

    @Override
    public Particle getCookingParticle() {
        return Particle.SMOKE_LARGE;
    }

    @Override
    protected boolean canCook(List<ItemStack> ingredientList, List<ItemStack> seasoningList) {
        return true;
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
        return 5 * 6;
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
