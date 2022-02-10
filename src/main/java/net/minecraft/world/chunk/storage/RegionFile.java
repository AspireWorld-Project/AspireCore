package net.minecraft.world.chunk.storage;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import net.minecraft.server.MinecraftServer;

public class RegionFile {
	private static final byte[] emptySector = new byte[4096];
	private final File fileName;
	private RandomAccessFile dataFile;
	private final int[] offsets = new int[1024];
	private final int[] chunkTimestamps = new int[1024];
	private ArrayList sectorFree;
	private int sizeDelta;
	private long lastModified;
	private static final String __OBFID = "CL_00000381";

	public RegionFile(File p_i2001_1_) {
		fileName = p_i2001_1_;
		sizeDelta = 0;

		try {
			if (p_i2001_1_.exists()) {
				lastModified = p_i2001_1_.lastModified();
			}

			dataFile = new RandomAccessFile(p_i2001_1_, "rw");
			int i;

			if (dataFile.length() < 4096L) {
				for (i = 0; i < 1024; ++i) {
					dataFile.writeInt(0);
				}

				for (i = 0; i < 1024; ++i) {
					dataFile.writeInt(0);
				}

				sizeDelta += 8192;
			}

			if ((dataFile.length() & 4095L) != 0L) {
				for (i = 0; i < (dataFile.length() & 4095L); ++i) {
					dataFile.write(0);
				}
			}

			i = (int) dataFile.length() / 4096;
			sectorFree = new ArrayList(i);
			int j;

			for (j = 0; j < i; ++j) {
				sectorFree.add(Boolean.valueOf(true));
			}

			sectorFree.set(0, Boolean.valueOf(false));
			sectorFree.set(1, Boolean.valueOf(false));
			dataFile.seek(0L);
			int k;

			for (j = 0; j < 1024; ++j) {
				k = dataFile.readInt();
				offsets[j] = k;

				if (k != 0 && (k >> 8) + (k & 255) <= sectorFree.size()) {
					for (int l = 0; l < (k & 255); ++l) {
						sectorFree.set((k >> 8) + l, Boolean.valueOf(false));
					}
				}
			}

			for (j = 0; j < 1024; ++j) {
				k = dataFile.readInt();
				chunkTimestamps[j] = k;
			}
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}

	// This is a copy (sort of) of the method below it, make sure they stay in sync
	public synchronized boolean chunkExists(int x, int z) {
		if (outOfBounds(x, z))
			return false;

		// try
		// {
		int offset = getOffset(x, z);

		if (offset == 0)
			return false;

		int sectorNumber = offset >> 8;
		int numSectors = offset & 255;

		if (sectorNumber + numSectors > sectorFree.size())
			return false;

		// No IO operations in main thread
		// this.dataFile.seek((long)(sectorNumber * 4096));
		// int length = this.dataFile.readInt();
		//
		// if (length > 4096 * numSectors || length <= 0) return false;
		//
		// byte version = this.dataFile.readByte();
		//
		// if (version == 1 || version == 2) return true;
		// }
		// catch (IOException ioexception)
		// {
		// return false;
		// }
		//
		// return false;

		return true;
	}

	public synchronized DataInputStream getChunkDataInputStream(int p_76704_1_, int p_76704_2_) {
		if (outOfBounds(p_76704_1_, p_76704_2_))
			return null;
		else {
			try {
				int k = getOffset(p_76704_1_, p_76704_2_);

				if (k == 0)
					return null;
				else {
					int l = k >> 8;
					int i1 = k & 255;

					if (l + i1 > sectorFree.size())
						return null;
					else {
						dataFile.seek(l * 4096);
						int j1 = dataFile.readInt();

						if (j1 > 4096 * i1)
							return null;
						else if (j1 <= 0)
							return null;
						else {
							byte b0 = dataFile.readByte();
							byte[] abyte;

							if (b0 == 1) {
								abyte = new byte[j1 - 1];
								dataFile.read(abyte);
								return new DataInputStream(
										new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(abyte))));
							} else if (b0 == 2) {
								abyte = new byte[j1 - 1];
								dataFile.read(abyte);
								return new DataInputStream(new BufferedInputStream(
										new InflaterInputStream(new ByteArrayInputStream(abyte))));
							} else
								return null;
						}
					}
				}
			} catch (IOException ioexception) {
				return null;
			}
		}
	}

	public DataOutputStream getChunkDataOutputStream(int p_76710_1_, int p_76710_2_) {
		return outOfBounds(p_76710_1_, p_76710_2_) ? null
				: new DataOutputStream(new DeflaterOutputStream(new RegionFile.ChunkBuffer(p_76710_1_, p_76710_2_)));
	}

	protected synchronized void write(int p_76706_1_, int p_76706_2_, byte[] p_76706_3_, int p_76706_4_) {
		try {
			int l = getOffset(p_76706_1_, p_76706_2_);
			int i1 = l >> 8;
			int j1 = l & 255;
			int k1 = (p_76706_4_ + 5) / 4096 + 1;

			if (k1 >= 256)
				return;

			if (i1 != 0 && j1 == k1) {
				this.write(i1, p_76706_3_, p_76706_4_);
			} else {
				int l1;

				for (l1 = 0; l1 < j1; ++l1) {
					sectorFree.set(i1 + l1, Boolean.valueOf(true));
				}

				l1 = sectorFree.indexOf(Boolean.valueOf(true));
				int i2 = 0;
				int j2;

				if (l1 != -1) {
					for (j2 = l1; j2 < sectorFree.size(); ++j2) {
						if (i2 != 0) {
							if (((Boolean) sectorFree.get(j2)).booleanValue()) {
								++i2;
							} else {
								i2 = 0;
							}
						} else if (((Boolean) sectorFree.get(j2)).booleanValue()) {
							l1 = j2;
							i2 = 1;
						}

						if (i2 >= k1) {
							break;
						}
					}
				}

				if (i2 >= k1) {
					i1 = l1;
					setOffset(p_76706_1_, p_76706_2_, l1 << 8 | k1);

					for (j2 = 0; j2 < k1; ++j2) {
						sectorFree.set(i1 + j2, Boolean.valueOf(false));
					}

					this.write(i1, p_76706_3_, p_76706_4_);
				} else {
					dataFile.seek(dataFile.length());
					i1 = sectorFree.size();

					for (j2 = 0; j2 < k1; ++j2) {
						dataFile.write(emptySector);
						sectorFree.add(Boolean.valueOf(false));
					}

					sizeDelta += 4096 * k1;
					this.write(i1, p_76706_3_, p_76706_4_);
					setOffset(p_76706_1_, p_76706_2_, i1 << 8 | k1);
				}
			}

			setChunkTimestamp(p_76706_1_, p_76706_2_, (int) (MinecraftServer.getSystemTimeMillis() / 1000L));
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}

	private void write(int p_76712_1_, byte[] p_76712_2_, int p_76712_3_) throws IOException {
		dataFile.seek(p_76712_1_ * 4096);
		dataFile.writeInt(p_76712_3_ + 1);
		dataFile.writeByte(2);
		dataFile.write(p_76712_2_, 0, p_76712_3_);
	}

	private boolean outOfBounds(int p_76705_1_, int p_76705_2_) {
		return p_76705_1_ < 0 || p_76705_1_ >= 32 || p_76705_2_ < 0 || p_76705_2_ >= 32;
	}

	private int getOffset(int p_76707_1_, int p_76707_2_) {
		return offsets[p_76707_1_ + p_76707_2_ * 32];
	}

	public boolean isChunkSaved(int p_76709_1_, int p_76709_2_) {
		return getOffset(p_76709_1_, p_76709_2_) != 0;
	}

	private void setOffset(int p_76711_1_, int p_76711_2_, int p_76711_3_) throws IOException {
		offsets[p_76711_1_ + p_76711_2_ * 32] = p_76711_3_;
		dataFile.seek((p_76711_1_ + p_76711_2_ * 32) * 4);
		dataFile.writeInt(p_76711_3_);
	}

	private void setChunkTimestamp(int p_76713_1_, int p_76713_2_, int p_76713_3_) throws IOException {
		chunkTimestamps[p_76713_1_ + p_76713_2_ * 32] = p_76713_3_;
		dataFile.seek(4096 + (p_76713_1_ + p_76713_2_ * 32) * 4);
		dataFile.writeInt(p_76713_3_);
	}

	public synchronized void close() throws IOException {
		if (dataFile != null) {
			dataFile.close();
			dataFile = null;
		}
	}

	class ChunkBuffer extends ByteArrayOutputStream {
		private int chunkX;
		private int chunkZ;
		private static final String __OBFID = "CL_00000382";

		public ChunkBuffer(int p_i2000_2_, int p_i2000_3_) {
			super(8096);
			chunkX = p_i2000_2_;
			chunkZ = p_i2000_3_;
		}

		@Override
		public void close() throws IOException {
			RegionFile.this.write(chunkX, chunkZ, buf, count);
		}
	}
}