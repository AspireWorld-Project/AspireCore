package net.minecraft.util;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class RegistrySimple implements IRegistry {
	private static final Logger logger = LogManager.getLogger();
	protected final Map registryObjects = createUnderlyingMap();
	private static final String __OBFID = "CL_00001210";

	protected Map createUnderlyingMap() {
		return Maps.newHashMap();
	}

	@Override
	public Object getObject(Object p_82594_1_) {
		return registryObjects.get(p_82594_1_);
	}

	@Override
	public void putObject(Object p_82595_1_, Object p_82595_2_) {
		if (registryObjects.containsKey(p_82595_1_)) {
			logger.debug("Adding duplicate key '" + p_82595_1_ + "' to registry");
		}

		registryObjects.put(p_82595_1_, p_82595_2_);
	}

	public Set getKeys() {
		return Collections.unmodifiableSet(registryObjects.keySet());
	}

	public boolean containsKey(Object p_148741_1_) {
		return registryObjects.containsKey(p_148741_1_);
	}
}