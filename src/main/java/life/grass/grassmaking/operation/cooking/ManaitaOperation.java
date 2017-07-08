package life.grass.grassmaking.operation.cooking;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;

public class ManaitaOperation extends VisualCookingOperation {

    public ManaitaOperation(Block block) {
        super(block);

        increaseCircleHeight(0.2);
    }

    @Override
    protected Location getArmorStandLocation() {
        return getBlock().getLocation().clone().add(1, -0.73, 0.35);
    }

    @Override
    protected void onOperate() {
        super.onOperate();

        Block block = getBlock();

        if (Math.random() < 0.3) {
            block.getWorld().spawnParticle(
                    Particle.SWEEP_ATTACK,
                    block.getLocation().clone().add(0.5, 0.5, 0.5),
                    1,
                    0.2,
                    0,
                    0.2,
                    0);
        }
    }
}
