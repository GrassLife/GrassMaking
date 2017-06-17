package life.grass.grassmaking.table;

import life.grass.grassmaking.food.Food;
import life.grass.grassmaking.food.FoodElement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class IronPlate extends Cooker {
    private static final ItemStack PADDING_ICON_FENCE;
    private static final ItemStack PADDING_ICON_FIRE;
    private static final ItemStack MAKING_ICON;

    static {
        PADDING_ICON_FENCE = createIcon(Material.IRON_FENCE, 0, null, null);
        PADDING_ICON_FIRE = createIcon(Material.STAINED_GLASS_PANE, 14, null, null);
        MAKING_ICON = createIcon(Material.CAULDRON_ITEM, 0, ChatColor.RED + "調理する", null);
    }

    public IronPlate(Block block) {
        super(block);
    }

    // TODO: move to Cooker
    @Override
    protected Food cook(List<Food> ingredientList, List<Food> seasoningList) {
        Inventory inventory = this.getInventory();
        ingredientList.forEach(ingredient -> subtractOneItem(inventory, ingredient.getItem()));
        seasoningList.forEach(seasoning -> subtractOneItem(inventory, seasoning.getItem()));

        if (ingredientList.isEmpty()) return null;

        Food mainIngredient = ingredientList.stream().sorted(Comparator.comparing(Food::getTotalWeight).reversed()).findFirst().orElse(null);
        Food mainSeasoning = seasoningList.stream().sorted(Comparator.comparing(Food::getTotalWeight).reversed()).findFirst().orElse(null);

        int amount = ingredientList.size();

        int totalWeight = 0;
        int[] ingredientWeights = ingredientList.stream().mapToInt(Food::getTotalWeight).toArray();
        for (int i = 0; i < ingredientWeights.length; i++) totalWeight += ingredientWeights[i];

        Food cuisine = Food.makeCuisine(new ItemStack(mainIngredient.getAfterMaterial(), amount));

        int weight = totalWeight / amount;
        cuisine.setAdditionalWeight(weight);

        long ingredientDiff = ChronoUnit.MINUTES.between(LocalDateTime.now(), mainIngredient.getExpireDate());
        long seasoningDiff = mainSeasoning == null ? 0 : ChronoUnit.MINUTES.between(LocalDateTime.now(), mainSeasoning.getExpireDate());
        cuisine.setExpireDate(LocalDateTime.now().plusMinutes(ingredientDiff * 2).plusMinutes(seasoningDiff / 4));

        cuisine.setAdditionalRestoreAmount(weight / 5);

        Map<FoodElement, Integer> elementMap = new HashMap<>();
        ingredientList.forEach(ingredient -> ingredient.getTotalElementMap().forEach((key, value) -> elementMap.put(key, value / 2)));
        seasoningList.forEach(seasoning -> seasoning.getTotalElementMap().forEach((key, value) -> elementMap.put(key, value * 2)));
        elementMap.forEach((key, value) -> cuisine.increaseAdditionalElement(key, value / amount));

        cuisine.setName("焼いた" + mainIngredient);

        return cuisine;
    }

    @Override
    public String getTitle() {
        return ChatColor.DARK_RED + "鉄板";
    }

    @Override
    public ItemStack getPaddingIcon(int position) {
        ItemStack icon = super.getPaddingIcon(position);

        switch (position) {
            case 9:
            case 14:
            case 18:
            case 23:
            case 28:
            case 31:
            case 38:
            case 39:
                icon = PADDING_ICON_FENCE;
                break;
            case 47:
            case 48:
                icon = PADDING_ICON_FIRE;
                break;
        }

        return icon;
    }

    @Override
    public ItemStack getMakingIcon() {
        return MAKING_ICON;
    }

    @Override
    public int getSeasoningIconPosition() {
        return 34;
    }

    @Override
    public List<Integer> getSeasoningSpacePositionList() {
        return Arrays.asList(41, 42, 43);
    }

    @Override
    public int getCookingTick() {
        return 5 * 6;
    }

    @Override
    public int getMakingIconPosition() {
        return 16;
    }

    @Override
    public List<Integer> getIngredientSpacePositionList() {
        return Arrays.asList(10, 11, 12, 13, 19, 20, 21, 22, 29, 30);
    }
}
