package apps.harrislim.donelist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
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
import java.util.concurrent.Executors;

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
    SharedPreferences listPre;
    SharedPreferences dataPre;
    SharedPreferences.Editor editor;
    String title;

    boolean deletable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("tag", "onCreate in Main");
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

        listPre = getSharedPreferences("list", 0);
        editor= listPre.edit();

        emptyView = (TextView)findViewById(R.id.tempView);
        topLL = (android.widget.LinearLayout)findViewById(R.id.dynamicArea);
        makeTempView();
    }
    void makeTempView(){
        Log.i("tag", "size: "+ listPre.getAll().size());
        if(listPre.getAll().size() > 0){
            topLL.removeView(emptyView);
            m2();
        }
    }

    @Override
    public void onResume(){
        Log.i("tag", "onResume in Main");
        super.onResume();
    }

    @Override
    public void onRestart(){
        Log.i("tag", "onRestart in Main");
        super.onRestart();
    }

    @Override
    public void onStart(){
        Log.i("tag", "onStart in Main");
        super.onStart();
    }

    @Override
    public void onPause(){
        Log.i("tag", "onPause in Main");
        super.onPause();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        ArrayAdapter<String> aa = (ArrayAdapter<String>)parent.getAdapter();
    }
    ArrayAdapter<String> aa;
    int positionIdx;
    void m2(){
            items.removeAll(items);
        Iterator<String> keys = listPre.getAll().keySet().iterator();
        Log.i("tag", "listPre.size(): "+listPre.getAll().size());
        while(keys.hasNext()){
            String key = keys.next();
            items.add(key);
        }
        Log.i("tag", "items.size(): "+items.size());
        Collections.sort(items);
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);
        lv.setAdapter(aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                positionIdx = position;
                if(deletable){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.DeleteTitle);
                    builder.setMessage(R.string.noticeInDelete);
                    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                String tempTitle = items.get(positionIdx);
                                Log.i("tag", "te: "+positionIdx);
                                Log.i("tag", "tempTitle: "+tempTitle);
                                Log.i("tag", "items.size(): "+items.size());
                                items.remove(positionIdx);
                                editor= listPre.edit();
                                editor.remove(tempTitle);
                                editor.commit();
                                dataPre = getSharedPreferences(tempTitle, 0);
                                editor = dataPre.edit();
                                editor.clear();
                                editor.commit();

                                editor= listPre.edit();

                                Iterator<String> keys = listPre.getAll().keySet().iterator();
                                for(String z: items) Log.i("tag", "items: "+z );
                                while(keys.hasNext()){
                                    String key = keys.next();
                                    Log.i("tag", "key: "+key);
                                }
                                lv.setAdapter(aa);
                                Resources res = getResources();
                                String text = String.format(res.getString(R.string.confirmDeleting), tempTitle);
                                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                            } else {
                                return;
                            }
                        }
                    };

                    builder.setPositiveButton(R.string.yes, listener);
                    builder.setNegativeButton(R.string.no, listener);
                    builder.show();
                }else {
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
        builder.setTitle(R.string.addListTitle);
        View view = View.inflate(this, R.layout.custom, null);
        builder.setView(view);
        alert = builder.create();

        okBtn = (Button)view.findViewById(R.id.okBtn);
        cancelBtn = (Button)view.findViewById(R.id.cancelBtn);
        addList = (EditText)view.findViewById(R.id.addListText);
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
                title = addList.getText().toString().trim();
                if(title.length()>15) {
                    Toast.makeText(MainActivity.this, R.string.limitString, Toast.LENGTH_SHORT).show();
                } else if(hasDuplicate(title)){ // list 중복검사
                    Toast.makeText(MainActivity.this, R.string.unduplicatable, Toast.LENGTH_SHORT).show();
                }else {
                    Resources res = getResources();
                    String text = String.format(res.getString(R.string.add), title);
                    Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                    if(emptyView!=null){
                        topLL.removeView(emptyView);
                     }
                    editor.putString(title, title);
                    editor.commit();
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
            Toast.makeText(this, R.string.undeletable, Toast.LENGTH_SHORT).show();
            setting.setImageResource(R.drawable.setting);
        }else{ // 지우기 시작
            Toast.makeText(this, R.string.deletable, Toast.LENGTH_SHORT).show();
            setting.setImageResource(R.drawable.unsetting);
        }
        settingToggle();
    }
    void settingToggle(){
        if(deletable) deletable=false;
        else deletable=true;
    }
}

