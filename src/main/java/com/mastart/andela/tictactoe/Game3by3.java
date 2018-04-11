package com.mastart.andela.tictactoe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Game3by3 extends AppCompatActivity
{
    private Context context;
    private TableLayout tableLayout;
    private TableRow table3Row1, table3Row2, table3Row3 ;
    private String sideselected, player2name="Comp", playvs;

    private TextView mypoints, otherusername, otheruserpoints;
    private Integer mypoints_int = 0, otheruserpoints_int = 0;

    private ArrayList<TableRow> table3rows = new ArrayList<TableRow>();
    private ArrayList<String> mylist;

    private AlertDialog dialog;
    private ImageView img_newgame, img_menu, alertimg_newgame, alertimg_menu, alertimg_close;

    private int side, myside;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = Game3by3.this;
        setContentView(R.layout.game);

        mypoints = findViewById(R.id.mypoints);
        otherusername = findViewById(R.id.otherusername);
        otheruserpoints = findViewById(R.id.otheruserpoints);

        tableLayout = findViewById(R.id.tablelayout);

        table3Row1 = tableLayout.findViewById(R.id.table3row1);
        table3Row2 = tableLayout.findViewById(R.id.table3row2);
        table3Row3 = tableLayout.findViewById(R.id.table3row3);

        img_newgame = findViewById(R.id.img_newgame);
        img_menu = findViewById(R.id.img_menu);

        img_newgame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RefreshTable();
                TableSetEnabled(true);
                img_newgame.setVisibility(View.INVISIBLE);
                img_menu.setVisibility(View.INVISIBLE);
            }
        });

        img_menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent main = new Intent(context, MainActivity.class);
                startActivity(main);
            }
        });

        playvs = getIntent().getStringExtra("playvs");
        if(playvs.equals("human"))
        {
            player2name = getIntent().getStringExtra("player2name");
            otherusername.setText(player2name);
        }
        else { otherusername.setText("Comp"); }

        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) tableLayout.getLayoutParams();

        TableRow.LayoutParams rowparams = new TableRow.LayoutParams();
        rowparams.height = lParams.height / 3;
        rowparams.width = lParams.width / 3;

        RefreshMyList();

        final int otherside;
        sideselected = getIntent().getStringExtra("sideselected");
        if(sideselected.equals("x_s"))
        {
            myside = R.drawable.img_x;
            otherside = R.drawable.img_o;
        }
        else
        {
            myside = R.drawable.img_o;
            otherside = R.drawable.img_x;
        }

        side = myside;

        table3rows.add(table3Row1);
        table3rows.add(table3Row2);
        table3rows.add(table3Row3);

        int x = 1;
        for(final TableRow tablerow : table3rows)
        {
            while(x <= 9)
            {
                final int tmpx = x;
                final ImageView imgview = new ImageView(context);
                imgview.setLayoutParams(rowparams);
                imgview.setPadding(28,0,0,14);
                imgview.setId(x);
                imgview.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(imgview.getTag() == null)
                        {
                            imgview.setImageResource(side);
                            if(side == myside)
                            {
                                imgview.setTag("has_myside");
                                if(playvs.equals("human"))
                                {
                                    side = otherside;
                                }
                            }
                            else
                            {
                                imgview.setTag("has_otherside");
                                if(playvs.equals("human"))
                                {
                                    side = myside;
                                }
                            }

                            mylist.remove(Integer.toString(tmpx));      //update list of non-occupied

                            //check for winner
                            if(CheckForWinner() == true){ return; }


                            int mylistsize = mylist.size();
                            if(mylistsize >0)
                            {
                                if(!playvs.equals("human"))
                                {
                                    int randomindex = ThreadLocalRandom.current().nextInt(0, mylistsize);

                                    //id of img to place otherside
                                    int randomid = Integer.parseInt(mylist.get(randomindex));
                                    mylist.remove(Integer.toString(randomid));

                                    if(randomid < 4)
                                    {
                                        ImageView randomimgview = (ImageView) table3Row1.getChildAt(randomid-1);
                                        randomimgview.setImageResource(otherside);
                                        randomimgview.setTag("has_otherside");
                                    }
                                    else if(randomid < 7)
                                    {
                                        ImageView randomimgview = (ImageView) table3Row2.getChildAt(randomid-4);
                                        randomimgview.setImageResource(otherside);
                                        randomimgview.setTag("has_otherside");
                                    }
                                    else
                                    {
                                        ImageView randomimgview = (ImageView) table3Row3.getChildAt(randomid-7);
                                        randomimgview.setImageResource(otherside);
                                        randomimgview.setTag("has_otherside");
                                    }
                                    //check if comp wins
                                    CheckForWinner();
                                }

                            }
                            else
                            {
                                //no winner found
                                ShowAlertWinner("draw");
                            }

                        }

                    }
                });
                tablerow.addView(imgview);

                x++;
                if(x == 4 || x == 7){ break; }

            }
        }


    }

    private Boolean CheckForWinner()
    {
        if(CheckDiagWinner() == false)
        {
            if(CheckRowsWinner() == false)
            {
                if(CheckColsWinner() == true) { return true; }
            }
            else{ return true; }
        }
        else{ return true; }
        return false;
    }

    private void RefreshMyList()
    {
        mylist = new ArrayList<String>();
        for(int no=1;no<=9;no++)
        {
            mylist.add(Integer.toString(no));
        }
    }

    private void RefreshTable()
    {
        RefreshMyList();
        for(TableRow tr : table3rows)
        {
            for(int x=0;x<3;x++)
            {
                ImageView imgview = (ImageView)tr.getChildAt(x);
                imgview.setImageResource(0);
                imgview.setTag(null);
            }

        }
        side = myside;
    }

    private void TableSetEnabled(Boolean enable)
    {
        for(TableRow tr : table3rows)
        {
            for(int x=0;x<3;x++)
            {
                ImageView imgview = (ImageView)tr.getChildAt(x);
                imgview.setEnabled(enable);
            }

        }
    }

    private Boolean CheckRowsWinner()
    {
        for(TableRow tr : table3rows)
        {
            ImageView img = (ImageView)tr.getChildAt(0);
            String whichside = (String)img.getTag();
            if(whichside != null)
            {
                //compare with index 1 & 2
                ImageView tmpimg = (ImageView)tr.getChildAt(1);
                String tmpwhichside = (String)tmpimg.getTag();

                ImageView tmpimg2 = (ImageView)tr.getChildAt(2);
                String tmpwhichside2 = (String)tmpimg2.getTag();

                if(tmpwhichside != null && tmpwhichside2 != null)
                {
                    if(tmpwhichside.equals(whichside) && tmpwhichside2.equals(whichside))
                    {
                        ShowAlertWinner(whichside);
                        return true;
                    }
                }

            }
        }
        return false;

    }

    private Boolean CheckDiagWinner()
    {
        String whichside;

        ImageView img = (ImageView)table3Row2.getChildAt(1);
        whichside = (String)img.getTag();

        if(whichside != null)
        {
            int b = 0, c=2;
            for(int k=1;k<=2;k++)
            {
                ImageView tmpimg = (ImageView)table3Row1.getChildAt(b);
                String tmpwhichside = (String)tmpimg.getTag();

                ImageView tmpimg2 = (ImageView)table3Row3.getChildAt(c);
                String tmpwhichside2 = (String)tmpimg2.getTag();

                if(tmpwhichside != null && tmpwhichside2 != null)
                {
                    if(tmpwhichside.equals(whichside) && tmpwhichside2.equals(whichside))
                    {
                        ShowAlertWinner(whichside);
                        return true;
                    }
                }

                b = 2;
                c = 0;
            }

        }

        return false;
    }

    private Boolean CheckColsWinner()
    {
        for(int y=0;y<3;y++)
        {
            ImageView img = (ImageView)table3Row1.getChildAt(y);
            String whichside = (String)img.getTag();

            if(whichside != null)
            {
                ImageView tmpimg = (ImageView)table3Row2.getChildAt(y);
                String tmpwhichside = (String)tmpimg.getTag();

                ImageView tmpimg2 = (ImageView)table3Row3.getChildAt(y);
                String tmpwhichside2 = (String)tmpimg2.getTag();

                if(tmpwhichside != null && tmpwhichside2 != null)
                {
                    if(tmpwhichside.equals(whichside) && tmpwhichside2.equals(whichside))
                    {
                        ShowAlertWinner(whichside);
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private void ShowAlertWinner(String side)
    {
        String strmypoints = "0", strotheruserpoints = "0";
        TextView title, otheruser, txt1, txt2;

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.alertdialog_layout, null);

        title = view.findViewById(R.id.txtwho_won);
        otheruser = view.findViewById(R.id.alerttxt_otheruser);
        txt1 = view.findViewById(R.id.alertdialog_txt1);
        txt2 = view.findViewById(R.id.alertdialog_txt2);

        alertimg_newgame = view.findViewById(R.id.alertimg_new_game);
        alertimg_menu = view.findViewById(R.id.alertimg_menu);
        alertimg_close = view.findViewById(R.id.alertimg_close);

        otheruser.setText(player2name+"'s Score: ");

        alertimg_newgame.setClipToOutline(true);
        alertimg_menu.setClipToOutline(true);

        if(side.equals("has_myside"))
        {
            title.setText("You Won This Round!");
            mypoints_int+=10;

            txt1.setTextColor(Color.WHITE);
        }
        else if(side.equals("has_otherside"))
        {
            title.setText(player2name+" Wins!");
            otheruserpoints_int+=10;

            txt2.setTextColor(Color.WHITE);
        }
        else
        {
            title.setText("Draw!");
            mypoints_int+=5;
            otheruserpoints_int+=5;

            txt1.setTextColor(Color.WHITE);
            txt2.setTextColor(Color.WHITE);
        }

        strmypoints = Integer.toString(mypoints_int);
        strotheruserpoints = Integer.toString(otheruserpoints_int);

        mypoints.setText(strmypoints);
        otheruserpoints.setText(strotheruserpoints);
        txt1.setText(strmypoints);
        txt2.setText(strotheruserpoints);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(false);
        alert.setView(view);

        dialog = alert.create();

        alertimg_newgame.setOnTouchListener(MyTouchListener(alertimg_newgame));
        alertimg_menu.setOnTouchListener(MyTouchListener(alertimg_menu));
        alertimg_close.setOnTouchListener(MyTouchListener(alertimg_close));

        dialog.show();
    }

    private View.OnTouchListener MyTouchListener(final ImageView imgview)
    {
        return (new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        imgview.setBackgroundResource(R.drawable.alertimg_style_clikd);
                        break;
                    case MotionEvent.ACTION_UP:
                        imgview.setBackgroundResource(0);
                        if(imgview == alertimg_newgame)
                        {
                            dialog.dismiss();
                            RefreshTable();
                        }
                        else if(imgview == alertimg_menu)
                        {
                            dialog.dismiss();
                            Intent main = new Intent(context, MainActivity.class);
                            startActivity(main);
                        }
                        else
                        {
                            dialog.dismiss();
                            TableSetEnabled(false);
                            img_newgame.setVisibility(View.VISIBLE);
                            img_menu.setVisibility(View.VISIBLE);

                        }
                        break;
                }
                return true;
            }
        });
    }

}
