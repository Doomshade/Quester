package com.gmail.molnardad.quester.listeners;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.molnardad.quester.ActionSource;
import com.gmail.molnardad.quester.Quester;
import com.gmail.molnardad.quester.elements.Objective;
import com.gmail.molnardad.quester.objectives.ActionObjective;
import com.gmail.molnardad.quester.profiles.PlayerProfile;
import com.gmail.molnardad.quester.profiles.ProfileManager;
import com.gmail.molnardad.quester.quests.Quest;
import com.gmail.molnardad.quester.utils.Util;

public class ActionListener implements Listener {
	
	private final ProfileManager profMan;
	
	public ActionListener(final Quester plugin) {
		profMan = plugin.getProfileManager();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onAction(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if(!Util.isPlayer(player)) {
			return;
		}
		final PlayerProfile prof = profMan.getProfile(player.getName());
		final Quest quest = prof.getQuest();
		if(quest != null) {
			if(!quest.allowedWorld(player.getWorld().getName().toLowerCase())) {
				return;
			}
			final List<Objective> objs = quest.getObjectives();
			final Block block = event.getClickedBlock();
			final ItemStack item = player.getItemInHand();
			for(int i = 0; i < objs.size(); i++) {
				if(objs.get(i).getType().equalsIgnoreCase("ACTION")) {
					if(!profMan.isObjectiveActive(prof, i)) {
						continue;
					}
					final ActionObjective obj = (ActionObjective) objs.get(i);
					if(block != null) {
						if(!obj.checkLocation(block.getLocation())) {
							continue;
						}
					}
					if(obj.checkClick(event.getAction()) && obj.checkBlock(block)
							&& obj.checkHand(item)) {
						profMan.incProgress(player, ActionSource.listenerSource(event), i);
						return;
					}
				}
			}
		}
	}
	
}
