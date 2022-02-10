package net.minecraft.entity.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.profiler.Profiler;

public class EntityAITasks {
	private static final Logger logger = LogManager.getLogger();
	public List taskEntries = new ArrayList();
	private List executingTaskEntries = new ArrayList();
	private final Profiler theProfiler;
	private int tickCount;
	private int tickRate = 3;
	private static final String __OBFID = "CL_00001588";

	public EntityAITasks(Profiler p_i1628_1_) {
		theProfiler = p_i1628_1_;
	}

	public void addTask(int p_75776_1_, EntityAIBase p_75776_2_) {
		taskEntries.add(new EntityAITasks.EntityAITaskEntry(p_75776_1_, p_75776_2_));
	}

	public void removeTask(EntityAIBase p_85156_1_) {
		Iterator iterator = taskEntries.iterator();

		while (iterator.hasNext()) {
			EntityAITasks.EntityAITaskEntry entityaitaskentry = (EntityAITasks.EntityAITaskEntry) iterator.next();
			EntityAIBase entityaibase1 = entityaitaskentry.action;

			if (entityaibase1 == p_85156_1_) {
				if (executingTaskEntries.contains(entityaitaskentry)) {
					entityaibase1.resetTask();
					executingTaskEntries.remove(entityaitaskentry);
				}

				iterator.remove();
			}
		}
	}

	public void onUpdateTasks() {
		ArrayList arraylist = new ArrayList();
		Iterator iterator;
		EntityAITasks.EntityAITaskEntry entityaitaskentry;

		if (tickCount++ % tickRate == 0) {
			iterator = taskEntries.iterator();

			while (iterator.hasNext()) {
				entityaitaskentry = (EntityAITasks.EntityAITaskEntry) iterator.next();
				boolean flag = executingTaskEntries.contains(entityaitaskentry);

				if (flag) {
					if (canUse(entityaitaskentry) && canContinue(entityaitaskentry)) {
						continue;
					}

					entityaitaskentry.action.resetTask();
					executingTaskEntries.remove(entityaitaskentry);
				}

				if (canUse(entityaitaskentry) && entityaitaskentry.action.shouldExecute()) {
					arraylist.add(entityaitaskentry);
					executingTaskEntries.add(entityaitaskentry);
				}
			}
		} else {
			iterator = executingTaskEntries.iterator();

			while (iterator.hasNext()) {
				entityaitaskentry = (EntityAITasks.EntityAITaskEntry) iterator.next();

				if (!entityaitaskentry.action.continueExecuting()) {
					entityaitaskentry.action.resetTask();
					iterator.remove();
				}
			}
		}

		theProfiler.startSection("goalStart");
		iterator = arraylist.iterator();

		while (iterator.hasNext()) {
			entityaitaskentry = (EntityAITasks.EntityAITaskEntry) iterator.next();
			theProfiler.startSection(entityaitaskentry.action.getClass().getSimpleName());
			entityaitaskentry.action.startExecuting();
			theProfiler.endSection();
		}

		theProfiler.endSection();
		theProfiler.startSection("goalTick");
		iterator = executingTaskEntries.iterator();

		while (iterator.hasNext()) {
			entityaitaskentry = (EntityAITasks.EntityAITaskEntry) iterator.next();
			entityaitaskentry.action.updateTask();
		}

		theProfiler.endSection();
	}

	private boolean canContinue(EntityAITasks.EntityAITaskEntry p_75773_1_) {
		theProfiler.startSection("canContinue");
		boolean flag = p_75773_1_.action.continueExecuting();
		theProfiler.endSection();
		return flag;
	}

	private boolean canUse(EntityAITasks.EntityAITaskEntry p_75775_1_) {
		theProfiler.startSection("canUse");
		Iterator iterator = taskEntries.iterator();

		while (iterator.hasNext()) {
			EntityAITasks.EntityAITaskEntry entityaitaskentry = (EntityAITasks.EntityAITaskEntry) iterator.next();

			if (entityaitaskentry != p_75775_1_) {
				if (p_75775_1_.priority >= entityaitaskentry.priority) {
					if (executingTaskEntries.contains(entityaitaskentry)
							&& !areTasksCompatible(p_75775_1_, entityaitaskentry)) {
						theProfiler.endSection();
						return false;
					}
				} else if (executingTaskEntries.contains(entityaitaskentry)
						&& !entityaitaskentry.action.isInterruptible()) {
					theProfiler.endSection();
					return false;
				}
			}
		}

		theProfiler.endSection();
		return true;
	}

	private boolean areTasksCompatible(EntityAITasks.EntityAITaskEntry p_75777_1_,
			EntityAITasks.EntityAITaskEntry p_75777_2_) {
		return (p_75777_1_.action.getMutexBits() & p_75777_2_.action.getMutexBits()) == 0;
	}

	public class EntityAITaskEntry {
		public EntityAIBase action;
		public int priority;
		private static final String __OBFID = "CL_00001589";

		public EntityAITaskEntry(int p_i1627_2_, EntityAIBase p_i1627_3_) {
			priority = p_i1627_2_;
			action = p_i1627_3_;
		}
	}
}