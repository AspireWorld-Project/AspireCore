package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;

public class BlockPressurePlateWeighted extends BlockBasePressurePlate {
	private final int field_150068_a;
	protected BlockPressurePlateWeighted(String p_i45436_1_, Material p_i45436_2_, int p_i45436_3_) {
		super(p_i45436_1_, p_i45436_2_);
		field_150068_a = p_i45436_3_;
	}

	@Override
	protected int func_150065_e(World world, int x, int y, int z) {
		int l = 0;
		for (Object entityObject : world.getEntitiesWithinAABB(Entity.class, func_150061_a(x, y, z))) {
			Entity entity = (Entity) entityObject;
			Cancellable cancellable;
			if (entity instanceof EntityPlayer) {
				cancellable = CraftEventFactory.callPlayerInteractEvent((EntityPlayer) entity, Action.PHYSICAL, x, y, z,
						-1, null);
			} else {
				cancellable = new EntityInteractEvent(entity.getBukkitEntity(), world.getWorld().getBlockAt(x, y, z));
				Bukkit.getPluginManager().callEvent((EntityInteractEvent) cancellable);
			}
			if (!cancellable.isCancelled()) {
				l++;
			}
		}
		l = Math.min(l, field_150068_a);
		if (l <= 0)
			return 0;
		float f = Math.min(field_150068_a, l) / field_150068_a;
		return MathHelper.ceiling_float_int(f * 15.0F);
	}

	@Override
	protected int func_150060_c(int p_150060_1_) {
		return p_150060_1_;
	}

	@Override
	protected int func_150066_d(int p_150066_1_) {
		return p_150066_1_;
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return 10;
	}
}