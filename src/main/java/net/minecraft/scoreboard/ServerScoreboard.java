package net.minecraft.scoreboard;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.server.MinecraftServer;

import java.util.*;

public class ServerScoreboard extends Scoreboard {
	private final MinecraftServer scoreboardMCServer;
	private final Set field_96553_b = new HashSet();
	private ScoreboardSaveData field_96554_c;
	private static final String __OBFID = "CL_00001424";

	public ServerScoreboard(MinecraftServer p_i1501_1_) {
		scoreboardMCServer = p_i1501_1_;
	}

	@Override
	public void func_96536_a(Score p_96536_1_) {
		super.func_96536_a(p_96536_1_);

		if (field_96553_b.contains(p_96536_1_.func_96645_d())) {
			scoreboardMCServer.getConfigurationManager()
					.sendPacketToAllPlayers(new S3CPacketUpdateScore(p_96536_1_, 0));
		}

		func_96551_b();
	}

	@Override
	public void func_96516_a(String p_96516_1_) {
		super.func_96516_a(p_96516_1_);
		scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3CPacketUpdateScore(p_96516_1_));
		func_96551_b();
	}

	@Override
	public void func_96530_a(int p_96530_1_, ScoreObjective p_96530_2_) {
		ScoreObjective scoreobjective1 = func_96539_a(p_96530_1_);
		super.func_96530_a(p_96530_1_, p_96530_2_);

		if (scoreobjective1 != p_96530_2_ && scoreobjective1 != null) {
			if (func_96552_h(scoreobjective1) > 0) {
				scoreboardMCServer.getConfigurationManager()
						.sendPacketToAllPlayers(new S3DPacketDisplayScoreboard(p_96530_1_, p_96530_2_));
			} else {
				func_96546_g(scoreobjective1);
			}
		}

		if (p_96530_2_ != null) {
			if (field_96553_b.contains(p_96530_2_)) {
				scoreboardMCServer.getConfigurationManager()
						.sendPacketToAllPlayers(new S3DPacketDisplayScoreboard(p_96530_1_, p_96530_2_));
			} else {
				func_96549_e(p_96530_2_);
			}
		}

		func_96551_b();
	}

	@Override
	public boolean func_151392_a(String p_151392_1_, String p_151392_2_) {
		if (super.func_151392_a(p_151392_1_, p_151392_2_)) {
			ScorePlayerTeam scoreplayerteam = getTeam(p_151392_2_);
			scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(
					new S3EPacketTeams(scoreplayerteam, Arrays.asList(p_151392_1_), 3));
			func_96551_b();
			return true;
		} else
			return false;
	}

	@Override
	public void removePlayerFromTeam(String p_96512_1_, ScorePlayerTeam p_96512_2_) {
		super.removePlayerFromTeam(p_96512_1_, p_96512_2_);
		scoreboardMCServer.getConfigurationManager()
				.sendPacketToAllPlayers(new S3EPacketTeams(p_96512_2_, Arrays.asList(p_96512_1_), 4));
		func_96551_b();
	}

	@Override
	public void func_96522_a(ScoreObjective p_96522_1_) {
		super.func_96522_a(p_96522_1_);
		func_96551_b();
	}

	@Override
	public void func_96532_b(ScoreObjective p_96532_1_) {
		super.func_96532_b(p_96532_1_);

		if (field_96553_b.contains(p_96532_1_)) {
			scoreboardMCServer.getConfigurationManager()
					.sendPacketToAllPlayers(new S3BPacketScoreboardObjective(p_96532_1_, 2));
		}

		func_96551_b();
	}

	@Override
	public void func_96533_c(ScoreObjective p_96533_1_) {
		super.func_96533_c(p_96533_1_);

		if (field_96553_b.contains(p_96533_1_)) {
			func_96546_g(p_96533_1_);
		}

		func_96551_b();
	}

	@Override
	public void broadcastTeamCreated(ScorePlayerTeam p_96523_1_) {
		super.broadcastTeamCreated(p_96523_1_);
		scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3EPacketTeams(p_96523_1_, 0));
		func_96551_b();
	}

	@Override
	public void broadcastTeamRemoved(ScorePlayerTeam p_96538_1_) {
		super.broadcastTeamRemoved(p_96538_1_);
		scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3EPacketTeams(p_96538_1_, 2));
		func_96551_b();
	}

	@Override
	public void func_96513_c(ScorePlayerTeam p_96513_1_) {
		super.func_96513_c(p_96513_1_);
		scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3EPacketTeams(p_96513_1_, 1));
		func_96551_b();
	}

	public void func_96547_a(ScoreboardSaveData p_96547_1_) {
		field_96554_c = p_96547_1_;
	}

	protected void func_96551_b() {
		if (field_96554_c != null) {
			field_96554_c.markDirty();
		}
	}

	public List func_96550_d(ScoreObjective p_96550_1_) {
		ArrayList arraylist = new ArrayList();
		arraylist.add(new S3BPacketScoreboardObjective(p_96550_1_, 0));

		for (int i = 0; i < 3; ++i) {
			if (func_96539_a(i) == p_96550_1_) {
				arraylist.add(new S3DPacketDisplayScoreboard(i, p_96550_1_));
			}
		}

		Iterator iterator = func_96534_i(p_96550_1_).iterator();

		while (iterator.hasNext()) {
			Score score = (Score) iterator.next();
			arraylist.add(new S3CPacketUpdateScore(score, 0));
		}

		return arraylist;
	}

	public void func_96549_e(ScoreObjective p_96549_1_) {
		List list = func_96550_d(p_96549_1_);
		Iterator iterator = scoreboardMCServer.getConfigurationManager().playerEntityList.iterator();

		while (iterator.hasNext()) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) iterator.next();
			Iterator iterator1 = list.iterator();

			while (iterator1.hasNext()) {
				Packet packet = (Packet) iterator1.next();
				entityplayermp.playerNetServerHandler.sendPacket(packet);
			}
		}

		field_96553_b.add(p_96549_1_);
	}

	public List func_96548_f(ScoreObjective p_96548_1_) {
		ArrayList arraylist = new ArrayList();
		arraylist.add(new S3BPacketScoreboardObjective(p_96548_1_, 1));

		for (int i = 0; i < 3; ++i) {
			if (func_96539_a(i) == p_96548_1_) {
				arraylist.add(new S3DPacketDisplayScoreboard(i, p_96548_1_));
			}
		}

		return arraylist;
	}

	public void func_96546_g(ScoreObjective p_96546_1_) {
		List list = func_96548_f(p_96546_1_);
		Iterator iterator = scoreboardMCServer.getConfigurationManager().playerEntityList.iterator();

		while (iterator.hasNext()) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP) iterator.next();
			Iterator iterator1 = list.iterator();

			while (iterator1.hasNext()) {
				Packet packet = (Packet) iterator1.next();
				entityplayermp.playerNetServerHandler.sendPacket(packet);
			}
		}

		field_96553_b.remove(p_96546_1_);
	}

	public int func_96552_h(ScoreObjective p_96552_1_) {
		int i = 0;

		for (int j = 0; j < 3; ++j) {
			if (func_96539_a(j) == p_96552_1_) {
				++i;
			}
		}

		return i;
	}
}