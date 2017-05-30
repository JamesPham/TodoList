package ninhpham.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by ninhp on 5/24/2017.
 */
public class todoAdapter extends ArrayAdapter<todoItem> {
    public todoAdapter(Context context, ArrayList<todoItem> todoItem) {
        super(context, 0, todoItem);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        todoItem todoItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_view_layout, parent, false);
        }
        // Lookup view for data population
        TextView itemContent = (TextView) convertView.findViewById(R.id.itemContent);
        TextView dueDate = (TextView) convertView.findViewById(R.id.dueDate);
        TextView priorityListView = (TextView) convertView.findViewById(R.id.priorityListView);
        // Populate the data into the template view using the data object
        itemContent.setText(todoItem.itemContent);
        dueDate.setText(todoItem.dueDate);
        priorityListView.setText(todoItem.priority);
        // Return the completed view to render on screen
        return convertView;
    }
}

