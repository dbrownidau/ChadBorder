package au.id.dbrown.chadborder;

import org.bukkit.plugin.java.JavaPlugin;

import au.id.dbrown.chadborder.minecraft.event.PlayerMovementEvent;

public class ChadBorder extends JavaPlugin {

	public enum props {
		Poo;
	}

	@Override
	public void onEnable() {

		// Event Listeners
		PlayerMovementEvent pme = new PlayerMovementEvent();

		// Register events
		this.getServer().getPluginManager().registerEvents(pme, this);

	}

}
