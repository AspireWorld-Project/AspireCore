package net.minecraft.command;

public class CommandException extends RuntimeException {
	private final Object[] errorObjects;
	private static final String __OBFID = "CL_00001187";

	public CommandException(String p_i1359_1_, Object... p_i1359_2_) {
		super(p_i1359_1_);
		errorObjects = p_i1359_2_;
	}

	public Object[] getErrorOjbects() {
		return errorObjects;
	}
}