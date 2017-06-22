package life.grass.grassmaking.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import javafx.util.converter.LocalDateTimeStringConverter;
import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.cooking.FoodEffect;
import life.grass.grassmaking.cooking.FoodElement;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class ItemPacketRewriter {
    private static final String EXPIRE_DATE_LORE;
    private static final String RESTORE_AMOUNT_LORE;
    private static final String CALORIE_LORE;
    private static final String WEIGHT_LORE;

    private static ItemPacketRewriter instance;

    static {
        EXPIRE_DATE_LORE = ChatColor.RED + "消費期限" + ChatColor.GRAY + ": " + ChatColor.RED;
        RESTORE_AMOUNT_LORE = ChatColor.AQUA + "スタミナ回復量" + ChatColor.GRAY + ": " + ChatColor.AQUA;
        CALORIE_LORE = ChatColor.GOLD + "カロリー[kcal]" + ChatColor.GRAY + ": " + ChatColor.GOLD;
        WEIGHT_LORE = ChatColor.YELLOW + "重量[g]" + ChatColor.GRAY + ": " + ChatColor.YELLOW;

        instance = new ItemPacketRewriter();
    }

    private ItemPacketRewriter() {
    }

    public static ItemPacketRewriter getInstance() {
        return instance;
    }


    public void addListener(ProtocolManager manager, JavaPlugin plugin) {
        manager.addPacketListener(new PacketAdapter(plugin,
                ListenerPriority.NORMAL,
                PacketType.Play.Server.SET_SLOT,
                PacketType.Play.Server.WINDOW_ITEMS) {

            @Override
            public void onPacketSending(PacketEvent event) {
                PacketType type = event.getPacketType();
                if (type.equals(PacketType.Play.Server.SET_SLOT)) {
                    for (ItemStack item : new ItemStack[]{event.getPacket().getItemModifier().read(0)}) {
                        rewriteItem(item);
                    }
                } else if (type.equals(PacketType.Play.Server.WINDOW_ITEMS)) {
                    for (ItemStack item : event.getPacket().getItemListModifier().read(0)) {
                        rewriteItem(item);
                    }
                }
            }
        });
    }

    private void rewriteItem(ItemStack item) {
        GrassJson grassJson = JsonHandler.getGrassJson(item);
        if (grassJson == null || !(grassJson.hasItemTag("Cuisine") || grassJson.hasItemTag("Ingredient") || grassJson.hasItemTag("Seasoning")))
            return;

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(" ");

        if (grassJson.hasDynamicValue("ExpireDate")) {
            LocalDateTime expireDate = LocalDateTime.parse(grassJson.getDynamicValue("ExpireDate").getAsOverwritedString().orElse(LocalDateTime.now().toString()));
            expireDate = expireDate.minusMinutes(expireDate.getMinute() % 10).truncatedTo(ChronoUnit.MINUTES);
            lore.add(EXPIRE_DATE_LORE + new LocalDateTimeStringConverter().toString(expireDate));
        }

        if (grassJson.hasDynamicValue("Calorie")) {
            lore.add(CALORIE_LORE + grassJson.getDynamicValue("Calorie").getAsMaskedInteger().orElse(0));
        }

        if (grassJson.hasDynamicValue("Weight")) {
            lore.add(WEIGHT_LORE + grassJson.getDynamicValue("Weight").getAsMaskedInteger().orElse(0));
        }

        if (Arrays.stream(FoodElement.values()).anyMatch(element -> grassJson.getDynamicValue("FoodElement/" + element.toString()).getAsMaskedInteger().orElse(0) != 0)) {
            lore.add(" ");
        }
        Arrays.stream(FoodElement.values()).forEach(element -> {
            int value = grassJson.getDynamicValue("FoodElement/" + element.toString()).getAsMaskedInteger().orElse(0);
            if (value == 0) return;

            int absoluteValue = Math.abs(value);
            lore.add(ChatColor.DARK_GRAY + " * " + blendColor(absoluteValue) + (0 < absoluteValue ? element.getUprightName() : element.getReversedName()));
        });

        if (Arrays.stream(FoodEffect.values()).anyMatch(effect -> grassJson.getDynamicValue("FoodEffect/" + effect.toString()).getAsMaskedInteger().orElse(0) != 0)) {
            lore.add(" ");
        }
        Arrays.stream(FoodEffect.values()).forEach(effect -> {
            int value = grassJson.getDynamicValue("FoodEffect/" + effect.toString()).getAsMaskedInteger().orElse(0);
            if (value == 0) return;

            lore.add(ChatColor.GRAY + " * " + blendColor(value) + effect.getDisplayName());
        });

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    private ChatColor blendColor(int value) {
        ChatColor color;
        switch (value) {
            case 1:
                color = ChatColor.AQUA;
                break;
            case 2:
                color = ChatColor.GREEN;
                break;
            case 3:
                color = ChatColor.YELLOW;
                break;
            case 4:
                color = ChatColor.GOLD;
                break;
            case 5:
            default:
                color = ChatColor.RED;
                break;
        }

        return color;
    }
}
