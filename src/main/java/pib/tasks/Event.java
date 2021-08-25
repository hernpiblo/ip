package pib.tasks;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import pib.Ui;
import pib.pibexception.PibException;
/**
 * Event task which contains the task description, and the date of the event
 */
public class Event extends Task {

    private String date;
    private String time;

    /**
     * A public constructor to create an Event task
     *
     * @param description description of the deadline task
     * @param date        the date stated after "/at " portion
     */
    private Event(String description, String date, String time) {
        super(description);
        this.date = date;
        this.time = time;
    }

    private Event(String description, int isDone, String date, String time) {
        super(description, isDone);
        this.date = date;
        this.time = time;
    }

    public static Event createEvent(String details) throws PibException {
        try {
            int atIndex = details.indexOf("/at ");
            String description = details.substring(0, atIndex).trim();
            if (description.isBlank()) {
                Ui.printError("empty-task-description");
                throw new PibException();
            }
            String[] dateTime = details.substring(atIndex + 4).trim().split(" ");
            String date = LocalDate.parse(dateTime[0].trim()).format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
            String time = LocalTime.parse(dateTime[1].trim(), DateTimeFormatter.ofPattern("HHmm")).format(DateTimeFormatter.ofPattern("hh:mm a"));
            return new Event(description, date, time);
        } catch (IndexOutOfBoundsException e) {
            Ui.printError("e-wrong-format");
            throw new PibException();
        } catch (DateTimeParseException e) {
            Ui.printError("wrong-datetime-format");
            throw new PibException();
        }
    }

    public static Event createEvent(String description, int isDone, String date, String time) {
        return new Event(description, isDone, date, time);
    }

    public String toDataString() {
        return "E," + getIsDone() + "," + getDescription() + "," + date + "," + time + System.lineSeparator();
    }

    /**
     * A public toString method to add the task type [E] in front of the checkbox, and the date behind the task description
     *
     * @return the string representation of an event task
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (at: " + this.date + ", " + this.time + ")";
    }
}