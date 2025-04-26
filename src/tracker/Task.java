package tracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
	private int id;
	private String description;
	private String status;
	private String createAt;
	private String updatedAt;

	public Task(int id, String description, String status) {
		this.id = id;
		this.description = description;
		this.status = status;

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

		this.createAt = now.format(formatter);
		this.updatedAt = this.createAt;
	}

	public Task(int id, String description, String status, String createAt, String updateAt) {
		this.id = id;
		this.description = description;
		this.status = status;
		this.createAt = createAt;
		this.updatedAt = updateAt;
	}

	public void updateTimestamp() {
		this.updatedAt = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", description=" + description + ", status=" + status + ", createAt=" + createAt
				+ ", updatedAt=" + updatedAt + "]";
	}

}
