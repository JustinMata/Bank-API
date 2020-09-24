package model;

import java.util.HashMap;

public enum Status {
	PENDING(1), OPEN(2), CLOSED(3), DENIED(4);

	private int value;
	private static HashMap<Integer, Status> map = new HashMap<Integer, Status>();

	private Status(int value) {
		this.value = value;
	}

	static {
		for (Status status : Status.values()) {
			map.put(status.value, status);
		}
	}

	public static Status valueOf(int status) {
		return (Status) map.get(status);
	}

	public int getValue() {
		return value;
	}
}
