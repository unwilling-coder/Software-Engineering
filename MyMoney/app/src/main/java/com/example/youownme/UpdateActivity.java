package com.example.youownme;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youownme.Fragment.CalendarFragment;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {
    private EditText editTextInfoName;
    private EditText editTextInfoMoney;
    private EditText editTextInfoDate;
    private static int reason_pos = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);

        final int groupPosition = getIntent().getIntExtra("groupPosition", 0);
        final int childPosition = getIntent().getIntExtra("childPosition", 0);
        String infoName = getIntent().getStringExtra("infoName");
        int infoMoney = getIntent().getIntExtra("infoMoney", 0);
        String infoDate = getIntent().getStringExtra("infoDate");
        if(infoDate == null)
            infoDate = CalendarFragment.date;

        editTextInfoName = UpdateActivity.this.findViewById(R.id.edit_add);
        if(editTextInfoName != null)
            editTextInfoName.setText(infoName);
        editTextInfoMoney = UpdateActivity.this.findViewById(R.id.edit_money);
        if(editTextInfoMoney != null)
            editTextInfoMoney.setText(infoMoney + "");
        editTextInfoDate = UpdateActivity.this.findViewById(R.id.edit_date);
        if(editTextInfoDate != null)
                editTextInfoDate.setText(infoDate);

        Spinner spinner = UpdateActivity.this.findViewById(R.id.spin_reason);

        assert editTextInfoDate != null;
        editTextInfoDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    showDatePickDlg();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reason_pos = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        Button btn = findViewById(R.id.save_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                String money = editTextInfoMoney.getText().toString();

                while(money.charAt(0) == '0' && money.length()>1)
                    money = money.substring(1);

                if(editTextInfoDate.getText().toString().isEmpty())
                    Toast.makeText(UpdateActivity.this, "数据不完整", Toast.LENGTH_SHORT).show();
                else if(editTextInfoMoney.getText().toString().length()>11)
                    Toast.makeText(UpdateActivity.this, "金额过大", Toast.LENGTH_SHORT).show();
                else {
                    boolean flag = true;

                    if(editTextInfoMoney.getText().toString().length()==10){
                        String int_max = String.valueOf(Integer.MAX_VALUE);
                        for(int i=0; i<money.length(); i++){
                            if(money.charAt(i)-int_max.charAt(i) > 0)
                                flag = false;
                        }
                    }

                    if(flag){
                        intent.putExtra("groupPosition", groupPosition);
                        intent.putExtra("childPosition", childPosition);
                        intent.putExtra("infoName", editTextInfoName.getText().toString());
                        intent.putExtra("infoMoney", Integer.parseInt(editTextInfoMoney.getText().toString()));
                        intent.putExtra("infoDate", editTextInfoDate.getText().toString());
                        intent.putExtra("infoReason", reason_pos);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else
                        Toast.makeText(UpdateActivity.this, "金额过大", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                if(monthOfYear < 10)
                    UpdateActivity.this.editTextInfoDate.setText(year + "-0" + monthOfYear + "-" + dayOfMonth);
                else
                    UpdateActivity.this.editTextInfoDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}