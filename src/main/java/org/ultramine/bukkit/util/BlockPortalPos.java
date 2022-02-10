package org.ultramine.bukkit.util;

public class BlockPortalPos {
	private int x;
	private int y;
	private int z;
	private int field_150865_b;
	private int flag;

	public BlockPortalPos(int x, int y, int z, int field_150865_b, int flag) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.field_150865_b = field_150865_b;
		this.flag = flag;
	}

	public int getX() {

		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getField_150865_b() {
		return field_150865_b;
	}

	public void setField_150865_b(int field_150865_b) {
		this.field_150865_b = field_150865_b;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
