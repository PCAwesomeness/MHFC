package mhfc.net.common.crafting.recipes.equipment;

import java.util.List;

import mhfc.net.common.crafting.recipes.MHFCShapelessRecipe;
import net.minecraft.item.ItemStack;

public class EquipmentRecipe extends MHFCShapelessRecipe {

	private int heat, duration;

	public EquipmentRecipe(ItemStack recipeProduct,
		List<ItemStack> recipeIngredients, int requiredHeat, int duration) {
		super(recipeProduct, recipeIngredients);
		this.heat = requiredHeat;
		this.duration = duration;

	}

	public ItemStack[] getRequirements(int padTo) {
		padTo = Math.max(0, padTo);
		ItemStack[] stacks = new ItemStack[padTo];
		return recipeItems.toArray(stacks);
	}

	public int getRequiredHeat() {
		return heat;
	}

	public int getDuration() {
		return duration;
	}

}
