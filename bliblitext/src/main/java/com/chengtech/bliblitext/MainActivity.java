package com.chengtech.bliblitext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.chengtech.bliblitext.view.MyDiyView1;
import com.chengtech.bliblitext.view.MyDrawLine2;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button_left,button_right,button_top,button_bottom;
    private MyDiyView1 myDiyView1;
    private int offsetx = 100;
    private int offsety = 100;
    private MyDrawLine2 myDrawLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        button_left = (Button) findViewById(R.id.move_left);
        button_right = (Button) findViewById(R.id.move_right);
        button_top = (Button) findViewById(R.id.move_top);
        button_bottom = (Button) findViewById(R.id.move_bottom);

        button_left.setOnClickListener(this);
        button_right.setOnClickListener(this);
        button_top.setOnClickListener(this);
        button_bottom.setOnClickListener(this);

        //myDiyView1 = (MyDiyView1) findViewById(R.id.myView1);
        myDrawLine = (MyDrawLine2) findViewById(R.id.id_line);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.move_left:
//                myDiyView1.offsetLeftAndRight(-offsetx);
//                ((View)myDiyView1.getParent()).scrollBy(offsetx,0);
                //myDrawLine.addPath((int)(500*Math.random()),(int)(250*Math.random()));
                break;
            case R.id.move_right:
                myDiyView1.offsetLeftAndRight(offsetx);
//                ((View)myDiyView1.getParent()).scrollBy(-offsetx,0);
                break;
            case R.id.move_top:
                myDiyView1.offsetTopAndBottom(-offsety);
                break;
            case R.id.move_bottom:
                myDiyView1.offsetTopAndBottom(offsety);
                break;
        }
    }
}
