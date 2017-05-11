package life.grass.grasscooking.manager;

import life.grass.grasscooking.table.Table;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TableManager {
    private Map<Block, Table> tableMap;

    public TableManager() {
        tableMap = new HashMap<>();
    }

    public Optional<Table> findTable(Block block) {
        return Optional.ofNullable(tableMap.getOrDefault(block, null));
    }

    public Table createTable(Block block, Class<? extends Table> clazz) {
        try {
            tableMap.put(block, clazz.getConstructor(Block.class).newInstance(block));
            return tableMap.get(block);
        } catch (Exception ignore) {
            return null;
        }
    }

    public List<Table> getTableSet() {
        return (List<Table>) tableMap.values();
    }

    public void remove(Block block) {
        tableMap.remove(block);
    }
}
