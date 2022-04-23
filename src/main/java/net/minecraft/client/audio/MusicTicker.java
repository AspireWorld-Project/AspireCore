package net.minecraft.client.audio;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class MusicTicker implements IUpdatePlayerListBox {
	private final Random field_147679_a = new Random();
	private final Minecraft field_147677_b;
	private ISound field_147678_c;
	private int field_147676_d = 100;
	private static final String __OBFID = "CL_00001138";

	public MusicTicker(Minecraft p_i45112_1_) {
		field_147677_b = p_i45112_1_;
	}

	@Override
	public void update() {
		MusicTicker.MusicType musictype = field_147677_b.func_147109_W();

		if (field_147678_c != null) {
			if (!musictype.getMusicTickerLocation().equals(field_147678_c.getPositionedSoundLocation())) {
				field_147677_b.getSoundHandler().stopSound(field_147678_c);
				field_147676_d = MathHelper.getRandomIntegerInRange(field_147679_a, 0, musictype.func_148634_b() / 2);
			}

			if (!field_147677_b.getSoundHandler().isSoundPlaying(field_147678_c)) {
				field_147678_c = null;
				field_147676_d = Math.min(MathHelper.getRandomIntegerInRange(field_147679_a, musictype.func_148634_b(),
						musictype.func_148633_c()), field_147676_d);
			}
		}

		if (field_147678_c == null && field_147676_d-- <= 0) {
			field_147678_c = PositionedSoundRecord.func_147673_a(musictype.getMusicTickerLocation());
			field_147677_b.getSoundHandler().playSound(field_147678_c);
			field_147676_d = Integer.MAX_VALUE;
		}
	}

	@SideOnly(Side.CLIENT)
	public enum MusicType {
		MENU(new ResourceLocation("minecraft:music.menu"), 20, 600), GAME(new ResourceLocation("minecraft:music.game"),
				12000, 24000), CREATIVE(new ResourceLocation("minecraft:music.game.creative"), 1200, 3600), CREDITS(
						new ResourceLocation("minecraft:music.game.end.credits"), Integer.MAX_VALUE,
						Integer.MAX_VALUE), NETHER(new ResourceLocation("minecraft:music.game.nether"), 1200,
								3600), END_BOSS(new ResourceLocation("minecraft:music.game.end.dragon"), 0,
										0), END(new ResourceLocation("minecraft:music.game.end"), 6000, 24000);
		private final ResourceLocation field_148645_h;
		private final int field_148646_i;
		private final int field_148643_j;

		private static final String __OBFID = "CL_00001139";

		MusicType(ResourceLocation p_i45111_3_, int p_i45111_4_, int p_i45111_5_) {
			field_148645_h = p_i45111_3_;
			field_148646_i = p_i45111_4_;
			field_148643_j = p_i45111_5_;
		}

		public ResourceLocation getMusicTickerLocation() {
			return field_148645_h;
		}

		public int func_148634_b() {
			return field_148646_i;
		}

		public int func_148633_c() {
			return field_148643_j;
		}
	}
}