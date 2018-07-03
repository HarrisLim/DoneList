package apps.harrislim.donelist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class TotalActivity extends AppCompatActivity {
    TextView totalTitle;
    String title;
    Intent i_self;
    ArrayList<String> dayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);

        i_self = getIntent();
        title = i_self.getStringExtra("title");
        dayList = i_self.getStringArrayListExtra("dayList");
        totalTitle = (TextView) findViewById(R.id.totalTitle);
        totalTitle.setText(title);
        for(int i=0; i<dayList.size(); i++){
            Log.i("tag", "haha: "+dayList.get(i));
        }
    }
}
