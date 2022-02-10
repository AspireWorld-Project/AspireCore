package org.bukkit.craftbukkit;

import org.bukkit.Location;
import org.bukkit.TravelAgent;

public class CraftTravelAgent extends net.minecraft.world.Teleporter implements TravelAgent {

	public static TravelAgent DEFAULT = null;

	private int searchRadius = 128;
	private int creationRadius = 16;
	private boolean canCreatePortal = true;

	public CraftTravelAgent(net.minecraft.world.WorldServer worldserver) {
		super(worldserver);
		if (DEFAULT == null && worldserver.provider.dimensionId == 0) { // Cauldron
			DEFAULT = this;
		}
	}

	@Override
	public Location findOrCreate(Location target) {
		net.minecraft.world.WorldServer worldServer = ((CraftWorld) target.getWorld()).getHandle();
		boolean before = worldServer.theChunkProviderServer.loadChunkOnProvideRequest;
		worldServer.theChunkProviderServer.loadChunkOnProvideRequest = true;

		Location found = this.findPortal(target);
		if (found == null) {
			if (getCanCreatePortal() && this.createPortal(target)) {
				found = this.findPortal(target);
			} else {
				found = target; // fallback to original if unable to find or create
			}
		}

		worldServer.theChunkProviderServer.loadChunkOnProvideRequest = before;
		return found;
	}

	@Override
	public Location findPortal(Location location) {
		net.minecraft.world.Teleporter pta = ((CraftWorld) location.getWorld()).getHandle().getDefaultTeleporter(); // Should
																													// be
																													// getTravelAgent
		net.minecraft.util.ChunkCoordinates found = pta.findPortal(location.getX(), location.getY(), location.getZ(),
				getSearchRadius());
		return found != null
				? new Location(location.getWorld(), found.posX, found.posY, found.posZ, location.getYaw(),
						location.getPitch())
				: null;
	}

	@Override
	public boolean createPortal(Location location) {
		net.minecraft.world.Teleporter pta = ((CraftWorld) location.getWorld()).getHandle().getDefaultTeleporter();
		return pta.createPortal(location.getX(), location.getY(), location.getZ(), getCreationRadius());
	}

	@Override
	public TravelAgent setSearchRadius(int radius) {
		searchRadius = radius;
		return this;
	}

	@Override
	public int getSearchRadius() {
		return searchRadius;
	}

	@Override
	public TravelAgent setCreationRadius(int radius) {
		creationRadius = radius < 2 ? 0 : radius;
		return this;
	}

	@Override
	public int getCreationRadius() {
		return creationRadius;
	}

	@Override
	public boolean getCanCreatePortal() {
		return canCreatePortal;
	}

	@Override
	public void setCanCreatePortal(boolean create) {
		canCreatePortal = create;
	}
}
