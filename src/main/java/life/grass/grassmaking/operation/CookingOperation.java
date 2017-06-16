package life.grass.grassmaking.operation;

import life.grass.grassmaking.food.Cuisine;
import life.grass.grassmaking.food.Ingredient;
import life.grass.grassmaking.food.Seasoning;
import life.grass.grassmaking.manager.Kitchen;
import life.grass.grassmaking.table.Cooker;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CookingOperation extends VisualOperation {
    private Ingredient mainIngredient;
    private Seasoning mainSeasoning;
    private Cooker cooker;
    private Cuisine cuisine;

    public CookingOperation(Block block, Cooker cooker) {
        super(block);

        this.cooker = cooker;
    }

    public boolean precook(List<ItemStack> ingredientItemList, List<ItemStack> seasoningItemList) {
        List<Ingredient> ingredientList = new ArrayList<>();
        List<Seasoning> seasoningList = new ArrayList<>();
        ingredientItemList.stream()
                .filter(item -> Kitchen.generateFoodFromItemStack(item) instanceof Ingredient)
                .forEach(item -> {
                    ingredientList.add((Ingredient) Kitchen.generateFoodFromItemStack(item));
                    removeOneSlotItems(cooker.getInventory(), item);

                });
        seasoningItemList.stream()
                .filter(item -> Kitchen.generateFoodFromItemStack(item) instanceof Seasoning)
                .forEach(item -> {
                    seasoningList.add((Seasoning) Kitchen.generateFoodFromItemStack(item));
                    removeOneSlotItems(cooker.getInventory(), item);
                });
        if (ingredientList.isEmpty()) return false;

        mainIngredient = ingredientList.stream()
                .sorted(Comparator.comparing(Ingredient::getWeight).reversed())
                .findFirst().orElseThrow(IllegalArgumentException::new);

        mainSeasoning = seasoningList.stream()
                .sorted(Comparator.comparing(Seasoning::getWeight).reversed())
                .findFirst().orElse(null);

        this.cuisine = Kitchen.cook(cooker, ingredientList, seasoningList);
        return true;
    }

    @Override
    protected void onOperate() {
        Block block = getBlock();

        block.getWorld().spawnParticle(
                Particle.SMOKE_LARGE,
                block.getLocation().clone().add(0.5, 0.5, 0.5),
                3,
                0.25,
                0.25,
                0.25,
                0);
    }

    @Override
    protected void onFinish() {
        Block block = getBlock();
        World world = block.getWorld();

        world.spawnParticle(
                Particle.LAVA,
                block.getLocation().clone().add(0.5, 0.5, 0.5),
                8,
                0,
                0,
                0,
                0);

        world.dropItem(block.getLocation().clone().add(0.5D, 1.1D, 0.5D),
                cuisine.getItem()).setVelocity(new Vector(Math.random(), 8, Math.random()).multiply(0.03));
    }

    @Override
    protected ItemStack getVisualItem() {
        return mainIngredient.getItem();
    }

    public Cooker getCooker() {
        return cooker;
    }

    public Ingredient getMainIngredient() {
        return mainIngredient;
    }

    public Seasoning getMainSeasoning() {
        return mainSeasoning;
    }

    private void removeOneSlotItems(Inventory inventory, ItemStack item) {
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack slotItem = inventory.getItem(slot);

            if (slotItem != null && slotItem.equals(item)) {
                inventory.setItem(slot, new ItemStack(Material.AIR));
                return;
            }
        }

        throw new IllegalArgumentException();
    }

}
