package org.ultramine.server;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.functions.GenericIterableFactory;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import org.ultramine.server.util.BasicTypeFormatter;

import java.io.File;

import static net.minecraft.util.EnumChatFormatting.DARK_PURPLE;

@SideOnly(Side.SERVER)
public class Restarter
{
	private static Restarter currentRestarter;
	private ServerConfigurationManager mgr;
	private int toRestart;
	private int ticks;
	public static String osPrefix = isWindows() ? ".bat" : ".sh";
	public static boolean restart;
	
	private Restarter(int seconds)
	{
		toRestart = seconds;
		mgr = MinecraftServer.getServer().getConfigurationManager();
		// s5a4ed1sa7 code start
		restart = false;
		// s5a4ed1sa7 code end
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ServerTickEvent e)
	{
		if(e.phase == TickEvent.Phase.START)
		{
			if(++ticks % 20 == 0)
				onSecond();
		}
	}
	
	public void onSecond()
	{
		if(toRestart <= 0)
		{
			initiateRestart();
		}
		else
		{
			if(toRestart == 3600 || toRestart == 1800 || toRestart == 900 || toRestart == 600 || toRestart == 300 || toRestart == 120 || toRestart == 60 ||
					toRestart == 40 || toRestart == 20 || toRestart == 15 || toRestart < 11)
			{
				mgr.sendChatMsg(new ChatComponentTranslation("ultramine.restart.after", BasicTypeFormatter.formatTime(toRestart*1000, true))
						.setChatStyle(new ChatStyle().setColor(DARK_PURPLE)));
				if(toRestart == 1)
				{
					mgr.sendChatMsg(new ChatComponentTranslation("ultramine.restart.now1").setChatStyle(new ChatStyle().setColor(DARK_PURPLE)));
					mgr.sendChatMsg(new ChatComponentTranslation("ultramine.restart.now2").setChatStyle(new ChatStyle().setColor(DARK_PURPLE)));
				}
			}
		}
		
		toRestart--;
	}
	
	private void initiateRestart()
	{
		mgr.saveAllPlayerData();
		for(EntityPlayerMP player : GenericIterableFactory.newCastingIterable(mgr.playerEntityList, EntityPlayerMP.class))
			player.playerNetServerHandler.kickPlayerFromServer("\u00a75"+player.translate("ultramine.restart.kick1")+"\n\u00a7d"+player.translate("ultramine.restart.kick2"));
		// s5a4ed1sa7 code start
		restart = true;
		// s5a4ed1sa7 code end
		mgr.getServerInstance().initiateShutdown();
	}
	
	public static void restart(int seconds)
	{
		abort();
		currentRestarter = new Restarter(seconds);
		FMLCommonHandler.instance().bus().register(currentRestarter);
	}
	
	public static boolean abort()
	{
		if(currentRestarter != null)
		{
			FMLCommonHandler.instance().bus().unregister(currentRestarter);
			currentRestarter = null;
			return true;
		}
		
		return false;
	}
	// s5a4ed1sa7 code start, zaxar163 be like
	 public static boolean isWindows(){

	        String os = System.getProperty("os.name").toLowerCase();
	        //windows
	        return (os.indexOf( "win" ) >= 0); 

	    }
	 // s5a4ed1sa7 code end
	 
	 // s5a4ed1sa7 code start, spigot restart be like
	  @SuppressWarnings("static-access")
	public static void restart(boolean forbidShutdown) {
		  if(currentRestarter.restart) {
	        try {
	        	UltramineServerConfig usc = new UltramineServerConfig();
	        	String restartScript = usc.fixer.baseFixer.restartScriptName + osPrefix;
	            final File file = new File(restartScript);
	            if (file.isFile()) {
	                System.out.println("Attempting to restart with " + restartScript);

	                // Forbid new logons
	                net.minecraft.server.dedicated.DedicatedServer.allowPlayerLogins = false;
	                // Give the socket a chance to send the packets
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException ignored) {
	                }
	                // Close the socket so we can rebind with the new process
	                net.minecraft.server.MinecraftServer.getServer().func_147137_ag().terminateEndpoints();

	                // Give time for it to kick in
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException ignored) {
	                }

	                // This will be done AFTER the server has completely halted
	                Thread shutdownHook = new Thread(() -> {
						try {
						Runtime.getRuntime().exec("cmd /c start " + file.getPath());
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
	                shutdownHook.setDaemon(true);
	                Runtime.getRuntime().addShutdownHook(shutdownHook);
	            } else {
	                if (forbidShutdown) {
	                    System.out.println("Attempt to restart server without restart script, decline request");
	                    return;
	                }
	                System.out.println("Startup script '" + restartScript + "' does not exist! Stopping server.");
	            }
	            cpw.mods.fml.common.FMLCommonHandler.instance().exitJava(0, false);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	  }
	// s5a4ed1sa7 code end
}

