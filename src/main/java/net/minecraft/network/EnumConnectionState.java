package net.minecraft.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import org.apache.logging.log4j.LogManager;

import java.util.Map;

public enum EnumConnectionState {
	HANDSHAKING(-1) {
		{
			func_150751_a(0, C00Handshake.class);
		}
	},
	PLAY(0) {
		{
			func_150756_b(0, S00PacketKeepAlive.class);
			func_150756_b(1, S01PacketJoinGame.class);
			func_150756_b(2, S02PacketChat.class);
			func_150756_b(3, S03PacketTimeUpdate.class);
			func_150756_b(4, S04PacketEntityEquipment.class);
			func_150756_b(5, S05PacketSpawnPosition.class);
			func_150756_b(6, S06PacketUpdateHealth.class);
			func_150756_b(7, S07PacketRespawn.class);
			func_150756_b(8, S08PacketPlayerPosLook.class);
			func_150756_b(9, S09PacketHeldItemChange.class);
			func_150756_b(10, S0APacketUseBed.class);
			func_150756_b(11, S0BPacketAnimation.class);
			func_150756_b(12, S0CPacketSpawnPlayer.class);
			func_150756_b(13, S0DPacketCollectItem.class);
			func_150756_b(14, S0EPacketSpawnObject.class);
			func_150756_b(15, S0FPacketSpawnMob.class);
			func_150756_b(16, S10PacketSpawnPainting.class);
			func_150756_b(17, S11PacketSpawnExperienceOrb.class);
			func_150756_b(18, S12PacketEntityVelocity.class);
			func_150756_b(19, S13PacketDestroyEntities.class);
			func_150756_b(20, S14PacketEntity.class);
			func_150756_b(21, S14PacketEntity.S15PacketEntityRelMove.class);
			func_150756_b(22, S14PacketEntity.S16PacketEntityLook.class);
			func_150756_b(23, S14PacketEntity.S17PacketEntityLookMove.class);
			func_150756_b(24, S18PacketEntityTeleport.class);
			func_150756_b(25, S19PacketEntityHeadLook.class);
			func_150756_b(26, S19PacketEntityStatus.class);
			func_150756_b(27, S1BPacketEntityAttach.class);
			func_150756_b(28, S1CPacketEntityMetadata.class);
			func_150756_b(29, S1DPacketEntityEffect.class);
			func_150756_b(30, S1EPacketRemoveEntityEffect.class);
			func_150756_b(31, S1FPacketSetExperience.class);
			func_150756_b(32, S20PacketEntityProperties.class);
			func_150756_b(33, S21PacketChunkData.class);
			func_150756_b(34, S22PacketMultiBlockChange.class);
			func_150756_b(35, S23PacketBlockChange.class);
			func_150756_b(36, S24PacketBlockAction.class);
			func_150756_b(37, S25PacketBlockBreakAnim.class);
			func_150756_b(38, S26PacketMapChunkBulk.class);
			func_150756_b(39, S27PacketExplosion.class);
			func_150756_b(40, S28PacketEffect.class);
			func_150756_b(41, S29PacketSoundEffect.class);
			func_150756_b(42, S2APacketParticles.class);
			func_150756_b(43, S2BPacketChangeGameState.class);
			func_150756_b(44, S2CPacketSpawnGlobalEntity.class);
			func_150756_b(45, S2DPacketOpenWindow.class);
			func_150756_b(46, S2EPacketCloseWindow.class);
			func_150756_b(47, S2FPacketSetSlot.class);
			func_150756_b(48, S30PacketWindowItems.class);
			func_150756_b(49, S31PacketWindowProperty.class);
			func_150756_b(50, S32PacketConfirmTransaction.class);
			func_150756_b(51, S33PacketUpdateSign.class);
			func_150756_b(52, S34PacketMaps.class);
			func_150756_b(53, S35PacketUpdateTileEntity.class);
			func_150756_b(54, S36PacketSignEditorOpen.class);
			func_150756_b(55, S37PacketStatistics.class);
			func_150756_b(56, S38PacketPlayerListItem.class);
			func_150756_b(57, S39PacketPlayerAbilities.class);
			func_150756_b(58, S3APacketTabComplete.class);
			func_150756_b(59, S3BPacketScoreboardObjective.class);
			func_150756_b(60, S3CPacketUpdateScore.class);
			func_150756_b(61, S3DPacketDisplayScoreboard.class);
			func_150756_b(62, S3EPacketTeams.class);
			func_150756_b(63, S3FPacketCustomPayload.class);
			func_150756_b(64, S40PacketDisconnect.class);
			func_150751_a(0, C00PacketKeepAlive.class);
			func_150751_a(1, C01PacketChatMessage.class);
			func_150751_a(2, C02PacketUseEntity.class);
			func_150751_a(3, C03PacketPlayer.class);
			func_150751_a(4, C03PacketPlayer.C04PacketPlayerPosition.class);
			func_150751_a(5, C03PacketPlayer.C05PacketPlayerLook.class);
			func_150751_a(6, C03PacketPlayer.C06PacketPlayerPosLook.class);
			func_150751_a(7, C07PacketPlayerDigging.class);
			func_150751_a(8, C08PacketPlayerBlockPlacement.class);
			func_150751_a(9, C09PacketHeldItemChange.class);
			func_150751_a(10, C0APacketAnimation.class);
			func_150751_a(11, C0BPacketEntityAction.class);
			func_150751_a(12, C0CPacketInput.class);
			func_150751_a(13, C0DPacketCloseWindow.class);
			func_150751_a(14, C0EPacketClickWindow.class);
			func_150751_a(15, C0FPacketConfirmTransaction.class);
			func_150751_a(16, C10PacketCreativeInventoryAction.class);
			func_150751_a(17, C11PacketEnchantItem.class);
			func_150751_a(18, C12PacketUpdateSign.class);
			func_150751_a(19, C13PacketPlayerAbilities.class);
			func_150751_a(20, C14PacketTabComplete.class);
			func_150751_a(21, C15PacketClientSettings.class);
			func_150751_a(22, C16PacketClientStatus.class);
			func_150751_a(23, C17PacketCustomPayload.class);
		}
	},
	STATUS(1) {
		{
			func_150751_a(0, C00PacketServerQuery.class);
			func_150756_b(0, S00PacketServerInfo.class);
			func_150751_a(1, C01PacketPing.class);
			func_150756_b(1, S01PacketPong.class);
		}
	},
	LOGIN(2) {
		{
			func_150756_b(0, S00PacketDisconnect.class);
			func_150756_b(1, S01PacketEncryptionRequest.class);
			func_150756_b(2, S02PacketLoginSuccess.class);
			func_150751_a(0, C00PacketLoginStart.class);
			func_150751_a(1, C01PacketEncryptionResponse.class);
		}
	};
	private static final TIntObjectMap<EnumConnectionState> field_150764_e = new TIntObjectHashMap();
	private static final Map<Class<? extends Packet>, EnumConnectionState> field_150761_f = Maps.newHashMap();
	private final int field_150762_g;
	private final BiMap<Integer, Class<Packet>> field_150769_h;
	private final BiMap<Integer, Class<Packet>> field_150770_i;

