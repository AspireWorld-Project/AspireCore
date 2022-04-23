package net.minecraft.client.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.network.play.client.*;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Session;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityClientPlayerMP extends EntityPlayerSP {
	public final NetHandlerPlayClient sendQueue;
	private final StatFileWriter field_146108_bO;
	private double oldPosX;
	private double oldMinY;
	private double oldPosZ;
	private float oldRotationYaw;
	private float oldRotationPitch;
	private boolean wasSneaking;
	private boolean wasSprinting;
	private int ticksSinceMovePacket;
	private boolean hasSetHealth;
	private String field_142022_ce;
	public EntityClientPlayerMP(Minecraft p_i45064_1_, World p_i45064_2_, Session p_i45064_3_,
			NetHandlerPlayClient p_i45064_4_, StatFileWriter p_i45064_5_) {
		super(p_i45064_1_, p_i45064_2_, p_i45064_3_, 0);
		sendQueue = p_i45064_4_;
		field_146108_bO = p_i45064_5_;
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		return false;
	}

	@Override
	public void heal(float p_70691_1_) {
	}

	@Override
	public void mountEntity(Entity p_70078_1_) {
		super.mountEntity(p_70078_1_);

		if (p_70078_1_ instanceof EntityMinecart) {
			mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart) p_70078_1_));
		}
	}

	@Override
	public void onUpdate() {
		if (worldObj.blockExists(MathHelper.floor_double(posX), 0, MathHelper.floor_double(posZ))) {
			super.onUpdate();

			if (isRiding()) {
				sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(rotationYaw, rotationPitch, onGround));
				sendQueue.addToSendQueue(
						new C0CPacketInput(moveStrafing, moveForward, movementInput.jump, movementInput.sneak));
			} else {
				sendMotionUpdates();
			}
		}
	}

	public void sendMotionUpdates() {
		boolean flag = isSprinting();

		if (flag != wasSprinting) {
			if (flag) {
				sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 4));
			} else {
				sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 5));
			}

			wasSprinting = flag;
		}

		boolean flag1 = isSneaking();

		if (flag1 != wasSneaking) {
			if (flag1) {
				sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 1));
			} else {
				sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 2));
			}

			wasSneaking = flag1;
		}

		double d0 = posX - oldPosX;
		double d1 = boundingBox.minY - oldMinY;
		double d2 = posZ - oldPosZ;
		double d3 = rotationYaw - oldRotationYaw;
		double d4 = rotationPitch - oldRotationPitch;
		boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || ticksSinceMovePacket >= 20;
		boolean flag3 = d3 != 0.0D || d4 != 0.0D;

		if (ridingEntity != null) {
			sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(motionX, -999.0D, -999.0D, motionZ,
					rotationYaw, rotationPitch, onGround));
			flag2 = false;
		} else if (flag2 && flag3) {
			sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(posX, boundingBox.minY, posY, posZ,
					rotationYaw, rotationPitch, onGround));
		} else if (flag2) {
			sendQueue.addToSendQueue(
					new C03PacketPlayer.C04PacketPlayerPosition(posX, boundingBox.minY, posY, posZ, onGround));
		} else if (flag3) {
			sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(rotationYaw, rotationPitch, onGround));
		} else {
			sendQueue.addToSendQueue(new C03PacketPlayer(onGround));
		}

		++ticksSinceMovePacket;
		if (flag2) {
			oldPosX = posX;
			oldMinY = boundingBox.minY;
			oldPosZ = posZ;
			ticksSinceMovePacket = 0;
		}

		if (flag3) {
			oldRotationYaw = rotationYaw;
			oldRotationPitch = rotationPitch;
		}
	}

	@Override
	public EntityItem dropOneItem(boolean p_71040_1_) {
		int i = p_71040_1_ ? 3 : 4;
		sendQueue.addToSendQueue(new C07PacketPlayerDigging(i, 0, 0, 0, 0));
		return null;
	}

	@Override
	public void joinEntityItemWithWorld(EntityItem p_71012_1_) {
	}

	public void sendChatMessage(String p_71165_1_) {
		sendQueue.addToSendQueue(new C01PacketChatMessage(p_71165_1_));
	}

	@Override
	public void swingItem() {
		super.swingItem();
		sendQueue.addToSendQueue(new C0APacketAnimation(this, 1));
	}

	@Override
	public void respawnPlayer() {
		sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
	}

	@Override
	protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_) {
		if (!isEntityInvulnerable()) {
			setHealth(getHealth() - p_70665_2_);
		}
	}

	@Override
	public void closeScreen() {
		sendQueue.addToSendQueue(new C0DPacketCloseWindow(openContainer.windowId));
		closeScreenNoPacket();
	}

	public void closeScreenNoPacket() {
		inventory.setItemStack(null);
		super.closeScreen();
	}

	@Override
	public void setPlayerSPHealth(float p_71150_1_) {
		if (hasSetHealth) {
			super.setPlayerSPHealth(p_71150_1_);
		} else {
			setHealth(p_71150_1_);
			hasSetHealth = true;
		}
	}

	@Override
	public void addStat(StatBase p_71064_1_, int p_71064_2_) {
		if (p_71064_1_ != null) {
			if (p_71064_1_.isIndependent) {
				super.addStat(p_71064_1_, p_71064_2_);
			}
		}
	}

	@Override
	public void sendPlayerAbilities() {
		sendQueue.addToSendQueue(new C13PacketPlayerAbilities(capabilities));
	}

	@Override
	protected void func_110318_g() {
		sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 6, (int) (getHorseJumpPower() * 100.0F)));
	}

	public void func_110322_i() {
		sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 7));
	}

	public void func_142020_c(String p_142020_1_) {
		field_142022_ce = p_142020_1_;
	}

	public String func_142021_k() {
		return field_142022_ce;
	}

	public StatFileWriter getStatFileWriter() {
		return field_146108_bO;
	}
}