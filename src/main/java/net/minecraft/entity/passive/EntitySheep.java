package net.minecraft.entity.passive;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import org.bukkit.event.entity.SheepRegrowWoolEvent;

import java.util.ArrayList;
import java.util.Random;

public class EntitySheep extends EntityAnimal implements IShearable {
	private final InventoryCrafting field_90016_e = new InventoryCrafting(new Container() {
		private static final String __OBFID = "CL_00001649";

		@Override
		public boolean canInteractWith(EntityPlayer p_75145_1_) {
			return false;
		}
	}, 2, 1);
	public static final float[][] fleeceColorTable = new float[][] { { 1.0F, 1.0F, 1.0F }, { 0.85F, 0.5F, 0.2F },
			{ 0.7F, 0.3F, 0.85F }, { 0.4F, 0.6F, 0.85F }, { 0.9F, 0.9F, 0.2F }, { 0.5F, 0.8F, 0.1F },
			{ 0.95F, 0.5F, 0.65F }, { 0.3F, 0.3F, 0.3F }, { 0.6F, 0.6F, 0.6F }, { 0.3F, 0.5F, 0.6F },
			{ 0.5F, 0.25F, 0.7F }, { 0.2F, 0.3F, 0.7F }, { 0.4F, 0.3F, 0.2F }, { 0.4F, 0.5F, 0.2F },
			{ 0.6F, 0.2F, 0.2F }, { 0.1F, 0.1F, 0.1F } };
	private int sheepTimer;
	private final EntityAIEatGrass field_146087_bs = new EntityAIEatGrass(this);
	private static final String __OBFID = "CL_00001648";

	public EntitySheep(World p_i1691_1_) {
		super(p_i1691_1_);
		setSize(0.9F, 1.3F);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 1.25D));
		tasks.addTask(2, new EntityAIMate(this, 1.0D));
		tasks.addTask(3, new EntityAITempt(this, 1.1D, Items.wheat, false));
		tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
		tasks.addTask(5, field_146087_bs);
		tasks.addTask(6, new EntityAIWander(this, 1.0D));
		tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		tasks.addTask(8, new EntityAILookIdle(this));
		field_90016_e.setInventorySlotContents(0, new ItemStack(Items.dye, 1, 0));
		field_90016_e.setInventorySlotContents(1, new ItemStack(Items.dye, 1, 0));
	}

	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	@Override
	protected void updateAITasks() {
		sheepTimer = field_146087_bs.func_151499_f();
		super.updateAITasks();
	}

	@Override
	public void onLivingUpdate() {
		if (worldObj.isRemote) {
			sheepTimer = Math.max(0, sheepTimer - 1);
		}

		super.onLivingUpdate();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, new Byte((byte) 0));
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		if (!getSheared()) {
			entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, getFleeceColor()), 0.0F);
		}
	}

	@Override
	protected Item getDropItem() {
		return Item.getItemFromBlock(Blocks.wool);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 10) {
			sheepTimer = 40;
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		return super.interact(p_70085_1_);
	}

	@SideOnly(Side.CLIENT)
	public float func_70894_j(float p_70894_1_) {
		return sheepTimer <= 0 ? 0.0F
				: sheepTimer >= 4 && sheepTimer <= 36 ? 1.0F
						: sheepTimer < 4 ? (sheepTimer - p_70894_1_) / 4.0F : -(sheepTimer - 40 - p_70894_1_) / 4.0F;
	}

	@SideOnly(Side.CLIENT)
	public float func_70890_k(float p_70890_1_) {
		if (sheepTimer > 4 && sheepTimer <= 36) {
			float f1 = (sheepTimer - 4 - p_70890_1_) / 32.0F;
			return (float) Math.PI / 5F + (float) Math.PI * 7F / 100F * MathHelper.sin(f1 * 28.7F);
		} else
			return sheepTimer > 0 ? (float) Math.PI / 5F : rotationPitch / (180F / (float) Math.PI);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setBoolean("Sheared", getSheared());
		p_70014_1_.setByte("Color", (byte) getFleeceColor());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		setSheared(p_70037_1_.getBoolean("Sheared"));
		setFleeceColor(p_70037_1_.getByte("Color"));
	}

	@Override
	protected String getLivingSound() {
		return "mob.sheep.say";
	}

	@Override
	protected String getHurtSound() {
		return "mob.sheep.say";
	}

	@Override
	protected String getDeathSound() {
		return "mob.sheep.say";
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
		playSound("mob.sheep.step", 0.15F, 1.0F);
	}

	public int getFleeceColor() {
		return dataWatcher.getWatchableObjectByte(16) & 15;
	}

	public void setFleeceColor(int p_70891_1_) {
		byte b0 = dataWatcher.getWatchableObjectByte(16);
		dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & 240 | p_70891_1_ & 15)));
	}

	public boolean getSheared() {
		return (dataWatcher.getWatchableObjectByte(16) & 16) != 0;
	}

	public void setSheared(boolean p_70893_1_) {
		byte b0 = dataWatcher.getWatchableObjectByte(16);

		if (p_70893_1_) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 16)));
		} else {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -17)));
		}
	}

	public static int getRandomFleeceColor(Random p_70895_0_) {
		int i = p_70895_0_.nextInt(100);
		return i < 5 ? 15 : i < 10 ? 7 : i < 15 ? 8 : i < 18 ? 12 : p_70895_0_.nextInt(500) == 0 ? 6 : 0;
	}

	@Override
	public EntitySheep createChild(EntityAgeable p_90011_1_) {
		EntitySheep entitysheep = (EntitySheep) p_90011_1_;
		EntitySheep entitysheep1 = new EntitySheep(worldObj);
		int i = func_90014_a(this, entitysheep);
		entitysheep1.setFleeceColor(15 - i);
		return entitysheep1;
	}

	@Override
	public void eatGrassBonus() {
		// CraftBukkit start
		SheepRegrowWoolEvent event = new SheepRegrowWoolEvent((org.bukkit.entity.Sheep) this.getBukkitEntity());
		this.worldObj.getServer().getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			this.setSheared(false);
		}
		// CraftBukkit end

		if (isChild()) {
			addGrowth(60);
		}
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		p_110161_1_ = super.onSpawnWithEgg(p_110161_1_);
		setFleeceColor(getRandomFleeceColor(worldObj.rand));
		return p_110161_1_;
	}

	private int func_90014_a(EntityAnimal p_90014_1_, EntityAnimal p_90014_2_) {
		int i = func_90013_b(p_90014_1_);
		int j = func_90013_b(p_90014_2_);
		field_90016_e.getStackInSlot(0).setItemDamage(i);
		field_90016_e.getStackInSlot(1).setItemDamage(j);
		ItemStack itemstack = CraftingManager.getInstance().findMatchingRecipe(field_90016_e,
				p_90014_1_.worldObj);
		int k;

		if (itemstack != null && itemstack.getItem() == Items.dye) {
			k = itemstack.getItemDamage();
		} else {
			k = worldObj.rand.nextBoolean() ? i : j;
		}

		return k;
	}

	private int func_90013_b(EntityAnimal p_90013_1_) {
		return 15 - ((EntitySheep) p_90013_1_).getFleeceColor();
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) {
		return !getSheared() && !isChild();
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<>();
		setSheared(true);
		int i = 1 + rand.nextInt(3);
		for (int j = 0; j < i; j++) {
			ret.add(new ItemStack(Blocks.wool, 1, getFleeceColor()));
		}
		playSound("mob.sheep.shear", 1.0F, 1.0F);
		return ret;
	}
}