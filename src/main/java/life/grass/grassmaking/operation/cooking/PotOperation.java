package life.grass.grassmaking.operation.cooking;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

public class PotOperation extends CookingOperation {
    private ArmorStand armorStand;

    public PotOperation(Block block) {
        super(block);
    }

    @Override
    protected void onStart() {
        super.onStart();

        World world = getBlock().getWorld();
        Location location = getBlock().getLocation().clone().add(0.5, -0.2, 0.5);

        armorStand = world.spawn(location, ArmorStand.class);
        armorStand.setCustomName("GrassMaking_" + Double.toString(Math.random()));
        armorStand.setCustomNameVisible(false);
        armorStand.setVisible(false);
        armorStand.setSmall(true);
        armorStand.setGravity(false);
        armorStand.setHelmet(getResult());

        setCircleLocation(getCircleLocation().add(0, 1, 0));
        setDropLocation(getDropLocation().clone().add(0, 0.55, 0));
    }

    @Override
    protected void onOperate() {
        super.onOperate();

        Block block = getBlock();

        block.getWorld().spawnParticle(
                Particle.CLOUD,
                block.getLocation().clone().add(0.5, 1.25, 0.5),
                1,
                0.2,
                0.03,
                0.2,
                0);
    }

    @Override
    protected void onFinish() {
        super.onFinish();

        armorStand.remove();
    }
}
