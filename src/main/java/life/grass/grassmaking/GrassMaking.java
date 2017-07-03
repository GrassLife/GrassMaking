package life.grass.grassmaking;

import life.grass.grassmaking.listener.*;
import life.grass.grassmaking.manager.TableManager;
import life.grass.grassmaking.operation.Operable;
import life.grass.grassmaking.operation.Operation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrassMaking extends JavaPlugin {
    private static GrassMaking instance;
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

        tableManager.getTableSet().stream()
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

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new BlockBreak(), this);
        pm.registerEvents(new BlockPistonExtend(), this);
        pm.registerEvents(new ChunkLoad(), this);
        pm.registerEvents(new GrassCook(), this);
        pm.registerEvents(new InventoryClick(), this);
        pm.registerEvents(new ItemRewrite(), this);
        pm.registerEvents(new PlayerInteract(), this);
        pm.registerEvents(new PlayerItemConsume(), this);
    }
}
