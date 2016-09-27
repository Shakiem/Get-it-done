package com.shakiemsaunders.get_it_done;

import java.io.Serializable;
import java.util.Calendar;

public class ToDoItem implements Serializable {
    private long id;
    private String task;
    private Calendar dueDate;
    private Priority priority;
    private State state;
    private String comments;

    public ToDoItem(long id, String task, Calendar dueDate, Priority priority, State state, String comments){
        this.id = id;
        this.task = task;
        this.dueDate = dueDate;
        this.priority = priority;
        this.state = state;
        this.comments = comments;
    }

    public ToDoItem(String task, Calendar dueDate, Priority priority, State state, String comments){
        this.task = task;
        this.dueDate = dueDate;
        this.priority = priority;
        this.state = state;
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ToDoItem)) return false;

        ToDoItem toDoItem = (ToDoItem) o;

        if (id != toDoItem.id) return false;
        if (comments != null ? !comments.equals(toDoItem.comments) : toDoItem.comments != null)
            return false;
        if (!dueDate.equals(toDoItem.dueDate)) return false;
        if (priority != toDoItem.priority) return false;
        if (state != toDoItem.state) return false;
        if (!task.equals(toDoItem.task)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + task.hashCode();
        result = 31 * result + dueDate.hashCode();
        result = 31 * result + priority.hashCode();
        result = 31 * result + state.hashCode();
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }
}
