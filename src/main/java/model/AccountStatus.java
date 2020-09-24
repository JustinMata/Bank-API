package model;

public class AccountStatus {
	private int statusId; // primary key
	private Status status; // not null, unique

	public AccountStatus() {
		super();
	}
	
	public AccountStatus(Status status) {
		super();
		this.statusId = status.getValue();
		this.status = status;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "AccountStatus [statusId = " + statusId + ", status = " + status + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + statusId;
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
		AccountStatus other = (AccountStatus) obj;
		if (status != other.status)
			return false;
		if (statusId != other.statusId)
			return false;
		return true;
	}
}
