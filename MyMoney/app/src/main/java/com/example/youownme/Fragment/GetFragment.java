package com.example.youownme.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.youownme.Data.DataBank;
import com.example.youownme.Data.OwnInfo;
import com.example.youownme.R;
import com.example.youownme.UpdateActivity;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class GetFragment extends Fragment {
    private static final int CONTEXT_MENU_ITEM_UPDATE = 4;
    private static final int CONTEXT_MENU_ITEM_DELETE = CONTEXT_MENU_ITEM_UPDATE + 1;
    private static final int REQUEST_CODE_UPDATE = 100;

    @SuppressLint("StaticFieldLeak")
    public static DataBank dataBank_get;
    public static CalendarFragment.ShowAdapter infoAdapter_get_show;

    public GetFragment(){}

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get, container, false);

        initView(view);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initView(View view){

        ExpandableListView expandableListView = view.findViewById(R.id.get_list);
        expandableListView.setDivider(null);
        infoAdapter_get_show = new CalendarFragment.ShowAdapter(dataBank_get.getGroup_list_get(), dataBank_get.getChild_list_get());
        expandableListView.setAdapter(infoAdapter_get_show);
        this.registerForContextMenu(expandableListView);
    }

    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        ExpandableListView.ExpandableListContextMenuInfo info =(ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
        int type = ExpandableListView
                .getPackedPositionType(info.packedPosition);

        if(type == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
            menu.setHeaderTitle("操作");
            menu.add(2, CONTEXT_MENU_ITEM_UPDATE, 2, "更改");
            menu.add(2, CONTEXT_MENU_ITEM_DELETE, 2, "删除");
        }
    }

    public boolean onContextItemSelected(@NonNull MenuItem item) {
        ExpandableListView.ExpandableListContextMenuInfo menuInfo = (ExpandableListView.ExpandableListContextMenuInfo)item.getMenuInfo();
        Intent intent;
        if(item.getGroupId() != 2)
            return false;
        long packedPosition = menuInfo.packedPosition;
        final int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
        final int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
        switch (item.getItemId()){
            case CONTEXT_MENU_ITEM_UPDATE:
                intent = new Intent(this.getContext(), UpdateActivity.class);
                intent.putExtra("groupPosition", groupPosition);
                intent.putExtra("childPosition", childPosition);
                intent.putExtra("infoName", dataBank_get.getChild_list_get().get(groupPosition).get(childPosition).getName());
                intent.putExtra("infoMoney", dataBank_get.getChild_list_get().get(groupPosition).get(childPosition).getMoney());
                intent.putExtra("infoDate", dataBank_get.getChild_list_get().get(groupPosition).get(childPosition).getDate());
                intent.putExtra("infoReason", dataBank_get.getChild_list_get().get(groupPosition).get(childPosition).getReason_pos());
                startActivityForResult(intent, REQUEST_CODE_UPDATE);
                break;
            case CONTEXT_MENU_ITEM_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setTitle("询问");
                builder.setMessage("你确定要删除\"" + dataBank_get.getChild_list_get().get(groupPosition).get(childPosition).getName() + "\"吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<OwnInfo> temp = new ArrayList<>();
                        for(OwnInfo i:dataBank_get.getInfoArrayList_get()) {
                            if (i.equals(dataBank_get.getChild_list_get().get(groupPosition).get(childPosition))){
                                temp.add(i);
                            }
                        }
                        dataBank_get.getInfoArrayList_get().removeAll(temp);
                        dataBank_get.getChild_list_get().get(groupPosition).removeAll(temp);
                        dataBank_get.check_data_get();
                        dataBank_get.initDate_get();
                        dataBank_get.saveGet();
                        infoAdapter_get_show.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
                builder.create().show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        switch (requestCode){
            case REQUEST_CODE_UPDATE:
                if(resultCode == RESULT_OK){
                    int groupPosition = intent.getIntExtra("groupPosition", 0);
                    int childPosition = intent.getIntExtra("childPosition", 0);
                    String infoName = intent.getStringExtra("infoName");
                    int infoMoney = intent.getIntExtra("infoMoney", 0);
                    String infoDate = intent.getStringExtra("infoDate");
                    int infoReason = intent.getIntExtra("infoReason", 0);

                    for(OwnInfo i:dataBank_get.getInfoArrayList_get()){
                        if(i.equals( GetFragment.dataBank_get.getChild_list_get().get(groupPosition).get(childPosition))){
                            i.setName(infoName);
                            i.setDate(infoDate);
                            i.setMoney(infoMoney);
                            i.setReason_pos(infoReason);
                        }
                    }
                    GetFragment.dataBank_get.initDate_get();
                    GetFragment.dataBank_get.check_data_get();
                    GetFragment.dataBank_get.saveGet();
                    GetFragment.infoAdapter_get_show.notifyDataSetChanged();
                }
                break;
            case CONTEXT_MENU_ITEM_DELETE:
                break;
            default:
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}