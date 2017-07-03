package life.grass.grassmaking.operation.cooking;

import org.bukkit.Particle;
import org.bukkit.block.Block;

public class ManaitaOperation extends CuttingOperation {

    public ManaitaOperation(Block block) {
        super(block);
    }

    @Override
    protected void onOperate() {
        super.onOperate();

        Block block = getBlock();

        if (Math.random() < 0.7) {
            block.getWorld().spawnParticle(
                    Particle.SWEEP_ATTACK,
                    block.getLocation().clone().add(0.5, 0.5, 0.5),
                    2,
                    0.2,
                    0,
                    0.2,
                    0);
        }
    }
}
