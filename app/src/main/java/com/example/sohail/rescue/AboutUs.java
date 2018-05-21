package com.example.sohail.rescue;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;

public class AboutUs extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Context mContext;
    ClipData.Item item;
    Menu menuk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        navigationView =  findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.fragAU);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.signout){
            finish();
            AuthUI.getInstance().signOut(this);
            startActivity(new Intent(this,MainActivity.class));
        }


        return false;
    }

}
