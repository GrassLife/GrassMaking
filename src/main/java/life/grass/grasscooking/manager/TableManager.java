package life.grass.grasscooking.manager;

import life.grass.grasscooking.table.Table;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TableManager {
    private Map<String, Table> tableMap;

    public TableManager() {
        tableMap = new HashMap<>();
    }

    public Optional<Table> findTable(Block block) {
        return Optional.ofNullable(tableMap.get(generateKey(block)));
    }

    public Table createTable(Block block, Class<? extends Table> clazz) {
        String key = generateKey(block);
        try {
            tableMap.put(key, clazz.getConstructor(Block.class).newInstance(block));
            return tableMap.get(key);
        } catch (Exception ignore) {
            return null;
        }
    }

    public List<Table> getTableList() {
        return (List<Table>) tableMap.values();
    }

    public void remove(Block block) {
        tableMap.remove(generateKey(block));
    }

    private static String generateKey(Block block) {
        return block.getX() + "_" + block.getY() + "_" + block.getZ();
    }
}
