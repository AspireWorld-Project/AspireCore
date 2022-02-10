package net.minecraft.block;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityInteractEvent;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockFarmland extends Block {
	@SideOnly(Side.CLIENT)
	private IIcon field_149824_a;
	@SideOnly(Side.CLIENT)
	private IIcon field_149823_b;
	private static final String __OBFID = "CL_00000241";

	protected BlockFarmland() {
		super(Material.ground);
		setTickRandomly(true);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
		setLightOpacity(255);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_,
			int p_149668_4_) {
		return AxisAlignedBB.getBoundingBox(p_149668_2_ + 0, p_149668_3_ + 0, p_149668_4_ + 0, p_149668_2_ + 1,
				p_149668_3_ + 1, p_149668_4_ + 1);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 1 ? p_149691_2_ > 0 ? field_149824_a : field_149823_b
				: Blocks.dirt.getBlockTextureFromSide(p_149691_1_);
	}

	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
		if (!func_149821_m(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_)
				&& !p_149674_1_.canLightningStrikeAt(p_149674_2_, p_149674_3_ + 1, p_149674_4_)) {
			int l = p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_, p_149674_4_);

			if (l > 0) {
				p_149674_1_.setBlockMetadataWithNotify(p_149674_2_, p_149674_3_, p_149674_4_, l - 1, 2);
			} else if (!func_149822_e(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_)) {

				org.bukkit.block.Block block = p_149674_1_.getWorld().getBlockAt(p_149674_2_, p_149674_3_, p_149674_4_);
				if (CraftEventFactory.callBlockFadeEvent(block, Blocks.dirt).isCancelled())
					return;
				p_149674_1_.setBlock(p_149674_2_, p_149674_3_, p_149674_4_, Blocks.dirt);
			}
		} else {
			p_149674_1_.setBlockMetadataWithNotify(p_149674_2_, p_149674_3_, p_149674_4_, 7, 2);
		}
	}

	@Override
	public void onFallenUpon(World p_149746_1_, int p_149746_2_, int p_149746_3_, int p_149746_4_, Entity p_149746_5_,
			float p_149746_6_) {
		if (!p_149746_1_.isRemote && p_149746_1_.rand.nextFloat() < p_149746_6_ - 0.5F) {
			if (!(p_149746_5_ instanceof EntityPlayer)
					&& !p_149746_1_.getGameRules().getGameRuleBooleanValue("mobGriefing"))
				return;

			if (p_149746_5_ != null) {
				Cancellable cancellable = null;
				if (p_149746_5_ instanceof EntityPlayer) {
					cancellable = CraftEventFactory.callPlayerInteractEvent((EntityPlayer) p_149746_5_,
							org.bukkit.event.block.Action.PHYSICAL, p_149746_2_, p_149746_3_, p_149746_4_, -1, null);
				} else if (p_149746_1_.getWorld() != null) {
					cancellable = new EntityInteractEvent(p_149746_5_.getBukkitEntity(),
							p_149746_1_.getWorld().getBlockAt(p_149746_2_, p_149746_3_, p_149746_4_));
					Bukkit.getPluginManager().callEvent((EntityInteractEvent) cancellable);
				}
				if (cancellable != null && cancellable.isCancelled())
					return;
			}

			p_149746_1_.setBlock(p_149746_2_, p_149746_3_, p_149746_4_, Blocks.dirt);
		}
	}

	private boolean func_149822_e(World p_149822_1_, int p_149822_2_, int p_149822_3_, int p_149822_4_) {
		byte b0 = 0;

		for (int l = p_149822_2_ - b0; l <= p_149822_2_ + b0; ++l) {
			for (int i1 = p_149822_4_ - b0; i1 <= p_149822_4_ + b0; ++i1) {
				Block block = p_149822_1_.getBlock(l, p_149822_3_ + 1, i1);

				if (block instanceof IPlantable && canSustainPlant(p_149822_1_, p_149822_2_, p_149822_3_, p_149822_4_,
						ForgeDirection.UP, (IPlantable) block))
					return true;
			}
		}

		return false;
	}

	private boolean func_149821_m(World p_149821_1_, int p_149821_2_, int p_149821_3_, int p_149821_4_) {
		for (int l = p_149821_2_ - 4; l <= p_149821_2_ + 4; ++l) {
			for (int i1 = p_149821_3_; i1 <= p_149821_3_ + 1; ++i1) {
				for (int j1 = p_149821_4_ - 4; j1 <= p_149821_4_ + 4; ++j1) {
					if (p_149821_1_.getBlock(l, i1, j1).getMaterial() == Material.water)
						return true;
				}
			}
		}

		return false;
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		super.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_5_);
		Material material = p_149695_1_.getBlock(p_149695_2_, p_149695_3_ + 1, p_149695_4_).getMaterial();

		if (material.isSolid()) {
			p_149695_1_.setBlock(p_149695_2_, p_149695_3_, p_149695_4_, Blocks.dirt);
		}
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Blocks.dirt.getItemDropped(0, p_149650_2_, p_149650_3_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Item.getItemFromBlock(Blocks.dirt);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_149824_a = p_149651_1_.registerIcon(getTextureName() + "_wet");
		field_149823_b = p_149651_1_.registerIcon(getTextureName() + "_dry");
	}
}