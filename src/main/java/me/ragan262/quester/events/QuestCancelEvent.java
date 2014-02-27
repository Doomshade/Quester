package me.ragan262.quester.events;

import me.ragan262.quester.ActionSource;
import me.ragan262.quester.quests.Quest;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class QuestCancelEvent extends QuesterEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	private boolean cancelled = false;
	private final Player player;
	private final Quest quest;
	private final ActionSource actionSource;
	
	public QuestCancelEvent(final ActionSource actionSource, final Player player, final Quest quest) {
		this.player = player;
		this.quest = quest;
		this.actionSource = actionSource;
	}
	
	public ActionSource getActionSource() {
		return actionSource;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Quest getQuest() {
		return quest;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(final boolean value) {
		cancelled = false;
	}
	
}
