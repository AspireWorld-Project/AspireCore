package net.minecraft.client.gui;

import com.google.common.collect.Maps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class MapItemRenderer {
	private static final ResourceLocation field_148253_a = new ResourceLocation("textures/map/map_icons.png");
	private final TextureManager field_148251_b;
	private final Map field_148252_c = Maps.newHashMap();
	private static final String __OBFID = "CL_00000663";

	public MapItemRenderer(TextureManager p_i45009_1_) {
		field_148251_b = p_i45009_1_;
	}

	public void func_148246_a(MapData p_148246_1_) {
		func_148248_b(p_148246_1_).func_148236_a();
	}

	public void func_148250_a(MapData p_148250_1_, boolean p_148250_2_) {
		func_148248_b(p_148250_1_).func_148237_a(p_148250_2_);
	}

	private MapItemRenderer.Instance func_148248_b(MapData p_148248_1_) {
		MapItemRenderer.Instance instance = (MapItemRenderer.Instance) field_148252_c.get(p_148248_1_.mapName);

		if (instance == null) {
			instance = new MapItemRenderer.Instance(p_148248_1_, null);
			field_148252_c.put(p_148248_1_.mapName, instance);
		}

		return instance;
	}

	public void func_148249_a() {
		Iterator iterator = field_148252_c.values().iterator();

		while (iterator.hasNext()) {
			MapItemRenderer.Instance instance = (MapItemRenderer.Instance) iterator.next();
			field_148251_b.deleteTexture(instance.field_148240_d);
		}

		field_148252_c.clear();
	}

	@SideOnly(Side.CLIENT)
	class Instance {
		private final MapData field_148242_b;
		private final DynamicTexture field_148243_c;
		private final ResourceLocation field_148240_d;
		private final int[] field_148241_e;
		private static final String __OBFID = "CL_00000665";

		private Instance(MapData p_i45007_2_) {
			field_148242_b = p_i45007_2_;
			field_148243_c = new DynamicTexture(128, 128);
			field_148241_e = field_148243_c.getTextureData();
			field_148240_d = field_148251_b.getDynamicTextureLocation("map/" + p_i45007_2_.mapName, field_148243_c);

			for (int i = 0; i < field_148241_e.length; ++i) {
				field_148241_e[i] = 0;
			}
		}

		private void func_148236_a() {
			for (int i = 0; i < 16384; ++i) {
				int j = field_148242_b.colors[i] & 255;

				if (j / 4 == 0) {
					field_148241_e[i] = (i + i / 128 & 1) * 8 + 16 << 24;
				} else {
					field_148241_e[i] = MapColor.mapColorArray[j / 4].func_151643_b(j & 3);
				}
			}

			field_148243_c.updateDynamicTexture();
		}

		private void func_148237_a(boolean p_148237_1_) {
			byte b0 = 0;
			byte b1 = 0;
			Tessellator tessellator = Tessellator.instance;
			float f = 0.0F;
			field_148251_b.bindTexture(field_148240_d);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(1, 771, 0, 1);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(b0 + 0 + f, b1 + 128 - f, -0.009999999776482582D, 0.0D, 1.0D);
			tessellator.addVertexWithUV(b0 + 128 - f, b1 + 128 - f, -0.009999999776482582D, 1.0D, 1.0D);
			tessellator.addVertexWithUV(b0 + 128 - f, b1 + 0 + f, -0.009999999776482582D, 1.0D, 0.0D);
			tessellator.addVertexWithUV(b0 + 0 + f, b1 + 0 + f, -0.009999999776482582D, 0.0D, 0.0D);
			tessellator.draw();
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_BLEND);
			field_148251_b.bindTexture(MapItemRenderer.field_148253_a);
			int i = 0;
			Iterator iterator = field_148242_b.playersVisibleOnMap.values().iterator();

			while (iterator.hasNext()) {
				MapData.MapCoord mapcoord = (MapData.MapCoord) iterator.next();

				if (!p_148237_1_ || mapcoord.iconSize == 1) {
					GL11.glPushMatrix();
					GL11.glTranslatef(b0 + mapcoord.centerX / 2.0F + 64.0F, b1 + mapcoord.centerZ / 2.0F + 64.0F,
							-0.02F);
					GL11.glRotatef(mapcoord.iconRotation * 360 / 16.0F, 0.0F, 0.0F, 1.0F);
					GL11.glScalef(4.0F, 4.0F, 3.0F);
					GL11.glTranslatef(-0.125F, 0.125F, 0.0F);
					float f1 = (mapcoord.iconSize % 4 + 0) / 4.0F;
					float f2 = (mapcoord.iconSize / 4 + 0) / 4.0F;
					float f3 = (mapcoord.iconSize % 4 + 1) / 4.0F;
					float f4 = (mapcoord.iconSize / 4 + 1) / 4.0F;
					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV(-1.0D, 1.0D, i * 0.001F, f1, f2);
					tessellator.addVertexWithUV(1.0D, 1.0D, i * 0.001F, f3, f2);
					tessellator.addVertexWithUV(1.0D, -1.0D, i * 0.001F, f3, f4);
					tessellator.addVertexWithUV(-1.0D, -1.0D, i * 0.001F, f1, f4);
					tessellator.draw();
					GL11.glPopMatrix();
					++i;
				}
			}

			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, 0.0F, -0.04F);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}

		Instance(MapData p_i45008_2_, Object p_i45008_3_) {
			this(p_i45008_2_);
		}
	}
}