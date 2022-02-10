package net.minecraft.client.resources;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FileResourcePack extends AbstractResourcePack implements Closeable {
	public static final Splitter entryNameSplitter = Splitter.on('/').omitEmptyStrings().limit(3);
	private ZipFile resourcePackZipFile;
	private static final String __OBFID = "CL_00001075";

	public FileResourcePack(File p_i1290_1_) {
		super(p_i1290_1_);
	}

	private ZipFile getResourcePackZipFile() throws IOException {
		if (resourcePackZipFile == null) {
			resourcePackZipFile = new ZipFile(resourcePackFile);
		}

		return resourcePackZipFile;
	}

	@Override
	protected InputStream getInputStreamByName(String p_110591_1_) throws IOException {
		ZipFile zipfile = getResourcePackZipFile();
		ZipEntry zipentry = zipfile.getEntry(p_110591_1_);

		if (zipentry == null)
			throw new ResourcePackFileNotFoundException(resourcePackFile, p_110591_1_);
		else
			return zipfile.getInputStream(zipentry);
	}

	@Override
	public boolean hasResourceName(String p_110593_1_) {
		try {
			return getResourcePackZipFile().getEntry(p_110593_1_) != null;
		} catch (IOException ioexception) {
			return false;
		}
	}

	@Override
	public Set getResourceDomains() {
		ZipFile zipfile;

		try {
			zipfile = getResourcePackZipFile();
		} catch (IOException ioexception) {
			return Collections.emptySet();
		}

		Enumeration enumeration = zipfile.entries();
		HashSet hashset = Sets.newHashSet();

		while (enumeration.hasMoreElements()) {
			ZipEntry zipentry = (ZipEntry) enumeration.nextElement();
			String s = zipentry.getName();

			if (s.startsWith("assets/")) {
				ArrayList arraylist = Lists.newArrayList(entryNameSplitter.split(s));

				if (arraylist.size() > 1) {
					String s1 = (String) arraylist.get(1);

					if (!s1.equals(s1.toLowerCase())) {
						logNameNotLowercase(s1);
					} else {
						hashset.add(s1);
					}
				}
			}
		}

		return hashset;
	}

	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	@Override
	public void close() throws IOException {
		if (resourcePackZipFile != null) {
			resourcePackZipFile.close();
			resourcePackZipFile = null;
		}
	}
}