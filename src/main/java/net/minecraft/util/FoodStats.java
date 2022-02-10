package net.minecraft.util;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.world.EnumDifficulty;

public class FoodStats {
	private int foodLevel = 20;
	private float foodSaturationLevel = 5.0F;
	private float foodExhaustionLevel;
	private int foodTimer;
	private int prevFoodLevel = 20;
	private static final String __OBFID = "CL_00001729";

	private EntityPlayer entityplayer;

	public void addStats(int p_75122_1_, float p_75122_2_) {
		foodLevel = Math.min(p_75122_1_ + foodLevel, 20);
		foodSaturationLevel = Math.min(foodSaturationLevel + p_75122_1_ * p_75122_2_ * 2.0F, foodLevel);
	}

	public void func_151686_a(ItemFood p_151686_1_, ItemStack p_151686_2_) {
		if (entityplayer == null)
			return;
		// CraftBukkit start
		int oldFoodLevel = foodLevel;
		org.bukkit.event.entity.FoodLevelChangeEvent event = CraftEventFactory.callFoodLevelChangeEvent(entityplayer,
				p_151686_1_.func_150905_g(p_151686_2_) + oldFoodLevel);

		if (!event.isCancelled()) {
			addStats(event.getFoodLevel() - oldFoodLevel, p_151686_1_.func_150906_h(p_151686_2_));
		}

		sendUpdatePacket();
		// CraftBukkit end
	}

	public void onUpdate(EntityPlayer p_75118_1_) {
		entityplayer = p_75118_1_;

		EnumDifficulty enumdifficulty = p_75118_1_.worldObj.difficultySetting;
		prevFoodLevel = foodLevel;

		if (foodExhaustionLevel > 4.0F) {
			foodExhaustionLevel -= 4.0F;

			if (foodSaturationLevel > 0.0F) {
				foodSaturationLevel = Math.max(foodSaturationLevel - 1.0F, 0.0F);
			} else if (enumdifficulty != EnumDifficulty.PEACEFUL) {
				// foodLevel = Math.max(foodLevel - 1, 0);

				org.bukkit.event.entity.FoodLevelChangeEvent event = CraftEventFactory
						.callFoodLevelChangeEvent(entityplayer, Math.max(foodLevel - 1, 0));

				if (!event.isCancelled()) {
					foodLevel = event.getFoodLevel();
				}

				sendUpdatePacket((EntityPlayerMP) p_75118_1_);
			}
		}

		if (p_75118_1_.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration") && foodLevel >= 18
				&& p_75118_1_.shouldHeal()) {
			++foodTimer;

			if (foodTimer >= 80) {
				p_75118_1_.heal(1.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.SATIATED);
				addExhaustion(3.0F);
				foodTimer = 0;
			}
		} else if (foodLevel <= 0) {
			++foodTimer;

			if (foodTimer >= 80) {
				if (p_75118_1_.getHealth() > 10.0F || enumdifficulty == EnumDifficulty.HARD
						|| p_75118_1_.getHealth() > 1.0F && enumdifficulty == EnumDifficulty.NORMAL) {
					p_75118_1_.attackEntityFrom(DamageSource.starve, 1.0F);
				}

				foodTimer = 0;
			}
		} else {
			foodTimer = 0;
		}
	}

	public void readNBT(NBTTagCompound p_75112_1_) {
		if (p_75112_1_.hasKey("foodLevel", 99)) {
			foodLevel = p_75112_1_.getInteger("foodLevel");
			foodTimer = p_75112_1_.getInteger("foodTickTimer");
			foodSaturationLevel = p_75112_1_.getFloat("foodSaturationLevel");
			foodExhaustionLevel = p_75112_1_.getFloat("foodExhaustionLevel");
		}
	}

	public void writeNBT(NBTTagCompound p_75117_1_) {
		p_75117_1_.setInteger("foodLevel", foodLevel);
		p_75117_1_.setInteger("foodTickTimer", foodTimer);
		p_75117_1_.setFloat("foodSaturationLevel", foodSaturationLevel);
		p_75117_1_.setFloat("foodExhaustionLevel", foodExhaustionLevel);
	}

	public int getFoodLevel() {
		return foodLevel;
	}

	@SideOnly(Side.CLIENT)
	public int getPrevFoodLevel() {
		return prevFoodLevel;
	}

	public boolean needFood() {
		return foodLevel < 20;
	}

	public void addExhaustion(float p_75113_1_) {
		foodExhaustionLevel = Math.min(foodExhaustionLevel + p_75113_1_, 40.0F);
	}

	public float getSaturationLevel() {
		return foodSaturationLevel;
	}

	public void setFoodLevel(int p_75114_1_) {
		foodLevel = p_75114_1_;
	}

	public void setFoodSaturationLevel(float p_75119_1_) {
		foodSaturationLevel = p_75119_1_;
	}

	public void sendUpdatePacket() {
		sendUpdatePacket((EntityPlayerMP) entityplayer);
	}

	public void sendUpdatePacket(EntityPlayerMP player) {
		player.playerNetServerHandler.sendPacket(new S06PacketUpdateHealth(
				((CraftPlayer) entityplayer.getBukkitEntity()).getScaledHealth(),
				entityplayer.getFoodStats().getFoodLevel(), entityplayer.getFoodStats().getFoodSaturationLevel()));
	}

	public float getFoodSaturationLevel() {
		return foodSaturationLevel;
	}

	public float getFoodExhaustionLevel() {
		return foodExhaustionLevel;
	}

	public void setFoodExhaustionLevel(float foodExhaustionLevel) {
		this.foodExhaustionLevel = foodExhaustionLevel;
	}

	public int getFoodTimer() {
		return foodTimer;
	}

	public void setFoodTimer(int foodTimer) {
		this.foodTimer = foodTimer;
	}
}