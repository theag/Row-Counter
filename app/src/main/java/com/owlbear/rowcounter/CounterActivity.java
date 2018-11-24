package com.owlbear.rowcounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class CounterActivity extends FragmentActivity implements ConfirmDialog.ConfirmDialogListener {

    public static final String INDEX = "CounterActivity.Index";

    private Counter counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        if(!DataController.hasInstance()) {
            try {
                DataController.loadInstance(new File(getFilesDir(), "data.bin"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        counter = DataController.getInstance().get(getIntent().getIntExtra(INDEX, -1));
        TextView tv = findViewById(R.id.txtName);
        tv.setText(counter.name);
        tv = findViewById(R.id.txtRow);
        tv.setText(""+counter.rowNumber);
        tv = findViewById(R.id.txtRepeat);
        tv.setText(""+counter.repeatNumber);
        CheckBox cb = findViewById(R.id.chkLink);
        cb.setChecked(counter.linkCounters);
        cb = findViewById(R.id.chkScreenOn);
        cb.setChecked(counter.keepScreenOn);
        if(counter.keepScreenOn) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_counter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DialogFragment frag;
        Bundle args;
        switch (item.getItemId()) {
            case R.id.mi_edit:
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra(EditActivity.INDEX, getIntent().getIntExtra(INDEX, -1));
                startActivityForResult(intent, 1);
                return true;
            case R.id.mi_delete:
                frag = new ConfirmDialog();
                args = new Bundle();
                args.putString(ConfirmDialog.MESSAGE,"Are you sure you wish to delete this counter?");
                frag.setArguments(args);
                frag.show(getSupportFragmentManager(), "delete");
                return true;
            case R.id.mi_reset:
                frag = new ConfirmDialog();
                args = new Bundle();
                args.putString(ConfirmDialog.MESSAGE,"Are you sure you wish to reset this counter?");
                frag.setArguments(args);
                frag.show(getSupportFragmentManager(), "reset");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void btnClick(View v) {
        TextView tv;
        switch (v.getId()) {
            case R.id.btnIncrement:
                counter.increment();
                tv = findViewById(R.id.txtRow);
                tv.setText(""+counter.rowNumber);
                tv = findViewById(R.id.txtRepeat);
                tv.setText(""+counter.repeatNumber);
                break;
            case R.id.btnDecrement:
                counter.decrement();
                tv = findViewById(R.id.txtRow);
                tv.setText(""+counter.rowNumber);
                tv = findViewById(R.id.txtRepeat);
                tv.setText(""+counter.repeatNumber);
                break;
            case R.id.chkLink:
                counter.linkCounters = !counter.linkCounters;
                break;
            case R.id.chkScreenOn:
                counter.keepScreenOn = !counter.keepScreenOn;
                if(counter.keepScreenOn) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
                break;
        }
    }

    @Override
    public void onclick(String tag, boolean ok) {
        switch (tag) {
            case "delete":
                if(ok) {
                    DataController.getInstance().deleteCounter(counter);
                    finish();
                }
                break;
            case "reset":
                if(ok) {
                    counter.reset();
                    TextView tv = findViewById(R.id.txtRow);
                    tv.setText(""+counter.rowNumber);
                    tv = findViewById(R.id.txtRepeat);
                    tv.setText(""+counter.repeatNumber);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK) {
            TextView tv = findViewById(R.id.txtName);
            tv.setText(counter.name);
            tv = findViewById(R.id.txtRow);
            tv.setText(""+counter.rowNumber);
            tv = findViewById(R.id.txtRepeat);
            tv.setText(""+counter.repeatNumber);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            DataController instance = DataController.getInstance();
            if(instance.file == null) {
                instance.file = new File(getFilesDir(), "data.bin");
            }
            instance.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
