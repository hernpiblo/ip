package pib.Tasks;

import pib.Ui;

/**
 * Task is a parent class of other specific types of tasks
 */
public abstract class Task {
    private String description;
    private int isDone;

    /**
     * @param description The description of the task
     */
    public Task(String description) {
        this.description = description.trim();
        isDone = 0;
        Ui.printAddSuccess(description);
    }

    public Task(String description, int isDone) {
        this.description = description.trim();
        this.isDone = isDone;
        Ui.printAddSuccess(description);
    }

    /**
     * A public toString method to return the task description with the checkbox for toggling completion
     *
     * @return the string representation of a task
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Method to toggle the isDone variable, to display to the user if a task is marked as done or not
     */
    public void markAsDone() {
        if (this.isDone == 1) {
            Ui.printError("already-markedasdone");
        } else {
            this.isDone = 1;
            Ui.printMarkAsDoneSuccess(description);
        }
    }

    public String getDescription() {
        return this.description;
    }

    protected int getIsDone() {
        return isDone;
    }

    public abstract String toDataString();

    private String getStatusIcon() {
        return (isDone == 1 ? "X" : " ");
    }
}

