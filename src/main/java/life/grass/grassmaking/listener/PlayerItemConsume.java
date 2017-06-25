package life.grass.grassmaking.listener;

import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.cooking.FoodEffect;
import life.grass.grassplayer.GrassPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.time.LocalDateTime;
import java.util.Arrays;

public class PlayerItemConsume implements Listener {
    private static final int CALORIE_PER_RESTORE_AMOUNT = 4;

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        ItemStack item = event.getItem().clone();
        GrassJson grassJson = JsonHandler.getGrassJson(item);

        event.setCancelled(true);

        if (!grassJson.hasItemTag("Cuisine")) return;

        if (item.getAmount() < 2) item.setType(Material.AIR);
        else item.setAmount(item.getAmount() - 1);

        if (inv.getItemInMainHand().equals(event.getItem())) {
            inv.setItemInMainHand(item);
        } else if (inv.getItemInOffHand().equals(event.getItem())) {
            inv.setItemInOffHand(item);
        } else {
            return;
        }

        GrassPlayer grassPlayer = GrassPlayer.findOrCreate(event.getPlayer());
        LocalDateTime expireDate = LocalDateTime.parse(grassJson.getDynamicValue("ExpireDate")
                .getAsOverwritedString()
                .orElse(LocalDateTime.now().minusSeconds(1).toString()));
        if (expireDate.isBefore(LocalDateTime.now())) {
            player.sendTitle("", ChatColor.DARK_GREEN + "腐っているようだ...", 3, 20 * 2, 3);
            grassPlayer.incrementEffectiveStamina(-10);
        } else {
            grassPlayer.incrementEffectiveStamina(grassJson.getDynamicValue("Calorie").getAsMaskedInteger().orElse(1) / CALORIE_PER_RESTORE_AMOUNT);
        }

        Arrays.stream(FoodEffect.values()).forEach(element -> {
            if (grassJson.hasDynamicValue("FoodEffect/" + element.toString())) {
                element.exert(player, grassJson.getDynamicValue("FoodEffect/" + element.toString()).getAsMaskedInteger().orElse(1));
            }
        });

        player.updateInventory();
    }
}
