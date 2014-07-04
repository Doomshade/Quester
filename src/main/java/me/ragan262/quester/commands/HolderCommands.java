package me.ragan262.quester.commands;

import me.ragan262.quester.Quester;
import me.ragan262.quester.commandbase.QCommand;
import me.ragan262.quester.commandbase.QCommandContext;
import me.ragan262.quester.commandbase.QCommandLabels;
import me.ragan262.quester.exceptions.HolderException;
import me.ragan262.quester.exceptions.QuesterException;
import me.ragan262.quester.holder.QuestHolder;
import me.ragan262.quester.holder.QuestHolderManager;
import me.ragan262.quester.lang.Messenger;
import me.ragan262.quester.profiles.ProfileManager;
import me.ragan262.quester.quests.QuestManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HolderCommands {
	
	final QuestHolderManager holMan;
	final ProfileManager profMan;
	final QuestManager qMan;
	final Messenger messenger;
	
	public HolderCommands(final Quester plugin) {
		holMan = plugin.getHolderManager();
		profMan = plugin.getProfileManager();
		qMan = plugin.getQuestManager();
		messenger = plugin.getMessenger();
	}
	
	@QCommandLabels({ "create", "c" })
	@QCommand(section = "Mod", desc = "creates a holder", min = 1, max = 1, usage = "<holder name>")
	public void set(final QCommandContext context, final CommandSender sender) throws QuesterException {
		final int id = holMan.createHolder(context.getString(0));
		profMan.selectHolder(profMan.getProfile(sender.getName()), id);
		sender.sendMessage(ChatColor.GREEN + context.getSenderLang().get("HOL_CREATED"));
	}
	
	@QCommandLabels({ "delete", "d" })
	@QCommand(section = "Mod", desc = "deletes a holder", min = 1, max = 1, usage = "<holder ID>")
	public void delete(final QCommandContext context, final CommandSender sender) throws QuesterException {
		holMan.removeHolder(context.getInt(0));
		sender.sendMessage(ChatColor.GREEN + context.getSenderLang().get("HOL_REMOVED"));
	}
	
	@QCommandLabels({ "add", "a" })
	@QCommand(
			section = "HMod",
			desc = "adds quest to holder",
			min = 1,
			max = 1,
			usage = "<quest ID>")
	public void add(final QCommandContext context, final CommandSender sender) throws QuesterException {
		holMan.addHolderQuest(sender.getName(), context.getInt(0), context.getSenderLang());
		sender.sendMessage(ChatColor.GREEN + context.getSenderLang().get("HOL_Q_ADDED"));
	}
	
	@QCommandLabels({ "remove", "r" })
	@QCommand(
			section = "HMod",
			desc = "removes quest from holder",
			min = 1,
			max = 1,
			usage = "<quest ID>")
	public void remove(final QCommandContext context, final CommandSender sender) throws QuesterException {
		holMan.removeHolderQuest(sender.getName(), context.getInt(0), context.getSenderLang());
		sender.sendMessage(ChatColor.GREEN + context.getSenderLang().get("HOL_Q_REMOVED"));
	}
	
	@QCommandLabels({ "move", "m" })
	@QCommand(
			section = "HMod",
			desc = "moves quest in holder",
			min = 2,
			max = 2,
			usage = "<from> <to>")
	public void move(final QCommandContext context, final CommandSender sender) throws QuesterException {
		holMan.moveHolderQuest(sender.getName(), context.getInt(0), context.getInt(1),
				context.getSenderLang());
		sender.sendMessage(ChatColor.GREEN + context.getSenderLang().get("HOL_Q_MOVED"));
	}
	
	@QCommandLabels({ "list", "l" })
	@QCommand(section = "Mod", desc = "lists quest holders", max = 0)
	public void list(final QCommandContext context, final CommandSender sender) throws QuesterException {
		messenger.showHolderList(sender, holMan);
	}
	
	@QCommandLabels({ "info", "i" })
	@QCommand(
			section = "Mod",
			desc = "shows info about holder",
			min = 0,
			max = 1,
			usage = "[holder ID]")
	public void info(final QCommandContext context, final CommandSender sender) throws QuesterException {
		int id;;
		if(context.length() > 0) {
			id = context.getInt(0);
		}
		else {
			id = profMan.getProfile(sender.getName()).getHolderID();
		}
		final QuestHolder qh = holMan.getHolder(id);
		if(qh == null) {
			if(id < 0) {
				throw new HolderException(context.getSenderLang().get("ERROR_HOL_NOT_SELECTED"));
			}
			else {
				throw new HolderException(context.getSenderLang().get("ERROR_HOL_NOT_EXIST"));
			}
		}
		sender.sendMessage(ChatColor.GOLD + "Holder ID: " + ChatColor.RESET + id);
		messenger.showHolderQuestsModify(qh, sender, qMan);
	}
	
	@QCommandLabels({ "select", "sel" })
	@QCommand(section = "Mod", desc = "selects holder", min = 1, max = 1, usage = "<holder ID>")
	public void select(final QCommandContext context, final CommandSender sender) throws QuesterException {
		profMan.selectHolder(profMan.getProfile(sender.getName()), context.getInt(0));
		sender.sendMessage(ChatColor.GREEN + context.getSenderLang().get("HOL_SELECTED"));
	}
}