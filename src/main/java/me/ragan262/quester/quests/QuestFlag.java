package me.ragan262.quester.quests;

import java.util.HashSet;
import java.util.Set;

public enum QuestFlag {
	
	ACTIVE(0), UNCANCELLABLE(2), ONLYFIRST(3), HIDDEN(4), HIDDENOBJS(5), NODESC(6), DEATHCANCEL(7), REPEATABLE(8);
	
	private final int type;
	
	QuestFlag(final int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static QuestFlag getByName(final String name) {
		try {
			return valueOf(name.toUpperCase());
		}
		catch (final Exception e) {}
		
		return null;
	}
	
	public static String stringize(final QuestFlag[] flags) {
		String result = "";
		final String gl = ", ";
		boolean first = true;
		for(final QuestFlag f : flags) {
			if(f.getType() == 0) {
				continue;
			}
			if(first) {
				result += f.name();
				first = false;
			}
			else {
				result += gl + f.name();
			}
		}
		return result;
	}
	
	public static String stringize(final Set<QuestFlag> flags) {
		return stringize(flags.toArray(new QuestFlag[0]));
	}
	
	public static String serialize(final Set<QuestFlag> flags) {
		String result = "";
		for(final QuestFlag f : flags) {
			result += f.name() + ";";
		}
		return result;
	}
	
	public static Set<QuestFlag> deserialize(final String input) {
		final Set<QuestFlag> flags = new HashSet<QuestFlag>();
		
		for(final String s : input.split(";")) {
			final QuestFlag f = QuestFlag.getByName(s);
			if(f != null) {
				flags.add(f);
			}
		}
		return flags;
	}
}
