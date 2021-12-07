package com.example.myapplication;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import com.google.android.material.switchmaterial.SwitchMaterial;

import static android.app.PendingIntent.getActivity;

public class MyPref_Frag extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
        //dark theme
        View parentView;
        SwitchMaterial themeSwitch;
        TextView themeTV, titleTV;
        UserSetting settings;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //addPreferencesFromResource(R.xml.preferences);
            //dark theme
            settings = (UserSetting) getApplication();
            initWidgets();
            loadSharedPreferences();
            initSwitchListener();
        }

        //dark theme
        private void initWidgets() {
            themeTV = findViewById(R.id.themeTV);
            titleTV = findViewById(R.id.titleTV);
            themeSwitch = findViewById(R.id.themeSwitch);
            parentView = findViewById(R.id.parentView);
        }

        private void loadSharedPreferences() {
            SharedPreferences sharedPreferences = getSharedPreferences(UserSetting.PREFERENCES, MODE_PRIVATE);
            String theme = sharedPreferences.getString(UserSetting.CUSTOM_THEME, UserSetting.LIGHT_THEME);
            settings.setCustomTheme(theme);
        }

        private void initSwitchListener() {
            themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
                    if (checked)
                        settings.setCustomTheme(UserSetting.DARK_THEME);
                    else
                        settings.setCustomTheme(UserSetting.LIGHT_THEME);

                    SharedPreferences.Editor editor = getSharedPreferences(UserSetting.PREFERENCES, MODE_PRIVATE).edit();
                    editor.putString(UserSetting.CUSTOM_THEME, settings.getCustomTheme());
                    editor.apply();
                    updateView();
                }
            });
        }
        @Override
        /*public void onResume(){
            super.onResume();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String bgTheme = prefs.getString(getString(R.String.UserSetting.CUSTOM_THEME_key),getString());
        }*/

        private void updateView() {
            final int black = ContextCompat.getColor(this, R.color.black);
            final int white = ContextCompat.getColor(this, R.color.colorWhite);

            if (settings.getCustomTheme().equals(UserSetting.DARK_THEME)) {
                titleTV.setTextColor(white);
                themeTV.setTextColor(white);
                //themeTV.setText("Dark");
                parentView.setBackgroundColor(black);
                themeSwitch.setChecked(true);
            } else {
                titleTV.setTextColor(black);
                themeTV.setTextColor(black);
               // themeTV.setText("Light");
                parentView.setBackgroundColor(white);
                themeSwitch.setChecked(false);
            }
        }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller,0);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings_container, fragment)
                .addToBackStack(null)
                .commit();
        return true;
    }

    }


