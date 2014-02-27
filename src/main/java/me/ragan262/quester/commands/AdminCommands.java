package me.ragan262.quester.commands;

import java.io.File;

import javax.management.InstanceNotFoundException;

import me.ragan262.quester.QConfiguration;
import me.ragan262.quester.Quester;
import me.ragan262.quester.commandbase.QCommand;
import me.ragan262.quester.commandbase.QCommandContext;
import me.ragan262.quester.commandbase.QCommandLabels;
import me.ragan262.quester.commandbase.QNestedCommand;
import me.ragan262.quester.holder.QuestHolderManager;
import me.ragan262.quester.lang.LanguageManager;
import me.ragan262.quester.profiles.PlayerProfile;
import me.ragan262.quester.profiles.ProfileManager;
import me.ragan262.quester.quests.QuestManager;
import me.ragan262.quester.utils.Ql;
import me.ragan262.quester.utils.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommands {
	
	final Quester plugin;
	final ProfileManager profMan;
	final QuestManager qMan;
	final QuestHolderManager holMan;
	final LanguageManager langMan;
	
	public AdminCommands(final Quester plugin) {
		this.plugin = plugin;
		profMan = plugin.getProfileManager();
		qMan = plugin.getQuestManager();
		holMan = plugin.getHolderManager();
		langMan = plugin.getLanguageManager();
	}
	
	@QCommandLabels({ "startsave" })
	@QCommand(
			section = "Admin",
			desc = "starts scheduled profile saving",
			max = 0,
			permission = QConfiguration.PERM_ADMIN)
	public void startsave(final QCommandContext context, final CommandSender sender) {
		if(QConfiguration.saveInterval == 0) {
			sender.sendMessage(ChatColor.RED + context.getSenderLang().get("MSG_AUTOSAVE_DISABLED"));
			return;
		}
		if(plugin.startSaving()) {
			sender.sendMessage(ChatColor.GREEN
					+ context.getSenderLang().get("MSG_AUTOSAVE_STARTED")
							.replaceAll("%interval", String.valueOf(QConfiguration.saveInterval)));
		}
		else {
			sender.sendMessage(ChatColor.RED + context.getSenderLang().get("MSG_AUTOSAVE_RUNNING"));
		}
	}
	
	@QCommandLabels({ "stopsave" })
	@QCommand(
			section = "Admin",
			desc = "stops scheduled profile saving",
			max = 0,
			permission = QConfiguration.PERM_ADMIN)
	public void stopsave(final QCommandContext context, final CommandSender sender) {
		if(QConfiguration.saveInterval == 0) {
			sender.sendMessage(ChatColor.RED + context.getSenderLang().get("MSG_AUTOSAVE_DISABLED"));
			return;
		}
		if(plugin.stopSaving()) {
			sender.sendMessage(ChatColor.GREEN
					+ context.getSenderLang().get("MSG_AUTOSAVE_STOPPED"));
		}
		else {
			sender.sendMessage(ChatColor.RED
					+ context.getSenderLang().get("MSG_AUTOSAVE_NOT_RUNNING"));
		}
	}
	
	@QCommandLabels({ "save" })
	@QCommand(
			section = "Admin",
			desc = "saves quests and profiles",
			max = 0,
			usage = "(-hpq)",
			permission = QConfiguration.PERM_ADMIN)
	public void save(final QCommandContext context, final CommandSender sender) {
		boolean pro, que, hol;
		pro = context.hasFlag('p');
		que = context.hasFlag('q');
		hol = context.hasFlag('h');
		if(pro || que || hol) {
			if(que) {
				qMan.saveQuests();
			}
			if(pro) {
				profMan.saveProfiles();
			}
			if(hol) {
				holMan.saveHolders();
			}
		}
		else {
			profMan.saveProfiles();
			qMan.saveQuests();
			holMan.saveHolders();
		}
		
		sender.sendMessage(context.getSenderLang().get("MSG_DATA_SAVE"));
	}
	
	@QCommandLabels({ "reload" })
	@QCommand(
			section = "Admin",
			desc = "reloads quests, config and languages",
			usage = "(-clq)",
			permission = QConfiguration.PERM_ADMIN)
	public void reload(final QCommandContext context, final CommandSender sender) {
		boolean con, que, lang;
		con = context.hasFlag('c');
		que = context.hasFlag('q');
		lang = context.hasFlag('l');
		if(con || que || lang) {
			if(con) {
				reloadData();
			}
			if(que) {
				reloadQuests();
			}
			if(lang) {
				langMan.clearCustomMessages();
				langMan.loadCustomMessages(new File(plugin.getDataFolder(), "messages.yml"));
				langMan.loadLangs();
			}
		}
		else {
			reloadData();
			
			reloadQuests();
			
			langMan.clearCustomMessages();
			langMan.loadCustomMessages(new File(plugin.getDataFolder(), "messages.yml"));
			langMan.loadLangs();
		}
		sender.sendMessage(ChatColor.GREEN + context.getSenderLang().get("MSG_DATA_RELOADED"));
	}
	
	// used above
	private void reloadData() {
		try {
			QConfiguration.reloadData();
			profMan.loadRanks();
			profMan.updateRanks();
		}
		catch (final InstanceNotFoundException e) {
			Ql.info("Failed to reload config: No instance of QConfiguration.");
		}
	}
	
	private void reloadQuests() {
		if(qMan.loadQuests()) {
			for(final PlayerProfile prof : profMan.getProfiles()) {
				final String[] unset = profMan.validateProgress(prof);
				Player player = null;
				if(unset.length > 0
						&& (player = Bukkit.getServer().getPlayerExact(prof.getName())) != null) {
					player.sendMessage(Quester.LABEL
							+ langMan.getPlayerLang(player.getName()).get("MSG_Q_SOME_CANCELLED"));
					player.sendMessage(Quester.LABEL + ChatColor.WHITE + Util.implode(unset, ','));
				}
			}
		}
		else {
			Ql.info("Failed to reload quests.");
		}
	}
	
	@QCommandLabels({ "version", "ver" })
	@QCommand(
			section = "Admin",
			desc = "version info",
			max = 0,
			permission = QConfiguration.PERM_ADMIN)
	public void version(final QCommandContext context, final CommandSender sender) {
		sender.sendMessage(Quester.LABEL + ChatColor.GOLD + "version "
				+ plugin.getDescription().getVersion());
		sender.sendMessage(Quester.LABEL + plugin.getDescription().getWebsite());
		sender.sendMessage(Quester.LABEL + ChatColor.GRAY + "made by "
				+ plugin.getDescription().getAuthors().get(0));
	}
	
	@QCommandLabels({ "player", "pl" })
	@QCommand(
			section = "Admin",
			desc = "player profile modification commands",
			permission = QConfiguration.PERM_ADMIN)
	@QNestedCommand(PlayerCommands.class)
	public void player(final QCommandContext context, final CommandSender sender) {
	}
	
	@QCommandLabels({ "message", "msg" })
	@QCommand(
			section = "Admin",
			desc = "custom messages manipulation",
			permission = QConfiguration.PERM_ADMIN)
	@QNestedCommand(MessageCommands.class)
	public void message(final QCommandContext context, final CommandSender sender) {
	}
}
