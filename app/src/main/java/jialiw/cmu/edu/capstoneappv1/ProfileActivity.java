package jialiw.cmu.edu.capstoneappv1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int resCode;
        InputStream inputStream;
        Gson gson = new Gson();
        final TextView homepage_username = (TextView) findViewById(R.id.homepage_username);
        final TextView welcomeMessage = (TextView) findViewById(R.id.homepage_welcome);
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        String result = "";
        new RetrieveDataTask().execute();

        String message = username + " " + result;
        welcomeMessage.setText(message);
        homepage_username.setText(username);

        Button button = (Button) findViewById(R.id.homepage_start_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(ProfileActivity.this, ActivityDetailsTest.class);
                startIntent.putExtra("username", username);
                startActivity(startIntent);
            }
        });
    }


    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.EMAIL_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            //calling logout method when the logout button is clicked
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    public class RetrieveDataTask extends AsyncTask<String, Void, String> {
        private AlertDialog.Builder alertDialogBuilder;
        private AlertDialog alertDialog;
        private int resCode;
        private Context context;
        private HttpURLConnection httpURLConnection;
        private  String result;

        protected void onPreExecute() {
            super.onPreExecute();
            alertDialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
        }

        @Override
        protected String doInBackground(String... urls) {

            InputStream inputStream;
            try{
                URL url = new URL("https://intense-mountain-41887.herokuapp.com/" + "paa001");
                Log.e("in try", "line 154");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                Log.e("in try", "line 156");

                httpURLConnection = (HttpURLConnection)urlConnection;
                httpURLConnection.setAllowUserInteraction(false);
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect(); //android.os.NetworkOnMainThreadException

                //waiting for server response
                resCode = httpURLConnection.getResponseCode();

                if(resCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line=reader.readLine())!=null){
                        sb.append(line).append("\n");
                    }
                    inputStream.close();
                    result = sb.toString();
                    Log.e("in try block",result);
                }
            }catch (Exception e){
                e.printStackTrace();
                result = e.getMessage();
            }
            return result;
        }
        // method does not call
        protected  void onPostExecute(String result) {
            super.onPostExecute(result);
            if (resCode == HttpURLConnection.HTTP_NO_CONTENT) {
                String msg = null;
                Log.e("in post", "line 190");
                try {
                    msg = httpURLConnection.getResponseMessage();
                    alertDialogBuilder.setTitle("No matched data");
                    alertDialogBuilder.setMessage(msg)
                            .setNegativeButton("OK",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id){
                                    dialog.cancel();
                                }
                            });
                    Log.e("in try","line 200");
                } catch (IOException e) {
                    e.printStackTrace();
                    alertDialogBuilder.setTitle("No matched data error");
                    alertDialogBuilder.setMessage("Cannot find response message")
                            .setNegativeButton("OK",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id){
                                    dialog.cancel();
                                }
                            });
                    Log.e("in catch","line 210");
                }
            } else if (resCode != HttpURLConnection.HTTP_OK) {
                alertDialogBuilder.setTitle("Database import error!");
                alertDialogBuilder.setMessage("Cannot connect database. Pls check the setttings.")
                        .setNegativeButton("OK",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                dialog.cancel();
                            }
                        });
                Log.e("in else if", "line 220");
            }
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

    }


}
