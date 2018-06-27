package apps.harrislim.donelist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import apps.harrislim.donelist.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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
    ArrayList<Integer> viewIdList = new ArrayList<Integer>();
    int viewId = 0;
    boolean hasFinger = false;
    boolean checkDelete = false;
    int textViewId;
    View innerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r = getResources();

        plus = (ImageButton)findViewById(R.id.plus);
        setting = (ImageButton)findViewById(R.id.setting);

        plus.setOnClickListener(this);
        setting.setOnClickListener(this);
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
                String textViewIdStr = view.toString();
                int lastIdx = textViewIdStr.lastIndexOf("}");
                innerView = view;
                textViewId = Integer.parseInt(textViewIdStr.substring(textViewIdStr.length()-2, lastIdx));
                if(hasFinger) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("알림");
                    builder.setMessage("정말 삭제하시겠습니까?");
                    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == DialogInterface.BUTTON_POSITIVE){
                                Toast.makeText(MainActivity.this, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                topLL.removeView(innerView);
                                viewIdList.set(textViewId, -1);
                                checkDelete=true;
                            }else{
                                return;
                            }
                        }
                    };
                    builder.setPositiveButton("예", listener);
                    builder.setNegativeButton("아니오", listener);
                    builder.show();
                }
                else startActivity(inte);
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View view) {
                String title = addList.getText().toString().trim();
                if(title.length()>10) {
                    Toast.makeText(MainActivity.this, "10글자를 초과할 수 없습니다.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "\""+title+"\" 추가", Toast.LENGTH_SHORT).show();
                    viewIdList.add(viewId);
                    topTV1.setId(viewIdList.get(viewId)); // id는 0부터 시작
                    viewId++;
                    topTV1.setText(title);
                    topTV1.setTextSize(40);
                    topTV1.setPadding(0, 32, 0, 0);
                    topTV1.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.envelope));
                    android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(android.support.v7.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT, 150);

                    lp.setMargins(20, 10, 20, 10);
                    topTV1.setLayoutParams(lp);
                    topTV1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    if(emptyView!=null){
                        topLL.removeView(emptyView);
                     }
                    topLL.addView(topTV1);
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
    public void setting(){
        if(hasFinger){ // setting 비활성화
             for(int i=0; i<viewIdList.size(); i++){
                 if(viewIdList.get(i)!=-1) {
                     android.widget.TextView tempTV = (android.widget.TextView) findViewById(i);
                     tempTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                     tempTV.setPadding(0, 42, 0, 0);
                 }
            }
        }else{ // setting 활성화
             for(int i=0; i<viewIdList.size(); i++){
                 if(viewIdList.get(i)!=-1) {
                     android.widget.TextView tempTV = (android.widget.TextView) findViewById(i);
                     tempTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.trash_bin, 0, R.drawable.finger, 0);
                     tempTV.setPadding(10, 42, 20, 42);
                 }
            }
        }
        settingToggle();
    }
    void settingToggle(){
        if(hasFinger) hasFinger=false;
        else hasFinger=true;
    }
}
