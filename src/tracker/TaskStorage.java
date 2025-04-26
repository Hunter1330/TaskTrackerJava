package tracker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskStorage {
	private final static String FILE_NAME = "./tasks.json";

	// Helper Functions
	private static String escape(String input) {
		return input.replace("\"", "\\\"");
	}

	public static int getInt(String str, String key) {
		String search = "\"" + key + "\": ";
		int start = str.indexOf(search) + search.length();

		int end = str.indexOf(",", start);
		if (end == -1)
			end = str.indexOf("}", start);
		return Integer.parseInt(str.substring(start, end));
	}

	public static String getString(String str, String key) {
		String search = "\"" + key + "\": ";
		int start = str.indexOf(search) + search.length() + 1;

		int end = str.indexOf("\",", start);
		if (end == -1)
			end = str.indexOf("\"}", start);

		return str.substring(start, end);
	}

	public static void displayTask(Task t) {
		System.out.printf("[%d] %s - %s (Created: %s, Updated: %s)%n", t.getId(), t.getDescription(), t.getStatus(),
				t.getCreateAt(), t.getUpdatedAt());
	}

	// Reads file and returns string
	public static String ReadFileAsString() {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line.trim());
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return sb.toString();
	}

	// reads the file and returns it as List of tasks
	public static List<Task> loadTasks() {
		List<Task> Tasks = new ArrayList<Task>();
		String diskData = ReadFileAsString();

		if (diskData.startsWith("["))
			diskData = diskData.substring(1);
		if (diskData.endsWith("]"))
			diskData = diskData.substring(0, diskData.length() - 1);

		String[] jsonSplit = diskData.split("\\},\\{");

		for (String str : jsonSplit) {
			if (!str.startsWith("{"))
				str = "{" + str;
			if (!str.endsWith("}"))
				str = str + "}";

			int id = getInt(str, "id");
			String description = getString(str, "description");
			String status = getString(str, "status");
			String createAt = getString(str, "createAt");
			String updatedAt = getString(str, "updatedAt");
			Task task = new Task(id, description, status, createAt, updatedAt);
			Tasks.add(task);
		}
		return Tasks;
	}

	// Saves tasks to file
	public static void saveTasks(List<Task> tasks) {
		try (FileWriter writer = new FileWriter(FILE_NAME)) {
			writer.write("[\n");
			for (int i = 0; i < tasks.size(); i++) {
				Task task = tasks.get(i);
				writer.write("  {\n");
				writer.write("    \"id\": " + task.getId() + ",\n");
				writer.write("    \"description\": \"" + escape(task.getDescription()) + "\",\n");
				writer.write("    \"status\": \"" + task.getStatus() + "\",\n");
				writer.write("    \"createAt\": \"" + task.getCreateAt() + "\",\n");
				writer.write("    \"updatedAt\": \"" + task.getUpdatedAt() + "\"\n");
				writer.write("  }" + (i < tasks.size() - 1 ? "," : "") + "\n");
			}
			writer.write("]");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	// Adds tasks to the list
	public static void addTask(String str) {
		List<Task> tasks = loadTasks();
		Task task = new Task(tasks.size() + 1, str, "todo");
		tasks.add(task);
		saveTasks(tasks);
	}

	// Lists Every tasks
	public static void listTasks() {
		List<Task> tasks = loadTasks();
		if (tasks.size() == 0) {
			System.out.println("Tasks is empty");
		} else {
			System.out.println("The tasks are:");
			for (Task t : tasks) {
				displayTask(t);
			}
		}
	}

	// List Tasks based on Argument
	public static void listTasks(String str) {
		List<Task> tasks = loadTasks();
		if (str != null && str.startsWith("sort:")) {
			switch (str.substring(5)) {
			case "created":
				tasks.sort(Comparator.comparing(Task::getCreateAt));
				break;

			case "status":
				tasks.sort(Comparator.comparing(Task::getStatus));
				break;
			default:
				System.out.println("Unknown type");
			}
		} else if (str != null) {
			tasks.removeIf(task -> !task.getStatus().equalsIgnoreCase(str));
		}

		if (tasks.isEmpty()) {
			System.out.println("Nothing to display");
		} else {
			for (Task t : tasks) {
				displayTask(t);
			}
		}
	}

	// Update a task using Id
	public static void updateTask(int id, String Status) {
		List<Task> tasks = loadTasks();
		boolean found = false;
		for (Task t : tasks) {
			if (t.getId() == id) {
				t.setStatus(Status);
				t.setUpdatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
				found = true;
				break;
			}
		}
		if (found) {
			saveTasks(tasks);
			System.out.println("Task Updated.");
		} else {
			System.out.println("Task not Found.");
		}
	}

	// Delete a task using Id
	public static void deleteTask(int id) {
		List<Task> tasks = loadTasks();
		boolean found = tasks.removeIf(task -> task.getId() == id);
		if (found) {
			for (int i = 0; i < tasks.size(); i++) {
				tasks.get(i).setId(i + 1);
			}
			saveTasks(tasks);
			System.out.println("Task Deleted.");
		} else {
			System.out.println("Task Not Found.");
		}
	}

	// Search tasks based on description
	public static void searchTask(String string) {
		List<Task> tasks = loadTasks();
		boolean found = false;
		for (Task t : tasks) {
			if (t.getDescription().toLowerCase().contains(string.toLowerCase())) {
				displayTask(t);
				found = true;
			}
		}
		if (!found) {
			System.out.println(string + " not found");
		}

	}
}