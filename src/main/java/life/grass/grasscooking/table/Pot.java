package life.grass.grasscooking.table;

import life.grass.grasscooking.operation.Operation;
import life.grass.grasscooking.operation.VisualOperation;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Pot extends Cooker {
    private static final ItemStack PADDING_ICON_FENCE;
    private static final ItemStack PADDING_ICON_FIRE;
    private static final ItemStack MAKING_ICON;

    private VisualOperation operation;

    static {
        PADDING_ICON_FENCE = createIcon(Material.IRON_FENCE, 0, null, null);
        PADDING_ICON_FIRE = createIcon(Material.STAINED_GLASS_PANE, 14, null, null);
        MAKING_ICON = createIcon(Material.CAULDRON_ITEM, 0, ChatColor.RED + "調理する", null);
    }

    public Pot(Block block) {
        super(block);

        // TODO: change
        operation = new VisualOperation(block) {
            @Override
            protected void onOperate() {
                block.getWorld().spawnParticle(
                        Particle.SMOKE_LARGE,
                        block.getLocation().clone().add(0.5, 0.5, 0.5),
                        3,
                        0.25,
                        0.25,
                        0.25,
                        0);
            }

            @Override
            protected void onFinish() {
                block.getWorld().spawnParticle(
                        Particle.LAVA,
                        block.getLocation().clone().add(0.5, 0.5, 0.5),
                        8,
                        0,
                        0,
                        0,
                        0);
            }

            @Override
            protected ItemStack getVisualItem() {
                return createIcon(Material.RAW_BEEF, 0, "test", null);
            }
        };
    }

    @Override
    public String getTitle() {
        return ChatColor.DARK_RED + "鍋";
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
    public int getMakingIconPosition() {
        return 16;
    }

    @Override
    public List<Integer> getIngredientSpacePositionList() {
        return Arrays.asList(10, 11, 12, 13, 19, 20, 21, 22, 29, 30);
    }

    @Override
    public void onPressedMaking() {
        operation.start(5 * 4);
    }

    @Override
    public Operation getOperation() {
        return operation;
    }
}
