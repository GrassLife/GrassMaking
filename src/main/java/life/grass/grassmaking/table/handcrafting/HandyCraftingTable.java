package life.grass.grassmaking.table.handcrafting;

import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.handcrafting.Recipe;
import life.grass.grassmaking.manager.RecipeShelf;
import life.grass.grassmaking.table.Table;
import life.grass.grassmaking.ui.SelectorInterface;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandyCraftingTable extends Table implements SelectorInterface {
    private static final ItemStack PADDING_ICON;
    private static RecipeShelf recipeShelf;

    private Inventory inventory;
    private Player player;
    private Map<Integer, ItemStack> selectedItemMap;

    static {
        PADDING_ICON = createIcon(Material.STAINED_GLASS_PANE, 12, null, null);
        recipeShelf = RecipeShelf.getInstance();
    }

    public HandyCraftingTable(Player player) {
        this.player = player;
        selectedItemMap = new HashMap<>();

        for (int i = 0; i < recipeShelf.getRecipeList().size() && i < TABLE_SIZE; i++) {
            Recipe recipe = recipeShelf.getRecipeList().get(i);
            GrassJson grassJson = JsonHandler.getGrassJson(recipe.getResultUniqueName());
            ItemStack grassItem = grassJson.toItemStack();

            ItemStack displayItem = new ItemStack(grassItem.getType(), recipe.getResultAmount(), grassItem.getDurability());
            ItemMeta meta = displayItem.getItemMeta();
            meta.setDisplayName(ChatColor.WHITE + grassJson.getDisplayName());

            List<String> lore = new ArrayList<>();
            lore.add(" ");
            recipe.getCraftingMaterialMap().forEach((craftingMaterial, amount) ->
                    lore.add(ChatColor.GRAY + " * " + JsonHandler.getGrassJson(craftingMaterial).getDisplayName() + "   x" + amount));

            meta.setLore(lore);
            displayItem.setItemMeta(meta);
            displayItem = JsonHandler.putDynamicData(displayItem, "Ignore", 1);

            selectedItemMap.put(i, displayItem);
        }

        inventory = initInventory();
    }

    @Override
    public ItemStack getPaddingIcon(int position) {
        return PADDING_ICON;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Inventory initInventory() {
        Inventory inv = super.initInventory();

        for (int i = 0; i < Table.TABLE_SIZE; i++) {
            ItemStack selectedItem = getSelectedItem(i);

            if (selectedItem != null && selectedItem.getType() != Material.AIR) inv.setItem(i, selectedItem);
        }

        return inv;
    }

    @Override
    public String getTitle() {
        return ChatColor.DARK_GRAY + "簡易作業台";
    }

    @Override
    public ItemStack getSelectedItem(int position) {
        return selectedItemMap.getOrDefault(position, null);
    }

    @Override
    public void onPressSelectedItem(int position) {
        Recipe recipe = recipeShelf.getRecipeList().get(position);
        if (recipe == null) return;

        PlayerInventory inventory = player.getInventory();

        Map<String, Integer> craftingMaterialMap = new HashMap<>();
        recipe.getCraftingMaterialMap().forEach(craftingMaterialMap::put);
        Map<Integer, Integer> decreaseSlotMap = new HashMap<>();
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item == null) continue;

            int amount = item.getAmount();
            GrassJson grassJson = JsonHandler.getGrassJson(item);
            if (grassJson == null) continue;

            String uniqueName = grassJson.getUniqueName();
            if (!craftingMaterialMap.containsKey(uniqueName) || craftingMaterialMap.get(uniqueName) == 0) continue;

            int left = craftingMaterialMap.get(uniqueName);
            if (left - amount <= 0) {
                decreaseSlotMap.put(slot, amount - left);
                craftingMaterialMap.put(uniqueName, 0);
            } else {
                decreaseSlotMap.put(slot, 0);
                craftingMaterialMap.put(uniqueName, left - amount);
            }
        }

        if (craftingMaterialMap.values().stream().anyMatch(value -> (0 < value))) return;

        decreaseSlotMap.forEach((slot, amount) -> {
            ItemStack item = inventory.getItem(slot);
            item.setAmount(amount);
            inventory.setItem(slot, item);
        });

        inventory.addItem(JsonHandler.getGrassJson(recipe.getResultUniqueName()).toItemStack(recipe.getResultAmount()));
    }
}
