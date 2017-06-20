package life.grass.grassmaking.table;

import life.grass.grassmaking.food.*;
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
        List<Ingredient> ingredientList = new ArrayList<>();
        getIngredientSpacePositionList().stream()
                .map(position -> Food.makeFood(getInventory().getItem(position)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(food -> food instanceof Ingredient)
                .map(food -> (Ingredient) food)
                .forEach(ingredientList::add);

        List<Seasoning> seasoningList = new ArrayList<>();
        getSeasoningSpacePositionList().stream()
                .map(position -> Food.makeFood(getInventory().getItem(position)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(food -> food instanceof Seasoning)
                .map(food -> (Seasoning) food)
                .forEach(seasoningList::add);

        Cuisine result = cook(ingredientList, seasoningList);
        if (result != null) {
            operation.setCuisine(result);
            operation.start(getCookingTick());
        }
    }

    public Cuisine cook(List<Ingredient> ingredientList, List<Seasoning> seasoningList) {
        if (ingredientList.isEmpty() || operation.isOperating()) return null;

        int amount = ingredientList.size();

        int totalWeight = 0;
        int[] ingredientWeights = ingredientList.stream().mapToInt(Food::getWeight).toArray();
        for (int i = 0; i < ingredientWeights.length; i++) totalWeight += ingredientWeights[i];

        Inventory inventory = this.getInventory();
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack slotItem = inventory.getItem(slot);
            Food.makeFood(slotItem).ifPresent(food -> {
                if (food instanceof Ingredient || food instanceof Seasoning)
                    slotItem.setAmount(slotItem.getAmount() - 1);
            });

            inventory.setItem(slot, slotItem);
        }

        Ingredient mainIngredient = ingredientList.stream().sorted(Comparator.comparing(Food::getWeight).reversed()).findFirst().orElseThrow(IllegalArgumentException::new);
        Seasoning mainSeasoning = seasoningList.stream().sorted(Comparator.comparing(Food::getWeight).reversed()).findFirst().orElse(null);
        final Cuisine cuisine = Cuisine.makeCuisine(new ItemStack(mainIngredient.getAfterMaterial(), amount));

        cuisine.setType(mainIngredient.getAfterMaterial());

        int weight = totalWeight / amount;
        cuisine.setAdditionalWeight(weight);

        long ingredientDiff = ChronoUnit.MINUTES.between(LocalDateTime.now(), mainIngredient.getExpireDate());
        long seasoningDiff = mainSeasoning == null ? 0 : ChronoUnit.MINUTES.between(LocalDateTime.now(), mainSeasoning.getExpireDate());
        cuisine.setExpireDate(LocalDateTime.now().plusMinutes(ingredientDiff * 2).plusMinutes(seasoningDiff / 4));

        cuisine.setAdditionalRestoreAmount(weight / 5);

        Map<FoodElement, Integer> elementMap = new HashMap<>();
        ingredientList.forEach(ingredient -> ingredient.getElementMap().forEach((key, value) -> elementMap.put(key, value / 2)));
        seasoningList.forEach(seasoning -> seasoning.getElementMap().forEach((key, value) -> elementMap.put(key, value * 2)));
        elementMap.forEach((key, value) -> {
            if (value != 0) cuisine.putElement(key, value);
        });

        cuisine.setName("調理後料理");

        return cuisine;
    }

    public CookingOperation getOperation() {
        return operation;
    }

    public int getCookingTick() {
        return 5 * 4;
    }
}
