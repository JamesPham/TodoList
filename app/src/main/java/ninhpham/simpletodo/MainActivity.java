package ninhpham.simpletodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ninhpham.simpletodo.EditDialogFragment.EditDialogListener;

import static android.R.attr.data;
import static android.R.attr.id;
import static android.R.attr.width;
import static com.raizlabs.android.dbflow.sql.language.SQLite.delete;
import static ninhpham.simpletodo.R.id.itemContent;


public class MainActivity extends AppCompatActivity implements EditDialogListener {

    private static final String TAG = "MainActivity";
    ArrayList<todoItem> items = new ArrayList<todoItem>(15);
    todoAdapter itemsAdapter;
    ListView lvItems;
    String setPriority, date;
    Spinner spinner;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FlowManager.init(new FlowConfig.Builder(this).build());
        mDisplayDate = (TextView) findViewById(R.id.tvDate);

        spinner = (Spinner) findViewById(R.id.priorityMain);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                              @Override
                                              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                  switch (position) {
                                                      case 0:
                                                          setPriority = "Priority";
                                                          break;
                                                      case 1:
                                                          setPriority = "Low";
                                                          break;
                                                      case 2:
                                                          setPriority = "Normal";
                                                          break;
                                                      case 3:
                                                          setPriority = "High";
                                                          break;
                                                  }

                                              }

                                              public void onNothingSelected(AdapterView<?> parent) {
                                                  // Another interface callback
                                              }
                                          }
        );
        //Display current date in Edit Item box.

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        date = month + "/" + day + "/" + year;
        mDisplayDate.setText(date);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };


        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new todoAdapter(getApplicationContext(), items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        SQLite.delete(itemDb.class)
                                .where(itemDb_Table.id.eq(items.size()))
                                .async()
                                .execute();
                        return true;
                    }
                });

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        FragmentManager fm = getFragmentManager();
                        EditDialogFragment dialogFragment = new EditDialogFragment();
                        dialogFragment.setItem(pos, items.get(pos).priority, items.get(pos).itemContent, items.get(pos).dueDate);
                        //dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, 0);
                        dialogFragment.show(fm, "Sample Fragment");


                    }
                }
        );
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        TextView etDueDate = (TextView) findViewById(R.id.tvDate);
        String itemText = etNewItem.getText().toString();
        String dueDate = etDueDate.getText().toString();
        todoItem newItem = new todoItem(setPriority, itemText, dueDate);
        spinner.setSelection(0);
        etDueDate.setText(date);
        itemsAdapter.add(newItem);
        etNewItem.setText("");
        writeItems();
    }

    private void readItems() {
        ArrayList<itemDb> tmp = (ArrayList<itemDb>) new Select().from(itemDb.class).queryList();
        if (tmp.size() != 0) {
            for (int i = 0; i < tmp.size(); i++) {
                todoItem newItem = new todoItem(tmp.get(i).getPriority(), tmp.get(i).getItemDb(), tmp.get(i).getDueDate());
                items.add(newItem);
            }
        }


    }

    private void writeItems() {
        itemDb itemDb = new itemDb();
        for (int i = 0; i < items.size(); i++) {
            itemDb.setItemDb(items.get(i).itemContent);
            itemDb.setDueDate(items.get(i).dueDate);
            itemDb.setPriority(items.get(i).priority);
            itemDb.setId(i);
            itemDb.save();
        }

    }

    public void updateResult(int pos, String priority, String itemContent, String dueDate) {
        items.set(pos, new todoItem(priority, itemContent, dueDate));
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }
}
