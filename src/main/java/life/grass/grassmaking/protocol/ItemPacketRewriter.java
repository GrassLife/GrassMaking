package life.grass.grassmaking.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import javafx.util.converter.LocalDateTimeStringConverter;
import life.grass.grassmaking.food.Food;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class ItemPacketRewriter {
    private static final String CUISINE_NAME_LORE;
    private static final String EXPIRE_DATE_LORE;
    private static final String RESTORE_AMOUNT_LORE;
    private static final String WEIGHT_LORE;

    private static ItemPacketRewriter instance;

    static {
        CUISINE_NAME_LORE = ChatColor.GRAY + "料理名: " + ChatColor.GOLD;
        EXPIRE_DATE_LORE = ChatColor.RED + "消費期限" + ChatColor.GRAY + ": " + ChatColor.RED;
        RESTORE_AMOUNT_LORE = ChatColor.AQUA + "スタミナ回復量" + ChatColor.GRAY + ": " + ChatColor.AQUA;
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
        if (item == null || !Food.makeFood(item).isPresent()) return;
        Food food = Food.makeFood(item).get();

        item.setType(food.getItem().getType());

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(food.getName());

        List<String> lore = meta.getLore();
        lore.addAll(Arrays.asList(
                EXPIRE_DATE_LORE + new LocalDateTimeStringConverter().toString(food.getExpireDate()),
                RESTORE_AMOUNT_LORE + food.getRestoreAmount(),
                WEIGHT_LORE + food.getWeight()
        ));

        if (!food.getElementMap().isEmpty()) lore.add(" ");
        food.getElementMap().forEach((key, value) -> {
                    if (value == 0) return;
                    String name = value > 0 ? key.getUprightName() : key.getReversedName();
                    String element = ChatColor.DARK_GRAY + " * " + ChatColor.YELLOW + name + ChatColor.GRAY + ": ";

                    ChatColor color;
                    for (int i = 1; i <= Math.abs(value); i++) {
                        switch (i) {
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
                                color = ChatColor.RED;
                                break;
                            default:
                                color = ChatColor.BLACK;
                        }
                        element += color + ChatColor.BOLD.toString() + "*";
                    }

                    lore.add(element);
                }
        );

        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}
