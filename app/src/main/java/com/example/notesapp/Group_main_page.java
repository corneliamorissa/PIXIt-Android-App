package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class Group_main_page extends AppCompatActivity {
    String name;
    int id;
    TextView name_show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main_page);
        System.out.println(name);
        name_show = findViewById(R.id.groupName);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name").toUpperCase(Locale.ROOT);
            id = extras.getInt("id");
            //The key argument here must match that used in the other activity
        }
        name_show.setText(name);


    }

    public void onTopic_Clicked(View caller) {
        startActivity(new Intent(Group_main_page.this, Topic_Main_Page.class).putExtra("name",name)
                .putExtra("id",id));
        finish();
    }
    public void onMemebrs_Clicked(View caller) {
        startActivity(new Intent(Group_main_page.this, Topic_Main_Page.class).putExtra("name",name)
                .putExtra("id",id));
        finish();
    }
}