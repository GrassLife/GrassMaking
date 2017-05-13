package life.grass.grasscooking;

import life.grass.grasscooking.listener.ChunkLoad;
import life.grass.grasscooking.listener.InventoryClick;
import life.grass.grasscooking.listener.PlayerInteract;
import life.grass.grasscooking.manager.TableManager;
import life.grass.grasscooking.operation.Operable;
import life.grass.grasscooking.operation.Operation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrassCooking extends JavaPlugin {
    private static GrassCooking instance;
    private static TableManager tableManager;

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        tableManager = new TableManager();

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

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new ChunkLoad(), this);
        pm.registerEvents(new InventoryClick(), this);
        pm.registerEvents(new PlayerInteract(), this);
    }
}
