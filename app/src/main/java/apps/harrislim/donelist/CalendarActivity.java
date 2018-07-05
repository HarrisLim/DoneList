package apps.harrislim.donelist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences calendarPre;
    SharedPreferences.Editor editor;
    Intent i_self;
    String title;
    TextView ct;
    Button totalButton;
    ArrayList<String> dayList = new ArrayList<String>();
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    MaterialCalendarView materialCalendarView;
    ArrayList<String> result = new ArrayList<String>();

    void customizeCalendar(){
        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new OneDayDecorator());
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if(isDuplicate(date)) { // 빨간점 삭제
                    Log.i("tag", "빨간점 삭제: "+ getDate(date));
                    result.remove(getDate(date));
                    editor.remove(getDate(date)).commit();
                    new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());
                    materialCalendarView.removeDecorators();
                    materialCalendarView.addDecorators(
                            new SundayDecorator(),
                            new SaturdayDecorator(),
                            new OneDayDecorator());
                }
                else { // 빨간점 생성
                    if(date!=null) {
                        Log.i("tag", "빨간점 생성: " + getDate(date));
                        result.add(getDate(date));
                        editor.putString(getDate(date), getDate(date));
                        editor.commit();
                        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());
                    }
                }
            }
        });
    }
    boolean isDuplicate(CalendarDay date){
        for(int i=0; i<result.size(); i++){
            if(result.get(i).equals(getDate(date))){
                return true;
            }
        }
        return false;
    }
    String getDate(CalendarDay date){
        Log.i("tag", "date: " + date);
        String str = date.toString();
        int a = str.indexOf("{");
        int b = str.indexOf("}");
        str = str.substring(a + 1, b);
        return str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("onCreate","onCreate!!");
        super.onCreate(savedInstanceState);
        i_self = getIntent();
        title = i_self.getStringExtra("title");
        setContentView(R.layout.activity_calendar);

        totalButton = (Button)findViewById(R.id.totalButton);
        totalButton.setOnClickListener(this);
        ct = (TextView) findViewById(R.id.calendarTitle);
        ct.setText(title);
        calendarPre = getSharedPreferences(title, 0);
        editor= calendarPre.edit();

        customizeCalendar();
    }


    @Override
    public void onClick(View v){
        Log.i("tag", "in onClick in CalendarActivity");
        switch (v.getId()){
            case R.id.totalButton: toTotal(); break;
        }
    }
    void toTotal(){
        Log.i("tag", "in toTotal in CalendarActivity");
        Intent inte = new Intent(CalendarActivity.this, YearListActivity.class);
        inte.putStringArrayListExtra("dayList", dayList);
        inte.putExtra("title", title);
        startActivity(inte);
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.i("tag", "onResume in CalendarView");
        Iterator<String> keys = calendarPre.getAll().keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            result.add(key);
        }
        // 지울 때 onResume이 실행되니까 ApiSimulator를 여기에.
        if(dayList!=null) dayList.removeAll(dayList);
        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());
    }
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {
        ArrayList<String> Time_Result;
        ApiSimulator(ArrayList<String> Time_Result){
            this.Time_Result = Time_Result;
        }
        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            if (result.size() > 0) {
                Calendar calendar = Calendar.getInstance();
                ArrayList<CalendarDay> dates = new ArrayList<>();
                /*특정날짜 달력에 점표시해주는곳*/
                /*월은 0이 1월 년,일은 그대로*/
                //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
                for(int i = 0 ; i < Time_Result.size() ; i ++){
                    CalendarDay day = CalendarDay.from(calendar);
                    String[] time = Time_Result.get(i).split("-"); // .하면 안돼
                    int year = Integer.parseInt(time[0]);
                    int month = Integer.parseInt(time[1]);
                    int dayy = Integer.parseInt(time[2]);

                    dates.add(day);
                    calendar.set(year, month, dayy);
                }
                dates.set(0, null); // 오늘 날짜를 추가하지 않아도 추가되어서 삭제.
                dates.add(CalendarDay.from(calendar)); // 배열 마지막 날짜가 안들어가서 직접 추가.
                return dates;
            }
            return null;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            Set<CalendarDay> fooSet;
            List<CalendarDay> fooList;

            if(calendarDays!=null){ // 빨간 점이 1개 이상일 때만
                // calendarDays내의 데이터 중복제거
                fooSet = new HashSet<CalendarDay>(calendarDays);
                Iterator<CalendarDay> keys = fooSet.iterator();
                while(keys.hasNext()){
                    CalendarDay key = keys.next();
                    Log.i("tag"," key: " + key);
                }
                fooList = new ArrayList<CalendarDay>(fooSet);
                for(int i=0; i<fooList.size(); i++){
                    Log.i("tag", "foo: "+fooList.get(i));
                }

                if(calendarDays==null) {
                    Log.i("tag", "null이야");
                    Log.i("tag", "fooList: " + fooList);
                    Log.i("tag", "calendarDays: "+ calendarDays);
                }
                else if(calendarDays!=null){
                    Log.i("tag", "null아니야 ");
                    Log.i("tag", "fooList.size(): "+ fooList.size());
                            Log.i("tag", "calendarDays.size(): "+ calendarDays.size());
                    Log.i("tag", "fooList: "+ fooList);
                    Log.i("tag", "calendarDays: "+ calendarDays);
                }

                dayList.removeAll(dayList);
                for(int i=1; i<fooList.size(); i++) // 0은 null이 있어.
                    if(fooList.get(i)!=null)
                        dayList.add(getDate(fooList.get(i)));

                materialCalendarView.addDecorator(new EventDecorator(Color.RED, fooList, CalendarActivity.this));
            }

            super.onPostExecute(calendarDays);
            if (isFinishing()) {
                return;
            }
        }
    }

}

/*
  사용한 오픈소스 캘린더 링크
  http://applandeo.com/blog/material-calendar-view-customized-calendar-widget-android/

  이거 사용했어.
  https://github.com/prolificinteractive/material-calendarview // github주소
  http://dpdpwl.tistory.com/3 // 설명 블로그
*/