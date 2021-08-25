package pib;

import pib.enums.TaskType;
import pib.tasks.Deadline;
import pib.tasks.Event;
import pib.tasks.Task;
import pib.tasks.Todo;
import pib.pibexception.PibException;

import java.util.ArrayList;

import static pib.Ui.DIVIDER;

/**
 * Class to store the tasks created
 */
public class TaskList {
    private ArrayList<Task> list;

    /**
     * Public constructor to create a new TaskList
     */
    public TaskList() {
        this.list = new ArrayList<Task>();
    }

    public TaskList(ArrayList<Task> list) {
        this.list = list;
    }

    /**
     * Prints to UI the entire list of Tasks
     *
     * @throws PibException when the TaskList is empty
     */
    public void viewList() throws PibException {
        if (list.size() == 0) {
            throw new PibException("empty-list");
        } else {
            for (int i = 0; i < list.size(); i++) {
                System.out.println((i + 1) + "." + list.get(i).toString());
            }
            System.out.println(DIVIDER);
        }
    }

    /**
     * Prints to UI how many tasks the TaskList contains
     */
    public void viewListSize() {
        System.out.println("There are " + list.size() + " task(s) in the list\n" + DIVIDER);
    }

    /**
     * Adds a new Task to the TaskList
     *
     * @param t type of task as specified by the enum TaskType
     * @param taskDetails task details (ie. the remaining part of the command)
     * @throws PibException if the creation of new Tasks throws a PibException
     */
    public void add(TaskType t, String taskDetails) throws PibException {
        Task newTask = null;
        switch (t) {
        case TODO:
            newTask = Todo.createTodo(taskDetails, true);
            break;
        case EVENT:
            newTask = Event.createEvent(taskDetails, true);
            break;
        case DEADLINE:
            newTask = Deadline.createDeadline(taskDetails, true);
            break;
        default:
            break;
        }
        if (newTask != null) {
            list.add(newTask);
            Storage.saveData(this, Pib.DATA_FILE_PATH);
            Ui.printListSize(this);
        }
    }

    /**
     * Adds tasks into the TaskList that were loaded from the saved data
     *
     * @param t Task to be added
     */
    public void addSavedData(Task t) {
        list.add(t);
    }

    /**
     * Delete task specified by the task number
     *
     * @param taskNum Index of task to be deleted (starting from 1)
     * @throws PibException when IndexOutOfBoundsException is thrown when user enters invalid task number/blank
     */
    public void delete(int taskNum) throws PibException {
        try {
            String taskDesc = list.get(taskNum - 1).getDescription();
            list.remove(taskNum - 1);
            Storage.saveData(this, Pib.DATA_FILE_PATH);
            Ui.printDeleteSuccess(taskDesc);
        } catch (IndexOutOfBoundsException e) {
            throw new PibException("ioob-exception");
        }
    }

    /**
     * Mark a task as specified by the task number
     *
     * @param taskNum Index of task to be marked as done (starting from 1)
     * @throws PibException when IndexOutOfBoundsException is thrown when user enters invalid task number/blank
     */
    public void markAsDone(int taskNum) throws PibException {
        try {
            list.get(taskNum - 1).markAsDone();
            Storage.saveData(this, Pib.DATA_FILE_PATH);
        } catch (IndexOutOfBoundsException e) {
            throw new PibException("ioob-exception");
        }
    }

    /**
     * Convert a TaskList to a string format to be saved inside a file
     *
     * @return String format of TaskList to be saved
     */
    public String convertListToSaveData() {
        StringBuilder data = new StringBuilder();
        for (Task t : list) {
            data.append(t.toDataString());
        }
        return data.toString();
    }

    public void find(String query) throws PibException {
        if (query.isBlank()) {
            throw new PibException("empty-query");
        }
        ArrayList<Task> filtered = new ArrayList<>();
        for (Task t : this.list) {
            if (t.getDescription().contains(query)) {
                filtered.add(t);
            }
        }
        if (filtered.size() > 0) {
            Ui.printQueryFound(query);
            (new TaskList(filtered)).viewList();
        } else {
            Ui.printQueryNotFound(query);
        }
    }
}
