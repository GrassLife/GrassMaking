package life.grass.grassmaking.operation;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

public abstract class VisualOperation extends Operation {
    private ArmorStand armorStand;

    public VisualOperation(Block block) {
        super(block);
    }

    @Override
    protected void onStart() {
        World world = getBlock().getWorld();
        Location location = getBlock().getLocation().clone().add(0.5, -1.1, 0.5);

        armorStand = world.spawn(location, ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setCustomName("GMG_" + Double.toString(Math.random()));
        armorStand.setCustomNameVisible(false);
        armorStand.setSmall(true);
        armorStand.setHelmet(getVisualItem());
    }

    @Override
    protected void onEnd() {
        armorStand.remove();
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    protected abstract ItemStack getVisualItem();
}