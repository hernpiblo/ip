import java.util.ArrayList;
import java.util.Scanner;

import Tasks.Task;
import Tasks.Todo;
import Tasks.Deadline;
import Tasks.Event;
import pibexception.PibException;

/**
 * Pib is a Personal Assistant Chat-bot that is able to keep track of tasks (CRUD) and deadlines
 */
public class Pib {
    private static String DIVIDER = "____________________________________________________________\n";
    private ArrayList<Task> list;
    private Scanner sc;

    /**
     * Public constructor to instantiate an instance of Pib and start the program
     */
    public Pib() {
        System.out.println(DIVIDER + "Hello! I'm Pib\n" + "Tell me something!\n" + DIVIDER);
        list = new ArrayList<>();
        sc = new Scanner(System.in);
        readInput();
    }

    private enum TaskType {
        TODO, DEADLINE, EVENT
    }

    private void readInput() {
        while (sc.hasNextLine()) {
            try {
                System.out.println(DIVIDER);
                String next = sc.nextLine();

                if (next.contains(" ")) {
                    int spaceDividerIndex = next.indexOf(" ");
                    String taskType = next.substring(0, spaceDividerIndex).toLowerCase();
                    String taskDetails = next.substring(1 + spaceDividerIndex);
                    switch (taskType) {
                        case "todo":
                            addToList(TaskType.TODO, taskDetails);
                            break;
                        case "deadline": {
                            addToList(TaskType.DEADLINE, taskDetails);
                            break;
                        }
                        case "event": {
                            addToList(TaskType.EVENT, taskDetails);
                            break;
                        }
                        case "done": {
                            try {
                                markAsDone(taskDetails);
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("Uh oh :( Please enter a valid task number!\n");
                            }
                            break;
                        }
                        case "delete": {
                            try {
                                delete(taskDetails);
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("Uh oh :( Please enter a valid task number!\n");
                            }
                            break;
                        }
                        default: {
                            throw new PibException("Uh oh :( I don't know that command\n");
                        }
                    }
                } else {
                    String action = next.toLowerCase();
                    if (action.equals("list")) {
                        displayList();
                    } else if (action.equals("bye")) {
                        endPib();
                        break;
                    } else {
                        throw new PibException("Uh oh :( I don't know that command\n");
                    }
                }
                System.out.println(DIVIDER);
            } catch (PibException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void endPib() {
        System.out.println("Bye! See you next time!\n");
        System.out.println(DIVIDER);
        sc.close();
    }

    private void addToList(TaskType t, String taskDetails) {
        try {
            switch (t) {
            case TODO:
                list.add(Todo.createTodo(taskDetails));
                break;
            case EVENT:
                list.add(Event.createEvent(taskDetails));
                break;
            case DEADLINE:
                list.add(Deadline.createDeadline(taskDetails));
                break;
            default:
                break;
            }
            System.out.println("Now you have " + list.size() + " task(s) in your list.\n");
        } catch (PibException e) {
            System.out.println(e.getMessage());
        }
    }

    private void displayList() {
        if (list.size() == 0) {
            System.out.println("Nothing added yet\n");
        } else {
            for (int i = 0; i < list.size(); i++) {
                System.out.println((i + 1) + "." + list.get(i).toString());
            }
            System.out.println("");
        }
    }

    private void markAsDone(String s) {
        if (s.isBlank()) {
            System.out.println("Tell me which item to mark as complete!\n ");
        } else {
            list.get(Integer.parseInt(s) - 1).markAsDone();
        }
    }

    private void delete(String s) {
        if (s.isBlank()) {
            System.out.println("Tell me which item to delete!\n ");
        } else {
            list.remove(Integer.parseInt(s) - 1);
            System.out.println("Successfully deleted task!\n");
            System.out.println("Now you have " + list.size() + " task(s) in your list.\n");
        }
    }


    public static void main(String[] args) {
        Pib p = new Pib();
    }
}
