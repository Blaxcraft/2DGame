package us.mcsw.game.inv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CraftingManager {

	public static void addInitalRecipes() {
		addRecipe(Items.DOOR, Items.WOOD, Items.WOOD);
		addRecipe(Items.TRAPDOOR, Items.WOOD, Items.WOOD);
	}

	private static ArrayList<Recipe>	recipes	= new ArrayList<>();

	public static List<Recipe> getRecipes() {
		return recipes;
	}

	public static void addRecipe(Item product, Item... ingredients) {
		recipes.add(new Recipe(product, ingredients));
	}

	public static List<Recipe> getRecipesFor(Item i) {
		ArrayList<Recipe> ret = new ArrayList<>();

		for (Recipe r : recipes) {
			for (Item in : r.ingredients) {
				if (i.similar(in)) {
					ret.add(r);
					break;
				}
			}
		}

		return ret;
	}

	public static class Recipe {
		public Item		product;
		public Item[]	ingredients;

		private Recipe(Item product, Item... ingredients) {
			this.product = product;
			this.ingredients = ingredients;
		}

		public boolean canCraft(Inventory inv) {
			HashMap<Item, Integer> required = new HashMap<>();
			for (Item in : ingredients) {
				if (required.containsKey(in)) {
					int am = required.get(in);
					required.remove(in);
					required.put(in, am + 1);
				} else
					required.put(in, 1);
			}
			for (Item it : inv.contents) {
				if (it != null) {
					for (Item i : required.keySet().toArray(new Item[0])) {
						if (i.similar(it)) {
							if (required.get(i) > 1) {
								int am = required.get(i);
								required.remove(i);
								required.put(i, am - 1);
							} else
								required.remove(i);
						}
					}
				}
			}
			return required.size() == 0;
		}

		public HashMap<Item, Integer> ingredientCount() {
			HashMap<Item, Integer> req = new HashMap<>();

			for (Item i : ingredients) {
				if (req.containsKey(i)) {
					int am = req.get(i);
					req.remove(i);
					req.put(i, am + 1);
				} else
					req.put(i, 1);
			}

			return req;
		}
	}

}
