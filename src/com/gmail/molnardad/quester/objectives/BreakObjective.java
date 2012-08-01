package com.gmail.molnardad.quester.objectives;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

@SerializableAs("QuesterBreakObjective")
public final class BreakObjective implements Objective {

	private final String TYPE = "BREAK";
	private final Material material;
	private final byte data;
	private final int amount;
	
	public BreakObjective(int amt, Material mat, byte dat) {
		amount = amt;
		material = mat;
		data = dat;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public byte getData() {
		return data;
	}

	@Override
	public int getTargetAmount() {
		return amount;
	}

	@Override
	public boolean isComplete(Player player, int progress) {
		return progress >= amount;
	}
	
	@Override
	public String progress(int progress) {
		String datStr = data < 0 ? " of any type " : " of given type(" + data + ") ";
		return "Break " + material.name().toLowerCase() + datStr + "- " + (amount - progress) + "x.";
	}
	
	@Override
	public String toString() {
		String dataStr = (data < 0 ? "ANY" : String.valueOf(data));
		return TYPE + ": " + material.name() + "[" + material.getId() + "] DATA: " + dataStr + "; AMT: " + amount;
	}

	@Override
	public boolean finish(Player player) {
		return true;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("material", material.getId());
		map.put("data", data);
		map.put("amount", amount);
		
		return map;
	}

	public static BreakObjective deserialize(Map<String, Object> map) {
		Material mat;
		int dat, amt;
		
		try {
			mat = Material.getMaterial((Integer) map.get("material"));
			if(mat == null)
				return null;
			dat = (Integer) map.get("data");
			amt = (Integer) map.get("amount");
			if(amt < 1)
				return null;
			
			return new BreakObjective(amt, mat, (byte)dat);
		} catch (Exception e) {
			return null;
		}
	}
}
