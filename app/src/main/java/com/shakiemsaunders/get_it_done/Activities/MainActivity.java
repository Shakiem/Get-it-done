package com.shakiemsaunders.get_it_done.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.shakiemsaunders.get_it_done.Adapters.ToDoAdapter;
import com.shakiemsaunders.get_it_done.Priority;
import com.shakiemsaunders.get_it_done.R;
import com.shakiemsaunders.get_it_done.Service.ToDoService;
import com.shakiemsaunders.get_it_done.State;
import com.shakiemsaunders.get_it_done.ToDoConstants;
import com.shakiemsaunders.get_it_done.ToDoItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    List<ToDoItem> toDoItems;
    ToDoAdapter toDoAdapter;
    ListView listView;
    ToDoService toDoService;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        Toolbar actionBarToolbar = (Toolbar)findViewById(R.id.action_bar);
        if (actionBarToolbar != null) {
            actionBarToolbar.setTitleTextAppearance(context, android.R.style.TextAppearance_Large);
            actionBarToolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.app_dark_blue));
            actionBarToolbar.setLogo(ContextCompat.getDrawable(context, R.drawable.app_icon));
        }

        toDoService = new ToDoService(context);

        populateArrayItems();
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(toDoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ToDoItem selectedItem = toDoItems.get(position);
                Intent intent = new Intent(context, ToDoItemActivity.class);
                intent.putExtra(ToDoConstants.ITEM, selectedItem);
                intent.putExtra(ToDoConstants.POSITION_MODIFIED, position);
                startActivityForResult(intent, ToDoConstants.MODIFY_REQUEST);
            }
        });
    }

    public void populateArrayItems(){
        toDoItems = toDoService.getToDoItems();
        toDoAdapter = new ToDoAdapter(context, R.layout.to_do_item_row, toDoItems);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_item_action:
                startActivityForResult(new Intent(context, ToDoItemActivity.class), ToDoConstants.ADD_REQUEST);
                return true;
            case R.id.sort_by_date_action:
                sortByDate();
                return true;
            case R.id.sort_by_status_action:
                sortByState();
                return true;
            case R.id.sort_by_priority_action:
                sortByPriority();
                return true;
            case R.id.filter_by_date_action:
                filterByDateSelected();
                return true;
            case R.id.filter_by_incomplete_action:
                filterByState(State.INCOMPLETE);
                return true;
            case R.id.filter_by_complete_action:
                filterByState(State.COMPLETE);
                return true;
            case R.id.filter_by_very_big_deal_action:
                filterByPriority(Priority.VERY_BIG_DEAL);
                return true;
            case R.id.filter_by_big_deal_action:
                filterByPriority(Priority.BIG_DEAL);
                return true;
            case R.id.filter_by_not_a_big_deal_action:
                filterByPriority(Priority.NOT_A_BIG_DEAL);
                return true;
            case R.id.refresh_items_action:
                refreshList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ToDoConstants.MODIFY_REQUEST) {
            if(resultCode == Activity.RESULT_OK){
                String action = data.getStringExtra(ToDoConstants.ITEM_ACTION);
                if(action.equals(ToDoConstants.ITEM_DELETED)){
                    ToDoItem itemToRemove = (ToDoItem)data.getSerializableExtra(ToDoConstants.ITEM);
                    toDoItems.remove(itemToRemove);
                }
                if(action.equals(ToDoConstants.ITEM_MODIFIED)){
                    ToDoItem modifiedItem = (ToDoItem)data.getSerializableExtra(ToDoConstants.ITEM);
                    int itemPosition = data.getIntExtra(ToDoConstants.POSITION_MODIFIED, -1);
                    if(itemPosition==-1) {
                        refreshList();
                    }
                    else {
                        toDoItems.set(itemPosition, modifiedItem);
                    }
                }
                toDoAdapter.notifyDataSetChanged();
            }
        }
        else if(requestCode == ToDoConstants.ADD_REQUEST) {
            if(resultCode == Activity.RESULT_OK) {
                String action = data.getStringExtra(ToDoConstants.ITEM_ACTION);
                if (action.equals(ToDoConstants.ITEM_ADDED)) {
                    ToDoItem itemAdded = (ToDoItem) data.getSerializableExtra(ToDoConstants.ITEM);
                    toDoItems.add(0, itemAdded);
                    toDoAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void filterByPriority(Priority priority) {
        List<ToDoItem> removeList = new ArrayList<>();

        for (ToDoItem toDoItem : toDoItems) {
            if(toDoItem.getPriority() != priority)
                removeList.add(toDoItem);
        }
        toDoItems.removeAll(removeList);
        toDoAdapter.notifyDataSetChanged();
    }

    private void sortByPriority() {
        List<ToDoItem> bigDealList = new ArrayList<>();
        List<ToDoItem> notBigDealList = new ArrayList<>();
        List<ToDoItem> reallyBigDealList = new ArrayList<>();

        for (ToDoItem toDoItem : toDoItems) {
            if(toDoItem.getPriority() == Priority.NOT_A_BIG_DEAL)
                notBigDealList.add(toDoItem);
            else if(toDoItem.getPriority() == Priority.BIG_DEAL)
                bigDealList.add(toDoItem);
            else
                reallyBigDealList.add(toDoItem);
        }

        toDoItems.clear();
        toDoItems.addAll(reallyBigDealList);
        toDoItems.addAll(bigDealList);
        toDoItems.addAll(notBigDealList);
        toDoAdapter.notifyDataSetChanged();
    }

    private void refreshList() {
        toDoItems.clear();
        toDoItems.addAll(toDoService.getToDoItems());
        toDoAdapter.notifyDataSetChanged();
    }

    private void filterByDateSelected() {
        showDateFilterDialog();
    }

    private void filterByState(State state) {
        List<ToDoItem> removeList = new ArrayList<>();
        for (ToDoItem toDoItem : toDoItems) {
            if(toDoItem.getState() != state){
                removeList.add(toDoItem);
            }
        }
        toDoItems.removeAll(removeList);
        toDoAdapter.notifyDataSetChanged();
    }

    private void sortByState() {
        List<ToDoItem> tempList = new ArrayList<>();
        for (ToDoItem toDoItem : toDoItems) {
            if(toDoItem.getState() == State.COMPLETE){
                tempList.add(toDoItem);
            }
        }
        toDoItems.removeAll(tempList);
        toDoItems.addAll(tempList);
        toDoAdapter.notifyDataSetChanged();
    }

    private void sortByDate() {
        Collections.sort(toDoItems, new Comparator<ToDoItem>() {
            @Override
            public int compare(ToDoItem toDoItem, ToDoItem toDoItem2) {
                return toDoItem.getDueDate().getTimeInMillis() < toDoItem2.getDueDate().getTimeInMillis() ?
                        -1 : (toDoItem.getDueDate().getTimeInMillis() > toDoItem2.getDueDate().getTimeInMillis()) ? 1 : 0;
            }
        });
        toDoAdapter.notifyDataSetChanged();
    }

    private void showDateFilterDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_by_date_dialog);

        Button cancelBttn = (Button) dialog.findViewById(R.id.filterCancelBttn);
        Button filterBttn = (Button) dialog.findViewById(R.id.filterBttn);
        final EditText startDateEditText = (EditText) dialog.findViewById(R.id.startDateEditTxt);
        final EditText endDateEditText = (EditText) dialog.findViewById(R.id.endDateEditTxt);
        ImageView startDateHelperBttn = (ImageView) dialog.findViewById(R.id.startDateHelperBttn);
        ImageView endDateHelperBttn = (ImageView) dialog.findViewById(R.id.endDateHelperBttn);
        final LinearLayout startDatePickerLayout = (LinearLayout) dialog.findViewById(R.id.startDatePickerLayout);
        final LinearLayout endDatePickerLayout = (LinearLayout) dialog.findViewById(R.id.endDatePickerLayout);
        final DatePicker startDatePicker = (DatePicker) dialog.findViewById(R.id.startDatePicker);
        final DatePicker endDatePicker = (DatePicker) dialog.findViewById(R.id.endDatePicker);
        Button startDateOkayBttn = (Button) dialog.findViewById(R.id.startDateOkayBttn);
        Button endDateOkayBttn = (Button) dialog.findViewById(R.id.endDateOkayBttn);

        startDateHelperBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDatePickerLayout.setVisibility(View.VISIBLE);
            }
        });

        endDateHelperBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDatePickerLayout.setVisibility(View.VISIBLE);
            }
        });

        startDateOkayBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                int year = startDatePicker.getYear();
                int month = startDatePicker.getMonth();
                int day = startDatePicker.getDayOfMonth();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                String dateString = formatter.format(calendar.getTimeInMillis());
                startDateEditText.setText(dateString);
                startDatePickerLayout.setVisibility(View.GONE);
            }
        });

        endDateOkayBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                int year = endDatePicker.getYear();
                int month = endDatePicker.getMonth();
                int day = endDatePicker.getDayOfMonth();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                String dateString = formatter.format(calendar.getTimeInMillis());
                endDateEditText.setText(dateString);
                endDatePickerLayout.setVisibility(View.GONE);
            }
        });

        cancelBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        filterBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean dateErrors = false;
                final String dateRegex = "\\d{2}/\\d{2}/\\d{4}";
                String startDateString = startDateEditText.getText().toString();
                String endDateString = endDateEditText.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                Long startDate;
                Long endDate;

                if(!startDateString.matches(dateRegex)){
                    startDateEditText.setBackgroundResource(R.drawable.error_textbox);
                    dateErrors = true;
                }
                if(!endDateString.matches(dateRegex)){
                    endDateEditText.setBackgroundResource(R.drawable.error_textbox);
                    dateErrors = true;
                }
                if(dateErrors) return;

                try {
                    startDate = dateFormat.parse(startDateString).getTime();
                    endDate = dateFormat.parse(endDateString).getTime();
                } catch (ParseException e) {
                    Toast.makeText(context, "Invalid dates entered, cannot complete filter!", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    e.printStackTrace();
                    return;
                }

                if(endDate <= startDate) {
                    Toast.makeText(context, "End date must be after start date!!", Toast.LENGTH_LONG).show();
                    endDateEditText.setBackgroundResource(R.drawable.error_textbox);
                    return;
                }
                filterToDates(startDate, endDate);
                dialog.dismiss();
            }
        });



        dialog.show();
    }

    private void filterToDates(Long startDate, Long endDate) {
        List<ToDoItem> removeList = new ArrayList<>();

        for (ToDoItem toDoItem : toDoItems) {
            long dueDate = toDoItem.getDueDate().getTimeInMillis();
            if(!(dueDate > startDate && dueDate < endDate))
                removeList.add(toDoItem);
        }
        toDoItems.removeAll(removeList);
        toDoAdapter.notifyDataSetChanged();
    }
}
