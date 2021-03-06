package net.minecraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.entity.Fish;
import org.bukkit.event.player.PlayerFishEvent;

public class ItemFishingRod extends Item {
	@SideOnly(Side.CLIENT)
	private IIcon theIcon;
	private static final String __OBFID = "CL_00000034";

	public ItemFishingRod() {
		setMaxDamage(64);
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldRotateAroundWhenRendering() {
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
		if (p_77659_3_.fishEntity != null) {
			int i = p_77659_3_.fishEntity.func_146034_e();
			p_77659_1_.damageItem(i, p_77659_3_);
			p_77659_3_.swingItem();
		} else {
			EntityFishHook hook = new EntityFishHook(p_77659_2_, p_77659_3_);
			PlayerFishEvent playerFishEvent = new PlayerFishEvent(
					(org.bukkit.entity.Player) p_77659_3_.getBukkitEntity(), null, (Fish) hook.getBukkitEntity(),
					PlayerFishEvent.State.FISHING);
			Bukkit.getPluginManager().callEvent(playerFishEvent);
			if (playerFishEvent.isCancelled())
				return p_77659_1_;

			p_77659_2_.playSoundAtEntity(p_77659_3_, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

			if (!p_77659_2_.isRemote) {
				p_77659_2_.spawnEntityInWorld(new EntityFishHook(p_77659_2_, p_77659_3_));
			}

			p_77659_3_.swingItem();
		}

		return p_77659_1_;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister p_94581_1_) {
		itemIcon = p_94581_1_.registerIcon(getIconString() + "_uncast");
		theIcon = p_94581_1_.registerIcon(getIconString() + "_cast");
	}

	@SideOnly(Side.CLIENT)
	public IIcon func_94597_g() {
		return theIcon;
	}

	@Override
	public boolean isItemTool(ItemStack p_77616_1_) {
		return super.isItemTool(p_77616_1_);
	}

	@Override
	public int getItemEnchantability() {
		return 1;
	}
}