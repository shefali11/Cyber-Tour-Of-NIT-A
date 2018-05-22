package com.navigation.com.nb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;

public class contact extends AppCompatActivity {
    ListView lst;

    String[] name={"Shefali Gupta","Anusha Goswami", "Shivam Sigh Rajawat","Bishal Marak"};
    Integer[] imageId={ R.drawable.shefali,R.drawable.anusha, R.drawable.shivam_singh, R.drawable.bishal};
    String[] number={"+91-7409432626","+91-9407225314","+91-7386981586","+91-8787485741"};
    String[] gmail={"shef41184@gmail.com","anushagoswami10@gmail.com","shivamrajawatpy@gmail.com","marakbishal@gmail.com"};
    String[] linkedin={"https://www.linkedin.com/in/shefali-gupta-999532114/","https://www.linkedin.com/in/anusha-goswami/",
           "https://www.linkedin.com/in/shivam-singh-rajawat-80b7a5112/","https://www.linkedin.com/in/bishal-marak/"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);
        lst= (ListView) findViewById(R.id.list);
        CustomListView customListView=new CustomListView(this,name,number,gmail,imageId,linkedin);
        lst.setAdapter(customListView);


    }
    public void linked(View v)
    {   TextView tv4;
        tv4=(TextView) v.findViewById(R.id.linked);
        String yourtext= tv4.getText().toString();
        if(yourtext.equals("https://www.linkedin.com/in/shefali-gupta-999532114/"))
        {
        Intent myIntent = new Intent(this, shefali_linked.class);
        this.startActivity(myIntent); }
        else if(yourtext.equals("https://www.linkedin.com/in/anusha-goswami/"))
        {
            Intent myIntent = new Intent(this, anusha_linked.class);
            this.startActivity(myIntent);}
        else if(yourtext.equals("https://www.linkedin.com/in/shivam-singh-rajawat-80b7a5112/"))
        {
            Intent myIntent = new Intent(this, shivam_linked.class);
            this.startActivity(myIntent);}
        else if(yourtext.equals("https://www.linkedin.com/in/anusha-goswami/"))
        {
            Intent myIntent = new Intent(this, bishal_linked.class);
            this.startActivity(myIntent);}

    }
}


