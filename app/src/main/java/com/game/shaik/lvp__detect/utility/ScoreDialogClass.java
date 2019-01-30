package com.game.shaik.lvp__detect.utility;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.shaik.lvp__detect.R;

import org.w3c.dom.Text;

/**
 * Created by shaik on 27-01-2019.
 */
public class ScoreDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;

    int highscore,thisscore;
    TextView thist,best;
    ImageView cancel;



    public ScoreDialogClass(Activity a, int highscore, int thisscore) {
        super(a);
        this.highscore=highscore;
        this.thisscore=thisscore;
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.high_score);

        cancel = findViewById(R.id.close);

        cancel.setOnClickListener(this);

        thist= findViewById(R.id.thist);
        best=findViewById(R.id.best);

        thist.setText(""+thisscore);
        best.setText(""+highscore);


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


}
