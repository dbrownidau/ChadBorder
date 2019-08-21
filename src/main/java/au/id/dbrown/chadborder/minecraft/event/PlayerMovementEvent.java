package au.id.dbrown.chadborder.minecraft.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovementEvent implements Listener {

	int centerX = 0;
	int centerZ = 0;
	int size = 100;

	int borderX = centerX + (size / 2);
	int borderZ = centerZ + (size / 2);

	int playerX;
	int playerZ;

	@EventHandler
	public void onPlayerMovement(PlayerMoveEvent event) {

		playerX = event.getPlayer().getLocation().getBlockX();
		playerZ = event.getPlayer().getLocation().getBlockZ();
		
		//TODO
		// One of my axis generates blocks on both side for some reason
		// Also need to remove blocks once they leave.

		// Check X border
		if (isWithinBuffer(playerX, borderX, 5)) {
			if (isWithinTangent(playerZ, borderZ)) {
				if (playerX < 0) {
					event.getPlayer().sendBlockChange(
							new Location(event.getPlayer().getWorld(), (borderX *= -1),
									event.getPlayer().getLocation().getY(), playerZ),
							Material.COBBLESTONE.createBlockData());
				} else {
					event.getPlayer().sendBlockChange(new Location(event.getPlayer().getWorld(), borderX,
							event.getPlayer().getLocation().getY(), playerZ), Material.COBBLESTONE.createBlockData());
				}
			}
		}

		// Check Z border
		if (isWithinBuffer(playerZ, borderZ, 5)) {
			if (isWithinTangent(playerX, borderX)) {
				if (playerZ < 0) {
					event.getPlayer()
							.sendBlockChange(
									new Location(event.getPlayer().getWorld(), playerX,
											event.getPlayer().getLocation().getY(), (borderZ *= -1)),
									Material.COBBLESTONE.createBlockData());
				} else {
					event.getPlayer().sendBlockChange(new Location(event.getPlayer().getWorld(), playerX,
							event.getPlayer().getLocation().getY(), borderZ), Material.COBBLESTONE.createBlockData());
				}
			}
		}
	}

	/**
	 * Checks if the provided location is within the tangential border.
	 * 
	 * @param location entity location
	 * @param tangent  tangential border
	 * @return
	 */
	public static boolean isWithinTangent(int location, int tangent) {
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
	public static boolean isWithinBuffer(int location, int border, int buffer) {
		if ((Math.abs(location) - Math.abs(border) <= Math.abs(buffer)) && (Math.abs(location) > Math.abs(border))) {
			return true;
		}
		return false;
	}

}
