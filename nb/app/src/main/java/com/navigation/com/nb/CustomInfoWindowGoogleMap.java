package com.navigation.com.nb;

/**
 * Created by Admin on 3/17/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.content.Intent;
import java.util.Locale;
import android.widget.Toast;



/**
 * Created by TempAdmin2 on 18-02-2018.
 */

public class CustomInfoWindowGoogleMap implements InfoWindowAdapter {
    private Context context;
    //TTS object

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoContents(Marker marker) { return null; }

    @Override
    public View getInfoWindow(Marker marker) {

        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.map_custom_infowindow, null);

        TextView name_tv = view.findViewById(R.id.name);
        ImageView img = view.findViewById(R.id.pic);
        name_tv.setText(marker.getTitle());
        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
        int imageId = context.getResources().getIdentifier(infoWindowData.getImage().toLowerCase(),
                "drawable", context.getPackageName());
        img.setImageResource(imageId);
        return view;
    }


}
