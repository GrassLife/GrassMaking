package life.grass.grassmaking.listener;

import life.grass.grassmaking.GrassMaking;
import life.grass.grassmaking.generator.Generator;
import org.bukkit.entity.Creature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityDeath implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Creature)) return;

        List<ItemStack> dropList = new ArrayList<>();
        event.getDrops().forEach(drop -> {
            Optional<Generator> generatorOptional = GrassMaking.getGeneratorManager().findGenerator(drop.getType());
            dropList.add(generatorOptional.map(generator -> generator.generate(drop)).orElse(drop));
        });

        event.getDrops().clear();
        event.getDrops().addAll(dropList);
    }
}
