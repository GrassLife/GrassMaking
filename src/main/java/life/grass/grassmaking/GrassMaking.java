package life.grass.grassmaking;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import life.grass.grassmaking.listener.*;
import life.grass.grassmaking.manager.TableManager;
import life.grass.grassmaking.operation.Operable;
import life.grass.grassmaking.operation.Operation;
import life.grass.grassmaking.protocol.ItemPacketRewriter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrassMaking extends JavaPlugin {
    private static GrassMaking instance;
    private static TableManager tableManager;
    private static ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        tableManager = new TableManager();
        protocolManager = ProtocolLibrary.getProtocolManager();

        this.registerEvents();
        ItemPacketRewriter.getInstance().addListener(protocolManager, this);
        System.out.println();
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

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new ChunkLoad(), this);
        pm.registerEvents(new GrassCook(), this);
        pm.registerEvents(new InventoryClick(), this);
        pm.registerEvents(new PlayerInteract(), this);
        pm.registerEvents(new PlayerItemConsume(), this);
    }
}
