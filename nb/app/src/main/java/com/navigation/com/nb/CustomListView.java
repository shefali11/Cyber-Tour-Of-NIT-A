package com.navigation.com.nb;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Shefali on 22-03-2018.
 */

public class CustomListView extends ArrayAdapter<String> {
     private String[] name;
     private String[] gmail;
    private Integer[] imageId;
    private String[] number;
    private String[] linkedin;
    private Activity context;
    public CustomListView(Activity context, String[] name,String[] number, String[] gmail,Integer[] imageId,String[] linkedin) {
        super(context, R.layout.listviewlayout,name);
        this.context=context;
        this.name=name;
        this.gmail=gmail;
        this.number=number;
       this.linkedin=linkedin;
        this.imageId=imageId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v=convertView;
        ViewHolder viewHolder=null;
        if(v==null)
        {
            LayoutInflater layoutInflater=context.getLayoutInflater();
            v=layoutInflater.inflate(R.layout.listviewlayout,null,true);
            viewHolder=new ViewHolder(v);
            v.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)v.getTag();

        }
        viewHolder.iv.setImageResource(imageId[position]);
        viewHolder.tv1.setText(name[position]);
        viewHolder.tv2.setText(number[position]);
        viewHolder.tv3.setText(gmail[position]);
        viewHolder.tv4.setText(linkedin[position]);
        return v;
    }
    class ViewHolder
    {
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        ImageView iv;
ViewHolder(View v)
{
    tv1=(TextView) v.findViewById(R.id.names);
    tv2=(TextView) v.findViewById(R.id.ph_num);
    tv3=(TextView) v.findViewById(R.id.gmail);
    tv4=(TextView) v.findViewById(R.id.linked);
    iv=(ImageView) v.findViewById(R.id.iv);


}

    }
}
