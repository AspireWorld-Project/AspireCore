package net.minecraft.client.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.particle.EntityCrit2FX;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;

@SideOnly(Side.CLIENT)
public class EntityPlayerSP extends AbstractClientPlayer {
	public MovementInput movementInput;
	protected Minecraft mc;
	protected int sprintToggleTimer;
	public int sprintingTicksLeft;
	public float renderArmYaw;
	public float renderArmPitch;
	public float prevRenderArmYaw;
	public float prevRenderArmPitch;
	private int horseJumpPowerCounter;
	private float horseJumpPower;
	private MouseFilter field_71162_ch = new MouseFilter();
	private MouseFilter field_71160_ci = new MouseFilter();
	private MouseFilter field_71161_cj = new MouseFilter();
	public float timeInPortal;
	public float prevTimeInPortal;
	private static final String __OBFID = "CL_00000938";

	public EntityPlayerSP(Minecraft p_i1238_1_, World p_i1238_2_, Session p_i1238_3_, int p_i1238_4_) {
		super(p_i1238_2_, p_i1238_3_.func_148256_e());
		mc = p_i1238_1_;
		dimension = p_i1238_4_;
	}

	@Override
	public void updateEntityActionState() {
		super.updateEntityActionState();
		moveStrafing = movementInput.moveStrafe;
		moveForward = movementInput.moveForward;
		isJumping = movementInput.jump;
		prevRenderArmYaw = renderArmYaw;
		prevRenderArmPitch = renderArmPitch;
		renderArmPitch = (float) (renderArmPitch + (rotationPitch - renderArmPitch) * 0.5D);
		renderArmYaw = (float) (renderArmYaw + (rotationYaw - renderArmYaw) * 0.5D);
	}

