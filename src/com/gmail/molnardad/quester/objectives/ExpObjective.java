package com.gmail.molnardad.quester.objectives;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

import com.gmail.molnardad.quester.utils.ExpManager;

@SerializableAs("QuesterExpObjective")
public final class ExpObjective implements Objective {

	private final String TYPE = "EXPERIENCE";
	private final int amount;
	
	public ExpObjective(int amt) {
		amount = amt;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public int getTargetAmount() {
		return 0;
	}

	@Override
	public boolean isComplete(Player player, int progress) {
		return false;
	}

	@Override
	public boolean finish(Player player) {
		ExpManager expMan = new ExpManager(player);
		expMan.changeExp(-amount);
		return true;
	}
	
	@Override
	public String progress(int progress) {
		return "Have " + String.valueOf(amount) + " experience points on completion.";
	}
	
	@Override
	public String toString() {
		return TYPE + ": " + String.valueOf(amount);
	}
	
	public int takeExp(int amt) {
		return amt - amount;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("amount", amount);
		
		return map;
	}

	public static ExpObjective deserialize(Map<String, Object> map) {
		int amt;
		
		try {
			amt = (Integer) map.get("amount");
			if(amt < 1)
				return null;
		} catch (Exception e) {
			return null;
		}
		
		return new ExpObjective(amt);
	}
}
