package com.eurus.medicoassistant;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab_book_app;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        fab_book_app = findViewById(R.id.fab_action);
        fab_book_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,BookAppointment.class);
                startActivity(i);
            }
        });
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_list1) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                }
                else if(tabId == R.id.tab_list2){
                }
            }
        });
    }
}
