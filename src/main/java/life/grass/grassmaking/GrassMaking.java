package life.grass.grassmaking;

import life.grass.grassmaking.listener.*;
import life.grass.grassmaking.manager.RecipeShelf;
import life.grass.grassmaking.manager.StationaryTableHolder;
import life.grass.grassmaking.operation.Operable;
import life.grass.grassmaking.operation.Operation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrassMaking extends JavaPlugin {
    private static GrassMaking instance;

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        RecipeShelf.getInstance();
        StationaryTableHolder.getInstance();

        this.registerEvents();
        this.registerRecipes();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        instance = null;

        StationaryTableHolder.getInstance().getTableList().stream()
                .filter(table -> table instanceof Operable)
                .map(table -> ((Operable) table).getOperation())
                .forEach(Operation::cancel);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    public static GrassMaking getInstance() {
        return instance;
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new BlockBreak(), this);
        pm.registerEvents(new BlockPistonExtend(), this);
        pm.registerEvents(new ChunkLoad(), this);
        pm.registerEvents(new GrassBookBind(), this);
        pm.registerEvents(new GrassCook(), this);
        pm.registerEvents(new GrassEnchant(), this);
        pm.registerEvents(new InventoryClick(), this);
        pm.registerEvents(new InventoryClose(), this);
        pm.registerEvents(new ItemRewrite(), this);
        pm.registerEvents(new PlayerInteract(), this);
        pm.registerEvents(new PlayerItemConsume(), this);
    }

    private void registerRecipes() {
        ShapelessRecipe handyCraftingTable = new ShapelessRecipe(new NamespacedKey(this, "HandyCraftingTable"), new ItemStack(Material.BREWING_STAND_ITEM));
        handyCraftingTable.addIngredient(Material.STONE_PLATE);
        handyCraftingTable.addIngredient(Material.TRIPWIRE_HOOK);
        handyCraftingTable.addIngredient(Material.STICK);
        getServer().addRecipe(handyCraftingTable);
    }
}
