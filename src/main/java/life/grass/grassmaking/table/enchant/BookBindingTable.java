package life.grass.grassmaking.table.enchant;

import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonBucket;
import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.event.GrassBookBindEvent;
import life.grass.grassmaking.operation.Operation;
import life.grass.grassmaking.operation.enchant.BookBindingOperation;
import life.grass.grassmaking.table.Maker;
import life.grass.grassmaking.ui.enchant.BookBindingInterface;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class BookBindingTable extends Maker implements BookBindingInterface {
    private static final ItemStack MAKING_ICON;
    private static final ItemStack LEATHER_ICON;

    private BookBindingOperation operation;

    static {
        MAKING_ICON = createIcon(Material.BOOK_AND_QUILL, 0, ChatColor.GREEN + "製本する", null);
        LEATHER_ICON = createIcon(Material.LEATHER, 0, ChatColor.GOLD + "皮入れ", null);
    }

    public BookBindingTable(Block block, BookBindingOperation operation) {
        super(block);

        this.operation = operation;
    }

    @Override
    public Operation getOperation() {
        return operation;
    }

    @Override
    public ItemStack getMakingIcon() {
        return MAKING_ICON;
    }

    @Override
    public int getMakingIconPosition() {
        return 15;
    }

    @Override
    public List<Integer> getIngredientSpacePositionList() {
        return Arrays.asList(29, 30, 31, 32, 33, 38, 39, 40, 41, 42);
    }

    @Override
    public void onPressMaking() {
        ItemStack leather = getInventory().getItem(getLeatherSpacePosition());
        if (leather == null || !leather.getType().equals(Material.LEATHER)) return;
        Map<String, Integer> map = new HashMap<>();
        List<ItemStack> pages = getIngredientSpacePositionList().stream().map(position -> getInventory().getItem(position))
                .filter(item -> {
                    GrassJson json = JsonHandler.getGrassJson(item);
                    if (item == null || json == null || json.hasDynamicValue("EnchantPower")) return false;
                    map.put(json.getDynamicValue("CustomName").getAsMaskedString().orElse(""), 0);
                    return true;
                }).collect(Collectors.toList());
        double sum = pages.stream()
                .collect(Collectors.summingDouble(page -> {
                    GrassJson json = JsonHandler.getGrassJson(page);
                    return json.getDynamicValue("EnchantPower").getAsMaskedDouble().orElse(0.0);
                }));
        int level = (int) (sum / (11.0 - (double) map.size()));


//        GrassBookBindEvent event = new GrassBookBindEvent();
//        Bukkit.getServer().getPluginManager().callEvent(event);

        ItemStack result = JsonHandler.getEnchantBook(JsonBucket.getInstance().determineEnchant(Math.max(level, 1)), getInventory().getViewers().stream().findFirst().orElse(null));
        if (result != null) {
            pages.stream().forEach(page -> page.setAmount(page.getAmount() - 1));
            leather.setAmount(leather.getAmount() - 1);
            operation.setResult(result);
            operation.start(20 * 4 /* seconds */);
        }
        Iterator<HumanEntity> viewerIterator = this.getInventory().getViewers().iterator();
        while (viewerIterator.hasNext()) {
            HumanEntity viewer = viewerIterator.next();
            viewerIterator.remove();
            viewer.closeInventory();
        }
    }

    @Override
    public Inventory initInventory() {
        Inventory inventory = super.initInventory();

        inventory.setItem(getLeatherIconPosition(), getLeatherIcon());
        inventory.setItem(getLeatherSpacePosition(), null);

        return inventory;
    }

    @Override
    public String getTitle() {
        return ChatColor.DARK_GREEN + "製本";
    }

    @Override
    public boolean canOpen(Block block) {
        return true;
    }

    @Override
    public ItemStack getLeatherIcon() {
        return LEATHER_ICON;
    }

    @Override
    public int getLeatherIconPosition() {
        return 11;
    }

    @Override
    public int getLeatherSpacePosition() {
        return 12;
    }
}
