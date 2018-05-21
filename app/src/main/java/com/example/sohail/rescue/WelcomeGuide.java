package com.example.sohail.rescue;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

public class WelcomeGuide extends AppCompatActivity {
    public static GoogleApiClient welcomeGuideApiClient;
    ViewPager viewPager;
    TextView[] textView;
    LinearLayout linearLayout;
    SliderAdapter sliderAdapter;
    Button javaback,javanext;
    int currentpage;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_guide);

        viewPager = findViewById(R.id.XmlPager);
        linearLayout = findViewById(R.id.XmlDots);
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        javaback = findViewById(R.id.Xmlback);
        javanext = findViewById(R.id.Xmlnext);
        javanext.setEnabled(true);
        javanext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==(textView.length)-1)
                    startActivity(new Intent(WelcomeGuide.this,MapsActivity.class));
                viewPager.setCurrentItem(currentpage + 1);
            }
        });

        javaback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i--;
                viewPager.setCurrentItem(currentpage - 1);
            }
        });

        viewPager.addOnPageChangeListener(viewListener);

    }

    public void addDotsIndicator(int position)
    {
        textView = new TextView[3];
        linearLayout.removeAllViews();
        for(int i =0;i<textView.length;i++){
            textView[i] = new TextView(this);
            textView[i].setText(Html.fromHtml("&#8226;"));
            textView[i].setTextSize(35);
            textView[i].setTextColor(getResources().getColor(R.color.TransparentWhite));
            linearLayout.addView(textView[i]);
        }

        if(textView.length > 0){
            textView[position].setTextColor(getResources().getColor(R.color.white));
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentpage=position;

            if(position == 0)
            {
                i=0;
                javanext.setEnabled(true);
                javaback.setEnabled(false);
                javaback.setVisibility(View.INVISIBLE);
                javaback.setText("");
                javanext.setText("Next");
            }

            if(position == 1){
                i=1;
                javaback.setEnabled(true);
                javaback.setVisibility(View.VISIBLE);
                javaback.setText("Back");
                javanext.setText("Next");
            }

            else if(position == (textView.length)-1){
                i=2;
                javanext.setEnabled(true);
                javaback.setEnabled(true);
                javaback.setVisibility(View.VISIBLE);
                javaback.setText("Back");
                javanext.setText("Finish");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
