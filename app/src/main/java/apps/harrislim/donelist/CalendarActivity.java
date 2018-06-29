package apps.harrislim.donelist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity {
    TextView ct;
    Intent i_self;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ct = (TextView) findViewById(R.id.calendarTitle);
        i_self = getIntent();
        String title = i_self.getStringExtra("title");
        ct.setText(title);
    }
}

/*
  사용한 오픈소스 캘린더 링크
  http://applandeo.com/blog/material-calendar-view-customized-calendar-widget-android/
*/