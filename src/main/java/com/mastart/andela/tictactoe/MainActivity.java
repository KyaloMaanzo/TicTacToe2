package com.mastart.andela.tictactoe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private Context context;
    private RelativeLayout rl_welcome, rlimg_x_s, rlimg_o_s;
    private ImageView img_play, img_quit;
    private TextView txt3by3, txt5by5, txthuman, txtcomp, btndone;
    private EditText txtplayername;

    private LinearLayout ll_options;
    private String sideselected, boardtype, playvs="comp", player2name;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = MainActivity.this;
        setContentView(R.layout.main);

        rl_welcome = findViewById(R.id.rl_welcome);
        ll_options = findViewById(R.id.ll_options);

        img_play = findViewById(R.id.img_play);
        img_quit = findViewById(R.id.img_quit);
        rlimg_x_s = findViewById(R.id.rlimg_x_s);
        rlimg_o_s = findViewById(R.id.rlimg_o_s);

        txt3by3 = findViewById(R.id.txt3by3);
        txt5by5 = findViewById(R.id.txt5by5);
        txthuman = findViewById(R.id.txthuman);
        txtcomp = findViewById(R.id.txtcomp);

        txtplayername = findViewById(R.id.txtotherusername);

        btndone = findViewById(R.id.btndone);

        img_play.setClipToOutline(true);
        img_quit.setClipToOutline(true);
        img_play.setOnTouchListener(MyTouchListener(img_play));
        img_quit.setOnTouchListener(MyTouchListener(img_quit));

        rlimg_x_s.setOnClickListener(MyClicklistener("rlimg_x_s"));
        rlimg_o_s.setOnClickListener(MyClicklistener("rlimg_o_s"));
        txt3by3.setOnClickListener(MyClicklistener("txt3by3"));
        txt5by5.setOnClickListener(MyClicklistener("txt5by5"));
        txthuman.setOnClickListener(MyClicklistener("txthuman"));
        txtcomp.setOnClickListener(MyClicklistener("txtcomp"));


        btndone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(sideselected == null)
                {
                    Toast.makeText(context, "X's or O's?", Toast.LENGTH_SHORT).show();
                }
                else if(playvs.equals("human"))
                {
                    player2name = txtplayername.getText().toString();
                    if(player2name.isEmpty())
                    {
                        Toast.makeText(context, "Enter name of opponent", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        StartGame();
                    }
                }
                else
                {
                    StartGame();
                }
            }

            private void StartGame()
            {
                Intent game;
                if(boardtype.equals("3by3"))
                {
                     game = new Intent(context, Game3by3.class);
                }
                else
                {
                     game = new Intent(context, Game5by5.class);
                }
                game.putExtra("sideselected", sideselected);
                game.putExtra("playvs", playvs);
                game.putExtra("player2name", player2name);
                startActivity(game);
            }
        });

    }

    private View.OnTouchListener MyTouchListener(final ImageView imgview)
    {
        return (new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event)
            {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        v.setScaleX((float)0.91);
                        v.setScaleY((float)0.91);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setScaleX(1);
                        v.setScaleY(1);
                        if(imgview == img_quit)
                        {
                            finishAffinity();
                        }
                        else
                        {
                            //reset
                            rlimg_x_s.setBackgroundResource(0);
                            rlimg_o_s.setBackgroundResource(0);
                            sideselected = null;
                            txt3by3.setTextColor(getResources().getColor(R.color.yellowbrown));
                            txt5by5.setTextColor(Color.WHITE);
                            boardtype = "3by3";
                            txthuman.setTextColor(Color.WHITE);
                            txtcomp.setTextColor(getResources().getColor(R.color.yellowbrown));
                            playvs = "comp";
                            player2name = null;
                            txtplayername.setVisibility(View.INVISIBLE);

                            SelectSide();
                        }
                        break;

                }
                return true;
            }
        });
    }

    private View.OnClickListener MyClicklistener(final String strview) {
        return (new View.OnClickListener() {
            public void onClick(View v) {
                switch (strview)
                {
                    case "rlimg_x_s":
                        sideselected = "x_s";
                        rlimg_o_s.setBackgroundResource(0);
                        rlimg_x_s.setBackgroundResource(R.drawable.rlimgxsos_style_selected);
                        break;
                    case "rlimg_o_s":
                        sideselected = "o_s";
                        rlimg_x_s.setBackgroundResource(0);
                        rlimg_o_s.setBackgroundResource(R.drawable.rlimgxsos_style_selected);
                        break;
                    case "txt3by3":
                        txt3by3.setTextColor(getResources().getColor(R.color.yellowbrown));
                        txt5by5.setTextColor(Color.WHITE);
                        boardtype = "3by3";
                        break;
                    case "txt5by5":
                        txt5by5.setTextColor(getResources().getColor(R.color.yellowbrown));
                        txt3by3.setTextColor(Color.WHITE);
                        boardtype = "5by5";
                        break;
                    case "txthuman":
                        txthuman.setTextColor(getResources().getColor(R.color.yellowbrown));
                        txtcomp.setTextColor(Color.WHITE);
                        playvs = "human";

                        txtplayername.setVisibility(View.VISIBLE);
                        txtplayername.requestFocus();
                        break;
                    case "txtcomp":

                        txtcomp.setTextColor(getResources().getColor(R.color.yellowbrown));
                        txthuman.setTextColor(Color.WHITE);
                        playvs = "comp";

                        txtplayername.setVisibility(View.INVISIBLE);
                        player2name = null;
                        txtplayername.setText("");
                        break;
                }


            }
        });

    }

    private void SelectSide()
    {
        Animation slideoutleft = AnimationUtils.loadAnimation(context, R.anim.slideout_toleft);

        rl_welcome.startAnimation(slideoutleft);

        Animation slideinright = AnimationUtils.loadAnimation(context, R.anim.slidein_fromright);
        rl_welcome.setVisibility(View.GONE);

        ll_options.setVisibility(View.VISIBLE);
        ll_options.startAnimation(slideinright);
    }


    public void onBackPressed()
    {
        if(ll_options.isShown())
        {
            Animation slideoutright = AnimationUtils.loadAnimation(context, R.anim.slideout_toright);

            ll_options.startAnimation(slideoutright);

            Animation slideinleft = AnimationUtils.loadAnimation(context, R.anim.slidein_fromleft);
            ll_options.setVisibility(View.GONE);

            rl_welcome.setVisibility(View.VISIBLE);
            rl_welcome.startAnimation(slideinleft);
        }
        else
        {
            finishAffinity();
        }
    }
}
