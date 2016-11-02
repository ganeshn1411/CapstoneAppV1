package jialiw.cmu.edu.capstoneappv1;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.logging.SimpleFormatter;

public class ActivityDetailsTest extends AppCompatActivity {
//    Calendar c = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_test);

        Button bCount = (Button) findViewById(R.id.time_count_button);

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
            }
        });



    }


}
