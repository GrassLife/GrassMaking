package life.grass.grassmaking.table.cooking;

import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.cooking.CookingType;
import life.grass.grassmaking.table.cooking.Cooker;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Manaita extends Cooker {
    private static final ItemStack MAKING_ICON;


    static {
        MAKING_ICON = createIcon(Material.WOOD_PLATE, 0, ChatColor.YELLOW + "切る", null);
    }

    public Manaita(Block block) {
        super(block);
    }

    @Override
    public ItemStack getMakingIcon() {
        return MAKING_ICON;
    }

    @Override
    public int getMakingIconPosition() {
        return 37;
    }

    @Override
    public List<Integer> getIngredientSpacePositionList() {
        return Arrays.asList(21, 22, 23, 24, 25, 30, 31, 32, 33, 34, 39, 40, 41, 42, 43);
    }

    @Override
    public int getSeasoningIconPosition() {
        return 10;
    }

    @Override
    public List<Integer> getSeasoningSpacePositionList() {
        return Collections.singletonList(19);
    }

    @Override
    public String namesCuisine(ItemStack mainIngredient, ItemStack mainSeasoning) {
        return "切った" + JsonHandler.getGrassJson(mainIngredient).getDisplayName();
    }

    @Override
    public Particle getCookingParticle() {
        return Particle.SWEEP_ATTACK;
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
                && block.getRelative(BlockFace.DOWN).getType() == Material.LOG;
    }
}
