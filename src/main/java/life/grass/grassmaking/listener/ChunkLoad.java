package life.grass.grassmaking.listener;

import life.grass.grassmaking.GrassMaking;
import org.bukkit.Chunk;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class ChunkLoad implements Listener {
    private GrassMaking instance;

    public ChunkLoad() {
        instance = GrassMaking.getInstance();
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();

        new BukkitRunnable() {
            @Override
            public void run() {
                Arrays.stream(chunk.getEntities())
                        .filter(entity -> entity instanceof ArmorStand
                                && (entity.getCustomName() != null ? entity.getCustomName() : "NONE").startsWith("GrassMaking_"))
                        .forEach(Entity::remove);
            }
        }.runTaskLater(instance, 1);
    }
}
