package com.eurus.medicoassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    enum Window{HOME, APPOINTMENTS}

    FloatingActionButton fab_book_app;
    BottomNavigationView bottomBar;
    Toolbar toolbar;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        fragmentManager=getSupportFragmentManager();

        showWindow(Window.HOME);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Medico-Assistant");

        fab_book_app = findViewById(R.id.fab_action);
        fab_book_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,BookAppointment.class);
                startActivity(i);
            }
        });

        bottomBar = findViewById(R.id.bottomNavigationView);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(bottomBar.getSelectedItemId() != item.getItemId()){
                    if(item.getItemId() == R.id.navigation_menu_home)
                        showWindow(Window.HOME);
                    else if(item.getItemId() == R.id.navigation_menu_appointment)
                        showWindow(Window.APPOINTMENTS);
                    return true;
                }
                return false;
            }
        });
    }

    private void showWindow(Window window) {
        switch (window){
            case HOME:
                fragmentManager.beginTransaction()
                        .replace(R.id.dashboard_frag_lay, new HomeFragment())
                        .commit();
                /*
                    .replace(R.id.dashboard_base_lay, new HomeFragment())
                    .commit();*/
                break;
            case APPOINTMENTS:
                fragmentManager.beginTransaction()
                        .replace(R.id.dashboard_frag_lay, new AppointmentsFragment())
                        .commit();
                break;
        }
    }

    public TabLayout getTabLayout(){
        return findViewById(R.id.tab_layout);
    }
}
