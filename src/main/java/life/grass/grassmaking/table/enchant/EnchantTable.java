package life.grass.grassmaking.table.enchant;

import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.operation.Operation;
import life.grass.grassmaking.operation.enchant.EnchantOperation;
import life.grass.grassmaking.table.MakingTable;
import life.grass.grassmaking.ui.SlotPart;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;

public class EnchantTable extends MakingTable {
    private static final String REDSTONE_TAG = "Redstone";
    private static final String GLOWSTONE_TAG = "Glowstone";
    private static final String ENCHANTED_BOOK_TAG = "EnchantedBook";
    private static final String TARGET_TAG = "Target";
    private static final SlotPart MAKING_SLOT_PART = new SlotPart(false, MAKING_TAG, Material.ENCHANTMENT_TABLE, 0, ChatColor.BLUE + "エンチャントする", null);
    private static final SlotPart REDSTONE_SLOT_PART = new SlotPart(false, null, Material.REDSTONE, 0, ChatColor.RED + "レッドストーン入れ", null);
    private static final SlotPart GLOWSTONE_SLOT_PART = new SlotPart(false, null, Material.GLOWSTONE_DUST, 0, ChatColor.YELLOW + "グロウストーンダスト入れ", null);
    private static final SlotPart ENCHANTED_BOOK_SLOT_PART = new SlotPart(false, null, Material.ENCHANTED_BOOK, 0, ChatColor.LIGHT_PURPLE + "エンチャント本", null);
    private static final SlotPart TARGET_SLOT_PART = new SlotPart(false, null, Material.IRON_PICKAXE, 0, ChatColor.GOLD + "エンチャント対象", null);
    private static final SlotPart REDSTONE_SPACE_SLOT_PART = new SlotPart(true, REDSTONE_TAG);
    private static final SlotPart GLOWSTONE_SPACE_SLOT_PART = new SlotPart(true, GLOWSTONE_TAG);
    private static final SlotPart ENCHANTED_BOOK_SPACE_SLOT_PART = new SlotPart(true, ENCHANTED_BOOK_TAG);
    private static final SlotPart TARGET_SPACE_SLOT_PART = new SlotPart(true, TARGET_TAG);

    private EnchantOperation operation;

    public EnchantTable(Block block, EnchantOperation operation) {
        super(block);
        this.operation = operation;

        addSlotPart(28, REDSTONE_SLOT_PART);
        addSlotPart(29, REDSTONE_SPACE_SLOT_PART);
        addSlotPart(40, GLOWSTONE_SLOT_PART);
        addSlotPart(41, GLOWSTONE_SPACE_SLOT_PART);
        addSlotPart(11, ENCHANTED_BOOK_SLOT_PART);
        addSlotPart(12, ENCHANTED_BOOK_SPACE_SLOT_PART);
        addSlotPart(23, TARGET_SLOT_PART);
        addSlotPart(24, TARGET_SPACE_SLOT_PART);
        addSlotPart(43, MAKING_SLOT_PART);
    }

    @Override
    public Operation getOperation() {
        return operation;
    }

    @Override
    public void onPressMaking() {
        ItemStack glow = getInventory().getItem(collectTagSlotList(GLOWSTONE_TAG).get(0));
        ItemStack red = getInventory().getItem(collectTagSlotList(REDSTONE_TAG).get(0));
        if (glow == null || red == null) return;
        if (!glow.getType().equals(Material.GLOWSTONE_DUST) ||
                !red.getType().equals(Material.REDSTONE)) return;
        ItemStack book = getInventory().getItem(collectTagSlotList(ENCHANTED_BOOK_TAG).get(0));
        ItemStack target = getInventory().getItem(collectTagSlotList(TARGET_TAG).get(0));

        if (target == null || book == null) return;
        GrassJson bookJson = JsonHandler.getGrassJson(book);
        GrassJson targetJson = JsonHandler.getGrassJson(target);

        if (!bookJson.hasDynamicValue("Enchant/Target") || !bookJson.hasDynamicValue("Enchant/Of")) return;
        String targetKey = bookJson.getDynamicValue("Enchant/Target").getAsMaskedString().orElse("NONE");
        if (target.equals("NONE")) return;
        if (!targetJson.hasItemTag(targetKey)) return;
        String enchantKey = bookJson.getDynamicValue("Enchant/Of").getAsMaskedString().orElse("");
        if (enchantKey.equals("")) return;

        ItemStack result = JsonHandler.putDynamicData(target, "Enchant/Suffix", enchantKey);

//        GrassEnchantEvent event = new GrassEnchantEvent();
//        Bukkit.getServer().getPluginManager().callEvent(event);

//        ItemStack result = operation.getResult();
        if (result != null && !result.getType().equals(Material.AIR)) {
            book.setAmount(book.getAmount() - 1);
            target.setAmount(target.getAmount() - 1);
            red.setAmount(red.getAmount() - 1);
            glow.setAmount(glow.getAmount() - 1);
            operation.setResult(result);
            operation.start(20 * 3 /* seconds */);
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
        return ChatColor.DARK_BLUE + "エンチャント";
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
