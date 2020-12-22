package com.example.youownme.Data;

import android.content.Context;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class DataBank {
    private ArrayList<OwnInfo> infoArrayList_get = new ArrayList<>();
    private ArrayList<ArrayList<OwnInfo>> child_list_get = new ArrayList<>();
    private ArrayList<String> group_list_get = new ArrayList<>();

    private ArrayList<OwnInfo> infoArrayList_give = new ArrayList<>();
    private ArrayList<ArrayList<OwnInfo>> child_list_give = new ArrayList<>();
    private ArrayList<String> group_list_give = new ArrayList<>();

    private Context context;
    private final String GET_INFO_FILE_NAME = "GetInfo.txt";
    private final String GIVE_INFO_FILE_NAME = "GiveInfo.txt";

    public DataBank(Context context){ this.context = context; }

    public ArrayList<OwnInfo> getInfoArrayList_get(){ return infoArrayList_get; }
    public ArrayList<OwnInfo> getInfoArrayList_give(){ return infoArrayList_give; }

    public void initDate_get(){

        child_list_get.clear();
        for(OwnInfo i:infoArrayList_get){
            if(!group_list_get.contains(i.getDate().substring(0, 7)))
                group_list_get.add(i.getDate().substring(0, 7));
        }

        Collections.sort(group_list_get);

        for(String i:group_list_get){
            ArrayList<OwnInfo> tempInfo = new ArrayList<>();
            for(OwnInfo j:infoArrayList_get){
                if(j.getDate().substring(0, 7).equals(i))
                    tempInfo.add(j);
            }
            child_list_get.add(tempInfo);
        }
        System.out.println("");
    }

    public void check_data_get(){
        for(int i=0; i<child_list_get.size(); i++) {
            if (child_list_get.get(i).size() == 0)
                group_list_get.remove(i);
        }
    }

    public void initDate_give(){

        child_list_give.clear();
        for(OwnInfo i:infoArrayList_give){
            if(!group_list_give.contains(i.getDate().substring(0, 7)))
                group_list_give.add(i.getDate().substring(0, 7));
        }

        Collections.sort(group_list_give);

        for(String i:group_list_give){
            ArrayList<OwnInfo> tempInfo = new ArrayList<>();
            for(OwnInfo j:infoArrayList_give){
                if(j.getDate().substring(0, 7).equals(i))
                    tempInfo.add(j);
            }
            child_list_give.add(tempInfo);
        }
    }

    public void check_data_give(){
        for(int i=0; i<child_list_give.size(); i++) {
            if (child_list_give.get(i).size() == 0)
                group_list_give.remove(i);
        }
    }

    public ArrayList<ArrayList<OwnInfo>> getChild_list_get() {
        return child_list_get;
    }
    public ArrayList<String> getGroup_list_get() {
        return group_list_get;
    }
    public ArrayList<ArrayList<OwnInfo>> getChild_list_give() {
        return child_list_give;
    }
    public ArrayList<String> getGroup_list_give() {
        return group_list_give;
    }

    public void saveGet(){
        ObjectOutputStream out;
        try{
            out = new ObjectOutputStream(context.openFileOutput(GET_INFO_FILE_NAME, Context.MODE_PRIVATE));
            out.writeObject(infoArrayList_get);
            out.writeObject(group_list_get);
            out.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void saveGive(){
        ObjectOutputStream out;
        try{
            out = new ObjectOutputStream(context.openFileOutput(GIVE_INFO_FILE_NAME, Context.MODE_PRIVATE));
            out.writeObject(infoArrayList_give);
            out.writeObject(group_list_give);
            out.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadGet(){
        try {
            ObjectInputStream in;
            in = new ObjectInputStream(context.openFileInput(GET_INFO_FILE_NAME));
            infoArrayList_get = (ArrayList<OwnInfo>) in.readObject();
            group_list_get = (ArrayList<String>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGive(){
        try {
            ObjectInputStream in;
            in = new ObjectInputStream(context.openFileInput(GIVE_INFO_FILE_NAME));
            infoArrayList_give = (ArrayList<OwnInfo>) in.readObject();
            group_list_give = (ArrayList<String>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}