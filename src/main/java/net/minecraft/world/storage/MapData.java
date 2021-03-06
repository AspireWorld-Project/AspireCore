package net.minecraft.world.storage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import java.util.*;

public class MapData extends WorldSavedData {
	public int xCenter;
	public int zCenter;
	public int dimension;
	public byte scale;
	public byte[] colors = new byte[16384];
	@SuppressWarnings("rawtypes")
	public List playersArrayList = new ArrayList();
	@SuppressWarnings("rawtypes")
	private final Map playersHashMap = new HashMap();
	@SuppressWarnings("rawtypes")
	public Map playersVisibleOnMap = new LinkedHashMap();
	public MapData(String p_i2140_1_) {
		super(p_i2140_1_);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_76184_1_) {
		NBTBase dimension = p_76184_1_.getTag("dimension");

		if (dimension instanceof NBTTagByte) {
			this.dimension = ((NBTTagByte) dimension).func_150290_f();
		} else {
			this.dimension = ((NBTTagInt) dimension).func_150287_d();
		}

		xCenter = p_76184_1_.getInteger("xCenter");
		zCenter = p_76184_1_.getInteger("zCenter");
		scale = p_76184_1_.getByte("scale");

		if (scale < 0) {
			scale = 0;
		}

		if (scale > 4) {
			scale = 4;
		}

		short short1 = p_76184_1_.getShort("width");
		short short2 = p_76184_1_.getShort("height");

		if (short1 == 128 && short2 == 128) {
			colors = p_76184_1_.getByteArray("colors");
		} else {
			byte[] abyte = p_76184_1_.getByteArray("colors");
			colors = new byte[16384];
			int i = (128 - short1) / 2;
			int j = (128 - short2) / 2;

			for (int k = 0; k < short2; ++k) {
				int l = k + j;

				if (l >= 0 || l < 128) {
					for (int i1 = 0; i1 < short1; ++i1) {
						int j1 = i1 + i;

						if (j1 >= 0 || j1 < 128) {
							colors[j1 + l * 128] = abyte[i1 + k * short1];
						}
					}
				}
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound p_76187_1_) {
		p_76187_1_.setInteger("dimension", dimension);
		p_76187_1_.setInteger("xCenter", xCenter);
		p_76187_1_.setInteger("zCenter", zCenter);
		p_76187_1_.setByte("scale", scale);
		p_76187_1_.setShort("width", (short) 128);
		p_76187_1_.setShort("height", (short) 128);
		p_76187_1_.setByteArray("colors", colors);
	}

	@SuppressWarnings("unchecked")
	public void updateVisiblePlayers(EntityPlayer p_76191_1_, ItemStack p_76191_2_) {
		if (!playersHashMap.containsKey(p_76191_1_)) {
			MapData.MapInfo mapinfo = new MapData.MapInfo(p_76191_1_);
			playersHashMap.put(p_76191_1_, mapinfo);
			playersArrayList.add(mapinfo);
		}

		if (!p_76191_1_.inventory.hasItemStack(p_76191_2_)) {
			playersVisibleOnMap.remove(p_76191_1_.getCommandSenderName());
		}

		for (int i = 0; i < playersArrayList.size(); ++i) {
			MapData.MapInfo mapinfo1 = (MapData.MapInfo) playersArrayList.get(i);

			if (!mapinfo1.entityplayerObj.isDead
					&& (mapinfo1.entityplayerObj.inventory.hasItemStack(p_76191_2_) || p_76191_2_.isOnItemFrame())) {
				if (!p_76191_2_.isOnItemFrame() && mapinfo1.entityplayerObj.dimension == dimension) {
					func_82567_a(0, mapinfo1.entityplayerObj.worldObj, mapinfo1.entityplayerObj.getCommandSenderName(),
							mapinfo1.entityplayerObj.posX, mapinfo1.entityplayerObj.posZ,
							mapinfo1.entityplayerObj.rotationYaw);
				}
			} else {
				playersHashMap.remove(mapinfo1.entityplayerObj);
				playersArrayList.remove(mapinfo1);
			}
		}

		if (p_76191_2_.isOnItemFrame()) {
			func_82567_a(1, p_76191_1_.worldObj, "frame-" + p_76191_2_.getItemFrame().getEntityId(),
					p_76191_2_.getItemFrame().field_146063_b, p_76191_2_.getItemFrame().field_146062_d,
					p_76191_2_.getItemFrame().hangingDirection * 90);
		}
	}

	@SuppressWarnings("unchecked")
	private void func_82567_a(int p_82567_1_, World p_82567_2_, String p_82567_3_, double p_82567_4_, double p_82567_6_,
			double p_82567_8_) {
		int j = 1 << scale;
		float f = (float) (p_82567_4_ - xCenter) / j;
		float f1 = (float) (p_82567_6_ - zCenter) / j;
		byte b0 = (byte) (int) (f * 2.0F + 0.5D);
		byte b1 = (byte) (int) (f1 * 2.0F + 0.5D);
		byte b3 = 63;
		byte b2;

		if (f >= -b3 && f1 >= -b3 && f <= b3 && f1 <= b3) {
			p_82567_8_ += p_82567_8_ < 0.0D ? -8.0D : 8.0D;
			b2 = (byte) (int) (p_82567_8_ * 16.0D / 360.0D);

			if (p_82567_2_.provider.shouldMapSpin(p_82567_3_, p_82567_4_, p_82567_6_, p_82567_8_)) {
				int k = (int) (p_82567_2_.getWorldInfo().getWorldTime() / 10L);
				b2 = (byte) (k * k * 34187121 + k * 121 >> 15 & 15);
			}
		} else {
			if (Math.abs(f) >= 320.0F || Math.abs(f1) >= 320.0F) {
				playersVisibleOnMap.remove(p_82567_3_);
				return;
			}

			p_82567_1_ = 6;
			b2 = 0;

			if (f <= -b3) {
				b0 = (byte) (int) (b3 * 2 + 2.5D);
			}

			if (f1 <= -b3) {
				b1 = (byte) (int) (b3 * 2 + 2.5D);
			}

			if (f >= b3) {
				b0 = (byte) (b3 * 2 + 1);
			}

			if (f1 >= b3) {
				b1 = (byte) (b3 * 2 + 1);
			}
		}

		playersVisibleOnMap.put(p_82567_3_, new MapData.MapCoord((byte) p_82567_1_, b0, b1, b2));
	}

	public byte[] getUpdatePacketData(ItemStack p_76193_1_, World p_76193_2_, EntityPlayer p_76193_3_) {
		MapData.MapInfo mapinfo = (MapData.MapInfo) playersHashMap.get(p_76193_3_);
		return mapinfo == null ? null : mapinfo.getPlayersOnMap(p_76193_1_);
	}

	public void setColumnDirty(int p_76194_1_, int p_76194_2_, int p_76194_3_) {
		super.markDirty();

		for (int l = 0; l < playersArrayList.size(); ++l) {
			MapData.MapInfo mapinfo = (MapData.MapInfo) playersArrayList.get(l);

			if (mapinfo.field_76209_b[p_76194_1_] < 0 || mapinfo.field_76209_b[p_76194_1_] > p_76194_2_) {
				mapinfo.field_76209_b[p_76194_1_] = p_76194_2_;
			}

			if (mapinfo.field_76210_c[p_76194_1_] < 0 || mapinfo.field_76210_c[p_76194_1_] < p_76194_3_) {
				mapinfo.field_76210_c[p_76194_1_] = p_76194_3_;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	public void updateMPMapData(byte[] p_76192_1_) {
		int i;

		if (p_76192_1_[0] == 0) {
			i = p_76192_1_[1] & 255;
			int j = p_76192_1_[2] & 255;

			for (int k = 0; k < p_76192_1_.length - 3; ++k) {
				colors[(k + j) * 128 + i] = p_76192_1_[k + 3];
			}

			markDirty();
		} else if (p_76192_1_[0] == 1) {
			playersVisibleOnMap.clear();

			for (i = 0; i < (p_76192_1_.length - 1) / 3; ++i) {
				byte b2 = (byte) (p_76192_1_[i * 3 + 1] >> 4);
				byte b3 = p_76192_1_[i * 3 + 2];
				byte b0 = p_76192_1_[i * 3 + 3];
				byte b1 = (byte) (p_76192_1_[i * 3 + 1] & 15);
				playersVisibleOnMap.put("icon-" + i, new MapData.MapCoord(b2, b3, b0, b1));
			}
		} else if (p_76192_1_[0] == 2) {
			scale = p_76192_1_[1];
		}
	}

	@SuppressWarnings("unchecked")
	public MapData.MapInfo func_82568_a(EntityPlayer p_82568_1_) {
		MapData.MapInfo mapinfo = (MapData.MapInfo) playersHashMap.get(p_82568_1_);

		if (mapinfo == null) {
			mapinfo = new MapData.MapInfo(p_82568_1_);
			playersHashMap.put(p_82568_1_, mapinfo);
			playersArrayList.add(mapinfo);
		}

		return mapinfo;
	}

	public class MapCoord {
		public byte iconSize;
		public byte centerX;
		public byte centerZ;
		public byte iconRotation;
		public MapCoord(byte p_i2139_2_, byte p_i2139_3_, byte p_i2139_4_, byte p_i2139_5_) {
			iconSize = p_i2139_2_;
			centerX = p_i2139_3_;
			centerZ = p_i2139_4_;
			iconRotation = p_i2139_5_;
		}
	}

	public class MapInfo {
		public final EntityPlayer entityplayerObj;
		public int[] field_76209_b = new int[128];
		public int[] field_76210_c = new int[128];
		private int currentRandomNumber;
		private int ticksUntilPlayerLocationMapUpdate;
		private byte[] lastPlayerLocationOnMap;
		public int field_82569_d;
		private boolean field_82570_i;
		public MapInfo(EntityPlayer p_i2138_2_) {
			entityplayerObj = p_i2138_2_;

			for (int i = 0; i < field_76209_b.length; ++i) {
				field_76209_b[i] = 0;
				field_76210_c[i] = 127;
			}
		}

		@SuppressWarnings("rawtypes")
		public byte[] getPlayersOnMap(ItemStack p_76204_1_) {
			byte[] abyte;

			if (!field_82570_i) {
				abyte = new byte[] { (byte) 2, scale };
				field_82570_i = true;
				return abyte;
			} else {
				int i;
				int i1;

				if (--ticksUntilPlayerLocationMapUpdate < 0) {
					ticksUntilPlayerLocationMapUpdate = 4;
					abyte = new byte[playersVisibleOnMap.size() * 3 + 1];
					abyte[0] = 1;
					i = 0;

					for (Iterator iterator = playersVisibleOnMap.values().iterator(); iterator.hasNext(); ++i) {
						MapData.MapCoord mapcoord = (MapData.MapCoord) iterator.next();
						abyte[i * 3 + 1] = (byte) (mapcoord.iconSize << 4 | mapcoord.iconRotation & 15);
						abyte[i * 3 + 2] = mapcoord.centerX;
						abyte[i * 3 + 3] = mapcoord.centerZ;
					}

					boolean flag = !p_76204_1_.isOnItemFrame();

					if (lastPlayerLocationOnMap != null && lastPlayerLocationOnMap.length == abyte.length) {
						for (i1 = 0; i1 < abyte.length; ++i1) {
							if (abyte[i1] != lastPlayerLocationOnMap[i1]) {
								flag = false;
								break;
							}
						}
					} else {
						flag = false;
					}

					if (!flag) {
						lastPlayerLocationOnMap = abyte;
						return abyte;
					}
				}

				for (int k = 0; k < 1; ++k) {
					i = currentRandomNumber++ * 11 % 128;

					if (field_76209_b[i] >= 0) {
						int l = field_76210_c[i] - field_76209_b[i] + 1;
						i1 = field_76209_b[i];
						byte[] abyte1 = new byte[l + 3];
						abyte1[0] = 0;
						abyte1[1] = (byte) i;
						abyte1[2] = (byte) i1;

						for (int j = 0; j < abyte1.length - 3; ++j) {
							abyte1[j + 3] = colors[(j + i1) * 128 + i];
						}

						field_76210_c[i] = -1;
						field_76209_b[i] = -1;
						return abyte1;
					}
				}

				return null;
			}
		}
	}
}