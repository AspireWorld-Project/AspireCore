package net.minecraft.util;

import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class JsonSerializableSet extends ForwardingSet implements IJsonSerializable {
	private final Set underlyingSet = Sets.newHashSet();
	@SuppressWarnings("unchecked")
	@Override
	public void func_152753_a(JsonElement p_152753_1_) {
		if (p_152753_1_.isJsonArray()) {
			Iterator iterator = p_152753_1_.getAsJsonArray().iterator();

			while (iterator.hasNext()) {
				JsonElement jsonelement1 = (JsonElement) iterator.next();
				add(jsonelement1.getAsString());
			}
		}
	}

	@Override
	public JsonElement getSerializableElement() {
		JsonArray jsonarray = new JsonArray();
		Iterator iterator = iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			jsonarray.add(new JsonPrimitive(s));
		}

		return jsonarray;
	}

	@Override
	protected Set delegate() {
		return underlyingSet;
	}
}