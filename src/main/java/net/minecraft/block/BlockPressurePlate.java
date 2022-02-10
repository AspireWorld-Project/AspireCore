package net.minecraft.block;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockPressurePlate extends BlockBasePressurePlate {
	private BlockPressurePlate.Sensitivity field_150069_a;
	private static final String __OBFID = "CL_00000289";

	protected BlockPressurePlate(String p_i45418_1_, Material p_i45418_2_, BlockPressurePlate.Sensitivity p_i45418_3_) {
		super(p_i45418_1_, p_i45418_2_);
		field_150069_a = p_i45418_3_;
	}

	@Override
	protected int func_150066_d(int p_150066_1_) {
		return p_150066_1_ > 0 ? 1 : 0;
	}

	@Override
	protected int func_150060_c(int p_150060_1_) {
		return p_150060_1_ == 1 ? 15 : 0;
	}

	@Override
	protected int func_150065_e(World world, int x, int y, int z) {
		List entityList = null;
		if (field_150069_a == BlockPressurePlate.Sensitivity.everything) {
			entityList = world.getEntitiesWithinAABBExcludingEntity(null, func_150061_a(x, y, z));
		}
		if (field_150069_a == BlockPressurePlate.Sensitivity.mobs) {
			entityList = world.getEntitiesWithinAABB(EntityLivingBase.class, func_150061_a(x, y, z));
		}
		if (field_150069_a == BlockPressurePlate.Sensitivity.players) {
			entityList = world.getEntitiesWithinAABB(EntityPlayer.class, func_150061_a(x, y, z));
		}
		if (entityList != null && !entityList.isEmpty()) {
			for (Object entityObject : entityList) {
				Entity entity = (Entity) entityObject;
				if (func_150060_c(world.getBlockMetadata(x, y, z)) == 0) {
					Cancellable cancellable;
					if (entity instanceof EntityPlayer) {
						cancellable = CraftEventFactory.callPlayerInteractEvent((EntityPlayer) entity, Action.PHYSICAL,
								x, y, z, -1, null);
					} else {
						cancellable = new EntityInteractEvent(entity.getBukkitEntity(),
								world.getWorld().getBlockAt(x, y, z));
						Bukkit.getPluginManager().callEvent((EntityInteractEvent) cancellable);
					}
					if (cancellable.isCancelled()) {
						continue;
					}
				}
				if (!entity.doesEntityNotTriggerPressurePlate())
					return 15;
			}
		}
		return 0;
	}

	public static enum Sensitivity {
		everything, mobs, players;

		private static final String __OBFID = "CL_00000290";
	}
}