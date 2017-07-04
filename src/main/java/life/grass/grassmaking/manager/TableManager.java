package life.grass.grassmaking.manager;

import life.grass.grassmaking.table.Table;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TableManager {
    private static TableManager instance;

    private Map<String, Table> tableMap;

    static {
        instance = new TableManager();
    }

    private TableManager() {
        tableMap = new HashMap<>();
    }

    public static TableManager getInstance() {
        return instance;
    }

    public Optional<Table> findTable(Block block) {
        return Optional.ofNullable(tableMap.get(generateKey(block)));
    }

    public Table createTable(Block block, Table table) {
        String key = generateKey(block);
        try {
            tableMap.put(key, table);
            return tableMap.get(key);
        } catch (Exception ignore) {
            return null;
        }
    }

    public List<Table> getTableSet() {
        return (List<Table>) tableMap.values();
    }

    public void remove(Block block) {
        tableMap.remove(generateKey(block));
    }

    private static String generateKey(Block block) {
        return block.getX() + "_" + block.getY() + "_" + block.getZ();
    }
}
