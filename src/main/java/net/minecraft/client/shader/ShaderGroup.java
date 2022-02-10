package net.minecraft.client.shader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.vecmath.Matrix4f;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ShaderGroup {
	private final Framebuffer mainFramebuffer;
	private final IResourceManager resourceManager;
	private final String shaderGroupName;
	private final List listShaders = Lists.newArrayList();
	private final Map mapFramebuffers = Maps.newHashMap();
	private final List listFramebuffers = Lists.newArrayList();
	private Matrix4f projectionMatrix;
	private int mainFramebufferWidth;
	private int mainFramebufferHeight;
	private float field_148036_j;
	private float field_148037_k;
	private static final String __OBFID = "CL_00001041";

	public ShaderGroup(TextureManager p_i1050_1_, IResourceManager p_i1050_2_, Framebuffer p_i1050_3_,
			ResourceLocation p_i1050_4_) throws JsonException {
		resourceManager = p_i1050_2_;
		mainFramebuffer = p_i1050_3_;
		field_148036_j = 0.0F;
		field_148037_k = 0.0F;
		mainFramebufferWidth = p_i1050_3_.framebufferWidth;
		mainFramebufferHeight = p_i1050_3_.framebufferHeight;
		shaderGroupName = p_i1050_4_.toString();
		resetProjectionMatrix();
		func_152765_a(p_i1050_1_, p_i1050_4_);
	}

	public void func_152765_a(TextureManager p_152765_1_, ResourceLocation p_152765_2_) throws JsonException {
		JsonParser jsonparser = new JsonParser();
		InputStream inputstream = null;

		try {
			IResource iresource = resourceManager.getResource(p_152765_2_);
			inputstream = iresource.getInputStream();
			JsonObject jsonobject = jsonparser.parse(IOUtils.toString(inputstream, Charsets.UTF_8)).getAsJsonObject();
			JsonArray jsonarray;
			int i;
			Iterator iterator;
			JsonElement jsonelement;
			JsonException jsonexception1;

			if (JsonUtils.jsonObjectFieldTypeIsArray(jsonobject, "targets")) {
				jsonarray = jsonobject.getAsJsonArray("targets");
				i = 0;

				for (iterator = jsonarray.iterator(); iterator.hasNext(); ++i) {
					jsonelement = (JsonElement) iterator.next();

					try {
						initTarget(jsonelement);
					} catch (Exception exception1) {
						jsonexception1 = JsonException.func_151379_a(exception1);
						jsonexception1.func_151380_a("targets[" + i + "]");
						throw jsonexception1;
					}
				}
			}

			if (JsonUtils.jsonObjectFieldTypeIsArray(jsonobject, "passes")) {
				jsonarray = jsonobject.getAsJsonArray("passes");
				i = 0;

				for (iterator = jsonarray.iterator(); iterator.hasNext(); ++i) {
					jsonelement = (JsonElement) iterator.next();

					try {
						func_152764_a(p_152765_1_, jsonelement);
					} catch (Exception exception) {
						jsonexception1 = JsonException.func_151379_a(exception);
						jsonexception1.func_151380_a("passes[" + i + "]");
						throw jsonexception1;
					}
				}
			}
		} catch (Exception exception2) {
			JsonException jsonexception = JsonException.func_151379_a(exception2);
			jsonexception.func_151381_b(p_152765_2_.getResourcePath());
			throw jsonexception;
		} finally {
			IOUtils.closeQuietly(inputstream);
		}
	}

	private void initTarget(JsonElement p_148027_1_) throws JsonException {
		if (JsonUtils.jsonElementTypeIsString(p_148027_1_)) {
			addFramebuffer(p_148027_1_.getAsString(), mainFramebufferWidth, mainFramebufferHeight);
		} else {
			JsonObject jsonobject = JsonUtils.getJsonElementAsJsonObject(p_148027_1_, "target");
			String s = JsonUtils.getJsonObjectStringFieldValue(jsonobject, "name");
			int i = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "width", mainFramebufferWidth);
			int j = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "height", mainFramebufferHeight);

			if (mapFramebuffers.containsKey(s))
				throw new JsonException(s + " is already defined");

			addFramebuffer(s, i, j);
		}
	}

	private void func_152764_a(TextureManager p_152764_1_, JsonElement p_152764_2_) throws JsonException {
		JsonObject jsonobject = JsonUtils.getJsonElementAsJsonObject(p_152764_2_, "pass");
		String s = JsonUtils.getJsonObjectStringFieldValue(jsonobject, "name");
		String s1 = JsonUtils.getJsonObjectStringFieldValue(jsonobject, "intarget");
		String s2 = JsonUtils.getJsonObjectStringFieldValue(jsonobject, "outtarget");
		Framebuffer framebuffer = getFramebuffer(s1);
		Framebuffer framebuffer1 = getFramebuffer(s2);

		if (framebuffer == null)
			throw new JsonException("Input target \'" + s1 + "\' does not exist");
		else if (framebuffer1 == null)
			throw new JsonException("Output target \'" + s2 + "\' does not exist");
		else {
			Shader shader = addShader(s, framebuffer, framebuffer1);
			JsonArray jsonarray = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(jsonobject, "auxtargets",
					(JsonArray) null);

			if (jsonarray != null) {
				int i = 0;

				for (Iterator iterator = jsonarray.iterator(); iterator.hasNext(); ++i) {
					JsonElement jsonelement1 = (JsonElement) iterator.next();

					try {
						JsonObject jsonobject1 = JsonUtils.getJsonElementAsJsonObject(jsonelement1, "auxtarget");
						String s4 = JsonUtils.getJsonObjectStringFieldValue(jsonobject1, "name");
						String s3 = JsonUtils.getJsonObjectStringFieldValue(jsonobject1, "id");
						Framebuffer framebuffer2 = getFramebuffer(s3);

						if (framebuffer2 == null) {
							ResourceLocation resourcelocation = new ResourceLocation("textures/effect/" + s3 + ".png");

							try {
								resourceManager.getResource(resourcelocation);
							} catch (FileNotFoundException filenotfoundexception) {
								throw new JsonException("Render target or texture \'" + s3 + "\' does not exist");
							}

							p_152764_1_.bindTexture(resourcelocation);
							ITextureObject itextureobject = p_152764_1_.getTexture(resourcelocation);
							int j = JsonUtils.getJsonObjectIntegerFieldValue(jsonobject1, "width");
							int k = JsonUtils.getJsonObjectIntegerFieldValue(jsonobject1, "height");
							boolean flag = JsonUtils.getJsonObjectBooleanFieldValue(jsonobject1, "bilinear");

							if (flag) {
								GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
								GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
							} else {
								GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
								GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
							}

							shader.addAuxFramebuffer(s4, Integer.valueOf(itextureobject.getGlTextureId()), j, k);
						} else {
							shader.addAuxFramebuffer(s4, framebuffer2, framebuffer2.framebufferTextureWidth,
									framebuffer2.framebufferTextureHeight);
						}
					} catch (Exception exception1) {
						JsonException jsonexception = JsonException.func_151379_a(exception1);
						jsonexception.func_151380_a("auxtargets[" + i + "]");
						throw jsonexception;
					}
				}
			}

			JsonArray jsonarray1 = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(jsonobject, "uniforms",
					(JsonArray) null);

			if (jsonarray1 != null) {
				int l = 0;

				for (Iterator iterator1 = jsonarray1.iterator(); iterator1.hasNext(); ++l) {
					JsonElement jsonelement2 = (JsonElement) iterator1.next();

					try {
						initUniform(jsonelement2);
					} catch (Exception exception) {
						JsonException jsonexception1 = JsonException.func_151379_a(exception);
						jsonexception1.func_151380_a("uniforms[" + l + "]");
						throw jsonexception1;
					}
				}
			}
		}
	}

	private void initUniform(JsonElement p_148028_1_) throws JsonException {
		JsonObject jsonobject = JsonUtils.getJsonElementAsJsonObject(p_148028_1_, "uniform");
		String s = JsonUtils.getJsonObjectStringFieldValue(jsonobject, "name");
		ShaderUniform shaderuniform = ((Shader) listShaders.get(listShaders.size() - 1)).getShaderManager()
				.func_147991_a(s);

		if (shaderuniform == null)
			throw new JsonException("Uniform \'" + s + "\' does not exist");
		else {
			float[] afloat = new float[4];
			int i = 0;
			JsonArray jsonarray = JsonUtils.getJsonObjectJsonArrayField(jsonobject, "values");

			for (Iterator iterator = jsonarray.iterator(); iterator.hasNext(); ++i) {
				JsonElement jsonelement1 = (JsonElement) iterator.next();

				try {
					afloat[i] = JsonUtils.getJsonElementFloatValue(jsonelement1, "value");
				} catch (Exception exception) {
					JsonException jsonexception = JsonException.func_151379_a(exception);
					jsonexception.func_151380_a("values[" + i + "]");
					throw jsonexception;
				}
			}

			switch (i) {
			case 0:
			default:
				break;
			case 1:
				shaderuniform.func_148090_a(afloat[0]);
				break;
			case 2:
				shaderuniform.func_148087_a(afloat[0], afloat[1]);
				break;
			case 3:
				shaderuniform.func_148095_a(afloat[0], afloat[1], afloat[2]);
				break;
			case 4:
				shaderuniform.func_148081_a(afloat[0], afloat[1], afloat[2], afloat[3]);
			}
		}
	}

	public void addFramebuffer(String p_148020_1_, int p_148020_2_, int p_148020_3_) {
		Framebuffer framebuffer = new Framebuffer(p_148020_2_, p_148020_3_, true);
		framebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
		mapFramebuffers.put(p_148020_1_, framebuffer);

		if (p_148020_2_ == mainFramebufferWidth && p_148020_3_ == mainFramebufferHeight) {
			listFramebuffers.add(framebuffer);
		}
	}

	public void deleteShaderGroup() {
		Iterator iterator = mapFramebuffers.values().iterator();

		while (iterator.hasNext()) {
			Framebuffer framebuffer = (Framebuffer) iterator.next();
			framebuffer.deleteFramebuffer();
		}

		iterator = listShaders.iterator();

		while (iterator.hasNext()) {
			Shader shader = (Shader) iterator.next();
			shader.deleteShader();
		}

		listShaders.clear();
	}

	public Shader addShader(String p_148023_1_, Framebuffer p_148023_2_, Framebuffer p_148023_3_) throws JsonException {
		Shader shader = new Shader(resourceManager, p_148023_1_, p_148023_2_, p_148023_3_);
		listShaders.add(listShaders.size(), shader);
		return shader;
	}

	private void resetProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		projectionMatrix.setIdentity();
		projectionMatrix.m00 = 2.0F / mainFramebuffer.framebufferTextureWidth;
		projectionMatrix.m11 = 2.0F / -mainFramebuffer.framebufferTextureHeight;
		projectionMatrix.m22 = -0.0020001999F;
		projectionMatrix.m33 = 1.0F;
		projectionMatrix.m03 = -1.0F;
		projectionMatrix.m13 = 1.0F;
		projectionMatrix.m23 = -1.0001999F;
	}

	public void createBindFramebuffers(int p_148026_1_, int p_148026_2_) {
		mainFramebufferWidth = mainFramebuffer.framebufferTextureWidth;
		mainFramebufferHeight = mainFramebuffer.framebufferTextureHeight;
		resetProjectionMatrix();
		Iterator iterator = listShaders.iterator();

		while (iterator.hasNext()) {
			Shader shader = (Shader) iterator.next();
			shader.setProjectionMatrix(projectionMatrix);
		}

		iterator = listFramebuffers.iterator();

		while (iterator.hasNext()) {
			Framebuffer framebuffer = (Framebuffer) iterator.next();
			framebuffer.createBindFramebuffer(p_148026_1_, p_148026_2_);
		}
	}

	public void loadShaderGroup(float p_148018_1_) {
		if (p_148018_1_ < field_148037_k) {
			field_148036_j += 1.0F - field_148037_k;
			field_148036_j += p_148018_1_;
		} else {
			field_148036_j += p_148018_1_ - field_148037_k;
		}

		for (field_148037_k = p_148018_1_; field_148036_j > 20.0F; field_148036_j -= 20.0F) {
			;
		}

		Iterator iterator = listShaders.iterator();

		while (iterator.hasNext()) {
			Shader shader = (Shader) iterator.next();
			shader.loadShader(field_148036_j / 20.0F);
		}
	}

	public final String getShaderGroupName() {
		return shaderGroupName;
	}

	private Framebuffer getFramebuffer(String p_148017_1_) {
		return p_148017_1_ == null ? null
				: p_148017_1_.equals("minecraft:main") ? mainFramebuffer
						: (Framebuffer) mapFramebuffers.get(p_148017_1_);
	}
}