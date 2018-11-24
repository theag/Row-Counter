package com.owlbear.rowcounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends FragmentActivity implements ConfirmDialog.ConfirmDialogListener {

    private CounterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File f = new File(getFilesDir(), "data.bin");
        if(f.exists()) {
            try {
                DataController.loadInstance(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ListView lv = findViewById(R.id.lvMain);
        adapter = new CounterAdapter(this);
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miAdd:
                Intent intent = new Intent(this, EditActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void btnClick(View v) {
        switch(v.getId()) {
            case R.id.llCounter:
                Intent intent = new Intent(this, CounterActivity.class);
                intent.putExtra(CounterActivity.INDEX, (int)v.getTag());
                startActivityForResult(intent, 1);
                break;
            case R.id.btnDelete:
                DialogFragment frag = new ConfirmDialog();
                Bundle args = new Bundle();
                args.putString(ConfirmDialog.MESSAGE,"Are you sure you wish to delete this counter?");
                frag.setArguments(args);
                frag.show(getSupportFragmentManager(), ""+v.getTag());
                break;
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

    @Override
    public void onclick(String tag, boolean ok) {
        if(ok) {
            DataController.getInstance().deleteCounter(Integer.parseInt(tag));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1) {
            adapter.notifyDataSetChanged();
        }
    }
}
