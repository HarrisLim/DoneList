package apps.harrislim.donelist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class YearListActivity extends AppCompatActivity {
    Intent i_self, inte;
    String title;
    ArrayList<String> dayList = new ArrayList<String>();
    ArrayList<String> monthList = new ArrayList<String>();
//    ArrayList<ArrayList<String>> yearList = new ArrayList<ArrayList<String>>();
    HashSet<String> yearSet = new HashSet<String>();
    List<String> yearList;
    ArrayAdapter<String> aa;
    int[] month;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("tag", "onCreate in YearListActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_list);

        month = new int[12];
        i_self = getIntent();
        title = i_self.getStringExtra("title");
        dayList = i_self.getStringArrayListExtra("dayList");
        lv = (ListView) findViewById(R.id.MonthListview);
        lv.addFooterView(new View(this), null, true);
        for(int i=0; i<dayList.size(); i++){
            yearSet.add(pickYear(dayList.get(i)));
//            Log.i("tag", "haha in M "+ pickYear(dayList.get(i)));
//            Log.i("tag", "haha in MM "+ pickMonth(dayList.get(i)));
//            Log.i("tag", "haha in MM "+ pickDay(dayList.get(i)));
            Log.i("tag", "haha in MonthList: "+dayList.get(i));
        }
        yearList = new ArrayList<String>(yearSet);
        Collections.sort(yearList);
        for(String x: yearList) Log.i("tag", "yearList: "+ x);

        aa = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, yearList);
        lv.setAdapter(aa);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.i("tag", "year: "+ aa.getItem(position));
                inte = new Intent(YearListActivity.this, TotalActivity.class);
                countMonth(aa.getItem(position));
                inte.putStringArrayListExtra("monthList", monthList); // 여기에 매 달 나오게하자.
                inte.putExtra("title", aa.getItem(position)+"년");
                startActivity(inte);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        clear();
        Log.i("tag", "onResume in YearListActivity");
    }
    void clear(){
        if(monthList!=null) monthList.removeAll(monthList);
        Arrays.fill(month, 0);
    }
    void countMonth(String year){ // 여기 메소드랑 년도에 맞는 월 개수를 세서 월에 더해.
        for(int i=0; i<dayList.size(); i++){
            if(dayList.get(i).contains(year)){
                Log.i("tag", "test: "+ dayList.get(i));
                switch(pickMonth(dayList.get(i))){ // 월은 -1이라서 0부터 줬따.
                    case "0": month[0] = month[0]+1; break;
                    case "1": month[1] = month[1]+1; break;
                    case "2": month[2] = month[2]+1; break;
                    case "3": month[3] = month[3]+1; break;
                    case "4": month[4] = month[4]+1; break;
                    case "5": month[5] = month[5]+1; break;
                    case "6": month[6] = month[6]+1; break;
                    case "7": month[7] = month[7]+1; break;
                    case "8": month[8] = month[8]+1; break;
                    case "9": month[9] = month[9]+1; break;
                    case "10": month[10] = month[10]+1; break;
                    case "11": month[11] = month[11]+1; break;
                }
            }
        }
        for(int i=0; i<month.length; i++){
            monthList.add(month[i]+"");
        }
//        monthList = new ArrayList<String>(Arrays.asList(month));
//        for(String z: monthList) Log.i("tag", "test: "+ z);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        ArrayAdapter<String> aa = (ArrayAdapter<String>)parent.getAdapter();
    }

    String pickYear(String date){
        return date.substring(0, 4);
    }
    String pickMonth(String date){
        int a=0; int b=0;
        a = date.indexOf("-");
        b = date.lastIndexOf("-");
        return date.substring(a+1,b);
    }

    String pickDay(String date){
        int a=0;
        a = date.lastIndexOf("-");
        return date.substring(a+1);
    }
}
