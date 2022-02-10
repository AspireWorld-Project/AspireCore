package net.minecraft.item;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class ItemTool extends Item {
	private Set field_150914_c;
	protected float efficiencyOnProperMaterial = 4.0F;
	private float damageVsEntity;
	protected Item.ToolMaterial toolMaterial;
	private static final String __OBFID = "CL_00000019";

	protected ItemTool(float p_i45333_1_, Item.ToolMaterial p_i45333_2_, Set p_i45333_3_) {
		toolMaterial = p_i45333_2_;
		field_150914_c = p_i45333_3_;
		maxStackSize = 1;
		setMaxDamage(p_i45333_2_.getMaxUses());
		efficiencyOnProperMaterial = p_i45333_2_.getEfficiencyOnProperMaterial();
		damageVsEntity = p_i45333_1_ + p_i45333_2_.getDamageVsEntity();
		setCreativeTab(CreativeTabs.tabTools);
		if (this instanceof ItemPickaxe) {
			toolClass = "pickaxe";
		} else if (this instanceof ItemAxe) {
			toolClass = "axe";
		} else if (this instanceof ItemSpade) {
			toolClass = "shovel";
		}
	}

	@Override
	public float func_150893_a(ItemStack p_150893_1_, Block p_150893_2_) {
		return field_150914_c.contains(p_150893_2_) ? efficiencyOnProperMaterial : 1.0F;
	}

	@Override
	public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase p_77644_2_, EntityLivingBase p_77644_3_) {
		p_77644_1_.damageItem(2, p_77644_3_);
		return true;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack p_150894_1_, World p_150894_2_, Block p_150894_3_, int p_150894_4_,
			int p_150894_5_, int p_150894_6_, EntityLivingBase p_150894_7_) {
		if (p_150894_3_.getBlockHardness(p_150894_2_, p_150894_4_, p_150894_5_, p_150894_6_) != 0.0D) {
			p_150894_1_.damageItem(1, p_150894_7_);
		}

		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}

	public Item.ToolMaterial func_150913_i() {
		return toolMaterial;
	}

	@Override
	public int getItemEnchantability() {
		return toolMaterial.getEnchantability();
	}

	public String getToolMaterialName() {
		return toolMaterial.toString();
	}

	@Override
	public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_) {
		ItemStack mat = toolMaterial.getRepairItemStack();
		if (mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, p_82789_2_, false))
			return true;
		return super.getIsRepairable(p_82789_1_, p_82789_2_);
	}

	@Override
	public Multimap getItemAttributeModifiers() {
		Multimap multimap = super.getItemAttributeModifiers();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
				new AttributeModifier(field_111210_e, "Tool modifier", damageVsEntity, 0));
		return multimap;
	}

	/*
	 * ===================================== FORGE START
	 * =================================
	 */
	private String toolClass;

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) {
		int level = super.getHarvestLevel(stack, toolClass);
		if (level == -1 && toolClass != null && toolClass.equals(this.toolClass))
			return toolMaterial.getHarvestLevel();
		else
			return level;
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack) {
		return toolClass != null ? ImmutableSet.of(toolClass) : super.getToolClasses(stack);
	}

	@Override
	public float getDigSpeed(ItemStack stack, Block block, int meta) {
		if (ForgeHooks.isToolEffective(stack, block, meta))
			return efficiencyOnProperMaterial;
		return super.getDigSpeed(stack, block, meta);
	}
	/*
	 * ===================================== FORGE END
	 * =================================
	 */
}