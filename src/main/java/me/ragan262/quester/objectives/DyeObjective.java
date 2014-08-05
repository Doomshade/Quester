package me.ragan262.quester.objectives;

import me.ragan262.commandmanager.annotations.Command;
import me.ragan262.commandmanager.exceptions.CommandException;
import me.ragan262.quester.commandmanager.QuesterCommandContext;
import me.ragan262.quester.elements.Objective;
import me.ragan262.quester.elements.QElement;
import me.ragan262.quester.storage.StorageKey;
import me.ragan262.quester.utils.SerUtils;
import org.bukkit.DyeColor;

@QElement("DYE")
public final class DyeObjective extends Objective {
	
	private final int amount;
	private final DyeColor color;
	private final String colorName;
	
	public DyeObjective(final int amt, final DyeColor col) {
		amount = amt;
		color = col;
		if(col != null) {
			colorName = " " + col.name().toLowerCase().replaceAll("_", " ");
		}
		else {
			colorName = "";
		}
	}
	
	@Override
	public int getTargetAmount() {
		return amount;
	}
	
	@Override
	protected String show(final int progress) {
		return "Dye sheep" + colorName + " - " + (amount - progress) + "x";
	}
	
	@Override
	protected String info() {
		return amount
				+ "; COLOR:"
				+ (colorName.isEmpty() ? " ANY" : colorName + "(" + (15 - color.getDyeData()) + ")");
	}
	
	@Command(min = 1, max = 2, usage = "<amount> {[color]}")
	public static Objective fromCommand(final QuesterCommandContext context) throws CommandException {
		final int id = context.getInt(0);
		DyeColor col = null;
		if(id < 0) {
			throw new CommandException(context.getSenderLang().get("ERROR_CMD_AMOUNT_POSITIVE"));
		}
		if(context.length() > 1) {
			col = SerUtils.parseColor(context.getString(1));
			if(col == null) {
				throw new CommandException(context.getSenderLang().get("ERROR_CMD_COLOR_UNKNOWN"));
			}
		}
		return new DyeObjective(id, col);
	}
	
	@Override
	protected void save(final StorageKey key) {
		if(amount > 1) {
			key.setInt("amount", amount);
		}
		if(color != null) {
			key.setString("color", SerUtils.serializeColor(color));
		}
	}
	
	protected static Objective load(final StorageKey key) {
		int amt = 1;
		DyeColor col = null;
		amt = key.getInt("amount", 1);
		if(amt < 1) {
			amt = 1;
		}
		col = SerUtils.parseColor(key.getString("color", ""));
		return new DyeObjective(amt, col);
	}
	
	// Custom methods
	
	public boolean checkDye(final int data) {
		return color == null || color.getDyeData() == data;
	}
}
