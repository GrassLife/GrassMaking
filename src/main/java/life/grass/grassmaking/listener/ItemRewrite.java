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
                    lore.add(ChatColor.DARK_GRAY + "消費期限: " + localDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
                });
        if(grassJson.hasItemTag("Ingredient")) lore.add(ChatColor.DARK_GRAY + "調理可能アイテム");
        if(grassJson.hasItemTag("Seasoning")) lore.add(ChatColor.DARK_GRAY + "調味料");

        final String[] wc = {ChatColor.GRAY + ""};
        grassJson.getDynamicValue("Calorie").getAsMaskedInteger()
                .ifPresent(calorie -> {
                    if (calorie != 0) wc[0] += (calorie + "kcal");
                });
        grassJson.getDynamicValue("Weight").getAsMaskedInteger()
                .ifPresent(weight -> {
                    if (weight != 0) wc[0] += (" / " + weight + "g");
                });
        lore.add(wc[0]);

        Arrays.stream(FoodElement.values()).forEach(element ->
                grassJson.getDynamicValue("FoodElement/" + element.toString()).getAsMaskedInteger().ifPresent(value -> {
                    if (value != 0)
                        lore.add(ChatColor.GOLD + (0 < value ? element.getUprightName() : element.getReversedName()) + ": " + Math.abs(value));
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
