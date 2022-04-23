package cpw.mods.fml.common.network.simpleimpl;

import io.netty.buffer.ByteBuf;

/**
 * Implement this interface for each message you wish to define.
 *
 * @author cpw
 *
 */
public interface IMessage {
	/**
	 * Convert from the supplied buffer into your specific message type
	 *
	 * @param buf
	 */
    void fromBytes(ByteBuf buf);

	/**
	 * Deconstruct your message into the supplied byte buffer
	 *
	 * @param buf
	 */
    void toBytes(ByteBuf buf);
}