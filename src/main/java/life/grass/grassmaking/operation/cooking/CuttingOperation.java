package life.grass.grassmaking.operation.cooking;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public class CuttingOperation extends CookingOperation {
    private ArmorStand armorStand;

    public CuttingOperation(Block block) {
        super(block);
    }

    @Override
    protected void onStart() {
        World world = getBlock().getWorld();
        Location location = getBlock().getLocation().clone().add(1, -0.73, 0.35);

        armorStand = world.spawn(location, ArmorStand.class);
        armorStand.setCustomName("GrassMaking_" + Double.toString(Math.random()));
        armorStand.setCustomNameVisible(false);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setItemInHand(getResult());
        armorStand.setRightArmPose(new EulerAngle(0, 1.0, 0));
    }

    @Override
    protected void onFinish() {
        armorStand.remove();
    }
}
