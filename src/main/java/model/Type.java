package model;

import java.util.HashMap;

public enum Type {
	CHECKING(1), SAVINGS(2);

	private int value;
	private static HashMap<Integer, Type> map = new HashMap<Integer, Type>();

	private Type(int value) {
		this.value = value;
	}

	static {
		for (Type type : Type.values()) {
			map.put(type.value, type);
		}
	}

	public static Type valueOf(int type) {
		return (Type) map.get(type);
	}

	public int getValue() {
		return value;
	}
}
