package apps.harrislim.donelist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

public class CalendarActivity extends AppCompatActivity {
    TextView ct;
    Intent i_self;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    MaterialCalendarView materialCalendarView;

    void customizeCalendar(){
        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ct = (TextView) findViewById(R.id.calendarTitle);
        i_self = getIntent();
        String title = i_self.getStringExtra("title");
        ct.setText(title);

        customizeCalendar();
    }
}

/*
  사용한 오픈소스 캘린더 링크
  http://applandeo.com/blog/material-calendar-view-customized-calendar-widget-android/

  이거 사용했어.
  https://github.com/prolificinteractive/material-calendarview // github주소
  http://dpdpwl.tistory.com/3 // 설명 블로그
*/