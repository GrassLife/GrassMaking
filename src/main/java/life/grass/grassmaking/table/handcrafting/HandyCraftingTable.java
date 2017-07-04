package life.grass.grassmaking.table.handcrafting;

import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonHandler;
import life.grass.grassmaking.event.GrassHandyCraftEvent;
import life.grass.grassmaking.handcrafting.Recipe;
import life.grass.grassmaking.manager.RecipeShelf;
import life.grass.grassmaking.table.HandyTable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class HandyCraftingTable extends HandyTable {
    private static RecipeShelf recipeShelf;

    private Map<Integer, ItemStack> selectedItemMap;

    static {
        recipeShelf = RecipeShelf.getInstance();
    }

    public HandyCraftingTable() {
        selectedItemMap = new HashMap<>();
        for (int i = 0; i < recipeShelf.getRecipeList().size() && i < TABLE_SIZE; i++) {
            GrassJson grassJson = JsonHandler.getGrassJson(recipeShelf.getRecipeList().get(i).getResultUniqueName());
            selectedItemMap.put(i, grassJson.toItemStack());
        }
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
        Player player = (Player) getInventory().getViewers().get(0);
        Recipe recipe = recipeShelf.getRecipeList().get(position);
        if (player == null || recipe == null) return;

        GrassHandyCraftEvent event = new GrassHandyCraftEvent(player, this, recipe);
        if (event.getResult() != null) {
        }
    }
}
