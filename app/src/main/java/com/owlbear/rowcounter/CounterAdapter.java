package com.owlbear.rowcounter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class CounterAdapter extends BaseAdapter implements ListAdapter {

    private DataController controller;
    private Context context;

    public CounterAdapter(Context context) {
        this.context = context;
        controller = DataController.getInstance();
    }

    @Override
    public int getCount() {
        return controller.size();
    }

    @Override
    public Counter getItem(int position) {
        return controller.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_counter, null);
        }

        view.setTag(position);

        Counter c = getItem(position);
        TextView tv = view.findViewById(R.id.txtName);
        tv.setText(c.name);
        tv = view.findViewById(R.id.txtRowNumber);
        tv.setText((""+c.rowNumber));

        view.findViewById(R.id.btnDelete).setTag(position);

        return view;
    }
}
