package com.yura.c_simpl_lite.controllers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.yura.c_simpl_lite.utils.staticDataHolder.GlobalApplicationContext;
import com.yura.c_simpl_lite.utils.staticDataHolder.MyCustomExtra;

import java.util.List;

/**
 * Created by Yuriy S on 15.07.2016.
 */

public class SingleChoiceDialogFragment extends DialogFragment{


    public static final String DATA = "items";

    public static final String SELECTED = "selected";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)    {
        Resources res = getActivity().getResources();
        Bundle bundle = getArguments();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Select Item");
        dialog.setPositiveButton("Cancel", new PositiveButtonClickListener());

        List<String> list = (List<String>)bundle.get(DATA);
        int position = bundle.getInt(SELECTED);

        CharSequence[] cs = list.toArray(new CharSequence[list.size()]);
        dialog.setSingleChoiceItems(cs, position, selectItemListener);

        return dialog.create();
    }

    class PositiveButtonClickListener implements DialogInterface.OnClickListener    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            dialog.dismiss();
        }
    }

    DialogInterface.OnClickListener selectItemListener = new DialogInterface.OnClickListener()
    {

        @Override
        public void onClick(DialogInterface dialog, int which)
        {
           GoogleMap map = (GoogleMap) GlobalApplicationContext.getInstance().get("full_screen_map");
            switch (which){
                case 0:
                    MyCustomExtra.getMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case 1:
                    MyCustomExtra.getMap().setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
                case 2:
                    MyCustomExtra.getMap().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case 3:
                    MyCustomExtra.getMap().setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
            }
            //which means position
            dialog.dismiss();
        }

    };
}
