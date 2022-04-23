package net.minecraft.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.shader.TesselatorVertexState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SideOnly(Side.CLIENT)
public class WorldRenderer {
	private TesselatorVertexState vertexState;
	public World worldObj;
	private int glRenderList = -1;
	// private static Tessellator tessellator = Tessellator.instance;
	public static int chunksUpdated;
	public int posX;
	public int posY;
	public int posZ;
	public int posXMinus;
	public int posYMinus;
	public int posZMinus;
	public int posXClip;
	public int posYClip;
	public int posZClip;
	public boolean isInFrustum;
	public boolean[] skipRenderPass = new boolean[2];
	public int posXPlus;
	public int posYPlus;
	public int posZPlus;
	public boolean needsUpdate;
	public AxisAlignedBB rendererBoundingBox;
	public int chunkIndex;
	public boolean isVisible = true;
	public boolean isWaitingOnOcclusionQuery;
	public int glOcclusionQuery;
	public boolean isChunkLit;
	private boolean isInitialized;
	public List tileEntityRenderers = new ArrayList();
	private final List tileEntities;
	private int bytesDrawn;
	private static final String __OBFID = "CL_00000942";

	public WorldRenderer(World p_i1240_1_, List p_i1240_2_, int p_i1240_3_, int p_i1240_4_, int p_i1240_5_,
			int p_i1240_6_) {
		worldObj = p_i1240_1_;
		vertexState = null;
		tileEntities = p_i1240_2_;
		glRenderList = p_i1240_6_;
		posX = -999;
		setPosition(p_i1240_3_, p_i1240_4_, p_i1240_5_);
		needsUpdate = false;
	}

	public void setPosition(int p_78913_1_, int p_78913_2_, int p_78913_3_) {
		if (p_78913_1_ != posX || p_78913_2_ != posY || p_78913_3_ != posZ) {
			setDontDraw();
			posX = p_78913_1_;
			posY = p_78913_2_;
			posZ = p_78913_3_;
			posXPlus = p_78913_1_ + 8;
			posYPlus = p_78913_2_ + 8;
			posZPlus = p_78913_3_ + 8;
			posXClip = p_78913_1_ & 1023;
			posYClip = p_78913_2_;
			posZClip = p_78913_3_ & 1023;
			posXMinus = p_78913_1_ - posXClip;
			posYMinus = p_78913_2_ - posYClip;
			posZMinus = p_78913_3_ - posZClip;
			float f = 6.0F;
			rendererBoundingBox = AxisAlignedBB.getBoundingBox(p_78913_1_ - f, p_78913_2_ - f, p_78913_3_ - f,
					p_78913_1_ + 16 + f, p_78913_2_ + 16 + f, p_78913_3_ + 16 + f);
			GL11.glNewList(glRenderList + 2, GL11.GL_COMPILE);
			Render.renderAABB(AxisAlignedBB.getBoundingBox(posXClip - f, posYClip - f, posZClip - f, posXClip + 16 + f,
					posYClip + 16 + f, posZClip + 16 + f));
			GL11.glEndList();
			markDirty();
		}
	}

	private void setupGLTranslation() {
		GL11.glTranslatef(posXClip, posYClip, posZClip);
	}