	@Override
	public void onLivingUpdate() {
		if (sprintingTicksLeft > 0) {
			--sprintingTicksLeft;

			if (sprintingTicksLeft == 0) {
				setSprinting(false);
			}
		}

		if (sprintToggleTimer > 0) {
			--sprintToggleTimer;
		}

		if (mc.playerController.enableEverythingIsScrewedUpMode()) {
			posX = posZ = 0.5D;
			posX = 0.0D;
			posZ = 0.0D;
			rotationYaw = ticksExisted / 12.0F;
			rotationPitch = 10.0F;
			posY = 68.5D;
		} else {
			prevTimeInPortal = timeInPortal;

			if (inPortal) {
				if (mc.currentScreen != null) {
					mc.displayGuiScreen((GuiScreen) null);
				}

				if (timeInPortal == 0.0F) {
					mc.getSoundHandler().playSound(PositionedSoundRecord
							.func_147674_a(new ResourceLocation("portal.trigger"), rand.nextFloat() * 0.4F + 0.8F));
				}

				timeInPortal += 0.0125F;

				if (timeInPortal >= 1.0F) {
					timeInPortal = 1.0F;
				}

				inPortal = false;
			} else if (this.isPotionActive(Potion.confusion)
					&& getActivePotionEffect(Potion.confusion).getDuration() > 60) {
				timeInPortal += 0.006666667F;

				if (timeInPortal > 1.0F) {
					timeInPortal = 1.0F;
				}
			} else {
				if (timeInPortal > 0.0F) {
					timeInPortal -= 0.05F;
				}

				if (timeInPortal < 0.0F) {
					timeInPortal = 0.0F;
				}
			}

			if (timeUntilPortal > 0) {
				--timeUntilPortal;
			}

			boolean flag = movementInput.jump;
			float f = 0.8F;
			boolean flag1 = movementInput.moveForward >= f;
			movementInput.updatePlayerMoveState();

			if (isUsingItem() && !isRiding()) {
				movementInput.moveStrafe *= 0.2F;
				movementInput.moveForward *= 0.2F;
				sprintToggleTimer = 0;
			}

			if (movementInput.sneak && ySize < 0.2F) {
				ySize = 0.2F;
			}

			func_145771_j(posX - width * 0.35D, boundingBox.minY + 0.5D, posZ + width * 0.35D);
			func_145771_j(posX - width * 0.35D, boundingBox.minY + 0.5D, posZ - width * 0.35D);
			func_145771_j(posX + width * 0.35D, boundingBox.minY + 0.5D, posZ - width * 0.35D);
			func_145771_j(posX + width * 0.35D, boundingBox.minY + 0.5D, posZ + width * 0.35D);
			boolean flag2 = getFoodStats().getFoodLevel() > 6.0F || capabilities.allowFlying;

			if (onGround && !flag1 && movementInput.moveForward >= f && !isSprinting() && flag2 && !isUsingItem()
					&& !this.isPotionActive(Potion.blindness)) {
				if (sprintToggleTimer <= 0 && !mc.gameSettings.keyBindSprint.getIsKeyPressed()) {
					sprintToggleTimer = 7;
				} else {
					setSprinting(true);
				}
			}

			if (!isSprinting() && movementInput.moveForward >= f && flag2 && !isUsingItem()
					&& !this.isPotionActive(Potion.blindness) && mc.gameSettings.keyBindSprint.getIsKeyPressed()) {
				setSprinting(true);
			}

			if (isSprinting() && (movementInput.moveForward < f || isCollidedHorizontally || !flag2)) {
				setSprinting(false);
			}

			if (capabilities.allowFlying && !flag && movementInput.jump) {
				if (flyToggleTimer == 0) {
					flyToggleTimer = 7;
				} else {
					capabilities.isFlying = !capabilities.isFlying;
					sendPlayerAbilities();
					flyToggleTimer = 0;
				}
			}

			if (capabilities.isFlying) {
				if (movementInput.sneak) {
					motionY -= 0.15D;
				}

				if (movementInput.jump) {
					motionY += 0.15D;
				}
			}

			if (isRidingHorse()) {
				if (horseJumpPowerCounter < 0) {
					++horseJumpPowerCounter;

					if (horseJumpPowerCounter == 0) {
						horseJumpPower = 0.0F;
					}
				}

				if (flag && !movementInput.jump) {
					horseJumpPowerCounter = -10;
					func_110318_g();
				} else if (!flag && movementInput.jump) {
					horseJumpPowerCounter = 0;
					horseJumpPower = 0.0F;
				} else if (flag) {
					++horseJumpPowerCounter;

					if (horseJumpPowerCounter < 10) {
						horseJumpPower = horseJumpPowerCounter * 0.1F;
					} else {
						horseJumpPower = 0.8F + 2.0F / (horseJumpPowerCounter - 9) * 0.1F;
					}
				}
			} else {
				horseJumpPower = 0.0F;
			}

			super.onLivingUpdate();

			if (onGround && capabilities.isFlying) {
				capabilities.isFlying = false;
				sendPlayerAbilities();
			}
		}
	}

	public float getFOVMultiplier() {
		float f = 1.0F;

		if (capabilities.isFlying) {
			f *= 1.1F;
		}

		IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
		f = (float) (f * ((iattributeinstance.getAttributeValue() / capabilities.getWalkSpeed() + 1.0D) / 2.0D));

		if (capabilities.getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f)) {
			f = 1.0F;
		}

		if (isUsingItem() && getItemInUse().getItem() == Items.bow) {
			int i = getItemInUseDuration();
			float f1 = i / 20.0F;

			if (f1 > 1.0F) {
				f1 = 1.0F;
			} else {
				f1 *= f1;
			}

			f *= 1.0F - f1 * 0.15F;
		}

