package net.minecraft.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.Map.Entry;

@SideOnly(Side.CLIENT)
public class GLAllocation {
	private static final Map mapDisplayLists = new HashMap();
	private static final List listDummy = new ArrayList();
	private static final String __OBFID = "CL_00000630";

	public static synchronized int generateDisplayLists(int p_74526_0_) {
		int j = GL11.glGenLists(p_74526_0_);
		mapDisplayLists.put(Integer.valueOf(j), Integer.valueOf(p_74526_0_));
		return j;
	}

	public static synchronized void deleteDisplayLists(int p_74523_0_) {
		GL11.glDeleteLists(p_74523_0_, ((Integer) mapDisplayLists.remove(Integer.valueOf(p_74523_0_))).intValue());
	}

	public static synchronized void deleteTexturesAndDisplayLists() {
		Iterator iterator = mapDisplayLists.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();
			GL11.glDeleteLists(((Integer) entry.getKey()).intValue(), ((Integer) entry.getValue()).intValue());
		}

		mapDisplayLists.clear();
	}

	public static synchronized ByteBuffer createDirectByteBuffer(int p_74524_0_) {
		return ByteBuffer.allocateDirect(p_74524_0_).order(ByteOrder.nativeOrder());
	}

	public static IntBuffer createDirectIntBuffer(int p_74527_0_) {
		return createDirectByteBuffer(p_74527_0_ << 2).asIntBuffer();
	}

	public static FloatBuffer createDirectFloatBuffer(int p_74529_0_) {
		return createDirectByteBuffer(p_74529_0_ << 2).asFloatBuffer();
	}
}