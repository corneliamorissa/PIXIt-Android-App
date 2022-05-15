package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.notesapp.appObjects.Group;
import com.example.notesapp.appObjects.Topic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class Topic_Main_Page extends AppCompatActivity {
    private RequestQueue requestQueue;
    private static final String TOPIC_URL = "https://studev.groept.be/api/a21pt103/grap_topics/";
    private static final String NEW_TOPIC_URL = "https://studev.groept.be/api/a21pt103/add_topic/";
    private ArrayList<Topic> topics;
    private int id;
    private int g_id;
    Button add;
    AlertDialog dialog;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_page);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            g_id = extras.getInt("id");
            //The key argument here must match that used in the other activity
        }

        topics = new ArrayList<>();
        add = findViewById(R.id.add);
        layout = findViewById(R.id.container);

        grabTopics();

        buildDialog();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });


    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.add_topic, null);

        final EditText name = view.findViewById(R.id.nameEdit);

        builder.setView(view);
        builder.setTitle("Enter name")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        addTopic(name.getText().toString());
                        newTopic(name.getText().toString());
                        Topic t = new Topic(name.getText().toString(), g_id);
                        topics.add(t);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        dialog = builder.create();
    }

    private void addTopic(String name) {
        final View view = getLayoutInflater().inflate(R.layout.row_topic, null);


        Button topic_btn = view.findViewById(R.id.button_name);

        topic_btn.setText(name);

        topic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Topic_Main_Page.this, Topic_Activity.class)
                        .putExtra("name",name)
                        );
            }
        });

        layout.addView(view);
    }


    //TODO method to grab all topic for a group
    private void grabTopics()
    {
            String url = TOPIC_URL;

            JSONObject p = new JSONObject();
        String requestURL = url + g_id;

            JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.GET,
                    requestURL,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            int p;
                            for (int i = 0; i < response.length(); ++i) {
                                JSONObject o = null;
                                try {
                                    o = response.getJSONObject(i);
                                    int t_id = Integer.parseInt((String) o.get("topic_id"));
                                    String name = (String) o.get("topic_name").toString();
                                    Topic t = new Topic(t_id,name,g_id);
                                   topics.add(t);
                                   addTopic(t.getName());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }


                        }
                    },
                    error -> Toast.makeText(Topic_Main_Page.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

            requestQueue.add(queueRequest);
        }


        public void newTopic(String name)
        {

            String url = NEW_TOPIC_URL;
            requestQueue = Volley.newRequestQueue(this);
            JSONObject p = new JSONObject();
            String requestURL = url + g_id + "/" + name;

            JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.POST,
                    requestURL,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                                Toast.makeText(Topic_Main_Page.this, "topic added", Toast.LENGTH_SHORT).show();

                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(Topic_Main_Page.this, "Unable to add topic", Toast.LENGTH_LONG).show();

                                    }


                        });

                        //error -> Toast.makeText(Topic_Main_Page.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());


        }
    }
//TODO add topic page  layout


