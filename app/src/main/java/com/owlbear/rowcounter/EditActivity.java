package com.owlbear.rowcounter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.EditText;

public class EditActivity extends FragmentActivity {

    public static final String INDEX = "EditActivity.Index";

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        index = getIntent().getIntExtra(INDEX, -1);
        if(index >= 0) {
            Counter c = DataController.getInstance().get(index);
            EditText edt = findViewById(R.id.edtName);
            edt.setText(c.name);
            edt = findViewById(R.id.edtRepeatRow);
            edt.setText(""+c.repeatRowCount);
            edt = findViewById(R.id.edtRow);
            edt.setText(""+c.rowNumber);
            edt = findViewById(R.id.edtRepeat);
            edt.setText(""+c.repeatNumber);
        }
    }

    public void btnConfirm(View v) {
        EditText edt = findViewById(R.id.edtName);
        String name = edt.getText().toString().trim();
        if(name.isEmpty()) {
            DialogFragment frag = new MessageDialog();
            Bundle args = new Bundle();
            args.putString(MessageDialog.MESSAGE,"Must enter a name.");
            frag.setArguments(args);
            frag.show(getSupportFragmentManager(), "");
            return;
        }
        edt = findViewById(R.id.edtRepeatRow);
        String repeatRow = edt.getText().toString().trim();
        if(repeatRow.isEmpty()) {
            repeatRow = "0";
        }
        edt = findViewById(R.id.edtRow);
        String row = edt.getText().toString().trim();
        if(row.isEmpty()) {
            row = "0";
        }
        edt = findViewById(R.id.edtRepeat);
        String repeat = edt.getText().toString().trim();
        if(repeat.isEmpty()) {
            repeat = "0";
        }
        DataController controller = DataController.getInstance();
        if(index < 0) {
             controller.addCounter(new Counter(name, Integer.parseInt(repeatRow), Integer.parseInt(row), Integer.parseInt(repeat)));
        } else {
            Counter c = controller.get(index);
            c.name = name;
            c.repeatRowCount = Integer.parseInt(repeatRow);
            c.rowNumber = Integer.parseInt(row);
            c.repeatNumber = Integer.parseInt(repeat);
        }
        setResult(RESULT_OK);
        finish();
    }
}
