package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockPumpkin extends BlockDirectional {
	private final boolean field_149985_a;
	@SideOnly(Side.CLIENT)
	private IIcon field_149984_b;
	@SideOnly(Side.CLIENT)
	private IIcon field_149986_M;
	protected BlockPumpkin(boolean p_i45419_1_) {
		super(Material.gourd);
		setTickRandomly(true);
		field_149985_a = p_i45419_1_;
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 1 ? field_149984_b
				: p_149691_1_ == 0 ? field_149984_b
						: p_149691_2_ == 2 && p_149691_1_ == 2 ? field_149986_M
								: p_149691_2_ == 3 && p_149691_1_ == 5 ? field_149986_M
										: p_149691_2_ == 0 && p_149691_1_ == 3 ? field_149986_M
												: p_149691_2_ == 1 && p_149691_1_ == 4 ? field_149986_M : blockIcon;
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
		super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);

		if (p_149726_1_.getBlock(p_149726_2_, p_149726_3_ - 1, p_149726_4_) == Blocks.snow
				&& p_149726_1_.getBlock(p_149726_2_, p_149726_3_ - 2, p_149726_4_) == Blocks.snow) {
			if (!p_149726_1_.isRemote) {
				p_149726_1_.setBlock(p_149726_2_, p_149726_3_, p_149726_4_, getBlockById(0), 0, 2);
				p_149726_1_.setBlock(p_149726_2_, p_149726_3_ - 1, p_149726_4_, getBlockById(0), 0, 2);
				p_149726_1_.setBlock(p_149726_2_, p_149726_3_ - 2, p_149726_4_, getBlockById(0), 0, 2);
				EntitySnowman entitysnowman = new EntitySnowman(p_149726_1_);
				entitysnowman.setLocationAndAngles(p_149726_2_ + 0.5D, p_149726_3_ - 1.95D, p_149726_4_ + 0.5D, 0.0F,
						0.0F);
				p_149726_1_.spawnEntityInWorld(entitysnowman);
				p_149726_1_.notifyBlockChange(p_149726_2_, p_149726_3_, p_149726_4_, getBlockById(0));
				p_149726_1_.notifyBlockChange(p_149726_2_, p_149726_3_ - 1, p_149726_4_, getBlockById(0));
				p_149726_1_.notifyBlockChange(p_149726_2_, p_149726_3_ - 2, p_149726_4_, getBlockById(0));
			}

			for (int i1 = 0; i1 < 120; ++i1) {
				p_149726_1_.spawnParticle("snowshovel", p_149726_2_ + p_149726_1_.rand.nextDouble(),
						p_149726_3_ - 2 + p_149726_1_.rand.nextDouble() * 2.5D,
						p_149726_4_ + p_149726_1_.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
			}
		} else if (p_149726_1_.getBlock(p_149726_2_, p_149726_3_ - 1, p_149726_4_) == Blocks.iron_block
				&& p_149726_1_.getBlock(p_149726_2_, p_149726_3_ - 2, p_149726_4_) == Blocks.iron_block) {
			boolean flag = p_149726_1_.getBlock(p_149726_2_ - 1, p_149726_3_ - 1, p_149726_4_) == Blocks.iron_block
					&& p_149726_1_.getBlock(p_149726_2_ + 1, p_149726_3_ - 1, p_149726_4_) == Blocks.iron_block;
			boolean flag1 = p_149726_1_.getBlock(p_149726_2_, p_149726_3_ - 1, p_149726_4_ - 1) == Blocks.iron_block
					&& p_149726_1_.getBlock(p_149726_2_, p_149726_3_ - 1, p_149726_4_ + 1) == Blocks.iron_block;

			if (flag || flag1) {
				p_149726_1_.setBlock(p_149726_2_, p_149726_3_, p_149726_4_, getBlockById(0), 0, 2);
				p_149726_1_.setBlock(p_149726_2_, p_149726_3_ - 1, p_149726_4_, getBlockById(0), 0, 2);
				p_149726_1_.setBlock(p_149726_2_, p_149726_3_ - 2, p_149726_4_, getBlockById(0), 0, 2);

				if (flag) {
					p_149726_1_.setBlock(p_149726_2_ - 1, p_149726_3_ - 1, p_149726_4_, getBlockById(0), 0, 2);
					p_149726_1_.setBlock(p_149726_2_ + 1, p_149726_3_ - 1, p_149726_4_, getBlockById(0), 0, 2);
				} else {
					p_149726_1_.setBlock(p_149726_2_, p_149726_3_ - 1, p_149726_4_ - 1, getBlockById(0), 0, 2);
					p_149726_1_.setBlock(p_149726_2_, p_149726_3_ - 1, p_149726_4_ + 1, getBlockById(0), 0, 2);
				}

				EntityIronGolem entityirongolem = new EntityIronGolem(p_149726_1_);
				entityirongolem.setPlayerCreated(true);
				entityirongolem.setLocationAndAngles(p_149726_2_ + 0.5D, p_149726_3_ - 1.95D, p_149726_4_ + 0.5D, 0.0F,
						0.0F);
				p_149726_1_.spawnEntityInWorld(entityirongolem);

				for (int l = 0; l < 120; ++l) {
					p_149726_1_.spawnParticle("snowballpoof", p_149726_2_ + p_149726_1_.rand.nextDouble(),
							p_149726_3_ - 2 + p_149726_1_.rand.nextDouble() * 3.9D,
							p_149726_4_ + p_149726_1_.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
				}

				p_149726_1_.notifyBlockChange(p_149726_2_, p_149726_3_, p_149726_4_, getBlockById(0));
				p_149726_1_.notifyBlockChange(p_149726_2_, p_149726_3_ - 1, p_149726_4_, getBlockById(0));
				p_149726_1_.notifyBlockChange(p_149726_2_, p_149726_3_ - 2, p_149726_4_, getBlockById(0));

				if (flag) {
					p_149726_1_.notifyBlockChange(p_149726_2_ - 1, p_149726_3_ - 1, p_149726_4_, getBlockById(0));
					p_149726_1_.notifyBlockChange(p_149726_2_ + 1, p_149726_3_ - 1, p_149726_4_, getBlockById(0));
				} else {
					p_149726_1_.notifyBlockChange(p_149726_2_, p_149726_3_ - 1, p_149726_4_ - 1, getBlockById(0));
					p_149726_1_.notifyBlockChange(p_149726_2_, p_149726_3_ - 1, p_149726_4_ + 1, getBlockById(0));
				}
			}
		}
	}

	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_) {
		return p_149742_1_.getBlock(p_149742_2_, p_149742_3_, p_149742_4_).isReplaceable(p_149742_1_, p_149742_2_,
				p_149742_3_, p_149742_4_)
				&& World.doesBlockHaveSolidTopSurface(p_149742_1_, p_149742_2_, p_149742_3_ - 1, p_149742_4_);
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_,
			EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		int l = MathHelper.floor_double(p_149689_5_.rotationYaw * 4.0F / 360.0F + 2.5D) & 3;
		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_149986_M = p_149651_1_.registerIcon(getTextureName() + "_face_" + (field_149985_a ? "on" : "off"));
		field_149984_b = p_149651_1_.registerIcon(getTextureName() + "_top");
		blockIcon = p_149651_1_.registerIcon(getTextureName() + "_side");
	}
}