package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ResourcePackRepository {
	protected static final FileFilter resourcePackFilter = new FileFilter() {
		private static final String __OBFID = "CL_00001088";

		@Override
		public boolean accept(File p_accept_1_) {
			boolean flag = p_accept_1_.isFile() && p_accept_1_.getName().endsWith(".zip");
			boolean flag1 = p_accept_1_.isDirectory() && new File(p_accept_1_, "pack.mcmeta").isFile();
			return flag || flag1;
		}
	};
	private final File dirResourcepacks;
	public final IResourcePack rprDefaultResourcePack;
	private final File field_148534_e;
	public final IMetadataSerializer rprMetadataSerializer;
	private IResourcePack field_148532_f;
	private boolean field_148533_g;
	private List repositoryEntriesAll = Lists.newArrayList();
	private List repositoryEntries = Lists.newArrayList();
	private static final String __OBFID = "CL_00001087";

	public ResourcePackRepository(File p_i45101_1_, File p_i45101_2_, IResourcePack p_i45101_3_,
			IMetadataSerializer p_i45101_4_, GameSettings p_i45101_5_) {
		dirResourcepacks = p_i45101_1_;
		field_148534_e = p_i45101_2_;
		rprDefaultResourcePack = p_i45101_3_;
		rprMetadataSerializer = p_i45101_4_;
		fixDirResourcepacks();
		updateRepositoryEntriesAll();
		Iterator iterator = p_i45101_5_.resourcePacks.iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			Iterator iterator1 = repositoryEntriesAll.iterator();

			while (iterator1.hasNext()) {
				ResourcePackRepository.Entry entry = (ResourcePackRepository.Entry) iterator1.next();

				if (entry.getResourcePackName().equals(s)) {
					repositoryEntries.add(entry);
					break;
				}
			}
		}
	}

	private void fixDirResourcepacks() {
		if (!dirResourcepacks.isDirectory()) {
			dirResourcepacks.delete();
			dirResourcepacks.mkdirs();
		}
	}

	private List getResourcePackFiles() {
		return dirResourcepacks.isDirectory() ? Arrays.asList(dirResourcepacks.listFiles(resourcePackFilter))
				: Collections.emptyList();
	}

	public void updateRepositoryEntriesAll() {
		ArrayList arraylist = Lists.newArrayList();
		Iterator iterator = getResourcePackFiles().iterator();

		while (iterator.hasNext()) {
			File file1 = (File) iterator.next();
			ResourcePackRepository.Entry entry = new ResourcePackRepository.Entry(file1, null);

			if (!repositoryEntriesAll.contains(entry)) {
				try {
					entry.updateResourcePack();
					arraylist.add(entry);
				} catch (Exception exception) {
					arraylist.remove(entry);
				}
			} else {
				int i = repositoryEntriesAll.indexOf(entry);

				if (i > -1 && i < repositoryEntriesAll.size()) {
					arraylist.add(repositoryEntriesAll.get(i));
				}
			}
		}

		repositoryEntriesAll.removeAll(arraylist);
		iterator = repositoryEntriesAll.iterator();

		while (iterator.hasNext()) {
			ResourcePackRepository.Entry entry1 = (ResourcePackRepository.Entry) iterator.next();
			entry1.closeResourcePack();
		}

		repositoryEntriesAll = arraylist;
	}

	public List getRepositoryEntriesAll() {
		return ImmutableList.copyOf(repositoryEntriesAll);
	}

	public List getRepositoryEntries() {
		return ImmutableList.copyOf(repositoryEntries);
	}

	public void func_148527_a(List p_148527_1_) {
		repositoryEntries.clear();
		repositoryEntries.addAll(p_148527_1_);
	}

	public File getDirResourcepacks() {
		return dirResourcepacks;
	}

	public void func_148526_a(String p_148526_1_) {
		String s1 = p_148526_1_.substring(p_148526_1_.lastIndexOf("/") + 1);

		if (s1.contains("?")) {
			s1 = s1.substring(0, s1.indexOf("?"));
		}

		if (s1.endsWith(".zip")) {
			File file1 = new File(field_148534_e, s1.replaceAll("\\W", ""));
			func_148529_f();
			func_148528_a(p_148526_1_, file1);
		}
	}

	private void func_148528_a(String p_148528_1_, File p_148528_2_) {
		HashMap hashmap = Maps.newHashMap();
		GuiScreenWorking guiscreenworking = new GuiScreenWorking();
		hashmap.put("X-Minecraft-Username", Minecraft.getMinecraft().getSession().getUsername());
		hashmap.put("X-Minecraft-UUID", Minecraft.getMinecraft().getSession().getPlayerID());
		hashmap.put("X-Minecraft-Version", "1.7.10");
		field_148533_g = true;
		Minecraft.getMinecraft().displayGuiScreen(guiscreenworking);
		HttpUtil.func_151223_a(p_148528_2_, p_148528_1_, new HttpUtil.DownloadListener() {
			private static final String __OBFID = "CL_00001089";

			@Override
			public void func_148522_a(File p_148522_1_) {
				if (field_148533_g) {
					field_148533_g = false;
					field_148532_f = new FileResourcePack(p_148522_1_);
					Minecraft.getMinecraft().scheduleResourcesRefresh();
				}
			}
		}, hashmap, 52428800, guiscreenworking, Minecraft.getMinecraft().getProxy());
	}

	public IResourcePack func_148530_e() {
		return field_148532_f;
	}

	public void func_148529_f() {
		field_148532_f = null;
		field_148533_g = false;
	}

	@SideOnly(Side.CLIENT)
	public class Entry {
		private final File resourcePackFile;
		private IResourcePack reResourcePack;
		private PackMetadataSection rePackMetadataSection;
		private BufferedImage texturePackIcon;
		private ResourceLocation locationTexturePackIcon;
		private static final String __OBFID = "CL_00001090";

		private Entry(File p_i1295_2_) {
			resourcePackFile = p_i1295_2_;
		}

		public void updateResourcePack() throws IOException {
			reResourcePack = resourcePackFile.isDirectory() ? new FolderResourcePack(resourcePackFile)
					: new FileResourcePack(resourcePackFile);
			rePackMetadataSection = (PackMetadataSection) reResourcePack.getPackMetadata(rprMetadataSerializer, "pack");

			try {
				texturePackIcon = reResourcePack.getPackImage();
			} catch (IOException ioexception) {
				;
			}

			if (texturePackIcon == null) {
				texturePackIcon = rprDefaultResourcePack.getPackImage();
			}

			closeResourcePack();
		}

		public void bindTexturePackIcon(TextureManager p_110518_1_) {
			if (locationTexturePackIcon == null) {
				locationTexturePackIcon = p_110518_1_.getDynamicTextureLocation("texturepackicon",
						new DynamicTexture(texturePackIcon));
			}

			p_110518_1_.bindTexture(locationTexturePackIcon);
		}

		public void closeResourcePack() {
			if (reResourcePack instanceof Closeable) {
				IOUtils.closeQuietly((Closeable) reResourcePack);
			}
		}

		public IResourcePack getResourcePack() {
			return reResourcePack;
		}

		public String getResourcePackName() {
			return reResourcePack.getPackName();
		}

		public String getTexturePackDescription() {
			return rePackMetadataSection == null
					? EnumChatFormatting.RED + "Invalid pack.mcmeta (or missing \'pack\' section)"
					: rePackMetadataSection.func_152805_a().getFormattedText();
		}

		@Override
		public boolean equals(Object p_equals_1_) {
			return this == p_equals_1_ ? true
					: p_equals_1_ instanceof ResourcePackRepository.Entry ? toString().equals(p_equals_1_.toString())
							: false;
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}

		@Override
		public String toString() {
			return String.format("%s:%s:%d", new Object[] { resourcePackFile.getName(),
					resourcePackFile.isDirectory() ? "folder" : "zip", Long.valueOf(resourcePackFile.lastModified()) });
		}

		Entry(File p_i1296_2_, Object p_i1296_3_) {
			this(p_i1296_2_);
		}
	}
}