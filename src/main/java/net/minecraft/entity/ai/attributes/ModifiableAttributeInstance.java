package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Maps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.*;

public class ModifiableAttributeInstance implements IAttributeInstance {
	private final BaseAttributeMap attributeMap;
	private final IAttribute genericAttribute;
	private final Map mapByOperation = Maps.newHashMap();
	private final Map mapByName = Maps.newHashMap();
	private final Map mapByUUID = Maps.newHashMap();
	private double baseValue;
	private boolean needsUpdate = true;
	private double cachedValue;
	private static final String __OBFID = "CL_00001567";

	public ModifiableAttributeInstance(BaseAttributeMap p_i1608_1_, IAttribute p_i1608_2_) {
		attributeMap = p_i1608_1_;
		genericAttribute = p_i1608_2_;
		baseValue = p_i1608_2_.getDefaultValue();

		for (int i = 0; i < 3; ++i) {
			mapByOperation.put(Integer.valueOf(i), new HashSet());
		}
	}

	@Override
	public IAttribute getAttribute() {
		return genericAttribute;
	}

	@Override
	public double getBaseValue() {
		return baseValue;
	}

	@Override
	public void setBaseValue(double p_111128_1_) {
		if (p_111128_1_ != getBaseValue()) {
			baseValue = p_111128_1_;
			flagForUpdate();
		}
	}

	public Collection getModifiersByOperation(int p_111130_1_) {
		return (Collection) mapByOperation.get(Integer.valueOf(p_111130_1_));
	}

	@Override
	public Collection func_111122_c() {
		HashSet hashset = new HashSet();

		for (int i = 0; i < 3; ++i) {
			hashset.addAll(getModifiersByOperation(i));
		}

		return hashset;
	}

	@Override
	public AttributeModifier getModifier(UUID p_111127_1_) {
		return (AttributeModifier) mapByUUID.get(p_111127_1_);
	}

	@Override
	public void applyModifier(AttributeModifier p_111121_1_) {
		if (getModifier(p_111121_1_.getID()) != null)
			throw new IllegalArgumentException("Modifier is already applied on this attribute!");
		else {
			Object object = mapByName.get(p_111121_1_.getName());

			if (object == null) {
				object = new HashSet();
				mapByName.put(p_111121_1_.getName(), object);
			}

			((Set) mapByOperation.get(Integer.valueOf(p_111121_1_.getOperation()))).add(p_111121_1_);
			((Set) object).add(p_111121_1_);
			mapByUUID.put(p_111121_1_.getID(), p_111121_1_);
			flagForUpdate();
		}
	}

	private void flagForUpdate() {
		needsUpdate = true;
		attributeMap.addAttributeInstance(this);
	}

	@Override
	public void removeModifier(AttributeModifier p_111124_1_) {
		for (int i = 0; i < 3; ++i) {
			Set set = (Set) mapByOperation.get(Integer.valueOf(i));
			set.remove(p_111124_1_);
		}

		Set set1 = (Set) mapByName.get(p_111124_1_.getName());

		if (set1 != null) {
			set1.remove(p_111124_1_);

			if (set1.isEmpty()) {
				mapByName.remove(p_111124_1_.getName());
			}
		}

		mapByUUID.remove(p_111124_1_.getID());
		flagForUpdate();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void removeAllModifiers() {
		Collection collection = func_111122_c();

		if (collection != null) {
			ArrayList arraylist = new ArrayList(collection);
			Iterator iterator = arraylist.iterator();

			while (iterator.hasNext()) {
				AttributeModifier attributemodifier = (AttributeModifier) iterator.next();
				removeModifier(attributemodifier);
			}
		}
	}

	@Override
	public double getAttributeValue() {
		if (needsUpdate) {
			cachedValue = computeValue();
			needsUpdate = false;
		}

		return cachedValue;
	}

	private double computeValue() {
		double d0 = getBaseValue();
		AttributeModifier attributemodifier;

		for (Iterator iterator = getModifiersByOperation(0).iterator(); iterator
				.hasNext(); d0 += attributemodifier.getAmount()) {
			attributemodifier = (AttributeModifier) iterator.next();
		}

		double d1 = d0;
		Iterator iterator1;
		AttributeModifier attributemodifier1;

		for (iterator1 = getModifiersByOperation(1).iterator(); iterator1
				.hasNext(); d1 += d0 * attributemodifier1.getAmount()) {
			attributemodifier1 = (AttributeModifier) iterator1.next();
		}

		for (iterator1 = getModifiersByOperation(2).iterator(); iterator1
				.hasNext(); d1 *= 1.0D + attributemodifier1.getAmount()) {
			attributemodifier1 = (AttributeModifier) iterator1.next();
		}

		return genericAttribute.clampValue(d1);
	}
}