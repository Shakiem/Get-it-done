package com.shakiemsaunders.get_it_done.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shakiemsaunders.get_it_done.Priority;
import com.shakiemsaunders.get_it_done.R;
import com.shakiemsaunders.get_it_done.Service.ToDoService;
import com.shakiemsaunders.get_it_done.State;
import com.shakiemsaunders.get_it_done.ToDoConstants;
import com.shakiemsaunders.get_it_done.ToDoItem;

import java.util.Calendar;

public class ToDoItemActivity extends ActionBarActivity {

    private Context context;
    private EditText taskEditText;
    private EditText commentsEditText;
    private Spinner prioritySpinner;
    private Spinner statusSpinner;
    private DatePicker datePicker;
    private TimePicker timePicker;

    private ToDoItem toDoItem;
    private boolean creatingItem;
    private ToDoService toDoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_item);

        context = this;
        toDoService = new ToDoService(context);

        Toolbar actionBarToolbar = (Toolbar)findViewById(R.id.action_bar);
        if (actionBarToolbar != null) {
            actionBarToolbar.setTitleTextAppearance(context, android.R.style.TextAppearance_Large);
            actionBarToolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.app_dark_blue));
            actionBarToolbar.setLogo(ContextCompat.getDrawable(context, R.drawable.app_icon));

        }

        instantiateWidgets();

        toDoItem = (ToDoItem) getIntent().getSerializableExtra(ToDoConstants.ITEM);
        if(toDoItem==null){
            creatingItem = true;
        }

        if(!creatingItem){
            populateWidgets();
        }
    }

    private void populateWidgets() {
        taskEditText.setText(toDoItem.getTask());
        commentsEditText.setText(toDoItem.getComments());
        prioritySpinner.setSelection(((ArrayAdapter) prioritySpinner.getAdapter()).getPosition(toDoItem.getPriority()));
        statusSpinner.setSelection(((ArrayAdapter) statusSpinner.getAdapter()).getPosition(toDoItem.getState()));
        Calendar dueDate = toDoItem.getDueDate();
        datePicker.updateDate(dueDate.get(Calendar.YEAR), dueDate.get(Calendar.MONTH), dueDate.get(Calendar.DAY_OF_MONTH));
        timePicker.setCurrentHour(dueDate.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(dueDate.get(Calendar.MINUTE));
    }

    private void instantiateWidgets() {
        taskEditText = (EditText) findViewById(R.id.taskEditTxt);
        commentsEditText = (EditText) findViewById(R.id.commentEditTxt);
        prioritySpinner = (Spinner) findViewById(R.id.prioritySpinner);
        prioritySpinner.setAdapter(new ArrayAdapter<Priority>(context, R.layout.to_do_simple_spinner_item, Priority.values()));
        statusSpinner = (Spinner) findViewById(R.id.statusSpinner);
        statusSpinner.setAdapter(new ArrayAdapter<State>(context, R.layout.to_do_simple_spinner_item, State.values()));
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_to_do_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_cancel:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            case R.id.action_save:
                saveItem();
                return true;
            case R.id.action_delete:
                deleteItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.action_delete).setVisible(!creatingItem);
        return super.onPrepareOptionsMenu(menu);
    }

    private void deleteItem() {
        toDoService.deleteItem(toDoItem.getId());
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ToDoConstants.ITEM, toDoItem);
        returnIntent.putExtra(ToDoConstants.ITEM_ACTION, ToDoConstants.ITEM_DELETED);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void saveItem() {
        String taskString = taskEditText.getText().toString();
        if(taskString.isEmpty()){
            taskEditText.setBackgroundResource(R.drawable.error_textbox);
            Toast.makeText(context, "Task field cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        int hourOfDay = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        Calendar dueDate = Calendar.getInstance();
        dueDate.set(year, month, day, hourOfDay, minute);
        Priority priority = (Priority) prioritySpinner.getSelectedItem();
        State state = (State) statusSpinner.getSelectedItem();
        String commentsString = commentsEditText.getText().toString();

        if(!creatingItem){
            toDoItem.setTask(taskString);
            toDoItem.setDueDate(dueDate);
            toDoItem.setPriority(priority);
            toDoItem.setState(state);
            toDoItem.setComments(commentsString);

            toDoService.updateItem(toDoItem);
            Intent returnIntent = new Intent();
            returnIntent.putExtra(ToDoConstants.ITEM, toDoItem);
            returnIntent.putExtra(ToDoConstants.ITEM_ACTION, ToDoConstants.ITEM_MODIFIED);
            returnIntent.putExtra(ToDoConstants.POSITION_MODIFIED, getIntent().getIntExtra(ToDoConstants.POSITION_MODIFIED,-1));
            setResult(RESULT_OK, returnIntent);
            Toast.makeText(context, "Item updated!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            toDoItem = new ToDoItem(taskString, dueDate, priority, state, commentsString);
            toDoItem.setId(toDoService.insertItem(toDoItem));
            Toast.makeText(context, "Item added!", Toast.LENGTH_SHORT).show();
            Intent returnIntent = new Intent();
            returnIntent.putExtra(ToDoConstants.ITEM, toDoItem);
            returnIntent.putExtra(ToDoConstants.ITEM_ACTION, ToDoConstants.ITEM_ADDED);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }
}
