package com.example.sohail.rescue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context)
    {
        this.context = context;
    }

    public int[] icons = {
            R.drawable.helpingbanner,
            R.drawable.owl,
            R.drawable.lap
    };

    public String[] Heading = {
      "Help Others",
      "Get Helped",
      "Build a Better place"
    };

    public String[] Desc = {
        "Reach out to the surrounding people who are in need of your help",
        "Get helped when you are stuck in bad situations",
        "Together build a better place"
    };

    @Override
    public int getCount() {
        return Heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView1 = view.findViewById(R.id.textView);
        TextView textView2 = view.findViewById(R.id.textView2);
        imageView.setImageResource(icons[position]);
        textView1.setText(Heading[position]);
        textView2.setText(Desc[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
