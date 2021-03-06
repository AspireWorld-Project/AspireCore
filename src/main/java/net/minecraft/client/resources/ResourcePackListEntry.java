package net.minecraft.client.resources;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class ResourcePackListEntry implements GuiListExtended.IGuiListEntry {
	private static final ResourceLocation field_148316_c = new ResourceLocation("textures/gui/resource_packs.png");
	protected final Minecraft field_148317_a;
	protected final GuiScreenResourcePacks field_148315_b;
	private static final String __OBFID = "CL_00000821";

	public ResourcePackListEntry(GuiScreenResourcePacks p_i45051_1_) {
		field_148315_b = p_i45051_1_;
		field_148317_a = Minecraft.getMinecraft();
	}

	@Override
	public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_,
			Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_) {
		func_148313_c();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Gui.func_146110_a(p_148279_2_, p_148279_3_, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
		int i2;

		if ((field_148317_a.gameSettings.touchscreen || p_148279_9_) && func_148310_d()) {
			field_148317_a.getTextureManager().bindTexture(field_148316_c);
			Gui.drawRect(p_148279_2_, p_148279_3_, p_148279_2_ + 32, p_148279_3_ + 32, -1601138544);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			int l1 = p_148279_7_ - p_148279_2_;
			i2 = p_148279_8_ - p_148279_3_;

			if (func_148309_e()) {
				if (l1 < 32) {
					Gui.func_146110_a(p_148279_2_, p_148279_3_, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
				} else {
					Gui.func_146110_a(p_148279_2_, p_148279_3_, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
				}
			} else {
				if (func_148308_f()) {
					if (l1 < 16) {
						Gui.func_146110_a(p_148279_2_, p_148279_3_, 32.0F, 32.0F, 32, 32, 256.0F, 256.0F);
					} else {
						Gui.func_146110_a(p_148279_2_, p_148279_3_, 32.0F, 0.0F, 32, 32, 256.0F, 256.0F);
					}
				}

				if (func_148314_g()) {
					if (l1 < 32 && l1 > 16 && i2 < 16) {
						Gui.func_146110_a(p_148279_2_, p_148279_3_, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
					} else {
						Gui.func_146110_a(p_148279_2_, p_148279_3_, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
					}
				}

				if (func_148307_h()) {
					if (l1 < 32 && l1 > 16 && i2 > 16) {
						Gui.func_146110_a(p_148279_2_, p_148279_3_, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
					} else {
						Gui.func_146110_a(p_148279_2_, p_148279_3_, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
					}
				}
			}
		}

		String s = func_148312_b();
		i2 = field_148317_a.fontRenderer.getStringWidth(s);

		if (i2 > 157) {
			s = field_148317_a.fontRenderer.trimStringToWidth(s,
					157 - field_148317_a.fontRenderer.getStringWidth("...")) + "...";
		}

		field_148317_a.fontRenderer.drawStringWithShadow(s, p_148279_2_ + 32 + 2, p_148279_3_ + 1, 16777215);
		List list = field_148317_a.fontRenderer.listFormattedStringToWidth(func_148311_a(), 157);

		for (int j2 = 0; j2 < 2 && j2 < list.size(); ++j2) {
			field_148317_a.fontRenderer.drawStringWithShadow((String) list.get(j2), p_148279_2_ + 32 + 2,
					p_148279_3_ + 12 + 10 * j2, 8421504);
		}
	}

	protected abstract String func_148311_a();

	protected abstract String func_148312_b();

	protected abstract void func_148313_c();

	protected boolean func_148310_d() {
		return true;
	}

	protected boolean func_148309_e() {
		return !field_148315_b.func_146961_a(this);
	}

	protected boolean func_148308_f() {
		return field_148315_b.func_146961_a(this);
	}

	protected boolean func_148314_g() {
		List list = field_148315_b.func_146962_b(this);
		int i = list.indexOf(this);
		return i > 0 && ((ResourcePackListEntry) list.get(i - 1)).func_148310_d();
	}

	protected boolean func_148307_h() {
		List list = field_148315_b.func_146962_b(this);
		int i = list.indexOf(this);
		return i >= 0 && i < list.size() - 1 && ((ResourcePackListEntry) list.get(i + 1)).func_148310_d();
	}

	@Override
	public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_,
			int p_148278_6_) {
		if (func_148310_d() && p_148278_5_ <= 32) {
			if (func_148309_e()) {
				field_148315_b.func_146962_b(this).remove(this);
				field_148315_b.func_146963_h().add(0, this);
				return true;
			}

			if (p_148278_5_ < 16 && func_148308_f()) {
				field_148315_b.func_146962_b(this).remove(this);
				field_148315_b.func_146964_g().add(0, this);
				return true;
			}

			List list;
			int k1;

			if (p_148278_5_ > 16 && p_148278_6_ < 16 && func_148314_g()) {
				list = field_148315_b.func_146962_b(this);
				k1 = list.indexOf(this);
				list.remove(this);
				list.add(k1 - 1, this);
				return true;
			}

			if (p_148278_5_ > 16 && p_148278_6_ > 16 && func_148307_h()) {
				list = field_148315_b.func_146962_b(this);
				k1 = list.indexOf(this);
				list.remove(this);
				list.add(k1 + 1, this);
				return true;
			}
		}

		return false;
	}

	@Override
	public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_,
			int p_148277_6_) {
	}
}