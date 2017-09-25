package life.grass.grassmaking.table.enchant;

import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonBucket;
import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.operation.Operation;
import life.grass.grassmaking.operation.enchant.BookBindingOperation;
import life.grass.grassmaking.table.MakingTable;
import life.grass.grassmaking.ui.SlotPart;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class BookBindingTable extends MakingTable {
    private static final String PAGE_TAG = "Page";
    private static final String LEATHER_TAG = "Leather";
    private static final SlotPart MAKING_SLOT_PART = new SlotPart(false, false, MAKING_TAG, Material.BOOK_AND_QUILL, 0, ChatColor.GREEN + "製本する", null);
    private static final SlotPart LEATHER_SLOT_PART = new SlotPart(false, false, null, Material.LEATHER, 0, ChatColor.GOLD + "皮入れ", null);
    private static final SlotPart LEATHER_SPACE_SLOT_PART = new SlotPart(true, true, LEATHER_TAG);
    private static final SlotPart PAGE_SPACE_SLOT_PART = new SlotPart(true, true, PAGE_TAG);

    private BookBindingOperation operation;

    public BookBindingTable(Block block, BookBindingOperation operation) {
        super(block);
        this.operation = operation;

        addSlotPart(11, LEATHER_SLOT_PART);
        addSlotPart(12, LEATHER_SPACE_SLOT_PART);
        Arrays.asList(29, 30, 31, 32, 33, 38, 39, 40, 41, 42).forEach(slot -> addSlotPart(slot, PAGE_SPACE_SLOT_PART));
        addSlotPart(15, MAKING_SLOT_PART);
    }

    @Override
    public Operation getOperation() {
        return operation;
    }

    @Override
    public void onPressMaking() {
        ItemStack leather = getInventory().getItem(collectTagSlotList(LEATHER_TAG).get(0));
        if (leather == null || !leather.getType().equals(Material.LEATHER)) return;
        Map<String, Integer> map = new HashMap<>();
        List<ItemStack> pages = collectTagSlotList(PAGE_TAG).stream().map(position -> getInventory().getItem(position))
                .filter(item -> {
                    GrassJson json = JsonHandler.getGrassJson(item);
                    if (item == null || json == null || json.hasDynamicValue("EnchantPower")) return false;
                    map.put(json.getDynamicValue("CustomName").getAsMaskedString().orElse(""), 0);
                    return true;
                }).collect(Collectors.toList());
        double sum = pages.stream().mapToDouble(page -> {
            GrassJson json = JsonHandler.getGrassJson(page);
            return json.getDynamicValue("EnchantPower").getAsMaskedDouble().orElse(0.0);
        }).sum();
        int level = (int) (sum / (11.0 - (double) map.size()));


//        GrassBookBindEvent event = new GrassBookBindEvent();
//        Bukkit.getServer().getPluginManager().callEvent(event);

        ItemStack result = JsonHandler.getEnchantBook(JsonBucket.getInstance().determineEnchant(Math.max(level, 1)), getInventory().getViewers().stream().findFirst().orElse(null));
        if (result != null) {
            collectTagSlotList(PAGE_TAG).stream().map(position -> getInventory().getItem(position)).forEach(item -> {
                if (item != null) item.setAmount(item.getAmount() - 1);
            });
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
    public String getTitle() {
        return ChatColor.DARK_GREEN + "製本";
    }

    @Override
    public boolean canKeepInventory() {
        return false;
    }

    @Override
    public boolean canOpen(Block block) {
        return true;
    }
}
