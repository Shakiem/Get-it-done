package com.shakiemsaunders.get_it_done.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shakiemsaunders.get_it_done.R;
import com.shakiemsaunders.get_it_done.Service.ToDoService;
import com.shakiemsaunders.get_it_done.State;
import com.shakiemsaunders.get_it_done.ToDoItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ToDoAdapter extends ArrayAdapter<ToDoItem> {
    Context context;
    int layoutResourceId;
    List<ToDoItem> data= null;

    public ToDoAdapter(Context context, int layoutResourceId, List<ToDoItem> data){
        super(context,layoutResourceId,data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View row = convertView;
        ToDoItemHolder holder;
        final int SIX_HOURS = 21_600_000;
        final int ONE_DAY = 86_400_000;

        if(row==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ToDoItemHolder();
            holder.toDoItemTextView = (TextView)row.findViewById(R.id.toDoItemTextView);
            holder.dueDateTextView = (TextView)row.findViewById(R.id.dueDateTextView);
            holder.itemStatus = (CheckBox)row.findViewById(R.id.itemStatusCheckBox);
            holder.itemPriorityTextView = (TextView)row.findViewById(R.id.itemPriority);
            row.setTag(holder);
        }
        else
        {
            holder = (ToDoItemHolder)row.getTag();
        }
        if(data !=null){
            final ToDoItem toDoItem = data.get(position);
            holder.toDoItemTextView.setText(toDoItem.getTask());
            holder.toDoItemTextView.setTextColor(ContextCompat.getColor(context, R.color.app_dark_blue));

            if(toDoItem.getState() == State.COMPLETE)
                holder.itemStatus.setChecked(true);
            else
                holder.itemStatus.setChecked(false);

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy 'at' hh:mm a");
            holder.dueDateTextView.setText(format.format(toDoItem.getDueDate().getTimeInMillis()));

            if((toDoItem.getDueDate().getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) < SIX_HOURS && toDoItem.getState()!=State.COMPLETE) {
                holder.dueDateTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
            }
            else {
                if((toDoItem.getDueDate().getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) < ONE_DAY && toDoItem.getState()!=State.COMPLETE) {
                    holder.dueDateTextView.setTextColor(ContextCompat.getColor(context, R.color.warning_yellow));
                }
                else{
                    holder.dueDateTextView.setTextColor(ContextCompat.getColor(context, R.color.app_dark_blue));
                }
            }

            holder.itemPriorityTextView.setText(toDoItem.getPriority().toString());
            switch (toDoItem.getPriority()){
                case NOT_A_BIG_DEAL:
                    holder.itemPriorityTextView.setTextColor(ContextCompat.getColor(context, R.color.app_dark_blue));
                    break;
                case BIG_DEAL:
                    holder.itemPriorityTextView.setTextColor(ContextCompat.getColor(context, R.color.warning_yellow));
                    break;
                case VERY_BIG_DEAL:
                    holder.itemPriorityTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
            }

            if(toDoItem.getState()== State.COMPLETE){
                holder.dueDateTextView.setTextColor(ContextCompat.getColor(context, R.color.disabled_gray));
                holder.toDoItemTextView.setTextColor(ContextCompat.getColor(context, R.color.disabled_gray));
                holder.itemPriorityTextView.setTextColor(ContextCompat.getColor(context, R.color.disabled_gray));
            }

            holder.itemStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToDoService toDoService = new ToDoService(context);
                    if(((CheckBox)view).isChecked()){
                        toDoItem.setState(State.COMPLETE);
                        toDoService.saveState(toDoItem.getId(), State.COMPLETE);
                        data.remove(toDoItem);
                        data.add(toDoItem);
                        notifyDataSetChanged();
                    }
                    else{
                        toDoItem.setState(State.INCOMPLETE);
                        toDoService.saveState(toDoItem.getId(), State.INCOMPLETE);
                        data.remove(toDoItem);
                        data.add(position, toDoItem);
                        notifyDataSetChanged();
                    }
                }
            });
        }
        return row;
    }

    class ToDoItemHolder{
        TextView toDoItemTextView;
        TextView dueDateTextView;
        CheckBox itemStatus;
        TextView itemPriorityTextView;

        public ToDoItemHolder(){}
    }
}
