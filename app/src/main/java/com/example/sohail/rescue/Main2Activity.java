package com.example.sohail.rescue;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Canvas canvas = new Canvas();
        onDraw(canvas);
    }

    public Main2Activity(Context context){
        super();
    }

    protected void onDraw(Canvas canvas){
        Context context = getApplicationContext();
        int color = ContextCompat.getColor(context, R.color.darkgray);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(5,5,10,10,paint);
        canvas.drawOval(0,0,5,5,paint);
    }

}
