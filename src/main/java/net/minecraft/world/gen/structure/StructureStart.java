package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import org.ultramine.server.util.ListAsLinkedList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public abstract class StructureStart {
	protected ArrayList<StructureComponent> componentsUm = new ArrayList<>();
	@SuppressWarnings("rawtypes")
	protected LinkedList components = new ListAsLinkedList<>(componentsUm);
	protected StructureBoundingBox boundingBox;
	private int field_143024_c;
	private int field_143023_d;
	public StructureStart() {
	}

	public StructureStart(int p_i43002_1_, int p_i43002_2_) {
		field_143024_c = p_i43002_1_;
		field_143023_d = p_i43002_2_;
	}

	public StructureBoundingBox getBoundingBox() {
		return boundingBox;
	}

	@SuppressWarnings("rawtypes")
	public LinkedList getComponents() {
		return components;
	}

	@SuppressWarnings("rawtypes")
	public void generateStructure(World p_75068_1_, Random p_75068_2_, StructureBoundingBox p_75068_3_) {
		Iterator iterator = components.iterator();

		while (iterator.hasNext()) {
			StructureComponent structurecomponent = (StructureComponent) iterator.next();

			if (structurecomponent.getBoundingBox().intersectsWith(p_75068_3_)
					&& !structurecomponent.addComponentParts(p_75068_1_, p_75068_2_, p_75068_3_)) {
				iterator.remove();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	protected void updateBoundingBox() {
		boundingBox = StructureBoundingBox.getNewBoundingBox();
		Iterator iterator = components.iterator();

		while (iterator.hasNext()) {
			StructureComponent structurecomponent = (StructureComponent) iterator.next();
			boundingBox.expandTo(structurecomponent.getBoundingBox());
		}
	}

	@SuppressWarnings("rawtypes")
	public NBTTagCompound func_143021_a(int p_143021_1_, int p_143021_2_) {
		if (MapGenStructureIO.func_143033_a(this) == null)
			throw new RuntimeException("StructureStart \"" + this.getClass().getName()
					+ "\" missing ID Mapping, Modder see MapGenStructureIO");
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setString("id", MapGenStructureIO.func_143033_a(this));
		nbttagcompound.setInteger("ChunkX", p_143021_1_);
		nbttagcompound.setInteger("ChunkZ", p_143021_2_);
		nbttagcompound.setTag("BB", boundingBox.func_151535_h());
		NBTTagList nbttaglist = new NBTTagList(components.size());
		Iterator iterator = components.iterator();

		while (iterator.hasNext()) {
			StructureComponent structurecomponent = (StructureComponent) iterator.next();
			nbttaglist.appendTag(structurecomponent.func_143010_b());
		}

		nbttagcompound.setTag("Children", nbttaglist);
		func_143022_a(nbttagcompound);
		return nbttagcompound;
	}

	public void func_143022_a(NBTTagCompound p_143022_1_) {
	}

	@SuppressWarnings("unchecked")
	public void func_143020_a(World p_143020_1_, NBTTagCompound p_143020_2_) {
		field_143024_c = p_143020_2_.getInteger("ChunkX");
		field_143023_d = p_143020_2_.getInteger("ChunkZ");

		if (p_143020_2_.hasKey("BB")) {
			boundingBox = new StructureBoundingBox(p_143020_2_.getIntArray("BB"));
		}

		NBTTagList nbttaglist = p_143020_2_.getTagList("Children", 10);

		componentsUm.ensureCapacity(nbttaglist.tagCount());
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			StructureComponent tmp = MapGenStructureIO.func_143032_b(nbttaglist.getCompoundTagAt(i), p_143020_1_);
			if (tmp != null) {
				components.add(tmp); // Forge: Prevent NPEs further down the line when a component can't be loaded.
			}
		}
		componentsUm.trimToSize();

		func_143017_b(p_143020_2_);
	}

	public void func_143017_b(NBTTagCompound p_143017_1_) {
	}

	@SuppressWarnings("rawtypes")
	protected void markAvailableHeight(World p_75067_1_, Random p_75067_2_, int p_75067_3_) {
		int j = 63 - p_75067_3_;
		int k = boundingBox.getYSize() + 1;

		if (k < j) {
			k += p_75067_2_.nextInt(j - k);
		}

		int l = k - boundingBox.maxY;
		boundingBox.offset(0, l, 0);
		Iterator iterator = components.iterator();

		while (iterator.hasNext()) {
			StructureComponent structurecomponent = (StructureComponent) iterator.next();
			structurecomponent.getBoundingBox().offset(0, l, 0);
		}
	}

	@SuppressWarnings("rawtypes")
	protected void setRandomHeight(World p_75070_1_, Random p_75070_2_, int p_75070_3_, int p_75070_4_) {
		int k = p_75070_4_ - p_75070_3_ + 1 - boundingBox.getYSize();
		int i1;

		if (k > 1) {
			i1 = p_75070_3_ + p_75070_2_.nextInt(k);
		} else {
			i1 = p_75070_3_;
		}

		int l = i1 - boundingBox.minY;
		boundingBox.offset(0, l, 0);
		Iterator iterator = components.iterator();

		while (iterator.hasNext()) {
			StructureComponent structurecomponent = (StructureComponent) iterator.next();
			structurecomponent.getBoundingBox().offset(0, l, 0);
		}
	}

	public boolean isSizeableStructure() {
		return true;
	}

	public int func_143019_e() {
		return field_143024_c;
	}

	public int func_143018_f() {
		return field_143023_d;
	}
}