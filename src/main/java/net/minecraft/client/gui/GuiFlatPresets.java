package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.*;

@SideOnly(Side.CLIENT)
public class GuiFlatPresets extends GuiScreen {
	private static final RenderItem field_146437_a = new RenderItem();
	@SuppressWarnings("rawtypes")
	private static final List field_146431_f = new ArrayList();
	private final GuiCreateFlatWorld field_146432_g;
	private String field_146438_h;
	private String field_146439_i;
	private String field_146436_r;
	private GuiFlatPresets.ListSlot field_146435_s;
	private GuiButton field_146434_t;
	private GuiTextField field_146433_u;
	public GuiFlatPresets(GuiCreateFlatWorld p_i1049_1_) {
		field_146432_g = p_i1049_1_;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		field_146438_h = I18n.format("createWorld.customize.presets.title");
		field_146439_i = I18n.format("createWorld.customize.presets.share");
		field_146436_r = I18n.format("createWorld.customize.presets.list");
		field_146433_u = new GuiTextField(fontRendererObj, 50, 40, width - 100, 20);
		field_146435_s = new GuiFlatPresets.ListSlot();
		field_146433_u.setMaxStringLength(1230);
		field_146433_u.setText(field_146432_g.func_146384_e());
		buttonList.add(field_146434_t = new GuiButton(0, width / 2 - 155, height - 28, 150, 20,
				I18n.format("createWorld.customize.presets.select")));
		buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel")));
		func_146426_g();
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		field_146433_u.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (!field_146433_u.textboxKeyTyped(p_73869_1_, p_73869_2_)) {
			super.keyTyped(p_73869_1_, p_73869_2_);
		}
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 0 && func_146430_p()) {
			field_146432_g.func_146383_a(field_146433_u.getText());
			mc.displayGuiScreen(field_146432_g);
		} else if (p_146284_1_.id == 1) {
			mc.displayGuiScreen(field_146432_g);
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		field_146435_s.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		drawCenteredString(fontRendererObj, field_146438_h, width / 2, 8, 16777215);
		drawString(fontRendererObj, field_146439_i, 50, 30, 10526880);
		drawString(fontRendererObj, field_146436_r, 50, 70, 10526880);
		field_146433_u.drawTextBox();
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	@Override
	public void updateScreen() {
		field_146433_u.updateCursorCounter();
		super.updateScreen();
	}

	public void func_146426_g() {
		boolean flag = func_146430_p();
		field_146434_t.enabled = flag;
	}

	private boolean func_146430_p() {
		return field_146435_s.field_148175_k > -1 && field_146435_s.field_148175_k < field_146431_f.size()
				|| field_146433_u.getText().length() > 1;
	}

	private static void func_146425_a(String p_146425_0_, Item p_146425_1_, BiomeGenBase p_146425_2_,
			FlatLayerInfo... p_146425_3_) {
		func_146421_a(p_146425_0_, p_146425_1_, p_146425_2_, null, p_146425_3_);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void func_146421_a(String p_146421_0_, Item p_146421_1_, BiomeGenBase p_146421_2_, List p_146421_3_,
			FlatLayerInfo... p_146421_4_) {
		FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();

		for (int i = p_146421_4_.length - 1; i >= 0; --i) {
			flatgeneratorinfo.getFlatLayers().add(p_146421_4_[i]);
		}

		flatgeneratorinfo.setBiome(p_146421_2_.biomeID);
		flatgeneratorinfo.func_82645_d();

		if (p_146421_3_ != null) {
			Iterator iterator = p_146421_3_.iterator();

			while (iterator.hasNext()) {
				String s1 = (String) iterator.next();
				flatgeneratorinfo.getWorldFeatures().put(s1, new HashMap());
			}
		}

		field_146431_f.add(new GuiFlatPresets.LayerItem(p_146421_1_, p_146421_0_, flatgeneratorinfo.toString()));
	}

	static {
		func_146421_a("Classic Flat", Item.getItemFromBlock(Blocks.grass), BiomeGenBase.plains,
				Arrays.asList("village"), new FlatLayerInfo(1, Blocks.grass),
				new FlatLayerInfo(2, Blocks.dirt), new FlatLayerInfo(1, Blocks.bedrock));
		func_146421_a("Tunnelers' Dream", Item.getItemFromBlock(Blocks.stone), BiomeGenBase.extremeHills,
				Arrays.asList("biome_1", "dungeon", "decoration", "stronghold", "mineshaft"),
				new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(5, Blocks.dirt),
				new FlatLayerInfo(230, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
		func_146421_a("Water World", Item.getItemFromBlock(Blocks.flowing_water), BiomeGenBase.plains,
				Arrays.asList("village", "biome_1"),
				new FlatLayerInfo(90, Blocks.water), new FlatLayerInfo(5, Blocks.sand),
				new FlatLayerInfo(5, Blocks.dirt), new FlatLayerInfo(5, Blocks.stone),
				new FlatLayerInfo(1, Blocks.bedrock));
		func_146421_a("Overworld", Item.getItemFromBlock(Blocks.tallgrass), BiomeGenBase.plains,
				Arrays.asList("village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon",
						"lake", "lava_lake"),
				new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(3, Blocks.dirt),
				new FlatLayerInfo(59, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
		func_146421_a("Snowy Kingdom", Item.getItemFromBlock(Blocks.snow_layer), BiomeGenBase.icePlains,
				Arrays.asList("village", "biome_1"),
				new FlatLayerInfo(1, Blocks.snow_layer), new FlatLayerInfo(1, Blocks.grass),
				new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(59, Blocks.stone),
				new FlatLayerInfo(1, Blocks.bedrock));
		func_146421_a("Bottomless Pit", Items.feather, BiomeGenBase.plains,
				Arrays.asList("village", "biome_1"),
				new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(3, Blocks.dirt),
				new FlatLayerInfo(2, Blocks.cobblestone));
		func_146421_a("Desert", Item.getItemFromBlock(Blocks.sand), BiomeGenBase.desert,
				Arrays.asList(
						"village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon"),
				new FlatLayerInfo(8, Blocks.sand), new FlatLayerInfo(52, Blocks.sandstone),
				new FlatLayerInfo(3, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
		func_146425_a("Redstone Ready", Items.redstone, BiomeGenBase.desert,
				new FlatLayerInfo(52, Blocks.sandstone), new FlatLayerInfo(3, Blocks.stone),
				new FlatLayerInfo(1, Blocks.bedrock));
	}

	@SideOnly(Side.CLIENT)
	static class LayerItem {
		public Item field_148234_a;
		public String field_148232_b;
		public String field_148233_c;
		public LayerItem(Item p_i45022_1_, String p_i45022_2_, String p_i45022_3_) {
			field_148234_a = p_i45022_1_;
			field_148232_b = p_i45022_2_;
			field_148233_c = p_i45022_3_;
		}
	}

	@SideOnly(Side.CLIENT)
	class ListSlot extends GuiSlot {
		public int field_148175_k = -1;
		public ListSlot() {
			super(GuiFlatPresets.this.mc, GuiFlatPresets.this.width, GuiFlatPresets.this.height, 80,
					GuiFlatPresets.this.height - 37, 24);
		}

		private void func_148172_a(int p_148172_1_, int p_148172_2_, Item p_148172_3_) {
			func_148173_e(p_148172_1_ + 1, p_148172_2_ + 1);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.enableGUIStandardItemLighting();
			GuiFlatPresets.field_146437_a.renderItemIntoGUI(GuiFlatPresets.this.fontRendererObj,
					GuiFlatPresets.this.mc.getTextureManager(), new ItemStack(p_148172_3_, 1, 0), p_148172_1_ + 2,
					p_148172_2_ + 2);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		}

		private void func_148173_e(int p_148173_1_, int p_148173_2_) {
			func_148171_c(p_148173_1_, p_148173_2_, 0, 0);
		}

		private void func_148171_c(int p_148171_1_, int p_148171_2_, int p_148171_3_, int p_148171_4_) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GuiFlatPresets.this.mc.getTextureManager().bindTexture(Gui.statIcons);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(p_148171_1_ + 0, p_148171_2_ + 18, GuiFlatPresets.this.zLevel,
					(p_148171_3_ + 0) * 0.0078125F, (p_148171_4_ + 18) * 0.0078125F);
			tessellator.addVertexWithUV(p_148171_1_ + 18, p_148171_2_ + 18, GuiFlatPresets.this.zLevel,
					(p_148171_3_ + 18) * 0.0078125F, (p_148171_4_ + 18) * 0.0078125F);
			tessellator.addVertexWithUV(p_148171_1_ + 18, p_148171_2_ + 0, GuiFlatPresets.this.zLevel,
					(p_148171_3_ + 18) * 0.0078125F, (p_148171_4_ + 0) * 0.0078125F);
			tessellator.addVertexWithUV(p_148171_1_ + 0, p_148171_2_ + 0, GuiFlatPresets.this.zLevel,
					(p_148171_3_ + 0) * 0.0078125F, (p_148171_4_ + 0) * 0.0078125F);
			tessellator.draw();
		}

		@Override
		protected int getSize() {
			return GuiFlatPresets.field_146431_f.size();
		}

		@Override
		protected void elementClicked(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_) {
			field_148175_k = p_148144_1_;
			func_146426_g();
			field_146433_u.setText(((GuiFlatPresets.LayerItem) GuiFlatPresets.field_146431_f
					.get(field_146435_s.field_148175_k)).field_148233_c);
		}

		@Override
		protected boolean isSelected(int p_148131_1_) {
			return p_148131_1_ == field_148175_k;
		}

		@Override
		protected void drawBackground() {
		}

		@Override
		protected void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_, int p_148126_4_,
				Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_) {
			GuiFlatPresets.LayerItem layeritem = (GuiFlatPresets.LayerItem) GuiFlatPresets.field_146431_f
					.get(p_148126_1_);
			func_148172_a(p_148126_2_, p_148126_3_, layeritem.field_148234_a);
			GuiFlatPresets.this.fontRendererObj.drawString(layeritem.field_148232_b, p_148126_2_ + 18 + 5,
					p_148126_3_ + 6, 16777215);
		}
	}
}