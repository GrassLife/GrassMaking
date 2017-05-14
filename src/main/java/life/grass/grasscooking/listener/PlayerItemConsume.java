package life.grass.grasscooking.listener;

import life.grass.grassplayer.GrassPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerItemConsume implements Listener {

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();

        if (!true /* check it is not Food*/) {
            event.setCancelled(true);
            return;
        }

        GrassPlayer grassPlayer = GrassPlayer.findOrCreate(event.getPlayer());

        // TODO: change
        grassPlayer.incrementStamina(5);
        grassPlayer.incrementEffectiveStamina(5);
    }
}
