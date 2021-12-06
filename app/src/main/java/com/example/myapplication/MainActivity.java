package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.FragmentTransaction;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {

    //dark theme
    private View parentView;
    private SwitchMaterial themeSwitch;
    private TextView themeTV, titleTV;
    private UserSetting settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //preference
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        MyPref_Frag mypref_frag = new MyPref_Frag();
        fragmentTransaction.replace(android.R.id.content,mypref_frag);
        fragmentTransaction.commit();

        //dark theme
        settings = (UserSetting)getApplication();
        initWidgets();
        loadSharedPreferences();
        initSwitchListener();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //dark theme
    private void initWidgets(){
        themeTV = findViewById(R.id.themeTV);
        titleTV = findViewById(R.id.titleTV);
        themeSwitch = findViewById(R.id.themeSwitch);
        parentView = findViewById(R.id.parentView);
    }

    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(UserSetting.PREFERENCES,MODE_PRIVATE);
        String theme = sharedPreferences.getString(UserSetting.CUSTOM_THEME, UserSetting.LIGHT_THEME);
        settings.setCustomTheme(theme);
    }

    private void initSwitchListener(){
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
                if(checked)
                    settings.setCustomTheme(UserSetting.DARK_THEME);
                else
                    settings.setCustomTheme(UserSetting.LIGHT_THEME);

                SharedPreferences.Editor editor = getSharedPreferences(UserSetting.PREFERENCES,MODE_PRIVATE).edit();
                editor.putString(UserSetting.CUSTOM_THEME,settings.getCustomTheme());
                editor.apply();
                updateView();
            }
        });
    }

    private void updateView() {
        final int black = ContextCompat.getColor(this,R.color.black);
        final int white = ContextCompat.getColor(this,R.color.colorWhite);

        if(settings.getCustomTheme().equals(UserSetting.DARK_THEME)){
            titleTV.setTextColor(white);
            themeTV.setTextColor(white);
            themeTV.setText("Dark");
            parentView.setBackgroundColor(black);
            themeSwitch.setChecked(true);
        }
        else{
            titleTV.setTextColor(black);
            themeTV.setTextColor(black);
            themeTV.setText("Light");
            parentView.setBackgroundColor(white);
            themeSwitch.setChecked(false);
        }
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this,MyPref_Frag.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}