package cpw.mods.fml.common.event;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.List;

/**
 * This event is fired if a world is loaded that has block and item mappings
 * referring the mod that are not in existence. These can be remapped to other
 * existing objects, or simply discarded. Use get() and getAll() to process this
 * event.
 *
 * @author cpw, Player
 *
 */
public class FMLMissingMappingsEvent extends FMLEvent {
	/**
	 * Actions you can take with this missing mapping.
	 * <ul>
	 * <li>{@link #IGNORE} means this missing mapping will be ignored.
	 * <li>{@link #WARN} means this missing mapping will generate a warning.
	 * <li>{@link #FAIL} means this missing mapping will prevent the world from
	 * loading.
	 * </ul>
	 *
	 * @author cpw
	 *
	 */
	public enum Action {
		/**
		 * Take the default action
		 */
		DEFAULT,
		/**
		 * Ignore this missing mapping. This means the mapping will be abandoned
		 */
		IGNORE,
		/**
		 * Generate a warning but allow loading to continue
		 */
		WARN,
		/**
		 * Fail to load
		 */
		FAIL,
		/**
		 * Remap this name to a new name (add a migration mapping)
		 */
		REMAP,
		/**
		 * Allow a block to exist without itemblock anymore
		 */
		BLOCKONLY
	}

	public static class MissingMapping {
		public final GameRegistry.Type type;
		public final String name;
		public final int id;
		private Action action = Action.DEFAULT;
		private Object target;

		public MissingMapping(String name, int id) {
			type = name.charAt(0) == '\u0001' ? GameRegistry.Type.BLOCK : GameRegistry.Type.ITEM;
			this.name = name.substring(1);
			this.id = id;
		}

		/**
		 * @deprecated use ignore(), warn(), fail() or remap() instead
		 */
		@Deprecated
		public void setAction(Action target) {
			if (target == Action.DEFAULT || target == Action.REMAP || target == Action.BLOCKONLY)
				throw new IllegalArgumentException();

			action = target;
		}

		/**
		 * Ignore the missing item.
		 */
		public void ignore() {
			action = Action.IGNORE;
		}

		/**
		 * Warn the user about the missing item.
		 */
		public void warn() {
			action = Action.WARN;
		}

		/**
		 * Prevent the world from loading due to the missing item.
		 */
		public void fail() {
			action = Action.FAIL;
		}

		/**
		 * Remap the missing item to the specified Block.
		 *
		 * Use this if you have renamed a Block, don't forget to handle the ItemBlock.
		 * Existing references using the old name will point to the new one.
		 *
		 * @param target
		 *            Block to remap to.
		 */
		public void remap(Block target) {
			if (type != GameRegistry.Type.BLOCK)
				throw new IllegalArgumentException("Can't remap an item to a block.");
			if (target == null)
				throw new NullPointerException("remap target is null");
			if (GameData.getBlockRegistry().getId(target) < 0)
				throw new IllegalArgumentException(
						String.format("The specified block %s hasn't been registered at startup.", target));

			action = Action.REMAP;
			this.target = target;
		}

		/**
		 * Remap the missing item to the specified Item.
		 *
		 * Use this if you have renamed an Item. Existing references using the old name
		 * will point to the new one.
		 *
		 * @param target
		 *            Item to remap to.
		 */
		public void remap(Item target) {
			if (type != GameRegistry.Type.ITEM)
				throw new IllegalArgumentException("Can't remap a block to an item.");
			if (target == null)
				throw new NullPointerException("remap target is null");
			if (GameData.getItemRegistry().getId(target) < 0)
				throw new IllegalArgumentException(
						String.format("The specified item %s hasn't been registered at startup.", target));

			action = Action.REMAP;
			this.target = target;
		}

		public void skipItemBlock() {
			if (type != GameRegistry.Type.ITEM)
				throw new IllegalArgumentException("Cannot skip an item that is a block");
			if (GameData.getBlockRegistry().getRaw(id) == null)
				throw new IllegalArgumentException("Cannot skip an ItemBlock that doesn't have a Block");
			action = Action.BLOCKONLY;
		}
		// internal

		public Action getAction() {
			return action;
		}

		public Object getTarget() {
			return target;
		}
	}

	private final ListMultimap<String, MissingMapping> missing;
	private ModContainer activeContainer;

	public FMLMissingMappingsEvent(ListMultimap<String, MissingMapping> missingMappings) {
		missing = missingMappings;
	}

	@Override
	public void applyModContainer(ModContainer activeContainer) {
		super.applyModContainer(activeContainer);
		this.activeContainer = activeContainer;
	}

	/**
	 * Get the list of missing mappings for the active mod.
	 *
	 * Process the list entries by calling ignore(), warn(), fail() or remap() on
	 * each entry.
	 *
	 * @return list of missing mappings
	 */
	public List<MissingMapping> get() {
		return ImmutableList.copyOf(missing.get(activeContainer.getModId()));
	}

	/**
	 * Get the list of missing mappings for all mods.
	 *
	 * Only use this if you need to handle mod id changes, e.g. if you renamed your
	 * mod or split/merge into/from multiple mods.
	 *
	 * Process the list entries by calling ignore(), warn(), fail() or remap() on
	 * each entry you want to handle.
	 *
	 * @return list of missing mappings
	 */
	public List<MissingMapping> getAll() {
		return ImmutableList.copyOf(missing.values());
	}
}