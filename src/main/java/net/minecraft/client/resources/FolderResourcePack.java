package net.minecraft.client.resources;

import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class FolderResourcePack extends AbstractResourcePack {
	private static final String __OBFID = "CL_00001076";

	public FolderResourcePack(File p_i1291_1_) {
		super(p_i1291_1_);
	}

	@Override
	protected InputStream getInputStreamByName(String p_110591_1_) throws IOException {
		return new BufferedInputStream(new FileInputStream(new File(resourcePackFile, p_110591_1_)));
	}

	@Override
	protected boolean hasResourceName(String p_110593_1_) {
		return new File(resourcePackFile, p_110593_1_).isFile();
	}

	@Override
	public Set getResourceDomains() {
		HashSet hashset = Sets.newHashSet();
		File file1 = new File(resourcePackFile, "assets/");

		if (file1.isDirectory()) {
			File[] afile = file1.listFiles((java.io.FileFilter) DirectoryFileFilter.DIRECTORY);
			int i = afile.length;

			for (int j = 0; j < i; ++j) {
				File file2 = afile[j];
				String s = getRelativeName(file1, file2);

				if (!s.equals(s.toLowerCase())) {
					logNameNotLowercase(s);
				} else {
					hashset.add(s.substring(0, s.length() - 1));
				}
			}
		}

		return hashset;
	}
}