package life.grass.grassmaking.operation.cooking;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public abstract class VisualCookingOperation extends CookingOperation {
    private ArmorStand armorStand;

    public VisualCookingOperation(Block block) {
        super(block);
    }

    @Override
    protected void onStart() {
        super.onStart();

        World world = getBlock().getWorld();

        armorStand = world.spawn(getArmorStandLocation(), ArmorStand.class);
        armorStand.setCustomName("GrassMaking_" + Double.toString(Math.random()));
        armorStand.setCustomNameVisible(false);
        armorStand.setMarker(true);
        armorStand.setInvulnerable(true);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setItemInHand(getResult());
        armorStand.setRightArmPose(new EulerAngle(0, 1.0, 0));
    }

    @Override
    protected void onFinish() {
        super.onFinish();

        armorStand.remove();
    }

    protected abstract Location getArmorStandLocation();

    protected ArmorStand getArmorStand() {
        return armorStand;
    }

    protected void setArmorStand(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }
}
