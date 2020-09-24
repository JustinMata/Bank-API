package model;

public class Role {
	private int roleId; // primary key
	private Roles role; // not null, unique

	public Role() {
		super();
	}

	public Role(Roles role) {
		super();
		this.roleId = role.getValue();
		this.role = role;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Role [roleId = " + roleId + ", role = " + role + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + roleId;
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
		Role other = (Role) obj;
		if (role != other.role)
			return false;
		if (roleId != other.roleId)
			return false;
		return true;
	}

}