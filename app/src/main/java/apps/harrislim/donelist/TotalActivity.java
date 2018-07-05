package apps.harrislim.donelist;

import android.content.Intent;
import android.content.res.Resources;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TotalActivity extends AppCompatActivity {
    TextView totalTitle;
    String title;
    Intent i_self;
    ArrayList<String> monthList = new ArrayList<String>();
    HashMap<Integer, String> labels;
    LineChart lineChart;
    int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);

        i_self = getIntent();
        title = i_self.getStringExtra("title");
        monthList = i_self.getStringArrayListExtra("monthList");
        totalTitle = (TextView) findViewById(R.id.totalTitle);
        totalTitle.setText(title);

        lineChart = (LineChart)findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<>();
        for(int i=0; i<12; i++){ // 여기 스위치로 맞는 달이 있다면 넣자.
            entries.add(new Entry(i+1, Integer.parseInt(monthList.get(i))));
        }

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

        Resources res = getResources();
        String text = String.format(res.getString(R.string.doneCount), "");
        LineDataSet lineDataSet = new LineDataSet(entries, text);
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

        ArrayList<String> tempList = monthList;
        Collections.sort(tempList);
        max = Integer.parseInt(tempList.get(tempList.size()-1));

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setAxisMinimum(0f); // start at zero
        yLAxis.setLabelCount(7);
        yLAxis.setAxisMaximum(33f);

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
