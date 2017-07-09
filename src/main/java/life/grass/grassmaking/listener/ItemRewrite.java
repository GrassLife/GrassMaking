package life.grass.grassmaking.listener;

import life.grass.grassitem.GrassJson;
import life.grass.grassitem.events.ItemRewriteEvent;
import life.grass.grassitem.events.RewriteType;
import life.grass.grassmaking.cooking.FoodEffect;
import life.grass.grassmaking.cooking.FoodElement;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ItemRewrite implements Listener {

    @EventHandler
    public void onItemRewrite(ItemRewriteEvent event) {
        GrassJson grassJson = event.getJson();
        List<String> lore = event.getLore();

        if (grassJson == null || lore == null || event.getType() != RewriteType.COOKING) return;

        grassJson.getDynamicValue("ExpireDate").getAsOverwritedString()
                .ifPresent(expireDate -> {
                    LocalDateTime localDateTime = LocalDateTime.parse(expireDate);
                    lore.add(ChatColor.GRAY + "消費期限: " + localDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
                });
        grassJson.getDynamicValue("Weight").getAsMaskedInteger()
                .ifPresent(weight -> {
                    if (weight != 0) lore.add(ChatColor.GRAY + "重さ: " + weight + "g");
                });
        grassJson.getDynamicValue("Calorie").getAsMaskedInteger()
                .ifPresent(calorie -> {
                    if (calorie != 0) lore.add(ChatColor.GRAY + "カロリー: " + calorie + "kcal");
                });

        Arrays.stream(FoodElement.values()).forEach(element ->
                grassJson.getDynamicValue("FoodElement/" + element.toString()).getAsMaskedInteger().ifPresent(value -> {
                    if (value != 0)
                        lore.add(ChatColor.GRAY + (0 < value ? element.getUprightName() : element.getReversedName()) + ": " + Math.abs(value));
                })
        );

        Arrays.stream(FoodEffect.values()).forEach(effect ->
                grassJson.getDynamicValue("FoodEffect/" + effect.toString()).getAsOverwritedInteger().ifPresent(value -> {
                    if (value != 0)
                        lore.add(ChatColor.BLUE + effect.getDisplayName() + " Lv" + Math.abs(value));
                })
        );

        event.setLore(lore);
        event.setShowable(true);
    }
}
