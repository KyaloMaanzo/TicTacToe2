package com.mastart.andela.tictactoe;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private Context context;
    private RelativeLayout rl_welcome, rlimg_x_s, rlimg_o_s;
    private ImageView img_play, img_quit, img3by3, img5by5, imghuman, imgcomp, imgdone;
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

        img3by3 = findViewById(R.id.img3by3);
        img5by5 = findViewById(R.id.img5by5);
        imghuman = findViewById(R.id.img_human);
        imgcomp = findViewById(R.id.img_comp);

        txtplayername = findViewById(R.id.txtotherusername);

        imgdone = findViewById(R.id.img_done);

        img_play.setClipToOutline(true);
        img_quit.setClipToOutline(true);
        img_play.setOnTouchListener(MyTouchListener(img_play));
        img_quit.setOnTouchListener(MyTouchListener(img_quit));

        rlimg_x_s.setOnClickListener(MyClicklistener("rlimg_x_s"));
        rlimg_o_s.setOnClickListener(MyClicklistener("rlimg_o_s"));
        img3by3.setOnClickListener(MyClicklistener("img3by3"));
        img5by5.setOnClickListener(MyClicklistener("img5by5"));
        imghuman.setOnClickListener(MyClicklistener("imghuman"));
        imgcomp.setOnClickListener(MyClicklistener("imgcomp"));


        imgdone.setOnClickListener(new View.OnClickListener() {
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
                            rlimg_x_s.setBackgroundResource(R.drawable.rlimgxsos_style);
                            rlimg_o_s.setBackgroundResource(R.drawable.rlimgxsos_style);
                            sideselected = null;
                            img3by3.setImageResource(R.mipmap.img3x3_clikd);
                            img5by5.setImageResource(R.mipmap.img5x5);
                            boardtype = "3by3";
                            imghuman.setImageResource(R.mipmap.txthuman);
                            imgcomp.setImageResource(R.mipmap.txtcomp_clikd);
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
                        rlimg_o_s.setBackgroundResource(R.drawable.rlimgxsos_style);
                        rlimg_x_s.setBackgroundResource(R.drawable.rlimgxsos_style_selected);
                        break;
                    case "rlimg_o_s":
                        sideselected = "o_s";
                        rlimg_x_s.setBackgroundResource(R.drawable.rlimgxsos_style);
                        rlimg_o_s.setBackgroundResource(R.drawable.rlimgxsos_style_selected);
                        break;
                    case "img3by3":
                        img3by3.setImageResource(R.mipmap.img3x3_clikd);
                        img5by5.setImageResource(R.mipmap.img5x5);
                        boardtype = "3by3";
                        break;
                    case "img5by5":
                        img5by5.setImageResource(R.mipmap.img5x5_clikd);
                        img3by3.setImageResource(R.mipmap.img3x3);
                        boardtype = "5by5";
                        break;
                    case "imghuman":
                        imghuman.setImageResource(R.mipmap.txthuman_clikd);
                        imgcomp.setImageResource(R.mipmap.txtcomp);
                        playvs = "human";

                        txtplayername.setVisibility(View.VISIBLE);
                        txtplayername.requestFocus();
                        break;
                    case "imgcomp":

                        imgcomp.setImageResource(R.mipmap.txtcomp_clikd);
                        imghuman.setImageResource(R.mipmap.txthuman);
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
