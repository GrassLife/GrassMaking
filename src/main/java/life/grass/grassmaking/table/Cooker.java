package life.grass.grassmaking.table;

import life.grass.grassitem.GrassItemHandler;
import life.grass.grassmaking.food.Food;
import life.grass.grassmaking.food.FoodElement;
import life.grass.grassmaking.operation.CookingOperation;
import life.grass.grassmaking.ui.CookerInterface;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public abstract class Cooker extends Maker implements CookerInterface {
    private static final ItemStack SEASONING_ICON;

    private CookingOperation operation;

    static {
        SEASONING_ICON = createIcon(Material.NAME_TAG, 0, ChatColor.RED + "調味料", null);
    }

    public Cooker(Block block) {
        super(block);

        operation = new CookingOperation(block);
    }

    @Override
    public ItemStack getSeasoningIcon() {
        return SEASONING_ICON;
    }

    @Override
    public Inventory initInventory() {
        Inventory inv = super.initInventory();

        getSeasoningSpacePositionList().forEach(position -> inv.setItem(position, null));
        inv.setItem(getSeasoningIconPosition(), getSeasoningIcon());

        return inv;
    }

    @Override
    public void onPressedMaking() {
        List<Food> ingredientList = new ArrayList<>();
        getIngredientSpacePositionList().stream()
                .map(position -> Food.findFood(getInventory().getItem(position)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Food::isIngredient)
                .forEach(ingredientList::add);

        List<Food> seasoningList = new ArrayList<>();
        getSeasoningSpacePositionList().stream()
                .map(position -> Food.findFood(getInventory().getItem(position)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Food::isSeasoning)
                .forEach(seasoningList::add);

        Food result = cook(ingredientList, seasoningList);
        if (result != null) {
            operation.setCuisine(result);
            operation.start(getCookingTick());
        }
    }

    public Food cook(List<Food> ingredientList, List<Food> seasoningList) {
        if (ingredientList.isEmpty()) return null;

        Inventory inventory = this.getInventory();
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack slotItem = inventory.getItem(slot);
            if (Food.findFood(slotItem).isPresent()) inventory.remove(slotItem);
        }

        Food mainIngredient = ingredientList.stream().sorted(Comparator.comparing(Food::getWeight).reversed()).findFirst().orElse(null);
        Food mainSeasoning = seasoningList.stream().sorted(Comparator.comparing(Food::getWeight).reversed()).findFirst().orElse(null);

        int amount = ingredientList.size();

        int totalWeight = 0;
        int[] ingredientWeights = ingredientList.stream().mapToInt(Food::getWeight).toArray();
        for (int i = 0; i < ingredientWeights.length; i++) totalWeight += ingredientWeights[i];

        ItemStack cuisine = new ItemStack(mainIngredient.getAfterMaterial(), amount);
        GrassItemHandler.putUniqueNameToItemStack(cuisine, "Cuisine");

        int weight = totalWeight / amount;
        GrassItemHandler.putDynamicDataToItemStack(cuisine, "Weight", String.valueOf(weight));

        long ingredientDiff = ChronoUnit.MINUTES.between(LocalDateTime.now(), mainIngredient.getExpireDate());
        long seasoningDiff = mainSeasoning == null ? 0 : ChronoUnit.MINUTES.between(LocalDateTime.now(), mainSeasoning.getExpireDate());
        GrassItemHandler.putDynamicDataToItemStack(cuisine, "ExpireDate", (LocalDateTime.now().plusMinutes(ingredientDiff * 2).plusMinutes(seasoningDiff / 4).toString()));

        GrassItemHandler.putDynamicDataToItemStack(cuisine, "RestoreAmount", String.valueOf(weight / 5));

        Map<FoodElement, Integer> elementMap = new HashMap<>();
        ingredientList.forEach(ingredient -> ingredient.getElementMap().forEach((key, value) -> elementMap.put(key, value / 2)));
        seasoningList.forEach(seasoning -> seasoning.getElementMap().forEach((key, value) -> elementMap.put(key, value * 2)));
        elementMap.forEach((key, value) -> GrassItemHandler.putDynamicDataToItemStack(cuisine, "Element" + key.toString(), "+" + (value / amount)));

        GrassItemHandler.putDynamicDataToItemStack(cuisine, "CuisineName", "焼いた" + mainIngredient.getGrassJson().getDisplayName());

        return Food.findFood(cuisine).orElse(null);
    }

    public CookingOperation getOperation() {
        return operation;
    }

    public int getCookingTick() {
        return 5 * 4;
    }
}
