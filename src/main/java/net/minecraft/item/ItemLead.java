package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.event.hanging.HangingPlaceEvent;

import java.util.List;

public class ItemLead extends Item {
	private static final String __OBFID = "CL_00000045";

	public ItemLead() {
		setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_,
			int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		Block block = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);

		if (block.getRenderType() == 11) {
			if (p_77648_3_.isRemote)
				return true;
			else {
				func_150909_a(p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_);
				return true;
			}
		} else
			return false;
	}

	public static boolean func_150909_a(EntityPlayer player, World world, int x, int y, int z) {
		EntityLeashKnot entityLeashKnot = EntityLeashKnot.getKnotForBlock(world, x, y, z);
		double d0 = 7.0D;
		boolean flag = false;
		List list = world.getEntitiesWithinAABB(EntityLiving.class,
				AxisAlignedBB.getBoundingBox(x - d0, y - d0, z - d0, x + d0, y + d0, z + d0));
		if (list != null) {
			for (Object entityLivingObject : list) {
				EntityLiving entityLiving = (EntityLiving) entityLivingObject;
				if (entityLiving.getLeashed() && entityLiving.getLeashedToEntity() == player) {
					if (entityLeashKnot == null) {
						entityLeashKnot = EntityLeashKnot.func_110129_a(world, x, y, z);
						HangingPlaceEvent event = new HangingPlaceEvent((Hanging) entityLeashKnot.getBukkitEntity(),
								player != null ? (Player) player.getBukkitEntity() : null,
								world.getWorld().getBlockAt(x, y, z), BlockFace.SELF);
						Bukkit.getPluginManager().callEvent(event);
						if (event.isCancelled()) {
							entityLeashKnot.setDead();
							return false;
						}
					}
					// TODO: callPlayerLeashEntityEvent
					entityLiving.setLeashedToEntity(entityLeashKnot, true);
					flag = true;
				}
			}
		}
		return flag;
	}
}