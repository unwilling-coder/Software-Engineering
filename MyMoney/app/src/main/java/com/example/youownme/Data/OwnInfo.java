package com.example.youownme.Data;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Objects;

public class OwnInfo implements Serializable {
    private String name;
    private int money;
    private String date;
    private int reason_pos;

    public void setMoney(int money) {
        this.money = money;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDate(String date) { this.date = date; }
    public void setReason_pos(int reason_pos) { this.reason_pos = reason_pos; }

    public OwnInfo(String name, int money, String date, int reason_pos){
        this.name = name;
        this.money = money;
        this.date = date;
        this.reason_pos = reason_pos;
    }

    public String getName() {
        return name;
    }
    public int getMoney() {
        return money;
    }
    public String getDate() { return date; }
    public int getReason_pos(){ return reason_pos; }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OwnInfo)) return false;
        OwnInfo ownInfo = (OwnInfo) o;
        return getMoney() == ownInfo.getMoney() &&
                getReason_pos() == ownInfo.getReason_pos() &&
                Objects.equals(getName(), ownInfo.getName()) &&
                Objects.equals(getDate(), ownInfo.getDate());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getMoney(), getDate(), getReason_pos());
    }
}