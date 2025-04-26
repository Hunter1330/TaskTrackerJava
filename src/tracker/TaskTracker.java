package tracker;

public class TaskTracker {

	public static void main(String[] args) {
		switch (args[0]) {
		case "add": {
			if (args.length < 2) {
				System.out.println("Please provide a task description.");
			} else {
				TaskStorage.addTask(args[1]);
				System.out.println("Task added.");
			}
			break;
		}
		case "list": {
			if (args.length == 2)
				TaskStorage.listTasks(args[1]);
			else
				TaskStorage.listTasks();
			break;
		}
		case "update": {
			if (args.length < 3) {
				System.out.println("Usage: update <task_id> <new_status>");
			} else {
				try {
					TaskStorage.updateTask(Integer.parseInt(args[1]), args[2]);
				} catch (NumberFormatException e) {
					System.out.println(e.getMessage());
				}
			}
			break;
		}
		case "delete": {
			if (args.length < 2) {
				System.out.println("Please Enter a task Id to Delete.");
			} else {
				TaskStorage.deleteTask(Integer.parseInt(args[1]));
			}
			break;
		}
		case "search":
			if (args.length < 2) {
				System.out.println("Please enter something to search");
			} else {
				TaskStorage.searchTask(args[1]);
			}
			break;
		default:
			System.out.println("No command given.");
//			throw new IllegalArgumentException("Unexpected value: " + args[0]);
		}
	}
}