package jialiw.cmu.edu.capstoneappv1;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.nfc.Tag;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.SimpleFormatter;

public class ActivityDetailsTest extends AppCompatActivity {
//    Calendar c = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_test);

        final Button bCount = (Button) findViewById(R.id.time_count_button);

        Log.v("onCreate method","26");
        bCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("onClick method","30");
//                Calendar c = Calendar.getInstance();
//                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//                String formattedDate = df.format(c.getTime());
//                System.out.println("In line 36: " + formattedDate);
//                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                Date currentDate = new Date(System.currentTimeMillis());
                CharSequence s  = DateFormat.format("dd/mm/yyyy hh:mm:ss",currentDate.getTime());
                System.out.println("line 33");
                System.out.println(s.toString());
                TextView textView = (TextView) findViewById(R.id.test_text_view);
//                textView.setText(formattedDate);
                textView.setText(s.toString());
                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response){
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Log.i("test", "line 59");
                                Intent intent = new Intent(ActivityDetailsTest.this, ActivityDetailsTest.class);
                                ActivityDetailsTest.this.startActivity(intent);
                            } else {
                                Log.i("fail","in line 64");
                                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDetailsTest.this);
                                builder.setMessage("Register Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Intent intent = getIntent();
                final String username = intent.getStringExtra("username");
                ArrayList<String> resultList = intent.getStringArrayListExtra("info_list");
                Log.i("line 83", "start test arraylist");
                String procedureId = "", processTime = "";
                int processId = -1;
                Iterator<String> itr = resultList.iterator();
                while (itr.hasNext()){
                    procedureId = itr.next();
                    processId = Integer.parseInt(itr.next());
                    processTime = itr.next();
                }

                ActivityTestRequest1 activityTestRequest1 = new ActivityTestRequest1(username,procedureId, processId, processTime, responseListener );
                RequestQueue queue = Volley.newRequestQueue(ActivityDetailsTest.this);
                queue.add(activityTestRequest1);
                Log.e("line 99", "after activityTestRequest1 queue" );
            }
        });



    }


}
