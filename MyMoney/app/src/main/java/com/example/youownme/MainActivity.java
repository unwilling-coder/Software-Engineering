package com.example.youownme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.youownme.Data.DataBank;
import com.example.youownme.Data.OwnInfo;
import com.example.youownme.Fragment.CalendarFragment;
import com.example.youownme.Fragment.GetFragment;
import com.example.youownme.Fragment.GiveFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int CONTEXT_MENU_ITEM_GIVE = 1;
    private static final int CONTEXT_MENU_ITEM_GET = CONTEXT_MENU_ITEM_GIVE + 1;
    private static final int CONTEXT_MENU_ITEM_CANCEL = CONTEXT_MENU_ITEM_GET + 1;
    private static final int REQUEST_CODE_GIVE = 100;
    private static final int REQUEST_CODE_GET = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        initData();

        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(new GetFragment());
        fragments.add(new CalendarFragment());
        fragments.add(new GiveFragment());
        myFragmentAdapter.setData(fragments);

        ArrayList<String> titles = new ArrayList<>();
        titles.add("收入");
        titles.add("主页");
        titles.add("支出");
        myFragmentAdapter.setTitle(titles);

        TabLayout mTableLayout = findViewById(R.id.tab_layout);
        ViewPager mViewPager = findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(myFragmentAdapter);
        mViewPager.setCurrentItem(1);
        mTableLayout.setupWithViewPager(mViewPager);

        Button btn_update = findViewById(R.id.update_button);
        registerForContextMenu(btn_update);
        btn_update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.showContextMenu();
            }
        });
    }

    public static class MyFragmentAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> data;
        private ArrayList<String> title;

        public void setData(ArrayList<Fragment> data) {
            this.data = data;
        }
        public void setTitle(ArrayList<String> title) {
            this.title = title;
        }
        public MyFragmentAdapter(FragmentManager fm) { super(fm); }

        @NonNull
        public Fragment getItem(int position) {
            return data==null? null : data.get(position);
        }
        public int getCount() {
            return data == null? 0 : data.size();
        }
        public CharSequence getPageTitle(int position){
            return title == null ? null : title.get(position);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v==this.findViewById(R.id.update_button)){
            menu.setHeaderTitle("操作");
            menu.add(1, CONTEXT_MENU_ITEM_GET, 1, "收入");
            menu.add(1, CONTEXT_MENU_ITEM_GIVE, 1, "支出");
            menu.add(1, CONTEXT_MENU_ITEM_CANCEL, 1, "取消");
        }
    }

    public void initData(){
        GetFragment.dataBank_get = new DataBank(this);
        GiveFragment.dataBank_give = new DataBank(this);

        GetFragment.dataBank_get.loadGet();
        GetFragment.dataBank_get.initDate_get();
        GiveFragment.dataBank_give.loadGive();
        GiveFragment.dataBank_give.initDate_give();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        ExpandableListView.ExpandableListContextMenuInfo menuInfo = (ExpandableListView.ExpandableListContextMenuInfo)item.getMenuInfo();

        Intent intent;
        if(item.getGroupId() != 1)
            return false;
        switch (item.getItemId()){
            case CONTEXT_MENU_ITEM_GIVE:
                intent = new Intent(MainActivity.this, UpdateActivity.class);
                startActivityForResult(intent, REQUEST_CODE_GIVE);
                break;
            case CONTEXT_MENU_ITEM_GET:
                intent = new Intent(MainActivity.this, UpdateActivity.class);
                startActivityForResult(intent, REQUEST_CODE_GET);
                break;
            case CONTEXT_MENU_ITEM_CANCEL:
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        switch (requestCode){
            case REQUEST_CODE_GIVE:
                if(resultCode == RESULT_OK){
                    String infoName = intent.getStringExtra("infoName");
                    int infoMoney = intent.getIntExtra("infoMoney", 0);
                    String infoDate = intent.getStringExtra("infoDate");
                    int infoReason = intent.getIntExtra("infoReason", 0);
                    GiveFragment.dataBank_give.getInfoArrayList_give().add(new OwnInfo(infoName, infoMoney, infoDate, infoReason));
                    GiveFragment.dataBank_give.initDate_give();
                    GiveFragment.dataBank_give.saveGive();
                    GiveFragment.infoAdapter_give_show.notifyDataSetChanged();
                    CalendarFragment.updateDate(infoDate);
                    CalendarFragment.showAdapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_GET:
                if(resultCode == RESULT_OK){
                    String infoName = intent.getStringExtra("infoName");
                    int infoMoney = intent.getIntExtra("infoMoney", 0);
                    String infoDate = intent.getStringExtra("infoDate");
                    int infoReason = intent.getIntExtra("infoReason", 0);
                    GetFragment.dataBank_get.getInfoArrayList_get().add(new OwnInfo(infoName, infoMoney, infoDate, infoReason));
                    GetFragment.dataBank_get.initDate_get();
                    GetFragment.dataBank_get.saveGet();
                    GetFragment.infoAdapter_get_show.notifyDataSetChanged();
                    CalendarFragment.updateDate(infoDate);
                    CalendarFragment.showAdapter.notifyDataSetChanged();
                }
                break;
            default:
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}