		return ForgeHooksClient.getOffsetFOV(this, f);
	}

	@Override
	public void closeScreen() {
		super.closeScreen();
		mc.displayGuiScreen((GuiScreen) null);
	}

	@Override
	public void func_146100_a(TileEntity p_146100_1_) {
		if (p_146100_1_ instanceof TileEntitySign) {
			mc.displayGuiScreen(new GuiEditSign((TileEntitySign) p_146100_1_));
		} else if (p_146100_1_ instanceof TileEntityCommandBlock) {
			mc.displayGuiScreen(new GuiCommandBlock(((TileEntityCommandBlock) p_146100_1_).func_145993_a()));
		}
	}

	@Override
	public void func_146095_a(CommandBlockLogic p_146095_1_) {
		mc.displayGuiScreen(new GuiCommandBlock(p_146095_1_));
	}

	@Override
	public void displayGUIBook(ItemStack p_71048_1_) {
		Item item = p_71048_1_.getItem();

		if (item == Items.written_book) {
			mc.displayGuiScreen(new GuiScreenBook(this, p_71048_1_, false));
		} else if (item == Items.writable_book) {
			mc.displayGuiScreen(new GuiScreenBook(this, p_71048_1_, true));
		}
	}

	@Override
	public void displayGUIChest(IInventory p_71007_1_) {
		mc.displayGuiScreen(new GuiChest(inventory, p_71007_1_));
	}

	@Override
	public void func_146093_a(TileEntityHopper p_146093_1_) {
		mc.displayGuiScreen(new GuiHopper(inventory, p_146093_1_));
	}

	@Override
	public void displayGUIHopperMinecart(EntityMinecartHopper p_96125_1_) {
		mc.displayGuiScreen(new GuiHopper(inventory, p_96125_1_));
	}

	@Override
	public void displayGUIHorse(EntityHorse p_110298_1_, IInventory p_110298_2_) {
		mc.displayGuiScreen(new GuiScreenHorseInventory(inventory, p_110298_2_, p_110298_1_));
	}

	@Override
	public void displayGUIWorkbench(int p_71058_1_, int p_71058_2_, int p_71058_3_) {
		mc.displayGuiScreen(new GuiCrafting(inventory, worldObj, p_71058_1_, p_71058_2_, p_71058_3_));
	}

	@Override
	public void displayGUIEnchantment(int p_71002_1_, int p_71002_2_, int p_71002_3_, String p_71002_4_) {
		mc.displayGuiScreen(new GuiEnchantment(inventory, worldObj, p_71002_1_, p_71002_2_, p_71002_3_, p_71002_4_));
	}

	@Override
	public void displayGUIAnvil(int p_82244_1_, int p_82244_2_, int p_82244_3_) {
		mc.displayGuiScreen(new GuiRepair(inventory, worldObj, p_82244_1_, p_82244_2_, p_82244_3_));
	}

	@Override
	public void func_146101_a(TileEntityFurnace p_146101_1_) {
		mc.displayGuiScreen(new GuiFurnace(inventory, p_146101_1_));
	}

	@Override
	public void func_146098_a(TileEntityBrewingStand p_146098_1_) {
		mc.displayGuiScreen(new GuiBrewingStand(inventory, p_146098_1_));
	}

	@Override
	public void func_146104_a(TileEntityBeacon p_146104_1_) {
		mc.displayGuiScreen(new GuiBeacon(inventory, p_146104_1_));
	}

	@Override
	public void func_146102_a(TileEntityDispenser p_146102_1_) {
		mc.displayGuiScreen(new GuiDispenser(inventory, p_146102_1_));
	}

	@Override
	public void displayGUIMerchant(IMerchant p_71030_1_, String p_71030_2_) {
		mc.displayGuiScreen(new GuiMerchant(inventory, p_71030_1_, worldObj, p_71030_2_));
	}

	@Override
	public void onCriticalHit(Entity p_71009_1_) {
		mc.effectRenderer.addEffect(new EntityCrit2FX(mc.theWorld, p_71009_1_));
	}

	@Override
	public void onEnchantmentCritical(Entity p_71047_1_) {
		EntityCrit2FX entitycrit2fx = new EntityCrit2FX(mc.theWorld, p_71047_1_, "magicCrit");
		mc.effectRenderer.addEffect(entitycrit2fx);
	}

	@Override
	public void onItemPickup(Entity p_71001_1_, int p_71001_2_) {
		mc.effectRenderer.addEffect(new EntityPickupFX(mc.theWorld, p_71001_1_, this, -0.5F));
	}

	@Override
	public boolean isSneaking() {
		return movementInput.sneak && !sleeping;
	}

	public void setPlayerSPHealth(float p_71150_1_) {
		float f1 = getHealth() - p_71150_1_;

		if (f1 <= 0.0F) {
			setHealth(p_71150_1_);

			if (f1 < 0.0F) {
				hurtResistantTime = maxHurtResistantTime / 2;
			}
		} else {
			lastDamage = f1;
			setHealth(getHealth());
			hurtResistantTime = maxHurtResistantTime;
			damageEntity(DamageSource.generic, f1);
			hurtTime = maxHurtTime = 10;
		}
	}

	@Override
	public void addChatComponentMessage(IChatComponent p_146105_1_) {
		mc.ingameGUI.getChatGUI().printChatMessage(p_146105_1_);
	}

	private boolean isBlockTranslucent(int p_71153_1_, int p_71153_2_, int p_71153_3_) {
		return worldObj.getBlock(p_71153_1_, p_71153_2_, p_71153_3_).isNormalCube();
	}

	private boolean isHeadspaceFree(int x, int y, int z, int height) {
		for (int i1 = 0; i1 < height; i1++) {
			if (isBlockTranslucent(x, y + i1, z + 1))
				return false;
		}
		return true;
	}

	@Override
	protected boolean func_145771_j(double p_145771_1_, double p_145771_3_, double p_145771_5_) {
		if (noClip)
			return false;
		int i = MathHelper.floor_double(p_145771_1_);
		int j = MathHelper.floor_double(p_145771_3_);
		int k = MathHelper.floor_double(p_145771_5_);
		double d3 = p_145771_1_ - i;
		double d4 = p_145771_5_ - k;

		int entHeight = Math.max(Math.round(height), 1);

		boolean inTranslucentBlock = true;

		for (int i1 = 0; i1 < entHeight; i1++) {
			if (!isBlockTranslucent(i, j + i1, k)) {
				inTranslucentBlock = false;
			}
		}

		if (inTranslucentBlock) {
			boolean flag = !isHeadspaceFree(i - 1, j, k, entHeight);
			boolean flag1 = !isHeadspaceFree(i + 1, j, k, entHeight);
			boolean flag2 = !isHeadspaceFree(i, j, k - 1, entHeight);
			boolean flag3 = !isHeadspaceFree(i, j, k + 1, entHeight);
			byte b0 = -1;
			double d5 = 9999.0D;

			if (flag && d3 < d5) {
				d5 = d3;
				b0 = 0;
			}

			if (flag1 && 1.0D - d3 < d5) {
				d5 = 1.0D - d3;
				b0 = 1;
			}

			if (flag2 && d4 < d5) {
				d5 = d4;
				b0 = 4;
			}

			if (flag3 && 1.0D - d4 < d5) {
				d5 = 1.0D - d4;
				b0 = 5;
			}

			float f = 0.1F;

			if (b0 == 0) {
				motionX = -f;
			}

			if (b0 == 1) {
				motionX = f;
			}

			if (b0 == 4) {
				motionZ = -f;
			}

			if (b0 == 5) {
				motionZ = f;
			}
		}

		return false;
	}

	@Override
	public void setSprinting(boolean p_70031_1_) {
		super.setSprinting(p_70031_1_);
		sprintingTicksLeft = p_70031_1_ ? 600 : 0;
	}

	public void setXPStats(float p_71152_1_, int p_71152_2_, int p_71152_3_) {
		experience = p_71152_1_;
		experienceTotal = p_71152_2_;
		experienceLevel = p_71152_3_;
	}

	@Override
	public void addChatMessage(IChatComponent p_145747_1_) {
		mc.ingameGUI.getChatGUI().printChatMessage(p_145747_1_);
	}

	@Override
	public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
		return p_70003_1_ <= 0;
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return new ChunkCoordinates(MathHelper.floor_double(posX + 0.5D), MathHelper.floor_double(posY + 0.5D),
				MathHelper.floor_double(posZ + 0.5D));
	}

	@Override
	public void playSound(String p_85030_1_, float p_85030_2_, float p_85030_3_) {
		PlaySoundAtEntityEvent event = new PlaySoundAtEntityEvent(this, p_85030_1_, p_85030_2_, p_85030_3_);
		if (MinecraftForge.EVENT_BUS.post(event))
			return;
		p_85030_1_ = event.name;
		worldObj.playSound(posX, posY - yOffset, posZ, p_85030_1_, p_85030_2_, p_85030_3_, false);
	}

	@Override
	public boolean isClientWorld() {
		return true;
	}

	public boolean isRidingHorse() {
		return ridingEntity != null && ridingEntity instanceof EntityHorse;
	}

	public float getHorseJumpPower() {
		return horseJumpPower;
	}

	protected void func_110318_g() {
	}
}