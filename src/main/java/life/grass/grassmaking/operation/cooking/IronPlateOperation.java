package life.grass.grassmaking.operation.cooking;

import org.bukkit.Particle;
import org.bukkit.block.Block;

public class IronPlateOperation extends CuttingOperation {

    public IronPlateOperation(Block block) {
        super(block);
    }

    @Override
    protected void onOperate() {
        super.onOperate();

        Block block = getBlock();

        block.getWorld().spawnParticle(
                Particle.SMOKE_LARGE,
                block.getLocation().clone().add(0.5, 0.5, 0.5),
                3,
                0.25,
                0.25,
                0.25,
                0);
    }
}
