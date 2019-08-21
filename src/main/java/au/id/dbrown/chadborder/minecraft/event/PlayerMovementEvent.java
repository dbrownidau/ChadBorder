package au.id.dbrown.chadborder.minecraft.event;

import java.util.Stack;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovementEvent implements Listener {

	int centerX = 0;
	int centerZ = 0;
	int size = 100;
	int buffer = 10;

	int borderX = centerX + (size / 2);
	int borderZ = centerZ + (size / 2);
	int negX = -borderX; // (x *= -1) is buggy
	int negZ = -borderZ;

	int playerX = 0;
	int playerZ = 0;

	Stack<Location> locations = new Stack<Location>();

	@EventHandler
	public void onPlayerMovement(PlayerMoveEvent event) {

		BlockData bd = Material.STONE.createBlockData();

		playerX = event.getPlayer().getLocation().getBlockX();
		playerZ = event.getPlayer().getLocation().getBlockZ();

		// TODO
		// Also need to remove blocks once they leave.

		// Check X border
		if (isWithinBuffer(playerX, borderX, buffer)) {
			if (isWithinTangent(playerZ, borderZ)) {
				if (playerX < 0) {
					Location loc = new Location(event.getPlayer().getWorld(), negX,
							event.getPlayer().getLocation().getY(), playerZ);
					event.getPlayer().sendBlockChange(loc, bd);
					pushToStack(loc);
				} else {
					Location loc = new Location(event.getPlayer().getWorld(), borderX,
							event.getPlayer().getLocation().getY(), playerZ);
					event.getPlayer().sendBlockChange(loc, bd);
					pushToStack(loc);
				}
			}
		}

		// Check Z border
		if (isWithinBuffer(playerZ, borderZ, buffer)) {
			if (isWithinTangent(playerX, borderX)) {
				if (playerZ < 0) {
					Location loc = new Location(event.getPlayer().getWorld(), playerX,
							event.getPlayer().getLocation().getY(), negZ);
					event.getPlayer().sendBlockChange(loc, bd);
					pushToStack(loc);
				} else {
					Location loc = new Location(event.getPlayer().getWorld(), playerX,
							event.getPlayer().getLocation().getY(), borderZ);
					event.getPlayer().sendBlockChange(loc, bd);
					pushToStack(loc);
				}
			}
		}

		// Heh, pretty nice aye.
		if (locations.size() == 30) {
			Location loc = locations.pop();
			BlockData oldbd = event.getPlayer().getWorld().getBlockAt(loc).getBlockData();
			event.getPlayer().sendBlockChange(loc, oldbd);
		}

		if (isInside(event.getPlayer().getLocation(), borderX, borderZ)) {
			Player player = event.getPlayer();
			// X
			if (Math.abs(playerX) < Math.abs(borderX)) {
				if (playerX > 0) {
					player.teleport(new Location(player.getWorld(), borderX + 1, player.getLocation().getBlockY(),
							playerZ, player.getLocation().getPitch(), player.getLocation().getYaw()));
				} else {
					player.teleport(new Location(player.getWorld(), negX - 1, player.getLocation().getBlockY(), playerZ,
							player.getLocation().getPitch(), player.getLocation().getYaw()));
				}
			}
			// Z
			if (Math.abs(playerZ) < Math.abs(borderZ)) {
				if (playerZ > 0) {
					player.teleport(new Location(player.getWorld(), playerX, player.getLocation().getBlockY(),
							borderZ + 1, player.getLocation().getPitch(), player.getLocation().getYaw()));
				} else {
					player.teleport(new Location(player.getWorld(), playerX, player.getLocation().getBlockY(), negZ - 1,
							player.getLocation().getPitch(), player.getLocation().getYaw()));
				}
			}
		}
	}

	// Heh, pretty nice aye.
	public void pushToStack(Location loc) {
		if (!locations.contains(loc)) {
			locations.add(0, loc);
		}
	}

	/**
	 * Checks if the provided location is within the tangential border.
	 * 
	 * @param location entity location
	 * @param tangent  tangential border
	 * @return
	 */
	public boolean isWithinTangent(int location, int tangent) {
		if (Math.abs(location) < Math.abs(tangent)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the provided location is within buffer units of the border.
	 * 
	 * @param location entity location.
	 * @param border
	 * @param buffer
	 * @return
	 */
	public boolean isWithinBuffer(int location, int border, int buffer) {
		if ((Math.abs(location) - Math.abs(border) <= Math.abs(buffer)) && (Math.abs(location) > Math.abs(border))) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if location is inside bounding box
	 * 
	 * @param location
	 * @param X
	 * @param Z
	 * @return
	 */
	public boolean isInside(Location location, int X, int Z) {
		if (Math.abs(location.getBlockX()) < Math.abs(X)) {
			if (Math.abs(location.getBlockZ()) < Math.abs(Z)) {
				return true;
			}
		}
		if (Math.abs(location.getBlockZ()) < Math.abs(Z)) {
			if (Math.abs(location.getBlockX()) < Math.abs(X)) {
				return true;
			}
		}
		return false;
	}

}
