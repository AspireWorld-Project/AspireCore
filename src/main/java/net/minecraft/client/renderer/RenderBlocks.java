package net.minecraft.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererChestHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.src.FMLRenderAccessLibrary;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static net.minecraftforge.common.util.ForgeDirection.*;

@SideOnly(Side.CLIENT)
public class RenderBlocks {
	public IBlockAccess blockAccess;
	public IIcon overrideBlockTexture;
	public boolean flipTexture;
	public boolean field_152631_f;
	public boolean renderAllFaces;
	public static boolean fancyGrass = true;
	public boolean useInventoryTint = true;
	public boolean renderFromInside = false;
	public double renderMinX;
	public double renderMaxX;
	public double renderMinY;
	public double renderMaxY;
	public double renderMinZ;
	public double renderMaxZ;
	public boolean lockBlockBounds;
	public boolean partialRenderBounds;
	public final Minecraft minecraftRB;
	public int uvRotateEast;
	public int uvRotateWest;
	public int uvRotateSouth;
	public int uvRotateNorth;
	public int uvRotateTop;
	public int uvRotateBottom;
	public boolean enableAO;
	public float aoLightValueScratchXYZNNN;
	public float aoLightValueScratchXYNN;
	public float aoLightValueScratchXYZNNP;
	public float aoLightValueScratchYZNN;
	public float aoLightValueScratchYZNP;
	public float aoLightValueScratchXYZPNN;
	public float aoLightValueScratchXYPN;
	public float aoLightValueScratchXYZPNP;
	public float aoLightValueScratchXYZNPN;
	public float aoLightValueScratchXYNP;
	public float aoLightValueScratchXYZNPP;
	public float aoLightValueScratchYZPN;
	public float aoLightValueScratchXYZPPN;
	public float aoLightValueScratchXYPP;
	public float aoLightValueScratchYZPP;
	public float aoLightValueScratchXYZPPP;
	public float aoLightValueScratchXZNN;
	public float aoLightValueScratchXZPN;
	public float aoLightValueScratchXZNP;
	public float aoLightValueScratchXZPP;
	public int aoBrightnessXYZNNN;
	public int aoBrightnessXYNN;
	public int aoBrightnessXYZNNP;
	public int aoBrightnessYZNN;
	public int aoBrightnessYZNP;
	public int aoBrightnessXYZPNN;
	public int aoBrightnessXYPN;
	public int aoBrightnessXYZPNP;
	public int aoBrightnessXYZNPN;
	public int aoBrightnessXYNP;
	public int aoBrightnessXYZNPP;
	public int aoBrightnessYZPN;
	public int aoBrightnessXYZPPN;
	public int aoBrightnessXYPP;
	public int aoBrightnessYZPP;
	public int aoBrightnessXYZPPP;
	public int aoBrightnessXZNN;
	public int aoBrightnessXZPN;
	public int aoBrightnessXZNP;
	public int aoBrightnessXZPP;
	public int brightnessTopLeft;
	public int brightnessBottomLeft;
	public int brightnessBottomRight;
	public int brightnessTopRight;
	public float colorRedTopLeft;
	public float colorRedBottomLeft;
	public float colorRedBottomRight;
	public float colorRedTopRight;
	public float colorGreenTopLeft;
	public float colorGreenBottomLeft;
	public float colorGreenBottomRight;
	public float colorGreenTopRight;
	public float colorBlueTopLeft;
	public float colorBlueBottomLeft;
	public float colorBlueBottomRight;
	public float colorBlueTopRight;
	private static final String __OBFID = "CL_00000940";

	public RenderBlocks(IBlockAccess p_i1251_1_) {
		blockAccess = p_i1251_1_;
		field_152631_f = false;
		flipTexture = false;
		minecraftRB = Minecraft.getMinecraft();
	}

	public RenderBlocks() {
		minecraftRB = Minecraft.getMinecraft();
	}

	public void setOverrideBlockTexture(IIcon p_147757_1_) {
		overrideBlockTexture = p_147757_1_;
	}

	public void clearOverrideBlockTexture() {
		overrideBlockTexture = null;
	}

	public boolean hasOverrideBlockTexture() {
		return overrideBlockTexture != null;
	}

	public void setRenderFromInside(boolean p_147786_1_) {
		renderFromInside = p_147786_1_;
	}

	public void setRenderAllFaces(boolean p_147753_1_) {
		renderAllFaces = p_147753_1_;
	}

	public void setRenderBounds(double p_147782_1_, double p_147782_3_, double p_147782_5_, double p_147782_7_,
			double p_147782_9_, double p_147782_11_) {
		if (!lockBlockBounds) {
			renderMinX = p_147782_1_;
			renderMaxX = p_147782_7_;
			renderMinY = p_147782_3_;
			renderMaxY = p_147782_9_;
			renderMinZ = p_147782_5_;
			renderMaxZ = p_147782_11_;
			partialRenderBounds = minecraftRB.gameSettings.ambientOcclusion >= 2
					&& (renderMinX > 0.0D || renderMaxX < 1.0D || renderMinY > 0.0D || renderMaxY < 1.0D
							|| renderMinZ > 0.0D || renderMaxZ < 1.0D);
		}
	}

	public void setRenderBoundsFromBlock(Block p_147775_1_) {
		if (!lockBlockBounds) {
			renderMinX = p_147775_1_.getBlockBoundsMinX();
			renderMaxX = p_147775_1_.getBlockBoundsMaxX();
			renderMinY = p_147775_1_.getBlockBoundsMinY();
			renderMaxY = p_147775_1_.getBlockBoundsMaxY();
			renderMinZ = p_147775_1_.getBlockBoundsMinZ();
			renderMaxZ = p_147775_1_.getBlockBoundsMaxZ();
			partialRenderBounds = minecraftRB.gameSettings.ambientOcclusion >= 2
					&& (renderMinX > 0.0D || renderMaxX < 1.0D || renderMinY > 0.0D || renderMaxY < 1.0D
							|| renderMinZ > 0.0D || renderMaxZ < 1.0D);
		}
	}

	public void overrideBlockBounds(double p_147770_1_, double p_147770_3_, double p_147770_5_, double p_147770_7_,
			double p_147770_9_, double p_147770_11_) {
		renderMinX = p_147770_1_;
		renderMaxX = p_147770_7_;
		renderMinY = p_147770_3_;
		renderMaxY = p_147770_9_;
		renderMinZ = p_147770_5_;
		renderMaxZ = p_147770_11_;
		lockBlockBounds = true;
		partialRenderBounds = minecraftRB.gameSettings.ambientOcclusion >= 2 && (renderMinX > 0.0D || renderMaxX < 1.0D
				|| renderMinY > 0.0D || renderMaxY < 1.0D || renderMinZ > 0.0D || renderMaxZ < 1.0D);
	}

	public void unlockBlockBounds() {
		lockBlockBounds = false;
	}

	public void renderBlockUsingTexture(Block p_147792_1_, int p_147792_2_, int p_147792_3_, int p_147792_4_,
			IIcon p_147792_5_) {
		setOverrideBlockTexture(p_147792_5_);
		renderBlockByRenderType(p_147792_1_, p_147792_2_, p_147792_3_, p_147792_4_);
		clearOverrideBlockTexture();
	}

	public void renderBlockAllFaces(Block p_147769_1_, int p_147769_2_, int p_147769_3_, int p_147769_4_) {
		renderAllFaces = true;
		renderBlockByRenderType(p_147769_1_, p_147769_2_, p_147769_3_, p_147769_4_);
		renderAllFaces = false;
	}

	public boolean renderBlockByRenderType(Block p_147805_1_, int p_147805_2_, int p_147805_3_, int p_147805_4_) {
		int l = p_147805_1_.getRenderType();

		if (l == -1)
			return false;
		else {
			p_147805_1_.setBlockBoundsBasedOnState(blockAccess, p_147805_2_, p_147805_3_, p_147805_4_);
			setRenderBoundsFromBlock(p_147805_1_);

			switch (l) {
			// regex: ' : \(l == ([\d]+) \?' replace: ';\ncase \1: return' ::: IMPORTANT:
			// REMEMBER THIS ON FIRST line!
			case 0:
				return renderStandardBlock(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 4:
				return renderBlockLiquid(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 31:
				return renderBlockLog(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 1:
				return renderCrossedSquares(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 40:
				return renderBlockDoublePlant((BlockDoublePlant) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 2:
				return renderBlockTorch(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 20:
				return renderBlockVine(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 11:
				return renderBlockFence((BlockFence) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 39:
				return renderBlockQuartz(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 5:
				return renderBlockRedstoneWire(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 13:
				return renderBlockCactus(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 9:
				return renderBlockMinecartTrack((BlockRailBase) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 19:
				return renderBlockStem(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 23:
				return renderBlockLilyPad(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 6:
				return renderBlockCrops(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 3:
				return renderBlockFire((BlockFire) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 8:
				return renderBlockLadder(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 7:
				return renderBlockDoor(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 10:
				return renderBlockStairs((BlockStairs) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 27:
				return renderBlockDragonEgg((BlockDragonEgg) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 32:
				return renderBlockWall((BlockWall) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 12:
				return renderBlockLever(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 29:
				return renderBlockTripWireSource(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 30:
				return renderBlockTripWire(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 14:
				return renderBlockBed(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 15:
				return renderBlockRepeater((BlockRedstoneRepeater) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 36:
				return renderBlockRedstoneDiode((BlockRedstoneDiode) p_147805_1_, p_147805_2_, p_147805_3_,
						p_147805_4_);
			case 37:
				return renderBlockRedstoneComparator((BlockRedstoneComparator) p_147805_1_, p_147805_2_, p_147805_3_,
						p_147805_4_);
			case 16:
				return renderPistonBase(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_, false);
			case 17:
				return renderPistonExtension(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_, true);
			case 18:
				return renderBlockPane((BlockPane) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 41:
				return renderBlockStainedGlassPane(p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 21:
				return renderBlockFenceGate((BlockFenceGate) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 24:
				return renderBlockCauldron((BlockCauldron) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 33:
				return renderBlockFlowerpot((BlockFlowerPot) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 35:
				return renderBlockAnvil((BlockAnvil) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 25:
				return renderBlockBrewingStand((BlockBrewingStand) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 26:
				return renderBlockEndPortalFrame((BlockEndPortalFrame) p_147805_1_, p_147805_2_, p_147805_3_,
						p_147805_4_);
			case 28:
				return renderBlockCocoa((BlockCocoa) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 34:
				return renderBlockBeacon((BlockBeacon) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			case 38:
				return renderBlockHopper((BlockHopper) p_147805_1_, p_147805_2_, p_147805_3_, p_147805_4_);
			default:
				return FMLRenderAccessLibrary.renderWorldBlock(this, blockAccess, p_147805_2_, p_147805_3_, p_147805_4_,
						p_147805_1_, l);
			}
		}
	}

	public boolean renderBlockEndPortalFrame(BlockEndPortalFrame p_147743_1_, int p_147743_2_, int p_147743_3_,
			int p_147743_4_) {
		int l = blockAccess.getBlockMetadata(p_147743_2_, p_147743_3_, p_147743_4_);
		int i1 = l & 3;

		if (i1 == 0) {
			uvRotateTop = 3;
		} else if (i1 == 3) {
			uvRotateTop = 1;
		} else if (i1 == 1) {
			uvRotateTop = 2;
		}

		if (!BlockEndPortalFrame.isEnderEyeInserted(l)) {
			setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
			renderStandardBlock(p_147743_1_, p_147743_2_, p_147743_3_, p_147743_4_);
			uvRotateTop = 0;
			return true;
		} else {
			renderAllFaces = true;
			setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
			renderStandardBlock(p_147743_1_, p_147743_2_, p_147743_3_, p_147743_4_);
			setOverrideBlockTexture(p_147743_1_.getIconEndPortalFrameEye());
			setRenderBounds(0.25D, 0.8125D, 0.25D, 0.75D, 1.0D, 0.75D);
			renderStandardBlock(p_147743_1_, p_147743_2_, p_147743_3_, p_147743_4_);
			renderAllFaces = false;
			clearOverrideBlockTexture();
			uvRotateTop = 0;
			return true;
		}
	}

	public boolean renderBlockBed(Block p_147773_1_, int p_147773_2_, int p_147773_3_, int p_147773_4_) {
		Tessellator tessellator = Tessellator.instance;
		Block bed = blockAccess.getBlock(p_147773_2_, p_147773_3_, p_147773_4_);
		int i1 = bed.getBedDirection(blockAccess, p_147773_2_, p_147773_3_, p_147773_4_);
		boolean flag = bed.isBedFoot(blockAccess, p_147773_2_, p_147773_3_, p_147773_4_);
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		int j1 = p_147773_1_.getMixedBrightnessForBlock(blockAccess, p_147773_2_, p_147773_3_, p_147773_4_);
		tessellator.setBrightness(j1);
		tessellator.setColorOpaque_F(f, f, f);
		IIcon iicon = this.getBlockIcon(p_147773_1_, blockAccess, p_147773_2_, p_147773_3_, p_147773_4_, 0);
		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture; // BugFix Proper breaking texture on underside
		}
		double d0 = iicon.getMinU();
		double d1 = iicon.getMaxU();
		double d2 = iicon.getMinV();
		double d3 = iicon.getMaxV();
		double d4 = p_147773_2_ + renderMinX;
		double d5 = p_147773_2_ + renderMaxX;
		double d6 = p_147773_3_ + renderMinY + 0.1875D;
		double d7 = p_147773_4_ + renderMinZ;
		double d8 = p_147773_4_ + renderMaxZ;
		tessellator.addVertexWithUV(d4, d6, d8, d0, d3);
		tessellator.addVertexWithUV(d4, d6, d7, d0, d2);
		tessellator.addVertexWithUV(d5, d6, d7, d1, d2);
		tessellator.addVertexWithUV(d5, d6, d8, d1, d3);
		tessellator.setBrightness(
				p_147773_1_.getMixedBrightnessForBlock(blockAccess, p_147773_2_, p_147773_3_ + 1, p_147773_4_));
		tessellator.setColorOpaque_F(f1, f1, f1);
		iicon = this.getBlockIcon(p_147773_1_, blockAccess, p_147773_2_, p_147773_3_, p_147773_4_, 1);
		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture; // BugFix Proper breaking texture on underside
		}
		d0 = iicon.getMinU();
		d1 = iicon.getMaxU();
		d2 = iicon.getMinV();
		d3 = iicon.getMaxV();
		d4 = d0;
		d5 = d1;
		d6 = d2;
		d7 = d2;
		d8 = d0;
		double d9 = d1;
		double d10 = d3;
		double d11 = d3;

		if (i1 == 0) {
			d5 = d0;
			d6 = d3;
			d8 = d1;
			d11 = d2;
		} else if (i1 == 2) {
			d4 = d1;
			d7 = d3;
			d9 = d0;
			d10 = d2;
		} else if (i1 == 3) {
			d4 = d1;
			d7 = d3;
			d9 = d0;
			d10 = d2;
			d5 = d0;
			d6 = d3;
			d8 = d1;
			d11 = d2;
		}

		double d12 = p_147773_2_ + renderMinX;
		double d13 = p_147773_2_ + renderMaxX;
		double d14 = p_147773_3_ + renderMaxY;
		double d15 = p_147773_4_ + renderMinZ;
		double d16 = p_147773_4_ + renderMaxZ;
		tessellator.addVertexWithUV(d13, d14, d16, d8, d10);
		tessellator.addVertexWithUV(d13, d14, d15, d4, d6);
		tessellator.addVertexWithUV(d12, d14, d15, d5, d7);
		tessellator.addVertexWithUV(d12, d14, d16, d9, d11);
		int k1 = Direction.directionToFacing[i1];

		if (flag) {
			k1 = Direction.directionToFacing[Direction.rotateOpposite[i1]];
		}

		byte b0 = 4;

		switch (i1) {
		case 0:
			b0 = 5;
			break;
		case 1:
			b0 = 3;
		case 2:
		default:
			break;
		case 3:
			b0 = 2;
		}

		if (k1 != 2 && (renderAllFaces
				|| p_147773_1_.shouldSideBeRendered(blockAccess, p_147773_2_, p_147773_3_, p_147773_4_ - 1, 2))) {
			tessellator.setBrightness(renderMinZ > 0.0D ? j1
					: p_147773_1_.getMixedBrightnessForBlock(blockAccess, p_147773_2_, p_147773_3_, p_147773_4_ - 1));
			tessellator.setColorOpaque_F(f2, f2, f2);
			flipTexture = b0 == 2;
			renderFaceZNeg(p_147773_1_, p_147773_2_, p_147773_3_, p_147773_4_,
					this.getBlockIcon(p_147773_1_, blockAccess, p_147773_2_, p_147773_3_, p_147773_4_, 2));
		}

		if (k1 != 3 && (renderAllFaces
				|| p_147773_1_.shouldSideBeRendered(blockAccess, p_147773_2_, p_147773_3_, p_147773_4_ + 1, 3))) {
			tessellator.setBrightness(renderMaxZ < 1.0D ? j1
					: p_147773_1_.getMixedBrightnessForBlock(blockAccess, p_147773_2_, p_147773_3_, p_147773_4_ + 1));
			tessellator.setColorOpaque_F(f2, f2, f2);
			flipTexture = b0 == 3;
			renderFaceZPos(p_147773_1_, p_147773_2_, p_147773_3_, p_147773_4_,
					this.getBlockIcon(p_147773_1_, blockAccess, p_147773_2_, p_147773_3_, p_147773_4_, 3));
		}

		if (k1 != 4 && (renderAllFaces
				|| p_147773_1_.shouldSideBeRendered(blockAccess, p_147773_2_ - 1, p_147773_3_, p_147773_4_, 4))) {
			tessellator.setBrightness(renderMinZ > 0.0D ? j1
					: p_147773_1_.getMixedBrightnessForBlock(blockAccess, p_147773_2_ - 1, p_147773_3_, p_147773_4_));
			tessellator.setColorOpaque_F(f3, f3, f3);
			flipTexture = b0 == 4;
			renderFaceXNeg(p_147773_1_, p_147773_2_, p_147773_3_, p_147773_4_,
					this.getBlockIcon(p_147773_1_, blockAccess, p_147773_2_, p_147773_3_, p_147773_4_, 4));
		}

		if (k1 != 5 && (renderAllFaces
				|| p_147773_1_.shouldSideBeRendered(blockAccess, p_147773_2_ + 1, p_147773_3_, p_147773_4_, 5))) {
			tessellator.setBrightness(renderMaxZ < 1.0D ? j1
					: p_147773_1_.getMixedBrightnessForBlock(blockAccess, p_147773_2_ + 1, p_147773_3_, p_147773_4_));
			tessellator.setColorOpaque_F(f3, f3, f3);
			flipTexture = b0 == 5;
			renderFaceXPos(p_147773_1_, p_147773_2_, p_147773_3_, p_147773_4_,
					this.getBlockIcon(p_147773_1_, blockAccess, p_147773_2_, p_147773_3_, p_147773_4_, 5));
		}

		flipTexture = false;
		return true;
	}

	public boolean renderBlockBrewingStand(BlockBrewingStand p_147741_1_, int p_147741_2_, int p_147741_3_,
			int p_147741_4_) {
		setRenderBounds(0.4375D, 0.0D, 0.4375D, 0.5625D, 0.875D, 0.5625D);
		renderStandardBlock(p_147741_1_, p_147741_2_, p_147741_3_, p_147741_4_);
		setOverrideBlockTexture(p_147741_1_.getIconBrewingStandBase());
		renderAllFaces = true;
		setRenderBounds(0.5625D, 0.0D, 0.3125D, 0.9375D, 0.125D, 0.6875D);
		renderStandardBlock(p_147741_1_, p_147741_2_, p_147741_3_, p_147741_4_);
		setRenderBounds(0.125D, 0.0D, 0.0625D, 0.5D, 0.125D, 0.4375D);
		renderStandardBlock(p_147741_1_, p_147741_2_, p_147741_3_, p_147741_4_);
		setRenderBounds(0.125D, 0.0D, 0.5625D, 0.5D, 0.125D, 0.9375D);
		renderStandardBlock(p_147741_1_, p_147741_2_, p_147741_3_, p_147741_4_);
		renderAllFaces = false;
		clearOverrideBlockTexture();
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(
				p_147741_1_.getMixedBrightnessForBlock(blockAccess, p_147741_2_, p_147741_3_, p_147741_4_));
		int l = p_147741_1_.colorMultiplier(blockAccess, p_147741_2_, p_147741_3_, p_147741_4_);
		float f = (l >> 16 & 255) / 255.0F;
		float f1 = (l >> 8 & 255) / 255.0F;
		float f2 = (l & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		tessellator.setColorOpaque_F(f, f1, f2);
		IIcon iicon = getBlockIconFromSideAndMetadata(p_147741_1_, 0, 0);

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
		}

		double d8 = iicon.getMinV();
		double d0 = iicon.getMaxV();
		int i1 = blockAccess.getBlockMetadata(p_147741_2_, p_147741_3_, p_147741_4_);

		for (int j1 = 0; j1 < 3; ++j1) {
			double d1 = j1 * Math.PI * 2.0D / 3.0D + Math.PI / 2D;
			double d2 = iicon.getInterpolatedU(8.0D);
			double d3 = iicon.getMaxU();

			if ((i1 & 1 << j1) != 0) {
				d3 = iicon.getMinU();
			}

			double d4 = p_147741_2_ + 0.5D;
			double d5 = p_147741_2_ + 0.5D + Math.sin(d1) * 8.0D / 16.0D;
			double d6 = p_147741_4_ + 0.5D;
			double d7 = p_147741_4_ + 0.5D + Math.cos(d1) * 8.0D / 16.0D;
			tessellator.addVertexWithUV(d4, p_147741_3_ + 1, d6, d2, d8);
			tessellator.addVertexWithUV(d4, p_147741_3_ + 0, d6, d2, d0);
			tessellator.addVertexWithUV(d5, p_147741_3_ + 0, d7, d3, d0);
			tessellator.addVertexWithUV(d5, p_147741_3_ + 1, d7, d3, d8);
			tessellator.addVertexWithUV(d5, p_147741_3_ + 1, d7, d3, d8);
			tessellator.addVertexWithUV(d5, p_147741_3_ + 0, d7, d3, d0);
			tessellator.addVertexWithUV(d4, p_147741_3_ + 0, d6, d2, d0);
			tessellator.addVertexWithUV(d4, p_147741_3_ + 1, d6, d2, d8);
		}

		p_147741_1_.setBlockBoundsForItemRender();
		return true;
	}

	public boolean renderBlockCauldron(BlockCauldron p_147785_1_, int p_147785_2_, int p_147785_3_, int p_147785_4_) {
		renderStandardBlock(p_147785_1_, p_147785_2_, p_147785_3_, p_147785_4_);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(
				p_147785_1_.getMixedBrightnessForBlock(blockAccess, p_147785_2_, p_147785_3_, p_147785_4_));
		int l = p_147785_1_.colorMultiplier(blockAccess, p_147785_2_, p_147785_3_, p_147785_4_);
		float f = (l >> 16 & 255) / 255.0F;
		float f1 = (l >> 8 & 255) / 255.0F;
		float f2 = (l & 255) / 255.0F;
		float f4;

		if (EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		tessellator.setColorOpaque_F(f, f1, f2);
		IIcon iicon1 = p_147785_1_.getBlockTextureFromSide(2);
		f4 = 0.125F;
		renderFaceXPos(p_147785_1_, p_147785_2_ - 1.0F + f4, p_147785_3_, p_147785_4_, iicon1);
		renderFaceXNeg(p_147785_1_, p_147785_2_ + 1.0F - f4, p_147785_3_, p_147785_4_, iicon1);
		renderFaceZPos(p_147785_1_, p_147785_2_, p_147785_3_, p_147785_4_ - 1.0F + f4, iicon1);
		renderFaceZNeg(p_147785_1_, p_147785_2_, p_147785_3_, p_147785_4_ + 1.0F - f4, iicon1);
		IIcon iicon2 = BlockCauldron.getCauldronIcon("inner");
		renderFaceYPos(p_147785_1_, p_147785_2_, p_147785_3_ - 1.0F + 0.25F, p_147785_4_, iicon2);
		renderFaceYNeg(p_147785_1_, p_147785_2_, p_147785_3_ + 1.0F - 0.75F, p_147785_4_, iicon2);
		int i1 = blockAccess.getBlockMetadata(p_147785_2_, p_147785_3_, p_147785_4_);

		if (i1 > 0) {
			IIcon iicon = BlockLiquid.getLiquidIcon("water_still");
			renderFaceYPos(p_147785_1_, p_147785_2_, p_147785_3_ - 1.0F + BlockCauldron.getRenderLiquidLevel(i1),
					p_147785_4_, iicon);
		}

		return true;
	}

	public boolean renderBlockFlowerpot(BlockFlowerPot p_147752_1_, int p_147752_2_, int p_147752_3_, int p_147752_4_) {
		renderStandardBlock(p_147752_1_, p_147752_2_, p_147752_3_, p_147752_4_);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(
				p_147752_1_.getMixedBrightnessForBlock(blockAccess, p_147752_2_, p_147752_3_, p_147752_4_));
		int l = p_147752_1_.colorMultiplier(blockAccess, p_147752_2_, p_147752_3_, p_147752_4_);
		IIcon iicon = getBlockIconFromSide(p_147752_1_, 0);
		float f = (l >> 16 & 255) / 255.0F;
		float f1 = (l >> 8 & 255) / 255.0F;
		float f2 = (l & 255) / 255.0F;
		float f3;

		if (EntityRenderer.anaglyphEnable) {
			f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		tessellator.setColorOpaque_F(f, f1, f2);
		f3 = 0.1865F;
		renderFaceXPos(p_147752_1_, p_147752_2_ - 0.5F + f3, p_147752_3_, p_147752_4_, iicon);
		renderFaceXNeg(p_147752_1_, p_147752_2_ + 0.5F - f3, p_147752_3_, p_147752_4_, iicon);
		renderFaceZPos(p_147752_1_, p_147752_2_, p_147752_3_, p_147752_4_ - 0.5F + f3, iicon);
		renderFaceZNeg(p_147752_1_, p_147752_2_, p_147752_3_, p_147752_4_ + 0.5F - f3, iicon);
		renderFaceYPos(p_147752_1_, p_147752_2_, p_147752_3_ - 0.5F + f3 + 0.1875F, p_147752_4_,
				this.getBlockIcon(Blocks.dirt));
		TileEntity tileentity = blockAccess.getTileEntity(p_147752_2_, p_147752_3_, p_147752_4_);

		if (tileentity != null && tileentity instanceof TileEntityFlowerPot) {
			Item item = ((TileEntityFlowerPot) tileentity).getFlowerPotItem();
			int i1 = ((TileEntityFlowerPot) tileentity).getFlowerPotData();

			if (item instanceof ItemBlock) {
				Block block = Block.getBlockFromItem(item);
				int j1 = block.getRenderType();
				float f6 = 0.0F;
				float f7 = 4.0F;
				float f8 = 0.0F;
				tessellator.addTranslation(f6 / 16.0F, f7 / 16.0F, f8 / 16.0F);
				l = block.colorMultiplier(blockAccess, p_147752_2_, p_147752_3_, p_147752_4_);

				if (l != 16777215) {
					f = (l >> 16 & 255) / 255.0F;
					f1 = (l >> 8 & 255) / 255.0F;
					f2 = (l & 255) / 255.0F;
					tessellator.setColorOpaque_F(f, f1, f2);
				}

				if (j1 == 1) {
					drawCrossedSquares(getBlockIconFromSideAndMetadata(block, 0, i1), p_147752_2_, p_147752_3_,
							p_147752_4_, 0.75F);
				} else if (j1 == 13) {
					renderAllFaces = true;
					float f9 = 0.125F;
					setRenderBounds(0.5F - f9, 0.0D, 0.5F - f9, 0.5F + f9, 0.25D, 0.5F + f9);
					renderStandardBlock(block, p_147752_2_, p_147752_3_, p_147752_4_);
					setRenderBounds(0.5F - f9, 0.25D, 0.5F - f9, 0.5F + f9, 0.5D, 0.5F + f9);
					renderStandardBlock(block, p_147752_2_, p_147752_3_, p_147752_4_);
					setRenderBounds(0.5F - f9, 0.5D, 0.5F - f9, 0.5F + f9, 0.75D, 0.5F + f9);
					renderStandardBlock(block, p_147752_2_, p_147752_3_, p_147752_4_);
					renderAllFaces = false;
					setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
				}

				tessellator.addTranslation(-f6 / 16.0F, -f7 / 16.0F, -f8 / 16.0F);
			}
		}

		return true;
	}

	public boolean renderBlockAnvil(BlockAnvil p_147725_1_, int p_147725_2_, int p_147725_3_, int p_147725_4_) {
		return renderBlockAnvilMetadata(p_147725_1_, p_147725_2_, p_147725_3_, p_147725_4_,
				blockAccess.getBlockMetadata(p_147725_2_, p_147725_3_, p_147725_4_));
	}

	public boolean renderBlockAnvilMetadata(BlockAnvil p_147780_1_, int p_147780_2_, int p_147780_3_, int p_147780_4_,
			int p_147780_5_) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(
				p_147780_1_.getMixedBrightnessForBlock(blockAccess, p_147780_2_, p_147780_3_, p_147780_4_));
		int i1 = p_147780_1_.colorMultiplier(blockAccess, p_147780_2_, p_147780_3_, p_147780_4_);
		float f = (i1 >> 16 & 255) / 255.0F;
		float f1 = (i1 >> 8 & 255) / 255.0F;
		float f2 = (i1 & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		tessellator.setColorOpaque_F(f, f1, f2);
		return renderBlockAnvilOrient(p_147780_1_, p_147780_2_, p_147780_3_, p_147780_4_, p_147780_5_, false);
	}

	public boolean renderBlockAnvilOrient(BlockAnvil p_147728_1_, int p_147728_2_, int p_147728_3_, int p_147728_4_,
			int p_147728_5_, boolean p_147728_6_) {
		int i1 = p_147728_6_ ? 0 : p_147728_5_ & 3;
		boolean flag1 = false;
		float f = 0.0F;

		switch (i1) {
		case 0:
			uvRotateSouth = 2;
			uvRotateNorth = 1;
			uvRotateTop = 3;
			uvRotateBottom = 3;
			break;
		case 1:
			uvRotateEast = 1;
			uvRotateWest = 2;
			uvRotateTop = 2;
			uvRotateBottom = 1;
			flag1 = true;
			break;
		case 2:
			uvRotateSouth = 1;
			uvRotateNorth = 2;
			break;
		case 3:
			uvRotateEast = 2;
			uvRotateWest = 1;
			uvRotateTop = 1;
			uvRotateBottom = 2;
			flag1 = true;
		}

		f = renderBlockAnvilRotate(p_147728_1_, p_147728_2_, p_147728_3_, p_147728_4_, 0, f, 0.75F, 0.25F, 0.75F, flag1,
				p_147728_6_, p_147728_5_);
		f = renderBlockAnvilRotate(p_147728_1_, p_147728_2_, p_147728_3_, p_147728_4_, 1, f, 0.5F, 0.0625F, 0.625F,
				flag1, p_147728_6_, p_147728_5_);
		f = renderBlockAnvilRotate(p_147728_1_, p_147728_2_, p_147728_3_, p_147728_4_, 2, f, 0.25F, 0.3125F, 0.5F,
				flag1, p_147728_6_, p_147728_5_);
		renderBlockAnvilRotate(p_147728_1_, p_147728_2_, p_147728_3_, p_147728_4_, 3, f, 0.625F, 0.375F, 1.0F, flag1,
				p_147728_6_, p_147728_5_);
		setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		uvRotateEast = 0;
		uvRotateWest = 0;
		uvRotateSouth = 0;
		uvRotateNorth = 0;
		uvRotateTop = 0;
		uvRotateBottom = 0;
		return true;
	}

	public float renderBlockAnvilRotate(BlockAnvil p_147737_1_, int p_147737_2_, int p_147737_3_, int p_147737_4_,
			int p_147737_5_, float p_147737_6_, float p_147737_7_, float p_147737_8_, float p_147737_9_,
			boolean p_147737_10_, boolean p_147737_11_, int p_147737_12_) {
		if (p_147737_10_) {
			float f4 = p_147737_7_;
			p_147737_7_ = p_147737_9_;
			p_147737_9_ = f4;
		}

		p_147737_7_ /= 2.0F;
		p_147737_9_ /= 2.0F;
		p_147737_1_.anvilRenderSide = p_147737_5_;
		setRenderBounds(0.5F - p_147737_7_, p_147737_6_, 0.5F - p_147737_9_, 0.5F + p_147737_7_,
				p_147737_6_ + p_147737_8_, 0.5F + p_147737_9_);

		if (p_147737_11_) {
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			renderFaceYNeg(p_147737_1_, 0.0D, 0.0D, 0.0D,
					getBlockIconFromSideAndMetadata(p_147737_1_, 0, p_147737_12_));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderFaceYPos(p_147737_1_, 0.0D, 0.0D, 0.0D,
					getBlockIconFromSideAndMetadata(p_147737_1_, 1, p_147737_12_));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderFaceZNeg(p_147737_1_, 0.0D, 0.0D, 0.0D,
					getBlockIconFromSideAndMetadata(p_147737_1_, 2, p_147737_12_));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderFaceZPos(p_147737_1_, 0.0D, 0.0D, 0.0D,
					getBlockIconFromSideAndMetadata(p_147737_1_, 3, p_147737_12_));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderFaceXNeg(p_147737_1_, 0.0D, 0.0D, 0.0D,
					getBlockIconFromSideAndMetadata(p_147737_1_, 4, p_147737_12_));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderFaceXPos(p_147737_1_, 0.0D, 0.0D, 0.0D,
					getBlockIconFromSideAndMetadata(p_147737_1_, 5, p_147737_12_));
			tessellator.draw();
		} else {
			renderStandardBlock(p_147737_1_, p_147737_2_, p_147737_3_, p_147737_4_);
		}

		return p_147737_6_ + p_147737_8_;
	}

	public boolean renderBlockTorch(Block p_147791_1_, int p_147791_2_, int p_147791_3_, int p_147791_4_) {
		int l = blockAccess.getBlockMetadata(p_147791_2_, p_147791_3_, p_147791_4_);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(
				p_147791_1_.getMixedBrightnessForBlock(blockAccess, p_147791_2_, p_147791_3_, p_147791_4_));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double d0 = 0.4000000059604645D;
		double d1 = 0.5D - d0;
		double d2 = 0.20000000298023224D;

		if (l == 1) {
			renderTorchAtAngle(p_147791_1_, p_147791_2_ - d1, p_147791_3_ + d2, p_147791_4_, -d0, 0.0D, 0);
		} else if (l == 2) {
			renderTorchAtAngle(p_147791_1_, p_147791_2_ + d1, p_147791_3_ + d2, p_147791_4_, d0, 0.0D, 0);
		} else if (l == 3) {
			renderTorchAtAngle(p_147791_1_, p_147791_2_, p_147791_3_ + d2, p_147791_4_ - d1, 0.0D, -d0, 0);
		} else if (l == 4) {
			renderTorchAtAngle(p_147791_1_, p_147791_2_, p_147791_3_ + d2, p_147791_4_ + d1, 0.0D, d0, 0);
		} else {
			renderTorchAtAngle(p_147791_1_, p_147791_2_, p_147791_3_, p_147791_4_, 0.0D, 0.0D, 0);
		}

		return true;
	}

	public boolean renderBlockRepeater(BlockRedstoneRepeater p_147759_1_, int p_147759_2_, int p_147759_3_,
			int p_147759_4_) {
		int l = blockAccess.getBlockMetadata(p_147759_2_, p_147759_3_, p_147759_4_);
		int i1 = l & 3;
		int j1 = (l & 12) >> 2;
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(
				p_147759_1_.getMixedBrightnessForBlock(blockAccess, p_147759_2_, p_147759_3_, p_147759_4_));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double d0 = -0.1875D;
		boolean flag = p_147759_1_.func_149910_g(blockAccess, p_147759_2_, p_147759_3_, p_147759_4_, l);
		double d1 = 0.0D;
		double d2 = 0.0D;
		double d3 = 0.0D;
		double d4 = 0.0D;

		switch (i1) {
		case 0:
			d4 = -0.3125D;
			d2 = BlockRedstoneRepeater.repeaterTorchOffset[j1];
			break;
		case 1:
			d3 = 0.3125D;
			d1 = -BlockRedstoneRepeater.repeaterTorchOffset[j1];
			break;
		case 2:
			d4 = 0.3125D;
			d2 = -BlockRedstoneRepeater.repeaterTorchOffset[j1];
			break;
		case 3:
			d3 = -0.3125D;
			d1 = BlockRedstoneRepeater.repeaterTorchOffset[j1];
		}

		if (!flag) {
			renderTorchAtAngle(p_147759_1_, p_147759_2_ + d1, p_147759_3_ + d0, p_147759_4_ + d2, 0.0D, 0.0D, 0);
		} else {
			IIcon iicon = this.getBlockIcon(Blocks.bedrock);
			setOverrideBlockTexture(iicon);
			float f = 2.0F;
			float f1 = 14.0F;
			float f2 = 7.0F;
			float f3 = 9.0F;

			switch (i1) {
			case 1:
			case 3:
				f = 7.0F;
				f1 = 9.0F;
				f2 = 2.0F;
				f3 = 14.0F;
			case 0:
			case 2:
			default:
				setRenderBounds(f / 16.0F + (float) d1, 0.125D, f2 / 16.0F + (float) d2, f1 / 16.0F + (float) d1, 0.25D,
						f3 / 16.0F + (float) d2);
				double d5 = iicon.getInterpolatedU(f);
				double d6 = iicon.getInterpolatedV(f2);
				double d7 = iicon.getInterpolatedU(f1);
				double d8 = iicon.getInterpolatedV(f3);
				tessellator.addVertexWithUV(p_147759_2_ + f / 16.0F + d1, p_147759_3_ + 0.25F,
						p_147759_4_ + f2 / 16.0F + d2, d5, d6);
				tessellator.addVertexWithUV(p_147759_2_ + f / 16.0F + d1, p_147759_3_ + 0.25F,
						p_147759_4_ + f3 / 16.0F + d2, d5, d8);
				tessellator.addVertexWithUV(p_147759_2_ + f1 / 16.0F + d1, p_147759_3_ + 0.25F,
						p_147759_4_ + f3 / 16.0F + d2, d7, d8);
				tessellator.addVertexWithUV(p_147759_2_ + f1 / 16.0F + d1, p_147759_3_ + 0.25F,
						p_147759_4_ + f2 / 16.0F + d2, d7, d6);
				renderStandardBlock(p_147759_1_, p_147759_2_, p_147759_3_, p_147759_4_);
				setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
				clearOverrideBlockTexture();
			}
		}

		tessellator.setBrightness(
				p_147759_1_.getMixedBrightnessForBlock(blockAccess, p_147759_2_, p_147759_3_, p_147759_4_));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		renderTorchAtAngle(p_147759_1_, p_147759_2_ + d3, p_147759_3_ + d0, p_147759_4_ + d4, 0.0D, 0.0D, 0);
		renderBlockRedstoneDiode(p_147759_1_, p_147759_2_, p_147759_3_, p_147759_4_);
		return true;
	}

	public boolean renderBlockRedstoneComparator(BlockRedstoneComparator p_147781_1_, int p_147781_2_, int p_147781_3_,
			int p_147781_4_) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(
				p_147781_1_.getMixedBrightnessForBlock(blockAccess, p_147781_2_, p_147781_3_, p_147781_4_));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		int l = blockAccess.getBlockMetadata(p_147781_2_, p_147781_3_, p_147781_4_);
		int i1 = l & 3;
		double d0 = 0.0D;
		double d1 = -0.1875D;
		double d2 = 0.0D;
		double d3 = 0.0D;
		double d4 = 0.0D;
		IIcon iicon;

		if (p_147781_1_.func_149969_d(l)) {
			iicon = Blocks.redstone_torch.getBlockTextureFromSide(0);
		} else {
			d1 -= 0.1875D;
			iicon = Blocks.unlit_redstone_torch.getBlockTextureFromSide(0);
		}

		switch (i1) {
		case 0:
			d2 = -0.3125D;
			d4 = 1.0D;
			break;
		case 1:
			d0 = 0.3125D;
			d3 = -1.0D;
			break;
		case 2:
			d2 = 0.3125D;
			d4 = -1.0D;
			break;
		case 3:
			d0 = -0.3125D;
			d3 = 1.0D;
		}

		renderTorchAtAngle(p_147781_1_, p_147781_2_ + 0.25D * d3 + 0.1875D * d4, p_147781_3_ - 0.1875F,
				p_147781_4_ + 0.25D * d4 + 0.1875D * d3, 0.0D, 0.0D, l);
		renderTorchAtAngle(p_147781_1_, p_147781_2_ + 0.25D * d3 + -0.1875D * d4, p_147781_3_ - 0.1875F,
				p_147781_4_ + 0.25D * d4 + -0.1875D * d3, 0.0D, 0.0D, l);
		setOverrideBlockTexture(iicon);
		renderTorchAtAngle(p_147781_1_, p_147781_2_ + d0, p_147781_3_ + d1, p_147781_4_ + d2, 0.0D, 0.0D, l);
		clearOverrideBlockTexture();
		renderBlockRedstoneDiodeMetadata(p_147781_1_, p_147781_2_, p_147781_3_, p_147781_4_, i1);
		return true;
	}

	public boolean renderBlockRedstoneDiode(BlockRedstoneDiode p_147748_1_, int p_147748_2_, int p_147748_3_,
			int p_147748_4_) {
		renderBlockRedstoneDiodeMetadata(p_147748_1_, p_147748_2_, p_147748_3_, p_147748_4_,
				blockAccess.getBlockMetadata(p_147748_2_, p_147748_3_, p_147748_4_) & 3);
		return true;
	}

	public void renderBlockRedstoneDiodeMetadata(BlockRedstoneDiode p_147732_1_, int p_147732_2_, int p_147732_3_,
			int p_147732_4_, int p_147732_5_) {
		renderStandardBlock(p_147732_1_, p_147732_2_, p_147732_3_, p_147732_4_);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(
				p_147732_1_.getMixedBrightnessForBlock(blockAccess, p_147732_2_, p_147732_3_, p_147732_4_));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		int i1 = blockAccess.getBlockMetadata(p_147732_2_, p_147732_3_, p_147732_4_);
		IIcon iicon = getBlockIconFromSideAndMetadata(p_147732_1_, 1, i1);
		double d0 = iicon.getMinU();
		double d1 = iicon.getMaxU();
		double d2 = iicon.getMinV();
		double d3 = iicon.getMaxV();
		double d4 = 0.125D;
		double d5 = p_147732_2_ + 1;
		double d6 = p_147732_2_ + 1;
		double d7 = p_147732_2_ + 0;
		double d8 = p_147732_2_ + 0;
		double d9 = p_147732_4_ + 0;
		double d10 = p_147732_4_ + 1;
		double d11 = p_147732_4_ + 1;
		double d12 = p_147732_4_ + 0;
		double d13 = p_147732_3_ + d4;

		if (p_147732_5_ == 2) {
			d5 = d6 = p_147732_2_ + 0;
			d7 = d8 = p_147732_2_ + 1;
			d9 = d12 = p_147732_4_ + 1;
			d10 = d11 = p_147732_4_ + 0;
		} else if (p_147732_5_ == 3) {
			d5 = d8 = p_147732_2_ + 0;
			d6 = d7 = p_147732_2_ + 1;
			d9 = d10 = p_147732_4_ + 0;
			d11 = d12 = p_147732_4_ + 1;
		} else if (p_147732_5_ == 1) {
			d5 = d8 = p_147732_2_ + 1;
			d6 = d7 = p_147732_2_ + 0;
			d9 = d10 = p_147732_4_ + 1;
			d11 = d12 = p_147732_4_ + 0;
		}

		tessellator.addVertexWithUV(d8, d13, d12, d0, d2);
		tessellator.addVertexWithUV(d7, d13, d11, d0, d3);
		tessellator.addVertexWithUV(d6, d13, d10, d1, d3);
		tessellator.addVertexWithUV(d5, d13, d9, d1, d2);
	}

	public void renderPistonBaseAllFaces(Block p_147804_1_, int p_147804_2_, int p_147804_3_, int p_147804_4_) {
		renderAllFaces = true;
		renderPistonBase(p_147804_1_, p_147804_2_, p_147804_3_, p_147804_4_, true);
		renderAllFaces = false;
	}

	public boolean renderPistonBase(Block p_147731_1_, int p_147731_2_, int p_147731_3_, int p_147731_4_,
			boolean p_147731_5_) {
		int l = blockAccess.getBlockMetadata(p_147731_2_, p_147731_3_, p_147731_4_);
		boolean flag1 = p_147731_5_ || (l & 8) != 0;
		int i1 = BlockPistonBase.getPistonOrientation(l);
		if (flag1) {
			switch (i1) {
			case 0:
				uvRotateEast = 3;
				uvRotateWest = 3;
				uvRotateSouth = 3;
				uvRotateNorth = 3;
				setRenderBounds(0.0D, 0.25D, 0.0D, 1.0D, 1.0D, 1.0D);
				break;
			case 1:
				setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
				break;
			case 2:
				uvRotateSouth = 1;
				uvRotateNorth = 2;
				setRenderBounds(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D);
				break;
			case 3:
				uvRotateSouth = 2;
				uvRotateNorth = 1;
				uvRotateTop = 3;
				uvRotateBottom = 3;
				setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D);
				break;
			case 4:
				uvRotateEast = 1;
				uvRotateWest = 2;
				uvRotateTop = 2;
				uvRotateBottom = 1;
				setRenderBounds(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
				break;
			case 5:
				uvRotateEast = 2;
				uvRotateWest = 1;
				uvRotateTop = 1;
				uvRotateBottom = 2;
				setRenderBounds(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D);
			}

			((BlockPistonBase) p_147731_1_).func_150070_b((float) renderMinX, (float) renderMinY, (float) renderMinZ,
					(float) renderMaxX, (float) renderMaxY, (float) renderMaxZ);
			renderStandardBlock(p_147731_1_, p_147731_2_, p_147731_3_, p_147731_4_);
			uvRotateEast = 0;
			uvRotateWest = 0;
			uvRotateSouth = 0;
			uvRotateNorth = 0;
			uvRotateTop = 0;
			uvRotateBottom = 0;
			setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			((BlockPistonBase) p_147731_1_).func_150070_b((float) renderMinX, (float) renderMinY, (float) renderMinZ,
					(float) renderMaxX, (float) renderMaxY, (float) renderMaxZ);
		} else {
			switch (i1) {
			case 0:
				uvRotateEast = 3;
				uvRotateWest = 3;
				uvRotateSouth = 3;
				uvRotateNorth = 3;
			case 1:
			default:
				break;
			case 2:
				uvRotateSouth = 1;
				uvRotateNorth = 2;
				break;
			case 3:
				uvRotateSouth = 2;
				uvRotateNorth = 1;
				uvRotateTop = 3;
				uvRotateBottom = 3;
				break;
			case 4:
				uvRotateEast = 1;
				uvRotateWest = 2;
				uvRotateTop = 2;
				uvRotateBottom = 1;
				break;
			case 5:
				uvRotateEast = 2;
				uvRotateWest = 1;
				uvRotateTop = 1;
				uvRotateBottom = 2;
			}

			renderStandardBlock(p_147731_1_, p_147731_2_, p_147731_3_, p_147731_4_);
			uvRotateEast = 0;
			uvRotateWest = 0;
			uvRotateSouth = 0;
			uvRotateNorth = 0;
			uvRotateTop = 0;
			uvRotateBottom = 0;
		}

		return true;
	}

	public void renderPistonRodUD(double p_147763_1_, double p_147763_3_, double p_147763_5_, double p_147763_7_,
			double p_147763_9_, double p_147763_11_, float p_147763_13_, double p_147763_14_) {
		IIcon iicon = BlockPistonBase.getPistonBaseIcon("piston_side");

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
		}

		Tessellator tessellator = Tessellator.instance;
		double d7 = iicon.getMinU();
		double d8 = iicon.getMinV();
		double d9 = iicon.getInterpolatedU(p_147763_14_);
		double d10 = iicon.getInterpolatedV(4.0D);
		tessellator.setColorOpaque_F(p_147763_13_, p_147763_13_, p_147763_13_);
		tessellator.addVertexWithUV(p_147763_1_, p_147763_7_, p_147763_9_, d9, d8);
		tessellator.addVertexWithUV(p_147763_1_, p_147763_5_, p_147763_9_, d7, d8);
		tessellator.addVertexWithUV(p_147763_3_, p_147763_5_, p_147763_11_, d7, d10);
		tessellator.addVertexWithUV(p_147763_3_, p_147763_7_, p_147763_11_, d9, d10);
	}

	public void renderPistonRodSN(double p_147789_1_, double p_147789_3_, double p_147789_5_, double p_147789_7_,
			double p_147789_9_, double p_147789_11_, float p_147789_13_, double p_147789_14_) {
		IIcon iicon = BlockPistonBase.getPistonBaseIcon("piston_side");

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
		}

		Tessellator tessellator = Tessellator.instance;
		double d7 = iicon.getMinU();
		double d8 = iicon.getMinV();
		double d9 = iicon.getInterpolatedU(p_147789_14_);
		double d10 = iicon.getInterpolatedV(4.0D);
		tessellator.setColorOpaque_F(p_147789_13_, p_147789_13_, p_147789_13_);
		tessellator.addVertexWithUV(p_147789_1_, p_147789_5_, p_147789_11_, d9, d8);
		tessellator.addVertexWithUV(p_147789_1_, p_147789_5_, p_147789_9_, d7, d8);
		tessellator.addVertexWithUV(p_147789_3_, p_147789_7_, p_147789_9_, d7, d10);
		tessellator.addVertexWithUV(p_147789_3_, p_147789_7_, p_147789_11_, d9, d10);
	}

	public void renderPistonRodEW(double p_147738_1_, double p_147738_3_, double p_147738_5_, double p_147738_7_,
			double p_147738_9_, double p_147738_11_, float p_147738_13_, double p_147738_14_) {
		IIcon iicon = BlockPistonBase.getPistonBaseIcon("piston_side");

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
		}

		Tessellator tessellator = Tessellator.instance;
		double d7 = iicon.getMinU();
		double d8 = iicon.getMinV();
		double d9 = iicon.getInterpolatedU(p_147738_14_);
		double d10 = iicon.getInterpolatedV(4.0D);
		tessellator.setColorOpaque_F(p_147738_13_, p_147738_13_, p_147738_13_);
		tessellator.addVertexWithUV(p_147738_3_, p_147738_5_, p_147738_9_, d9, d8);
		tessellator.addVertexWithUV(p_147738_1_, p_147738_5_, p_147738_9_, d7, d8);
		tessellator.addVertexWithUV(p_147738_1_, p_147738_7_, p_147738_11_, d7, d10);
		tessellator.addVertexWithUV(p_147738_3_, p_147738_7_, p_147738_11_, d9, d10);
	}

	public void renderPistonExtensionAllFaces(Block p_147750_1_, int p_147750_2_, int p_147750_3_, int p_147750_4_,
			boolean p_147750_5_) {
		renderAllFaces = true;
		renderPistonExtension(p_147750_1_, p_147750_2_, p_147750_3_, p_147750_4_, p_147750_5_);
		renderAllFaces = false;
	}

	public boolean renderPistonExtension(Block p_147809_1_, int p_147809_2_, int p_147809_3_, int p_147809_4_,
			boolean p_147809_5_) {
		int l = blockAccess.getBlockMetadata(p_147809_2_, p_147809_3_, p_147809_4_);
		int i1 = BlockPistonExtension.getDirectionMeta(l);
		float f3 = p_147809_5_ ? 1.0F : 0.5F;
		double d0 = p_147809_5_ ? 16.0D : 8.0D;

		switch (i1) {
		case 0:
			uvRotateEast = 3;
			uvRotateWest = 3;
			uvRotateSouth = 3;
			uvRotateNorth = 3;
			setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
			renderStandardBlock(p_147809_1_, p_147809_2_, p_147809_3_, p_147809_4_);
			renderPistonRodUD(p_147809_2_ + 0.375F, p_147809_2_ + 0.625F, p_147809_3_ + 0.25F, p_147809_3_ + 0.25F + f3,
					p_147809_4_ + 0.625F, p_147809_4_ + 0.625F, 0.8F, d0);
			renderPistonRodUD(p_147809_2_ + 0.625F, p_147809_2_ + 0.375F, p_147809_3_ + 0.25F, p_147809_3_ + 0.25F + f3,
					p_147809_4_ + 0.375F, p_147809_4_ + 0.375F, 0.8F, d0);
			renderPistonRodUD(p_147809_2_ + 0.375F, p_147809_2_ + 0.375F, p_147809_3_ + 0.25F, p_147809_3_ + 0.25F + f3,
					p_147809_4_ + 0.375F, p_147809_4_ + 0.625F, 0.6F, d0);
			renderPistonRodUD(p_147809_2_ + 0.625F, p_147809_2_ + 0.625F, p_147809_3_ + 0.25F, p_147809_3_ + 0.25F + f3,
					p_147809_4_ + 0.625F, p_147809_4_ + 0.375F, 0.6F, d0);
			break;
		case 1:
			setRenderBounds(0.0D, 0.75D, 0.0D, 1.0D, 1.0D, 1.0D);
			renderStandardBlock(p_147809_1_, p_147809_2_, p_147809_3_, p_147809_4_);
			renderPistonRodUD(p_147809_2_ + 0.375F, p_147809_2_ + 0.625F, p_147809_3_ - 0.25F + 1.0F - f3,
					p_147809_3_ - 0.25F + 1.0F, p_147809_4_ + 0.625F, p_147809_4_ + 0.625F, 0.8F, d0);
			renderPistonRodUD(p_147809_2_ + 0.625F, p_147809_2_ + 0.375F, p_147809_3_ - 0.25F + 1.0F - f3,
					p_147809_3_ - 0.25F + 1.0F, p_147809_4_ + 0.375F, p_147809_4_ + 0.375F, 0.8F, d0);
			renderPistonRodUD(p_147809_2_ + 0.375F, p_147809_2_ + 0.375F, p_147809_3_ - 0.25F + 1.0F - f3,
					p_147809_3_ - 0.25F + 1.0F, p_147809_4_ + 0.375F, p_147809_4_ + 0.625F, 0.6F, d0);
			renderPistonRodUD(p_147809_2_ + 0.625F, p_147809_2_ + 0.625F, p_147809_3_ - 0.25F + 1.0F - f3,
					p_147809_3_ - 0.25F + 1.0F, p_147809_4_ + 0.625F, p_147809_4_ + 0.375F, 0.6F, d0);
			break;
		case 2:
			uvRotateSouth = 1;
			uvRotateNorth = 2;
			setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.25D);
			renderStandardBlock(p_147809_1_, p_147809_2_, p_147809_3_, p_147809_4_);
			renderPistonRodSN(p_147809_2_ + 0.375F, p_147809_2_ + 0.375F, p_147809_3_ + 0.625F, p_147809_3_ + 0.375F,
					p_147809_4_ + 0.25F, p_147809_4_ + 0.25F + f3, 0.6F, d0);
			renderPistonRodSN(p_147809_2_ + 0.625F, p_147809_2_ + 0.625F, p_147809_3_ + 0.375F, p_147809_3_ + 0.625F,
					p_147809_4_ + 0.25F, p_147809_4_ + 0.25F + f3, 0.6F, d0);
			renderPistonRodSN(p_147809_2_ + 0.375F, p_147809_2_ + 0.625F, p_147809_3_ + 0.375F, p_147809_3_ + 0.375F,
					p_147809_4_ + 0.25F, p_147809_4_ + 0.25F + f3, 0.5F, d0);
			renderPistonRodSN(p_147809_2_ + 0.625F, p_147809_2_ + 0.375F, p_147809_3_ + 0.625F, p_147809_3_ + 0.625F,
					p_147809_4_ + 0.25F, p_147809_4_ + 0.25F + f3, 1.0F, d0);
			break;
		case 3:
			uvRotateSouth = 2;
			uvRotateNorth = 1;
			uvRotateTop = 3;
			uvRotateBottom = 3;
			setRenderBounds(0.0D, 0.0D, 0.75D, 1.0D, 1.0D, 1.0D);
			renderStandardBlock(p_147809_1_, p_147809_2_, p_147809_3_, p_147809_4_);
			renderPistonRodSN(p_147809_2_ + 0.375F, p_147809_2_ + 0.375F, p_147809_3_ + 0.625F, p_147809_3_ + 0.375F,
					p_147809_4_ - 0.25F + 1.0F - f3, p_147809_4_ - 0.25F + 1.0F, 0.6F, d0);
			renderPistonRodSN(p_147809_2_ + 0.625F, p_147809_2_ + 0.625F, p_147809_3_ + 0.375F, p_147809_3_ + 0.625F,
					p_147809_4_ - 0.25F + 1.0F - f3, p_147809_4_ - 0.25F + 1.0F, 0.6F, d0);
			renderPistonRodSN(p_147809_2_ + 0.375F, p_147809_2_ + 0.625F, p_147809_3_ + 0.375F, p_147809_3_ + 0.375F,
					p_147809_4_ - 0.25F + 1.0F - f3, p_147809_4_ - 0.25F + 1.0F, 0.5F, d0);
			renderPistonRodSN(p_147809_2_ + 0.625F, p_147809_2_ + 0.375F, p_147809_3_ + 0.625F, p_147809_3_ + 0.625F,
					p_147809_4_ - 0.25F + 1.0F - f3, p_147809_4_ - 0.25F + 1.0F, 1.0F, d0);
			break;
		case 4:
			uvRotateEast = 1;
			uvRotateWest = 2;
			uvRotateTop = 2;
			uvRotateBottom = 1;
			setRenderBounds(0.0D, 0.0D, 0.0D, 0.25D, 1.0D, 1.0D);
			renderStandardBlock(p_147809_1_, p_147809_2_, p_147809_3_, p_147809_4_);
			renderPistonRodEW(p_147809_2_ + 0.25F, p_147809_2_ + 0.25F + f3, p_147809_3_ + 0.375F, p_147809_3_ + 0.375F,
					p_147809_4_ + 0.625F, p_147809_4_ + 0.375F, 0.5F, d0);
			renderPistonRodEW(p_147809_2_ + 0.25F, p_147809_2_ + 0.25F + f3, p_147809_3_ + 0.625F, p_147809_3_ + 0.625F,
					p_147809_4_ + 0.375F, p_147809_4_ + 0.625F, 1.0F, d0);
			renderPistonRodEW(p_147809_2_ + 0.25F, p_147809_2_ + 0.25F + f3, p_147809_3_ + 0.375F, p_147809_3_ + 0.625F,
					p_147809_4_ + 0.375F, p_147809_4_ + 0.375F, 0.6F, d0);
			renderPistonRodEW(p_147809_2_ + 0.25F, p_147809_2_ + 0.25F + f3, p_147809_3_ + 0.625F, p_147809_3_ + 0.375F,
					p_147809_4_ + 0.625F, p_147809_4_ + 0.625F, 0.6F, d0);
			break;
		case 5:
			uvRotateEast = 2;
			uvRotateWest = 1;
			uvRotateTop = 1;
			uvRotateBottom = 2;
			setRenderBounds(0.75D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			renderStandardBlock(p_147809_1_, p_147809_2_, p_147809_3_, p_147809_4_);
			renderPistonRodEW(p_147809_2_ - 0.25F + 1.0F - f3, p_147809_2_ - 0.25F + 1.0F, p_147809_3_ + 0.375F,
					p_147809_3_ + 0.375F, p_147809_4_ + 0.625F, p_147809_4_ + 0.375F, 0.5F, d0);
			renderPistonRodEW(p_147809_2_ - 0.25F + 1.0F - f3, p_147809_2_ - 0.25F + 1.0F, p_147809_3_ + 0.625F,
					p_147809_3_ + 0.625F, p_147809_4_ + 0.375F, p_147809_4_ + 0.625F, 1.0F, d0);
			renderPistonRodEW(p_147809_2_ - 0.25F + 1.0F - f3, p_147809_2_ - 0.25F + 1.0F, p_147809_3_ + 0.375F,
					p_147809_3_ + 0.625F, p_147809_4_ + 0.375F, p_147809_4_ + 0.375F, 0.6F, d0);
			renderPistonRodEW(p_147809_2_ - 0.25F + 1.0F - f3, p_147809_2_ - 0.25F + 1.0F, p_147809_3_ + 0.625F,
					p_147809_3_ + 0.375F, p_147809_4_ + 0.625F, p_147809_4_ + 0.625F, 0.6F, d0);
		}

		uvRotateEast = 0;
		uvRotateWest = 0;
		uvRotateSouth = 0;
		uvRotateNorth = 0;
		uvRotateTop = 0;
		uvRotateBottom = 0;
		setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		return true;
	}

	public boolean renderBlockLever(Block p_147790_1_, int p_147790_2_, int p_147790_3_, int p_147790_4_) {
		int l = blockAccess.getBlockMetadata(p_147790_2_, p_147790_3_, p_147790_4_);
		int i1 = l & 7;
		boolean flag = (l & 8) > 0;
		Tessellator tessellator = Tessellator.instance;
		boolean flag1 = hasOverrideBlockTexture();

		if (!flag1) {
			setOverrideBlockTexture(this.getBlockIcon(Blocks.cobblestone));
		}

		float f = 0.25F;
		float f1 = 0.1875F;
		float f2 = 0.1875F;

		if (i1 == 5) {
			setRenderBounds(0.5F - f1, 0.0D, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
		} else if (i1 == 6) {
			setRenderBounds(0.5F - f, 0.0D, 0.5F - f1, 0.5F + f, f2, 0.5F + f1);
		} else if (i1 == 4) {
			setRenderBounds(0.5F - f1, 0.5F - f, 1.0F - f2, 0.5F + f1, 0.5F + f, 1.0D);
		} else if (i1 == 3) {
			setRenderBounds(0.5F - f1, 0.5F - f, 0.0D, 0.5F + f1, 0.5F + f, f2);
		} else if (i1 == 2) {
			setRenderBounds(1.0F - f2, 0.5F - f, 0.5F - f1, 1.0D, 0.5F + f, 0.5F + f1);
		} else if (i1 == 1) {
			setRenderBounds(0.0D, 0.5F - f, 0.5F - f1, f2, 0.5F + f, 0.5F + f1);
		} else if (i1 == 0) {
			setRenderBounds(0.5F - f, 1.0F - f2, 0.5F - f1, 0.5F + f, 1.0D, 0.5F + f1);
		} else if (i1 == 7) {
			setRenderBounds(0.5F - f1, 1.0F - f2, 0.5F - f, 0.5F + f1, 1.0D, 0.5F + f);
		}

		renderStandardBlock(p_147790_1_, p_147790_2_, p_147790_3_, p_147790_4_);

		if (!flag1) {
			clearOverrideBlockTexture();
		}

		tessellator.setBrightness(
				p_147790_1_.getMixedBrightnessForBlock(blockAccess, p_147790_2_, p_147790_3_, p_147790_4_));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		IIcon iicon = getBlockIconFromSide(p_147790_1_, 0);

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
		}

		double d0 = iicon.getMinU();
		double d1 = iicon.getMinV();
		double d2 = iicon.getMaxU();
		double d3 = iicon.getMaxV();
		Vec3[] avec3 = new Vec3[8];
		float f3 = 0.0625F;
		float f4 = 0.0625F;
		float f5 = 0.625F;
		avec3[0] = Vec3.createVectorHelper(-f3, 0.0D, -f4);
		avec3[1] = Vec3.createVectorHelper(f3, 0.0D, -f4);
		avec3[2] = Vec3.createVectorHelper(f3, 0.0D, f4);
		avec3[3] = Vec3.createVectorHelper(-f3, 0.0D, f4);
		avec3[4] = Vec3.createVectorHelper(-f3, f5, -f4);
		avec3[5] = Vec3.createVectorHelper(f3, f5, -f4);
		avec3[6] = Vec3.createVectorHelper(f3, f5, f4);
		avec3[7] = Vec3.createVectorHelper(-f3, f5, f4);

		for (int j1 = 0; j1 < 8; ++j1) {
			if (flag) {
				avec3[j1].zCoord -= 0.0625D;
				avec3[j1].rotateAroundX((float) Math.PI * 2F / 9F);
			} else {
				avec3[j1].zCoord += 0.0625D;
				avec3[j1].rotateAroundX(-((float) Math.PI * 2F / 9F));
			}

			if (i1 == 0 || i1 == 7) {
				avec3[j1].rotateAroundZ((float) Math.PI);
			}

			if (i1 == 6 || i1 == 0) {
				avec3[j1].rotateAroundY((float) Math.PI / 2F);
			}

			if (i1 > 0 && i1 < 5) {
				avec3[j1].yCoord -= 0.375D;
				avec3[j1].rotateAroundX((float) Math.PI / 2F);

				if (i1 == 4) {
					avec3[j1].rotateAroundY(0.0F);
				}

				if (i1 == 3) {
					avec3[j1].rotateAroundY((float) Math.PI);
				}

				if (i1 == 2) {
					avec3[j1].rotateAroundY((float) Math.PI / 2F);
				}

				if (i1 == 1) {
					avec3[j1].rotateAroundY(-((float) Math.PI / 2F));
				}

				avec3[j1].xCoord += p_147790_2_ + 0.5D;
				avec3[j1].yCoord += p_147790_3_ + 0.5F;
				avec3[j1].zCoord += p_147790_4_ + 0.5D;
			} else if (i1 != 0 && i1 != 7) {
				avec3[j1].xCoord += p_147790_2_ + 0.5D;
				avec3[j1].yCoord += p_147790_3_ + 0.125F;
				avec3[j1].zCoord += p_147790_4_ + 0.5D;
			} else {
				avec3[j1].xCoord += p_147790_2_ + 0.5D;
				avec3[j1].yCoord += p_147790_3_ + 0.875F;
				avec3[j1].zCoord += p_147790_4_ + 0.5D;
			}
		}

		Vec3 vec33 = null;
		Vec3 vec3 = null;
		Vec3 vec31 = null;
		Vec3 vec32 = null;

		for (int k1 = 0; k1 < 6; ++k1) {
			if (k1 == 0) {
				d0 = iicon.getInterpolatedU(7.0D);
				d1 = iicon.getInterpolatedV(6.0D);
				d2 = iicon.getInterpolatedU(9.0D);
				d3 = iicon.getInterpolatedV(8.0D);
			} else if (k1 == 2) {
				d0 = iicon.getInterpolatedU(7.0D);
				d1 = iicon.getInterpolatedV(6.0D);
				d2 = iicon.getInterpolatedU(9.0D);
				d3 = iicon.getMaxV();
			}

			if (k1 == 0) {
				vec33 = avec3[0];
				vec3 = avec3[1];
				vec31 = avec3[2];
				vec32 = avec3[3];
			} else if (k1 == 1) {
				vec33 = avec3[7];
				vec3 = avec3[6];
				vec31 = avec3[5];
				vec32 = avec3[4];
			} else if (k1 == 2) {
				vec33 = avec3[1];
				vec3 = avec3[0];
				vec31 = avec3[4];
				vec32 = avec3[5];
			} else if (k1 == 3) {
				vec33 = avec3[2];
				vec3 = avec3[1];
				vec31 = avec3[5];
				vec32 = avec3[6];
			} else if (k1 == 4) {
				vec33 = avec3[3];
				vec3 = avec3[2];
				vec31 = avec3[6];
				vec32 = avec3[7];
			} else if (k1 == 5) {
				vec33 = avec3[0];
				vec3 = avec3[3];
				vec31 = avec3[7];
				vec32 = avec3[4];
			}

			tessellator.addVertexWithUV(vec33.xCoord, vec33.yCoord, vec33.zCoord, d0, d3);
			tessellator.addVertexWithUV(vec3.xCoord, vec3.yCoord, vec3.zCoord, d2, d3);
			tessellator.addVertexWithUV(vec31.xCoord, vec31.yCoord, vec31.zCoord, d2, d1);
			tessellator.addVertexWithUV(vec32.xCoord, vec32.yCoord, vec32.zCoord, d0, d1);
		}

		return true;
	}

	public boolean renderBlockTripWireSource(Block p_147723_1_, int p_147723_2_, int p_147723_3_, int p_147723_4_) {
		Tessellator tessellator = Tessellator.instance;
		int l = blockAccess.getBlockMetadata(p_147723_2_, p_147723_3_, p_147723_4_);
		int i1 = l & 3;
		boolean flag = (l & 4) == 4;
		boolean flag1 = (l & 8) == 8;
		boolean flag2 = !World.doesBlockHaveSolidTopSurface(blockAccess, p_147723_2_, p_147723_3_ - 1, p_147723_4_);
		boolean flag3 = hasOverrideBlockTexture();

		if (!flag3) {
			setOverrideBlockTexture(this.getBlockIcon(Blocks.planks));
		}

		float f = 0.25F;
		float f1 = 0.125F;
		float f2 = 0.125F;
		float f3 = 0.3F - f;
		float f4 = 0.3F + f;

		if (i1 == 2) {
			setRenderBounds(0.5F - f1, f3, 1.0F - f2, 0.5F + f1, f4, 1.0D);
		} else if (i1 == 0) {
			setRenderBounds(0.5F - f1, f3, 0.0D, 0.5F + f1, f4, f2);
		} else if (i1 == 1) {
			setRenderBounds(1.0F - f2, f3, 0.5F - f1, 1.0D, f4, 0.5F + f1);
		} else if (i1 == 3) {
			setRenderBounds(0.0D, f3, 0.5F - f1, f2, f4, 0.5F + f1);
		}

		renderStandardBlock(p_147723_1_, p_147723_2_, p_147723_3_, p_147723_4_);

		if (!flag3) {
			clearOverrideBlockTexture();
		}

		tessellator.setBrightness(
				p_147723_1_.getMixedBrightnessForBlock(blockAccess, p_147723_2_, p_147723_3_, p_147723_4_));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		IIcon iicon = getBlockIconFromSide(p_147723_1_, 0);

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
		}

		double d0 = iicon.getMinU();
		double d1 = iicon.getMinV();
		double d2 = iicon.getMaxU();
		double d3 = iicon.getMaxV();
		Vec3[] avec3 = new Vec3[8];
		float f5 = 0.046875F;
		float f6 = 0.046875F;
		float f7 = 0.3125F;
		avec3[0] = Vec3.createVectorHelper(-f5, 0.0D, -f6);
		avec3[1] = Vec3.createVectorHelper(f5, 0.0D, -f6);
		avec3[2] = Vec3.createVectorHelper(f5, 0.0D, f6);
		avec3[3] = Vec3.createVectorHelper(-f5, 0.0D, f6);
		avec3[4] = Vec3.createVectorHelper(-f5, f7, -f6);
		avec3[5] = Vec3.createVectorHelper(f5, f7, -f6);
		avec3[6] = Vec3.createVectorHelper(f5, f7, f6);
		avec3[7] = Vec3.createVectorHelper(-f5, f7, f6);

		for (int j1 = 0; j1 < 8; ++j1) {
			avec3[j1].zCoord += 0.0625D;

			if (flag1) {
				avec3[j1].rotateAroundX(0.5235988F);
				avec3[j1].yCoord -= 0.4375D;
			} else if (flag) {
				avec3[j1].rotateAroundX(0.08726647F);
				avec3[j1].yCoord -= 0.4375D;
			} else {
				avec3[j1].rotateAroundX(-((float) Math.PI * 2F / 9F));
				avec3[j1].yCoord -= 0.375D;
			}

			avec3[j1].rotateAroundX((float) Math.PI / 2F);

			if (i1 == 2) {
				avec3[j1].rotateAroundY(0.0F);
			}

			if (i1 == 0) {
				avec3[j1].rotateAroundY((float) Math.PI);
			}

			if (i1 == 1) {
				avec3[j1].rotateAroundY((float) Math.PI / 2F);
			}

			if (i1 == 3) {
				avec3[j1].rotateAroundY(-((float) Math.PI / 2F));
			}

			avec3[j1].xCoord += p_147723_2_ + 0.5D;
			avec3[j1].yCoord += p_147723_3_ + 0.3125F;
			avec3[j1].zCoord += p_147723_4_ + 0.5D;
		}

		Vec3 vec33 = null;
		Vec3 vec3 = null;
		Vec3 vec31 = null;
		Vec3 vec32 = null;
		byte b0 = 7;
		byte b1 = 9;
		byte b2 = 9;
		byte b3 = 16;

		for (int k1 = 0; k1 < 6; ++k1) {
			if (k1 == 0) {
				vec33 = avec3[0];
				vec3 = avec3[1];
				vec31 = avec3[2];
				vec32 = avec3[3];
				d0 = iicon.getInterpolatedU(b0);
				d1 = iicon.getInterpolatedV(b2);
				d2 = iicon.getInterpolatedU(b1);
				d3 = iicon.getInterpolatedV(b2 + 2);
			} else if (k1 == 1) {
				vec33 = avec3[7];
				vec3 = avec3[6];
				vec31 = avec3[5];
				vec32 = avec3[4];
			} else if (k1 == 2) {
				vec33 = avec3[1];
				vec3 = avec3[0];
				vec31 = avec3[4];
				vec32 = avec3[5];
				d0 = iicon.getInterpolatedU(b0);
				d1 = iicon.getInterpolatedV(b2);
				d2 = iicon.getInterpolatedU(b1);
				d3 = iicon.getInterpolatedV(b3);
			} else if (k1 == 3) {
				vec33 = avec3[2];
				vec3 = avec3[1];
				vec31 = avec3[5];
				vec32 = avec3[6];
			} else if (k1 == 4) {
				vec33 = avec3[3];
				vec3 = avec3[2];
				vec31 = avec3[6];
				vec32 = avec3[7];
			} else if (k1 == 5) {
				vec33 = avec3[0];
				vec3 = avec3[3];
				vec31 = avec3[7];
				vec32 = avec3[4];
			}

			tessellator.addVertexWithUV(vec33.xCoord, vec33.yCoord, vec33.zCoord, d0, d3);
			tessellator.addVertexWithUV(vec3.xCoord, vec3.yCoord, vec3.zCoord, d2, d3);
			tessellator.addVertexWithUV(vec31.xCoord, vec31.yCoord, vec31.zCoord, d2, d1);
			tessellator.addVertexWithUV(vec32.xCoord, vec32.yCoord, vec32.zCoord, d0, d1);
		}

		float f13 = 0.09375F;
		float f8 = 0.09375F;
		float f9 = 0.03125F;
		avec3[0] = Vec3.createVectorHelper(-f13, 0.0D, -f8);
		avec3[1] = Vec3.createVectorHelper(f13, 0.0D, -f8);
		avec3[2] = Vec3.createVectorHelper(f13, 0.0D, f8);
		avec3[3] = Vec3.createVectorHelper(-f13, 0.0D, f8);
		avec3[4] = Vec3.createVectorHelper(-f13, f9, -f8);
		avec3[5] = Vec3.createVectorHelper(f13, f9, -f8);
		avec3[6] = Vec3.createVectorHelper(f13, f9, f8);
		avec3[7] = Vec3.createVectorHelper(-f13, f9, f8);

		for (int l1 = 0; l1 < 8; ++l1) {
			avec3[l1].zCoord += 0.21875D;

			if (flag1) {
				avec3[l1].yCoord -= 0.09375D;
				avec3[l1].zCoord -= 0.1625D;
				avec3[l1].rotateAroundX(0.0F);
			} else if (flag) {
				avec3[l1].yCoord += 0.015625D;
				avec3[l1].zCoord -= 0.171875D;
				avec3[l1].rotateAroundX(0.17453294F);
			} else {
				avec3[l1].rotateAroundX(0.87266463F);
			}

			if (i1 == 2) {
				avec3[l1].rotateAroundY(0.0F);
			}

			if (i1 == 0) {
				avec3[l1].rotateAroundY((float) Math.PI);
			}

			if (i1 == 1) {
				avec3[l1].rotateAroundY((float) Math.PI / 2F);
			}

			if (i1 == 3) {
				avec3[l1].rotateAroundY(-((float) Math.PI / 2F));
			}

			avec3[l1].xCoord += p_147723_2_ + 0.5D;
			avec3[l1].yCoord += p_147723_3_ + 0.3125F;
			avec3[l1].zCoord += p_147723_4_ + 0.5D;
		}

		byte b7 = 5;
		byte b4 = 11;
		byte b5 = 3;
		byte b6 = 9;

		for (int i2 = 0; i2 < 6; ++i2) {
			if (i2 == 0) {
				vec33 = avec3[0];
				vec3 = avec3[1];
				vec31 = avec3[2];
				vec32 = avec3[3];
				d0 = iicon.getInterpolatedU(b7);
				d1 = iicon.getInterpolatedV(b5);
				d2 = iicon.getInterpolatedU(b4);
				d3 = iicon.getInterpolatedV(b6);
			} else if (i2 == 1) {
				vec33 = avec3[7];
				vec3 = avec3[6];
				vec31 = avec3[5];
				vec32 = avec3[4];
			} else if (i2 == 2) {
				vec33 = avec3[1];
				vec3 = avec3[0];
				vec31 = avec3[4];
				vec32 = avec3[5];
				d0 = iicon.getInterpolatedU(b7);
				d1 = iicon.getInterpolatedV(b5);
				d2 = iicon.getInterpolatedU(b4);
				d3 = iicon.getInterpolatedV(b5 + 2);
			} else if (i2 == 3) {
				vec33 = avec3[2];
				vec3 = avec3[1];
				vec31 = avec3[5];
				vec32 = avec3[6];
			} else if (i2 == 4) {
				vec33 = avec3[3];
				vec3 = avec3[2];
				vec31 = avec3[6];
				vec32 = avec3[7];
			} else if (i2 == 5) {
				vec33 = avec3[0];
				vec3 = avec3[3];
				vec31 = avec3[7];
				vec32 = avec3[4];
			}

			tessellator.addVertexWithUV(vec33.xCoord, vec33.yCoord, vec33.zCoord, d0, d3);
			tessellator.addVertexWithUV(vec3.xCoord, vec3.yCoord, vec3.zCoord, d2, d3);
			tessellator.addVertexWithUV(vec31.xCoord, vec31.yCoord, vec31.zCoord, d2, d1);
			tessellator.addVertexWithUV(vec32.xCoord, vec32.yCoord, vec32.zCoord, d0, d1);
		}

		if (flag) {
			double d9 = avec3[0].yCoord;
			float f10 = 0.03125F;
			float f11 = 0.5F - f10 / 2.0F;
			float f12 = f11 + f10;
			double d4 = iicon.getMinU();
			double d5 = iicon.getInterpolatedV(flag ? 2.0D : 0.0D);
			double d6 = iicon.getMaxU();
			double d7 = iicon.getInterpolatedV(flag ? 4.0D : 2.0D);
			double d8 = (flag2 ? 3.5F : 1.5F) / 16.0D;
			tessellator.setColorOpaque_F(0.75F, 0.75F, 0.75F);

			if (i1 == 2) {
				tessellator.addVertexWithUV(p_147723_2_ + f11, p_147723_3_ + d8, p_147723_4_ + 0.25D, d4, d5);
				tessellator.addVertexWithUV(p_147723_2_ + f12, p_147723_3_ + d8, p_147723_4_ + 0.25D, d4, d7);
				tessellator.addVertexWithUV(p_147723_2_ + f12, p_147723_3_ + d8, p_147723_4_, d6, d7);
				tessellator.addVertexWithUV(p_147723_2_ + f11, p_147723_3_ + d8, p_147723_4_, d6, d5);
				tessellator.addVertexWithUV(p_147723_2_ + f11, d9, p_147723_4_ + 0.5D, d4, d5);
				tessellator.addVertexWithUV(p_147723_2_ + f12, d9, p_147723_4_ + 0.5D, d4, d7);
				tessellator.addVertexWithUV(p_147723_2_ + f12, p_147723_3_ + d8, p_147723_4_ + 0.25D, d6, d7);
				tessellator.addVertexWithUV(p_147723_2_ + f11, p_147723_3_ + d8, p_147723_4_ + 0.25D, d6, d5);
			} else if (i1 == 0) {
				tessellator.addVertexWithUV(p_147723_2_ + f11, p_147723_3_ + d8, p_147723_4_ + 0.75D, d4, d5);
				tessellator.addVertexWithUV(p_147723_2_ + f12, p_147723_3_ + d8, p_147723_4_ + 0.75D, d4, d7);
				tessellator.addVertexWithUV(p_147723_2_ + f12, d9, p_147723_4_ + 0.5D, d6, d7);
				tessellator.addVertexWithUV(p_147723_2_ + f11, d9, p_147723_4_ + 0.5D, d6, d5);
				tessellator.addVertexWithUV(p_147723_2_ + f11, p_147723_3_ + d8, p_147723_4_ + 1, d4, d5);
				tessellator.addVertexWithUV(p_147723_2_ + f12, p_147723_3_ + d8, p_147723_4_ + 1, d4, d7);
				tessellator.addVertexWithUV(p_147723_2_ + f12, p_147723_3_ + d8, p_147723_4_ + 0.75D, d6, d7);
				tessellator.addVertexWithUV(p_147723_2_ + f11, p_147723_3_ + d8, p_147723_4_ + 0.75D, d6, d5);
			} else if (i1 == 1) {
				tessellator.addVertexWithUV(p_147723_2_, p_147723_3_ + d8, p_147723_4_ + f12, d4, d7);
				tessellator.addVertexWithUV(p_147723_2_ + 0.25D, p_147723_3_ + d8, p_147723_4_ + f12, d6, d7);
				tessellator.addVertexWithUV(p_147723_2_ + 0.25D, p_147723_3_ + d8, p_147723_4_ + f11, d6, d5);
				tessellator.addVertexWithUV(p_147723_2_, p_147723_3_ + d8, p_147723_4_ + f11, d4, d5);
				tessellator.addVertexWithUV(p_147723_2_ + 0.25D, p_147723_3_ + d8, p_147723_4_ + f12, d4, d7);
				tessellator.addVertexWithUV(p_147723_2_ + 0.5D, d9, p_147723_4_ + f12, d6, d7);
				tessellator.addVertexWithUV(p_147723_2_ + 0.5D, d9, p_147723_4_ + f11, d6, d5);
				tessellator.addVertexWithUV(p_147723_2_ + 0.25D, p_147723_3_ + d8, p_147723_4_ + f11, d4, d5);
			} else {
				tessellator.addVertexWithUV(p_147723_2_ + 0.5D, d9, p_147723_4_ + f12, d4, d7);
				tessellator.addVertexWithUV(p_147723_2_ + 0.75D, p_147723_3_ + d8, p_147723_4_ + f12, d6, d7);
				tessellator.addVertexWithUV(p_147723_2_ + 0.75D, p_147723_3_ + d8, p_147723_4_ + f11, d6, d5);
				tessellator.addVertexWithUV(p_147723_2_ + 0.5D, d9, p_147723_4_ + f11, d4, d5);
				tessellator.addVertexWithUV(p_147723_2_ + 0.75D, p_147723_3_ + d8, p_147723_4_ + f12, d4, d7);
				tessellator.addVertexWithUV(p_147723_2_ + 1, p_147723_3_ + d8, p_147723_4_ + f12, d6, d7);
				tessellator.addVertexWithUV(p_147723_2_ + 1, p_147723_3_ + d8, p_147723_4_ + f11, d6, d5);
				tessellator.addVertexWithUV(p_147723_2_ + 0.75D, p_147723_3_ + d8, p_147723_4_ + f11, d4, d5);
			}
		}

		return true;
	}

	public boolean renderBlockTripWire(Block p_147756_1_, int p_147756_2_, int p_147756_3_, int p_147756_4_) {
		Tessellator tessellator = Tessellator.instance;
		IIcon iicon = getBlockIconFromSide(p_147756_1_, 0);
		int l = blockAccess.getBlockMetadata(p_147756_2_, p_147756_3_, p_147756_4_);
		boolean flag = (l & 4) == 4;
		boolean flag1 = (l & 2) == 2;

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
		}

		tessellator.setBrightness(
				p_147756_1_.getMixedBrightnessForBlock(blockAccess, p_147756_2_, p_147756_3_, p_147756_4_));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double d0 = iicon.getMinU();
		double d1 = iicon.getInterpolatedV(flag ? 2.0D : 0.0D);
		double d2 = iicon.getMaxU();
		double d3 = iicon.getInterpolatedV(flag ? 4.0D : 2.0D);
		double d4 = (flag1 ? 3.5F : 1.5F) / 16.0D;
		boolean flag2 = BlockTripWire.func_150139_a(blockAccess, p_147756_2_, p_147756_3_, p_147756_4_, l, 1);
		boolean flag3 = BlockTripWire.func_150139_a(blockAccess, p_147756_2_, p_147756_3_, p_147756_4_, l, 3);
		boolean flag4 = BlockTripWire.func_150139_a(blockAccess, p_147756_2_, p_147756_3_, p_147756_4_, l, 2);
		boolean flag5 = BlockTripWire.func_150139_a(blockAccess, p_147756_2_, p_147756_3_, p_147756_4_, l, 0);
		float f = 0.03125F;
		float f1 = 0.5F - f / 2.0F;
		float f2 = f1 + f;

		if (!flag4 && !flag3 && !flag5 && !flag2) {
			flag4 = true;
			flag5 = true;
		}

		if (flag4) {
			tessellator.addVertexWithUV(p_147756_2_ + f1, p_147756_3_ + d4, p_147756_4_ + 0.25D, d0, d1);
			tessellator.addVertexWithUV(p_147756_2_ + f2, p_147756_3_ + d4, p_147756_4_ + 0.25D, d0, d3);
			tessellator.addVertexWithUV(p_147756_2_ + f2, p_147756_3_ + d4, p_147756_4_, d2, d3);
			tessellator.addVertexWithUV(p_147756_2_ + f1, p_147756_3_ + d4, p_147756_4_, d2, d1);
			tessellator.addVertexWithUV(p_147756_2_ + f1, p_147756_3_ + d4, p_147756_4_, d2, d1);
			tessellator.addVertexWithUV(p_147756_2_ + f2, p_147756_3_ + d4, p_147756_4_, d2, d3);
			tessellator.addVertexWithUV(p_147756_2_ + f2, p_147756_3_ + d4, p_147756_4_ + 0.25D, d0, d3);
			tessellator.addVertexWithUV(p_147756_2_ + f1, p_147756_3_ + d4, p_147756_4_ + 0.25D, d0, d1);
		}

		if (flag4 || flag5 && !flag3 && !flag2) {
			tessellator.addVertexWithUV(p_147756_2_ + f1, p_147756_3_ + d4, p_147756_4_ + 0.5D, d0, d1);
			tessellator.addVertexWithUV(p_147756_2_ + f2, p_147756_3_ + d4, p_147756_4_ + 0.5D, d0, d3);
			tessellator.addVertexWithUV(p_147756_2_ + f2, p_147756_3_ + d4, p_147756_4_ + 0.25D, d2, d3);
			tessellator.addVertexWithUV(p_147756_2_ + f1, p_147756_3_ + d4, p_147756_4_ + 0.25D, d2, d1);
			tessellator.addVertexWithUV(p_147756_2_ + f1, p_147756_3_ + d4, p_147756_4_ + 0.25D, d2, d1);
			tessellator.addVertexWithUV(p_147756_2_ + f2, p_147756_3_ + d4, p_147756_4_ + 0.25D, d2, d3);
			tessellator.addVertexWithUV(p_147756_2_ + f2, p_147756_3_ + d4, p_147756_4_ + 0.5D, d0, d3);
			tessellator.addVertexWithUV(p_147756_2_ + f1, p_147756_3_ + d4, p_147756_4_ + 0.5D, d0, d1);
		}

		if (flag5 || flag4 && !flag3 && !flag2) {
			tessellator.addVertexWithUV(p_147756_2_ + f1, p_147756_3_ + d4, p_147756_4_ + 0.75D, d0, d1);
			tessellator.addVertexWithUV(p_147756_2_ + f2, p_147756_3_ + d4, p_147756_4_ + 0.75D, d0, d3);
			tessellator.addVertexWithUV(p_147756_2_ + f2, p_147756_3_ + d4, p_147756_4_ + 0.5D, d2, d3);
			tessellator.addVertexWithUV(p_147756_2_ + f1, p_147756_3_ + d4, p_147756_4_ + 0.5D, d2, d1);
			tessellator.addVertexWithUV(p_147756_2_ + f1, p_147756_3_ + d4, p_147756_4_ + 0.5D, d2, d1);
			tessellator.addVertexWithUV(p_147756_2_ + f2, p_147756_3_ + d4, p_147756_4_ + 0.5D, d2, d3);
			tessellator.addVertexWithUV(p_147756_2_ + f2, p_147756_3_ + d4, p_147756_4_ + 0.75D, d0, d3);
			tessellator.addVertexWithUV(p_147756_2_ + f1, p_147756_3_ + d4, p_147756_4_ + 0.75D, d0, d1);
		}

		if (flag5) {
			tessellator.addVertexWithUV(p_147756_2_ + f1, p_147756_3_ + d4, p_147756_4_ + 1, d0, d1);
			tessellator.addVertexWithUV(p_147756_2_ + f2, p_147756_3_ + d4, p_147756_4_ + 1, d0, d3);
			tessellator.addVertexWithUV(p_147756_2_ + f2, p_147756_3_ + d4, p_147756_4_ + 0.75D, d2, d3);
			tessellator.addVertexWithUV(p_147756_2_ + f1, p_147756_3_ + d4, p_147756_4_ + 0.75D, d2, d1);
			tessellator.addVertexWithUV(p_147756_2_ + f1, p_147756_3_ + d4, p_147756_4_ + 0.75D, d2, d1);
			tessellator.addVertexWithUV(p_147756_2_ + f2, p_147756_3_ + d4, p_147756_4_ + 0.75D, d2, d3);
			tessellator.addVertexWithUV(p_147756_2_ + f2, p_147756_3_ + d4, p_147756_4_ + 1, d0, d3);
			tessellator.addVertexWithUV(p_147756_2_ + f1, p_147756_3_ + d4, p_147756_4_ + 1, d0, d1);
		}

		if (flag2) {
			tessellator.addVertexWithUV(p_147756_2_, p_147756_3_ + d4, p_147756_4_ + f2, d0, d3);
			tessellator.addVertexWithUV(p_147756_2_ + 0.25D, p_147756_3_ + d4, p_147756_4_ + f2, d2, d3);
			tessellator.addVertexWithUV(p_147756_2_ + 0.25D, p_147756_3_ + d4, p_147756_4_ + f1, d2, d1);
			tessellator.addVertexWithUV(p_147756_2_, p_147756_3_ + d4, p_147756_4_ + f1, d0, d1);
			tessellator.addVertexWithUV(p_147756_2_, p_147756_3_ + d4, p_147756_4_ + f1, d0, d1);
			tessellator.addVertexWithUV(p_147756_2_ + 0.25D, p_147756_3_ + d4, p_147756_4_ + f1, d2, d1);
			tessellator.addVertexWithUV(p_147756_2_ + 0.25D, p_147756_3_ + d4, p_147756_4_ + f2, d2, d3);
			tessellator.addVertexWithUV(p_147756_2_, p_147756_3_ + d4, p_147756_4_ + f2, d0, d3);
		}

		if (flag2 || flag3 && !flag4 && !flag5) {
			tessellator.addVertexWithUV(p_147756_2_ + 0.25D, p_147756_3_ + d4, p_147756_4_ + f2, d0, d3);
			tessellator.addVertexWithUV(p_147756_2_ + 0.5D, p_147756_3_ + d4, p_147756_4_ + f2, d2, d3);
			tessellator.addVertexWithUV(p_147756_2_ + 0.5D, p_147756_3_ + d4, p_147756_4_ + f1, d2, d1);
			tessellator.addVertexWithUV(p_147756_2_ + 0.25D, p_147756_3_ + d4, p_147756_4_ + f1, d0, d1);
			tessellator.addVertexWithUV(p_147756_2_ + 0.25D, p_147756_3_ + d4, p_147756_4_ + f1, d0, d1);
			tessellator.addVertexWithUV(p_147756_2_ + 0.5D, p_147756_3_ + d4, p_147756_4_ + f1, d2, d1);
			tessellator.addVertexWithUV(p_147756_2_ + 0.5D, p_147756_3_ + d4, p_147756_4_ + f2, d2, d3);
			tessellator.addVertexWithUV(p_147756_2_ + 0.25D, p_147756_3_ + d4, p_147756_4_ + f2, d0, d3);
		}

		if (flag3 || flag2 && !flag4 && !flag5) {
			tessellator.addVertexWithUV(p_147756_2_ + 0.5D, p_147756_3_ + d4, p_147756_4_ + f2, d0, d3);
			tessellator.addVertexWithUV(p_147756_2_ + 0.75D, p_147756_3_ + d4, p_147756_4_ + f2, d2, d3);
			tessellator.addVertexWithUV(p_147756_2_ + 0.75D, p_147756_3_ + d4, p_147756_4_ + f1, d2, d1);
			tessellator.addVertexWithUV(p_147756_2_ + 0.5D, p_147756_3_ + d4, p_147756_4_ + f1, d0, d1);
			tessellator.addVertexWithUV(p_147756_2_ + 0.5D, p_147756_3_ + d4, p_147756_4_ + f1, d0, d1);
			tessellator.addVertexWithUV(p_147756_2_ + 0.75D, p_147756_3_ + d4, p_147756_4_ + f1, d2, d1);
			tessellator.addVertexWithUV(p_147756_2_ + 0.75D, p_147756_3_ + d4, p_147756_4_ + f2, d2, d3);
			tessellator.addVertexWithUV(p_147756_2_ + 0.5D, p_147756_3_ + d4, p_147756_4_ + f2, d0, d3);
		}

		if (flag3) {
			tessellator.addVertexWithUV(p_147756_2_ + 0.75D, p_147756_3_ + d4, p_147756_4_ + f2, d0, d3);
			tessellator.addVertexWithUV(p_147756_2_ + 1, p_147756_3_ + d4, p_147756_4_ + f2, d2, d3);
			tessellator.addVertexWithUV(p_147756_2_ + 1, p_147756_3_ + d4, p_147756_4_ + f1, d2, d1);
			tessellator.addVertexWithUV(p_147756_2_ + 0.75D, p_147756_3_ + d4, p_147756_4_ + f1, d0, d1);
			tessellator.addVertexWithUV(p_147756_2_ + 0.75D, p_147756_3_ + d4, p_147756_4_ + f1, d0, d1);
			tessellator.addVertexWithUV(p_147756_2_ + 1, p_147756_3_ + d4, p_147756_4_ + f1, d2, d1);
			tessellator.addVertexWithUV(p_147756_2_ + 1, p_147756_3_ + d4, p_147756_4_ + f2, d2, d3);
			tessellator.addVertexWithUV(p_147756_2_ + 0.75D, p_147756_3_ + d4, p_147756_4_ + f2, d0, d3);
		}

		return true;
	}

	public boolean renderBlockFire(BlockFire p_147801_1_, int p_147801_2_, int p_147801_3_, int p_147801_4_) {
		Tessellator tessellator = Tessellator.instance;
		IIcon iicon = p_147801_1_.getFireIcon(0);
		IIcon iicon1 = p_147801_1_.getFireIcon(1);
		IIcon iicon2 = iicon;

		if (hasOverrideBlockTexture()) {
			iicon2 = overrideBlockTexture;
		}

		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		tessellator.setBrightness(
				p_147801_1_.getMixedBrightnessForBlock(blockAccess, p_147801_2_, p_147801_3_, p_147801_4_));
		double d0 = iicon2.getMinU();
		double d1 = iicon2.getMinV();
		double d2 = iicon2.getMaxU();
		double d3 = iicon2.getMaxV();
		float f = 1.4F;
		double d5;
		double d6;
		double d7;
		double d8;
		double d9;
		double d10;
		double d11;

		if (!World.doesBlockHaveSolidTopSurface(blockAccess, p_147801_2_, p_147801_3_ - 1, p_147801_4_)
				&& !Blocks.fire.canCatchFire(blockAccess, p_147801_2_, p_147801_3_ - 1, p_147801_4_, UP)) {
			float f2 = 0.2F;
			float f1 = 0.0625F;

			if ((p_147801_2_ + p_147801_3_ + p_147801_4_ & 1) == 1) {
				d0 = iicon1.getMinU();
				d1 = iicon1.getMinV();
				d2 = iicon1.getMaxU();
				d3 = iicon1.getMaxV();
			}

			if ((p_147801_2_ / 2 + p_147801_3_ / 2 + p_147801_4_ / 2 & 1) == 1) {
				d5 = d2;
				d2 = d0;
				d0 = d5;
			}

			if (Blocks.fire.canCatchFire(blockAccess, p_147801_2_ - 1, p_147801_3_, p_147801_4_, EAST)) {
				tessellator.addVertexWithUV(p_147801_2_ + f2, p_147801_3_ + f + f1, p_147801_4_ + 1, d2, d1);
				tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0 + f1, p_147801_4_ + 1, d2, d3);
				tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0 + f1, p_147801_4_ + 0, d0, d3);
				tessellator.addVertexWithUV(p_147801_2_ + f2, p_147801_3_ + f + f1, p_147801_4_ + 0, d0, d1);
				tessellator.addVertexWithUV(p_147801_2_ + f2, p_147801_3_ + f + f1, p_147801_4_ + 0, d0, d1);
				tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0 + f1, p_147801_4_ + 0, d0, d3);
				tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0 + f1, p_147801_4_ + 1, d2, d3);
				tessellator.addVertexWithUV(p_147801_2_ + f2, p_147801_3_ + f + f1, p_147801_4_ + 1, d2, d1);
			}

			if (Blocks.fire.canCatchFire(blockAccess, p_147801_2_ + 1, p_147801_3_, p_147801_4_, WEST)) {
				tessellator.addVertexWithUV(p_147801_2_ + 1 - f2, p_147801_3_ + f + f1, p_147801_4_ + 0, d0, d1);
				tessellator.addVertexWithUV(p_147801_2_ + 1 - 0, p_147801_3_ + 0 + f1, p_147801_4_ + 0, d0, d3);
				tessellator.addVertexWithUV(p_147801_2_ + 1 - 0, p_147801_3_ + 0 + f1, p_147801_4_ + 1, d2, d3);
				tessellator.addVertexWithUV(p_147801_2_ + 1 - f2, p_147801_3_ + f + f1, p_147801_4_ + 1, d2, d1);
				tessellator.addVertexWithUV(p_147801_2_ + 1 - f2, p_147801_3_ + f + f1, p_147801_4_ + 1, d2, d1);
				tessellator.addVertexWithUV(p_147801_2_ + 1 - 0, p_147801_3_ + 0 + f1, p_147801_4_ + 1, d2, d3);
				tessellator.addVertexWithUV(p_147801_2_ + 1 - 0, p_147801_3_ + 0 + f1, p_147801_4_ + 0, d0, d3);
				tessellator.addVertexWithUV(p_147801_2_ + 1 - f2, p_147801_3_ + f + f1, p_147801_4_ + 0, d0, d1);
			}

			if (Blocks.fire.canCatchFire(blockAccess, p_147801_2_, p_147801_3_, p_147801_4_ - 1, SOUTH)) {
				tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + f + f1, p_147801_4_ + f2, d2, d1);
				tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0 + f1, p_147801_4_ + 0, d2, d3);
				tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0 + f1, p_147801_4_ + 0, d0, d3);
				tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + f + f1, p_147801_4_ + f2, d0, d1);
				tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + f + f1, p_147801_4_ + f2, d0, d1);
				tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0 + f1, p_147801_4_ + 0, d0, d3);
				tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0 + f1, p_147801_4_ + 0, d2, d3);
				tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + f + f1, p_147801_4_ + f2, d2, d1);
			}

			if (Blocks.fire.canCatchFire(blockAccess, p_147801_2_, p_147801_3_, p_147801_4_ + 1, NORTH)) {
				tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + f + f1, p_147801_4_ + 1 - f2, d0, d1);
				tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0 + f1, p_147801_4_ + 1 - 0, d0, d3);
				tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0 + f1, p_147801_4_ + 1 - 0, d2, d3);
				tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + f + f1, p_147801_4_ + 1 - f2, d2, d1);
				tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + f + f1, p_147801_4_ + 1 - f2, d2, d1);
				tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0 + f1, p_147801_4_ + 1 - 0, d2, d3);
				tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0 + f1, p_147801_4_ + 1 - 0, d0, d3);
				tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + f + f1, p_147801_4_ + 1 - f2, d0, d1);
			}

			if (Blocks.fire.canCatchFire(blockAccess, p_147801_2_, p_147801_3_ + 1, p_147801_4_, DOWN)) {
				d5 = p_147801_2_ + 0.5D + 0.5D;
				d6 = p_147801_2_ + 0.5D - 0.5D;
				d7 = p_147801_4_ + 0.5D + 0.5D;
				d8 = p_147801_4_ + 0.5D - 0.5D;
				d9 = p_147801_2_ + 0.5D - 0.5D;
				d10 = p_147801_2_ + 0.5D + 0.5D;
				d11 = p_147801_4_ + 0.5D - 0.5D;
				double d12 = p_147801_4_ + 0.5D + 0.5D;
				d0 = iicon.getMinU();
				d1 = iicon.getMinV();
				d2 = iicon.getMaxU();
				d3 = iicon.getMaxV();
				++p_147801_3_;
				f = -0.2F;

				if ((p_147801_2_ + p_147801_3_ + p_147801_4_ & 1) == 0) {
					tessellator.addVertexWithUV(d9, p_147801_3_ + f, p_147801_4_ + 0, d2, d1);
					tessellator.addVertexWithUV(d5, p_147801_3_ + 0, p_147801_4_ + 0, d2, d3);
					tessellator.addVertexWithUV(d5, p_147801_3_ + 0, p_147801_4_ + 1, d0, d3);
					tessellator.addVertexWithUV(d9, p_147801_3_ + f, p_147801_4_ + 1, d0, d1);
					d0 = iicon1.getMinU();
					d1 = iicon1.getMinV();
					d2 = iicon1.getMaxU();
					d3 = iicon1.getMaxV();
					tessellator.addVertexWithUV(d10, p_147801_3_ + f, p_147801_4_ + 1, d2, d1);
					tessellator.addVertexWithUV(d6, p_147801_3_ + 0, p_147801_4_ + 1, d2, d3);
					tessellator.addVertexWithUV(d6, p_147801_3_ + 0, p_147801_4_ + 0, d0, d3);
					tessellator.addVertexWithUV(d10, p_147801_3_ + f, p_147801_4_ + 0, d0, d1);
				} else {
					tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + f, d12, d2, d1);
					tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0, d8, d2, d3);
					tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0, d8, d0, d3);
					tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + f, d12, d0, d1);
					d0 = iicon1.getMinU();
					d1 = iicon1.getMinV();
					d2 = iicon1.getMaxU();
					d3 = iicon1.getMaxV();
					tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + f, d11, d2, d1);
					tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0, d7, d2, d3);
					tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0, d7, d0, d3);
					tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + f, d11, d0, d1);
				}
			}
		} else {
			double d4 = p_147801_2_ + 0.5D + 0.2D;
			d5 = p_147801_2_ + 0.5D - 0.2D;
			d6 = p_147801_4_ + 0.5D + 0.2D;
			d7 = p_147801_4_ + 0.5D - 0.2D;
			d8 = p_147801_2_ + 0.5D - 0.3D;
			d9 = p_147801_2_ + 0.5D + 0.3D;
			d10 = p_147801_4_ + 0.5D - 0.3D;
			d11 = p_147801_4_ + 0.5D + 0.3D;
			tessellator.addVertexWithUV(d8, p_147801_3_ + f, p_147801_4_ + 1, d2, d1);
			tessellator.addVertexWithUV(d4, p_147801_3_ + 0, p_147801_4_ + 1, d2, d3);
			tessellator.addVertexWithUV(d4, p_147801_3_ + 0, p_147801_4_ + 0, d0, d3);
			tessellator.addVertexWithUV(d8, p_147801_3_ + f, p_147801_4_ + 0, d0, d1);
			tessellator.addVertexWithUV(d9, p_147801_3_ + f, p_147801_4_ + 0, d2, d1);
			tessellator.addVertexWithUV(d5, p_147801_3_ + 0, p_147801_4_ + 0, d2, d3);
			tessellator.addVertexWithUV(d5, p_147801_3_ + 0, p_147801_4_ + 1, d0, d3);
			tessellator.addVertexWithUV(d9, p_147801_3_ + f, p_147801_4_ + 1, d0, d1);
			d0 = iicon1.getMinU();
			d1 = iicon1.getMinV();
			d2 = iicon1.getMaxU();
			d3 = iicon1.getMaxV();
			tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + f, d11, d2, d1);
			tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0, d7, d2, d3);
			tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0, d7, d0, d3);
			tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + f, d11, d0, d1);
			tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + f, d10, d2, d1);
			tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0, d6, d2, d3);
			tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0, d6, d0, d3);
			tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + f, d10, d0, d1);
			d4 = p_147801_2_ + 0.5D - 0.5D;
			d5 = p_147801_2_ + 0.5D + 0.5D;
			d6 = p_147801_4_ + 0.5D - 0.5D;
			d7 = p_147801_4_ + 0.5D + 0.5D;
			d8 = p_147801_2_ + 0.5D - 0.4D;
			d9 = p_147801_2_ + 0.5D + 0.4D;
			d10 = p_147801_4_ + 0.5D - 0.4D;
			d11 = p_147801_4_ + 0.5D + 0.4D;
			tessellator.addVertexWithUV(d8, p_147801_3_ + f, p_147801_4_ + 0, d0, d1);
			tessellator.addVertexWithUV(d4, p_147801_3_ + 0, p_147801_4_ + 0, d0, d3);
			tessellator.addVertexWithUV(d4, p_147801_3_ + 0, p_147801_4_ + 1, d2, d3);
			tessellator.addVertexWithUV(d8, p_147801_3_ + f, p_147801_4_ + 1, d2, d1);
			tessellator.addVertexWithUV(d9, p_147801_3_ + f, p_147801_4_ + 1, d0, d1);
			tessellator.addVertexWithUV(d5, p_147801_3_ + 0, p_147801_4_ + 1, d0, d3);
			tessellator.addVertexWithUV(d5, p_147801_3_ + 0, p_147801_4_ + 0, d2, d3);
			tessellator.addVertexWithUV(d9, p_147801_3_ + f, p_147801_4_ + 0, d2, d1);
			d0 = iicon.getMinU();
			d1 = iicon.getMinV();
			d2 = iicon.getMaxU();
			d3 = iicon.getMaxV();
			tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + f, d11, d0, d1);
			tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0, d7, d0, d3);
			tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0, d7, d2, d3);
			tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + f, d11, d2, d1);
			tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + f, d10, d0, d1);
			tessellator.addVertexWithUV(p_147801_2_ + 1, p_147801_3_ + 0, d6, d0, d3);
			tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + 0, d6, d2, d3);
			tessellator.addVertexWithUV(p_147801_2_ + 0, p_147801_3_ + f, d10, d2, d1);
		}

		return true;
	}

	public boolean renderBlockRedstoneWire(Block p_147788_1_, int p_147788_2_, int p_147788_3_, int p_147788_4_) {
		Tessellator tessellator = Tessellator.instance;
		int l = blockAccess.getBlockMetadata(p_147788_2_, p_147788_3_, p_147788_4_);
		IIcon iicon = BlockRedstoneWire.getRedstoneWireIcon("cross");
		IIcon iicon1 = BlockRedstoneWire.getRedstoneWireIcon("line");
		IIcon iicon2 = BlockRedstoneWire.getRedstoneWireIcon("cross_overlay");
		IIcon iicon3 = BlockRedstoneWire.getRedstoneWireIcon("line_overlay");
		tessellator.setBrightness(
				p_147788_1_.getMixedBrightnessForBlock(blockAccess, p_147788_2_, p_147788_3_, p_147788_4_));
		float f = l / 15.0F;
		float f1 = f * 0.6F + 0.4F;

		if (l == 0) {
			f1 = 0.3F;
		}

		float f2 = f * f * 0.7F - 0.5F;
		float f3 = f * f * 0.6F - 0.7F;

		if (f2 < 0.0F) {
			f2 = 0.0F;
		}

		if (f3 < 0.0F) {
			f3 = 0.0F;
		}

		tessellator.setColorOpaque_F(f1, f2, f3);
		boolean flag = BlockRedstoneWire.isPowerProviderOrWire(blockAccess, p_147788_2_ - 1, p_147788_3_, p_147788_4_,
				1)
				|| !blockAccess.getBlock(p_147788_2_ - 1, p_147788_3_, p_147788_4_).isBlockNormalCube()
						&& BlockRedstoneWire.isPowerProviderOrWire(blockAccess, p_147788_2_ - 1, p_147788_3_ - 1,
								p_147788_4_, -1);
		boolean flag1 = BlockRedstoneWire.isPowerProviderOrWire(blockAccess, p_147788_2_ + 1, p_147788_3_, p_147788_4_,
				3)
				|| !blockAccess.getBlock(p_147788_2_ + 1, p_147788_3_, p_147788_4_).isBlockNormalCube()
						&& BlockRedstoneWire.isPowerProviderOrWire(blockAccess, p_147788_2_ + 1, p_147788_3_ - 1,
								p_147788_4_, -1);
		boolean flag2 = BlockRedstoneWire.isPowerProviderOrWire(blockAccess, p_147788_2_, p_147788_3_, p_147788_4_ - 1,
				2)
				|| !blockAccess.getBlock(p_147788_2_, p_147788_3_, p_147788_4_ - 1).isBlockNormalCube()
						&& BlockRedstoneWire.isPowerProviderOrWire(blockAccess, p_147788_2_, p_147788_3_ - 1,
								p_147788_4_ - 1, -1);
		boolean flag3 = BlockRedstoneWire.isPowerProviderOrWire(blockAccess, p_147788_2_, p_147788_3_, p_147788_4_ + 1,
				0)
				|| !blockAccess.getBlock(p_147788_2_, p_147788_3_, p_147788_4_ + 1).isBlockNormalCube()
						&& BlockRedstoneWire.isPowerProviderOrWire(blockAccess, p_147788_2_, p_147788_3_ - 1,
								p_147788_4_ + 1, -1);

		if (!blockAccess.getBlock(p_147788_2_, p_147788_3_ + 1, p_147788_4_).isBlockNormalCube()) {
			if (blockAccess.getBlock(p_147788_2_ - 1, p_147788_3_, p_147788_4_).isBlockNormalCube() && BlockRedstoneWire
					.isPowerProviderOrWire(blockAccess, p_147788_2_ - 1, p_147788_3_ + 1, p_147788_4_, -1)) {
				flag = true;
			}

			if (blockAccess.getBlock(p_147788_2_ + 1, p_147788_3_, p_147788_4_).isBlockNormalCube() && BlockRedstoneWire
					.isPowerProviderOrWire(blockAccess, p_147788_2_ + 1, p_147788_3_ + 1, p_147788_4_, -1)) {
				flag1 = true;
			}

			if (blockAccess.getBlock(p_147788_2_, p_147788_3_, p_147788_4_ - 1).isBlockNormalCube() && BlockRedstoneWire
					.isPowerProviderOrWire(blockAccess, p_147788_2_, p_147788_3_ + 1, p_147788_4_ - 1, -1)) {
				flag2 = true;
			}

			if (blockAccess.getBlock(p_147788_2_, p_147788_3_, p_147788_4_ + 1).isBlockNormalCube() && BlockRedstoneWire
					.isPowerProviderOrWire(blockAccess, p_147788_2_, p_147788_3_ + 1, p_147788_4_ + 1, -1)) {
				flag3 = true;
			}
		}

		float f4 = p_147788_2_ + 0;
		float f5 = p_147788_2_ + 1;
		float f6 = p_147788_4_ + 0;
		float f7 = p_147788_4_ + 1;
		int i1 = 0;

		if ((flag || flag1) && !flag2 && !flag3) {
			i1 = 1;
		}

		if ((flag2 || flag3) && !flag1 && !flag) {
			i1 = 2;
		}

		if (i1 == 0) {
			int j1 = 0;
			int k1 = 0;
			int l1 = 16;
			int i2 = 16;
			if (!flag) {
				f4 += 0.3125F;
			}

			if (!flag) {
				j1 += 5;
			}

			if (!flag1) {
				f5 -= 0.3125F;
			}

			if (!flag1) {
				l1 -= 5;
			}

			if (!flag2) {
				f6 += 0.3125F;
			}

			if (!flag2) {
				k1 += 5;
			}

			if (!flag3) {
				f7 -= 0.3125F;
			}

			if (!flag3) {
				i2 -= 5;
			}

			tessellator.addVertexWithUV(f5, p_147788_3_ + 0.015625D, f7, iicon.getInterpolatedU(l1),
					iicon.getInterpolatedV(i2));
			tessellator.addVertexWithUV(f5, p_147788_3_ + 0.015625D, f6, iicon.getInterpolatedU(l1),
					iicon.getInterpolatedV(k1));
			tessellator.addVertexWithUV(f4, p_147788_3_ + 0.015625D, f6, iicon.getInterpolatedU(j1),
					iicon.getInterpolatedV(k1));
			tessellator.addVertexWithUV(f4, p_147788_3_ + 0.015625D, f7, iicon.getInterpolatedU(j1),
					iicon.getInterpolatedV(i2));
			tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			tessellator.addVertexWithUV(f5, p_147788_3_ + 0.015625D, f7, iicon2.getInterpolatedU(l1),
					iicon2.getInterpolatedV(i2));
			tessellator.addVertexWithUV(f5, p_147788_3_ + 0.015625D, f6, iicon2.getInterpolatedU(l1),
					iicon2.getInterpolatedV(k1));
			tessellator.addVertexWithUV(f4, p_147788_3_ + 0.015625D, f6, iicon2.getInterpolatedU(j1),
					iicon2.getInterpolatedV(k1));
			tessellator.addVertexWithUV(f4, p_147788_3_ + 0.015625D, f7, iicon2.getInterpolatedU(j1),
					iicon2.getInterpolatedV(i2));
		} else if (i1 == 1) {
			tessellator.addVertexWithUV(f5, p_147788_3_ + 0.015625D, f7, iicon1.getMaxU(), iicon1.getMaxV());
			tessellator.addVertexWithUV(f5, p_147788_3_ + 0.015625D, f6, iicon1.getMaxU(), iicon1.getMinV());
			tessellator.addVertexWithUV(f4, p_147788_3_ + 0.015625D, f6, iicon1.getMinU(), iicon1.getMinV());
			tessellator.addVertexWithUV(f4, p_147788_3_ + 0.015625D, f7, iicon1.getMinU(), iicon1.getMaxV());
			tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			tessellator.addVertexWithUV(f5, p_147788_3_ + 0.015625D, f7, iicon3.getMaxU(), iicon3.getMaxV());
			tessellator.addVertexWithUV(f5, p_147788_3_ + 0.015625D, f6, iicon3.getMaxU(), iicon3.getMinV());
			tessellator.addVertexWithUV(f4, p_147788_3_ + 0.015625D, f6, iicon3.getMinU(), iicon3.getMinV());
			tessellator.addVertexWithUV(f4, p_147788_3_ + 0.015625D, f7, iicon3.getMinU(), iicon3.getMaxV());
		} else {
			tessellator.addVertexWithUV(f5, p_147788_3_ + 0.015625D, f7, iicon1.getMaxU(), iicon1.getMaxV());
			tessellator.addVertexWithUV(f5, p_147788_3_ + 0.015625D, f6, iicon1.getMinU(), iicon1.getMaxV());
			tessellator.addVertexWithUV(f4, p_147788_3_ + 0.015625D, f6, iicon1.getMinU(), iicon1.getMinV());
			tessellator.addVertexWithUV(f4, p_147788_3_ + 0.015625D, f7, iicon1.getMaxU(), iicon1.getMinV());
			tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			tessellator.addVertexWithUV(f5, p_147788_3_ + 0.015625D, f7, iicon3.getMaxU(), iicon3.getMaxV());
			tessellator.addVertexWithUV(f5, p_147788_3_ + 0.015625D, f6, iicon3.getMinU(), iicon3.getMaxV());
			tessellator.addVertexWithUV(f4, p_147788_3_ + 0.015625D, f6, iicon3.getMinU(), iicon3.getMinV());
			tessellator.addVertexWithUV(f4, p_147788_3_ + 0.015625D, f7, iicon3.getMaxU(), iicon3.getMinV());
		}

		if (!blockAccess.getBlock(p_147788_2_, p_147788_3_ + 1, p_147788_4_).isBlockNormalCube()) {
			if (blockAccess.getBlock(p_147788_2_ - 1, p_147788_3_, p_147788_4_).isBlockNormalCube()
					&& blockAccess.getBlock(p_147788_2_ - 1, p_147788_3_ + 1, p_147788_4_) == Blocks.redstone_wire) {
				tessellator.setColorOpaque_F(f1, f2, f3);
				tessellator.addVertexWithUV(p_147788_2_ + 0.015625D, p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 1,
						iicon1.getMaxU(), iicon1.getMinV());
				tessellator.addVertexWithUV(p_147788_2_ + 0.015625D, p_147788_3_ + 0, p_147788_4_ + 1, iicon1.getMinU(),
						iicon1.getMinV());
				tessellator.addVertexWithUV(p_147788_2_ + 0.015625D, p_147788_3_ + 0, p_147788_4_ + 0, iicon1.getMinU(),
						iicon1.getMaxV());
				tessellator.addVertexWithUV(p_147788_2_ + 0.015625D, p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 0,
						iicon1.getMaxU(), iicon1.getMaxV());
				tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
				tessellator.addVertexWithUV(p_147788_2_ + 0.015625D, p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 1,
						iicon3.getMaxU(), iicon3.getMinV());
				tessellator.addVertexWithUV(p_147788_2_ + 0.015625D, p_147788_3_ + 0, p_147788_4_ + 1, iicon3.getMinU(),
						iicon3.getMinV());
				tessellator.addVertexWithUV(p_147788_2_ + 0.015625D, p_147788_3_ + 0, p_147788_4_ + 0, iicon3.getMinU(),
						iicon3.getMaxV());
				tessellator.addVertexWithUV(p_147788_2_ + 0.015625D, p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 0,
						iicon3.getMaxU(), iicon3.getMaxV());
			}

			if (blockAccess.getBlock(p_147788_2_ + 1, p_147788_3_, p_147788_4_).isBlockNormalCube()
					&& blockAccess.getBlock(p_147788_2_ + 1, p_147788_3_ + 1, p_147788_4_) == Blocks.redstone_wire) {
				tessellator.setColorOpaque_F(f1, f2, f3);
				tessellator.addVertexWithUV(p_147788_2_ + 1 - 0.015625D, p_147788_3_ + 0, p_147788_4_ + 1,
						iicon1.getMinU(), iicon1.getMaxV());
				tessellator.addVertexWithUV(p_147788_2_ + 1 - 0.015625D, p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 1,
						iicon1.getMaxU(), iicon1.getMaxV());
				tessellator.addVertexWithUV(p_147788_2_ + 1 - 0.015625D, p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 0,
						iicon1.getMaxU(), iicon1.getMinV());
				tessellator.addVertexWithUV(p_147788_2_ + 1 - 0.015625D, p_147788_3_ + 0, p_147788_4_ + 0,
						iicon1.getMinU(), iicon1.getMinV());
				tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
				tessellator.addVertexWithUV(p_147788_2_ + 1 - 0.015625D, p_147788_3_ + 0, p_147788_4_ + 1,
						iicon3.getMinU(), iicon3.getMaxV());
				tessellator.addVertexWithUV(p_147788_2_ + 1 - 0.015625D, p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 1,
						iicon3.getMaxU(), iicon3.getMaxV());
				tessellator.addVertexWithUV(p_147788_2_ + 1 - 0.015625D, p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 0,
						iicon3.getMaxU(), iicon3.getMinV());
				tessellator.addVertexWithUV(p_147788_2_ + 1 - 0.015625D, p_147788_3_ + 0, p_147788_4_ + 0,
						iicon3.getMinU(), iicon3.getMinV());
			}

			if (blockAccess.getBlock(p_147788_2_, p_147788_3_, p_147788_4_ - 1).isBlockNormalCube()
					&& blockAccess.getBlock(p_147788_2_, p_147788_3_ + 1, p_147788_4_ - 1) == Blocks.redstone_wire) {
				tessellator.setColorOpaque_F(f1, f2, f3);
				tessellator.addVertexWithUV(p_147788_2_ + 1, p_147788_3_ + 0, p_147788_4_ + 0.015625D, iicon1.getMinU(),
						iicon1.getMaxV());
				tessellator.addVertexWithUV(p_147788_2_ + 1, p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 0.015625D,
						iicon1.getMaxU(), iicon1.getMaxV());
				tessellator.addVertexWithUV(p_147788_2_ + 0, p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 0.015625D,
						iicon1.getMaxU(), iicon1.getMinV());
				tessellator.addVertexWithUV(p_147788_2_ + 0, p_147788_3_ + 0, p_147788_4_ + 0.015625D, iicon1.getMinU(),
						iicon1.getMinV());
				tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
				tessellator.addVertexWithUV(p_147788_2_ + 1, p_147788_3_ + 0, p_147788_4_ + 0.015625D, iicon3.getMinU(),
						iicon3.getMaxV());
				tessellator.addVertexWithUV(p_147788_2_ + 1, p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 0.015625D,
						iicon3.getMaxU(), iicon3.getMaxV());
				tessellator.addVertexWithUV(p_147788_2_ + 0, p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 0.015625D,
						iicon3.getMaxU(), iicon3.getMinV());
				tessellator.addVertexWithUV(p_147788_2_ + 0, p_147788_3_ + 0, p_147788_4_ + 0.015625D, iicon3.getMinU(),
						iicon3.getMinV());
			}

			if (blockAccess.getBlock(p_147788_2_, p_147788_3_, p_147788_4_ + 1).isBlockNormalCube()
					&& blockAccess.getBlock(p_147788_2_, p_147788_3_ + 1, p_147788_4_ + 1) == Blocks.redstone_wire) {
				tessellator.setColorOpaque_F(f1, f2, f3);
				tessellator.addVertexWithUV(p_147788_2_ + 1, p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 1 - 0.015625D,
						iicon1.getMaxU(), iicon1.getMinV());
				tessellator.addVertexWithUV(p_147788_2_ + 1, p_147788_3_ + 0, p_147788_4_ + 1 - 0.015625D,
						iicon1.getMinU(), iicon1.getMinV());
				tessellator.addVertexWithUV(p_147788_2_ + 0, p_147788_3_ + 0, p_147788_4_ + 1 - 0.015625D,
						iicon1.getMinU(), iicon1.getMaxV());
				tessellator.addVertexWithUV(p_147788_2_ + 0, p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 1 - 0.015625D,
						iicon1.getMaxU(), iicon1.getMaxV());
				tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
				tessellator.addVertexWithUV(p_147788_2_ + 1, p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 1 - 0.015625D,
						iicon3.getMaxU(), iicon3.getMinV());
				tessellator.addVertexWithUV(p_147788_2_ + 1, p_147788_3_ + 0, p_147788_4_ + 1 - 0.015625D,
						iicon3.getMinU(), iicon3.getMinV());
				tessellator.addVertexWithUV(p_147788_2_ + 0, p_147788_3_ + 0, p_147788_4_ + 1 - 0.015625D,
						iicon3.getMinU(), iicon3.getMaxV());
				tessellator.addVertexWithUV(p_147788_2_ + 0, p_147788_3_ + 1 + 0.021875F, p_147788_4_ + 1 - 0.015625D,
						iicon3.getMaxU(), iicon3.getMaxV());
			}
		}

		return true;
	}

	public boolean renderBlockMinecartTrack(BlockRailBase p_147766_1_, int p_147766_2_, int p_147766_3_,
			int p_147766_4_) {
		Tessellator tessellator = Tessellator.instance;
		int l = blockAccess.getBlockMetadata(p_147766_2_, p_147766_3_, p_147766_4_);
		IIcon iicon = getBlockIconFromSideAndMetadata(p_147766_1_, 0, l);

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
		}

		if (p_147766_1_.isPowered()) {
			l &= 7;
		}

		tessellator.setBrightness(
				p_147766_1_.getMixedBrightnessForBlock(blockAccess, p_147766_2_, p_147766_3_, p_147766_4_));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double d0 = iicon.getMinU();
		double d1 = iicon.getMinV();
		double d2 = iicon.getMaxU();
		double d3 = iicon.getMaxV();
		double d4 = 0.0625D;
		double d5 = p_147766_2_ + 1;
		double d6 = p_147766_2_ + 1;
		double d7 = p_147766_2_ + 0;
		double d8 = p_147766_2_ + 0;
		double d9 = p_147766_4_ + 0;
		double d10 = p_147766_4_ + 1;
		double d11 = p_147766_4_ + 1;
		double d12 = p_147766_4_ + 0;
		double d13 = p_147766_3_ + d4;
		double d14 = p_147766_3_ + d4;
		double d15 = p_147766_3_ + d4;
		double d16 = p_147766_3_ + d4;

		if (l != 1 && l != 2 && l != 3 && l != 7) {
			if (l == 8) {
				d5 = d6 = p_147766_2_ + 0;
				d7 = d8 = p_147766_2_ + 1;
				d9 = d12 = p_147766_4_ + 1;
				d10 = d11 = p_147766_4_ + 0;
			} else if (l == 9) {
				d5 = d8 = p_147766_2_ + 0;
				d6 = d7 = p_147766_2_ + 1;
				d9 = d10 = p_147766_4_ + 0;
				d11 = d12 = p_147766_4_ + 1;
			}
		} else {
			d5 = d8 = p_147766_2_ + 1;
			d6 = d7 = p_147766_2_ + 0;
			d9 = d10 = p_147766_4_ + 1;
			d11 = d12 = p_147766_4_ + 0;
		}

		if (l != 2 && l != 4) {
			if (l == 3 || l == 5) {
				++d14;
				++d15;
			}
		} else {
			++d13;
			++d16;
		}

		tessellator.addVertexWithUV(d5, d13, d9, d2, d1);
		tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
		tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
		tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
		tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
		tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
		tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
		tessellator.addVertexWithUV(d5, d13, d9, d2, d1);
		return true;
	}

	public boolean renderBlockLadder(Block p_147794_1_, int p_147794_2_, int p_147794_3_, int p_147794_4_) {
		Tessellator tessellator = Tessellator.instance;
		IIcon iicon = getBlockIconFromSide(p_147794_1_, 0);

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
		}

		tessellator.setBrightness(
				p_147794_1_.getMixedBrightnessForBlock(blockAccess, p_147794_2_, p_147794_3_, p_147794_4_));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double d0 = iicon.getMinU();
		double d1 = iicon.getMinV();
		double d2 = iicon.getMaxU();
		double d3 = iicon.getMaxV();
		int l = blockAccess.getBlockMetadata(p_147794_2_, p_147794_3_, p_147794_4_);
		double d4 = 0.0D;
		double d5 = 0.05000000074505806D;

		if (l == 5) {
			tessellator.addVertexWithUV(p_147794_2_ + d5, p_147794_3_ + 1 + d4, p_147794_4_ + 1 + d4, d0, d1);
			tessellator.addVertexWithUV(p_147794_2_ + d5, p_147794_3_ + 0 - d4, p_147794_4_ + 1 + d4, d0, d3);
			tessellator.addVertexWithUV(p_147794_2_ + d5, p_147794_3_ + 0 - d4, p_147794_4_ + 0 - d4, d2, d3);
			tessellator.addVertexWithUV(p_147794_2_ + d5, p_147794_3_ + 1 + d4, p_147794_4_ + 0 - d4, d2, d1);
		}

		if (l == 4) {
			tessellator.addVertexWithUV(p_147794_2_ + 1 - d5, p_147794_3_ + 0 - d4, p_147794_4_ + 1 + d4, d2, d3);
			tessellator.addVertexWithUV(p_147794_2_ + 1 - d5, p_147794_3_ + 1 + d4, p_147794_4_ + 1 + d4, d2, d1);
			tessellator.addVertexWithUV(p_147794_2_ + 1 - d5, p_147794_3_ + 1 + d4, p_147794_4_ + 0 - d4, d0, d1);
			tessellator.addVertexWithUV(p_147794_2_ + 1 - d5, p_147794_3_ + 0 - d4, p_147794_4_ + 0 - d4, d0, d3);
		}

		if (l == 3) {
			tessellator.addVertexWithUV(p_147794_2_ + 1 + d4, p_147794_3_ + 0 - d4, p_147794_4_ + d5, d2, d3);
			tessellator.addVertexWithUV(p_147794_2_ + 1 + d4, p_147794_3_ + 1 + d4, p_147794_4_ + d5, d2, d1);
			tessellator.addVertexWithUV(p_147794_2_ + 0 - d4, p_147794_3_ + 1 + d4, p_147794_4_ + d5, d0, d1);
			tessellator.addVertexWithUV(p_147794_2_ + 0 - d4, p_147794_3_ + 0 - d4, p_147794_4_ + d5, d0, d3);
		}

		if (l == 2) {
			tessellator.addVertexWithUV(p_147794_2_ + 1 + d4, p_147794_3_ + 1 + d4, p_147794_4_ + 1 - d5, d0, d1);
			tessellator.addVertexWithUV(p_147794_2_ + 1 + d4, p_147794_3_ + 0 - d4, p_147794_4_ + 1 - d5, d0, d3);
			tessellator.addVertexWithUV(p_147794_2_ + 0 - d4, p_147794_3_ + 0 - d4, p_147794_4_ + 1 - d5, d2, d3);
			tessellator.addVertexWithUV(p_147794_2_ + 0 - d4, p_147794_3_ + 1 + d4, p_147794_4_ + 1 - d5, d2, d1);
		}

		return true;
	}

	public boolean renderBlockVine(Block p_147726_1_, int p_147726_2_, int p_147726_3_, int p_147726_4_) {
		Tessellator tessellator = Tessellator.instance;
		IIcon iicon = getBlockIconFromSide(p_147726_1_, 0);

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
		}

		tessellator.setBrightness(
				p_147726_1_.getMixedBrightnessForBlock(blockAccess, p_147726_2_, p_147726_3_, p_147726_4_));
		int l = p_147726_1_.colorMultiplier(blockAccess, p_147726_2_, p_147726_3_, p_147726_4_);
		float f = (l >> 16 & 255) / 255.0F;
		float f1 = (l >> 8 & 255) / 255.0F;
		float f2 = (l & 255) / 255.0F;
		tessellator.setColorOpaque_F(f, f1, f2);
		double d3 = iicon.getMinU();
		double d4 = iicon.getMinV();
		double d0 = iicon.getMaxU();
		double d1 = iicon.getMaxV();
		double d2 = 0.05000000074505806D;
		int i1 = blockAccess.getBlockMetadata(p_147726_2_, p_147726_3_, p_147726_4_);

		if ((i1 & 2) != 0) {
			tessellator.addVertexWithUV(p_147726_2_ + d2, p_147726_3_ + 1, p_147726_4_ + 1, d3, d4);
			tessellator.addVertexWithUV(p_147726_2_ + d2, p_147726_3_ + 0, p_147726_4_ + 1, d3, d1);
			tessellator.addVertexWithUV(p_147726_2_ + d2, p_147726_3_ + 0, p_147726_4_ + 0, d0, d1);
			tessellator.addVertexWithUV(p_147726_2_ + d2, p_147726_3_ + 1, p_147726_4_ + 0, d0, d4);
			tessellator.addVertexWithUV(p_147726_2_ + d2, p_147726_3_ + 1, p_147726_4_ + 0, d0, d4);
			tessellator.addVertexWithUV(p_147726_2_ + d2, p_147726_3_ + 0, p_147726_4_ + 0, d0, d1);
			tessellator.addVertexWithUV(p_147726_2_ + d2, p_147726_3_ + 0, p_147726_4_ + 1, d3, d1);
			tessellator.addVertexWithUV(p_147726_2_ + d2, p_147726_3_ + 1, p_147726_4_ + 1, d3, d4);
		}

		if ((i1 & 8) != 0) {
			tessellator.addVertexWithUV(p_147726_2_ + 1 - d2, p_147726_3_ + 0, p_147726_4_ + 1, d0, d1);
			tessellator.addVertexWithUV(p_147726_2_ + 1 - d2, p_147726_3_ + 1, p_147726_4_ + 1, d0, d4);
			tessellator.addVertexWithUV(p_147726_2_ + 1 - d2, p_147726_3_ + 1, p_147726_4_ + 0, d3, d4);
			tessellator.addVertexWithUV(p_147726_2_ + 1 - d2, p_147726_3_ + 0, p_147726_4_ + 0, d3, d1);
			tessellator.addVertexWithUV(p_147726_2_ + 1 - d2, p_147726_3_ + 0, p_147726_4_ + 0, d3, d1);
			tessellator.addVertexWithUV(p_147726_2_ + 1 - d2, p_147726_3_ + 1, p_147726_4_ + 0, d3, d4);
			tessellator.addVertexWithUV(p_147726_2_ + 1 - d2, p_147726_3_ + 1, p_147726_4_ + 1, d0, d4);
			tessellator.addVertexWithUV(p_147726_2_ + 1 - d2, p_147726_3_ + 0, p_147726_4_ + 1, d0, d1);
		}

		if ((i1 & 4) != 0) {
			tessellator.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 0, p_147726_4_ + d2, d0, d1);
			tessellator.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 1, p_147726_4_ + d2, d0, d4);
			tessellator.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 1, p_147726_4_ + d2, d3, d4);
			tessellator.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 0, p_147726_4_ + d2, d3, d1);
			tessellator.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 0, p_147726_4_ + d2, d3, d1);
			tessellator.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 1, p_147726_4_ + d2, d3, d4);
			tessellator.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 1, p_147726_4_ + d2, d0, d4);
			tessellator.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 0, p_147726_4_ + d2, d0, d1);
		}

		if ((i1 & 1) != 0) {
			tessellator.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 1, p_147726_4_ + 1 - d2, d3, d4);
			tessellator.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 0, p_147726_4_ + 1 - d2, d3, d1);
			tessellator.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 0, p_147726_4_ + 1 - d2, d0, d1);
			tessellator.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 1, p_147726_4_ + 1 - d2, d0, d4);
			tessellator.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 1, p_147726_4_ + 1 - d2, d0, d4);
			tessellator.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 0, p_147726_4_ + 1 - d2, d0, d1);
			tessellator.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 0, p_147726_4_ + 1 - d2, d3, d1);
			tessellator.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 1, p_147726_4_ + 1 - d2, d3, d4);
		}

		if (blockAccess.getBlock(p_147726_2_, p_147726_3_ + 1, p_147726_4_).isBlockNormalCube()) {
			tessellator.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 1 - d2, p_147726_4_ + 0, d3, d4);
			tessellator.addVertexWithUV(p_147726_2_ + 1, p_147726_3_ + 1 - d2, p_147726_4_ + 1, d3, d1);
			tessellator.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 1 - d2, p_147726_4_ + 1, d0, d1);
			tessellator.addVertexWithUV(p_147726_2_ + 0, p_147726_3_ + 1 - d2, p_147726_4_ + 0, d0, d4);
		}

		return true;
	}

	public boolean renderBlockStainedGlassPane(Block p_147733_1_, int p_147733_2_, int p_147733_3_, int p_147733_4_) {
		blockAccess.getHeight();
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(
				p_147733_1_.getMixedBrightnessForBlock(blockAccess, p_147733_2_, p_147733_3_, p_147733_4_));
		int i1 = p_147733_1_.colorMultiplier(blockAccess, p_147733_2_, p_147733_3_, p_147733_4_);
		float f = (i1 >> 16 & 255) / 255.0F;
		float f1 = (i1 >> 8 & 255) / 255.0F;
		float f2 = (i1 & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		tessellator.setColorOpaque_F(f, f1, f2);
		boolean flag5 = p_147733_1_ instanceof BlockStainedGlassPane;
		IIcon iicon;
		IIcon iicon1;

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
			iicon1 = overrideBlockTexture;
		} else {
			int j1 = blockAccess.getBlockMetadata(p_147733_2_, p_147733_3_, p_147733_4_);
			iicon = getBlockIconFromSideAndMetadata(p_147733_1_, 0, j1);
			iicon1 = flag5 ? ((BlockStainedGlassPane) p_147733_1_).func_150104_b(j1)
					: ((BlockPane) p_147733_1_).func_150097_e();
		}

		double d22 = iicon.getMinU();
		double d0 = iicon.getInterpolatedU(7.0D);
		double d1 = iicon.getInterpolatedU(9.0D);
		double d2 = iicon.getMaxU();
		double d3 = iicon.getMinV();
		double d4 = iicon.getMaxV();
		double d5 = iicon1.getInterpolatedU(7.0D);
		double d6 = iicon1.getInterpolatedU(9.0D);
		double d7 = iicon1.getMinV();
		double d8 = iicon1.getMaxV();
		double d9 = iicon1.getInterpolatedV(7.0D);
		double d10 = iicon1.getInterpolatedV(9.0D);
		double d11 = p_147733_2_;
		double d12 = p_147733_2_ + 1;
		double d13 = p_147733_4_;
		double d14 = p_147733_4_ + 1;
		double d15 = p_147733_2_ + 0.5D - 0.0625D;
		double d16 = p_147733_2_ + 0.5D + 0.0625D;
		double d17 = p_147733_4_ + 0.5D - 0.0625D;
		double d18 = p_147733_4_ + 0.5D + 0.0625D;
		boolean flag = ((BlockPane) p_147733_1_).canPaneConnectTo(blockAccess, p_147733_2_, p_147733_3_,
				p_147733_4_ - 1, NORTH);
		boolean flag1 = ((BlockPane) p_147733_1_).canPaneConnectTo(blockAccess, p_147733_2_, p_147733_3_,
				p_147733_4_ + 1, SOUTH);
		boolean flag2 = ((BlockPane) p_147733_1_).canPaneConnectTo(blockAccess, p_147733_2_ - 1, p_147733_3_,
				p_147733_4_, WEST);
		boolean flag3 = ((BlockPane) p_147733_1_).canPaneConnectTo(blockAccess, p_147733_2_ + 1, p_147733_3_,
				p_147733_4_, EAST);
		boolean flag4 = !flag && !flag1 && !flag2 && !flag3;

		if (!flag2 && !flag4) {
			if (!flag && !flag1) {
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d17, d0, d3);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d17, d0, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d18, d1, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d18, d1, d3);
			}
		} else if (flag2 && flag3) {
			if (!flag) {
				tessellator.addVertexWithUV(d12, p_147733_3_ + 0.999D, d17, d2, d3);
				tessellator.addVertexWithUV(d12, p_147733_3_ + 0.001D, d17, d2, d4);
				tessellator.addVertexWithUV(d11, p_147733_3_ + 0.001D, d17, d22, d4);
				tessellator.addVertexWithUV(d11, p_147733_3_ + 0.999D, d17, d22, d3);
			} else {
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d17, d0, d3);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d17, d0, d4);
				tessellator.addVertexWithUV(d11, p_147733_3_ + 0.001D, d17, d22, d4);
				tessellator.addVertexWithUV(d11, p_147733_3_ + 0.999D, d17, d22, d3);
				tessellator.addVertexWithUV(d12, p_147733_3_ + 0.999D, d17, d2, d3);
				tessellator.addVertexWithUV(d12, p_147733_3_ + 0.001D, d17, d2, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d17, d1, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d17, d1, d3);
			}

			if (!flag1) {
				tessellator.addVertexWithUV(d11, p_147733_3_ + 0.999D, d18, d22, d3);
				tessellator.addVertexWithUV(d11, p_147733_3_ + 0.001D, d18, d22, d4);
				tessellator.addVertexWithUV(d12, p_147733_3_ + 0.001D, d18, d2, d4);
				tessellator.addVertexWithUV(d12, p_147733_3_ + 0.999D, d18, d2, d3);
			} else {
				tessellator.addVertexWithUV(d11, p_147733_3_ + 0.999D, d18, d22, d3);
				tessellator.addVertexWithUV(d11, p_147733_3_ + 0.001D, d18, d22, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d18, d0, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d18, d0, d3);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d18, d1, d3);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d18, d1, d4);
				tessellator.addVertexWithUV(d12, p_147733_3_ + 0.001D, d18, d2, d4);
				tessellator.addVertexWithUV(d12, p_147733_3_ + 0.999D, d18, d2, d3);
			}

			tessellator.addVertexWithUV(d11, p_147733_3_ + 0.999D, d18, d6, d7);
			tessellator.addVertexWithUV(d12, p_147733_3_ + 0.999D, d18, d6, d8);
			tessellator.addVertexWithUV(d12, p_147733_3_ + 0.999D, d17, d5, d8);
			tessellator.addVertexWithUV(d11, p_147733_3_ + 0.999D, d17, d5, d7);
			tessellator.addVertexWithUV(d12, p_147733_3_ + 0.001D, d18, d5, d8);
			tessellator.addVertexWithUV(d11, p_147733_3_ + 0.001D, d18, d5, d7);
			tessellator.addVertexWithUV(d11, p_147733_3_ + 0.001D, d17, d6, d7);
			tessellator.addVertexWithUV(d12, p_147733_3_ + 0.001D, d17, d6, d8);
		} else {
			if (!flag && !flag4) {
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d17, d1, d3);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d17, d1, d4);
				tessellator.addVertexWithUV(d11, p_147733_3_ + 0.001D, d17, d22, d4);
				tessellator.addVertexWithUV(d11, p_147733_3_ + 0.999D, d17, d22, d3);
			} else {
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d17, d0, d3);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d17, d0, d4);
				tessellator.addVertexWithUV(d11, p_147733_3_ + 0.001D, d17, d22, d4);
				tessellator.addVertexWithUV(d11, p_147733_3_ + 0.999D, d17, d22, d3);
			}

			if (!flag1 && !flag4) {
				tessellator.addVertexWithUV(d11, p_147733_3_ + 0.999D, d18, d22, d3);
				tessellator.addVertexWithUV(d11, p_147733_3_ + 0.001D, d18, d22, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d18, d1, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d18, d1, d3);
			} else {
				tessellator.addVertexWithUV(d11, p_147733_3_ + 0.999D, d18, d22, d3);
				tessellator.addVertexWithUV(d11, p_147733_3_ + 0.001D, d18, d22, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d18, d0, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d18, d0, d3);
			}

			tessellator.addVertexWithUV(d11, p_147733_3_ + 0.999D, d18, d6, d7);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d18, d6, d9);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d17, d5, d9);
			tessellator.addVertexWithUV(d11, p_147733_3_ + 0.999D, d17, d5, d7);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d18, d5, d9);
			tessellator.addVertexWithUV(d11, p_147733_3_ + 0.001D, d18, d5, d7);
			tessellator.addVertexWithUV(d11, p_147733_3_ + 0.001D, d17, d6, d7);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d17, d6, d9);
		}

		if ((flag3 || flag4) && !flag2) {
			if (!flag1 && !flag4) {
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d18, d0, d3);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d18, d0, d4);
				tessellator.addVertexWithUV(d12, p_147733_3_ + 0.001D, d18, d2, d4);
				tessellator.addVertexWithUV(d12, p_147733_3_ + 0.999D, d18, d2, d3);
			} else {
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d18, d1, d3);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d18, d1, d4);
				tessellator.addVertexWithUV(d12, p_147733_3_ + 0.001D, d18, d2, d4);
				tessellator.addVertexWithUV(d12, p_147733_3_ + 0.999D, d18, d2, d3);
			}

			if (!flag && !flag4) {
				tessellator.addVertexWithUV(d12, p_147733_3_ + 0.999D, d17, d2, d3);
				tessellator.addVertexWithUV(d12, p_147733_3_ + 0.001D, d17, d2, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d17, d0, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d17, d0, d3);
			} else {
				tessellator.addVertexWithUV(d12, p_147733_3_ + 0.999D, d17, d2, d3);
				tessellator.addVertexWithUV(d12, p_147733_3_ + 0.001D, d17, d2, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d17, d1, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d17, d1, d3);
			}

			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d18, d6, d10);
			tessellator.addVertexWithUV(d12, p_147733_3_ + 0.999D, d18, d6, d7);
			tessellator.addVertexWithUV(d12, p_147733_3_ + 0.999D, d17, d5, d7);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d17, d5, d10);
			tessellator.addVertexWithUV(d12, p_147733_3_ + 0.001D, d18, d5, d8);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d18, d5, d10);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d17, d6, d10);
			tessellator.addVertexWithUV(d12, p_147733_3_ + 0.001D, d17, d6, d8);
		} else if (!flag3 && !flag && !flag1) {
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d18, d0, d3);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d18, d0, d4);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d17, d1, d4);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d17, d1, d3);
		}

		if (!flag && !flag4) {
			if (!flag3 && !flag2) {
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d17, d1, d3);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d17, d1, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d17, d0, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d17, d0, d3);
			}
		} else if (flag && flag1) {
			if (!flag2) {
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d13, d22, d3);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d13, d22, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d14, d2, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d14, d2, d3);
			} else {
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d13, d22, d3);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d13, d22, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d17, d0, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d17, d0, d3);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d18, d1, d3);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d18, d1, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d14, d2, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d14, d2, d3);
			}

			if (!flag3) {
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d14, d2, d3);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d14, d2, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d13, d22, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d13, d22, d3);
			} else {
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d17, d0, d3);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d17, d0, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d13, d22, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d13, d22, d3);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d14, d2, d3);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d14, d2, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d18, d1, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d18, d1, d3);
			}

			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d13, d6, d7);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d13, d5, d7);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d14, d5, d8);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d14, d6, d8);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d13, d5, d7);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d13, d6, d7);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d14, d6, d8);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d14, d5, d8);
		} else {
			if (!flag2 && !flag4) {
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d13, d22, d3);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d13, d22, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d18, d1, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d18, d1, d3);
			} else {
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d13, d22, d3);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d13, d22, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d17, d0, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d17, d0, d3);
			}

			if (!flag3 && !flag4) {
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d18, d1, d3);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d18, d1, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d13, d22, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d13, d22, d3);
			} else {
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d17, d0, d3);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d17, d0, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d13, d22, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d13, d22, d3);
			}

			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d13, d6, d7);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d13, d5, d7);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d17, d5, d9);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d17, d6, d9);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d13, d5, d7);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d13, d6, d7);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d17, d6, d9);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d17, d5, d9);
		}

		if ((flag1 || flag4) && !flag) {
			if (!flag2 && !flag4) {
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d17, d0, d3);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d17, d0, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d14, d2, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d14, d2, d3);
			} else {
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d18, d1, d3);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d18, d1, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d14, d2, d4);
				tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d14, d2, d3);
			}

			if (!flag3 && !flag4) {
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d14, d2, d3);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d14, d2, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d17, d0, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d17, d0, d3);
			} else {
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d14, d2, d3);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d14, d2, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d18, d1, d4);
				tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d18, d1, d3);
			}

			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d18, d6, d10);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d18, d5, d10);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d14, d5, d8);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d14, d6, d8);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d18, d5, d10);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d18, d6, d10);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d14, d6, d8);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d14, d5, d8);
		} else if (!flag1 && !flag3 && !flag2) {
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d18, d0, d3);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d18, d0, d4);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d18, d1, d4);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d18, d1, d3);
		}

		tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d17, d6, d9);
		tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d17, d5, d9);
		tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d18, d5, d10);
		tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d18, d6, d10);
		tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d17, d5, d9);
		tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d17, d6, d9);
		tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d18, d6, d10);
		tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d18, d5, d10);

		if (flag4) {
			tessellator.addVertexWithUV(d11, p_147733_3_ + 0.999D, d17, d0, d3);
			tessellator.addVertexWithUV(d11, p_147733_3_ + 0.001D, d17, d0, d4);
			tessellator.addVertexWithUV(d11, p_147733_3_ + 0.001D, d18, d1, d4);
			tessellator.addVertexWithUV(d11, p_147733_3_ + 0.999D, d18, d1, d3);
			tessellator.addVertexWithUV(d12, p_147733_3_ + 0.999D, d18, d0, d3);
			tessellator.addVertexWithUV(d12, p_147733_3_ + 0.001D, d18, d0, d4);
			tessellator.addVertexWithUV(d12, p_147733_3_ + 0.001D, d17, d1, d4);
			tessellator.addVertexWithUV(d12, p_147733_3_ + 0.999D, d17, d1, d3);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d13, d1, d3);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d13, d1, d4);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d13, d0, d4);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d13, d0, d3);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.999D, d14, d0, d3);
			tessellator.addVertexWithUV(d15, p_147733_3_ + 0.001D, d14, d0, d4);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.001D, d14, d1, d4);
			tessellator.addVertexWithUV(d16, p_147733_3_ + 0.999D, d14, d1, d3);
		}

		return true;
	}

	public boolean renderBlockPane(BlockPane p_147767_1_, int p_147767_2_, int p_147767_3_, int p_147767_4_) {
		int l = blockAccess.getHeight();
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(
				p_147767_1_.getMixedBrightnessForBlock(blockAccess, p_147767_2_, p_147767_3_, p_147767_4_));
		int i1 = p_147767_1_.colorMultiplier(blockAccess, p_147767_2_, p_147767_3_, p_147767_4_);
		float f = (i1 >> 16 & 255) / 255.0F;
		float f1 = (i1 >> 8 & 255) / 255.0F;
		float f2 = (i1 & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		tessellator.setColorOpaque_F(f, f1, f2);
		IIcon iicon;
		IIcon iicon1;

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
			iicon1 = overrideBlockTexture;
		} else {
			int j1 = blockAccess.getBlockMetadata(p_147767_2_, p_147767_3_, p_147767_4_);
			iicon = getBlockIconFromSideAndMetadata(p_147767_1_, 0, j1);
			iicon1 = p_147767_1_.func_150097_e();
		}

		double d21 = iicon.getMinU();
		double d0 = iicon.getInterpolatedU(8.0D);
		double d1 = iicon.getMaxU();
		double d2 = iicon.getMinV();
		double d3 = iicon.getMaxV();
		double d4 = iicon1.getInterpolatedU(7.0D);
		double d5 = iicon1.getInterpolatedU(9.0D);
		double d6 = iicon1.getMinV();
		double d7 = iicon1.getInterpolatedV(8.0D);
		double d8 = iicon1.getMaxV();
		double d9 = p_147767_2_;
		double d10 = p_147767_2_ + 0.5D;
		double d11 = p_147767_2_ + 1;
		double d12 = p_147767_4_;
		double d13 = p_147767_4_ + 0.5D;
		double d14 = p_147767_4_ + 1;
		double d15 = p_147767_2_ + 0.5D - 0.0625D;
		double d16 = p_147767_2_ + 0.5D + 0.0625D;
		double d17 = p_147767_4_ + 0.5D - 0.0625D;
		double d18 = p_147767_4_ + 0.5D + 0.0625D;
		boolean flag = p_147767_1_.canPaneConnectTo(blockAccess, p_147767_2_, p_147767_3_, p_147767_4_ - 1, NORTH);
		boolean flag1 = p_147767_1_.canPaneConnectTo(blockAccess, p_147767_2_, p_147767_3_, p_147767_4_ + 1, SOUTH);
		boolean flag2 = p_147767_1_.canPaneConnectTo(blockAccess, p_147767_2_ - 1, p_147767_3_, p_147767_4_, WEST);
		boolean flag3 = p_147767_1_.canPaneConnectTo(blockAccess, p_147767_2_ + 1, p_147767_3_, p_147767_4_, EAST);
		boolean flag4 = p_147767_1_.shouldSideBeRendered(blockAccess, p_147767_2_, p_147767_3_ + 1, p_147767_4_, 1);
		boolean flag5 = p_147767_1_.shouldSideBeRendered(blockAccess, p_147767_2_, p_147767_3_ - 1, p_147767_4_, 0);
		if ((!flag2 || !flag3) && (flag2 || flag3 || flag || flag1)) {
			if (flag2 && !flag3) {
				tessellator.addVertexWithUV(d9, p_147767_3_ + 1, d13, d21, d2);
				tessellator.addVertexWithUV(d9, p_147767_3_ + 0, d13, d21, d3);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d13, d0, d3);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d13, d0, d2);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d13, d21, d2);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d13, d21, d3);
				tessellator.addVertexWithUV(d9, p_147767_3_ + 0, d13, d0, d3);
				tessellator.addVertexWithUV(d9, p_147767_3_ + 1, d13, d0, d2);

				if (!flag1 && !flag) {
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d18, d4, d6);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d18, d4, d8);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d17, d5, d8);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d17, d5, d6);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d17, d4, d6);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d17, d4, d8);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d18, d5, d8);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d18, d5, d6);
				}

				if (flag4 || p_147767_3_ < l - 1
						&& blockAccess.isAirBlock(p_147767_2_ - 1, p_147767_3_ + 1, p_147767_4_)) {
					tessellator.addVertexWithUV(d9, p_147767_3_ + 1 + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1 + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1 + 0.01D, d17, d4, d8);
					tessellator.addVertexWithUV(d9, p_147767_3_ + 1 + 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1 + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d9, p_147767_3_ + 1 + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d9, p_147767_3_ + 1 + 0.01D, d17, d4, d8);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1 + 0.01D, d17, d4, d7);
				}

				if (flag5 || p_147767_3_ > 1 && blockAccess.isAirBlock(p_147767_2_ - 1, p_147767_3_ - 1, p_147767_4_)) {
					tessellator.addVertexWithUV(d9, p_147767_3_ - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d10, p_147767_3_ - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d10, p_147767_3_ - 0.01D, d17, d4, d8);
					tessellator.addVertexWithUV(d9, p_147767_3_ - 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d10, p_147767_3_ - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d9, p_147767_3_ - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d9, p_147767_3_ - 0.01D, d17, d4, d8);
					tessellator.addVertexWithUV(d10, p_147767_3_ - 0.01D, d17, d4, d7);
				}
			} else if (!flag2 && flag3) {
				tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d13, d0, d2);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d13, d0, d3);
				tessellator.addVertexWithUV(d11, p_147767_3_ + 0, d13, d1, d3);
				tessellator.addVertexWithUV(d11, p_147767_3_ + 1, d13, d1, d2);
				tessellator.addVertexWithUV(d11, p_147767_3_ + 1, d13, d0, d2);
				tessellator.addVertexWithUV(d11, p_147767_3_ + 0, d13, d0, d3);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d13, d1, d3);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d13, d1, d2);

				if (!flag1 && !flag) {
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d17, d4, d6);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d17, d4, d8);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d18, d5, d8);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d18, d5, d6);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d18, d4, d6);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d18, d4, d8);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d17, d5, d8);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d17, d5, d6);
				}

				if (flag4 || p_147767_3_ < l - 1
						&& blockAccess.isAirBlock(p_147767_2_ + 1, p_147767_3_ + 1, p_147767_4_)) {
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1 + 0.01D, d18, d5, d6);
					tessellator.addVertexWithUV(d11, p_147767_3_ + 1 + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d11, p_147767_3_ + 1 + 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1 + 0.01D, d17, d4, d6);
					tessellator.addVertexWithUV(d11, p_147767_3_ + 1 + 0.01D, d18, d5, d6);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1 + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1 + 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d11, p_147767_3_ + 1 + 0.01D, d17, d4, d6);
				}

				if (flag5 || p_147767_3_ > 1 && blockAccess.isAirBlock(p_147767_2_ + 1, p_147767_3_ - 1, p_147767_4_)) {
					tessellator.addVertexWithUV(d10, p_147767_3_ - 0.01D, d18, d5, d6);
					tessellator.addVertexWithUV(d11, p_147767_3_ - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d11, p_147767_3_ - 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d10, p_147767_3_ - 0.01D, d17, d4, d6);
					tessellator.addVertexWithUV(d11, p_147767_3_ - 0.01D, d18, d5, d6);
					tessellator.addVertexWithUV(d10, p_147767_3_ - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d10, p_147767_3_ - 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d11, p_147767_3_ - 0.01D, d17, d4, d6);
				}
			}
		} else {
			tessellator.addVertexWithUV(d9, p_147767_3_ + 1, d13, d21, d2);
			tessellator.addVertexWithUV(d9, p_147767_3_ + 0, d13, d21, d3);
			tessellator.addVertexWithUV(d11, p_147767_3_ + 0, d13, d1, d3);
			tessellator.addVertexWithUV(d11, p_147767_3_ + 1, d13, d1, d2);
			tessellator.addVertexWithUV(d11, p_147767_3_ + 1, d13, d21, d2);
			tessellator.addVertexWithUV(d11, p_147767_3_ + 0, d13, d21, d3);
			tessellator.addVertexWithUV(d9, p_147767_3_ + 0, d13, d1, d3);
			tessellator.addVertexWithUV(d9, p_147767_3_ + 1, d13, d1, d2);

			if (flag4) {
				tessellator.addVertexWithUV(d9, p_147767_3_ + 1 + 0.01D, d18, d5, d8);
				tessellator.addVertexWithUV(d11, p_147767_3_ + 1 + 0.01D, d18, d5, d6);
				tessellator.addVertexWithUV(d11, p_147767_3_ + 1 + 0.01D, d17, d4, d6);
				tessellator.addVertexWithUV(d9, p_147767_3_ + 1 + 0.01D, d17, d4, d8);
				tessellator.addVertexWithUV(d11, p_147767_3_ + 1 + 0.01D, d18, d5, d8);
				tessellator.addVertexWithUV(d9, p_147767_3_ + 1 + 0.01D, d18, d5, d6);
				tessellator.addVertexWithUV(d9, p_147767_3_ + 1 + 0.01D, d17, d4, d6);
				tessellator.addVertexWithUV(d11, p_147767_3_ + 1 + 0.01D, d17, d4, d8);
			} else {
				if (p_147767_3_ < l - 1 && blockAccess.isAirBlock(p_147767_2_ - 1, p_147767_3_ + 1, p_147767_4_)) {
					tessellator.addVertexWithUV(d9, p_147767_3_ + 1 + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1 + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1 + 0.01D, d17, d4, d8);
					tessellator.addVertexWithUV(d9, p_147767_3_ + 1 + 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1 + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d9, p_147767_3_ + 1 + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d9, p_147767_3_ + 1 + 0.01D, d17, d4, d8);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1 + 0.01D, d17, d4, d7);
				}

				if (p_147767_3_ < l - 1 && blockAccess.isAirBlock(p_147767_2_ + 1, p_147767_3_ + 1, p_147767_4_)) {
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1 + 0.01D, d18, d5, d6);
					tessellator.addVertexWithUV(d11, p_147767_3_ + 1 + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d11, p_147767_3_ + 1 + 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1 + 0.01D, d17, d4, d6);
					tessellator.addVertexWithUV(d11, p_147767_3_ + 1 + 0.01D, d18, d5, d6);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1 + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d10, p_147767_3_ + 1 + 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d11, p_147767_3_ + 1 + 0.01D, d17, d4, d6);
				}
			}

			if (flag5) {
				tessellator.addVertexWithUV(d9, p_147767_3_ - 0.01D, d18, d5, d8);
				tessellator.addVertexWithUV(d11, p_147767_3_ - 0.01D, d18, d5, d6);
				tessellator.addVertexWithUV(d11, p_147767_3_ - 0.01D, d17, d4, d6);
				tessellator.addVertexWithUV(d9, p_147767_3_ - 0.01D, d17, d4, d8);
				tessellator.addVertexWithUV(d11, p_147767_3_ - 0.01D, d18, d5, d8);
				tessellator.addVertexWithUV(d9, p_147767_3_ - 0.01D, d18, d5, d6);
				tessellator.addVertexWithUV(d9, p_147767_3_ - 0.01D, d17, d4, d6);
				tessellator.addVertexWithUV(d11, p_147767_3_ - 0.01D, d17, d4, d8);
			} else {
				if (p_147767_3_ > 1 && blockAccess.isAirBlock(p_147767_2_ - 1, p_147767_3_ - 1, p_147767_4_)) {
					tessellator.addVertexWithUV(d9, p_147767_3_ - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d10, p_147767_3_ - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d10, p_147767_3_ - 0.01D, d17, d4, d8);
					tessellator.addVertexWithUV(d9, p_147767_3_ - 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d10, p_147767_3_ - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d9, p_147767_3_ - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d9, p_147767_3_ - 0.01D, d17, d4, d8);
					tessellator.addVertexWithUV(d10, p_147767_3_ - 0.01D, d17, d4, d7);
				}

				if (p_147767_3_ > 1 && blockAccess.isAirBlock(p_147767_2_ + 1, p_147767_3_ - 1, p_147767_4_)) {
					tessellator.addVertexWithUV(d10, p_147767_3_ - 0.01D, d18, d5, d6);
					tessellator.addVertexWithUV(d11, p_147767_3_ - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d11, p_147767_3_ - 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d10, p_147767_3_ - 0.01D, d17, d4, d6);
					tessellator.addVertexWithUV(d11, p_147767_3_ - 0.01D, d18, d5, d6);
					tessellator.addVertexWithUV(d10, p_147767_3_ - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d10, p_147767_3_ - 0.01D, d17, d4, d7);
					tessellator.addVertexWithUV(d11, p_147767_3_ - 0.01D, d17, d4, d6);
				}
			}
		}

		if ((!flag || !flag1) && (flag2 || flag3 || flag || flag1)) {
			if (flag && !flag1) {
				tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d12, d21, d2);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d12, d21, d3);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d13, d0, d3);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d13, d0, d2);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d13, d21, d2);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d13, d21, d3);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d12, d0, d3);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d12, d0, d2);

				if (!flag3 && !flag2) {
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1, d13, d4, d6);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 0, d13, d4, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 0, d13, d5, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1, d13, d5, d6);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1, d13, d4, d6);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 0, d13, d4, d8);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 0, d13, d5, d8);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1, d13, d5, d6);
				}

				if (flag4 || p_147767_3_ < l - 1
						&& blockAccess.isAirBlock(p_147767_2_, p_147767_3_ + 1, p_147767_4_ - 1)) {
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d12, d5, d6);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d13, d5, d7);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d13, d4, d7);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d12, d4, d6);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d13, d5, d6);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d12, d5, d7);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d12, d4, d7);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d13, d4, d6);
				}

				if (flag5 || p_147767_3_ > 1 && blockAccess.isAirBlock(p_147767_2_, p_147767_3_ - 1, p_147767_4_ - 1)) {
					tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d12, d5, d6);
					tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d13, d5, d7);
					tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d13, d4, d7);
					tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d12, d4, d6);
					tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d13, d5, d6);
					tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d12, d5, d7);
					tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d12, d4, d7);
					tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d13, d4, d6);
				}
			} else if (!flag && flag1) {
				tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d13, d0, d2);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d13, d0, d3);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d14, d1, d3);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d14, d1, d2);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d14, d0, d2);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d14, d0, d3);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d13, d1, d3);
				tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d13, d1, d2);

				if (!flag3 && !flag2) {
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1, d13, d4, d6);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 0, d13, d4, d8);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 0, d13, d5, d8);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1, d13, d5, d6);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1, d13, d4, d6);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 0, d13, d4, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 0, d13, d5, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1, d13, d5, d6);
				}

				if (flag4 || p_147767_3_ < l - 1
						&& blockAccess.isAirBlock(p_147767_2_, p_147767_3_ + 1, p_147767_4_ + 1)) {
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d13, d4, d7);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d14, d4, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d14, d5, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d13, d5, d7);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d14, d4, d7);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d13, d4, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d13, d5, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d14, d5, d7);
				}

				if (flag5 || p_147767_3_ > 1 && blockAccess.isAirBlock(p_147767_2_, p_147767_3_ - 1, p_147767_4_ + 1)) {
					tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d13, d4, d7);
					tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d14, d4, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d14, d5, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d13, d5, d7);
					tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d14, d4, d7);
					tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d13, d4, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d13, d5, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d14, d5, d7);
				}
			}
		} else {
			tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d14, d21, d2);
			tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d14, d21, d3);
			tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d12, d1, d3);
			tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d12, d1, d2);
			tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d12, d21, d2);
			tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d12, d21, d3);
			tessellator.addVertexWithUV(d10, p_147767_3_ + 0, d14, d1, d3);
			tessellator.addVertexWithUV(d10, p_147767_3_ + 1, d14, d1, d2);

			if (flag4) {
				tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d14, d5, d8);
				tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d12, d5, d6);
				tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d12, d4, d6);
				tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d14, d4, d8);
				tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d12, d5, d8);
				tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d14, d5, d6);
				tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d14, d4, d6);
				tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d12, d4, d8);
			} else {
				if (p_147767_3_ < l - 1 && blockAccess.isAirBlock(p_147767_2_, p_147767_3_ + 1, p_147767_4_ - 1)) {
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d12, d5, d6);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d13, d5, d7);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d13, d4, d7);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d12, d4, d6);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d13, d5, d6);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d12, d5, d7);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d12, d4, d7);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d13, d4, d6);
				}

				if (p_147767_3_ < l - 1 && blockAccess.isAirBlock(p_147767_2_, p_147767_3_ + 1, p_147767_4_ + 1)) {
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d13, d4, d7);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d14, d4, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d14, d5, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d13, d5, d7);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d14, d4, d7);
					tessellator.addVertexWithUV(d15, p_147767_3_ + 1 + 0.005D, d13, d4, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d13, d5, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ + 1 + 0.005D, d14, d5, d7);
				}
			}

			if (flag5) {
				tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d14, d5, d8);
				tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d12, d5, d6);
				tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d12, d4, d6);
				tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d14, d4, d8);
				tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d12, d5, d8);
				tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d14, d5, d6);
				tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d14, d4, d6);
				tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d12, d4, d8);
			} else {
				if (p_147767_3_ > 1 && blockAccess.isAirBlock(p_147767_2_, p_147767_3_ - 1, p_147767_4_ - 1)) {
					tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d12, d5, d6);
					tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d13, d5, d7);
					tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d13, d4, d7);
					tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d12, d4, d6);
					tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d13, d5, d6);
					tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d12, d5, d7);
					tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d12, d4, d7);
					tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d13, d4, d6);
				}

				if (p_147767_3_ > 1 && blockAccess.isAirBlock(p_147767_2_, p_147767_3_ - 1, p_147767_4_ + 1)) {
					tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d13, d4, d7);
					tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d14, d4, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d14, d5, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d13, d5, d7);
					tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d14, d4, d7);
					tessellator.addVertexWithUV(d15, p_147767_3_ - 0.005D, d13, d4, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d13, d5, d8);
					tessellator.addVertexWithUV(d16, p_147767_3_ - 0.005D, d14, d5, d7);
				}
			}
		}

		return true;
	}

	public boolean renderCrossedSquares(Block p_147746_1_, int p_147746_2_, int p_147746_3_, int p_147746_4_) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(
				p_147746_1_.getMixedBrightnessForBlock(blockAccess, p_147746_2_, p_147746_3_, p_147746_4_));
		int l = p_147746_1_.colorMultiplier(blockAccess, p_147746_2_, p_147746_3_, p_147746_4_);
		float f = (l >> 16 & 255) / 255.0F;
		float f1 = (l >> 8 & 255) / 255.0F;
		float f2 = (l & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		tessellator.setColorOpaque_F(f, f1, f2);
		double d1 = p_147746_2_;
		double d2 = p_147746_3_;
		double d0 = p_147746_4_;
		long i1;

		if (p_147746_1_ == Blocks.tallgrass) {
			i1 = p_147746_2_ * 3129871 ^ p_147746_4_ * 116129781L ^ p_147746_3_;
			i1 = i1 * i1 * 42317861L + i1 * 11L;
			d1 += ((i1 >> 16 & 15L) / 15.0F - 0.5D) * 0.5D;
			d2 += ((i1 >> 20 & 15L) / 15.0F - 1.0D) * 0.2D;
			d0 += ((i1 >> 24 & 15L) / 15.0F - 0.5D) * 0.5D;
		} else if (p_147746_1_ == Blocks.red_flower || p_147746_1_ == Blocks.yellow_flower) {
			i1 = p_147746_2_ * 3129871 ^ p_147746_4_ * 116129781L ^ p_147746_3_;
			i1 = i1 * i1 * 42317861L + i1 * 11L;
			d1 += ((i1 >> 16 & 15L) / 15.0F - 0.5D) * 0.3D;
			d0 += ((i1 >> 24 & 15L) / 15.0F - 0.5D) * 0.3D;
		}

		IIcon iicon = getBlockIconFromSideAndMetadata(p_147746_1_, 0,
				blockAccess.getBlockMetadata(p_147746_2_, p_147746_3_, p_147746_4_));
		drawCrossedSquares(iicon, d1, d2, d0, 1.0F);
		return true;
	}

	public boolean renderBlockDoublePlant(BlockDoublePlant p_147774_1_, int p_147774_2_, int p_147774_3_,
			int p_147774_4_) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(
				p_147774_1_.getMixedBrightnessForBlock(blockAccess, p_147774_2_, p_147774_3_, p_147774_4_));
		int l = p_147774_1_.colorMultiplier(blockAccess, p_147774_2_, p_147774_3_, p_147774_4_);
		float f = (l >> 16 & 255) / 255.0F;
		float f1 = (l >> 8 & 255) / 255.0F;
		float f2 = (l & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		tessellator.setColorOpaque_F(f, f1, f2);
		long j1 = p_147774_2_ * 3129871 ^ p_147774_4_ * 116129781L;
		j1 = j1 * j1 * 42317861L + j1 * 11L;
		double d19 = p_147774_2_;
		double d0 = p_147774_3_;
		double d1 = p_147774_4_;
		d19 += ((j1 >> 16 & 15L) / 15.0F - 0.5D) * 0.3D;
		d1 += ((j1 >> 24 & 15L) / 15.0F - 0.5D) * 0.3D;
		int i1 = blockAccess.getBlockMetadata(p_147774_2_, p_147774_3_, p_147774_4_);
		boolean flag1 = BlockDoublePlant.func_149887_c(i1);
		int k1;

		if (flag1) {
			if (blockAccess.getBlock(p_147774_2_, p_147774_3_ - 1, p_147774_4_) != p_147774_1_)
				return false;

			k1 = BlockDoublePlant
					.func_149890_d(blockAccess.getBlockMetadata(p_147774_2_, p_147774_3_ - 1, p_147774_4_));
		} else {
			k1 = BlockDoublePlant.func_149890_d(i1);
		}

		IIcon iicon = p_147774_1_.func_149888_a(flag1, k1);
		drawCrossedSquares(iicon, d19, d0, d1, 1.0F);

		if (flag1 && k1 == 0) {
			IIcon iicon1 = p_147774_1_.sunflowerIcons[0];
			double d2 = Math.cos(j1 * 0.8D) * Math.PI * 0.1D;
			double d3 = Math.cos(d2);
			double d4 = Math.sin(d2);
			double d5 = iicon1.getMinU();
			double d6 = iicon1.getMinV();
			double d7 = iicon1.getMaxU();
			double d8 = iicon1.getMaxV();
			double d11 = 0.5D + 0.3D * d3 - 0.5D * d4;
			double d12 = 0.5D + 0.5D * d3 + 0.3D * d4;
			double d13 = 0.5D + 0.3D * d3 + 0.5D * d4;
			double d14 = 0.5D + -0.5D * d3 + 0.3D * d4;
			double d15 = 0.5D + -0.05D * d3 + 0.5D * d4;
			double d16 = 0.5D + -0.5D * d3 + -0.05D * d4;
			double d17 = 0.5D + -0.05D * d3 - 0.5D * d4;
			double d18 = 0.5D + 0.5D * d3 + -0.05D * d4;
			tessellator.addVertexWithUV(d19 + d15, d0 + 1.0D, d1 + d16, d5, d8);
			tessellator.addVertexWithUV(d19 + d17, d0 + 1.0D, d1 + d18, d7, d8);
			tessellator.addVertexWithUV(d19 + d11, d0 + 0.0D, d1 + d12, d7, d6);
			tessellator.addVertexWithUV(d19 + d13, d0 + 0.0D, d1 + d14, d5, d6);
			IIcon iicon2 = p_147774_1_.sunflowerIcons[1];
			d5 = iicon2.getMinU();
			d6 = iicon2.getMinV();
			d7 = iicon2.getMaxU();
			d8 = iicon2.getMaxV();
			tessellator.addVertexWithUV(d19 + d17, d0 + 1.0D, d1 + d18, d5, d8);
			tessellator.addVertexWithUV(d19 + d15, d0 + 1.0D, d1 + d16, d7, d8);
			tessellator.addVertexWithUV(d19 + d13, d0 + 0.0D, d1 + d14, d7, d6);
			tessellator.addVertexWithUV(d19 + d11, d0 + 0.0D, d1 + d12, d5, d6);
		}

		return true;
	}

	public boolean renderBlockStem(Block p_147724_1_, int p_147724_2_, int p_147724_3_, int p_147724_4_) {
		BlockStem blockstem = (BlockStem) p_147724_1_;
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(
				blockstem.getMixedBrightnessForBlock(blockAccess, p_147724_2_, p_147724_3_, p_147724_4_));
		int l = blockstem.colorMultiplier(blockAccess, p_147724_2_, p_147724_3_, p_147724_4_);
		float f = (l >> 16 & 255) / 255.0F;
		float f1 = (l >> 8 & 255) / 255.0F;
		float f2 = (l & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		tessellator.setColorOpaque_F(f, f1, f2);
		blockstem.setBlockBoundsBasedOnState(blockAccess, p_147724_2_, p_147724_3_, p_147724_4_);
		int i1 = blockstem.getState(blockAccess, p_147724_2_, p_147724_3_, p_147724_4_);

		if (i1 < 0) {
			renderBlockStemSmall(blockstem, blockAccess.getBlockMetadata(p_147724_2_, p_147724_3_, p_147724_4_),
					renderMaxY, p_147724_2_, p_147724_3_ - 0.0625F, p_147724_4_);
		} else {
			renderBlockStemSmall(blockstem, blockAccess.getBlockMetadata(p_147724_2_, p_147724_3_, p_147724_4_), 0.5D,
					p_147724_2_, p_147724_3_ - 0.0625F, p_147724_4_);
			renderBlockStemBig(blockstem, blockAccess.getBlockMetadata(p_147724_2_, p_147724_3_, p_147724_4_), i1,
					renderMaxY, p_147724_2_, p_147724_3_ - 0.0625F, p_147724_4_);
		}

		return true;
	}

	public boolean renderBlockCrops(Block p_147796_1_, int p_147796_2_, int p_147796_3_, int p_147796_4_) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(
				p_147796_1_.getMixedBrightnessForBlock(blockAccess, p_147796_2_, p_147796_3_, p_147796_4_));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		renderBlockCropsImpl(p_147796_1_, blockAccess.getBlockMetadata(p_147796_2_, p_147796_3_, p_147796_4_),
				p_147796_2_, p_147796_3_ - 0.0625F, p_147796_4_);
		return true;
	}

	public void renderTorchAtAngle(Block p_147747_1_, double p_147747_2_, double p_147747_4_, double p_147747_6_,
			double p_147747_8_, double p_147747_10_, int p_147747_12_) {
		Tessellator tessellator = Tessellator.instance;
		IIcon iicon = getBlockIconFromSideAndMetadata(p_147747_1_, 0, p_147747_12_);

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
		}

		double d5 = iicon.getMinU();
		double d6 = iicon.getMinV();
		double d7 = iicon.getMaxU();
		double d8 = iicon.getMaxV();
		double d9 = iicon.getInterpolatedU(7.0D);
		double d10 = iicon.getInterpolatedV(6.0D);
		double d11 = iicon.getInterpolatedU(9.0D);
		double d12 = iicon.getInterpolatedV(8.0D);
		double d13 = iicon.getInterpolatedU(7.0D);
		double d14 = iicon.getInterpolatedV(13.0D);
		double d15 = iicon.getInterpolatedU(9.0D);
		double d16 = iicon.getInterpolatedV(15.0D);
		p_147747_2_ += 0.5D;
		p_147747_6_ += 0.5D;
		double d17 = p_147747_2_ - 0.5D;
		double d18 = p_147747_2_ + 0.5D;
		double d19 = p_147747_6_ - 0.5D;
		double d20 = p_147747_6_ + 0.5D;
		double d21 = 0.0625D;
		double d22 = 0.625D;
		tessellator.addVertexWithUV(p_147747_2_ + p_147747_8_ * (1.0D - d22) - d21, p_147747_4_ + d22,
				p_147747_6_ + p_147747_10_ * (1.0D - d22) - d21, d9, d10);
		tessellator.addVertexWithUV(p_147747_2_ + p_147747_8_ * (1.0D - d22) - d21, p_147747_4_ + d22,
				p_147747_6_ + p_147747_10_ * (1.0D - d22) + d21, d9, d12);
		tessellator.addVertexWithUV(p_147747_2_ + p_147747_8_ * (1.0D - d22) + d21, p_147747_4_ + d22,
				p_147747_6_ + p_147747_10_ * (1.0D - d22) + d21, d11, d12);
		tessellator.addVertexWithUV(p_147747_2_ + p_147747_8_ * (1.0D - d22) + d21, p_147747_4_ + d22,
				p_147747_6_ + p_147747_10_ * (1.0D - d22) - d21, d11, d10);
		tessellator.addVertexWithUV(p_147747_2_ + d21 + p_147747_8_, p_147747_4_, p_147747_6_ - d21 + p_147747_10_, d15,
				d14);
		tessellator.addVertexWithUV(p_147747_2_ + d21 + p_147747_8_, p_147747_4_, p_147747_6_ + d21 + p_147747_10_, d15,
				d16);
		tessellator.addVertexWithUV(p_147747_2_ - d21 + p_147747_8_, p_147747_4_, p_147747_6_ + d21 + p_147747_10_, d13,
				d16);
		tessellator.addVertexWithUV(p_147747_2_ - d21 + p_147747_8_, p_147747_4_, p_147747_6_ - d21 + p_147747_10_, d13,
				d14);
		tessellator.addVertexWithUV(p_147747_2_ - d21, p_147747_4_ + 1.0D, d19, d5, d6);
		tessellator.addVertexWithUV(p_147747_2_ - d21 + p_147747_8_, p_147747_4_ + 0.0D, d19 + p_147747_10_, d5, d8);
		tessellator.addVertexWithUV(p_147747_2_ - d21 + p_147747_8_, p_147747_4_ + 0.0D, d20 + p_147747_10_, d7, d8);
		tessellator.addVertexWithUV(p_147747_2_ - d21, p_147747_4_ + 1.0D, d20, d7, d6);
		tessellator.addVertexWithUV(p_147747_2_ + d21, p_147747_4_ + 1.0D, d20, d5, d6);
		tessellator.addVertexWithUV(p_147747_2_ + p_147747_8_ + d21, p_147747_4_ + 0.0D, d20 + p_147747_10_, d5, d8);
		tessellator.addVertexWithUV(p_147747_2_ + p_147747_8_ + d21, p_147747_4_ + 0.0D, d19 + p_147747_10_, d7, d8);
		tessellator.addVertexWithUV(p_147747_2_ + d21, p_147747_4_ + 1.0D, d19, d7, d6);
		tessellator.addVertexWithUV(d17, p_147747_4_ + 1.0D, p_147747_6_ + d21, d5, d6);
		tessellator.addVertexWithUV(d17 + p_147747_8_, p_147747_4_ + 0.0D, p_147747_6_ + d21 + p_147747_10_, d5, d8);
		tessellator.addVertexWithUV(d18 + p_147747_8_, p_147747_4_ + 0.0D, p_147747_6_ + d21 + p_147747_10_, d7, d8);
		tessellator.addVertexWithUV(d18, p_147747_4_ + 1.0D, p_147747_6_ + d21, d7, d6);
		tessellator.addVertexWithUV(d18, p_147747_4_ + 1.0D, p_147747_6_ - d21, d5, d6);
		tessellator.addVertexWithUV(d18 + p_147747_8_, p_147747_4_ + 0.0D, p_147747_6_ - d21 + p_147747_10_, d5, d8);
		tessellator.addVertexWithUV(d17 + p_147747_8_, p_147747_4_ + 0.0D, p_147747_6_ - d21 + p_147747_10_, d7, d8);
		tessellator.addVertexWithUV(d17, p_147747_4_ + 1.0D, p_147747_6_ - d21, d7, d6);
	}

	public void drawCrossedSquares(IIcon p_147765_1_, double p_147765_2_, double p_147765_4_, double p_147765_6_,
			float p_147765_8_) {
		Tessellator tessellator = Tessellator.instance;

		if (hasOverrideBlockTexture()) {
			p_147765_1_ = overrideBlockTexture;
		}

		double d3 = p_147765_1_.getMinU();
		double d4 = p_147765_1_.getMinV();
		double d5 = p_147765_1_.getMaxU();
		double d6 = p_147765_1_.getMaxV();
		double d7 = 0.45D * p_147765_8_;
		double d8 = p_147765_2_ + 0.5D - d7;
		double d9 = p_147765_2_ + 0.5D + d7;
		double d10 = p_147765_6_ + 0.5D - d7;
		double d11 = p_147765_6_ + 0.5D + d7;
		tessellator.addVertexWithUV(d8, p_147765_4_ + p_147765_8_, d10, d3, d4);
		tessellator.addVertexWithUV(d8, p_147765_4_ + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d9, p_147765_4_ + 0.0D, d11, d5, d6);
		tessellator.addVertexWithUV(d9, p_147765_4_ + p_147765_8_, d11, d5, d4);
		tessellator.addVertexWithUV(d9, p_147765_4_ + p_147765_8_, d11, d3, d4);
		tessellator.addVertexWithUV(d9, p_147765_4_ + 0.0D, d11, d3, d6);
		tessellator.addVertexWithUV(d8, p_147765_4_ + 0.0D, d10, d5, d6);
		tessellator.addVertexWithUV(d8, p_147765_4_ + p_147765_8_, d10, d5, d4);
		tessellator.addVertexWithUV(d8, p_147765_4_ + p_147765_8_, d11, d3, d4);
		tessellator.addVertexWithUV(d8, p_147765_4_ + 0.0D, d11, d3, d6);
		tessellator.addVertexWithUV(d9, p_147765_4_ + 0.0D, d10, d5, d6);
		tessellator.addVertexWithUV(d9, p_147765_4_ + p_147765_8_, d10, d5, d4);
		tessellator.addVertexWithUV(d9, p_147765_4_ + p_147765_8_, d10, d3, d4);
		tessellator.addVertexWithUV(d9, p_147765_4_ + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d8, p_147765_4_ + 0.0D, d11, d5, d6);
		tessellator.addVertexWithUV(d8, p_147765_4_ + p_147765_8_, d11, d5, d4);
	}

	public void renderBlockStemSmall(Block p_147730_1_, int p_147730_2_, double p_147730_3_, double p_147730_5_,
			double p_147730_7_, double p_147730_9_) {
		Tessellator tessellator = Tessellator.instance;
		IIcon iicon = getBlockIconFromSideAndMetadata(p_147730_1_, 0, p_147730_2_);

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
		}

		double d4 = iicon.getMinU();
		double d5 = iicon.getMinV();
		double d6 = iicon.getMaxU();
		double d7 = iicon.getInterpolatedV(p_147730_3_ * 16.0D);
		double d8 = p_147730_5_ + 0.5D - 0.44999998807907104D;
		double d9 = p_147730_5_ + 0.5D + 0.44999998807907104D;
		double d10 = p_147730_9_ + 0.5D - 0.44999998807907104D;
		double d11 = p_147730_9_ + 0.5D + 0.44999998807907104D;
		tessellator.addVertexWithUV(d8, p_147730_7_ + p_147730_3_, d10, d4, d5);
		tessellator.addVertexWithUV(d8, p_147730_7_ + 0.0D, d10, d4, d7);
		tessellator.addVertexWithUV(d9, p_147730_7_ + 0.0D, d11, d6, d7);
		tessellator.addVertexWithUV(d9, p_147730_7_ + p_147730_3_, d11, d6, d5);
		tessellator.addVertexWithUV(d9, p_147730_7_ + p_147730_3_, d11, d6, d5);
		tessellator.addVertexWithUV(d9, p_147730_7_ + 0.0D, d11, d6, d7);
		tessellator.addVertexWithUV(d8, p_147730_7_ + 0.0D, d10, d4, d7);
		tessellator.addVertexWithUV(d8, p_147730_7_ + p_147730_3_, d10, d4, d5);
		tessellator.addVertexWithUV(d8, p_147730_7_ + p_147730_3_, d11, d4, d5);
		tessellator.addVertexWithUV(d8, p_147730_7_ + 0.0D, d11, d4, d7);
		tessellator.addVertexWithUV(d9, p_147730_7_ + 0.0D, d10, d6, d7);
		tessellator.addVertexWithUV(d9, p_147730_7_ + p_147730_3_, d10, d6, d5);
		tessellator.addVertexWithUV(d9, p_147730_7_ + p_147730_3_, d10, d6, d5);
		tessellator.addVertexWithUV(d9, p_147730_7_ + 0.0D, d10, d6, d7);
		tessellator.addVertexWithUV(d8, p_147730_7_ + 0.0D, d11, d4, d7);
		tessellator.addVertexWithUV(d8, p_147730_7_ + p_147730_3_, d11, d4, d5);
	}

	public boolean renderBlockLilyPad(Block p_147783_1_, int p_147783_2_, int p_147783_3_, int p_147783_4_) {
		Tessellator tessellator = Tessellator.instance;
		IIcon iicon = getBlockIconFromSide(p_147783_1_, 1);

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
		}

		float f = 0.015625F;
		double d0 = iicon.getMinU();
		double d1 = iicon.getMinV();
		double d2 = iicon.getMaxU();
		double d3 = iicon.getMaxV();
		long l = p_147783_2_ * 3129871 ^ p_147783_4_ * 116129781L ^ p_147783_3_;
		l = l * l * 42317861L + l * 11L;
		int i1 = (int) (l >> 16 & 3L);
		tessellator.setBrightness(
				p_147783_1_.getMixedBrightnessForBlock(blockAccess, p_147783_2_, p_147783_3_, p_147783_4_));
		float f1 = p_147783_2_ + 0.5F;
		float f2 = p_147783_4_ + 0.5F;
		float f3 = (i1 & 1) * 0.5F * (1 - i1 / 2 % 2 * 2);
		float f4 = (i1 + 1 & 1) * 0.5F * (1 - (i1 + 1) / 2 % 2 * 2);
		tessellator.setColorOpaque_I(p_147783_1_.getBlockColor());
		tessellator.addVertexWithUV(f1 + f3 - f4, p_147783_3_ + f, f2 + f3 + f4, d0, d1);
		tessellator.addVertexWithUV(f1 + f3 + f4, p_147783_3_ + f, f2 - f3 + f4, d2, d1);
		tessellator.addVertexWithUV(f1 - f3 + f4, p_147783_3_ + f, f2 - f3 - f4, d2, d3);
		tessellator.addVertexWithUV(f1 - f3 - f4, p_147783_3_ + f, f2 + f3 - f4, d0, d3);
		tessellator.setColorOpaque_I((p_147783_1_.getBlockColor() & 16711422) >> 1);
		tessellator.addVertexWithUV(f1 - f3 - f4, p_147783_3_ + f, f2 + f3 - f4, d0, d3);
		tessellator.addVertexWithUV(f1 - f3 + f4, p_147783_3_ + f, f2 - f3 - f4, d2, d3);
		tessellator.addVertexWithUV(f1 + f3 + f4, p_147783_3_ + f, f2 - f3 + f4, d2, d1);
		tessellator.addVertexWithUV(f1 + f3 - f4, p_147783_3_ + f, f2 + f3 + f4, d0, d1);
		return true;
	}

	public void renderBlockStemBig(BlockStem p_147740_1_, int p_147740_2_, int p_147740_3_, double p_147740_4_,
			double p_147740_6_, double p_147740_8_, double p_147740_10_) {
		Tessellator tessellator = Tessellator.instance;
		IIcon iicon = p_147740_1_.getStemIcon();

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
		}

		double d4 = iicon.getMinU();
		double d5 = iicon.getMinV();
		double d6 = iicon.getMaxU();
		double d7 = iicon.getMaxV();
		double d8 = p_147740_6_ + 0.5D - 0.5D;
		double d9 = p_147740_6_ + 0.5D + 0.5D;
		double d10 = p_147740_10_ + 0.5D - 0.5D;
		double d11 = p_147740_10_ + 0.5D + 0.5D;
		double d12 = p_147740_6_ + 0.5D;
		double d13 = p_147740_10_ + 0.5D;

		if ((p_147740_3_ + 1) / 2 % 2 == 1) {
			double d14 = d6;
			d6 = d4;
			d4 = d14;
		}

		if (p_147740_3_ < 2) {
			tessellator.addVertexWithUV(d8, p_147740_8_ + p_147740_4_, d13, d4, d5);
			tessellator.addVertexWithUV(d8, p_147740_8_ + 0.0D, d13, d4, d7);
			tessellator.addVertexWithUV(d9, p_147740_8_ + 0.0D, d13, d6, d7);
			tessellator.addVertexWithUV(d9, p_147740_8_ + p_147740_4_, d13, d6, d5);
			tessellator.addVertexWithUV(d9, p_147740_8_ + p_147740_4_, d13, d6, d5);
			tessellator.addVertexWithUV(d9, p_147740_8_ + 0.0D, d13, d6, d7);
			tessellator.addVertexWithUV(d8, p_147740_8_ + 0.0D, d13, d4, d7);
			tessellator.addVertexWithUV(d8, p_147740_8_ + p_147740_4_, d13, d4, d5);
		} else {
			tessellator.addVertexWithUV(d12, p_147740_8_ + p_147740_4_, d11, d4, d5);
			tessellator.addVertexWithUV(d12, p_147740_8_ + 0.0D, d11, d4, d7);
			tessellator.addVertexWithUV(d12, p_147740_8_ + 0.0D, d10, d6, d7);
			tessellator.addVertexWithUV(d12, p_147740_8_ + p_147740_4_, d10, d6, d5);
			tessellator.addVertexWithUV(d12, p_147740_8_ + p_147740_4_, d10, d6, d5);
			tessellator.addVertexWithUV(d12, p_147740_8_ + 0.0D, d10, d6, d7);
			tessellator.addVertexWithUV(d12, p_147740_8_ + 0.0D, d11, d4, d7);
			tessellator.addVertexWithUV(d12, p_147740_8_ + p_147740_4_, d11, d4, d5);
		}
	}

	public void renderBlockCropsImpl(Block p_147795_1_, int p_147795_2_, double p_147795_3_, double p_147795_5_,
			double p_147795_7_) {
		Tessellator tessellator = Tessellator.instance;
		IIcon iicon = getBlockIconFromSideAndMetadata(p_147795_1_, 0, p_147795_2_);

		if (hasOverrideBlockTexture()) {
			iicon = overrideBlockTexture;
		}

		double d3 = iicon.getMinU();
		double d4 = iicon.getMinV();
		double d5 = iicon.getMaxU();
		double d6 = iicon.getMaxV();
		double d7 = p_147795_3_ + 0.5D - 0.25D;
		double d8 = p_147795_3_ + 0.5D + 0.25D;
		double d9 = p_147795_7_ + 0.5D - 0.5D;
		double d10 = p_147795_7_ + 0.5D + 0.5D;
		tessellator.addVertexWithUV(d7, p_147795_5_ + 1.0D, d9, d3, d4);
		tessellator.addVertexWithUV(d7, p_147795_5_ + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d7, p_147795_5_ + 0.0D, d10, d5, d6);
		tessellator.addVertexWithUV(d7, p_147795_5_ + 1.0D, d10, d5, d4);
		tessellator.addVertexWithUV(d7, p_147795_5_ + 1.0D, d10, d3, d4);
		tessellator.addVertexWithUV(d7, p_147795_5_ + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d7, p_147795_5_ + 0.0D, d9, d5, d6);
		tessellator.addVertexWithUV(d7, p_147795_5_ + 1.0D, d9, d5, d4);
		tessellator.addVertexWithUV(d8, p_147795_5_ + 1.0D, d10, d3, d4);
		tessellator.addVertexWithUV(d8, p_147795_5_ + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d8, p_147795_5_ + 0.0D, d9, d5, d6);
		tessellator.addVertexWithUV(d8, p_147795_5_ + 1.0D, d9, d5, d4);
		tessellator.addVertexWithUV(d8, p_147795_5_ + 1.0D, d9, d3, d4);
		tessellator.addVertexWithUV(d8, p_147795_5_ + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d8, p_147795_5_ + 0.0D, d10, d5, d6);
		tessellator.addVertexWithUV(d8, p_147795_5_ + 1.0D, d10, d5, d4);
		d7 = p_147795_3_ + 0.5D - 0.5D;
		d8 = p_147795_3_ + 0.5D + 0.5D;
		d9 = p_147795_7_ + 0.5D - 0.25D;
		d10 = p_147795_7_ + 0.5D + 0.25D;
		tessellator.addVertexWithUV(d7, p_147795_5_ + 1.0D, d9, d3, d4);
		tessellator.addVertexWithUV(d7, p_147795_5_ + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d8, p_147795_5_ + 0.0D, d9, d5, d6);
		tessellator.addVertexWithUV(d8, p_147795_5_ + 1.0D, d9, d5, d4);
		tessellator.addVertexWithUV(d8, p_147795_5_ + 1.0D, d9, d3, d4);
		tessellator.addVertexWithUV(d8, p_147795_5_ + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d7, p_147795_5_ + 0.0D, d9, d5, d6);
		tessellator.addVertexWithUV(d7, p_147795_5_ + 1.0D, d9, d5, d4);
		tessellator.addVertexWithUV(d8, p_147795_5_ + 1.0D, d10, d3, d4);
		tessellator.addVertexWithUV(d8, p_147795_5_ + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d7, p_147795_5_ + 0.0D, d10, d5, d6);
		tessellator.addVertexWithUV(d7, p_147795_5_ + 1.0D, d10, d5, d4);
		tessellator.addVertexWithUV(d7, p_147795_5_ + 1.0D, d10, d3, d4);
		tessellator.addVertexWithUV(d7, p_147795_5_ + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d8, p_147795_5_ + 0.0D, d10, d5, d6);
		tessellator.addVertexWithUV(d8, p_147795_5_ + 1.0D, d10, d5, d4);
	}

	public boolean renderBlockLiquid(Block p_147721_1_, int p_147721_2_, int p_147721_3_, int p_147721_4_) {
		Tessellator tessellator = Tessellator.instance;
		int l = p_147721_1_.colorMultiplier(blockAccess, p_147721_2_, p_147721_3_, p_147721_4_);
		float f = (l >> 16 & 255) / 255.0F;
		float f1 = (l >> 8 & 255) / 255.0F;
		float f2 = (l & 255) / 255.0F;
		boolean flag = p_147721_1_.shouldSideBeRendered(blockAccess, p_147721_2_, p_147721_3_ + 1, p_147721_4_, 1);
		boolean flag1 = p_147721_1_.shouldSideBeRendered(blockAccess, p_147721_2_, p_147721_3_ - 1, p_147721_4_, 0);
		boolean[] aboolean = new boolean[] {
				p_147721_1_.shouldSideBeRendered(blockAccess, p_147721_2_, p_147721_3_, p_147721_4_ - 1, 2),
				p_147721_1_.shouldSideBeRendered(blockAccess, p_147721_2_, p_147721_3_, p_147721_4_ + 1, 3),
				p_147721_1_.shouldSideBeRendered(blockAccess, p_147721_2_ - 1, p_147721_3_, p_147721_4_, 4),
				p_147721_1_.shouldSideBeRendered(blockAccess, p_147721_2_ + 1, p_147721_3_, p_147721_4_, 5) };

		if (!flag && !flag1 && !aboolean[0] && !aboolean[1] && !aboolean[2] && !aboolean[3])
			return false;
		else {
			boolean flag2 = false;
			float f3 = 0.5F;
			float f4 = 1.0F;
			float f5 = 0.8F;
			float f6 = 0.6F;
			double d0 = 0.0D;
			double d1 = 1.0D;
			Material material = p_147721_1_.getMaterial();
			int i1 = blockAccess.getBlockMetadata(p_147721_2_, p_147721_3_, p_147721_4_);
			double d2 = getLiquidHeight(p_147721_2_, p_147721_3_, p_147721_4_, material);
			double d3 = getLiquidHeight(p_147721_2_, p_147721_3_, p_147721_4_ + 1, material);
			double d4 = getLiquidHeight(p_147721_2_ + 1, p_147721_3_, p_147721_4_ + 1, material);
			double d5 = getLiquidHeight(p_147721_2_ + 1, p_147721_3_, p_147721_4_, material);
			double d6 = 0.0010000000474974513D;
			float f9;
			float f10;
			float f11;

			if (renderAllFaces || flag) {
				flag2 = true;
				IIcon iicon = getBlockIconFromSideAndMetadata(p_147721_1_, 1, i1);
				float f7 = (float) BlockLiquid.getFlowDirection(blockAccess, p_147721_2_, p_147721_3_, p_147721_4_,
						material);

				if (f7 > -999.0F) {
					iicon = getBlockIconFromSideAndMetadata(p_147721_1_, 2, i1);
				}

				d2 -= d6;
				d3 -= d6;
				d4 -= d6;
				d5 -= d6;
				double d7;
				double d8;
				double d10;
				double d12;
				double d14;
				double d16;
				double d18;
				double d20;

				if (f7 < -999.0F) {
					d7 = iicon.getInterpolatedU(0.0D);
					d14 = iicon.getInterpolatedV(0.0D);
					d8 = d7;
					d16 = iicon.getInterpolatedV(16.0D);
					d10 = iicon.getInterpolatedU(16.0D);
					d18 = d16;
					d12 = d10;
					d20 = d14;
				} else {
					f9 = MathHelper.sin(f7) * 0.25F;
					f10 = MathHelper.cos(f7) * 0.25F;
					f11 = 8.0F;
					d7 = iicon.getInterpolatedU(8.0F + (-f10 - f9) * 16.0F);
					d14 = iicon.getInterpolatedV(8.0F + (-f10 + f9) * 16.0F);
					d8 = iicon.getInterpolatedU(8.0F + (-f10 + f9) * 16.0F);
					d16 = iicon.getInterpolatedV(8.0F + (f10 + f9) * 16.0F);
					d10 = iicon.getInterpolatedU(8.0F + (f10 + f9) * 16.0F);
					d18 = iicon.getInterpolatedV(8.0F + (f10 - f9) * 16.0F);
					d12 = iicon.getInterpolatedU(8.0F + (f10 - f9) * 16.0F);
					d20 = iicon.getInterpolatedV(8.0F + (-f10 - f9) * 16.0F);
				}

				tessellator.setBrightness(
						p_147721_1_.getMixedBrightnessForBlock(blockAccess, p_147721_2_, p_147721_3_, p_147721_4_));
				tessellator.setColorOpaque_F(f4 * f, f4 * f1, f4 * f2);
				tessellator.addVertexWithUV(p_147721_2_ + 0, p_147721_3_ + d2, p_147721_4_ + 0, d7, d14);
				tessellator.addVertexWithUV(p_147721_2_ + 0, p_147721_3_ + d3, p_147721_4_ + 1, d8, d16);
				tessellator.addVertexWithUV(p_147721_2_ + 1, p_147721_3_ + d4, p_147721_4_ + 1, d10, d18);
				tessellator.addVertexWithUV(p_147721_2_ + 1, p_147721_3_ + d5, p_147721_4_ + 0, d12, d20);
				tessellator.addVertexWithUV(p_147721_2_ + 0, p_147721_3_ + d2, p_147721_4_ + 0, d7, d14);
				tessellator.addVertexWithUV(p_147721_2_ + 1, p_147721_3_ + d5, p_147721_4_ + 0, d12, d20);
				tessellator.addVertexWithUV(p_147721_2_ + 1, p_147721_3_ + d4, p_147721_4_ + 1, d10, d18);
				tessellator.addVertexWithUV(p_147721_2_ + 0, p_147721_3_ + d3, p_147721_4_ + 1, d8, d16);
			}

			if (renderAllFaces || flag1) {
				tessellator.setBrightness(
						p_147721_1_.getMixedBrightnessForBlock(blockAccess, p_147721_2_, p_147721_3_ - 1, p_147721_4_));
				tessellator.setColorOpaque_F(f3, f3, f3);
				renderFaceYNeg(p_147721_1_, p_147721_2_, p_147721_3_ + d6, p_147721_4_,
						getBlockIconFromSide(p_147721_1_, 0));
				flag2 = true;
			}

			for (int k1 = 0; k1 < 4; ++k1) {
				int l1 = p_147721_2_;
				int j1 = p_147721_4_;

				if (k1 == 0) {
					j1 = p_147721_4_ - 1;
				}

				if (k1 == 1) {
					++j1;
				}

				if (k1 == 2) {
					l1 = p_147721_2_ - 1;
				}

				if (k1 == 3) {
					++l1;
				}

				IIcon iicon1 = getBlockIconFromSideAndMetadata(p_147721_1_, k1 + 2, i1);

				if (renderAllFaces || aboolean[k1]) {
					double d9;
					double d11;
					double d13;
					double d15;
					double d17;
					double d19;

					if (k1 == 0) {
						d9 = d2;
						d11 = d5;
						d13 = p_147721_2_;
						d17 = p_147721_2_ + 1;
						d15 = p_147721_4_ + d6;
						d19 = p_147721_4_ + d6;
					} else if (k1 == 1) {
						d9 = d4;
						d11 = d3;
						d13 = p_147721_2_ + 1;
						d17 = p_147721_2_;
						d15 = p_147721_4_ + 1 - d6;
						d19 = p_147721_4_ + 1 - d6;
					} else if (k1 == 2) {
						d9 = d3;
						d11 = d2;
						d13 = p_147721_2_ + d6;
						d17 = p_147721_2_ + d6;
						d15 = p_147721_4_ + 1;
						d19 = p_147721_4_;
					} else {
						d9 = d5;
						d11 = d4;
						d13 = p_147721_2_ + 1 - d6;
						d17 = p_147721_2_ + 1 - d6;
						d15 = p_147721_4_;
						d19 = p_147721_4_ + 1;
					}

					flag2 = true;
					float f8 = iicon1.getInterpolatedU(0.0D);
					f9 = iicon1.getInterpolatedU(8.0D);
					f10 = iicon1.getInterpolatedV((1.0D - d9) * 16.0D * 0.5D);
					f11 = iicon1.getInterpolatedV((1.0D - d11) * 16.0D * 0.5D);
					float f12 = iicon1.getInterpolatedV(8.0D);
					tessellator.setBrightness(p_147721_1_.getMixedBrightnessForBlock(blockAccess, l1, p_147721_3_, j1));
					float f13 = 1.0F;
					f13 *= k1 < 2 ? f5 : f6;
					tessellator.setColorOpaque_F(f4 * f13 * f, f4 * f13 * f1, f4 * f13 * f2);
					tessellator.addVertexWithUV(d13, p_147721_3_ + d9, d15, f8, f10);
					tessellator.addVertexWithUV(d17, p_147721_3_ + d11, d19, f9, f11);
					tessellator.addVertexWithUV(d17, p_147721_3_ + 0, d19, f9, f12);
					tessellator.addVertexWithUV(d13, p_147721_3_ + 0, d15, f8, f12);
					tessellator.addVertexWithUV(d13, p_147721_3_ + 0, d15, f8, f12);
					tessellator.addVertexWithUV(d17, p_147721_3_ + 0, d19, f9, f12);
					tessellator.addVertexWithUV(d17, p_147721_3_ + d11, d19, f9, f11);
					tessellator.addVertexWithUV(d13, p_147721_3_ + d9, d15, f8, f10);
				}
			}

			renderMinY = d0;
			renderMaxY = d1;
			return flag2;
		}
	}

	public float getLiquidHeight(int p_147729_1_, int p_147729_2_, int p_147729_3_, Material p_147729_4_) {
		int l = 0;
		float f = 0.0F;

		for (int i1 = 0; i1 < 4; ++i1) {
			int j1 = p_147729_1_ - (i1 & 1);
			int k1 = p_147729_3_ - (i1 >> 1 & 1);

			if (blockAccess.getBlock(j1, p_147729_2_ + 1, k1).getMaterial() == p_147729_4_)
				return 1.0F;

			Material material1 = blockAccess.getBlock(j1, p_147729_2_, k1).getMaterial();

			if (material1 == p_147729_4_) {
				int l1 = blockAccess.getBlockMetadata(j1, p_147729_2_, k1);

				if (l1 >= 8 || l1 == 0) {
					f += BlockLiquid.getLiquidHeightPercent(l1) * 10.0F;
					l += 10;
				}

				f += BlockLiquid.getLiquidHeightPercent(l1);
				++l;
			} else if (!material1.isSolid()) {
				++f;
				++l;
			}
		}

		return 1.0F - f / l;
	}

	public void renderBlockSandFalling(Block p_147749_1_, World p_147749_2_, int p_147749_3_, int p_147749_4_,
			int p_147749_5_, int p_147749_6_) {
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setBrightness(
				p_147749_1_.getMixedBrightnessForBlock(p_147749_2_, p_147749_3_, p_147749_4_, p_147749_5_));
		tessellator.setColorOpaque_F(f, f, f);
		renderFaceYNeg(p_147749_1_, -0.5D, -0.5D, -0.5D, getBlockIconFromSideAndMetadata(p_147749_1_, 0, p_147749_6_));
		tessellator.setColorOpaque_F(f1, f1, f1);
		renderFaceYPos(p_147749_1_, -0.5D, -0.5D, -0.5D, getBlockIconFromSideAndMetadata(p_147749_1_, 1, p_147749_6_));
		tessellator.setColorOpaque_F(f2, f2, f2);
		renderFaceZNeg(p_147749_1_, -0.5D, -0.5D, -0.5D, getBlockIconFromSideAndMetadata(p_147749_1_, 2, p_147749_6_));
		tessellator.setColorOpaque_F(f2, f2, f2);
		renderFaceZPos(p_147749_1_, -0.5D, -0.5D, -0.5D, getBlockIconFromSideAndMetadata(p_147749_1_, 3, p_147749_6_));
		tessellator.setColorOpaque_F(f3, f3, f3);
		renderFaceXNeg(p_147749_1_, -0.5D, -0.5D, -0.5D, getBlockIconFromSideAndMetadata(p_147749_1_, 4, p_147749_6_));
		tessellator.setColorOpaque_F(f3, f3, f3);
		renderFaceXPos(p_147749_1_, -0.5D, -0.5D, -0.5D, getBlockIconFromSideAndMetadata(p_147749_1_, 5, p_147749_6_));
		tessellator.draw();
	}

	public boolean renderStandardBlock(Block p_147784_1_, int p_147784_2_, int p_147784_3_, int p_147784_4_) {
		int l = p_147784_1_.colorMultiplier(blockAccess, p_147784_2_, p_147784_3_, p_147784_4_);
		float f = (l >> 16 & 255) / 255.0F;
		float f1 = (l >> 8 & 255) / 255.0F;
		float f2 = (l & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		return Minecraft.isAmbientOcclusionEnabled() && p_147784_1_.getLightValue() == 0 ? partialRenderBounds
				? renderStandardBlockWithAmbientOcclusionPartial(p_147784_1_, p_147784_2_, p_147784_3_, p_147784_4_, f,
						f1, f2)
				: renderStandardBlockWithAmbientOcclusion(p_147784_1_, p_147784_2_, p_147784_3_, p_147784_4_, f, f1, f2)
				: renderStandardBlockWithColorMultiplier(p_147784_1_, p_147784_2_, p_147784_3_, p_147784_4_, f, f1, f2);
	}

	public boolean renderBlockLog(Block p_147742_1_, int p_147742_2_, int p_147742_3_, int p_147742_4_) {
		int l = blockAccess.getBlockMetadata(p_147742_2_, p_147742_3_, p_147742_4_);
		int i1 = l & 12;

		if (i1 == 4) {
			uvRotateEast = 1;
			uvRotateWest = 1;
			uvRotateTop = 1;
			uvRotateBottom = 1;
		} else if (i1 == 8) {
			uvRotateSouth = 1;
			uvRotateNorth = 1;
		}

		boolean flag = renderStandardBlock(p_147742_1_, p_147742_2_, p_147742_3_, p_147742_4_);
		uvRotateSouth = 0;
		uvRotateEast = 0;
		uvRotateWest = 0;
		uvRotateNorth = 0;
		uvRotateTop = 0;
		uvRotateBottom = 0;
		return flag;
	}

	public boolean renderBlockQuartz(Block p_147779_1_, int p_147779_2_, int p_147779_3_, int p_147779_4_) {
		int l = blockAccess.getBlockMetadata(p_147779_2_, p_147779_3_, p_147779_4_);

		if (l == 3) {
			uvRotateEast = 1;
			uvRotateWest = 1;
			uvRotateTop = 1;
			uvRotateBottom = 1;
		} else if (l == 4) {
			uvRotateSouth = 1;
			uvRotateNorth = 1;
		}

		boolean flag = renderStandardBlock(p_147779_1_, p_147779_2_, p_147779_3_, p_147779_4_);
		uvRotateSouth = 0;
		uvRotateEast = 0;
		uvRotateWest = 0;
		uvRotateNorth = 0;
		uvRotateTop = 0;
		uvRotateBottom = 0;
		return flag;
	}

	public boolean renderStandardBlockWithAmbientOcclusion(Block p_147751_1_, int p_147751_2_, int p_147751_3_,
			int p_147751_4_, float p_147751_5_, float p_147751_6_, float p_147751_7_) {
		enableAO = true;
		boolean flag = false;
		float f3 = 0.0F;
		float f4 = 0.0F;
		float f5 = 0.0F;
		float f6 = 0.0F;
		boolean flag1 = true;
		int l = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_, p_147751_4_);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(983055);

		if (this.getBlockIcon(p_147751_1_).getIconName().equals("grass_top")) {
			flag1 = false;
		} else if (hasOverrideBlockTexture()) {
			flag1 = false;
		}

		boolean flag2;
		boolean flag3;
		boolean flag4;
		boolean flag5;
		int i1;
		float f7;

		if (renderAllFaces
				|| p_147751_1_.shouldSideBeRendered(blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_, 0)) {
			if (renderMinY <= 0.0D) {
				--p_147751_3_;
			}

			aoBrightnessXYNN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ - 1, p_147751_3_,
					p_147751_4_);
			aoBrightnessYZNN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_,
					p_147751_4_ - 1);
			aoBrightnessYZNP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_,
					p_147751_4_ + 1);
			aoBrightnessXYPN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ + 1, p_147751_3_,
					p_147751_4_);
			aoLightValueScratchXYNN = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchYZNN = blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchYZNP = blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXYPN = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_)
					.getAmbientOcclusionLightValue();
			flag2 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_).getCanBlockGrass();
			flag3 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_).getCanBlockGrass();
			flag4 = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1).getCanBlockGrass();

			if (!flag5 && !flag3) {
				aoLightValueScratchXYZNNN = aoLightValueScratchXYNN;
				aoBrightnessXYZNNN = aoBrightnessXYNN;
			} else {
				aoLightValueScratchXYZNNN = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNNN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ - 1, p_147751_3_,
						p_147751_4_ - 1);
			}

			if (!flag4 && !flag3) {
				aoLightValueScratchXYZNNP = aoLightValueScratchXYNN;
				aoBrightnessXYZNNP = aoBrightnessXYNN;
			} else {
				aoLightValueScratchXYZNNP = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNNP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ - 1, p_147751_3_,
						p_147751_4_ + 1);
			}

			if (!flag5 && !flag2) {
				aoLightValueScratchXYZPNN = aoLightValueScratchXYPN;
				aoBrightnessXYZPNN = aoBrightnessXYPN;
			} else {
				aoLightValueScratchXYZPNN = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPNN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ + 1, p_147751_3_,
						p_147751_4_ - 1);
			}

			if (!flag4 && !flag2) {
				aoLightValueScratchXYZPNP = aoLightValueScratchXYPN;
				aoBrightnessXYZPNP = aoBrightnessXYPN;
			} else {
				aoLightValueScratchXYZPNP = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPNP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ + 1, p_147751_3_,
						p_147751_4_ + 1);
			}

			if (renderMinY <= 0.0D) {
				++p_147751_3_;
			}

			i1 = l;

			if (renderMinY <= 0.0D || !blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_).isOpaqueCube()) {
				i1 = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ - 1, p_147751_4_);
			}

			f7 = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_).getAmbientOcclusionLightValue();
			f3 = (aoLightValueScratchXYZNNP + aoLightValueScratchXYNN + aoLightValueScratchYZNP + f7) / 4.0F;
			f6 = (aoLightValueScratchYZNP + f7 + aoLightValueScratchXYZPNP + aoLightValueScratchXYPN) / 4.0F;
			f5 = (f7 + aoLightValueScratchYZNN + aoLightValueScratchXYPN + aoLightValueScratchXYZPNN) / 4.0F;
			f4 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNN + f7 + aoLightValueScratchYZNN) / 4.0F;
			brightnessTopLeft = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXYNN, aoBrightnessYZNP, i1);
			brightnessTopRight = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXYPN, i1);
			brightnessBottomRight = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYPN, aoBrightnessXYZPNN, i1);
			brightnessBottomLeft = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNN, aoBrightnessYZNN, i1);

			if (flag1) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147751_5_ * 0.5F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147751_6_
						* 0.5F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147751_7_ * 0.5F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.5F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.5F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.5F;
			}

			colorRedTopLeft *= f3;
			colorGreenTopLeft *= f3;
			colorBlueTopLeft *= f3;
			colorRedBottomLeft *= f4;
			colorGreenBottomLeft *= f4;
			colorBlueBottomLeft *= f4;
			colorRedBottomRight *= f5;
			colorGreenBottomRight *= f5;
			colorBlueBottomRight *= f5;
			colorRedTopRight *= f6;
			colorGreenTopRight *= f6;
			colorBlueTopRight *= f6;
			renderFaceYNeg(p_147751_1_, p_147751_2_, p_147751_3_, p_147751_4_,
					this.getBlockIcon(p_147751_1_, blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 0));
			flag = true;
		}

		if (renderAllFaces
				|| p_147751_1_.shouldSideBeRendered(blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_, 1)) {
			if (renderMaxY >= 1.0D) {
				++p_147751_3_;
			}

			aoBrightnessXYNP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ - 1, p_147751_3_,
					p_147751_4_);
			aoBrightnessXYPP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ + 1, p_147751_3_,
					p_147751_4_);
			aoBrightnessYZPN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_,
					p_147751_4_ - 1);
			aoBrightnessYZPP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_,
					p_147751_4_ + 1);
			aoLightValueScratchXYNP = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXYPP = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchYZPN = blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchYZPP = blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1)
					.getAmbientOcclusionLightValue();
			flag2 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_).getCanBlockGrass();
			flag3 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_).getCanBlockGrass();
			flag4 = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1).getCanBlockGrass();

			if (!flag5 && !flag3) {
				aoLightValueScratchXYZNPN = aoLightValueScratchXYNP;
				aoBrightnessXYZNPN = aoBrightnessXYNP;
			} else {
				aoLightValueScratchXYZNPN = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNPN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ - 1, p_147751_3_,
						p_147751_4_ - 1);
			}

			if (!flag5 && !flag2) {
				aoLightValueScratchXYZPPN = aoLightValueScratchXYPP;
				aoBrightnessXYZPPN = aoBrightnessXYPP;
			} else {
				aoLightValueScratchXYZPPN = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPPN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ + 1, p_147751_3_,
						p_147751_4_ - 1);
			}

			if (!flag4 && !flag3) {
				aoLightValueScratchXYZNPP = aoLightValueScratchXYNP;
				aoBrightnessXYZNPP = aoBrightnessXYNP;
			} else {
				aoLightValueScratchXYZNPP = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNPP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ - 1, p_147751_3_,
						p_147751_4_ + 1);
			}

			if (!flag4 && !flag2) {
				aoLightValueScratchXYZPPP = aoLightValueScratchXYPP;
				aoBrightnessXYZPPP = aoBrightnessXYPP;
			} else {
				aoLightValueScratchXYZPPP = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPPP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ + 1, p_147751_3_,
						p_147751_4_ + 1);
			}

			if (renderMaxY >= 1.0D) {
				--p_147751_3_;
			}

			i1 = l;

			if (renderMaxY >= 1.0D || !blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_).isOpaqueCube()) {
				i1 = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ + 1, p_147751_4_);
			}

			f7 = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_).getAmbientOcclusionLightValue();
			f6 = (aoLightValueScratchXYZNPP + aoLightValueScratchXYNP + aoLightValueScratchYZPP + f7) / 4.0F;
			f3 = (aoLightValueScratchYZPP + f7 + aoLightValueScratchXYZPPP + aoLightValueScratchXYPP) / 4.0F;
			f4 = (f7 + aoLightValueScratchYZPN + aoLightValueScratchXYPP + aoLightValueScratchXYZPPN) / 4.0F;
			f5 = (aoLightValueScratchXYNP + aoLightValueScratchXYZNPN + f7 + aoLightValueScratchYZPN) / 4.0F;
			brightnessTopRight = getAoBrightness(aoBrightnessXYZNPP, aoBrightnessXYNP, aoBrightnessYZPP, i1);
			brightnessTopLeft = getAoBrightness(aoBrightnessYZPP, aoBrightnessXYZPPP, aoBrightnessXYPP, i1);
			brightnessBottomLeft = getAoBrightness(aoBrightnessYZPN, aoBrightnessXYPP, aoBrightnessXYZPPN, i1);
			brightnessBottomRight = getAoBrightness(aoBrightnessXYNP, aoBrightnessXYZNPN, aoBrightnessYZPN, i1);
			colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147751_5_;
			colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147751_6_;
			colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147751_7_;
			colorRedTopLeft *= f3;
			colorGreenTopLeft *= f3;
			colorBlueTopLeft *= f3;
			colorRedBottomLeft *= f4;
			colorGreenBottomLeft *= f4;
			colorBlueBottomLeft *= f4;
			colorRedBottomRight *= f5;
			colorGreenBottomRight *= f5;
			colorBlueBottomRight *= f5;
			colorRedTopRight *= f6;
			colorGreenTopRight *= f6;
			colorBlueTopRight *= f6;
			renderFaceYPos(p_147751_1_, p_147751_2_, p_147751_3_, p_147751_4_,
					this.getBlockIcon(p_147751_1_, blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 1));
			flag = true;
		}

		IIcon iicon;

		if (renderAllFaces
				|| p_147751_1_.shouldSideBeRendered(blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ - 1, 2)) {
			if (renderMinZ <= 0.0D) {
				--p_147751_4_;
			}

			aoLightValueScratchXZNN = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchYZNN = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchYZPN = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXZPN = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_)
					.getAmbientOcclusionLightValue();
			aoBrightnessXZNN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ - 1, p_147751_3_,
					p_147751_4_);
			aoBrightnessYZNN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ - 1,
					p_147751_4_);
			aoBrightnessYZPN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ + 1,
					p_147751_4_);
			aoBrightnessXZPN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ + 1, p_147751_3_,
					p_147751_4_);
			flag2 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1).getCanBlockGrass();
			flag3 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1).getCanBlockGrass();
			flag4 = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1).getCanBlockGrass();

			if (!flag3 && !flag5) {
				aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
				aoBrightnessXYZNNN = aoBrightnessXZNN;
			} else {
				aoLightValueScratchXYZNNN = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNNN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ - 1,
						p_147751_3_ - 1, p_147751_4_);
			}

			if (!flag3 && !flag4) {
				aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
				aoBrightnessXYZNPN = aoBrightnessXZNN;
			} else {
				aoLightValueScratchXYZNPN = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNPN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ - 1,
						p_147751_3_ + 1, p_147751_4_);
			}

			if (!flag2 && !flag5) {
				aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
				aoBrightnessXYZPNN = aoBrightnessXZPN;
			} else {
				aoLightValueScratchXYZPNN = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPNN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ + 1,
						p_147751_3_ - 1, p_147751_4_);
			}

			if (!flag2 && !flag4) {
				aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
				aoBrightnessXYZPPN = aoBrightnessXZPN;
			} else {
				aoLightValueScratchXYZPPN = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPPN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ + 1,
						p_147751_3_ + 1, p_147751_4_);
			}

			if (renderMinZ <= 0.0D) {
				++p_147751_4_;
			}

			i1 = l;

			if (renderMinZ <= 0.0D || !blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1).isOpaqueCube()) {
				i1 = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ - 1);
			}

			f7 = blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1).getAmbientOcclusionLightValue();
			f3 = (aoLightValueScratchXZNN + aoLightValueScratchXYZNPN + f7 + aoLightValueScratchYZPN) / 4.0F;
			f4 = (f7 + aoLightValueScratchYZPN + aoLightValueScratchXZPN + aoLightValueScratchXYZPPN) / 4.0F;
			f5 = (aoLightValueScratchYZNN + f7 + aoLightValueScratchXYZPNN + aoLightValueScratchXZPN) / 4.0F;
			f6 = (aoLightValueScratchXYZNNN + aoLightValueScratchXZNN + aoLightValueScratchYZNN + f7) / 4.0F;
			brightnessTopLeft = getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN, aoBrightnessYZPN, i1);
			brightnessBottomLeft = getAoBrightness(aoBrightnessYZPN, aoBrightnessXZPN, aoBrightnessXYZPPN, i1);
			brightnessBottomRight = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYZPNN, aoBrightnessXZPN, i1);
			brightnessTopRight = getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXZNN, aoBrightnessYZNN, i1);

			if (flag1) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147751_5_ * 0.8F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147751_6_
						* 0.8F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147751_7_ * 0.8F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.8F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.8F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.8F;
			}

			colorRedTopLeft *= f3;
			colorGreenTopLeft *= f3;
			colorBlueTopLeft *= f3;
			colorRedBottomLeft *= f4;
			colorGreenBottomLeft *= f4;
			colorBlueBottomLeft *= f4;
			colorRedBottomRight *= f5;
			colorGreenBottomRight *= f5;
			colorBlueBottomRight *= f5;
			colorRedTopRight *= f6;
			colorGreenTopRight *= f6;
			colorBlueTopRight *= f6;
			iicon = this.getBlockIcon(p_147751_1_, blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 2);
			renderFaceZNeg(p_147751_1_, p_147751_2_, p_147751_3_, p_147751_4_, iicon);

			if (fancyGrass && iicon.getIconName().equals("grass_side") && !hasOverrideBlockTexture()) {
				colorRedTopLeft *= p_147751_5_;
				colorRedBottomLeft *= p_147751_5_;
				colorRedBottomRight *= p_147751_5_;
				colorRedTopRight *= p_147751_5_;
				colorGreenTopLeft *= p_147751_6_;
				colorGreenBottomLeft *= p_147751_6_;
				colorGreenBottomRight *= p_147751_6_;
				colorGreenTopRight *= p_147751_6_;
				colorBlueTopLeft *= p_147751_7_;
				colorBlueBottomLeft *= p_147751_7_;
				colorBlueBottomRight *= p_147751_7_;
				colorBlueTopRight *= p_147751_7_;
				renderFaceZNeg(p_147751_1_, p_147751_2_, p_147751_3_, p_147751_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderAllFaces
				|| p_147751_1_.shouldSideBeRendered(blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ + 1, 3)) {
			if (renderMaxZ >= 1.0D) {
				++p_147751_4_;
			}

			aoLightValueScratchXZNP = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXZPP = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchYZNP = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchYZPP = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_)
					.getAmbientOcclusionLightValue();
			aoBrightnessXZNP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ - 1, p_147751_3_,
					p_147751_4_);
			aoBrightnessXZPP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ + 1, p_147751_3_,
					p_147751_4_);
			aoBrightnessYZNP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ - 1,
					p_147751_4_);
			aoBrightnessYZPP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ + 1,
					p_147751_4_);
			flag2 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1).getCanBlockGrass();
			flag3 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1).getCanBlockGrass();
			flag4 = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1).getCanBlockGrass();

			if (!flag3 && !flag5) {
				aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
				aoBrightnessXYZNNP = aoBrightnessXZNP;
			} else {
				aoLightValueScratchXYZNNP = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNNP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ - 1,
						p_147751_3_ - 1, p_147751_4_);
			}

			if (!flag3 && !flag4) {
				aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
				aoBrightnessXYZNPP = aoBrightnessXZNP;
			} else {
				aoLightValueScratchXYZNPP = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNPP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ - 1,
						p_147751_3_ + 1, p_147751_4_);
			}

			if (!flag2 && !flag5) {
				aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
				aoBrightnessXYZPNP = aoBrightnessXZPP;
			} else {
				aoLightValueScratchXYZPNP = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPNP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ + 1,
						p_147751_3_ - 1, p_147751_4_);
			}

			if (!flag2 && !flag4) {
				aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
				aoBrightnessXYZPPP = aoBrightnessXZPP;
			} else {
				aoLightValueScratchXYZPPP = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPPP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ + 1,
						p_147751_3_ + 1, p_147751_4_);
			}

			if (renderMaxZ >= 1.0D) {
				--p_147751_4_;
			}

			i1 = l;

			if (renderMaxZ >= 1.0D || !blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1).isOpaqueCube()) {
				i1 = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_, p_147751_4_ + 1);
			}

			f7 = blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1).getAmbientOcclusionLightValue();
			f3 = (aoLightValueScratchXZNP + aoLightValueScratchXYZNPP + f7 + aoLightValueScratchYZPP) / 4.0F;
			f6 = (f7 + aoLightValueScratchYZPP + aoLightValueScratchXZPP + aoLightValueScratchXYZPPP) / 4.0F;
			f5 = (aoLightValueScratchYZNP + f7 + aoLightValueScratchXYZPNP + aoLightValueScratchXZPP) / 4.0F;
			f4 = (aoLightValueScratchXYZNNP + aoLightValueScratchXZNP + aoLightValueScratchYZNP + f7) / 4.0F;
			brightnessTopLeft = getAoBrightness(aoBrightnessXZNP, aoBrightnessXYZNPP, aoBrightnessYZPP, i1);
			brightnessTopRight = getAoBrightness(aoBrightnessYZPP, aoBrightnessXZPP, aoBrightnessXYZPPP, i1);
			brightnessBottomRight = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXZPP, i1);
			brightnessBottomLeft = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXZNP, aoBrightnessYZNP, i1);

			if (flag1) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147751_5_ * 0.8F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147751_6_
						* 0.8F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147751_7_ * 0.8F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.8F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.8F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.8F;
			}

			colorRedTopLeft *= f3;
			colorGreenTopLeft *= f3;
			colorBlueTopLeft *= f3;
			colorRedBottomLeft *= f4;
			colorGreenBottomLeft *= f4;
			colorBlueBottomLeft *= f4;
			colorRedBottomRight *= f5;
			colorGreenBottomRight *= f5;
			colorBlueBottomRight *= f5;
			colorRedTopRight *= f6;
			colorGreenTopRight *= f6;
			colorBlueTopRight *= f6;
			iicon = this.getBlockIcon(p_147751_1_, blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 3);
			renderFaceZPos(p_147751_1_, p_147751_2_, p_147751_3_, p_147751_4_,
					this.getBlockIcon(p_147751_1_, blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 3));

			if (fancyGrass && iicon.getIconName().equals("grass_side") && !hasOverrideBlockTexture()) {
				colorRedTopLeft *= p_147751_5_;
				colorRedBottomLeft *= p_147751_5_;
				colorRedBottomRight *= p_147751_5_;
				colorRedTopRight *= p_147751_5_;
				colorGreenTopLeft *= p_147751_6_;
				colorGreenBottomLeft *= p_147751_6_;
				colorGreenBottomRight *= p_147751_6_;
				colorGreenTopRight *= p_147751_6_;
				colorBlueTopLeft *= p_147751_7_;
				colorBlueBottomLeft *= p_147751_7_;
				colorBlueBottomRight *= p_147751_7_;
				colorBlueTopRight *= p_147751_7_;
				renderFaceZPos(p_147751_1_, p_147751_2_, p_147751_3_, p_147751_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderAllFaces
				|| p_147751_1_.shouldSideBeRendered(blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_, 4)) {
			if (renderMinX <= 0.0D) {
				--p_147751_2_;
			}

			aoLightValueScratchXYNN = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXZNN = blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXZNP = blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXYNP = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_)
					.getAmbientOcclusionLightValue();
			aoBrightnessXYNN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ - 1,
					p_147751_4_);
			aoBrightnessXZNN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_,
					p_147751_4_ - 1);
			aoBrightnessXZNP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_,
					p_147751_4_ + 1);
			aoBrightnessXYNP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ + 1,
					p_147751_4_);
			flag2 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ + 1, p_147751_4_).getCanBlockGrass();
			flag3 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_ - 1, p_147751_4_).getCanBlockGrass();
			flag4 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ - 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_ + 1).getCanBlockGrass();

			if (!flag4 && !flag3) {
				aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
				aoBrightnessXYZNNN = aoBrightnessXZNN;
			} else {
				aoLightValueScratchXYZNNN = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNNN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ - 1,
						p_147751_4_ - 1);
			}

			if (!flag5 && !flag3) {
				aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
				aoBrightnessXYZNNP = aoBrightnessXZNP;
			} else {
				aoLightValueScratchXYZNNP = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNNP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ - 1,
						p_147751_4_ + 1);
			}

			if (!flag4 && !flag2) {
				aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
				aoBrightnessXYZNPN = aoBrightnessXZNN;
			} else {
				aoLightValueScratchXYZNPN = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNPN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ + 1,
						p_147751_4_ - 1);
			}

			if (!flag5 && !flag2) {
				aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
				aoBrightnessXYZNPP = aoBrightnessXZNP;
			} else {
				aoLightValueScratchXYZNPP = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNPP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ + 1,
						p_147751_4_ + 1);
			}

			if (renderMinX <= 0.0D) {
				++p_147751_2_;
			}

			i1 = l;

			if (renderMinX <= 0.0D || !blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_).isOpaqueCube()) {
				i1 = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ - 1, p_147751_3_, p_147751_4_);
			}

			f7 = blockAccess.getBlock(p_147751_2_ - 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			f6 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNP + f7 + aoLightValueScratchXZNP) / 4.0F;
			f3 = (f7 + aoLightValueScratchXZNP + aoLightValueScratchXYNP + aoLightValueScratchXYZNPP) / 4.0F;
			f4 = (aoLightValueScratchXZNN + f7 + aoLightValueScratchXYZNPN + aoLightValueScratchXYNP) / 4.0F;
			f5 = (aoLightValueScratchXYZNNN + aoLightValueScratchXYNN + aoLightValueScratchXZNN + f7) / 4.0F;
			brightnessTopRight = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNP, aoBrightnessXZNP, i1);
			brightnessTopLeft = getAoBrightness(aoBrightnessXZNP, aoBrightnessXYNP, aoBrightnessXYZNPP, i1);
			brightnessBottomLeft = getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN, aoBrightnessXYNP, i1);
			brightnessBottomRight = getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXYNN, aoBrightnessXZNN, i1);

			if (flag1) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147751_5_ * 0.6F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147751_6_
						* 0.6F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147751_7_ * 0.6F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.6F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.6F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.6F;
			}

			colorRedTopLeft *= f3;
			colorGreenTopLeft *= f3;
			colorBlueTopLeft *= f3;
			colorRedBottomLeft *= f4;
			colorGreenBottomLeft *= f4;
			colorBlueBottomLeft *= f4;
			colorRedBottomRight *= f5;
			colorGreenBottomRight *= f5;
			colorBlueBottomRight *= f5;
			colorRedTopRight *= f6;
			colorGreenTopRight *= f6;
			colorBlueTopRight *= f6;
			iicon = this.getBlockIcon(p_147751_1_, blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 4);
			renderFaceXNeg(p_147751_1_, p_147751_2_, p_147751_3_, p_147751_4_, iicon);

			if (fancyGrass && iicon.getIconName().equals("grass_side") && !hasOverrideBlockTexture()) {
				colorRedTopLeft *= p_147751_5_;
				colorRedBottomLeft *= p_147751_5_;
				colorRedBottomRight *= p_147751_5_;
				colorRedTopRight *= p_147751_5_;
				colorGreenTopLeft *= p_147751_6_;
				colorGreenBottomLeft *= p_147751_6_;
				colorGreenBottomRight *= p_147751_6_;
				colorGreenTopRight *= p_147751_6_;
				colorBlueTopLeft *= p_147751_7_;
				colorBlueBottomLeft *= p_147751_7_;
				colorBlueBottomRight *= p_147751_7_;
				colorBlueTopRight *= p_147751_7_;
				renderFaceXNeg(p_147751_1_, p_147751_2_, p_147751_3_, p_147751_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderAllFaces
				|| p_147751_1_.shouldSideBeRendered(blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_, 5)) {
			if (renderMaxX >= 1.0D) {
				++p_147751_2_;
			}

			aoLightValueScratchXYPN = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXZPN = blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ - 1)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXZPP = blockAccess.getBlock(p_147751_2_, p_147751_3_, p_147751_4_ + 1)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXYPP = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_)
					.getAmbientOcclusionLightValue();
			aoBrightnessXYPN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ - 1,
					p_147751_4_);
			aoBrightnessXZPN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_,
					p_147751_4_ - 1);
			aoBrightnessXZPP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_,
					p_147751_4_ + 1);
			aoBrightnessXYPP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ + 1,
					p_147751_4_);
			flag2 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ + 1, p_147751_4_).getCanBlockGrass();
			flag3 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_ - 1, p_147751_4_).getCanBlockGrass();
			flag4 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ + 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_ - 1).getCanBlockGrass();

			if (!flag3 && !flag5) {
				aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
				aoBrightnessXYZPNN = aoBrightnessXZPN;
			} else {
				aoLightValueScratchXYZPNN = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ - 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPNN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ - 1,
						p_147751_4_ - 1);
			}

			if (!flag3 && !flag4) {
				aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
				aoBrightnessXYZPNP = aoBrightnessXZPP;
			} else {
				aoLightValueScratchXYZPNP = blockAccess.getBlock(p_147751_2_, p_147751_3_ - 1, p_147751_4_ + 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPNP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ - 1,
						p_147751_4_ + 1);
			}

			if (!flag2 && !flag5) {
				aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
				aoBrightnessXYZPPN = aoBrightnessXZPN;
			} else {
				aoLightValueScratchXYZPPN = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ - 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPPN = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ + 1,
						p_147751_4_ - 1);
			}

			if (!flag2 && !flag4) {
				aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
				aoBrightnessXYZPPP = aoBrightnessXZPP;
			} else {
				aoLightValueScratchXYZPPP = blockAccess.getBlock(p_147751_2_, p_147751_3_ + 1, p_147751_4_ + 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPPP = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_, p_147751_3_ + 1,
						p_147751_4_ + 1);
			}

			if (renderMaxX >= 1.0D) {
				--p_147751_2_;
			}

			i1 = l;

			if (renderMaxX >= 1.0D || !blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_).isOpaqueCube()) {
				i1 = p_147751_1_.getMixedBrightnessForBlock(blockAccess, p_147751_2_ + 1, p_147751_3_, p_147751_4_);
			}

			f7 = blockAccess.getBlock(p_147751_2_ + 1, p_147751_3_, p_147751_4_).getAmbientOcclusionLightValue();
			f3 = (aoLightValueScratchXYPN + aoLightValueScratchXYZPNP + f7 + aoLightValueScratchXZPP) / 4.0F;
			f4 = (aoLightValueScratchXYZPNN + aoLightValueScratchXYPN + aoLightValueScratchXZPN + f7) / 4.0F;
			f5 = (aoLightValueScratchXZPN + f7 + aoLightValueScratchXYZPPN + aoLightValueScratchXYPP) / 4.0F;
			f6 = (f7 + aoLightValueScratchXZPP + aoLightValueScratchXYPP + aoLightValueScratchXYZPPP) / 4.0F;
			brightnessTopLeft = getAoBrightness(aoBrightnessXYPN, aoBrightnessXYZPNP, aoBrightnessXZPP, i1);
			brightnessTopRight = getAoBrightness(aoBrightnessXZPP, aoBrightnessXYPP, aoBrightnessXYZPPP, i1);
			brightnessBottomRight = getAoBrightness(aoBrightnessXZPN, aoBrightnessXYZPPN, aoBrightnessXYPP, i1);
			brightnessBottomLeft = getAoBrightness(aoBrightnessXYZPNN, aoBrightnessXYPN, aoBrightnessXZPN, i1);

			if (flag1) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147751_5_ * 0.6F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147751_6_
						* 0.6F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147751_7_ * 0.6F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.6F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.6F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.6F;
			}

			colorRedTopLeft *= f3;
			colorGreenTopLeft *= f3;
			colorBlueTopLeft *= f3;
			colorRedBottomLeft *= f4;
			colorGreenBottomLeft *= f4;
			colorBlueBottomLeft *= f4;
			colorRedBottomRight *= f5;
			colorGreenBottomRight *= f5;
			colorBlueBottomRight *= f5;
			colorRedTopRight *= f6;
			colorGreenTopRight *= f6;
			colorBlueTopRight *= f6;
			iicon = this.getBlockIcon(p_147751_1_, blockAccess, p_147751_2_, p_147751_3_, p_147751_4_, 5);
			renderFaceXPos(p_147751_1_, p_147751_2_, p_147751_3_, p_147751_4_, iicon);

			if (fancyGrass && iicon.getIconName().equals("grass_side") && !hasOverrideBlockTexture()) {
				colorRedTopLeft *= p_147751_5_;
				colorRedBottomLeft *= p_147751_5_;
				colorRedBottomRight *= p_147751_5_;
				colorRedTopRight *= p_147751_5_;
				colorGreenTopLeft *= p_147751_6_;
				colorGreenBottomLeft *= p_147751_6_;
				colorGreenBottomRight *= p_147751_6_;
				colorGreenTopRight *= p_147751_6_;
				colorBlueTopLeft *= p_147751_7_;
				colorBlueBottomLeft *= p_147751_7_;
				colorBlueBottomRight *= p_147751_7_;
				colorBlueTopRight *= p_147751_7_;
				renderFaceXPos(p_147751_1_, p_147751_2_, p_147751_3_, p_147751_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		enableAO = false;
		return flag;
	}

	public boolean renderStandardBlockWithAmbientOcclusionPartial(Block p_147808_1_, int p_147808_2_, int p_147808_3_,
			int p_147808_4_, float p_147808_5_, float p_147808_6_, float p_147808_7_) {
		enableAO = true;
		boolean flag = false;
		float f3 = 0.0F;
		float f4 = 0.0F;
		float f5 = 0.0F;
		float f6 = 0.0F;
		boolean flag1 = true;
		int l = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_, p_147808_4_);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(983055);

		if (this.getBlockIcon(p_147808_1_).getIconName().equals("grass_top")) {
			flag1 = false;
		} else if (hasOverrideBlockTexture()) {
			flag1 = false;
		}

		boolean flag2;
		boolean flag3;
		boolean flag4;
		boolean flag5;
		int i1;
		float f7;

		if (renderAllFaces
				|| p_147808_1_.shouldSideBeRendered(blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_, 0)) {
			if (renderMinY <= 0.0D) {
				--p_147808_3_;
			}

			aoBrightnessXYNN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ - 1, p_147808_3_,
					p_147808_4_);
			aoBrightnessYZNN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_,
					p_147808_4_ - 1);
			aoBrightnessYZNP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_,
					p_147808_4_ + 1);
			aoBrightnessXYPN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ + 1, p_147808_3_,
					p_147808_4_);
			aoLightValueScratchXYNN = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchYZNN = blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchYZNP = blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXYPN = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_)
					.getAmbientOcclusionLightValue();
			flag2 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_).getCanBlockGrass();
			flag3 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_).getCanBlockGrass();
			flag4 = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1).getCanBlockGrass();

			if (!flag5 && !flag3) {
				aoLightValueScratchXYZNNN = aoLightValueScratchXYNN;
				aoBrightnessXYZNNN = aoBrightnessXYNN;
			} else {
				aoLightValueScratchXYZNNN = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNNN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ - 1, p_147808_3_,
						p_147808_4_ - 1);
			}

			if (!flag4 && !flag3) {
				aoLightValueScratchXYZNNP = aoLightValueScratchXYNN;
				aoBrightnessXYZNNP = aoBrightnessXYNN;
			} else {
				aoLightValueScratchXYZNNP = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNNP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ - 1, p_147808_3_,
						p_147808_4_ + 1);
			}

			if (!flag5 && !flag2) {
				aoLightValueScratchXYZPNN = aoLightValueScratchXYPN;
				aoBrightnessXYZPNN = aoBrightnessXYPN;
			} else {
				aoLightValueScratchXYZPNN = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPNN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ + 1, p_147808_3_,
						p_147808_4_ - 1);
			}

			if (!flag4 && !flag2) {
				aoLightValueScratchXYZPNP = aoLightValueScratchXYPN;
				aoBrightnessXYZPNP = aoBrightnessXYPN;
			} else {
				aoLightValueScratchXYZPNP = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPNP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ + 1, p_147808_3_,
						p_147808_4_ + 1);
			}

			if (renderMinY <= 0.0D) {
				++p_147808_3_;
			}

			i1 = l;

			if (renderMinY <= 0.0D || !blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_).isOpaqueCube()) {
				i1 = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ - 1, p_147808_4_);
			}

			f7 = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_).getAmbientOcclusionLightValue();
			f3 = (aoLightValueScratchXYZNNP + aoLightValueScratchXYNN + aoLightValueScratchYZNP + f7) / 4.0F;
			f6 = (aoLightValueScratchYZNP + f7 + aoLightValueScratchXYZPNP + aoLightValueScratchXYPN) / 4.0F;
			f5 = (f7 + aoLightValueScratchYZNN + aoLightValueScratchXYPN + aoLightValueScratchXYZPNN) / 4.0F;
			f4 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNN + f7 + aoLightValueScratchYZNN) / 4.0F;
			brightnessTopLeft = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXYNN, aoBrightnessYZNP, i1);
			brightnessTopRight = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXYPN, i1);
			brightnessBottomRight = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYPN, aoBrightnessXYZPNN, i1);
			brightnessBottomLeft = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNN, aoBrightnessYZNN, i1);

			if (flag1) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147808_5_ * 0.5F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147808_6_
						* 0.5F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147808_7_ * 0.5F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.5F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.5F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.5F;
			}

			colorRedTopLeft *= f3;
			colorGreenTopLeft *= f3;
			colorBlueTopLeft *= f3;
			colorRedBottomLeft *= f4;
			colorGreenBottomLeft *= f4;
			colorBlueBottomLeft *= f4;
			colorRedBottomRight *= f5;
			colorGreenBottomRight *= f5;
			colorBlueBottomRight *= f5;
			colorRedTopRight *= f6;
			colorGreenTopRight *= f6;
			colorBlueTopRight *= f6;
			renderFaceYNeg(p_147808_1_, p_147808_2_, p_147808_3_, p_147808_4_,
					this.getBlockIcon(p_147808_1_, blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 0));
			flag = true;
		}

		if (renderAllFaces
				|| p_147808_1_.shouldSideBeRendered(blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_, 1)) {
			if (renderMaxY >= 1.0D) {
				++p_147808_3_;
			}

			aoBrightnessXYNP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ - 1, p_147808_3_,
					p_147808_4_);
			aoBrightnessXYPP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ + 1, p_147808_3_,
					p_147808_4_);
			aoBrightnessYZPN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_,
					p_147808_4_ - 1);
			aoBrightnessYZPP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_,
					p_147808_4_ + 1);
			aoLightValueScratchXYNP = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXYPP = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchYZPN = blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchYZPP = blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1)
					.getAmbientOcclusionLightValue();
			flag2 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_).getCanBlockGrass();
			flag3 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_).getCanBlockGrass();
			flag4 = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1).getCanBlockGrass();

			if (!flag5 && !flag3) {
				aoLightValueScratchXYZNPN = aoLightValueScratchXYNP;
				aoBrightnessXYZNPN = aoBrightnessXYNP;
			} else {
				aoLightValueScratchXYZNPN = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNPN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ - 1, p_147808_3_,
						p_147808_4_ - 1);
			}

			if (!flag5 && !flag2) {
				aoLightValueScratchXYZPPN = aoLightValueScratchXYPP;
				aoBrightnessXYZPPN = aoBrightnessXYPP;
			} else {
				aoLightValueScratchXYZPPN = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPPN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ + 1, p_147808_3_,
						p_147808_4_ - 1);
			}

			if (!flag4 && !flag3) {
				aoLightValueScratchXYZNPP = aoLightValueScratchXYNP;
				aoBrightnessXYZNPP = aoBrightnessXYNP;
			} else {
				aoLightValueScratchXYZNPP = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNPP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ - 1, p_147808_3_,
						p_147808_4_ + 1);
			}

			if (!flag4 && !flag2) {
				aoLightValueScratchXYZPPP = aoLightValueScratchXYPP;
				aoBrightnessXYZPPP = aoBrightnessXYPP;
			} else {
				aoLightValueScratchXYZPPP = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPPP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ + 1, p_147808_3_,
						p_147808_4_ + 1);
			}

			if (renderMaxY >= 1.0D) {
				--p_147808_3_;
			}

			i1 = l;

			if (renderMaxY >= 1.0D || !blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_).isOpaqueCube()) {
				i1 = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ + 1, p_147808_4_);
			}

			f7 = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_).getAmbientOcclusionLightValue();
			f6 = (aoLightValueScratchXYZNPP + aoLightValueScratchXYNP + aoLightValueScratchYZPP + f7) / 4.0F;
			f3 = (aoLightValueScratchYZPP + f7 + aoLightValueScratchXYZPPP + aoLightValueScratchXYPP) / 4.0F;
			f4 = (f7 + aoLightValueScratchYZPN + aoLightValueScratchXYPP + aoLightValueScratchXYZPPN) / 4.0F;
			f5 = (aoLightValueScratchXYNP + aoLightValueScratchXYZNPN + f7 + aoLightValueScratchYZPN) / 4.0F;
			brightnessTopRight = getAoBrightness(aoBrightnessXYZNPP, aoBrightnessXYNP, aoBrightnessYZPP, i1);
			brightnessTopLeft = getAoBrightness(aoBrightnessYZPP, aoBrightnessXYZPPP, aoBrightnessXYPP, i1);
			brightnessBottomLeft = getAoBrightness(aoBrightnessYZPN, aoBrightnessXYPP, aoBrightnessXYZPPN, i1);
			brightnessBottomRight = getAoBrightness(aoBrightnessXYNP, aoBrightnessXYZNPN, aoBrightnessYZPN, i1);
			colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147808_5_;
			colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147808_6_;
			colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147808_7_;
			colorRedTopLeft *= f3;
			colorGreenTopLeft *= f3;
			colorBlueTopLeft *= f3;
			colorRedBottomLeft *= f4;
			colorGreenBottomLeft *= f4;
			colorBlueBottomLeft *= f4;
			colorRedBottomRight *= f5;
			colorGreenBottomRight *= f5;
			colorBlueBottomRight *= f5;
			colorRedTopRight *= f6;
			colorGreenTopRight *= f6;
			colorBlueTopRight *= f6;
			renderFaceYPos(p_147808_1_, p_147808_2_, p_147808_3_, p_147808_4_,
					this.getBlockIcon(p_147808_1_, blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 1));
			flag = true;
		}

		float f8;
		float f9;
		float f10;
		float f11;
		int j1;
		int k1;
		int l1;
		int i2;
		IIcon iicon;

		if (renderAllFaces
				|| p_147808_1_.shouldSideBeRendered(blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ - 1, 2)) {
			if (renderMinZ <= 0.0D) {
				--p_147808_4_;
			}

			aoLightValueScratchXZNN = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchYZNN = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchYZPN = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXZPN = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_)
					.getAmbientOcclusionLightValue();
			aoBrightnessXZNN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ - 1, p_147808_3_,
					p_147808_4_);
			aoBrightnessYZNN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ - 1,
					p_147808_4_);
			aoBrightnessYZPN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ + 1,
					p_147808_4_);
			aoBrightnessXZPN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ + 1, p_147808_3_,
					p_147808_4_);
			flag2 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1).getCanBlockGrass();
			flag3 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1).getCanBlockGrass();
			flag4 = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1).getCanBlockGrass();

			if (!flag3 && !flag5) {
				aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
				aoBrightnessXYZNNN = aoBrightnessXZNN;
			} else {
				aoLightValueScratchXYZNNN = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNNN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ - 1,
						p_147808_3_ - 1, p_147808_4_);
			}

			if (!flag3 && !flag4) {
				aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
				aoBrightnessXYZNPN = aoBrightnessXZNN;
			} else {
				aoLightValueScratchXYZNPN = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNPN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ - 1,
						p_147808_3_ + 1, p_147808_4_);
			}

			if (!flag2 && !flag5) {
				aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
				aoBrightnessXYZPNN = aoBrightnessXZPN;
			} else {
				aoLightValueScratchXYZPNN = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPNN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ + 1,
						p_147808_3_ - 1, p_147808_4_);
			}

			if (!flag2 && !flag4) {
				aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
				aoBrightnessXYZPPN = aoBrightnessXZPN;
			} else {
				aoLightValueScratchXYZPPN = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPPN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ + 1,
						p_147808_3_ + 1, p_147808_4_);
			}

			if (renderMinZ <= 0.0D) {
				++p_147808_4_;
			}

			i1 = l;

			if (renderMinZ <= 0.0D || !blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1).isOpaqueCube()) {
				i1 = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ - 1);
			}

			f7 = blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1).getAmbientOcclusionLightValue();
			f8 = (aoLightValueScratchXZNN + aoLightValueScratchXYZNPN + f7 + aoLightValueScratchYZPN) / 4.0F;
			f9 = (f7 + aoLightValueScratchYZPN + aoLightValueScratchXZPN + aoLightValueScratchXYZPPN) / 4.0F;
			f10 = (aoLightValueScratchYZNN + f7 + aoLightValueScratchXYZPNN + aoLightValueScratchXZPN) / 4.0F;
			f11 = (aoLightValueScratchXYZNNN + aoLightValueScratchXZNN + aoLightValueScratchYZNN + f7) / 4.0F;
			f3 = (float) (f8 * renderMaxY * (1.0D - renderMinX) + f9 * renderMaxY * renderMinX
					+ f10 * (1.0D - renderMaxY) * renderMinX + f11 * (1.0D - renderMaxY) * (1.0D - renderMinX));
			f4 = (float) (f8 * renderMaxY * (1.0D - renderMaxX) + f9 * renderMaxY * renderMaxX
					+ f10 * (1.0D - renderMaxY) * renderMaxX + f11 * (1.0D - renderMaxY) * (1.0D - renderMaxX));
			f5 = (float) (f8 * renderMinY * (1.0D - renderMaxX) + f9 * renderMinY * renderMaxX
					+ f10 * (1.0D - renderMinY) * renderMaxX + f11 * (1.0D - renderMinY) * (1.0D - renderMaxX));
			f6 = (float) (f8 * renderMinY * (1.0D - renderMinX) + f9 * renderMinY * renderMinX
					+ f10 * (1.0D - renderMinY) * renderMinX + f11 * (1.0D - renderMinY) * (1.0D - renderMinX));
			j1 = getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN, aoBrightnessYZPN, i1);
			k1 = getAoBrightness(aoBrightnessYZPN, aoBrightnessXZPN, aoBrightnessXYZPPN, i1);
			l1 = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYZPNN, aoBrightnessXZPN, i1);
			i2 = getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXZNN, aoBrightnessYZNN, i1);
			brightnessTopLeft = mixAoBrightness(j1, k1, l1, i2, renderMaxY * (1.0D - renderMinX),
					renderMaxY * renderMinX, (1.0D - renderMaxY) * renderMinX,
					(1.0D - renderMaxY) * (1.0D - renderMinX));
			brightnessBottomLeft = mixAoBrightness(j1, k1, l1, i2, renderMaxY * (1.0D - renderMaxX),
					renderMaxY * renderMaxX, (1.0D - renderMaxY) * renderMaxX,
					(1.0D - renderMaxY) * (1.0D - renderMaxX));
			brightnessBottomRight = mixAoBrightness(j1, k1, l1, i2, renderMinY * (1.0D - renderMaxX),
					renderMinY * renderMaxX, (1.0D - renderMinY) * renderMaxX,
					(1.0D - renderMinY) * (1.0D - renderMaxX));
			brightnessTopRight = mixAoBrightness(j1, k1, l1, i2, renderMinY * (1.0D - renderMinX),
					renderMinY * renderMinX, (1.0D - renderMinY) * renderMinX,
					(1.0D - renderMinY) * (1.0D - renderMinX));

			if (flag1) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147808_5_ * 0.8F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147808_6_
						* 0.8F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147808_7_ * 0.8F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.8F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.8F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.8F;
			}

			colorRedTopLeft *= f3;
			colorGreenTopLeft *= f3;
			colorBlueTopLeft *= f3;
			colorRedBottomLeft *= f4;
			colorGreenBottomLeft *= f4;
			colorBlueBottomLeft *= f4;
			colorRedBottomRight *= f5;
			colorGreenBottomRight *= f5;
			colorBlueBottomRight *= f5;
			colorRedTopRight *= f6;
			colorGreenTopRight *= f6;
			colorBlueTopRight *= f6;
			iicon = this.getBlockIcon(p_147808_1_, blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 2);
			renderFaceZNeg(p_147808_1_, p_147808_2_, p_147808_3_, p_147808_4_, iicon);

			if (fancyGrass && iicon.getIconName().equals("grass_side") && !hasOverrideBlockTexture()) {
				colorRedTopLeft *= p_147808_5_;
				colorRedBottomLeft *= p_147808_5_;
				colorRedBottomRight *= p_147808_5_;
				colorRedTopRight *= p_147808_5_;
				colorGreenTopLeft *= p_147808_6_;
				colorGreenBottomLeft *= p_147808_6_;
				colorGreenBottomRight *= p_147808_6_;
				colorGreenTopRight *= p_147808_6_;
				colorBlueTopLeft *= p_147808_7_;
				colorBlueBottomLeft *= p_147808_7_;
				colorBlueBottomRight *= p_147808_7_;
				colorBlueTopRight *= p_147808_7_;
				renderFaceZNeg(p_147808_1_, p_147808_2_, p_147808_3_, p_147808_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderAllFaces
				|| p_147808_1_.shouldSideBeRendered(blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ + 1, 3)) {
			if (renderMaxZ >= 1.0D) {
				++p_147808_4_;
			}

			aoLightValueScratchXZNP = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXZPP = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchYZNP = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchYZPP = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_)
					.getAmbientOcclusionLightValue();
			aoBrightnessXZNP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ - 1, p_147808_3_,
					p_147808_4_);
			aoBrightnessXZPP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ + 1, p_147808_3_,
					p_147808_4_);
			aoBrightnessYZNP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ - 1,
					p_147808_4_);
			aoBrightnessYZPP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ + 1,
					p_147808_4_);
			flag2 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1).getCanBlockGrass();
			flag3 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1).getCanBlockGrass();
			flag4 = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1).getCanBlockGrass();

			if (!flag3 && !flag5) {
				aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
				aoBrightnessXYZNNP = aoBrightnessXZNP;
			} else {
				aoLightValueScratchXYZNNP = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNNP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ - 1,
						p_147808_3_ - 1, p_147808_4_);
			}

			if (!flag3 && !flag4) {
				aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
				aoBrightnessXYZNPP = aoBrightnessXZNP;
			} else {
				aoLightValueScratchXYZNPP = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNPP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ - 1,
						p_147808_3_ + 1, p_147808_4_);
			}

			if (!flag2 && !flag5) {
				aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
				aoBrightnessXYZPNP = aoBrightnessXZPP;
			} else {
				aoLightValueScratchXYZPNP = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPNP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ + 1,
						p_147808_3_ - 1, p_147808_4_);
			}

			if (!flag2 && !flag4) {
				aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
				aoBrightnessXYZPPP = aoBrightnessXZPP;
			} else {
				aoLightValueScratchXYZPPP = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPPP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ + 1,
						p_147808_3_ + 1, p_147808_4_);
			}

			if (renderMaxZ >= 1.0D) {
				--p_147808_4_;
			}

			i1 = l;

			if (renderMaxZ >= 1.0D || !blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1).isOpaqueCube()) {
				i1 = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_, p_147808_4_ + 1);
			}

			f7 = blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1).getAmbientOcclusionLightValue();
			f8 = (aoLightValueScratchXZNP + aoLightValueScratchXYZNPP + f7 + aoLightValueScratchYZPP) / 4.0F;
			f9 = (f7 + aoLightValueScratchYZPP + aoLightValueScratchXZPP + aoLightValueScratchXYZPPP) / 4.0F;
			f10 = (aoLightValueScratchYZNP + f7 + aoLightValueScratchXYZPNP + aoLightValueScratchXZPP) / 4.0F;
			f11 = (aoLightValueScratchXYZNNP + aoLightValueScratchXZNP + aoLightValueScratchYZNP + f7) / 4.0F;
			f3 = (float) (f8 * renderMaxY * (1.0D - renderMinX) + f9 * renderMaxY * renderMinX
					+ f10 * (1.0D - renderMaxY) * renderMinX + f11 * (1.0D - renderMaxY) * (1.0D - renderMinX));
			f4 = (float) (f8 * renderMinY * (1.0D - renderMinX) + f9 * renderMinY * renderMinX
					+ f10 * (1.0D - renderMinY) * renderMinX + f11 * (1.0D - renderMinY) * (1.0D - renderMinX));
			f5 = (float) (f8 * renderMinY * (1.0D - renderMaxX) + f9 * renderMinY * renderMaxX
					+ f10 * (1.0D - renderMinY) * renderMaxX + f11 * (1.0D - renderMinY) * (1.0D - renderMaxX));
			f6 = (float) (f8 * renderMaxY * (1.0D - renderMaxX) + f9 * renderMaxY * renderMaxX
					+ f10 * (1.0D - renderMaxY) * renderMaxX + f11 * (1.0D - renderMaxY) * (1.0D - renderMaxX));
			j1 = getAoBrightness(aoBrightnessXZNP, aoBrightnessXYZNPP, aoBrightnessYZPP, i1);
			k1 = getAoBrightness(aoBrightnessYZPP, aoBrightnessXZPP, aoBrightnessXYZPPP, i1);
			l1 = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXZPP, i1);
			i2 = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXZNP, aoBrightnessYZNP, i1);
			brightnessTopLeft = mixAoBrightness(j1, i2, l1, k1, renderMaxY * (1.0D - renderMinX),
					(1.0D - renderMaxY) * (1.0D - renderMinX), (1.0D - renderMaxY) * renderMinX,
					renderMaxY * renderMinX);
			brightnessBottomLeft = mixAoBrightness(j1, i2, l1, k1, renderMinY * (1.0D - renderMinX),
					(1.0D - renderMinY) * (1.0D - renderMinX), (1.0D - renderMinY) * renderMinX,
					renderMinY * renderMinX);
			brightnessBottomRight = mixAoBrightness(j1, i2, l1, k1, renderMinY * (1.0D - renderMaxX),
					(1.0D - renderMinY) * (1.0D - renderMaxX), (1.0D - renderMinY) * renderMaxX,
					renderMinY * renderMaxX);
			brightnessTopRight = mixAoBrightness(j1, i2, l1, k1, renderMaxY * (1.0D - renderMaxX),
					(1.0D - renderMaxY) * (1.0D - renderMaxX), (1.0D - renderMaxY) * renderMaxX,
					renderMaxY * renderMaxX);

			if (flag1) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147808_5_ * 0.8F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147808_6_
						* 0.8F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147808_7_ * 0.8F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.8F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.8F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.8F;
			}

			colorRedTopLeft *= f3;
			colorGreenTopLeft *= f3;
			colorBlueTopLeft *= f3;
			colorRedBottomLeft *= f4;
			colorGreenBottomLeft *= f4;
			colorBlueBottomLeft *= f4;
			colorRedBottomRight *= f5;
			colorGreenBottomRight *= f5;
			colorBlueBottomRight *= f5;
			colorRedTopRight *= f6;
			colorGreenTopRight *= f6;
			colorBlueTopRight *= f6;
			iicon = this.getBlockIcon(p_147808_1_, blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 3);
			renderFaceZPos(p_147808_1_, p_147808_2_, p_147808_3_, p_147808_4_, iicon);

			if (fancyGrass && iicon.getIconName().equals("grass_side") && !hasOverrideBlockTexture()) {
				colorRedTopLeft *= p_147808_5_;
				colorRedBottomLeft *= p_147808_5_;
				colorRedBottomRight *= p_147808_5_;
				colorRedTopRight *= p_147808_5_;
				colorGreenTopLeft *= p_147808_6_;
				colorGreenBottomLeft *= p_147808_6_;
				colorGreenBottomRight *= p_147808_6_;
				colorGreenTopRight *= p_147808_6_;
				colorBlueTopLeft *= p_147808_7_;
				colorBlueBottomLeft *= p_147808_7_;
				colorBlueBottomRight *= p_147808_7_;
				colorBlueTopRight *= p_147808_7_;
				renderFaceZPos(p_147808_1_, p_147808_2_, p_147808_3_, p_147808_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderAllFaces
				|| p_147808_1_.shouldSideBeRendered(blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_, 4)) {
			if (renderMinX <= 0.0D) {
				--p_147808_2_;
			}

			aoLightValueScratchXYNN = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXZNN = blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXZNP = blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXYNP = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_)
					.getAmbientOcclusionLightValue();
			aoBrightnessXYNN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ - 1,
					p_147808_4_);
			aoBrightnessXZNN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_,
					p_147808_4_ - 1);
			aoBrightnessXZNP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_,
					p_147808_4_ + 1);
			aoBrightnessXYNP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ + 1,
					p_147808_4_);
			flag2 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ + 1, p_147808_4_).getCanBlockGrass();
			flag3 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_ - 1, p_147808_4_).getCanBlockGrass();
			flag4 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ - 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_ + 1).getCanBlockGrass();

			if (!flag4 && !flag3) {
				aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
				aoBrightnessXYZNNN = aoBrightnessXZNN;
			} else {
				aoLightValueScratchXYZNNN = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNNN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ - 1,
						p_147808_4_ - 1);
			}

			if (!flag5 && !flag3) {
				aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
				aoBrightnessXYZNNP = aoBrightnessXZNP;
			} else {
				aoLightValueScratchXYZNNP = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNNP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ - 1,
						p_147808_4_ + 1);
			}

			if (!flag4 && !flag2) {
				aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
				aoBrightnessXYZNPN = aoBrightnessXZNN;
			} else {
				aoLightValueScratchXYZNPN = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNPN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ + 1,
						p_147808_4_ - 1);
			}

			if (!flag5 && !flag2) {
				aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
				aoBrightnessXYZNPP = aoBrightnessXZNP;
			} else {
				aoLightValueScratchXYZNPP = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZNPP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ + 1,
						p_147808_4_ + 1);
			}

			if (renderMinX <= 0.0D) {
				++p_147808_2_;
			}

			i1 = l;

			if (renderMinX <= 0.0D || !blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_).isOpaqueCube()) {
				i1 = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ - 1, p_147808_3_, p_147808_4_);
			}

			f7 = blockAccess.getBlock(p_147808_2_ - 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			f8 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNP + f7 + aoLightValueScratchXZNP) / 4.0F;
			f9 = (f7 + aoLightValueScratchXZNP + aoLightValueScratchXYNP + aoLightValueScratchXYZNPP) / 4.0F;
			f10 = (aoLightValueScratchXZNN + f7 + aoLightValueScratchXYZNPN + aoLightValueScratchXYNP) / 4.0F;
			f11 = (aoLightValueScratchXYZNNN + aoLightValueScratchXYNN + aoLightValueScratchXZNN + f7) / 4.0F;
			f3 = (float) (f9 * renderMaxY * renderMaxZ + f10 * renderMaxY * (1.0D - renderMaxZ)
					+ f11 * (1.0D - renderMaxY) * (1.0D - renderMaxZ) + f8 * (1.0D - renderMaxY) * renderMaxZ);
			f4 = (float) (f9 * renderMaxY * renderMinZ + f10 * renderMaxY * (1.0D - renderMinZ)
					+ f11 * (1.0D - renderMaxY) * (1.0D - renderMinZ) + f8 * (1.0D - renderMaxY) * renderMinZ);
			f5 = (float) (f9 * renderMinY * renderMinZ + f10 * renderMinY * (1.0D - renderMinZ)
					+ f11 * (1.0D - renderMinY) * (1.0D - renderMinZ) + f8 * (1.0D - renderMinY) * renderMinZ);
			f6 = (float) (f9 * renderMinY * renderMaxZ + f10 * renderMinY * (1.0D - renderMaxZ)
					+ f11 * (1.0D - renderMinY) * (1.0D - renderMaxZ) + f8 * (1.0D - renderMinY) * renderMaxZ);
			j1 = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNP, aoBrightnessXZNP, i1);
			k1 = getAoBrightness(aoBrightnessXZNP, aoBrightnessXYNP, aoBrightnessXYZNPP, i1);
			l1 = getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN, aoBrightnessXYNP, i1);
			i2 = getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXYNN, aoBrightnessXZNN, i1);
			brightnessTopLeft = mixAoBrightness(k1, l1, i2, j1, renderMaxY * renderMaxZ,
					renderMaxY * (1.0D - renderMaxZ), (1.0D - renderMaxY) * (1.0D - renderMaxZ),
					(1.0D - renderMaxY) * renderMaxZ);
			brightnessBottomLeft = mixAoBrightness(k1, l1, i2, j1, renderMaxY * renderMinZ,
					renderMaxY * (1.0D - renderMinZ), (1.0D - renderMaxY) * (1.0D - renderMinZ),
					(1.0D - renderMaxY) * renderMinZ);
			brightnessBottomRight = mixAoBrightness(k1, l1, i2, j1, renderMinY * renderMinZ,
					renderMinY * (1.0D - renderMinZ), (1.0D - renderMinY) * (1.0D - renderMinZ),
					(1.0D - renderMinY) * renderMinZ);
			brightnessTopRight = mixAoBrightness(k1, l1, i2, j1, renderMinY * renderMaxZ,
					renderMinY * (1.0D - renderMaxZ), (1.0D - renderMinY) * (1.0D - renderMaxZ),
					(1.0D - renderMinY) * renderMaxZ);

			if (flag1) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147808_5_ * 0.6F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147808_6_
						* 0.6F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147808_7_ * 0.6F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.6F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.6F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.6F;
			}

			colorRedTopLeft *= f3;
			colorGreenTopLeft *= f3;
			colorBlueTopLeft *= f3;
			colorRedBottomLeft *= f4;
			colorGreenBottomLeft *= f4;
			colorBlueBottomLeft *= f4;
			colorRedBottomRight *= f5;
			colorGreenBottomRight *= f5;
			colorBlueBottomRight *= f5;
			colorRedTopRight *= f6;
			colorGreenTopRight *= f6;
			colorBlueTopRight *= f6;
			iicon = this.getBlockIcon(p_147808_1_, blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 4);
			renderFaceXNeg(p_147808_1_, p_147808_2_, p_147808_3_, p_147808_4_, iicon);

			if (fancyGrass && iicon.getIconName().equals("grass_side") && !hasOverrideBlockTexture()) {
				colorRedTopLeft *= p_147808_5_;
				colorRedBottomLeft *= p_147808_5_;
				colorRedBottomRight *= p_147808_5_;
				colorRedTopRight *= p_147808_5_;
				colorGreenTopLeft *= p_147808_6_;
				colorGreenBottomLeft *= p_147808_6_;
				colorGreenBottomRight *= p_147808_6_;
				colorGreenTopRight *= p_147808_6_;
				colorBlueTopLeft *= p_147808_7_;
				colorBlueBottomLeft *= p_147808_7_;
				colorBlueBottomRight *= p_147808_7_;
				colorBlueTopRight *= p_147808_7_;
				renderFaceXNeg(p_147808_1_, p_147808_2_, p_147808_3_, p_147808_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderAllFaces
				|| p_147808_1_.shouldSideBeRendered(blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_, 5)) {
			if (renderMaxX >= 1.0D) {
				++p_147808_2_;
			}

			aoLightValueScratchXYPN = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXZPN = blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ - 1)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXZPP = blockAccess.getBlock(p_147808_2_, p_147808_3_, p_147808_4_ + 1)
					.getAmbientOcclusionLightValue();
			aoLightValueScratchXYPP = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_)
					.getAmbientOcclusionLightValue();
			aoBrightnessXYPN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ - 1,
					p_147808_4_);
			aoBrightnessXZPN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_,
					p_147808_4_ - 1);
			aoBrightnessXZPP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_,
					p_147808_4_ + 1);
			aoBrightnessXYPP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ + 1,
					p_147808_4_);
			flag2 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ + 1, p_147808_4_).getCanBlockGrass();
			flag3 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_ - 1, p_147808_4_).getCanBlockGrass();
			flag4 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ + 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_ - 1).getCanBlockGrass();

			if (!flag3 && !flag5) {
				aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
				aoBrightnessXYZPNN = aoBrightnessXZPN;
			} else {
				aoLightValueScratchXYZPNN = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ - 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPNN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ - 1,
						p_147808_4_ - 1);
			}

			if (!flag3 && !flag4) {
				aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
				aoBrightnessXYZPNP = aoBrightnessXZPP;
			} else {
				aoLightValueScratchXYZPNP = blockAccess.getBlock(p_147808_2_, p_147808_3_ - 1, p_147808_4_ + 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPNP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ - 1,
						p_147808_4_ + 1);
			}

			if (!flag2 && !flag5) {
				aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
				aoBrightnessXYZPPN = aoBrightnessXZPN;
			} else {
				aoLightValueScratchXYZPPN = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ - 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPPN = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ + 1,
						p_147808_4_ - 1);
			}

			if (!flag2 && !flag4) {
				aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
				aoBrightnessXYZPPP = aoBrightnessXZPP;
			} else {
				aoLightValueScratchXYZPPP = blockAccess.getBlock(p_147808_2_, p_147808_3_ + 1, p_147808_4_ + 1)
						.getAmbientOcclusionLightValue();
				aoBrightnessXYZPPP = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_, p_147808_3_ + 1,
						p_147808_4_ + 1);
			}

			if (renderMaxX >= 1.0D) {
				--p_147808_2_;
			}

			i1 = l;

			if (renderMaxX >= 1.0D || !blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_).isOpaqueCube()) {
				i1 = p_147808_1_.getMixedBrightnessForBlock(blockAccess, p_147808_2_ + 1, p_147808_3_, p_147808_4_);
			}

			f7 = blockAccess.getBlock(p_147808_2_ + 1, p_147808_3_, p_147808_4_).getAmbientOcclusionLightValue();
			f8 = (aoLightValueScratchXYPN + aoLightValueScratchXYZPNP + f7 + aoLightValueScratchXZPP) / 4.0F;
			f9 = (aoLightValueScratchXYZPNN + aoLightValueScratchXYPN + aoLightValueScratchXZPN + f7) / 4.0F;
			f10 = (aoLightValueScratchXZPN + f7 + aoLightValueScratchXYZPPN + aoLightValueScratchXYPP) / 4.0F;
			f11 = (f7 + aoLightValueScratchXZPP + aoLightValueScratchXYPP + aoLightValueScratchXYZPPP) / 4.0F;
			f3 = (float) (f8 * (1.0D - renderMinY) * renderMaxZ + f9 * (1.0D - renderMinY) * (1.0D - renderMaxZ)
					+ f10 * renderMinY * (1.0D - renderMaxZ) + f11 * renderMinY * renderMaxZ);
			f4 = (float) (f8 * (1.0D - renderMinY) * renderMinZ + f9 * (1.0D - renderMinY) * (1.0D - renderMinZ)
					+ f10 * renderMinY * (1.0D - renderMinZ) + f11 * renderMinY * renderMinZ);
			f5 = (float) (f8 * (1.0D - renderMaxY) * renderMinZ + f9 * (1.0D - renderMaxY) * (1.0D - renderMinZ)
					+ f10 * renderMaxY * (1.0D - renderMinZ) + f11 * renderMaxY * renderMinZ);
			f6 = (float) (f8 * (1.0D - renderMaxY) * renderMaxZ + f9 * (1.0D - renderMaxY) * (1.0D - renderMaxZ)
					+ f10 * renderMaxY * (1.0D - renderMaxZ) + f11 * renderMaxY * renderMaxZ);
			j1 = getAoBrightness(aoBrightnessXYPN, aoBrightnessXYZPNP, aoBrightnessXZPP, i1);
			k1 = getAoBrightness(aoBrightnessXZPP, aoBrightnessXYPP, aoBrightnessXYZPPP, i1);
			l1 = getAoBrightness(aoBrightnessXZPN, aoBrightnessXYZPPN, aoBrightnessXYPP, i1);
			i2 = getAoBrightness(aoBrightnessXYZPNN, aoBrightnessXYPN, aoBrightnessXZPN, i1);
			brightnessTopLeft = mixAoBrightness(j1, i2, l1, k1, (1.0D - renderMinY) * renderMaxZ,
					(1.0D - renderMinY) * (1.0D - renderMaxZ), renderMinY * (1.0D - renderMaxZ),
					renderMinY * renderMaxZ);
			brightnessBottomLeft = mixAoBrightness(j1, i2, l1, k1, (1.0D - renderMinY) * renderMinZ,
					(1.0D - renderMinY) * (1.0D - renderMinZ), renderMinY * (1.0D - renderMinZ),
					renderMinY * renderMinZ);
			brightnessBottomRight = mixAoBrightness(j1, i2, l1, k1, (1.0D - renderMaxY) * renderMinZ,
					(1.0D - renderMaxY) * (1.0D - renderMinZ), renderMaxY * (1.0D - renderMinZ),
					renderMaxY * renderMinZ);
			brightnessTopRight = mixAoBrightness(j1, i2, l1, k1, (1.0D - renderMaxY) * renderMaxZ,
					(1.0D - renderMaxY) * (1.0D - renderMaxZ), renderMaxY * (1.0D - renderMaxZ),
					renderMaxY * renderMaxZ);

			if (flag1) {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = p_147808_5_ * 0.6F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = p_147808_6_
						* 0.6F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = p_147808_7_ * 0.6F;
			} else {
				colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.6F;
				colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.6F;
				colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.6F;
			}

			colorRedTopLeft *= f3;
			colorGreenTopLeft *= f3;
			colorBlueTopLeft *= f3;
			colorRedBottomLeft *= f4;
			colorGreenBottomLeft *= f4;
			colorBlueBottomLeft *= f4;
			colorRedBottomRight *= f5;
			colorGreenBottomRight *= f5;
			colorBlueBottomRight *= f5;
			colorRedTopRight *= f6;
			colorGreenTopRight *= f6;
			colorBlueTopRight *= f6;
			iicon = this.getBlockIcon(p_147808_1_, blockAccess, p_147808_2_, p_147808_3_, p_147808_4_, 5);
			renderFaceXPos(p_147808_1_, p_147808_2_, p_147808_3_, p_147808_4_, iicon);

			if (fancyGrass && iicon.getIconName().equals("grass_side") && !hasOverrideBlockTexture()) {
				colorRedTopLeft *= p_147808_5_;
				colorRedBottomLeft *= p_147808_5_;
				colorRedBottomRight *= p_147808_5_;
				colorRedTopRight *= p_147808_5_;
				colorGreenTopLeft *= p_147808_6_;
				colorGreenBottomLeft *= p_147808_6_;
				colorGreenBottomRight *= p_147808_6_;
				colorGreenTopRight *= p_147808_6_;
				colorBlueTopLeft *= p_147808_7_;
				colorBlueBottomLeft *= p_147808_7_;
				colorBlueBottomRight *= p_147808_7_;
				colorBlueTopRight *= p_147808_7_;
				renderFaceXPos(p_147808_1_, p_147808_2_, p_147808_3_, p_147808_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		enableAO = false;
		return flag;
	}

	public int getAoBrightness(int p_147778_1_, int p_147778_2_, int p_147778_3_, int p_147778_4_) {
		if (p_147778_1_ == 0) {
			p_147778_1_ = p_147778_4_;
		}

		if (p_147778_2_ == 0) {
			p_147778_2_ = p_147778_4_;
		}

		if (p_147778_3_ == 0) {
			p_147778_3_ = p_147778_4_;
		}

		return p_147778_1_ + p_147778_2_ + p_147778_3_ + p_147778_4_ >> 2 & 16711935;
	}

	public int mixAoBrightness(int p_147727_1_, int p_147727_2_, int p_147727_3_, int p_147727_4_, double p_147727_5_,
			double p_147727_7_, double p_147727_9_, double p_147727_11_) {
		int i1 = (int) ((p_147727_1_ >> 16 & 255) * p_147727_5_ + (p_147727_2_ >> 16 & 255) * p_147727_7_
				+ (p_147727_3_ >> 16 & 255) * p_147727_9_ + (p_147727_4_ >> 16 & 255) * p_147727_11_) & 255;
		int j1 = (int) ((p_147727_1_ & 255) * p_147727_5_ + (p_147727_2_ & 255) * p_147727_7_
				+ (p_147727_3_ & 255) * p_147727_9_ + (p_147727_4_ & 255) * p_147727_11_) & 255;
		return i1 << 16 | j1;
	}

	public boolean renderStandardBlockWithColorMultiplier(Block p_147736_1_, int p_147736_2_, int p_147736_3_,
			int p_147736_4_, float p_147736_5_, float p_147736_6_, float p_147736_7_) {
		enableAO = false;
		Tessellator tessellator = Tessellator.instance;
		boolean flag = false;
		float f3 = 0.5F;
		float f4 = 1.0F;
		float f5 = 0.8F;
		float f6 = 0.6F;
		float f7 = f4 * p_147736_5_;
		float f8 = f4 * p_147736_6_;
		float f9 = f4 * p_147736_7_;
		float f10 = f3;
		float f11 = f5;
		float f12 = f6;
		float f13 = f3;
		float f14 = f5;
		float f15 = f6;
		float f16 = f3;
		float f17 = f5;
		float f18 = f6;

		if (p_147736_1_ != Blocks.grass) {
			f10 = f3 * p_147736_5_;
			f11 = f5 * p_147736_5_;
			f12 = f6 * p_147736_5_;
			f13 = f3 * p_147736_6_;
			f14 = f5 * p_147736_6_;
			f15 = f6 * p_147736_6_;
			f16 = f3 * p_147736_7_;
			f17 = f5 * p_147736_7_;
			f18 = f6 * p_147736_7_;
		}

		int l = p_147736_1_.getMixedBrightnessForBlock(blockAccess, p_147736_2_, p_147736_3_, p_147736_4_);

		if (renderAllFaces
				|| p_147736_1_.shouldSideBeRendered(blockAccess, p_147736_2_, p_147736_3_ - 1, p_147736_4_, 0)) {
			tessellator.setBrightness(renderMinY > 0.0D ? l
					: p_147736_1_.getMixedBrightnessForBlock(blockAccess, p_147736_2_, p_147736_3_ - 1, p_147736_4_));
			tessellator.setColorOpaque_F(f10, f13, f16);
			renderFaceYNeg(p_147736_1_, p_147736_2_, p_147736_3_, p_147736_4_,
					this.getBlockIcon(p_147736_1_, blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 0));
			flag = true;
		}

		if (renderAllFaces
				|| p_147736_1_.shouldSideBeRendered(blockAccess, p_147736_2_, p_147736_3_ + 1, p_147736_4_, 1)) {
			tessellator.setBrightness(renderMaxY < 1.0D ? l
					: p_147736_1_.getMixedBrightnessForBlock(blockAccess, p_147736_2_, p_147736_3_ + 1, p_147736_4_));
			tessellator.setColorOpaque_F(f7, f8, f9);
			renderFaceYPos(p_147736_1_, p_147736_2_, p_147736_3_, p_147736_4_,
					this.getBlockIcon(p_147736_1_, blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 1));
			flag = true;
		}

		IIcon iicon;

		if (renderAllFaces
				|| p_147736_1_.shouldSideBeRendered(blockAccess, p_147736_2_, p_147736_3_, p_147736_4_ - 1, 2)) {
			tessellator.setBrightness(renderMinZ > 0.0D ? l
					: p_147736_1_.getMixedBrightnessForBlock(blockAccess, p_147736_2_, p_147736_3_, p_147736_4_ - 1));
			tessellator.setColorOpaque_F(f11, f14, f17);
			iicon = this.getBlockIcon(p_147736_1_, blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 2);
			renderFaceZNeg(p_147736_1_, p_147736_2_, p_147736_3_, p_147736_4_, iicon);

			if (fancyGrass && iicon.getIconName().equals("grass_side") && !hasOverrideBlockTexture()) {
				tessellator.setColorOpaque_F(f11 * p_147736_5_, f14 * p_147736_6_, f17 * p_147736_7_);
				renderFaceZNeg(p_147736_1_, p_147736_2_, p_147736_3_, p_147736_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderAllFaces
				|| p_147736_1_.shouldSideBeRendered(blockAccess, p_147736_2_, p_147736_3_, p_147736_4_ + 1, 3)) {
			tessellator.setBrightness(renderMaxZ < 1.0D ? l
					: p_147736_1_.getMixedBrightnessForBlock(blockAccess, p_147736_2_, p_147736_3_, p_147736_4_ + 1));
			tessellator.setColorOpaque_F(f11, f14, f17);
			iicon = this.getBlockIcon(p_147736_1_, blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 3);
			renderFaceZPos(p_147736_1_, p_147736_2_, p_147736_3_, p_147736_4_, iicon);

			if (fancyGrass && iicon.getIconName().equals("grass_side") && !hasOverrideBlockTexture()) {
				tessellator.setColorOpaque_F(f11 * p_147736_5_, f14 * p_147736_6_, f17 * p_147736_7_);
				renderFaceZPos(p_147736_1_, p_147736_2_, p_147736_3_, p_147736_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderAllFaces
				|| p_147736_1_.shouldSideBeRendered(blockAccess, p_147736_2_ - 1, p_147736_3_, p_147736_4_, 4)) {
			tessellator.setBrightness(renderMinX > 0.0D ? l
					: p_147736_1_.getMixedBrightnessForBlock(blockAccess, p_147736_2_ - 1, p_147736_3_, p_147736_4_));
			tessellator.setColorOpaque_F(f12, f15, f18);
			iicon = this.getBlockIcon(p_147736_1_, blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 4);
			renderFaceXNeg(p_147736_1_, p_147736_2_, p_147736_3_, p_147736_4_, iicon);

			if (fancyGrass && iicon.getIconName().equals("grass_side") && !hasOverrideBlockTexture()) {
				tessellator.setColorOpaque_F(f12 * p_147736_5_, f15 * p_147736_6_, f18 * p_147736_7_);
				renderFaceXNeg(p_147736_1_, p_147736_2_, p_147736_3_, p_147736_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		if (renderAllFaces
				|| p_147736_1_.shouldSideBeRendered(blockAccess, p_147736_2_ + 1, p_147736_3_, p_147736_4_, 5)) {
			tessellator.setBrightness(renderMaxX < 1.0D ? l
					: p_147736_1_.getMixedBrightnessForBlock(blockAccess, p_147736_2_ + 1, p_147736_3_, p_147736_4_));
			tessellator.setColorOpaque_F(f12, f15, f18);
			iicon = this.getBlockIcon(p_147736_1_, blockAccess, p_147736_2_, p_147736_3_, p_147736_4_, 5);
			renderFaceXPos(p_147736_1_, p_147736_2_, p_147736_3_, p_147736_4_, iicon);

			if (fancyGrass && iicon.getIconName().equals("grass_side") && !hasOverrideBlockTexture()) {
				tessellator.setColorOpaque_F(f12 * p_147736_5_, f15 * p_147736_6_, f18 * p_147736_7_);
				renderFaceXPos(p_147736_1_, p_147736_2_, p_147736_3_, p_147736_4_, BlockGrass.getIconSideOverlay());
			}

			flag = true;
		}

		return flag;
	}

	public boolean renderBlockCocoa(BlockCocoa p_147772_1_, int p_147772_2_, int p_147772_3_, int p_147772_4_) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(
				p_147772_1_.getMixedBrightnessForBlock(blockAccess, p_147772_2_, p_147772_3_, p_147772_4_));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		int l = blockAccess.getBlockMetadata(p_147772_2_, p_147772_3_, p_147772_4_);
		int i1 = BlockDirectional.getDirection(l);
		int j1 = BlockCocoa.func_149987_c(l);
		IIcon iicon = p_147772_1_.getCocoaIcon(j1);
		int k1 = 4 + j1 * 2;
		int l1 = 5 + j1 * 2;
		double d0 = 15.0D - k1;
		double d1 = 15.0D;
		double d2 = 4.0D;
		double d3 = 4.0D + l1;
		double d4 = iicon.getInterpolatedU(d0);
		double d5 = iicon.getInterpolatedU(d1);
		double d6 = iicon.getInterpolatedV(d2);
		double d7 = iicon.getInterpolatedV(d3);
		double d8 = 0.0D;
		double d9 = 0.0D;

		switch (i1) {
		case 0:
			d8 = 8.0D - k1 / 2;
			d9 = 15.0D - k1;
			break;
		case 1:
			d8 = 1.0D;
			d9 = 8.0D - k1 / 2;
			break;
		case 2:
			d8 = 8.0D - k1 / 2;
			d9 = 1.0D;
			break;
		case 3:
			d8 = 15.0D - k1;
			d9 = 8.0D - k1 / 2;
		}

		double d10 = p_147772_2_ + d8 / 16.0D;
		double d11 = p_147772_2_ + (d8 + k1) / 16.0D;
		double d12 = p_147772_3_ + (12.0D - l1) / 16.0D;
		double d13 = p_147772_3_ + 0.75D;
		double d14 = p_147772_4_ + d9 / 16.0D;
		double d15 = p_147772_4_ + (d9 + k1) / 16.0D;
		tessellator.addVertexWithUV(d10, d12, d14, d4, d7);
		tessellator.addVertexWithUV(d10, d12, d15, d5, d7);
		tessellator.addVertexWithUV(d10, d13, d15, d5, d6);
		tessellator.addVertexWithUV(d10, d13, d14, d4, d6);
		tessellator.addVertexWithUV(d11, d12, d15, d4, d7);
		tessellator.addVertexWithUV(d11, d12, d14, d5, d7);
		tessellator.addVertexWithUV(d11, d13, d14, d5, d6);
		tessellator.addVertexWithUV(d11, d13, d15, d4, d6);
		tessellator.addVertexWithUV(d11, d12, d14, d4, d7);
		tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
		tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
		tessellator.addVertexWithUV(d11, d13, d14, d4, d6);
		tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
		tessellator.addVertexWithUV(d11, d12, d15, d5, d7);
		tessellator.addVertexWithUV(d11, d13, d15, d5, d6);
		tessellator.addVertexWithUV(d10, d13, d15, d4, d6);
		int i2 = k1;

		if (j1 >= 2) {
			i2 = k1 - 1;
		}

		d4 = iicon.getMinU();
		d5 = iicon.getInterpolatedU(i2);
		d6 = iicon.getMinV();
		d7 = iicon.getInterpolatedV(i2);
		tessellator.addVertexWithUV(d10, d13, d15, d4, d7);
		tessellator.addVertexWithUV(d11, d13, d15, d5, d7);
		tessellator.addVertexWithUV(d11, d13, d14, d5, d6);
		tessellator.addVertexWithUV(d10, d13, d14, d4, d6);
		tessellator.addVertexWithUV(d10, d12, d14, d4, d6);
		tessellator.addVertexWithUV(d11, d12, d14, d5, d6);
		tessellator.addVertexWithUV(d11, d12, d15, d5, d7);
		tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
		d4 = iicon.getInterpolatedU(12.0D);
		d5 = iicon.getMaxU();
		d6 = iicon.getMinV();
		d7 = iicon.getInterpolatedV(4.0D);
		d8 = 8.0D;
		d9 = 0.0D;
		double d16;

		switch (i1) {
		case 0:
			d8 = 8.0D;
			d9 = 12.0D;
			d16 = d4;
			d4 = d5;
			d5 = d16;
			break;
		case 1:
			d8 = 0.0D;
			d9 = 8.0D;
			break;
		case 2:
			d8 = 8.0D;
			d9 = 0.0D;
			break;
		case 3:
			d8 = 12.0D;
			d9 = 8.0D;
			d16 = d4;
			d4 = d5;
			d5 = d16;
		}

		d10 = p_147772_2_ + d8 / 16.0D;
		d11 = p_147772_2_ + (d8 + 4.0D) / 16.0D;
		d12 = p_147772_3_ + 0.75D;
		d13 = p_147772_3_ + 1.0D;
		d14 = p_147772_4_ + d9 / 16.0D;
		d15 = p_147772_4_ + (d9 + 4.0D) / 16.0D;

		if (i1 != 2 && i1 != 0) {
			if (i1 == 1 || i1 == 3) {
				tessellator.addVertexWithUV(d11, d12, d14, d4, d7);
				tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
				tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
				tessellator.addVertexWithUV(d11, d13, d14, d4, d6);
				tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
				tessellator.addVertexWithUV(d11, d12, d14, d4, d7);
				tessellator.addVertexWithUV(d11, d13, d14, d4, d6);
				tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
			}
		} else {
			tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
			tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
			tessellator.addVertexWithUV(d10, d13, d15, d4, d6);
			tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
			tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
			tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
			tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
			tessellator.addVertexWithUV(d10, d13, d15, d4, d6);
		}

		return true;
	}

	public boolean renderBlockBeacon(BlockBeacon p_147797_1_, int p_147797_2_, int p_147797_3_, int p_147797_4_) {
		float f = 0.1875F;
		setOverrideBlockTexture(this.getBlockIcon(Blocks.glass));
		setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		renderStandardBlock(p_147797_1_, p_147797_2_, p_147797_3_, p_147797_4_);
		renderAllFaces = true;
		setOverrideBlockTexture(this.getBlockIcon(Blocks.obsidian));
		setRenderBounds(0.125D, 0.0062500000931322575D, 0.125D, 0.875D, f, 0.875D);
		renderStandardBlock(p_147797_1_, p_147797_2_, p_147797_3_, p_147797_4_);
		setOverrideBlockTexture(this.getBlockIcon(Blocks.beacon));
		setRenderBounds(0.1875D, f, 0.1875D, 0.8125D, 0.875D, 0.8125D);
		renderStandardBlock(p_147797_1_, p_147797_2_, p_147797_3_, p_147797_4_);
		renderAllFaces = false;
		clearOverrideBlockTexture();
		return true;
	}

	public boolean renderBlockCactus(Block p_147755_1_, int p_147755_2_, int p_147755_3_, int p_147755_4_) {
		int l = p_147755_1_.colorMultiplier(blockAccess, p_147755_2_, p_147755_3_, p_147755_4_);
		float f = (l >> 16 & 255) / 255.0F;
		float f1 = (l >> 8 & 255) / 255.0F;
		float f2 = (l & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		return renderBlockCactusImpl(p_147755_1_, p_147755_2_, p_147755_3_, p_147755_4_, f, f1, f2);
	}

	public boolean renderBlockCactusImpl(Block p_147754_1_, int p_147754_2_, int p_147754_3_, int p_147754_4_,
			float p_147754_5_, float p_147754_6_, float p_147754_7_) {
		Tessellator tessellator = Tessellator.instance;
		float f3 = 0.5F;
		float f4 = 1.0F;
		float f5 = 0.8F;
		float f6 = 0.6F;
		float f7 = f3 * p_147754_5_;
		float f8 = f4 * p_147754_5_;
		float f9 = f5 * p_147754_5_;
		float f10 = f6 * p_147754_5_;
		float f11 = f3 * p_147754_6_;
		float f12 = f4 * p_147754_6_;
		float f13 = f5 * p_147754_6_;
		float f14 = f6 * p_147754_6_;
		float f15 = f3 * p_147754_7_;
		float f16 = f4 * p_147754_7_;
		float f17 = f5 * p_147754_7_;
		float f18 = f6 * p_147754_7_;
		float f19 = 0.0625F;
		int l = p_147754_1_.getMixedBrightnessForBlock(blockAccess, p_147754_2_, p_147754_3_, p_147754_4_);

		if (renderAllFaces
				|| p_147754_1_.shouldSideBeRendered(blockAccess, p_147754_2_, p_147754_3_ - 1, p_147754_4_, 0)) {
			tessellator.setBrightness(renderMinY > 0.0D ? l
					: p_147754_1_.getMixedBrightnessForBlock(blockAccess, p_147754_2_, p_147754_3_ - 1, p_147754_4_));
			tessellator.setColorOpaque_F(f7, f11, f15);
			renderFaceYNeg(p_147754_1_, p_147754_2_, p_147754_3_, p_147754_4_,
					this.getBlockIcon(p_147754_1_, blockAccess, p_147754_2_, p_147754_3_, p_147754_4_, 0));
		}

		if (renderAllFaces
				|| p_147754_1_.shouldSideBeRendered(blockAccess, p_147754_2_, p_147754_3_ + 1, p_147754_4_, 1)) {
			tessellator.setBrightness(renderMaxY < 1.0D ? l
					: p_147754_1_.getMixedBrightnessForBlock(blockAccess, p_147754_2_, p_147754_3_ + 1, p_147754_4_));
			tessellator.setColorOpaque_F(f8, f12, f16);
			renderFaceYPos(p_147754_1_, p_147754_2_, p_147754_3_, p_147754_4_,
					this.getBlockIcon(p_147754_1_, blockAccess, p_147754_2_, p_147754_3_, p_147754_4_, 1));
		}

		tessellator.setBrightness(l);
		tessellator.setColorOpaque_F(f9, f13, f17);
		tessellator.addTranslation(0.0F, 0.0F, f19);
		renderFaceZNeg(p_147754_1_, p_147754_2_, p_147754_3_, p_147754_4_,
				this.getBlockIcon(p_147754_1_, blockAccess, p_147754_2_, p_147754_3_, p_147754_4_, 2));
		tessellator.addTranslation(0.0F, 0.0F, -f19);
		tessellator.addTranslation(0.0F, 0.0F, -f19);
		renderFaceZPos(p_147754_1_, p_147754_2_, p_147754_3_, p_147754_4_,
				this.getBlockIcon(p_147754_1_, blockAccess, p_147754_2_, p_147754_3_, p_147754_4_, 3));
		tessellator.addTranslation(0.0F, 0.0F, f19);
		tessellator.setColorOpaque_F(f10, f14, f18);
		tessellator.addTranslation(f19, 0.0F, 0.0F);
		renderFaceXNeg(p_147754_1_, p_147754_2_, p_147754_3_, p_147754_4_,
				this.getBlockIcon(p_147754_1_, blockAccess, p_147754_2_, p_147754_3_, p_147754_4_, 4));
		tessellator.addTranslation(-f19, 0.0F, 0.0F);
		tessellator.addTranslation(-f19, 0.0F, 0.0F);
		renderFaceXPos(p_147754_1_, p_147754_2_, p_147754_3_, p_147754_4_,
				this.getBlockIcon(p_147754_1_, blockAccess, p_147754_2_, p_147754_3_, p_147754_4_, 5));
		tessellator.addTranslation(f19, 0.0F, 0.0F);
		return true;
	}

	public boolean renderBlockFence(BlockFence p_147735_1_, int p_147735_2_, int p_147735_3_, int p_147735_4_) {
		boolean flag = false;
		float f = 0.375F;
		float f1 = 0.625F;
		setRenderBounds(f, 0.0D, f, f1, 1.0D, f1);
		renderStandardBlock(p_147735_1_, p_147735_2_, p_147735_3_, p_147735_4_);
		flag = true;
		boolean flag1 = false;
		boolean flag2 = false;

		if (p_147735_1_.canConnectFenceTo(blockAccess, p_147735_2_ - 1, p_147735_3_, p_147735_4_)
				|| p_147735_1_.canConnectFenceTo(blockAccess, p_147735_2_ + 1, p_147735_3_, p_147735_4_)) {
			flag1 = true;
		}

		if (p_147735_1_.canConnectFenceTo(blockAccess, p_147735_2_, p_147735_3_, p_147735_4_ - 1)
				|| p_147735_1_.canConnectFenceTo(blockAccess, p_147735_2_, p_147735_3_, p_147735_4_ + 1)) {
			flag2 = true;
		}

		boolean flag3 = p_147735_1_.canConnectFenceTo(blockAccess, p_147735_2_ - 1, p_147735_3_, p_147735_4_);
		boolean flag4 = p_147735_1_.canConnectFenceTo(blockAccess, p_147735_2_ + 1, p_147735_3_, p_147735_4_);
		boolean flag5 = p_147735_1_.canConnectFenceTo(blockAccess, p_147735_2_, p_147735_3_, p_147735_4_ - 1);
		boolean flag6 = p_147735_1_.canConnectFenceTo(blockAccess, p_147735_2_, p_147735_3_, p_147735_4_ + 1);

		if (!flag1 && !flag2) {
			flag1 = true;
		}

		f = 0.4375F;
		f1 = 0.5625F;
		float f2 = 0.75F;
		float f3 = 0.9375F;
		float f4 = flag3 ? 0.0F : f;
		float f5 = flag4 ? 1.0F : f1;
		float f6 = flag5 ? 0.0F : f;
		float f7 = flag6 ? 1.0F : f1;
		field_152631_f = true;

		if (flag1) {
			setRenderBounds(f4, f2, f, f5, f3, f1);
			renderStandardBlock(p_147735_1_, p_147735_2_, p_147735_3_, p_147735_4_);
			flag = true;
		}

		if (flag2) {
			setRenderBounds(f, f2, f6, f1, f3, f7);
			renderStandardBlock(p_147735_1_, p_147735_2_, p_147735_3_, p_147735_4_);
			flag = true;
		}

		f2 = 0.375F;
		f3 = 0.5625F;

		if (flag1) {
			setRenderBounds(f4, f2, f, f5, f3, f1);
			renderStandardBlock(p_147735_1_, p_147735_2_, p_147735_3_, p_147735_4_);
			flag = true;
		}

		if (flag2) {
			setRenderBounds(f, f2, f6, f1, f3, f7);
			renderStandardBlock(p_147735_1_, p_147735_2_, p_147735_3_, p_147735_4_);
			flag = true;
		}

		field_152631_f = false;
		p_147735_1_.setBlockBoundsBasedOnState(blockAccess, p_147735_2_, p_147735_3_, p_147735_4_);
		return flag;
	}

	public boolean renderBlockWall(BlockWall p_147807_1_, int p_147807_2_, int p_147807_3_, int p_147807_4_) {
		boolean flag = p_147807_1_.canConnectWallTo(blockAccess, p_147807_2_ - 1, p_147807_3_, p_147807_4_);
		boolean flag1 = p_147807_1_.canConnectWallTo(blockAccess, p_147807_2_ + 1, p_147807_3_, p_147807_4_);
		boolean flag2 = p_147807_1_.canConnectWallTo(blockAccess, p_147807_2_, p_147807_3_, p_147807_4_ - 1);
		boolean flag3 = p_147807_1_.canConnectWallTo(blockAccess, p_147807_2_, p_147807_3_, p_147807_4_ + 1);
		boolean flag4 = flag2 && flag3 && !flag && !flag1;
		boolean flag5 = !flag2 && !flag3 && flag && flag1;
		boolean flag6 = blockAccess.isAirBlock(p_147807_2_, p_147807_3_ + 1, p_147807_4_);

		if ((flag4 || flag5) && flag6) {
			if (flag4) {
				setRenderBounds(0.3125D, 0.0D, 0.0D, 0.6875D, 0.8125D, 1.0D);
				renderStandardBlock(p_147807_1_, p_147807_2_, p_147807_3_, p_147807_4_);
			} else {
				setRenderBounds(0.0D, 0.0D, 0.3125D, 1.0D, 0.8125D, 0.6875D);
				renderStandardBlock(p_147807_1_, p_147807_2_, p_147807_3_, p_147807_4_);
			}
		} else {
			setRenderBounds(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
			renderStandardBlock(p_147807_1_, p_147807_2_, p_147807_3_, p_147807_4_);

			if (flag) {
				setRenderBounds(0.0D, 0.0D, 0.3125D, 0.25D, 0.8125D, 0.6875D);
				renderStandardBlock(p_147807_1_, p_147807_2_, p_147807_3_, p_147807_4_);
			}

			if (flag1) {
				setRenderBounds(0.75D, 0.0D, 0.3125D, 1.0D, 0.8125D, 0.6875D);
				renderStandardBlock(p_147807_1_, p_147807_2_, p_147807_3_, p_147807_4_);
			}

			if (flag2) {
				setRenderBounds(0.3125D, 0.0D, 0.0D, 0.6875D, 0.8125D, 0.25D);
				renderStandardBlock(p_147807_1_, p_147807_2_, p_147807_3_, p_147807_4_);
			}

			if (flag3) {
				setRenderBounds(0.3125D, 0.0D, 0.75D, 0.6875D, 0.8125D, 1.0D);
				renderStandardBlock(p_147807_1_, p_147807_2_, p_147807_3_, p_147807_4_);
			}
		}

		p_147807_1_.setBlockBoundsBasedOnState(blockAccess, p_147807_2_, p_147807_3_, p_147807_4_);
		return true;
	}

	public boolean renderBlockDragonEgg(BlockDragonEgg p_147802_1_, int p_147802_2_, int p_147802_3_, int p_147802_4_) {
		boolean flag = false;
		int l = 0;

		for (int i1 = 0; i1 < 8; ++i1) {
			byte b0 = 0;
			byte b1 = 1;

			if (i1 == 0) {
				b0 = 2;
			}

			if (i1 == 1) {
				b0 = 3;
			}

			if (i1 == 2) {
				b0 = 4;
			}

			if (i1 == 3) {
				b0 = 5;
				b1 = 2;
			}

			if (i1 == 4) {
				b0 = 6;
				b1 = 3;
			}

			if (i1 == 5) {
				b0 = 7;
				b1 = 5;
			}

			if (i1 == 6) {
				b0 = 6;
				b1 = 2;
			}

			if (i1 == 7) {
				b0 = 3;
			}

			float f = b0 / 16.0F;
			float f1 = 1.0F - l / 16.0F;
			float f2 = 1.0F - (l + b1) / 16.0F;
			l += b1;
			setRenderBounds(0.5F - f, f2, 0.5F - f, 0.5F + f, f1, 0.5F + f);
			renderStandardBlock(p_147802_1_, p_147802_2_, p_147802_3_, p_147802_4_);
		}

		flag = true;
		setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		return flag;
	}

	public boolean renderBlockFenceGate(BlockFenceGate p_147776_1_, int p_147776_2_, int p_147776_3_, int p_147776_4_) {
		boolean flag = true;
		int l = blockAccess.getBlockMetadata(p_147776_2_, p_147776_3_, p_147776_4_);
		boolean flag1 = BlockFenceGate.isFenceGateOpen(l);
		int i1 = BlockDirectional.getDirection(l);
		float f = 0.375F;
		float f1 = 0.5625F;
		float f2 = 0.75F;
		float f3 = 0.9375F;
		float f4 = 0.3125F;
		float f5 = 1.0F;

		if ((i1 == 2 || i1 == 0)
				&& blockAccess.getBlock(p_147776_2_ - 1, p_147776_3_, p_147776_4_) == Blocks.cobblestone_wall
				&& blockAccess.getBlock(p_147776_2_ + 1, p_147776_3_, p_147776_4_) == Blocks.cobblestone_wall
				|| (i1 == 3 || i1 == 1)
						&& blockAccess.getBlock(p_147776_2_, p_147776_3_, p_147776_4_ - 1) == Blocks.cobblestone_wall
						&& blockAccess.getBlock(p_147776_2_, p_147776_3_, p_147776_4_ + 1) == Blocks.cobblestone_wall) {
			f -= 0.1875F;
			f1 -= 0.1875F;
			f2 -= 0.1875F;
			f3 -= 0.1875F;
			f4 -= 0.1875F;
			f5 -= 0.1875F;
		}

		renderAllFaces = true;
		float f6;
		float f7;
		float f8;
		float f9;

		if (i1 != 3 && i1 != 1) {
			f6 = 0.0F;
			f7 = 0.125F;
			f8 = 0.4375F;
			f9 = 0.5625F;
			setRenderBounds(f6, f4, f8, f7, f5, f9);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			f6 = 0.875F;
			f7 = 1.0F;
			setRenderBounds(f6, f4, f8, f7, f5, f9);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
		} else {
			uvRotateTop = 1;
			f6 = 0.4375F;
			f7 = 0.5625F;
			f8 = 0.0F;
			f9 = 0.125F;
			setRenderBounds(f6, f4, f8, f7, f5, f9);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			f8 = 0.875F;
			f9 = 1.0F;
			setRenderBounds(f6, f4, f8, f7, f5, f9);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			uvRotateTop = 0;
		}

		if (flag1) {
			if (i1 == 2 || i1 == 0) {
				uvRotateTop = 1;
			}

			if (i1 == 3) {
				f6 = 0.0F;
				f7 = 0.125F;
				f8 = 0.875F;
				f9 = 1.0F;
				setRenderBounds(0.8125D, f, 0.0D, 0.9375D, f3, 0.125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.8125D, f, 0.875D, 0.9375D, f3, 1.0D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.5625D, f, 0.0D, 0.8125D, f1, 0.125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.5625D, f, 0.875D, 0.8125D, f1, 1.0D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.5625D, f2, 0.0D, 0.8125D, f3, 0.125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.5625D, f2, 0.875D, 0.8125D, f3, 1.0D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			} else if (i1 == 1) {
				f6 = 0.0F;
				f7 = 0.125F;
				f8 = 0.875F;
				f9 = 1.0F;
				setRenderBounds(0.0625D, f, 0.0D, 0.1875D, f3, 0.125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.0625D, f, 0.875D, 0.1875D, f3, 1.0D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.1875D, f, 0.0D, 0.4375D, f1, 0.125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.1875D, f, 0.875D, 0.4375D, f1, 1.0D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.1875D, f2, 0.0D, 0.4375D, f3, 0.125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.1875D, f2, 0.875D, 0.4375D, f3, 1.0D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			} else if (i1 == 0) {
				f6 = 0.0F;
				f7 = 0.125F;
				f8 = 0.875F;
				f9 = 1.0F;
				setRenderBounds(0.0D, f, 0.8125D, 0.125D, f3, 0.9375D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.875D, f, 0.8125D, 1.0D, f3, 0.9375D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.0D, f, 0.5625D, 0.125D, f1, 0.8125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.875D, f, 0.5625D, 1.0D, f1, 0.8125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.0D, f2, 0.5625D, 0.125D, f3, 0.8125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.875D, f2, 0.5625D, 1.0D, f3, 0.8125D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			} else if (i1 == 2) {
				f6 = 0.0F;
				f7 = 0.125F;
				f8 = 0.875F;
				f9 = 1.0F;
				setRenderBounds(0.0D, f, 0.0625D, 0.125D, f3, 0.1875D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.875D, f, 0.0625D, 1.0D, f3, 0.1875D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.0D, f, 0.1875D, 0.125D, f1, 0.4375D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.875D, f, 0.1875D, 1.0D, f1, 0.4375D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.0D, f2, 0.1875D, 0.125D, f3, 0.4375D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
				setRenderBounds(0.875D, f2, 0.1875D, 1.0D, f3, 0.4375D);
				renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			}
		} else if (i1 != 3 && i1 != 1) {
			f6 = 0.375F;
			f7 = 0.5F;
			f8 = 0.4375F;
			f9 = 0.5625F;
			setRenderBounds(f6, f, f8, f7, f3, f9);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			f6 = 0.5F;
			f7 = 0.625F;
			setRenderBounds(f6, f, f8, f7, f3, f9);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			f6 = 0.625F;
			f7 = 0.875F;
			setRenderBounds(f6, f, f8, f7, f1, f9);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			setRenderBounds(f6, f2, f8, f7, f3, f9);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			f6 = 0.125F;
			f7 = 0.375F;
			setRenderBounds(f6, f, f8, f7, f1, f9);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			setRenderBounds(f6, f2, f8, f7, f3, f9);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
		} else {
			uvRotateTop = 1;
			f6 = 0.4375F;
			f7 = 0.5625F;
			f8 = 0.375F;
			f9 = 0.5F;
			setRenderBounds(f6, f, f8, f7, f3, f9);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			f8 = 0.5F;
			f9 = 0.625F;
			setRenderBounds(f6, f, f8, f7, f3, f9);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			f8 = 0.625F;
			f9 = 0.875F;
			setRenderBounds(f6, f, f8, f7, f1, f9);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			setRenderBounds(f6, f2, f8, f7, f3, f9);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			f8 = 0.125F;
			f9 = 0.375F;
			setRenderBounds(f6, f, f8, f7, f1, f9);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
			setRenderBounds(f6, f2, f8, f7, f3, f9);
			renderStandardBlock(p_147776_1_, p_147776_2_, p_147776_3_, p_147776_4_);
		}

		renderAllFaces = false;
		uvRotateTop = 0;
		setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		return flag;
	}

	public boolean renderBlockHopper(BlockHopper p_147803_1_, int p_147803_2_, int p_147803_3_, int p_147803_4_) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(
				p_147803_1_.getMixedBrightnessForBlock(blockAccess, p_147803_2_, p_147803_3_, p_147803_4_));
		int l = p_147803_1_.colorMultiplier(blockAccess, p_147803_2_, p_147803_3_, p_147803_4_);
		float f = (l >> 16 & 255) / 255.0F;
		float f1 = (l >> 8 & 255) / 255.0F;
		float f2 = (l & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		tessellator.setColorOpaque_F(f, f1, f2);
		return renderBlockHopperMetadata(p_147803_1_, p_147803_2_, p_147803_3_, p_147803_4_,
				blockAccess.getBlockMetadata(p_147803_2_, p_147803_3_, p_147803_4_), false);
	}

	public boolean renderBlockHopperMetadata(BlockHopper p_147799_1_, int p_147799_2_, int p_147799_3_, int p_147799_4_,
			int p_147799_5_, boolean p_147799_6_) {
		Tessellator tessellator = Tessellator.instance;
		int i1 = BlockHopper.getDirectionFromMetadata(p_147799_5_);
		double d0 = 0.625D;
		setRenderBounds(0.0D, d0, 0.0D, 1.0D, 1.0D, 1.0D);

		if (p_147799_6_) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			renderFaceYNeg(p_147799_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(p_147799_1_, 0, p_147799_5_));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderFaceYPos(p_147799_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(p_147799_1_, 1, p_147799_5_));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderFaceZNeg(p_147799_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(p_147799_1_, 2, p_147799_5_));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderFaceZPos(p_147799_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(p_147799_1_, 3, p_147799_5_));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderFaceXNeg(p_147799_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(p_147799_1_, 4, p_147799_5_));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderFaceXPos(p_147799_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(p_147799_1_, 5, p_147799_5_));
			tessellator.draw();
		} else {
			renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_);
		}

		float f1;

		if (!p_147799_6_) {
			tessellator.setBrightness(
					p_147799_1_.getMixedBrightnessForBlock(blockAccess, p_147799_2_, p_147799_3_, p_147799_4_));
			int j1 = p_147799_1_.colorMultiplier(blockAccess, p_147799_2_, p_147799_3_, p_147799_4_);
			float f = (j1 >> 16 & 255) / 255.0F;
			f1 = (j1 >> 8 & 255) / 255.0F;
			float f2 = (j1 & 255) / 255.0F;

			if (EntityRenderer.anaglyphEnable) {
				float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
				float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
				float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
				f = f3;
				f1 = f4;
				f2 = f5;
			}

			tessellator.setColorOpaque_F(f, f1, f2);
		}

		IIcon iicon = BlockHopper.getHopperIcon("hopper_outside");
		IIcon iicon1 = BlockHopper.getHopperIcon("hopper_inside");
		f1 = 0.125F;

		if (p_147799_6_) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderFaceXPos(p_147799_1_, -1.0F + f1, 0.0D, 0.0D, iicon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderFaceXNeg(p_147799_1_, 1.0F - f1, 0.0D, 0.0D, iicon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderFaceZPos(p_147799_1_, 0.0D, 0.0D, -1.0F + f1, iicon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderFaceZNeg(p_147799_1_, 0.0D, 0.0D, 1.0F - f1, iicon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderFaceYPos(p_147799_1_, 0.0D, -1.0D + d0, 0.0D, iicon1);
			tessellator.draw();
		} else {
			renderFaceXPos(p_147799_1_, p_147799_2_ - 1.0F + f1, p_147799_3_, p_147799_4_, iicon);
			renderFaceXNeg(p_147799_1_, p_147799_2_ + 1.0F - f1, p_147799_3_, p_147799_4_, iicon);
			renderFaceZPos(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_ - 1.0F + f1, iicon);
			renderFaceZNeg(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_ + 1.0F - f1, iicon);
			renderFaceYPos(p_147799_1_, p_147799_2_, p_147799_3_ - 1.0F + d0, p_147799_4_, iicon1);
		}

		setOverrideBlockTexture(iicon);
		double d3 = 0.25D;
		double d4 = 0.25D;
		setRenderBounds(d3, d4, d3, 1.0D - d3, d0 - 0.002D, 1.0D - d3);

		if (p_147799_6_) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderFaceXPos(p_147799_1_, 0.0D, 0.0D, 0.0D, iicon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderFaceXNeg(p_147799_1_, 0.0D, 0.0D, 0.0D, iicon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderFaceZPos(p_147799_1_, 0.0D, 0.0D, 0.0D, iicon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderFaceZNeg(p_147799_1_, 0.0D, 0.0D, 0.0D, iicon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderFaceYPos(p_147799_1_, 0.0D, 0.0D, 0.0D, iicon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			renderFaceYNeg(p_147799_1_, 0.0D, 0.0D, 0.0D, iicon);
			tessellator.draw();
		} else {
			renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_);
		}

		if (!p_147799_6_) {
			double d1 = 0.375D;
			double d2 = 0.25D;
			setOverrideBlockTexture(iicon);

			if (i1 == 0) {
				setRenderBounds(d1, 0.0D, d1, 1.0D - d1, 0.25D, 1.0D - d1);
				renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_);
			}

			if (i1 == 2) {
				setRenderBounds(d1, d4, 0.0D, 1.0D - d1, d4 + d2, d3);
				renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_);
			}

			if (i1 == 3) {
				setRenderBounds(d1, d4, 1.0D - d3, 1.0D - d1, d4 + d2, 1.0D);
				renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_);
			}

			if (i1 == 4) {
				setRenderBounds(0.0D, d4, d1, d3, d4 + d2, 1.0D - d1);
				renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_);
			}

			if (i1 == 5) {
				setRenderBounds(1.0D - d3, d4, d1, 1.0D, d4 + d2, 1.0D - d1);
				renderStandardBlock(p_147799_1_, p_147799_2_, p_147799_3_, p_147799_4_);
			}
		}

		clearOverrideBlockTexture();
		return true;
	}

	public boolean renderBlockStairs(BlockStairs p_147722_1_, int p_147722_2_, int p_147722_3_, int p_147722_4_) {
		p_147722_1_.func_150147_e(blockAccess, p_147722_2_, p_147722_3_, p_147722_4_);
		setRenderBoundsFromBlock(p_147722_1_);
		renderStandardBlock(p_147722_1_, p_147722_2_, p_147722_3_, p_147722_4_);
		field_152631_f = true;
		boolean flag = p_147722_1_.func_150145_f(blockAccess, p_147722_2_, p_147722_3_, p_147722_4_);
		setRenderBoundsFromBlock(p_147722_1_);
		renderStandardBlock(p_147722_1_, p_147722_2_, p_147722_3_, p_147722_4_);

		if (flag && p_147722_1_.func_150144_g(blockAccess, p_147722_2_, p_147722_3_, p_147722_4_)) {
			setRenderBoundsFromBlock(p_147722_1_);
			renderStandardBlock(p_147722_1_, p_147722_2_, p_147722_3_, p_147722_4_);
		}

		field_152631_f = false;
		return true;
	}

	public boolean renderBlockDoor(Block p_147760_1_, int p_147760_2_, int p_147760_3_, int p_147760_4_) {
		Tessellator tessellator = Tessellator.instance;
		int l = blockAccess.getBlockMetadata(p_147760_2_, p_147760_3_, p_147760_4_);

		if ((l & 8) != 0) {
			if (blockAccess.getBlock(p_147760_2_, p_147760_3_ - 1, p_147760_4_) != p_147760_1_)
				return false;
		} else if (blockAccess.getBlock(p_147760_2_, p_147760_3_ + 1, p_147760_4_) != p_147760_1_)
			return false;

		boolean flag = false;
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		int i1 = p_147760_1_.getMixedBrightnessForBlock(blockAccess, p_147760_2_, p_147760_3_, p_147760_4_);
		tessellator.setBrightness(renderMinY > 0.0D ? i1
				: p_147760_1_.getMixedBrightnessForBlock(blockAccess, p_147760_2_, p_147760_3_ - 1, p_147760_4_));
		tessellator.setColorOpaque_F(f, f, f);
		renderFaceYNeg(p_147760_1_, p_147760_2_, p_147760_3_, p_147760_4_,
				this.getBlockIcon(p_147760_1_, blockAccess, p_147760_2_, p_147760_3_, p_147760_4_, 0));
		flag = true;
		tessellator.setBrightness(renderMaxY < 1.0D ? i1
				: p_147760_1_.getMixedBrightnessForBlock(blockAccess, p_147760_2_, p_147760_3_ + 1, p_147760_4_));
		tessellator.setColorOpaque_F(f1, f1, f1);
		renderFaceYPos(p_147760_1_, p_147760_2_, p_147760_3_, p_147760_4_,
				this.getBlockIcon(p_147760_1_, blockAccess, p_147760_2_, p_147760_3_, p_147760_4_, 1));
		flag = true;
		tessellator.setBrightness(renderMinZ > 0.0D ? i1
				: p_147760_1_.getMixedBrightnessForBlock(blockAccess, p_147760_2_, p_147760_3_, p_147760_4_ - 1));
		tessellator.setColorOpaque_F(f2, f2, f2);
		IIcon iicon = this.getBlockIcon(p_147760_1_, blockAccess, p_147760_2_, p_147760_3_, p_147760_4_, 2);
		renderFaceZNeg(p_147760_1_, p_147760_2_, p_147760_3_, p_147760_4_, iicon);
		flag = true;
		flipTexture = false;
		tessellator.setBrightness(renderMaxZ < 1.0D ? i1
				: p_147760_1_.getMixedBrightnessForBlock(blockAccess, p_147760_2_, p_147760_3_, p_147760_4_ + 1));
		tessellator.setColorOpaque_F(f2, f2, f2);
		iicon = this.getBlockIcon(p_147760_1_, blockAccess, p_147760_2_, p_147760_3_, p_147760_4_, 3);
		renderFaceZPos(p_147760_1_, p_147760_2_, p_147760_3_, p_147760_4_, iicon);
		flag = true;
		flipTexture = false;
		tessellator.setBrightness(renderMinX > 0.0D ? i1
				: p_147760_1_.getMixedBrightnessForBlock(blockAccess, p_147760_2_ - 1, p_147760_3_, p_147760_4_));
		tessellator.setColorOpaque_F(f3, f3, f3);
		iicon = this.getBlockIcon(p_147760_1_, blockAccess, p_147760_2_, p_147760_3_, p_147760_4_, 4);
		renderFaceXNeg(p_147760_1_, p_147760_2_, p_147760_3_, p_147760_4_, iicon);
		flag = true;
		flipTexture = false;
		tessellator.setBrightness(renderMaxX < 1.0D ? i1
				: p_147760_1_.getMixedBrightnessForBlock(blockAccess, p_147760_2_ + 1, p_147760_3_, p_147760_4_));
		tessellator.setColorOpaque_F(f3, f3, f3);
		iicon = this.getBlockIcon(p_147760_1_, blockAccess, p_147760_2_, p_147760_3_, p_147760_4_, 5);
		renderFaceXPos(p_147760_1_, p_147760_2_, p_147760_3_, p_147760_4_, iicon);
		flag = true;
		flipTexture = false;
		return flag;
	}

	public void renderFaceYNeg(Block p_147768_1_, double p_147768_2_, double p_147768_4_, double p_147768_6_,
			IIcon p_147768_8_) {
		Tessellator tessellator = Tessellator.instance;

		if (hasOverrideBlockTexture()) {
			p_147768_8_ = overrideBlockTexture;
		}

		double d3 = p_147768_8_.getInterpolatedU(renderMinX * 16.0D);
		double d4 = p_147768_8_.getInterpolatedU(renderMaxX * 16.0D);
		double d5 = p_147768_8_.getInterpolatedV(renderMinZ * 16.0D);
		double d6 = p_147768_8_.getInterpolatedV(renderMaxZ * 16.0D);

		if (renderMinX < 0.0D || renderMaxX > 1.0D) {
			d3 = p_147768_8_.getMinU();
			d4 = p_147768_8_.getMaxU();
		}

		if (renderMinZ < 0.0D || renderMaxZ > 1.0D) {
			d5 = p_147768_8_.getMinV();
			d6 = p_147768_8_.getMaxV();
		}

		double d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;

		if (uvRotateBottom == 2) {
			d3 = p_147768_8_.getInterpolatedU(renderMinZ * 16.0D);
			d5 = p_147768_8_.getInterpolatedV(16.0D - renderMaxX * 16.0D);
			d4 = p_147768_8_.getInterpolatedU(renderMaxZ * 16.0D);
			d6 = p_147768_8_.getInterpolatedV(16.0D - renderMinX * 16.0D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if (uvRotateBottom == 1) {
			d3 = p_147768_8_.getInterpolatedU(16.0D - renderMaxZ * 16.0D);
			d5 = p_147768_8_.getInterpolatedV(renderMinX * 16.0D);
			d4 = p_147768_8_.getInterpolatedU(16.0D - renderMinZ * 16.0D);
			d6 = p_147768_8_.getInterpolatedV(renderMaxX * 16.0D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if (uvRotateBottom == 3) {
			d3 = p_147768_8_.getInterpolatedU(16.0D - renderMinX * 16.0D);
			d4 = p_147768_8_.getInterpolatedU(16.0D - renderMaxX * 16.0D);
			d5 = p_147768_8_.getInterpolatedV(16.0D - renderMinZ * 16.0D);
			d6 = p_147768_8_.getInterpolatedV(16.0D - renderMaxZ * 16.0D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}

		double d11 = p_147768_2_ + renderMinX;
		double d12 = p_147768_2_ + renderMaxX;
		double d13 = p_147768_4_ + renderMinY;
		double d14 = p_147768_6_ + renderMinZ;
		double d15 = p_147768_6_ + renderMaxZ;

		if (renderFromInside) {
			d11 = p_147768_2_ + renderMaxX;
			d12 = p_147768_2_ + renderMinX;
		}

		if (enableAO) {
			tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
			tessellator.setBrightness(brightnessTopLeft);
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
			tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
			tessellator.setBrightness(brightnessBottomLeft);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
			tessellator.setBrightness(brightnessBottomRight);
			tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
			tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
			tessellator.setBrightness(brightnessTopRight);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
		} else {
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
		}
	}

	public void renderFaceYPos(Block p_147806_1_, double p_147806_2_, double p_147806_4_, double p_147806_6_,
			IIcon p_147806_8_) {
		Tessellator tessellator = Tessellator.instance;

		if (hasOverrideBlockTexture()) {
			p_147806_8_ = overrideBlockTexture;
		}

		double d3 = p_147806_8_.getInterpolatedU(renderMinX * 16.0D);
		double d4 = p_147806_8_.getInterpolatedU(renderMaxX * 16.0D);
		double d5 = p_147806_8_.getInterpolatedV(renderMinZ * 16.0D);
		double d6 = p_147806_8_.getInterpolatedV(renderMaxZ * 16.0D);

		if (renderMinX < 0.0D || renderMaxX > 1.0D) {
			d3 = p_147806_8_.getMinU();
			d4 = p_147806_8_.getMaxU();
		}

		if (renderMinZ < 0.0D || renderMaxZ > 1.0D) {
			d5 = p_147806_8_.getMinV();
			d6 = p_147806_8_.getMaxV();
		}

		double d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;

		if (uvRotateTop == 1) {
			d3 = p_147806_8_.getInterpolatedU(renderMinZ * 16.0D);
			d5 = p_147806_8_.getInterpolatedV(16.0D - renderMaxX * 16.0D);
			d4 = p_147806_8_.getInterpolatedU(renderMaxZ * 16.0D);
			d6 = p_147806_8_.getInterpolatedV(16.0D - renderMinX * 16.0D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if (uvRotateTop == 2) {
			d3 = p_147806_8_.getInterpolatedU(16.0D - renderMaxZ * 16.0D);
			d5 = p_147806_8_.getInterpolatedV(renderMinX * 16.0D);
			d4 = p_147806_8_.getInterpolatedU(16.0D - renderMinZ * 16.0D);
			d6 = p_147806_8_.getInterpolatedV(renderMaxX * 16.0D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if (uvRotateTop == 3) {
			d3 = p_147806_8_.getInterpolatedU(16.0D - renderMinX * 16.0D);
			d4 = p_147806_8_.getInterpolatedU(16.0D - renderMaxX * 16.0D);
			d5 = p_147806_8_.getInterpolatedV(16.0D - renderMinZ * 16.0D);
			d6 = p_147806_8_.getInterpolatedV(16.0D - renderMaxZ * 16.0D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}

		double d11 = p_147806_2_ + renderMinX;
		double d12 = p_147806_2_ + renderMaxX;
		double d13 = p_147806_4_ + renderMaxY;
		double d14 = p_147806_6_ + renderMinZ;
		double d15 = p_147806_6_ + renderMaxZ;

		if (renderFromInside) {
			d11 = p_147806_2_ + renderMaxX;
			d12 = p_147806_2_ + renderMinX;
		}

		if (enableAO) {
			tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
			tessellator.setBrightness(brightnessTopLeft);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
			tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
			tessellator.setBrightness(brightnessBottomLeft);
			tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
			tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
			tessellator.setBrightness(brightnessBottomRight);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
			tessellator.setBrightness(brightnessTopRight);
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
		} else {
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
			tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
		}
	}

	public void renderFaceZNeg(Block p_147761_1_, double p_147761_2_, double p_147761_4_, double p_147761_6_,
			IIcon p_147761_8_) {
		Tessellator tessellator = Tessellator.instance;

		if (hasOverrideBlockTexture()) {
			p_147761_8_ = overrideBlockTexture;
		}

		double d3 = p_147761_8_.getInterpolatedU(renderMinX * 16.0D);
		double d4 = p_147761_8_.getInterpolatedU(renderMaxX * 16.0D);

		if (field_152631_f) {
			d4 = p_147761_8_.getInterpolatedU((1.0D - renderMinX) * 16.0D);
			d3 = p_147761_8_.getInterpolatedU((1.0D - renderMaxX) * 16.0D);
		}

		double d5 = p_147761_8_.getInterpolatedV(16.0D - renderMaxY * 16.0D);
		double d6 = p_147761_8_.getInterpolatedV(16.0D - renderMinY * 16.0D);
		double d7;

		if (flipTexture) {
			d7 = d3;
			d3 = d4;
			d4 = d7;
		}

		if (renderMinX < 0.0D || renderMaxX > 1.0D) {
			d3 = p_147761_8_.getMinU();
			d4 = p_147761_8_.getMaxU();
		}

		if (renderMinY < 0.0D || renderMaxY > 1.0D) {
			d5 = p_147761_8_.getMinV();
			d6 = p_147761_8_.getMaxV();
		}

		d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;

		if (uvRotateEast == 2) {
			d3 = p_147761_8_.getInterpolatedU(renderMinY * 16.0D);
			d4 = p_147761_8_.getInterpolatedU(renderMaxY * 16.0D);
			d5 = p_147761_8_.getInterpolatedV(16.0D - renderMinX * 16.0D);
			d6 = p_147761_8_.getInterpolatedV(16.0D - renderMaxX * 16.0D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if (uvRotateEast == 1) {
			d3 = p_147761_8_.getInterpolatedU(16.0D - renderMaxY * 16.0D);
			d4 = p_147761_8_.getInterpolatedU(16.0D - renderMinY * 16.0D);
			d5 = p_147761_8_.getInterpolatedV(renderMaxX * 16.0D);
			d6 = p_147761_8_.getInterpolatedV(renderMinX * 16.0D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if (uvRotateEast == 3) {
			d3 = p_147761_8_.getInterpolatedU(16.0D - renderMinX * 16.0D);
			d4 = p_147761_8_.getInterpolatedU(16.0D - renderMaxX * 16.0D);
			d5 = p_147761_8_.getInterpolatedV(renderMaxY * 16.0D);
			d6 = p_147761_8_.getInterpolatedV(renderMinY * 16.0D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}

		double d11 = p_147761_2_ + renderMinX;
		double d12 = p_147761_2_ + renderMaxX;
		double d13 = p_147761_4_ + renderMinY;
		double d14 = p_147761_4_ + renderMaxY;
		double d15 = p_147761_6_ + renderMinZ;

		if (renderFromInside) {
			d11 = p_147761_2_ + renderMaxX;
			d12 = p_147761_2_ + renderMinX;
		}

		if (enableAO) {
			tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
			tessellator.setBrightness(brightnessTopLeft);
			tessellator.addVertexWithUV(d11, d14, d15, d7, d9);
			tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
			tessellator.setBrightness(brightnessBottomLeft);
			tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
			tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
			tessellator.setBrightness(brightnessBottomRight);
			tessellator.addVertexWithUV(d12, d13, d15, d8, d10);
			tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
			tessellator.setBrightness(brightnessTopRight);
			tessellator.addVertexWithUV(d11, d13, d15, d4, d6);
		} else {
			tessellator.addVertexWithUV(d11, d14, d15, d7, d9);
			tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
			tessellator.addVertexWithUV(d12, d13, d15, d8, d10);
			tessellator.addVertexWithUV(d11, d13, d15, d4, d6);
		}
	}

	public void renderFaceZPos(Block p_147734_1_, double p_147734_2_, double p_147734_4_, double p_147734_6_,
			IIcon p_147734_8_) {
		Tessellator tessellator = Tessellator.instance;

		if (hasOverrideBlockTexture()) {
			p_147734_8_ = overrideBlockTexture;
		}

		double d3 = p_147734_8_.getInterpolatedU(renderMinX * 16.0D);
		double d4 = p_147734_8_.getInterpolatedU(renderMaxX * 16.0D);
		double d5 = p_147734_8_.getInterpolatedV(16.0D - renderMaxY * 16.0D);
		double d6 = p_147734_8_.getInterpolatedV(16.0D - renderMinY * 16.0D);
		double d7;

		if (flipTexture) {
			d7 = d3;
			d3 = d4;
			d4 = d7;
		}

		if (renderMinX < 0.0D || renderMaxX > 1.0D) {
			d3 = p_147734_8_.getMinU();
			d4 = p_147734_8_.getMaxU();
		}

		if (renderMinY < 0.0D || renderMaxY > 1.0D) {
			d5 = p_147734_8_.getMinV();
			d6 = p_147734_8_.getMaxV();
		}

		d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;

		if (uvRotateWest == 1) {
			d3 = p_147734_8_.getInterpolatedU(renderMinY * 16.0D);
			d6 = p_147734_8_.getInterpolatedV(16.0D - renderMinX * 16.0D);
			d4 = p_147734_8_.getInterpolatedU(renderMaxY * 16.0D);
			d5 = p_147734_8_.getInterpolatedV(16.0D - renderMaxX * 16.0D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if (uvRotateWest == 2) {
			d3 = p_147734_8_.getInterpolatedU(16.0D - renderMaxY * 16.0D);
			d5 = p_147734_8_.getInterpolatedV(renderMinX * 16.0D);
			d4 = p_147734_8_.getInterpolatedU(16.0D - renderMinY * 16.0D);
			d6 = p_147734_8_.getInterpolatedV(renderMaxX * 16.0D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if (uvRotateWest == 3) {
			d3 = p_147734_8_.getInterpolatedU(16.0D - renderMinX * 16.0D);
			d4 = p_147734_8_.getInterpolatedU(16.0D - renderMaxX * 16.0D);
			d5 = p_147734_8_.getInterpolatedV(renderMaxY * 16.0D);
			d6 = p_147734_8_.getInterpolatedV(renderMinY * 16.0D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}

		double d11 = p_147734_2_ + renderMinX;
		double d12 = p_147734_2_ + renderMaxX;
		double d13 = p_147734_4_ + renderMinY;
		double d14 = p_147734_4_ + renderMaxY;
		double d15 = p_147734_6_ + renderMaxZ;

		if (renderFromInside) {
			d11 = p_147734_2_ + renderMaxX;
			d12 = p_147734_2_ + renderMinX;
		}

		if (enableAO) {
			tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
			tessellator.setBrightness(brightnessTopLeft);
			tessellator.addVertexWithUV(d11, d14, d15, d3, d5);
			tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
			tessellator.setBrightness(brightnessBottomLeft);
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
			tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
			tessellator.setBrightness(brightnessBottomRight);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
			tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
			tessellator.setBrightness(brightnessTopRight);
			tessellator.addVertexWithUV(d12, d14, d15, d7, d9);
		} else {
			tessellator.addVertexWithUV(d11, d14, d15, d3, d5);
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
			tessellator.addVertexWithUV(d12, d14, d15, d7, d9);
		}
	}

	public void renderFaceXNeg(Block p_147798_1_, double p_147798_2_, double p_147798_4_, double p_147798_6_,
			IIcon p_147798_8_) {
		Tessellator tessellator = Tessellator.instance;

		if (hasOverrideBlockTexture()) {
			p_147798_8_ = overrideBlockTexture;
		}

		double d3 = p_147798_8_.getInterpolatedU(renderMinZ * 16.0D);
		double d4 = p_147798_8_.getInterpolatedU(renderMaxZ * 16.0D);
		double d5 = p_147798_8_.getInterpolatedV(16.0D - renderMaxY * 16.0D);
		double d6 = p_147798_8_.getInterpolatedV(16.0D - renderMinY * 16.0D);
		double d7;

		if (flipTexture) {
			d7 = d3;
			d3 = d4;
			d4 = d7;
		}

		if (renderMinZ < 0.0D || renderMaxZ > 1.0D) {
			d3 = p_147798_8_.getMinU();
			d4 = p_147798_8_.getMaxU();
		}

		if (renderMinY < 0.0D || renderMaxY > 1.0D) {
			d5 = p_147798_8_.getMinV();
			d6 = p_147798_8_.getMaxV();
		}

		d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;

		if (uvRotateNorth == 1) {
			d3 = p_147798_8_.getInterpolatedU(renderMinY * 16.0D);
			d5 = p_147798_8_.getInterpolatedV(16.0D - renderMaxZ * 16.0D);
			d4 = p_147798_8_.getInterpolatedU(renderMaxY * 16.0D);
			d6 = p_147798_8_.getInterpolatedV(16.0D - renderMinZ * 16.0D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if (uvRotateNorth == 2) {
			d3 = p_147798_8_.getInterpolatedU(16.0D - renderMaxY * 16.0D);
			d5 = p_147798_8_.getInterpolatedV(renderMinZ * 16.0D);
			d4 = p_147798_8_.getInterpolatedU(16.0D - renderMinY * 16.0D);
			d6 = p_147798_8_.getInterpolatedV(renderMaxZ * 16.0D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if (uvRotateNorth == 3) {
			d3 = p_147798_8_.getInterpolatedU(16.0D - renderMinZ * 16.0D);
			d4 = p_147798_8_.getInterpolatedU(16.0D - renderMaxZ * 16.0D);
			d5 = p_147798_8_.getInterpolatedV(renderMaxY * 16.0D);
			d6 = p_147798_8_.getInterpolatedV(renderMinY * 16.0D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}

		double d11 = p_147798_2_ + renderMinX;
		double d12 = p_147798_4_ + renderMinY;
		double d13 = p_147798_4_ + renderMaxY;
		double d14 = p_147798_6_ + renderMinZ;
		double d15 = p_147798_6_ + renderMaxZ;

		if (renderFromInside) {
			d14 = p_147798_6_ + renderMaxZ;
			d15 = p_147798_6_ + renderMinZ;
		}

		if (enableAO) {
			tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
			tessellator.setBrightness(brightnessTopLeft);
			tessellator.addVertexWithUV(d11, d13, d15, d7, d9);
			tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
			tessellator.setBrightness(brightnessBottomLeft);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
			tessellator.setBrightness(brightnessBottomRight);
			tessellator.addVertexWithUV(d11, d12, d14, d8, d10);
			tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
			tessellator.setBrightness(brightnessTopRight);
			tessellator.addVertexWithUV(d11, d12, d15, d4, d6);
		} else {
			tessellator.addVertexWithUV(d11, d13, d15, d7, d9);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.addVertexWithUV(d11, d12, d14, d8, d10);
			tessellator.addVertexWithUV(d11, d12, d15, d4, d6);
		}
	}

	public void renderFaceXPos(Block p_147764_1_, double p_147764_2_, double p_147764_4_, double p_147764_6_,
			IIcon p_147764_8_) {
		Tessellator tessellator = Tessellator.instance;

		if (hasOverrideBlockTexture()) {
			p_147764_8_ = overrideBlockTexture;
		}

		double d3 = p_147764_8_.getInterpolatedU(renderMinZ * 16.0D);
		double d4 = p_147764_8_.getInterpolatedU(renderMaxZ * 16.0D);

		if (field_152631_f) {
			d4 = p_147764_8_.getInterpolatedU((1.0D - renderMinZ) * 16.0D);
			d3 = p_147764_8_.getInterpolatedU((1.0D - renderMaxZ) * 16.0D);
		}

		double d5 = p_147764_8_.getInterpolatedV(16.0D - renderMaxY * 16.0D);
		double d6 = p_147764_8_.getInterpolatedV(16.0D - renderMinY * 16.0D);
		double d7;

		if (flipTexture) {
			d7 = d3;
			d3 = d4;
			d4 = d7;
		}

		if (renderMinZ < 0.0D || renderMaxZ > 1.0D) {
			d3 = p_147764_8_.getMinU();
			d4 = p_147764_8_.getMaxU();
		}

		if (renderMinY < 0.0D || renderMaxY > 1.0D) {
			d5 = p_147764_8_.getMinV();
			d6 = p_147764_8_.getMaxV();
		}

		d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;

		if (uvRotateSouth == 2) {
			d3 = p_147764_8_.getInterpolatedU(renderMinY * 16.0D);
			d5 = p_147764_8_.getInterpolatedV(16.0D - renderMinZ * 16.0D);
			d4 = p_147764_8_.getInterpolatedU(renderMaxY * 16.0D);
			d6 = p_147764_8_.getInterpolatedV(16.0D - renderMaxZ * 16.0D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if (uvRotateSouth == 1) {
			d3 = p_147764_8_.getInterpolatedU(16.0D - renderMaxY * 16.0D);
			d5 = p_147764_8_.getInterpolatedV(renderMaxZ * 16.0D);
			d4 = p_147764_8_.getInterpolatedU(16.0D - renderMinY * 16.0D);
			d6 = p_147764_8_.getInterpolatedV(renderMinZ * 16.0D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if (uvRotateSouth == 3) {
			d3 = p_147764_8_.getInterpolatedU(16.0D - renderMinZ * 16.0D);
			d4 = p_147764_8_.getInterpolatedU(16.0D - renderMaxZ * 16.0D);
			d5 = p_147764_8_.getInterpolatedV(renderMaxY * 16.0D);
			d6 = p_147764_8_.getInterpolatedV(renderMinY * 16.0D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}

		double d11 = p_147764_2_ + renderMaxX;
		double d12 = p_147764_4_ + renderMinY;
		double d13 = p_147764_4_ + renderMaxY;
		double d14 = p_147764_6_ + renderMinZ;
		double d15 = p_147764_6_ + renderMaxZ;

		if (renderFromInside) {
			d14 = p_147764_6_ + renderMaxZ;
			d15 = p_147764_6_ + renderMinZ;
		}

		if (enableAO) {
			tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
			tessellator.setBrightness(brightnessTopLeft);
			tessellator.addVertexWithUV(d11, d12, d15, d8, d10);
			tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
			tessellator.setBrightness(brightnessBottomLeft);
			tessellator.addVertexWithUV(d11, d12, d14, d4, d6);
			tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
			tessellator.setBrightness(brightnessBottomRight);
			tessellator.addVertexWithUV(d11, d13, d14, d7, d9);
			tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
			tessellator.setBrightness(brightnessTopRight);
			tessellator.addVertexWithUV(d11, d13, d15, d3, d5);
		} else {
			tessellator.addVertexWithUV(d11, d12, d15, d8, d10);
			tessellator.addVertexWithUV(d11, d12, d14, d4, d6);
			tessellator.addVertexWithUV(d11, d13, d14, d7, d9);
			tessellator.addVertexWithUV(d11, d13, d15, d3, d5);
		}
	}

	public void renderBlockAsItem(Block p_147800_1_, int p_147800_2_, float p_147800_3_) {
		Tessellator tessellator = Tessellator.instance;
		boolean flag = p_147800_1_ == Blocks.grass;

		if (p_147800_1_ == Blocks.dispenser || p_147800_1_ == Blocks.dropper || p_147800_1_ == Blocks.furnace) {
			p_147800_2_ = 3;
		}

		int j;
		float f1;
		float f2;
		float f3;

		if (useInventoryTint) {
			j = p_147800_1_.getRenderColor(p_147800_2_);

			if (flag) {
				j = 16777215;
			}

			f1 = (j >> 16 & 255) / 255.0F;
			f2 = (j >> 8 & 255) / 255.0F;
			f3 = (j & 255) / 255.0F;
			GL11.glColor4f(f1 * p_147800_3_, f2 * p_147800_3_, f3 * p_147800_3_, 1.0F);
		}

		j = p_147800_1_.getRenderType();
		setRenderBoundsFromBlock(p_147800_1_);
		int k;

		if (j != 0 && j != 31 && j != 39 && j != 16 && j != 26) {
			if (j == 1) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				IIcon iicon = getBlockIconFromSideAndMetadata(p_147800_1_, 0, p_147800_2_);
				drawCrossedSquares(iicon, -0.5D, -0.5D, -0.5D, 1.0F);
				tessellator.draw();
			} else if (j == 19) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				p_147800_1_.setBlockBoundsForItemRender();
				renderBlockStemSmall(p_147800_1_, p_147800_2_, renderMaxY, -0.5D, -0.5D, -0.5D);
				tessellator.draw();
			} else if (j == 23) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				p_147800_1_.setBlockBoundsForItemRender();
				tessellator.draw();
			} else if (j == 13) {
				p_147800_1_.setBlockBoundsForItemRender();
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				f1 = 0.0625F;
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				renderFaceYNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 0));
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 1.0F, 0.0F);
				renderFaceYPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 1));
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 0.0F, -1.0F);
				tessellator.addTranslation(0.0F, 0.0F, f1);
				renderFaceZNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 2));
				tessellator.addTranslation(0.0F, 0.0F, -f1);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 0.0F, 1.0F);
				tessellator.addTranslation(0.0F, 0.0F, -f1);
				renderFaceZPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 3));
				tessellator.addTranslation(0.0F, 0.0F, f1);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(-1.0F, 0.0F, 0.0F);
				tessellator.addTranslation(f1, 0.0F, 0.0F);
				renderFaceXNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 4));
				tessellator.addTranslation(-f1, 0.0F, 0.0F);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(1.0F, 0.0F, 0.0F);
				tessellator.addTranslation(-f1, 0.0F, 0.0F);
				renderFaceXPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 5));
				tessellator.addTranslation(f1, 0.0F, 0.0F);
				tessellator.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			} else if (j == 22) {
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				TileEntityRendererChestHelper.instance.renderChest(p_147800_1_, p_147800_2_, p_147800_3_);
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			} else if (j == 6) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				renderBlockCropsImpl(p_147800_1_, p_147800_2_, -0.5D, -0.5D, -0.5D);
				tessellator.draw();
			} else if (j == 2) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				renderTorchAtAngle(p_147800_1_, -0.5D, -0.5D, -0.5D, 0.0D, 0.0D, 0);
				tessellator.draw();
			} else if (j == 10) {
				for (k = 0; k < 2; ++k) {
					if (k == 0) {
						setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
					}

					if (k == 1) {
						setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, -1.0F, 0.0F);
					renderFaceYNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 0));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					renderFaceYPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 1));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, -1.0F);
					renderFaceZNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 2));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, 1.0F);
					renderFaceZPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 3));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(-1.0F, 0.0F, 0.0F);
					renderFaceXNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 4));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(1.0F, 0.0F, 0.0F);
					renderFaceXPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 5));
					tessellator.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}
			} else if (j == 27) {
				k = 0;
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				tessellator.startDrawingQuads();

				for (int l = 0; l < 8; ++l) {
					byte b0 = 0;
					byte b1 = 1;

					if (l == 0) {
						b0 = 2;
					}

					if (l == 1) {
						b0 = 3;
					}

					if (l == 2) {
						b0 = 4;
					}

					if (l == 3) {
						b0 = 5;
						b1 = 2;
					}

					if (l == 4) {
						b0 = 6;
						b1 = 3;
					}

					if (l == 5) {
						b0 = 7;
						b1 = 5;
					}

					if (l == 6) {
						b0 = 6;
						b1 = 2;
					}

					if (l == 7) {
						b0 = 3;
					}

					float f5 = b0 / 16.0F;
					float f6 = 1.0F - k / 16.0F;
					float f7 = 1.0F - (k + b1) / 16.0F;
					k += b1;
					setRenderBounds(0.5F - f5, f7, 0.5F - f5, 0.5F + f5, f6, 0.5F + f5);
					tessellator.setNormal(0.0F, -1.0F, 0.0F);
					renderFaceYNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 0));
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					renderFaceYPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 1));
					tessellator.setNormal(0.0F, 0.0F, -1.0F);
					renderFaceZNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 2));
					tessellator.setNormal(0.0F, 0.0F, 1.0F);
					renderFaceZPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 3));
					tessellator.setNormal(-1.0F, 0.0F, 0.0F);
					renderFaceXNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 4));
					tessellator.setNormal(1.0F, 0.0F, 0.0F);
					renderFaceXPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 5));
				}

				tessellator.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			} else if (j == 11) {
				for (k = 0; k < 4; ++k) {
					f2 = 0.125F;

					if (k == 0) {
						setRenderBounds(0.5F - f2, 0.0D, 0.0D, 0.5F + f2, 1.0D, f2 * 2.0F);
					}

					if (k == 1) {
						setRenderBounds(0.5F - f2, 0.0D, 1.0F - f2 * 2.0F, 0.5F + f2, 1.0D, 1.0D);
					}

					f2 = 0.0625F;

					if (k == 2) {
						setRenderBounds(0.5F - f2, 1.0F - f2 * 3.0F, -f2 * 2.0F, 0.5F + f2, 1.0F - f2,
								1.0F + f2 * 2.0F);
					}

					if (k == 3) {
						setRenderBounds(0.5F - f2, 0.5F - f2 * 3.0F, -f2 * 2.0F, 0.5F + f2, 0.5F - f2,
								1.0F + f2 * 2.0F);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, -1.0F, 0.0F);
					renderFaceYNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 0));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					renderFaceYPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 1));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, -1.0F);
					renderFaceZNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 2));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, 1.0F);
					renderFaceZPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 3));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(-1.0F, 0.0F, 0.0F);
					renderFaceXNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 4));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(1.0F, 0.0F, 0.0F);
					renderFaceXPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 5));
					tessellator.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

				setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			} else if (j == 21) {
				for (k = 0; k < 3; ++k) {
					f2 = 0.0625F;

					if (k == 0) {
						setRenderBounds(0.5F - f2, 0.30000001192092896D, 0.0D, 0.5F + f2, 1.0D, f2 * 2.0F);
					}

					if (k == 1) {
						setRenderBounds(0.5F - f2, 0.30000001192092896D, 1.0F - f2 * 2.0F, 0.5F + f2, 1.0D, 1.0D);
					}

					f2 = 0.0625F;

					if (k == 2) {
						setRenderBounds(0.5F - f2, 0.5D, 0.0D, 0.5F + f2, 1.0F - f2, 1.0D);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, -1.0F, 0.0F);
					renderFaceYNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 0));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					renderFaceYPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 1));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, -1.0F);
					renderFaceZNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 2));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, 1.0F);
					renderFaceZPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 3));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(-1.0F, 0.0F, 0.0F);
					renderFaceXNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 4));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(1.0F, 0.0F, 0.0F);
					renderFaceXPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(p_147800_1_, 5));
					tessellator.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}
			} else if (j == 32) {
				for (k = 0; k < 2; ++k) {
					if (k == 0) {
						setRenderBounds(0.0D, 0.0D, 0.3125D, 1.0D, 0.8125D, 0.6875D);
					}

					if (k == 1) {
						setRenderBounds(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, -1.0F, 0.0F);
					renderFaceYNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 0, p_147800_2_));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					renderFaceYPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 1, p_147800_2_));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, -1.0F);
					renderFaceZNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 2, p_147800_2_));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, 1.0F);
					renderFaceZPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 3, p_147800_2_));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(-1.0F, 0.0F, 0.0F);
					renderFaceXNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 4, p_147800_2_));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(1.0F, 0.0F, 0.0F);
					renderFaceXPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 5, p_147800_2_));
					tessellator.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

				setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			} else if (j == 35) {
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				renderBlockAnvilOrient((BlockAnvil) p_147800_1_, 0, 0, 0, p_147800_2_ << 2, true);
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			} else if (j == 34) {
				for (k = 0; k < 3; ++k) {
					if (k == 0) {
						setRenderBounds(0.125D, 0.0D, 0.125D, 0.875D, 0.1875D, 0.875D);
						setOverrideBlockTexture(this.getBlockIcon(Blocks.obsidian));
					} else if (k == 1) {
						setRenderBounds(0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.875D, 0.8125D);
						setOverrideBlockTexture(this.getBlockIcon(Blocks.beacon));
					} else if (k == 2) {
						setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
						setOverrideBlockTexture(this.getBlockIcon(Blocks.glass));
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, -1.0F, 0.0F);
					renderFaceYNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 0, p_147800_2_));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					renderFaceYPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 1, p_147800_2_));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, -1.0F);
					renderFaceZNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 2, p_147800_2_));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, 1.0F);
					renderFaceZPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 3, p_147800_2_));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(-1.0F, 0.0F, 0.0F);
					renderFaceXNeg(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 4, p_147800_2_));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(1.0F, 0.0F, 0.0F);
					renderFaceXPos(p_147800_1_, 0.0D, 0.0D, 0.0D,
							getBlockIconFromSideAndMetadata(p_147800_1_, 5, p_147800_2_));
					tessellator.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

				setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
				clearOverrideBlockTexture();
			} else if (j == 38) {
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				renderBlockHopperMetadata((BlockHopper) p_147800_1_, 0, 0, 0, 0, true);
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			} else {
				FMLRenderAccessLibrary.renderInventoryBlock(this, p_147800_1_, p_147800_2_, j);
			}
		} else {
			if (j == 16) {
				p_147800_2_ = 1;
			}

			p_147800_1_.setBlockBoundsForItemRender();
			setRenderBoundsFromBlock(p_147800_1_);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			renderFaceYNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(p_147800_1_, 0, p_147800_2_));
			tessellator.draw();

			if (flag && useInventoryTint) {
				k = p_147800_1_.getRenderColor(p_147800_2_);
				f2 = (k >> 16 & 255) / 255.0F;
				f3 = (k >> 8 & 255) / 255.0F;
				float f4 = (k & 255) / 255.0F;
				GL11.glColor4f(f2 * p_147800_3_, f3 * p_147800_3_, f4 * p_147800_3_, 1.0F);
			}

			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderFaceYPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(p_147800_1_, 1, p_147800_2_));
			tessellator.draw();

			if (flag && useInventoryTint) {
				GL11.glColor4f(p_147800_3_, p_147800_3_, p_147800_3_, 1.0F);
			}

			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderFaceZNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(p_147800_1_, 2, p_147800_2_));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderFaceZPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(p_147800_1_, 3, p_147800_2_));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderFaceXNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(p_147800_1_, 4, p_147800_2_));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderFaceXPos(p_147800_1_, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(p_147800_1_, 5, p_147800_2_));
			tessellator.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}
	}

	public static boolean renderItemIn3d(int p_147739_0_) {
		switch (p_147739_0_) {
		case 0:
			return true;
		case 31:
			return true;
		case 39:
			return true;
		case 13:
			return true;
		case 10:
			return true;
		case 11:
			return true;
		case 27:
			return true;
		case 22:
			return true;
		case 21:
			return true;
		case 16:
			return true;
		case 26:
			return true;
		case 32:
			return true;
		case 34:
			return true;
		case 35:
			return true;
		default:
			return FMLRenderAccessLibrary.renderItemAsFull3DBlock(p_147739_0_);
		}
	}

	public IIcon getBlockIcon(Block p_147793_1_, IBlockAccess p_147793_2_, int p_147793_3_, int p_147793_4_,
			int p_147793_5_, int p_147793_6_) {
		return getIconSafe(p_147793_1_.getIcon(p_147793_2_, p_147793_3_, p_147793_4_, p_147793_5_, p_147793_6_));
	}

	public IIcon getBlockIconFromSideAndMetadata(Block p_147787_1_, int p_147787_2_, int p_147787_3_) {
		return getIconSafe(p_147787_1_.getIcon(p_147787_2_, p_147787_3_));
	}

	public IIcon getBlockIconFromSide(Block p_147777_1_, int p_147777_2_) {
		return getIconSafe(p_147777_1_.getBlockTextureFromSide(p_147777_2_));
	}

	public IIcon getBlockIcon(Block p_147745_1_) {
		return getIconSafe(p_147745_1_.getBlockTextureFromSide(1));
	}

	public IIcon getIconSafe(IIcon p_147758_1_) {
		if (p_147758_1_ == null) {
			p_147758_1_ = ((TextureMap) Minecraft.getMinecraft().getTextureManager()
					.getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
		}

		return p_147758_1_;
	}

	/*
	 * ==================================== FORGE START
	 * ===========================================
	 */
	private static RenderBlocks instance;

	/**
	 * Returns a single lazy loaded instance of RenderBlocks, for use in mods who
	 * don't care about the interaction of other objects on the current state of the
	 * RenderBlocks they are using.
	 *
	 * @return A global instance of RenderBlocks
	 */
	public static RenderBlocks getInstance() {
		if (instance == null) {
			instance = new RenderBlocks();
		}
		return instance;
	}
	/*
	 * ==================================== FORGE END
	 * =============================================
	 */
}