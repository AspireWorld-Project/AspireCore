package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.server.management.LowerStringMap;

public class ServersideAttributeMap extends BaseAttributeMap {
	private final Set attributeInstanceSet = Sets.newHashSet();
	protected final Map descriptionToAttributeInstanceMap = new LowerStringMap();
	private static final String __OBFID = "CL_00001569";

	@Override
	public ModifiableAttributeInstance getAttributeInstance(IAttribute p_111151_1_) {
		return (ModifiableAttributeInstance) super.getAttributeInstance(p_111151_1_);
	}

	@Override
	public ModifiableAttributeInstance getAttributeInstanceByName(String p_111152_1_) {
		IAttributeInstance iattributeinstance = super.getAttributeInstanceByName(p_111152_1_);

		if (iattributeinstance == null) {
			iattributeinstance = (IAttributeInstance) descriptionToAttributeInstanceMap.get(p_111152_1_);
		}

		return (ModifiableAttributeInstance) iattributeinstance;
	}

	@Override
	public IAttributeInstance registerAttribute(IAttribute p_111150_1_) {
		if (attributesByName.containsKey(p_111150_1_.getAttributeUnlocalizedName()))
			throw new IllegalArgumentException("Attribute is already registered!");
		else {
			ModifiableAttributeInstance modifiableattributeinstance = new ModifiableAttributeInstance(this,
					p_111150_1_);
			attributesByName.put(p_111150_1_.getAttributeUnlocalizedName(), modifiableattributeinstance);

			if (p_111150_1_ instanceof RangedAttribute && ((RangedAttribute) p_111150_1_).getDescription() != null) {
				descriptionToAttributeInstanceMap.put(((RangedAttribute) p_111150_1_).getDescription(),
						modifiableattributeinstance);
			}

			attributes.put(p_111150_1_, modifiableattributeinstance);
			return modifiableattributeinstance;
		}
	}

	@Override
	public void addAttributeInstance(ModifiableAttributeInstance p_111149_1_) {
		if (p_111149_1_.getAttribute().getShouldWatch()) {
			attributeInstanceSet.add(p_111149_1_);
		}
	}

	public Set getAttributeInstanceSet() {
		return attributeInstanceSet;
	}

	public Collection getWatchedAttributes() {
		HashSet hashset = Sets.newHashSet();
		Iterator iterator = getAllAttributes().iterator();

		while (iterator.hasNext()) {
			IAttributeInstance iattributeinstance = (IAttributeInstance) iterator.next();

			if (iattributeinstance.getAttribute().getShouldWatch()) {
				hashset.add(iattributeinstance);
			}
		}

		return hashset;
	}
}