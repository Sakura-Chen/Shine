package com.tuesda.circlerefreshlayout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class CardAdapter extends BaseAdapter {

    private List<String> city;
    private List<String> name;
    private List<String> telephonenumber;
    private Context context;
    private LayoutInflater inflater;

    public void setData(List<String> data){
        this.city=data;
    }

    public CardAdapter(Context context, List<String> city,List<String> name,List<String> telephonenumber) {
        this.context = context;
        this.city = city;
        this.name = name;
        this.telephonenumber = telephonenumber;
        this.inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return city.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final CardViewHolder holder;
        if(convertView==null){
            holder=new CardViewHolder();
            convertView= inflater.inflate(R.layout.list_item_card,null);
            holder.cardcity=convertView.findViewById(R.id.cardcity);
            holder.name=convertView.findViewById(R.id.name);
            holder.telephonenumber=convertView.findViewById(R.id.telephonenumber);
            holder.editfamily=convertView.findViewById(R.id.editfamily);
            holder.deletecard=convertView.findViewById(R.id.deletecard);
            holder.sendmessage=convertView.findViewById(R.id.sendmessage);
            convertView.setTag(holder);
        }
        else {
            holder=(CardViewHolder) convertView.getTag();
        }
//        String str=data.get(position);

        holder.telephonenumber.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        holder.cardcity.setText(city.get(position));
        holder.name.setText(name.get(position));
        holder.telephonenumber.setText(telephonenumber.get(position));

        holder.editfamily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    holder.name.setEnabled(true);
                    holder.telephonenumber.setEnabled(true);
                }
                else {
                    holder.name.setEnabled(false);
                    holder.telephonenumber.setEnabled(false);

                    name.set(position,holder.name.getText().toString());
                    telephonenumber.set(position,holder.telephonenumber.getText().toString());
                    if(onUpdataClickListener!=null)
                    {
                        onUpdataClickListener.onUpdataClick();
                    }
                    if (!holder.name.getText().toString().isEmpty()&&!holder.telephonenumber.getText().toString().isEmpty()) {
                        Toast.makeText(context, "家庭成员设置成功", Toast.LENGTH_SHORT).show();
                    }
                    else if (holder.name.getText().toString().isEmpty()&&!holder.telephonenumber.getText().toString().isEmpty()){
                        Toast.makeText(context, "未设置昵称", Toast.LENGTH_SHORT).show();
                    }
                    else if(!holder.name.getText().toString().isEmpty()&&holder.telephonenumber.getText().toString().isEmpty()){
                        Toast.makeText(context, "未设置电话号码", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "未设置任何信息", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        holder.deletecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDelteClickListener!=null){
                    onDelteClickListener.onDeleteClick(position);
                }
            }
        });
        holder.cardcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onNewcityClickListener!=null){
                    onNewcityClickListener.onNewcityClick(position);
                }
            }
        });
        holder.sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSendMessageClickListener!=null){
                    onSendMessageClickListener.onSendMessageClick(city.get(position),telephonenumber.get(position));
                }
            }
        });
        return convertView;
    }

    private OnDeleteClickListener onDelteClickListener=null;
    private OnNewcityClickListener onNewcityClickListener=null;
    private OnSendMessageClickListener onSendMessageClickListener=null;
    private OnUpdataClickListener onUpdataClickListener=null;
    public interface OnDeleteClickListener{
        void onDeleteClick(int position);
    }
    public interface OnNewcityClickListener{
        void onNewcityClick(int position);
    }
    public interface OnSendMessageClickListener{
        void onSendMessageClick(String cityname,String telephonenumber);
    }
    public interface OnUpdataClickListener{
        void onUpdataClick();
    }
    public void setOnDeleteClickListener(OnDeleteClickListener listener){
        this.onDelteClickListener=listener;
    }
    public void setOnNewcityClickListener(OnNewcityClickListener listener){
        this.onNewcityClickListener=listener;
    }
    public void setOnSendMessageClickListener(OnSendMessageClickListener listener){
        this.onSendMessageClickListener=listener;
    }
    public void setOnUpdataClickListener(OnUpdataClickListener listener){
        this.onUpdataClickListener=listener;
    }
    public List<String> getName(){
        return this.name;
    }
    public List<String> getTelephoneNumber(){
        return this.telephonenumber;
    }
}
