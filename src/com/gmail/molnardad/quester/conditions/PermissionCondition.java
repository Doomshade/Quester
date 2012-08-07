package com.gmail.molnardad.quester.conditions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

import com.gmail.molnardad.quester.utils.Util;

@SerializableAs("QuesterPermissionCondition")
public final class PermissionCondition implements Condition {
	
	private final String TYPE = "PERMISSION";
	private final String perm;
	
	public PermissionCondition(String perm) {
		this.perm = perm;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public boolean isMet(Player player) {
		return Util.permCheck(player, perm, false);
	}
	
	@Override
	public String show() {
		return "Must have permission '" + perm + "'";
	}
	
	@Override
	public String toString() {
		return TYPE + ": " + perm;
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("permission", perm);
		
		return map;
	}

	public static PermissionCondition deserialize(Map<String, Object> map) {
		String prm;
		try {
			prm = (String) map.get("permission");
		} catch (Exception e) {
			return null;
		}
		
		return new PermissionCondition(prm);
	}
}