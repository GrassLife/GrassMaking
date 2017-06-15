package life.grass.grassmaking.listener;

import life.grass.grassitem.GrassItem;
import life.grass.grassmaking.tag.CookingTag;
import life.grass.grassplayer.GrassPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.time.LocalDateTime;

public class PlayerItemConsume implements Listener {

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        ItemStack item = event.getItem().clone();
        GrassItem grassItem = new GrassItem(item);

        event.setCancelled(true);

        if (!grassItem.hasNBT(CookingTag.RESTORE_AMOUNT)) return;

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
        if (LocalDateTime.parse((String) grassItem.getNBT(CookingTag.EXPIRE_DATE).get()).isBefore(LocalDateTime.now())) {
            grassPlayer.incrementEffectiveStamina(-10);
        } else {
            grassPlayer.incrementEffectiveStamina((int) grassItem.getNBT(CookingTag.RESTORE_AMOUNT).get());
        }
        player.updateInventory();
    }
}
