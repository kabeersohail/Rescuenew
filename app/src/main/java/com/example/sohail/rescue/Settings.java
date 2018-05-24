package com.example.sohail.rescue;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    ImageView darkmap,navybluemap,normal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        darkmap = findViewById(R.id.darkmap);
        navybluemap = findViewById(R.id.navyblue);
        normal = findViewById(R.id.normal);

        darkmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("MapStyle","dark").apply();
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(Settings.this,"Restart app",Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });

        navybluemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("MapStyle","Navybluemap").apply();
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(Settings.this,"Restart app",Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("MapStyle","normal").apply();
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(Settings.this,"Restart app",Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });

    }
}
