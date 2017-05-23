package life.grass.grasscooking.operation;

import life.grass.grasscooking.food.Cuisine;
import life.grass.grasscooking.food.Ingredient;
import life.grass.grasscooking.food.Meat;
import life.grass.grasscooking.manager.Kitchen;
import life.grass.grasscooking.table.Cooker;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CookingOperation extends VisualOperation {
    private ItemStack mainIngredient;
    private Cooker cooker;
    private Cuisine cuisine;

    public CookingOperation(Block block, Cooker cooker) {
        super(block);

        // TODO: change
        this.mainIngredient = new ItemStack(Material.RAW_BEEF);
        this.cooker = cooker;
    }

    public boolean precook(List<ItemStack> ingredientItemList, List<ItemStack> seasoningItemList) {
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientItemList.forEach(item -> {
            ingredientList.add(new Meat(item));
            cooker.getInventory().remove(item);
        });
        if (ingredientList.isEmpty()) return false;

        this.cuisine = Kitchen.cook(cooker, ingredientList, new ArrayList<>());
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
        return mainIngredient;
    }
}
