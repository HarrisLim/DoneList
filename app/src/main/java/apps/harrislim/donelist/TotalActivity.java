package apps.harrislim.donelist;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TotalActivity extends AppCompatActivity {
    TextView totalTitle;
    ListView totalListview;
    String title;
    Intent i_self;
    ArrayList<String> dayList = new ArrayList<String>();
    HashMap<Integer, String> labels;
    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);

        i_self = getIntent();
        title = i_self.getStringExtra("title");
        dayList = i_self.getStringArrayListExtra("dayList");
        totalTitle = (TextView) findViewById(R.id.totalTitle);
        totalListview = (ListView) findViewById(R.id.totalListview);
        totalTitle.setText(title);
        for(int i=0; i<dayList.size(); i++){
            Log.i("tag", "haha: "+dayList.get(i));
        }

        lineChart = (LineChart)findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<>();
//        for(int i=1; i<13; i++){ // 여기 스위치로 맞는 달이 있다면 넣자.
//            entries.add(new Entry(i, 31));
//        }

        // 이거 임시야. 위에 달 별로 데이터 가져와서 이거 위에 for문으로 바꿔.
        entries.add(new Entry(1, 4));
        entries.add(new Entry(2, 1));
        entries.add(new Entry(3, 7));
        entries.add(new Entry(4, 24));
        entries.add(new Entry(5, 5));
        entries.add(new Entry(6, 17));
        entries.add(new Entry(7, 20));
        entries.add(new Entry(8, 9));
        entries.add(new Entry(9, 0));
        entries.add(new Entry(10, 4));
        entries.add(new Entry(11, 16));
        entries.add(new Entry(12, 27));

        labels = new HashMap<Integer, String>();
        labels.put(1, "Jan.");
        labels.put(2, "Feb.");
        labels.put(3, "Mar.");
        labels.put(4, "Apr.");
        labels.put(5, "May");
        labels.put(6, "Jun.");
        labels.put(7, "Jul.");
        labels.put(8, "Aug.");
        labels.put(9, "Sep.");
        labels.put(10, "Oct.");
        labels.put(11, "Nov.");
        labels.put(12, "Dec.");

        LineDataSet lineDataSet = new LineDataSet(entries, "성공한 횟수");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleColorHole(Color.RED);
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(true);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setLabelCount(10);
        xAxis.enableGridDashedLine(8, 30, 0);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int)value);
            }
        });

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setAxisMinimum(0f); // start at zero
        yLAxis.setLabelCount(7);
        yLAxis.setAxisMaximum(35f);

        Description description = new Description();
        description.setText("");

        lineChart.getAxisRight().setEnabled(false); // 오른쪽 축 사용 x
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();
        lineChart.getViewPortHandler().setMaximumScaleX(1f); // 확대 방지
        lineChart.getViewPortHandler().setMaximumScaleY(1f); // 확대 방지
    }
}
