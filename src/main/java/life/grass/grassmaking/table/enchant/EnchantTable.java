package life.grass.grassmaking.table.enchant;

import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.operation.Operation;
import life.grass.grassmaking.operation.enchant.EnchantOperation;
import life.grass.grassmaking.table.Maker;
import life.grass.grassmaking.ui.enchant.EnchantInterface;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;

public class EnchantTable extends Maker implements EnchantInterface {
    private static final ItemStack MAKING_ICON;
    private static final ItemStack REDSTONE_ICON;
    private static final ItemStack GLOWSTONE_ICON;
    private static final ItemStack ENCHANTED_BOOK_ICON;
    private static final ItemStack TARGET_ICON;

    private EnchantOperation operation;

    static {
        MAKING_ICON = createIcon(Material.ENCHANTMENT_TABLE, 0, ChatColor.BLUE + "エンチャントする", null);
        REDSTONE_ICON = createIcon(Material.REDSTONE, 0, ChatColor.RED + "レッドストーン", null);
        GLOWSTONE_ICON = createIcon(Material.GLOWSTONE_DUST, 0, ChatColor.YELLOW + "グロウストーンダスト", null);
        ENCHANTED_BOOK_ICON = createIcon(Material.ENCHANTED_BOOK, 0, ChatColor.LIGHT_PURPLE + "エンチャント本", null);
        TARGET_ICON = createIcon(Material.IRON_PICKAXE, 0, ChatColor.GOLD + "エンチャント対象", null);
    }

    public EnchantTable(Block block, EnchantOperation operation) {
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
        return 43;
    }

    @Override
    public void onPressMaking() {
        ItemStack glow = getInventory().getItem(getGlowstoneSpacePosition());
        ItemStack red = getInventory().getItem(getRedstoneSpacePosition());
        if (glow == null || red == null) return;
        if (!glow.getType().equals(Material.GLOWSTONE_DUST) ||
                !red.getType().equals(Material.REDSTONE)) return;
        ItemStack book = getInventory().getItem(getEnchantedBookSpacePosition());
        ItemStack target = getInventory().getItem(getTargetSpacePosition());

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
    public boolean canOpen(Block block) {
        return true;
    }

    @Override
    public ItemStack getPaddingIcon(int position) {
        return super.getPaddingIcon(position);
    }

    @Override
    public Inventory initInventory() {
        Inventory inventory = super.initInventory();

        inventory.setItem(getRedstoneIconPosition(), getRedstoneIcon());
        inventory.setItem(getRedstoneSpacePosition(), null);
        inventory.setItem(getGlowstoneIconPosition(), getGlowstoneIcon());
        inventory.setItem(getGlowstoneSpacePosition(), null);
        inventory.setItem(getEnchantedBookIconPosition(), getEnchantedBookIcon());
        inventory.setItem(getEnchantedBookSpacePosition(), null);
        inventory.setItem(getTargetIconPosition(), getTargetIcon());
        inventory.setItem(getTargetSpacePosition(), null);

        return inventory;
    }

    public ItemStack getRedstoneIcon() {
        return REDSTONE_ICON;
    }

    @Override
    public int getRedstoneIconPosition() {
        return 28;
    }

    @Override
    public int getRedstoneSpacePosition() {
        return 29;
    }

    @Override
    public ItemStack getGlowstoneIcon() {
        return GLOWSTONE_ICON;
    }

    @Override
    public int getGlowstoneIconPosition() {
        return 40;
    }

    @Override
    public int getGlowstoneSpacePosition() {
        return 41;
    }

    @Override
    public ItemStack getEnchantedBookIcon() {
        return ENCHANTED_BOOK_ICON;
    }

    @Override
    public int getEnchantedBookIconPosition() {
        return 11;
    }

    @Override
    public int getEnchantedBookSpacePosition() {
        return 12;
    }

    @Override
    public ItemStack getTargetIcon() {
        return TARGET_ICON;
    }

    @Override
    public int getTargetIconPosition() {
        return 23;
    }

    @Override
    public int getTargetSpacePosition() {
        return 24;
    }
}
