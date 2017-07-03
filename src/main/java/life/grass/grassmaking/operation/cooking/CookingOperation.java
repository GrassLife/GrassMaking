package life.grass.grassmaking.operation.cooking;

import life.grass.grassmaking.GrassMaking;
import life.grass.grassmaking.operation.ResultOperation;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class CookingOperation extends ResultOperation {

    public CookingOperation(Block block) {
        super(block);
    }

    @Override
    protected void onFinish() {
        super.onFinish();

        new BukkitRunnable() {
            Location center = getBlock().getLocation().clone().add(0.5, 0.2, 0.5);
            double radius = 1.2;
            int space = 18;
            int count = 1;

            @Override
            public void run() {
                double rad = Math.toRadians(count);
                double x = radius * Math.cos(rad);
                double z = radius * Math.sin(rad);

                center.add(x, 0, z);
                center.getWorld().spawnParticle(Particle.END_ROD, center, 1, 0, 0, 0, 0);
                center.subtract(2.0 * x, 0, 2.0 * z);
                center.getWorld().spawnParticle(Particle.END_ROD, center, 1, 0, 0, 0, 0);
                center.add(x, 0, z);

                if (360 < count) {
                    cancel();
                }
                count += space;
            }
        }.runTaskTimer(GrassMaking.getInstance(), 0, 1);
    }
}
