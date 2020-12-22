package com.example.youownme.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CalendarView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.youownme.Data.OwnInfo;
import com.example.youownme.MainActivity;
import com.example.youownme.R;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarFragment extends Fragment {
    public static ArrayList<ArrayList<OwnInfo>> show_list = new ArrayList<>();
    private ArrayList<String> group_list = new ArrayList<>();
    public static ShowAdapter showAdapter;
    public static String date;

    public CalendarFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        CalendarView calendarView;
        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
                //显示用户选择的日期
                month++;
                if(month < 10)
                    date = year + "-0" + month + "-" + dayOfMonth;
                else
                    date = year + "-" + month + "-" + dayOfMonth;
                updateDate(date);
                showAdapter.notifyDataSetChanged();
            }
        });

        initDate();
        initView(view);

        return view;
    }

    public void initDate(){

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        date =year + "-" +
                (month<10 ? ("0" + month) : month) + "-" + day;

        group_list.add("收入");
        group_list.add("支出");

        updateDate(date);
    }

    public static void updateDate(String date){
        show_list.clear();
        ArrayList<OwnInfo> get_list = new ArrayList<>();
        for(OwnInfo i:GetFragment.dataBank_get.getInfoArrayList_get()){
            if(i.getDate().equals(date))
                get_list.add(i);
        }
        ArrayList<OwnInfo> give_list = new ArrayList<>();
        for(OwnInfo i:GiveFragment.dataBank_give.getInfoArrayList_give()){
            if(i.getDate().equals(date))
                give_list.add(i);
        }
        show_list.add(get_list);
        show_list.add(give_list);
    }

    public void initView(View view){

        ExpandableListView expandableListView = view.findViewById(R.id.show_list);
        expandableListView.setDivider(null);
        showAdapter = new ShowAdapter(group_list, show_list);
        expandableListView.setAdapter(showAdapter);

    }

    public static class ShowAdapter extends BaseExpandableListAdapter{
        ArrayList<String> group_list;
        ArrayList<ArrayList<OwnInfo>> child_list;
        String[] reasons = {"吃饭", "购物", "出行","随礼", "工资", "贩卖", "收礼" , "其他"};

        public ShowAdapter(ArrayList<String> group_list, ArrayList<ArrayList<OwnInfo>> child_list){
            this.group_list = group_list;
            this.child_list = child_list;
        }

        @Override
        public int getGroupCount() {
            return group_list==null ? 0 : group_list.size();
        }
        @Override
        public int getChildrenCount(int groupPosition) {
            return child_list==null ? 0 : child_list.get(groupPosition).size();
        }
        @Override
        public Object getGroup(int groupPosition) {
            return group_list.get(groupPosition);
        }
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return child_list.get(childPosition);
        }
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return groupPosition + childPosition;
        }
        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            GroupViewHolder groupViewHolder;
            if (convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show,parent,false);
                groupViewHolder = new GroupViewHolder();
                groupViewHolder.tvTitle = convertView.findViewById(R.id.item_show);
                convertView.setTag(groupViewHolder);
            }else {
                groupViewHolder = (GroupViewHolder)convertView.getTag();
            }
            groupViewHolder.tvTitle.setText(group_list.get(groupPosition));
            return convertView;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            ChildViewHolder childViewHolder;
            if (convertView==null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info,parent,false);
                childViewHolder = new ChildViewHolder();
                childViewHolder.tvName = convertView.findViewById(R.id.info_add);
                childViewHolder.tvDate = convertView.findViewById(R.id.info_date);
                childViewHolder.tvMoney = convertView.findViewById(R.id.info_money);
                childViewHolder.tvReason = convertView.findViewById(R.id.info_reason);
                convertView.setTag(childViewHolder);
            }else {
                childViewHolder = (ChildViewHolder) convertView.getTag();
            }
            childViewHolder.tvName.setText(child_list.get(groupPosition).get(childPosition).getName());
            childViewHolder.tvDate.setText(child_list.get(groupPosition).get(childPosition).getDate());
            childViewHolder.tvMoney.setText(child_list.get(groupPosition).get(childPosition).getMoney() + "");
            childViewHolder.tvReason.setText(reasons[child_list.get(groupPosition).get(childPosition).getReason_pos()]);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public static class GroupViewHolder {
        public TextView tvTitle;
    }

    public static class ChildViewHolder {
        public TextView tvName;
        public TextView tvDate;
        public TextView tvMoney;
        public TextView tvReason;
    }

}