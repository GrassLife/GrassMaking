package life.grass.grassmaking.operation;

import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CookingOperation extends VisualOperation {
    private ItemStack cuisine;

    public CookingOperation(Block block) {
        super(block);
    }

    @Override
    protected void onOperate() {
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

    @Override
    protected void onFinish() {
        Block block = getBlock();
        World world = block.getWorld();

        world.spawnParticle(
                Particle.LAVA,
                block.getLocation().clone().add(0.5, 0.5, 0.5),
                8,
                0,
                0,
                0,
                0);

        Item drop = world.dropItem(block.getLocation().clone().add(0.5D, 0.1D, 0.5D), cuisine);
        drop.setVelocity(new Vector(Math.random(), 8, Math.random()).multiply(0.03));
    }

    @Override
    protected ItemStack getVisualItem() {
        return cuisine;
    }

    public void setCuisine(ItemStack cuisine) {
        this.cuisine = cuisine;
    }
}
