package com.shakiemsaunders.get_it_done.DB.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shakiemsaunders.get_it_done.DB.DBHelper;
import com.shakiemsaunders.get_it_done.Priority;
import com.shakiemsaunders.get_it_done.State;
import com.shakiemsaunders.get_it_done.ToDoItem;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class ToDoDAO extends AbstractDAO {

    public ToDoDAO(Context context){
        super(context);
    }

    public List<ToDoItem> getToDoItems() {
        List<ToDoItem> itemList = new ArrayList<>();
        ToDoItem item;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + DBHelper.TO_DO_TABLE, null);
        result.moveToFirst();
        while(!result.isAfterLast()){
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(result.getLong(result.getColumnIndex(DBHelper.TO_DO_DUE_DATE_COLUMN)));
            item = new ToDoItem(
                    result.getLong(result.getColumnIndex(DBHelper.TO_DO_ID_COLUMN)),
                    result.getString(result.getColumnIndex(DBHelper.TO_DO_TASK_COLUMN)),
                    calendar,
                    Priority.byValue(result.getInt(result.getColumnIndex(DBHelper.TO_DO_PRIORITY_COLUMN))),
                    State.byValue(result.getInt(result.getColumnIndex(DBHelper.TO_DO_STATE_COLUMN))),
                    result.getString(result.getColumnIndex(DBHelper.TO_DO_COMMENTS_COLUMN))
            );
            itemList.add(item);
            result.moveToNext();
        }
        result.close();
        db.close();
        return itemList;
    }

    public void saveState(long id, State state) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TO_DO_STATE_COLUMN, state.ordinal());
        db.update(DBHelper.TO_DO_TABLE, values, DBHelper.TO_DO_ID_COLUMN + " = " + id, null);
        db.close();
    }

    public void updateItem(ToDoItem toDoItem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TO_DO_TASK_COLUMN, toDoItem.getTask());
        values.put(DBHelper.TO_DO_DUE_DATE_COLUMN, toDoItem.getDueDate().getTimeInMillis());
        values.put(DBHelper.TO_DO_PRIORITY_COLUMN, toDoItem.getPriority().ordinal());
        values.put(DBHelper.TO_DO_STATE_COLUMN, toDoItem.getState().ordinal());
        values.put(DBHelper.TO_DO_STATE_COLUMN, toDoItem.getState().ordinal());

        db.update(DBHelper.TO_DO_TABLE, values, DBHelper.TO_DO_ID_COLUMN + " = " + toDoItem.getId(), null);
    }

    public long insertItem(ToDoItem toDoItem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TO_DO_TASK_COLUMN, toDoItem.getTask());
        values.put(DBHelper.TO_DO_DUE_DATE_COLUMN, toDoItem.getDueDate().getTimeInMillis());
        values.put(DBHelper.TO_DO_PRIORITY_COLUMN, toDoItem.getPriority().ordinal());
        values.put(DBHelper.TO_DO_STATE_COLUMN, toDoItem.getState().ordinal());
        values.put(DBHelper.TO_DO_STATE_COLUMN, toDoItem.getState().ordinal());

        return db.insert(DBHelper.TO_DO_TABLE, null, values);
    }

    public void deleteItem(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TO_DO_TABLE, DBHelper.TO_DO_ID_COLUMN + " = " + id, null);
    }
}
