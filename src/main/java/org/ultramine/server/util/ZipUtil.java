package org.ultramine.server.util;

import com.google.common.base.Function;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	public static void zip(File zipfile, File directory) throws IOException {
		zipAll(zipfile, directory.getParentFile(), Arrays.asList(directory.getName()));
	}

	public static void zipAll(File zipfile, File parent, Collection<String> directories) throws IOException {
		if (directories.size() == 0)
			return;
		URI base = parent.toURI();
		Deque<File> queue = new LinkedList<>();
		for (String dir : directories) {
			queue.push(new File(parent, dir));
		}
		OutputStream out = new BufferedOutputStream(new FileOutputStream(zipfile), 65536);
		Closeable res = null;
		try {
			ZipOutputStream zout = new ZipOutputStream(out);
			res = zout;
			byte[] buffer = new byte[65536];
			while (!queue.isEmpty()) {
				File directory = queue.pop();
				if (!directory.isDirectory()) {
					continue;
				}
				for (File kid : directory.listFiles()) {
					String name = base.relativize(kid.toURI()).getPath();
					if (kid.isDirectory()) {
						queue.push(kid);
						name = name.endsWith("/") ? name : name + "/";
						zout.putNextEntry(new ZipEntry(name));
					} else {
						zout.putNextEntry(new ZipEntry(name));
						FileInputStream fin = null;
						try {
							fin = new FileInputStream(kid);
							IOUtils.copyLarge(fin, zout, buffer);
						} catch (FileNotFoundException ignored) {

						} finally {
							IOUtils.closeQuietly(fin);
							zout.closeEntry();
						}
					}
				}
			}
		} finally {
			IOUtils.closeQuietly(res);
		}
	}

	public static void unzip(File zipfile, File outDir) throws IOException {
		unzip(zipfile, outDir, null);
	}

	public static void unzip(File zipfile, File outDir, Function<String, String> filter) throws IOException {
		ZipFile zip = null;
		try {
			zip = new ZipFile(zipfile);

			byte[] buffer = new byte[65536];
			for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements();) {
				ZipEntry ze = e.nextElement();
				String name = ze.getName();
				if (filter != null) {
					name = filter.apply(name);
				}
				if (name == null) {
					continue;
				}
				File target = new File(outDir, name);
				if (ze.isDirectory()) {
					target.mkdirs();
				} else {
					FileOutputStream fout = null;
					InputStream inp = null;
					try {
						fout = new FileOutputStream(target);
						IOUtils.copyLarge(inp = zip.getInputStream(ze), fout, buffer);
					} finally {
						IOUtils.closeQuietly(fout);
						IOUtils.closeQuietly(inp);
					}
				}
			}
		} finally {
			IOUtils.closeQuietly(zip);
		}
	}

	public static Set<String> getRootFiles(File zipfile) throws IOException {
		Set<String> set = new HashSet<>();
		ZipFile zip = new ZipFile(zipfile);
		try {
			for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements();) {
				ZipEntry ze = e.nextElement();
				String name = ze.getName();
				if (ze.isDirectory()) {
					set.add(name.substring(0, name.indexOf('/')));
				}
			}
		} finally {
			IOUtils.closeQuietly(zip);
		}

		return set;
	}

	public static Set<String> getRootFiles(ZipFile zip) {
		Set<String> set = new HashSet<>();
		for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements();) {
			ZipEntry ze = e.nextElement();
			String name = ze.getName();
			if (ze.isDirectory()) {
				set.add(name.substring(0, name.indexOf('/')));
			}
		}

		return set;
	}
}
