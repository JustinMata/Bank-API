package model;

import java.util.HashMap;

public enum Roles {
	STANDARD(1), PREMIUM(2), EMPLOYEE(3), ADMIN(4);

	private int value;
	private static HashMap<Integer, Roles> map = new HashMap<Integer, Roles>();

	private Roles(int value) {
		this.value = value;
	}

	static {
		for (Roles role : Roles.values()) {
			map.put(role.value, role);
		}
	}

	public static Roles valueOf(int role) {
		return (Roles) map.get(role);
	}

	public int getValue() {
		return value;
	}
}
