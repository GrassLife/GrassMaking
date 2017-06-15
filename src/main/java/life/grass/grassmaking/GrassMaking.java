package life.grass.grassmaking;

import life.grass.grassmaking.listener.*;
import life.grass.grassmaking.manager.GeneratorManager;
import life.grass.grassmaking.manager.JsonFoodHamper;
import life.grass.grassmaking.manager.TableManager;
import life.grass.grassmaking.operation.Operable;
import life.grass.grassmaking.operation.Operation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrassMaking extends JavaPlugin {
    private static GrassMaking instance;
    private static TableManager tableManager;
    private static JsonFoodHamper foodHamper;
    private static GeneratorManager generatorManager;

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        tableManager = new TableManager();
        foodHamper = new JsonFoodHamper();
        generatorManager = new GeneratorManager();

        this.registerEvents();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        instance = null;

        tableManager.getTableList().stream()
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

    public static TableManager getTableManager() {
        return tableManager;
    }

    public static JsonFoodHamper getFoodHamper() {
        return foodHamper;
    }

    public static GeneratorManager getGeneratorManager() {
        return generatorManager;
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new ChunkLoad(), this);
        pm.registerEvents(new EntityDeath(), this);
        pm.registerEvents(new InventoryClick(), this);
        pm.registerEvents(new ItemSpawn(), this);
        pm.registerEvents(new PlayerInteract(), this);
        pm.registerEvents(new PlayerItemConsume(), this);
    }
}
