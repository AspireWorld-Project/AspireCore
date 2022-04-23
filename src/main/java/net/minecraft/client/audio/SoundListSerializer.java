package net.minecraft.client.audio;

import com.google.gson.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.JsonUtils;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Type;

@SuppressWarnings("rawtypes")
@SideOnly(Side.CLIENT)
public class SoundListSerializer implements JsonDeserializer {
	@SuppressWarnings("unchecked")
	@Override
	public SoundList deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_,
			JsonDeserializationContext p_deserialize_3_) {
		JsonObject jsonobject = JsonUtils.getJsonElementAsJsonObject(p_deserialize_1_, "entry");
		SoundList soundlist = new SoundList();
		soundlist.setReplaceExisting(JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject, "replace", false));
		SoundCategory soundcategory = SoundCategory.func_147154_a(JsonUtils.getJsonObjectStringFieldValueOrDefault(
				jsonobject, "category", SoundCategory.MASTER.getCategoryName()));
		soundlist.setSoundCategory(soundcategory);
		Validate.notNull(soundcategory, "Invalid category");

		if (jsonobject.has("sounds")) {
			JsonArray jsonarray = JsonUtils.getJsonObjectJsonArrayField(jsonobject, "sounds");

			for (int i = 0; i < jsonarray.size(); ++i) {
				JsonElement jsonelement1 = jsonarray.get(i);
				SoundList.SoundEntry soundentry = new SoundList.SoundEntry();

				if (JsonUtils.jsonElementTypeIsString(jsonelement1)) {
					soundentry.setSoundEntryName(JsonUtils.getJsonElementStringValue(jsonelement1, "sound"));
				} else {
					JsonObject jsonobject1 = JsonUtils.getJsonElementAsJsonObject(jsonelement1, "sound");
					soundentry.setSoundEntryName(JsonUtils.getJsonObjectStringFieldValue(jsonobject1, "name"));

					if (jsonobject1.has("type")) {
						SoundList.SoundEntry.Type type1 = SoundList.SoundEntry.Type
								.getType(JsonUtils.getJsonObjectStringFieldValue(jsonobject1, "type"));
						Validate.notNull(type1, "Invalid type");
						soundentry.setSoundEntryType(type1);
					}

					float f;

					if (jsonobject1.has("volume")) {
						f = JsonUtils.getJsonObjectFloatFieldValue(jsonobject1, "volume");
						Validate.isTrue(f > 0.0F, "Invalid volume");
						soundentry.setSoundEntryVolume(f);
					}

					if (jsonobject1.has("pitch")) {
						f = JsonUtils.getJsonObjectFloatFieldValue(jsonobject1, "pitch");
						Validate.isTrue(f > 0.0F, "Invalid pitch");
						soundentry.setSoundEntryPitch(f);
					}

					if (jsonobject1.has("weight")) {
						int j = JsonUtils.getJsonObjectIntegerFieldValue(jsonobject1, "weight");
						Validate.isTrue(j > 0, "Invalid weight");
						soundentry.setSoundEntryWeight(j);
					}

					if (jsonobject1.has("stream")) {
						soundentry.setStreaming(JsonUtils.getJsonObjectBooleanFieldValue(jsonobject1, "stream"));
					}
				}

				soundlist.getSoundList().add(soundentry);
			}
		}

		return soundlist;
	}
}