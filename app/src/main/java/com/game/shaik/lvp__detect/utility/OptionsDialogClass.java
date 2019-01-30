package com.game.shaik.lvp__detect.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.shaik.lvp__detect.MainActivity;
import com.game.shaik.lvp__detect.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by shaik on 29-01-2019.
 */

public class OptionsDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    private ExampleDialogListener listener;

    EditText minutes,sec;
    Button set;

    public OptionsDialogClass(Activity a) {
        super(a);

        this.c = a;
    }

    int min,second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option);

        minutes=findViewById(R.id.minutes);
        set=findViewById(R.id.settimer);
        sec=findViewById(R.id.sec);


        try {
            listener = (ExampleDialogListener) c;
        } catch (ClassCastException e) {
            throw new ClassCastException(c.toString() +
                    "must implement ExampleDialogListener");
        }

        try {
            set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(minutes.getText().toString().isEmpty())
                    {
                        minutes.setText("00");
                    }

                    if(sec.getText().toString().isEmpty())
                    {
                        sec.setText("00");
                    }

                    if (Integer.parseInt(minutes.getText().toString()) > 99) {
                        Toast.makeText(c, "Minutes limit is 99", Toast.LENGTH_SHORT).show();
                        minutes.setText("99");
                    }

                    if (Integer.parseInt(sec.getText().toString()) > 99) {
                        Toast.makeText(c, "Seconds limit is 60", Toast.LENGTH_SHORT).show();
                        minutes.setText("60");
                    }


                    SharedPreferences.Editor editor = c.getSharedPreferences("default", MODE_PRIVATE).edit();
                    min = Integer.parseInt(minutes.getText().toString());
                    second = Integer.parseInt(sec.getText().toString());
                    int timer =(min * 60000) + (second * 1000);

                    if(timer==0)
                    {timer=1000;}

                    editor.putInt("timer", timer);
                    editor.apply();
                    listener.putTimer(timer);


                   // Toast.makeText(c, ((min * 60000) + (second * 1000)) + "", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            });
        }catch ( Exception e){}


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                dismiss();
                break;


            default:
                dismiss();
                break;
        }
        dismiss();
    }


    public interface ExampleDialogListener {
        void putTimer(int timer);
    }


}


