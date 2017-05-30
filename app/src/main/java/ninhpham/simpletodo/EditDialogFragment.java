package ninhpham.simpletodo;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import static ninhpham.simpletodo.R.id.itemContent;
import static ninhpham.simpletodo.R.id.priorityEditView;
// ...

public class EditDialogFragment extends android.app.DialogFragment {
    String itemContent, dueDate;
    int pos;
    Button btnSave;
    EditText editText;
    TextView dueDate2, priority2;
    Spinner spinner;
    String setPriority;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView mDisplayDate;
    private Button.OnClickListener buttonSaveOnClickListerner = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditDialogListener activity = (EditDialogListener) getActivity();
            activity.updateResult(pos, setPriority, editText.getText().toString(), dueDate2.getText().toString());
            dismiss();
        }
    };

    public EditDialogFragment() {
    }

    public static EditDialogFragment newInstance() {
        return new EditDialogFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.activity_edit_item, container);
        editText = (EditText) dialogView.findViewById(R.id.tvDate2);
        dueDate2 = (TextView) dialogView.findViewById(R.id.etDueDate2);

        btnSave = (Button) dialogView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(buttonSaveOnClickListerner);

        spinner = (Spinner) dialogView.findViewById(R.id.priorityEditView);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.priority_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        switch (setPriority) {
            case "Priority":
                spinner.setSelection(0);
                break;
            case "Low":
                spinner.setSelection(1);
                break;
            case "Normal":
                spinner.setSelection(2);
                break;
            case "High":
                spinner.setSelection(3);
                break;
        }

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
        return dialogView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText editText = (EditText) view.findViewById(R.id.tvDate2);
        editText.setText(itemContent, TextView.BufferType.EDITABLE);
        editText.setSelection(itemContent.length());

        mDisplayDate = (TextView) view.findViewById(R.id.etDueDate2);
        mDisplayDate.setText(dueDate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        view.getContext(),
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
                dueDate = date;
                mDisplayDate.setText(date);
            }
        };


    }

    public void setItem(int pos, String setPriority, String itemContent, String dueDate) {
        this.itemContent = itemContent;
        this.dueDate = dueDate;
        this.pos = pos;
        this.setPriority = setPriority;
    }

    public interface EditDialogListener {
        void updateResult(int pos, String priority, String itemContent, String dueDate);
    }


}