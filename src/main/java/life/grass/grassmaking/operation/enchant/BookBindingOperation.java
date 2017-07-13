package life.grass.grassmaking.operation.enchant;

import life.grass.grassmaking.operation.Operation;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;

public class BookBindingOperation extends Operation {

    public BookBindingOperation(Block block) {
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
        setDropLocation(getBlock().getLocation().clone().add(0, 2, 0));
        super.onFinish();
    }
}
