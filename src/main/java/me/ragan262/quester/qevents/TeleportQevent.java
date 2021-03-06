package me.ragan262.quester.qevents;

import me.ragan262.commandmanager.annotations.Command;
import me.ragan262.commandmanager.exceptions.CommandException;
import me.ragan262.quester.Quester;
import me.ragan262.quester.commandmanager.QuesterCommandContext;
import me.ragan262.quester.elements.QElement;
import me.ragan262.quester.elements.Qevent;
import me.ragan262.quester.storage.StorageKey;
import me.ragan262.quester.utils.QLocation;
import me.ragan262.quester.utils.SerUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

@QElement("TELE")
public final class TeleportQevent extends Qevent {
	
	private final QLocation location;
	
	public TeleportQevent(final QLocation loc) {
		location = loc;
	}
	
	@Override
	public String info() {
		return SerUtils.displayLocation(location);
	}
	
	@Override
	protected void run(final Player player, final Quester plugin) {
		Location teleportTo = location.getLocation();
		Location loc = player.getLocation().clone();
		loc.setY(loc.getY() + 1);
		player.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 0);
		player.teleport(teleportTo, TeleportCause.PLUGIN);
		loc = teleportTo.clone();
		loc.setY(loc.getY() + 1);
		player.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1);
	}
	
	@Command(min = 1, max = 1, usage = "{<location>}")
	public static Qevent fromCommand(final QuesterCommandContext context) throws CommandException {
		final QLocation loc = SerUtils.getLoc(context.getPlayer(), context.getString(0), context.getSenderLang());
		if(loc == null) {
			throw new CommandException(context.getSenderLang().get("ERROR_CMD_LOC_INVALID"));
		}
		return new TeleportQevent(loc);
	}
	
	@Override
	protected void save(final StorageKey key) {
		key.setString("location", SerUtils.serializeLocString(location));
	}
	
	protected static Qevent load(final StorageKey key) {
		return new TeleportQevent(SerUtils.deserializeLocString(key.getString("location", "")));
	}
}
