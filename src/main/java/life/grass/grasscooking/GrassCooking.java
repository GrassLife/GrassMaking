package life.grass.grasscooking;

import life.grass.grasscooking.listener.*;
import life.grass.grasscooking.manager.JsonFoodHamper;
import life.grass.grasscooking.manager.TableManager;
import life.grass.grasscooking.operation.Operable;
import life.grass.grasscooking.operation.Operation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

public final class GrassCooking extends JavaPlugin {
    private static GrassCooking instance;
    private static TableManager tableManager;
    private static JsonFoodHamper foodHamper;

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        tableManager = new TableManager();
        foodHamper = new JsonFoodHamper();

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

    public static GrassCooking getInstance() {
        return instance;
    }

    public static TableManager getTableManager() {
        return tableManager;
    }

    public static JsonFoodHamper getFoodHamper() {
        return foodHamper;
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new ChunkLoad(), this);
        pm.registerEvents(new InventoryClick(), this);
        pm.registerEvents(new ItemSpawn(), this);
        pm.registerEvents(new PlayerInteract(), this);
        pm.registerEvents(new PlayerItemConsume(), this);
    }
}