	public void updateRenderer(EntityLivingBase p_147892_1_) {
		if (needsUpdate) {
			needsUpdate = false;
			int i = posX;
			int j = posY;
			int k = posZ;
			int l = posX + 16;
			int i1 = posY + 16;
			int j1 = posZ + 16;

			for (int k1 = 0; k1 < 2; ++k1) {
				skipRenderPass[k1] = true;
			}

			Chunk.isLit = false;
			HashSet hashset = new HashSet();
			hashset.addAll(tileEntityRenderers);
			tileEntityRenderers.clear();
			Minecraft minecraft = Minecraft.getMinecraft();
			EntityLivingBase entitylivingbase1 = minecraft.renderViewEntity;
			int l1 = MathHelper.floor_double(entitylivingbase1.posX);
			int i2 = MathHelper.floor_double(entitylivingbase1.posY);
			int j2 = MathHelper.floor_double(entitylivingbase1.posZ);
			byte b0 = 1;
			ChunkCache chunkcache = new ChunkCache(worldObj, i - b0, j - b0, k - b0, l + b0, i1 + b0, j1 + b0, b0);

			if (!chunkcache.extendedLevelsInChunkCache()) {
				++chunksUpdated;
				RenderBlocks renderblocks = new RenderBlocks(chunkcache);
				net.minecraftforge.client.ForgeHooksClient.setWorldRendererRB(renderblocks);
				bytesDrawn = 0;
				vertexState = null;

				for (int k2 = 0; k2 < 2; ++k2) {
					boolean flag = false;
					boolean flag1 = false;
					boolean flag2 = false;

					for (int l2 = j; l2 < i1; ++l2) {
						for (int i3 = k; i3 < j1; ++i3) {
							for (int j3 = i; j3 < l; ++j3) {
								Block block = chunkcache.getBlock(j3, l2, i3);

								if (block.getMaterial() != Material.air) {
									if (!flag2) {
										flag2 = true;
										preRenderBlocks(k2);
									}

									if (k2 == 0 && block.hasTileEntity(chunkcache.getBlockMetadata(j3, l2, i3))) {
										TileEntity tileentity = chunkcache.getTileEntity(j3, l2, i3);

										if (TileEntityRendererDispatcher.instance.hasSpecialRenderer(tileentity)) {
											tileEntityRenderers.add(tileentity);
										}
									}

									int k3 = block.getRenderBlockPass();

									if (k3 > k2) {
										flag = true;
									}

									if (!block.canRenderInPass(k2)) {
										continue;
									}

									{
										flag1 |= renderblocks.renderBlockByRenderType(block, j3, l2, i3);

										if (block.getRenderType() == 0 && j3 == l1 && l2 == i2 && i3 == j2) {
											renderblocks.setRenderFromInside(true);
											renderblocks.setRenderAllFaces(true);
											renderblocks.renderBlockByRenderType(block, j3, l2, i3);
											renderblocks.setRenderFromInside(false);
											renderblocks.setRenderAllFaces(false);
										}
									}
								}
							}
						}
					}

					if (flag1) {
						skipRenderPass[k2] = false;
					}

					if (flag2) {
						postRenderBlocks(k2, p_147892_1_);
					} else {
						flag1 = false;
					}

					if (!flag) {
						break;
					}
				}
				net.minecraftforge.client.ForgeHooksClient.setWorldRendererRB(null);
			}

			HashSet hashset1 = new HashSet();
			hashset1.addAll(tileEntityRenderers);
			hashset1.removeAll(hashset);
			tileEntities.addAll(hashset1);
			hashset.removeAll(tileEntityRenderers);
			tileEntities.removeAll(hashset);
			isChunkLit = Chunk.isLit;
			isInitialized = true;
		}
	}

	private void preRenderBlocks(int p_147890_1_) {
		GL11.glNewList(glRenderList + p_147890_1_, GL11.GL_COMPILE);
		GL11.glPushMatrix();
		setupGLTranslation();
		float f = 1.000001F;
		GL11.glTranslatef(-8.0F, -8.0F, -8.0F);
		GL11.glScalef(f, f, f);
		GL11.glTranslatef(8.0F, 8.0F, 8.0F);
		net.minecraftforge.client.ForgeHooksClient.onPreRenderWorld(this, p_147890_1_);
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setTranslation(-posX, -posY, -posZ);
	}

	private void postRenderBlocks(int p_147891_1_, EntityLivingBase p_147891_2_) {
		if (p_147891_1_ == 1 && !skipRenderPass[p_147891_1_]) {
			vertexState = Tessellator.instance.getVertexState((float) p_147891_2_.posX, (float) p_147891_2_.posY,
					(float) p_147891_2_.posZ);
		}

		bytesDrawn += Tessellator.instance.draw();
		net.minecraftforge.client.ForgeHooksClient.onPostRenderWorld(this, p_147891_1_);
		GL11.glPopMatrix();
		GL11.glEndList();
		Tessellator.instance.setTranslation(0.0D, 0.0D, 0.0D);
	}

	public void updateRendererSort(EntityLivingBase p_147889_1_) {
		if (vertexState != null && !skipRenderPass[1]) {
			preRenderBlocks(1);
			Tessellator.instance.setVertexState(vertexState);
			postRenderBlocks(1, p_147889_1_);
		}
	}

	public float distanceToEntitySquared(Entity p_78912_1_) {
		float f = (float) (p_78912_1_.posX - posXPlus);
		float f1 = (float) (p_78912_1_.posY - posYPlus);
		float f2 = (float) (p_78912_1_.posZ - posZPlus);
		return f * f + f1 * f1 + f2 * f2;
	}

	public void setDontDraw() {
		for (int i = 0; i < 2; ++i) {
			skipRenderPass[i] = true;
		}

		isInFrustum = false;
		isInitialized = false;
		vertexState = null;
	}

	public void stopRendering() {
		setDontDraw();
		worldObj = null;
	}

	public int getGLCallListForPass(int p_78909_1_) {
		return !isInFrustum ? -1 : !skipRenderPass[p_78909_1_] ? glRenderList + p_78909_1_ : -1;
	}

	public void updateInFrustum(ICamera p_78908_1_) {
		isInFrustum = p_78908_1_.isBoundingBoxInFrustum(rendererBoundingBox);
	}

	public void callOcclusionQueryList() {
		GL11.glCallList(glRenderList + 2);
	}

	public boolean skipAllRenderPasses() {
		return isInitialized && skipRenderPass[0] && skipRenderPass[1];
	}

	public void markDirty() {
		needsUpdate = true;
	}
}