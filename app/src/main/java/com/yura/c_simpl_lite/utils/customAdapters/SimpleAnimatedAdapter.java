package com.yura.c_simpl_lite.utils.customAdapters;

import android.animation.Animator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yura.c_simpl_lite.R;
import com.yura.c_simpl_lite.domainEntities.CropField;

import java.util.ArrayList;

/**
 * Created by Yuriy S on 15.07.2016.
 */
public class SimpleAnimatedAdapter extends BaseAdapter {
private  final static String TAG = "myLog";
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<CropField> objects;

    public SimpleAnimatedAdapter(Context context, ArrayList<CropField> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    // quantity of elems
    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id by position
    //unnsupported
    @Override
    public long getItemId(int position) {
        return 0;
    }
    // list horizontal elem
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_item, parent, false);
        }
        animatePostHc(position, view);



        CropField cropField = getCropField(position);
        // fullfill View elem from data fields of CropFields: name, tillArea, crop
        ((TextView) view.findViewById(R.id.tvName)).setText(cropField.getName());
        ((TextView) view.findViewById(R.id.tvTillArea)).setText(String.valueOf(cropField.getTillArea())+"ha");
        ((TextView) view.findViewById(R.id.tvCrop)).setText(cropField.getCrop());


        return view;
    }


    private void animatePostHc(int position, View v) {
        Log.d(TAG, (v == null) + " нал тест");
//        v.setTranslationX(1000f);
//        v.setTranslationY(100f);
        //-----------------------------------------------------------------------------//
//        Animation anim = AnimationUtils.loadAnimation(ctx, R.anim.alpha);
        Animation anim1 = AnimationUtils.loadAnimation(ctx, R.anim.combo);
//        Animation anim2= AnimationUtils.loadAnimation(ctx, R.anim.rotate);
//        Animation anim3 = AnimationUtils.loadAnimation(ctx, R.anim.scale);
        v.startAnimation(anim1);

        //---------------------------------------------------------------------------------//
//        v.animate().alpha(50).setDuration(1000).setListener(new InnerAnimatorListener(v)).start();;
//        v.animate().alpha(0).alphaBy(0).setDuration(1000).start();
//        v.animate().translationY(0).translationX(0).setDuration(1000)
//                .setListener(new InnerAnimatorListener(v)).start();

//        v.animate().withLayer().translationY(0).translationX(0).setDuration(300).
//                setListener(new InnerAnimatorListener(v)).start();
    }

    static class InnerAnimatorListener implements Animator.AnimatorListener {

        private View v;

        private int layerType;

        public InnerAnimatorListener(View v) {
            this.v = v;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            layerType = v.getLayerType();
            v.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            v.setLayerType(layerType, null);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }
    }





    // CropField by position
    CropField getCropField(int position) {
        return ((CropField) getItem(position));
    }
}