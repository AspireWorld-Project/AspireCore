/*
 * Forge Mod Loader
 * Copyright (c) 2012-2013 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 *     cpw - implementation
 */

package cpw.mods.fml.common;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class MetadataCollection {
	@SuppressWarnings("unused")
	private String modListVersion;
	private ModMetadata[] modList;
	private Map<String, ModMetadata> metadatas = Maps.newHashMap();

	public static MetadataCollection from(InputStream inputStream, String sourceName) {
		if (inputStream == null)
			return new MetadataCollection();

		InputStreamReader reader = new InputStreamReader(inputStream);
		try {
			MetadataCollection collection;
			Gson gson = new GsonBuilder().registerTypeAdapter(ArtifactVersion.class, new ArtifactVersionAdapter())
					.create();
			JsonParser parser = new JsonParser();
			JsonElement rootElement = parser.parse(reader);
			if (rootElement.isJsonArray()) {
				collection = new MetadataCollection();
				JsonArray jsonList = rootElement.getAsJsonArray();
				collection.modList = new ModMetadata[jsonList.size()];
				int i = 0;
				for (JsonElement mod : jsonList) {
					collection.modList[i++] = gson.fromJson(mod, ModMetadata.class);
				}
			} else {
				collection = gson.fromJson(rootElement, MetadataCollection.class);
			}
			collection.parseModMetadataList();
			return collection;
		} catch (JsonParseException e) {
			FMLLog.log(Level.ERROR, e, "The mcmod.info file in %s cannot be parsed as valid JSON. It will be ignored",
					sourceName);
			return new MetadataCollection();
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	private void parseModMetadataList() {
		for (ModMetadata modMetadata : modList) {
			metadatas.put(modMetadata.modId, modMetadata);
		}
	}

	public ModMetadata getMetadataForId(String modId, Map<String, Object> extraData) {
		if (!metadatas.containsKey(modId)) {
			ModMetadata dummy = new ModMetadata();
			dummy.modId = modId;
			dummy.name = (String) extraData.get("name");
			dummy.version = (String) extraData.get("version");
			dummy.autogenerated = true;
			metadatas.put(modId, dummy);
		}
		return metadatas.get(modId);
	}

	public static class ArtifactVersionAdapter extends TypeAdapter<ArtifactVersion> {

		@Override
		public void write(JsonWriter out, ArtifactVersion value) throws IOException {
			// no op - we never write these out
		}

		@Override
		public ArtifactVersion read(JsonReader in) throws IOException {
			return VersionParser.parseVersionReference(in.nextString());
		}

	}
}