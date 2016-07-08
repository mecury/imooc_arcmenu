package com.mecury.imooc_arcmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private ArcMenu mArcmenu;
    private List<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();

        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDatas));
        
        initEvent();
    }

    private void initEvent() {

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (mArcmenu.isOpen()){
                    mArcmenu.toggleMenu(600);
                }
            }
        });

        mArcmenu.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(MainActivity.this, position+":" + view.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {

        mListView = (ListView) findViewById(R.id.id_listView);
        mArcmenu = (ArcMenu) findViewById(R.id.id_menu_right);
    }

    private void initData() {

        mDatas = new ArrayList<>();

        for (int i = 'A';i<'Z';i++){
            mDatas.add((char)i +"");
        }

    }
}
