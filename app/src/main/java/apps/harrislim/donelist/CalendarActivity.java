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
import java.util.Iterator;
import java.util.List;
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
//        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

//                boolean isDuplicate = isDuplicate(date);
                if(isDuplicate(date)) {
                    Log.i("tag", "겹침");
                    result.remove(getDate(date));
                    editor.remove(getDate(date));
                    editor.commit();
//                    Intent intent = new Intent(CalendarActivity.this, CalendarActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    widget.refreshDrawableState();
//                    materialCalendarView.refreshDrawableState();
//                    materialCalendarView.invalidateDecorators();
                    finish();
                    startActivity(getIntent());
                }
                else {
                    Log.i("tag", "안겹침");
                    result.add(getDate(date));
                    editor.putString(getDate(date), getDate(date));
                    editor.commit();
                    // 생성될 때 onResume에 안걸리고 ApiSimulator가 여기서 실행되니까
                    new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());
                }
                Log.i("tag", "hi: "+ calendarPre.getString(getDate(date),null));
//                new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());
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
        String str = date.toString();
        int a = str.indexOf("{");
        int b = str.indexOf("}");
        str = str.substring(a+1, b);
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

//        String[] str = calendarPre.getAll();

//        Log.i("tag", "getAll: "+calendarPre.getAll().size());
//        Iterator<String> keys = calendarPre.getAll().keySet().iterator();
//        while(keys.hasNext()){
//            String key = keys.next();
//            result.add(key);
//        }

//        calendarPre.getString(getDate(date),null);

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
        Log.i("tag", "이건 언제돼?");
//        customizeCalendar();
        Log.i("tag", "getAll: "+calendarPre.getAll().size());
        Iterator<String> keys = calendarPre.getAll().keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            result.add(key);
        }
        // 지울 때 onResume이 실행되니까 ApiSimulator를 여기에.
        dayList.removeAll(dayList);
        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());
        super.onResume();
    }
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {
        ArrayList<String> Time_Result;
//        boolean isDuplicate;
//        ApiSimulator(ArrayList<String> Time_Result, boolean isDuplicate){
        ApiSimulator(ArrayList<String> Time_Result){
            this.Time_Result = Time_Result;
//            this.isDuplicate = isDuplicate;
        }
        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
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
            if(calendarDays==null)
                Log.i("tag", "null이야");
            else if(calendarDays!=null){
                Log.i("tag", "null아니야 ");
//                for(CalendarDay x: calendarDays) Log.i("tag", "calendarDays: "+ calendarDays);
                Log.i("tag", "calendarDays: "+ calendarDays.size());
            }
            if(calendarDays!=null){
                dayList.removeAll(dayList);
                for(int i=1; i<calendarDays.size(); i++){ // 0은 null이 있어.
//                    for(int j=0; j<dayList.size(); j++){
//                        if(!(calendarDays.get(i).toString().equals(dayList.get(j)))){
                            dayList.add(getDate(calendarDays.get(i)));
//                        }
//                    }
                }
                for(String d: dayList) Log.i("tag", "dddd: "+ d);
            }
            super.onPostExecute(calendarDays);
            if (isFinishing()) {
                return;
            }
            if(calendarDays!=null) {// 빨간 점이 1개 이상일 때만
//                Log.i("tag", "size: " + calendarDays.size());
                materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays, CalendarActivity.this));
            }
//            }else if(isDuplicate){ // 겹치면
//                materialCalendarView.addDecorator(new EventDecorator(Color.argb(00,00,00,00), calendarDays, CalendarActivity.this));
//            }
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