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

package cpw.mods.fml.common.discovery;

import com.google.common.base.Predicate;
import com.google.common.collect.*;
import cpw.mods.fml.common.ModContainer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ASMDataTable {
	public final static class ASMData implements Cloneable {
		private final ModCandidate candidate;
		private final String annotationName;
		private final String className;
		private final String objectName;
		private Map<String, Object> annotationInfo;

		public ASMData(ModCandidate candidate, String annotationName, String className, String objectName,
				Map<String, Object> info) {
			this.candidate = candidate;
			this.annotationName = annotationName;
			this.className = className;
			this.objectName = objectName;
			annotationInfo = info;
		}

		public ModCandidate getCandidate() {
			return candidate;
		}

		public String getAnnotationName() {
			return annotationName;
		}

		public String getClassName() {
			return className;
		}

		public String getObjectName() {
			return objectName;
		}

		public Map<String, Object> getAnnotationInfo() {
			return annotationInfo;
		}

		public ASMData copy(Map<String, Object> newAnnotationInfo) {
			try {
				ASMData clone = (ASMData) clone();
				clone.annotationInfo = newAnnotationInfo;
				return clone;
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException("Unpossible", e);
			}
		}
	}

	private static class ModContainerPredicate implements Predicate<ASMData> {
		private final ModContainer container;

		public ModContainerPredicate(ModContainer container) {
			this.container = container;
		}

		@Override
		public boolean apply(ASMData data) {
			return container.getSource().equals(data.candidate.getModContainer());
		}
	}

	private final SetMultimap<String, ASMData> globalAnnotationData = HashMultimap.create();
	private Map<ModContainer, SetMultimap<String, ASMData>> containerAnnotationData;

	private final List<ModContainer> containers = Lists.newArrayList();
	private final SetMultimap<String, ModCandidate> packageMap = HashMultimap.create();

	public SetMultimap<String, ASMData> getAnnotationsFor(ModContainer container) {
		if (containerAnnotationData == null) {
			ImmutableMap.Builder<ModContainer, SetMultimap<String, ASMData>> mapBuilder = ImmutableMap
					.builder();
			for (ModContainer cont : containers) {
				Multimap<String, ASMData> values = Multimaps.filterValues(globalAnnotationData,
						new ModContainerPredicate(cont));
				mapBuilder.put(cont, ImmutableSetMultimap.copyOf(values));
			}
			containerAnnotationData = mapBuilder.build();
		}
		return containerAnnotationData.get(container);
	}

	public Set<ASMData> getAll(String annotation) {
		return globalAnnotationData.get(annotation);
	}

	public void addASMData(ModCandidate candidate, String annotation, String className, String objectName,
			Map<String, Object> annotationInfo) {
		globalAnnotationData.put(annotation, new ASMData(candidate, annotation, className, objectName, annotationInfo));
	}

	public void addContainer(ModContainer container) {
		containers.add(container);
	}

	public void registerPackage(ModCandidate modCandidate, String pkg) {
		packageMap.put(pkg, modCandidate);
	}

	public Set<ModCandidate> getCandidatesFor(String pkg) {
		return packageMap.get(pkg);
	}
}