	EnumConnectionState(int p_i45152_3_) {
		field_150769_h = HashBiMap.create();
		field_150770_i = HashBiMap.create();
		field_150762_g = p_i45152_3_;
	}

	protected EnumConnectionState func_150751_a(int p_150751_1_, Class<? extends Packet> p_150751_2_) {
		String s;

		if (field_150769_h.containsKey(p_150751_1_)) {
			s = "Serverbound packet ID " + p_150751_1_ + " is already assigned to "
					+ field_150769_h.get(p_150751_1_) + "; cannot re-assign to " + p_150751_2_;
			LogManager.getLogger().fatal(s);
			throw new IllegalArgumentException(s);
		} else if (field_150769_h.containsValue(p_150751_2_)) {
			s = "Serverbound packet " + p_150751_2_ + " is already assigned to ID "
					+ field_150769_h.inverse().get(p_150751_2_) + "; cannot re-assign to " + p_150751_1_;
			LogManager.getLogger().fatal(s);
			throw new IllegalArgumentException(s);
		} else {
			field_150769_h.put(p_150751_1_, (Class<Packet>) p_150751_2_);
			return this;
		}
	}

	protected EnumConnectionState func_150756_b(int p_150756_1_, Class<? extends Packet> p_150756_2_) {
		String s;

		if (field_150770_i.containsKey(p_150756_1_)) {
			s = "Clientbound packet ID " + p_150756_1_ + " is already assigned to "
					+ field_150770_i.get(p_150756_1_) + "; cannot re-assign to " + p_150756_2_;
			LogManager.getLogger().fatal(s);
			throw new IllegalArgumentException(s);
		} else if (field_150770_i.containsValue(p_150756_2_)) {
			s = "Clientbound packet " + p_150756_2_ + " is already assigned to ID "
					+ field_150770_i.inverse().get(p_150756_2_) + "; cannot re-assign to " + p_150756_1_;
			LogManager.getLogger().fatal(s);
			throw new IllegalArgumentException(s);
		} else {
			field_150770_i.put(p_150756_1_, (Class<Packet>) p_150756_2_);
			return this;
		}
	}

	public BiMap<Integer, Class<Packet>> func_150753_a() {
		return field_150769_h;
	}

	public BiMap<Integer, Class<Packet>> func_150755_b() {
		return field_150770_i;
	}

	public BiMap<Integer, Class<Packet>> func_150757_a(boolean p_150757_1_) {
		return p_150757_1_ ? func_150755_b() : func_150753_a();
	}

	public BiMap<Integer, Class<Packet>> func_150754_b(boolean p_150754_1_) {
		return p_150754_1_ ? func_150753_a() : func_150755_b();
	}

	public int func_150759_c() {
		return field_150762_g;
	}

	public static EnumConnectionState func_150760_a(int p_150760_0_) {
		return field_150764_e.get(p_150760_0_);
	}

	public static EnumConnectionState func_150752_a(Packet p_150752_0_) {
		return field_150761_f.get(p_150752_0_.getClass());
	}

	EnumConnectionState(int p_i1197_3_, Object p_i1197_4_) {
		this(p_i1197_3_);
	}

	static {
		EnumConnectionState[] var0 = values();
		int var1 = var0.length;

		for (EnumConnectionState var3 : var0) {
			field_150764_e.put(var3.func_150759_c(), var3);

			for (Class<Packet> packetClass : Iterables.concat(var3.func_150755_b().values(), var3.func_150753_a().values())) {

				if (field_150761_f.containsKey(packetClass) && field_150761_f.get(packetClass) != var3)
					throw new Error("Packet " + packetClass + " is already assigned to protocol " + field_150761_f.get(packetClass)
							+ " - can't reassign to " + var3);

				field_150761_f.put(packetClass, var3);
			}
		}
	}
}