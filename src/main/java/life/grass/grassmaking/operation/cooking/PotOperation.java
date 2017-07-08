package life.grass.grassmaking.operation.cooking;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

public class PotOperation extends VisualCookingOperation {

    public PotOperation(Block block) {
        super(block);

        increaseCircleHeight(1.1);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ArmorStand armorStand = getArmorStand();
        armorStand.setSmall(true);
        armorStand.setItemInHand(null);
        armorStand.setHelmet(getResult());
        setArmorStand(armorStand);

        setDropLocation(getDropLocation().clone().add(0, 0.55, 0));
    }

    @Override
    protected void onOperate() {
        super.onOperate();

        Block block = getBlock();

        if (Math.random() < 0.7) {
            block.getWorld().spawnParticle(
                    Particle.CLOUD,
                    block.getLocation().clone().add(0.5, 1.3, 0.5),
                    1,
                    0.2,
                    0.03,
                    0.2,
                    0);
        }
    }

    @Override
    protected void onFinish() {
        super.onFinish();
    }

    @Override
    protected Location getArmorStandLocation() {
        return getBlock().getLocation().clone().add(0.5, -0.2, 0.5);
    }
}
