package apps.harrislim.donelist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.*;

import apps.harrislim.donelist.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageButton plus, setting;
    Resources r;
    Button okBtn, cancelBtn;
    TextView emptyView;
    EditText addList;
    android.widget.LinearLayout topLL;
    AlertDialog.Builder builder;
    AlertDialog alert;
    android.widget.TextView topTV1;
    Intent inte;
    ListView lv;
    ArrayList<Integer> viewIdList = new ArrayList<Integer>();
    ArrayList<String> items = new ArrayList<String>();

    boolean deletable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r = getResources();

        // listview 관련
        lv = (ListView)findViewById(R.id.listview);
        lv.addFooterView(new View(this), null, true);

        plus = (ImageButton)findViewById(R.id.plus);
        setting = (ImageButton)findViewById(R.id.setting);

        plus.setOnClickListener(this);
        setting.setOnClickListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        ArrayAdapter<String> aa = (ArrayAdapter<String>)parent.getAdapter();
    }
    ArrayAdapter<String> aa;
    List list;
    int positionIdx;
    void m2(){
        list = new Vector<String>();
        for(String item: items) {
            list.add(item);
        }
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(aa);

        // textSize 변경하기 위한 코드

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                positionIdx = position;
                if(deletable){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("알림");
                    builder.setMessage("정말 삭제하시겠습니까?");
                    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                String tempTitle = list.get(positionIdx).toString();
                                Log.i("tag", "te: "+positionIdx);
                                items.remove(positionIdx);
                                list.remove(positionIdx);
                                lv.setAdapter(aa);
                                Toast.makeText(MainActivity.this,
                                        tempTitle + "이(가) 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                return;
                            }
                        }
                    };
                    builder.setPositiveButton("예", listener);
                    builder.setNegativeButton("아니오", listener);
                    builder.show();
                }else {
                    // 여기 달력 들어가는 곳.
//                    Toast.makeText(MainActivity.this,
//                            "선택(list) : " + aa.getItem(position), Toast.LENGTH_SHORT).show();
                    inte = new Intent(MainActivity.this, CalendarActivity.class);
                    inte.putExtra("title", aa.getItem(position));
                    startActivity(inte);
                }
            }
        });
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.plus: plus(); break;
            case R.id.setting: setting(); break;
        }
    }
    public void plus(){

        builder = new AlertDialog.Builder(this);
        builder.setTitle("목록 추가");
        View view = View.inflate(this, R.layout.custom, null);
        builder.setView(view);
        alert = builder.create();

        okBtn = (Button)view.findViewById(R.id.okBtn);
        cancelBtn = (Button)view.findViewById(R.id.cancelBtn);
        emptyView = (TextView)findViewById(R.id.tempView);
        addList = (EditText)view.findViewById(R.id.addListText);
        topLL = (android.widget.LinearLayout)findViewById(R.id.dynamicArea);
        topTV1 = new android.widget.TextView(MainActivity.this);
        inte = new Intent(MainActivity.this, CalendarActivity.class);

        topTV1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                  startActivity(inte);
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View view) {
                String title = addList.getText().toString().trim();
                if(title.length()>20) {
                    Toast.makeText(MainActivity.this, "20글자를 초과할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else if(hasDuplicate(title)){ // list 중복검사
                    Toast.makeText(MainActivity.this, "중복되는 목록이 있습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "\""+title+"\" 추가", Toast.LENGTH_SHORT).show();
                    if(emptyView!=null){
                        topLL.removeView(emptyView);
                     }
                    items.add(title);
                    m2();
                    alert.dismiss();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }
    public boolean hasDuplicate(String name){
        for(int i=0; i<items.size(); i++){
            if(items.get(i).equals(name))
                return true;
        }
        return false;
    }
    public void setting(){
        if(deletable){ // 지우기 멈춤
            Toast.makeText(this, "삭제 비활성화", Toast.LENGTH_SHORT).show();
            setting.setImageResource(R.drawable.setting);
        }else{ // 지우기 시작
            Toast.makeText(this, "삭제 활성화", Toast.LENGTH_SHORT).show();
            setting.setImageResource(R.drawable.unsetting);
        }
        settingToggle();
    }
    void settingToggle(){
        if(deletable) deletable=false;
        else deletable=true;
    }
}

