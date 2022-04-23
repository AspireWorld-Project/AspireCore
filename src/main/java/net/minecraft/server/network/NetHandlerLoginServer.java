package net.minecraft.server.network;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.properties.Property;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.CryptManager;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ultramine.advanced.ThreadPlayerLookupUUID;
import org.ultramine.server.util.GlobalExecutors;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class NetHandlerLoginServer implements INetHandlerLoginServer {
	private static final AtomicInteger field_147331_b = new AtomicInteger(0);
	private static final Logger logger = LogManager.getLogger();
	private static final Random field_147329_d = new Random();
	private final byte[] field_147330_e = new byte[4];
	private final MinecraftServer field_147327_f;
	public final NetworkManager field_147333_a;
	private static NetHandlerLoginServer.LoginState field_147328_g;
	private int field_147336_h;
	private static GameProfile field_147337_i;
	private final String field_147334_j;
	private SecretKey field_147335_k;
	private static final String __OBFID = "CL_00001458";

	public NetHandlerLoginServer(MinecraftServer p_i45298_1_, NetworkManager p_i45298_2_) {
		field_147328_g = NetHandlerLoginServer.LoginState.HELLO;
		field_147334_j = "";
		field_147327_f = p_i45298_1_;
		field_147333_a = p_i45298_2_;
		field_147329_d.nextBytes(field_147330_e);
	}

	@Override
	public void onNetworkTick() {
		if (field_147328_g == NetHandlerLoginServer.LoginState.READY_TO_ACCEPT) {
			func_147326_c();
		}

		if (field_147336_h++ == FMLNetworkHandler.LOGIN_TIMEOUT) {
			func_147322_a("Took too long to log in");
		}
	}

	public void func_147322_a(String p_147322_1_) {
		try {
			logger.info("Disconnecting " + func_147317_d() + ": " + p_147322_1_);
			ChatComponentText chatcomponenttext = new ChatComponentText(p_147322_1_);
			field_147333_a.scheduleOutboundPacket(new S00PacketDisconnect(chatcomponenttext)
			);
			field_147333_a.closeChannel(chatcomponenttext);
		} catch (Exception exception) {
			logger.error("Error whilst disconnecting player", exception);
		}
	}

	public void func_147326_c()
	{
		EntityPlayerMP s = this.field_147327_f.getConfigurationManager().attemptLogin(this, field_147337_i, "");

		if (s == null)
		{
		}
		else
		{
			field_147328_g = NetHandlerLoginServer.LoginState.ACCEPTED;
			this.field_147333_a.scheduleOutboundPacket(new S02PacketLoginSuccess(field_147337_i));
			FMLNetworkHandler.fmlServerHandshake(this.field_147327_f.getConfigurationManager(), this.field_147333_a, this.field_147327_f.getConfigurationManager().processLogin(field_147337_i, s)); // CraftBukkit - add player reference
		}
	}

	@Override
	public void onDisconnect(IChatComponent p_147231_1_) {
		logger.info(func_147317_d() + " lost connection: " + p_147231_1_.getUnformattedText());
	}

	public String func_147317_d() {
		return field_147337_i != null
				? field_147337_i + " (" + field_147333_a.getSocketAddress().toString() + ")"
				: String.valueOf(field_147333_a.getSocketAddress());
	}

	@Override
	public void onConnectionStateTransition(EnumConnectionState p_147232_1_, EnumConnectionState p_147232_2_) {
		Validate.validState(
				field_147328_g == NetHandlerLoginServer.LoginState.ACCEPTED
						|| field_147328_g == NetHandlerLoginServer.LoginState.HELLO,
				"Unexpected change in protocol");
		Validate.validState(p_147232_2_ == EnumConnectionState.PLAY || p_147232_2_ == EnumConnectionState.LOGIN,
				"Unexpected protocol " + p_147232_2_);
	}

	public void processLoginStart(C00PacketLoginStart p_147316_1_)
	{
		Validate.validState(field_147328_g == LoginState.HELLO, "Unexpected hello packet");
		field_147337_i = p_147316_1_.func_149304_c();

		if (this.field_147327_f.isServerInOnlineMode() && !this.field_147333_a.isLocalChannel())
		{
			field_147328_g = LoginState.KEY;
			this.field_147333_a.scheduleOutboundPacket(new S01PacketEncryptionRequest(this.field_147334_j, this.field_147327_f.getKeyPair().getPublic(), this.field_147330_e));
		}
		else
		{
			(new ThreadPlayerLookupUUID(this, "User Authenticator #" + field_147331_b.incrementAndGet())).start(); // Spigot
		}
	}

	// Spigot start
	public void initUUID()
	{
		UUID uuid;
		if ( field_147333_a.spoofedUUID != null )
		{
			uuid = field_147333_a.spoofedUUID;
		} else
		{
			uuid = UUID.nameUUIDFromBytes( ( "OfflinePlayer:" + field_147337_i.getName() ).getBytes( Charsets.UTF_8 ) );
		}

		field_147337_i = new GameProfile( uuid, field_147337_i.getName() );

		if (field_147333_a.spoofedProfile != null)
		{
			for ( Property property : field_147333_a.spoofedProfile )
			{
				field_147337_i.getProperties().put( property.getName(), property );
			}
		}
	}
	// Spigot end

	@Override
	public void processEncryptionResponse(C01PacketEncryptionResponse p_147315_1_) {
		Validate.validState(field_147328_g == NetHandlerLoginServer.LoginState.KEY, "Unexpected key packet"
		);
		PrivateKey privatekey = field_147327_f.getKeyPair().getPrivate();

		if (!Arrays.equals(field_147330_e, p_147315_1_.func_149299_b(privatekey)))
			throw new IllegalStateException("Invalid nonce!");
		else {
			field_147335_k = p_147315_1_.func_149300_a(privatekey);
			field_147328_g = NetHandlerLoginServer.LoginState.AUTHENTICATING;
			field_147333_a.enableEncryption(field_147335_k);
			GlobalExecutors.cachedIO().execute(new Runnable() {
				private static final String __OBFID = "CL_00001459";

				@Override
				public void run() {
					GameProfile gameprofile = field_147337_i;

					try {
						String s = new BigInteger(CryptManager.getServerIdHash(field_147334_j,
								field_147327_f.getKeyPair().getPublic(), field_147335_k)).toString(16);
						field_147337_i = field_147327_f.func_147130_as()
								.hasJoinedServer(new GameProfile(null, gameprofile.getName()), s);

						if (field_147337_i != null) {
							NetHandlerLoginServer.logger.info(
									"\u00a79[Connect]   \u00a7r User '\u00a79{}\u00a7r' UUID: '{}'",
									field_147337_i.getName(), field_147337_i.getId());
							field_147328_g = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
						} else if (field_147327_f.isSinglePlayer()) {
							NetHandlerLoginServer.logger.warn("Failed to verify username but will let them in anyway!");
							field_147337_i = NetHandlerLoginServer.this.func_152506_a(gameprofile);
							field_147328_g = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
						} else {
							NetHandlerLoginServer.this.func_147322_a("Failed to verify username!");
							NetHandlerLoginServer.logger.error("Username '" + field_147337_i.getName()
									+ "' tried to join with an invalid session");
						}
					} catch (AuthenticationUnavailableException authenticationunavailableexception) {
						if (field_147327_f.isSinglePlayer()) {
							NetHandlerLoginServer.logger
									.warn("Authentication servers are down but will let them in anyway!");
							field_147337_i = NetHandlerLoginServer.this.func_152506_a(gameprofile);
							field_147328_g = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
						} else {
							NetHandlerLoginServer.this
									.func_147322_a("Authentication servers are down. Please try again later, sorry!");
							NetHandlerLoginServer.logger
									.error("Couldn't verify username because servers are unavailable");
						}
					}
				}
			});
		}
	}

	public GameProfile func_152506_a(GameProfile p_152506_1_) {
		UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + p_152506_1_.getName()).getBytes(Charsets.UTF_8));
		return new GameProfile(uuid, p_152506_1_.getName());
	}

	public enum LoginState {
		HELLO, KEY, AUTHENTICATING, READY_TO_ACCEPT, ACCEPTED

	}

	// Cauldron start - access methods for ThreadPlayerLookupUUID
	public static String getLoginServerId(NetHandlerLoginServer loginServer) {
		return loginServer.field_147334_j;
	}

	public static MinecraftServer getMinecraftServer(NetHandlerLoginServer loginServer) {
		return loginServer.field_147327_f;
	}

	public static SecretKey getSecretKey(NetHandlerLoginServer loginServer) {
		return loginServer.field_147335_k;
	}

	public static GameProfile processPlayerLoginGameProfile(NetHandlerLoginServer loginServer, GameProfile gameprofile) {
		return field_147337_i = gameprofile;
	}

	public static GameProfile getGameProfile(NetHandlerLoginServer loginServer) {
		return field_147337_i;
	}


	public static void setLoginState(NetHandlerLoginServer loginServer, LoginState state)
	{
		field_147328_g = state;
	}

	public static GameProfile getGameProfile() {
		return field_147337_i;
	}

	public static org.apache.logging.log4j.Logger getLogger() {
		return logger;
	}

	public NetworkManager getNetworkManager() {
		return field_147333_a;
	}

	public static void setLoginState(NetHandlerLoginServer.LoginState state) {
		field_147328_g = state;
	}
}