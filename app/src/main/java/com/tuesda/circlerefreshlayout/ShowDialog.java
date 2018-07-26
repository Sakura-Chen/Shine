package com.tuesda.circlerefreshlayout;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ShowDialog {

    private View view;
    private Context context;

    public ShowDialog(View view, Context context){
        this.view=view;
        this.context=context;

    }
    public void Show(){
        int viewId=view.getId();
        LayoutInflater inflater=LayoutInflater.from(context);
        if (viewId==R.id.chenlian){
            View view=inflater.inflate(R.layout.suggestion_layout,null);
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}

