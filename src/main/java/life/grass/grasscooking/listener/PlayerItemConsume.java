package life.grass.grasscooking.listener;

import life.grass.grassplayer.GrassPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerItemConsume implements Listener {

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        ItemStack item = event.getItem().clone();

        event.setCancelled(true);

        if (!true /* check it is not Food */) {
            return;
        }

        if (item.getAmount() < 2) {
            item.setType(Material.AIR);
        } else {
            item.setAmount(item.getAmount() - 1);
        }

        if (inv.getItemInMainHand().equals(event.getItem())) {
            inv.setItemInMainHand(item);
        } else if (inv.getItemInOffHand().equals(event.getItem())) {
            inv.setItemInOffHand(item);
        } else {
            return;
        }

        GrassPlayer grassPlayer = GrassPlayer.findOrCreate(event.getPlayer());

        // TODO: change
        grassPlayer.incrementEffectiveStamina(5);

        player.updateInventory();
    }
}
