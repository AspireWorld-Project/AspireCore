package net.minecraft.entity.item;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityPainting extends EntityHanging {
	public EntityPainting.EnumArt art;
	private static final String __OBFID = "CL_00001556";

	public EntityPainting(World p_i1599_1_) {
		super(p_i1599_1_);
	}

	public EntityPainting(World p_i1600_1_, int p_i1600_2_, int p_i1600_3_, int p_i1600_4_, int p_i1600_5_) {
		super(p_i1600_1_, p_i1600_2_, p_i1600_3_, p_i1600_4_, p_i1600_5_);
		ArrayList arraylist = new ArrayList();
		EntityPainting.EnumArt[] aenumart = EntityPainting.EnumArt.values();
		int i1 = aenumart.length;

		for (int j1 = 0; j1 < i1; ++j1) {
			EntityPainting.EnumArt enumart = aenumart[j1];
			art = enumart;
			setDirection(p_i1600_5_);

			if (onValidSurface()) {
				arraylist.add(enumart);
			}
		}

		if (!arraylist.isEmpty()) {
			art = (EntityPainting.EnumArt) arraylist.get(rand.nextInt(arraylist.size()));
		}

		setDirection(p_i1600_5_);
	}

	@SideOnly(Side.CLIENT)
	public EntityPainting(World p_i1601_1_, int p_i1601_2_, int p_i1601_3_, int p_i1601_4_, int p_i1601_5_,
			String p_i1601_6_) {
		this(p_i1601_1_, p_i1601_2_, p_i1601_3_, p_i1601_4_, p_i1601_5_);
		EntityPainting.EnumArt[] aenumart = EntityPainting.EnumArt.values();
		int i1 = aenumart.length;

		for (int j1 = 0; j1 < i1; ++j1) {
			EntityPainting.EnumArt enumart = aenumart[j1];

			if (enumart.title.equals(p_i1601_6_)) {
				art = enumart;
				break;
			}
		}

		setDirection(p_i1601_5_);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setString("Motive", art.title);
		super.writeEntityToNBT(p_70014_1_);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		String s = p_70037_1_.getString("Motive");
		EntityPainting.EnumArt[] aenumart = EntityPainting.EnumArt.values();
		int i = aenumart.length;

		for (int j = 0; j < i; ++j) {
			EntityPainting.EnumArt enumart = aenumart[j];

			if (enumart.title.equals(s)) {
				art = enumart;
			}
		}

		if (art == null) {
			art = EntityPainting.EnumArt.Kebab;
		}

		super.readEntityFromNBT(p_70037_1_);
	}

	@Override
	public int getWidthPixels() {
		return art.sizeX;
	}

	@Override
	public int getHeightPixels() {
		return art.sizeY;
	}

	@Override
	public void onBroken(Entity p_110128_1_) {
		if (p_110128_1_ instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) p_110128_1_;

			if (entityplayer.capabilities.isCreativeMode)
				return;
		}

		entityDropItem(new ItemStack(Items.painting), 0.0F);
	}

	public static enum EnumArt {
		Kebab("Kebab", 16, 16, 0, 0), Aztec("Aztec", 16, 16, 16, 0), Alban("Alban", 16, 16, 32, 0), Aztec2("Aztec2", 16,
				16, 48, 0), Bomb("Bomb", 16, 16, 64, 0), Plant("Plant", 16, 16, 80, 0), Wasteland("Wasteland", 16, 16,
						96, 0), Pool("Pool", 32, 16, 0, 32), Courbet("Courbet", 32, 16, 32, 32), Sea("Sea", 32, 16, 64,
								32), Sunset("Sunset", 32, 16, 96, 32), Creebet("Creebet", 32, 16, 128, 32), Wanderer(
										"Wanderer", 16, 32, 0,
										64), Graham("Graham", 16, 32, 16, 64), Match("Match", 32, 32, 0, 128), Bust(
												"Bust", 32, 32, 32,
												128), Stage("Stage", 32, 32, 64, 128), Void("Void", 32, 32, 96,
														128), SkullAndRoses("SkullAndRoses", 32, 32, 128, 128), Wither(
																"Wither", 32, 32, 160,
																128), Fighters("Fighters", 64, 32, 0, 96), Pointer(
																		"Pointer", 64, 64, 0, 192), Pigscene("Pigscene",
																				64, 64, 64,
																				192), BurningSkull("BurningSkull", 64,
																						64, 128, 192), Skeleton(
																								"Skeleton", 64, 48, 192,
																								64), DonkeyKong(
																										"DonkeyKong",
																										64, 48, 192,
																										112);
		public static final int maxArtTitleLength = "SkullAndRoses".length();
		public final String title;
		public final int sizeX;
		public final int sizeY;
		public final int offsetX;
		public final int offsetY;

		private static final String __OBFID = "CL_00001557";

		private EnumArt(String p_i1598_3_, int p_i1598_4_, int p_i1598_5_, int p_i1598_6_, int p_i1598_7_) {
			title = p_i1598_3_;
			sizeX = p_i1598_4_;
			sizeY = p_i1598_5_;
			offsetX = p_i1598_6_;
			offsetY = p_i1598_7_;
		}
	}
}