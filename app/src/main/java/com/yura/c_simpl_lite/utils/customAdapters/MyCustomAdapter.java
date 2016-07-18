package com.yura.c_simpl_lite.utils.customAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yura.c_simpl_lite.R;
import com.yura.c_simpl_lite.domainEntities.CropField;

import java.util.ArrayList;

/**
 * Created by Yuriy S on 11.07.2016.
 */
public class MyCustomAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<CropField> objects;

    public MyCustomAdapter(Context context, ArrayList<CropField> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    // how many elems
    @Override
    public int getCount() {
        return objects.size();
    }
    // elem by position
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id by position
    @Override
    public long getItemId(int position) {
        return 0;
    }
    // list horizontal elem
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // uses empty(free view)
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_item, parent, false);
        }
        CropField cropField = getCropField(position);
        // fullfill View elem from data fields of CropFields: name, tillArea, crop
        ((TextView) view.findViewById(R.id.tvName)).setText(cropField.getName());
        ((TextView) view.findViewById(R.id.tvTillArea)).setText(String.valueOf(cropField.getTillArea())+"ha");
        ((TextView) view.findViewById(R.id.tvCrop)).setText(cropField.getCrop());


        return view;
    }
    // CropField by position
    CropField getCropField(int position) {
        return ((CropField) getItem(position));
    }
}
