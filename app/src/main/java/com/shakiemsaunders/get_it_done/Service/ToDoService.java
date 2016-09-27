package com.shakiemsaunders.get_it_done.Service;

import android.content.Context;

import com.shakiemsaunders.get_it_done.DB.DAO.ToDoDAO;
import com.shakiemsaunders.get_it_done.State;
import com.shakiemsaunders.get_it_done.ToDoItem;

import java.util.List;

public class ToDoService {
    ToDoDAO toDoDAO;

    public ToDoService(Context context){
        toDoDAO = new ToDoDAO(context);
    }

    public List<ToDoItem> getToDoItems() {
        return toDoDAO.getToDoItems();
    }

    public void saveState(long id, State state) {
        toDoDAO.saveState(id, state);
    }

    public void updateItem(ToDoItem toDoItem) {
        toDoDAO.updateItem(toDoItem);
    }

    public long insertItem(ToDoItem toDoItem) {
        return toDoDAO.insertItem(toDoItem);
    }

    public void deleteItem(long id) {
        toDoDAO.deleteItem(id);
    }
}
