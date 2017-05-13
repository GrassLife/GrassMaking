package life.grass.grasscooking.listener;

import life.grass.grasscooking.GrassCooking;
import org.bukkit.Chunk;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class ChunkLoad implements Listener {
    private GrassCooking instance;

    public ChunkLoad() {
        instance = GrassCooking.getInstance();
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();

        new BukkitRunnable() {
            @Override
            public void run() {
                Arrays.stream(chunk.getEntities())
                        .filter(entity -> entity instanceof ArmorStand
                                && (entity.getCustomName() != null ? entity.getCustomName() : "NONE").startsWith("GSC_"))
                        .forEach(Entity::remove);
            }
        }.runTaskLater(instance, 1);
    }
}
