package life.grass.grassmaking.operation.cooking;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;

public class IronPlateOperation extends VisualCookingOperation {

    public IronPlateOperation(Block block) {
        super(block);

        increaseCircleHeight(0.2);
    }

    @Override
    protected void onOperate() {
        super.onOperate();

        Block block = getBlock();

        block.getWorld().spawnParticle(
                Particle.SMOKE_LARGE,
                block.getLocation().clone().add(0.5, 0.5, 0.5),
                1,
                0.25,
                0.25,
                0.25,
                0);
    }

    protected Location getArmorStandLocation() {
        return getBlock().getLocation().clone().add(1, -0.73, 0.35);
    }
}
