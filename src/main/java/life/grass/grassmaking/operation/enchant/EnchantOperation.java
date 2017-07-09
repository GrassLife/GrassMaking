package life.grass.grassmaking.operation.enchant;

import life.grass.grassmaking.operation.ResultOperation;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.util.Vector;

public class EnchantOperation extends ResultOperation {

    // TODO: add particle effects

    public EnchantOperation(Block block) {
        super(block);
    }

    @Override
    protected void onOperate() {
        super.onOperate();

        Block block = getBlock();

        block.getWorld().spawnParticle(
                Particle.ENCHANTMENT_TABLE,
                block.getLocation().clone().add(0.5, 0.5, 0.5),
                15,
                0.5,
                0.5,
                0.5,
                0);
    }

    @Override
    public Location getDropLocation() {
        return getBlock().getLocation().clone().add(0, 1, 0);
    }

    @Override
    protected void onFinish() {
        setDropLocation(getBlock().getLocation().clone().add(0, 5, 0));
        super.onFinish();
    }
}
