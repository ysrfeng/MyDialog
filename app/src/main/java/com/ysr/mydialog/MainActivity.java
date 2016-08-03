package com.ysr.mydialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    View getlistview;
    String[] mlistText = {"全选", "选择1", "选择2", "选择3", "选择4", "选择5", "选择6", "选择7"};
    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    AlertDialog.Builder builder;
    AlertDialog builder2;
    SimpleAdapter adapter;
    Boolean[] bl = {false, false, false, false, false, false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.btn);
        int lengh = mlistText.length;
        for (int i = 0; i < lengh; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("text", mlistText[i]);
            mData.add(item);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDialog();// 点击创建Dialog
            }
        });
    }

    class ItemOnClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
            CheckBox cBox = (CheckBox) view.findViewById(R.id.X_checkbox);
            if (cBox.isChecked()) {
                cBox.setChecked(false);
            } else {
                Log.i("TAG", "取消该选项");
                cBox.setChecked(true);
            }

            if (position == 0 && (cBox.isChecked())) {
                //如果是选中 全选  就把所有的都选上 然后更新
                for (int i = 0; i < bl.length; i++) {
                    bl[i] = true;
                }
                adapter.notifyDataSetChanged();
            } else if (position == 0 && (!cBox.isChecked())) {
                //如果是取消全选 就把所有的都取消 然后更新
                for (int i = 0; i < bl.length; i++) {
                    bl[i] = false;
                }
                adapter.notifyDataSetChanged();
            }
            if (position != 0 && (!cBox.isChecked())) {
                // 如果把其它的选项取消   把全选取消
                bl[0] = false;
                bl[position] = false;
                adapter.notifyDataSetChanged();
            } else if (position != 0 && (cBox.isChecked())) {
                //如果选择其它的选项，看是否全部选择
                //先把该选项选中 设置为true
                bl[position] = true;
                int a = 0;
                for (int i = 1; i < bl.length; i++) {
                    if (bl[i] == false) {
                        //如果有一个没选中  就不是全选 直接跳出循环
                        break;
                    } else {
                        //计算有多少个选中的
                        a++;
                        if (a == bl.length - 1) {
                            //如果选项都选中，就把全选 选中，然后更新
                            bl[0] = true;
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }

    }

    public void CreateDialog() {

        // 动态加载一个listview的布局文件进来
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        getlistview = inflater.inflate(R.layout.dialoglayout, null);

        // 给ListView绑定内容
        ListView listview = (ListView) getlistview.findViewById(R.id.X_listview);
        adapter = new SetSimpleAdapter(MainActivity.this, mData, R.layout.listitem, new String[]{"text"},
                new int[]{R.id.X_item_text});
        // 给listview加入适配器
        listview.setAdapter(adapter);
        listview.setItemsCanFocus(false);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listview.setOnItemClickListener(new ItemOnClick());

        builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择查询类型");
        builder.setIcon(R.mipmap.ic_launcher);
        //设置加载的listview
        builder.setView(getlistview);
        builder.setPositiveButton("确定", new DialogOnClick());
        builder.setNegativeButton("取消", new DialogOnClick());
        builder.create().show();
    }

    class DialogOnClick implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    //确定按钮的事件
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    //取消按钮的事件
                    break;
                default:
                    break;
            }
        }
    }

    //重写simpleadapterd的getview方法
    class SetSimpleAdapter extends SimpleAdapter {

        public SetSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from,
                                int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LinearLayout.inflate(getBaseContext(), R.layout.listitem, null);
            }
            CheckBox ckBox = (CheckBox) convertView.findViewById(R.id.X_checkbox);
            //每次都根据 bl[]来更新checkbox
            if (bl[position] == true) {
                ckBox.setChecked(true);
            } else if (bl[position] == false) {
                ckBox.setChecked(false);
            }
            return super.getView(position, convertView, parent);
        }
    }
}

