package model;

public class AccountType {
	private int typeId; // primary key
	private Type type; // not null, unique

	public AccountType() {
		super();
	}

	public AccountType(Type type) {
		super();
		this.typeId = type.getValue();
		this.type = type;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "AccountType [typeId = " + typeId + ", type = " + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + typeId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountType other = (AccountType) obj;
		if (type != other.type)
			return false;
		if (typeId != other.typeId)
			return false;
		return true;
	}

}
