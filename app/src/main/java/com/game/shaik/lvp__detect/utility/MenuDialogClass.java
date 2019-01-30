package com.game.shaik.lvp__detect.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.game.shaik.lvp__detect.MainActivity;
import com.game.shaik.lvp__detect.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by shaik on 31-01-2019.
 */

public class MenuDialogClass extends Dialog implements
        View.OnClickListener {

    public Activity c;

    Button resume,quit;

    private menuDialogListener listener;


    public MenuDialogClass(Activity a) {
        super(a);

        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu);

        resume=findViewById(R.id.resume);
        quit=findViewById(R.id.quit);

        try {
            listener = (menuDialogListener) c;
        } catch (ClassCastException e) {
            throw new ClassCastException(c.toString() +
                    "must implement menuDialogListener");
        }

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.resume();
                dismiss();
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.startActivity(new Intent(c, MainActivity.class));
            }
        });


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


    public interface menuDialogListener {
        void resume();
    }

}

