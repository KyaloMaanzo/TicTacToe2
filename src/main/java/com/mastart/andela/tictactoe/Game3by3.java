package com.mastart.andela.tictactoe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
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
    private TextView txtnewgame, txtmenu;
    private RelativeLayout alertrl_newgame, alertrl_menu, alertrl_close;

    private String side, myside;

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

        txtnewgame = findViewById(R.id.txtnewgame);
        txtmenu = findViewById(R.id.txtmenu);

        txtnewgame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RefreshTable();
                TableSetEnabled(true);
                txtnewgame.setVisibility(View.INVISIBLE);
                txtmenu.setVisibility(View.INVISIBLE);
            }
        });

        txtmenu.setOnClickListener(new View.OnClickListener() {
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

        final String otherside;
        sideselected = getIntent().getStringExtra("sideselected");
        if(sideselected.equals("x_s"))
        {
            myside = "X";
            otherside = "O";
        }
        else
        {
            myside = "O";
            otherside = "X";
        }

        side = myside;

        table3rows.add(table3Row1);
        table3rows.add(table3Row2);
        table3rows.add(table3Row3);

        Typeface tf = ResourcesCompat.getFont(context, R.font.mvboli);

        int x = 1;
        for(final TableRow tablerow : table3rows)
        {
            while(x <= 9)
            {
                final int tmpx = x;
                final TextView txtview = new TextView(context);
                txtview.setLayoutParams(rowparams);
                txtview.setTextColor(Color.WHITE);
                txtview.setTypeface(tf);
                txtview.setTextSize(35f);
                txtview.setGravity(Gravity.CENTER);
                txtview.setId(x);
                txtview.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(txtview.getTag() == null)
                        {
                            txtview.setText(side);
                            if(side == myside)
                            {
                                txtview.setTag("has_myside");
                                if(playvs.equals("human"))
                                {
                                    side = otherside;
                                }
                            }
                            else
                            {
                                txtview.setTag("has_otherside");
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
                                        TextView randomtxtview = (TextView) table3Row1.getChildAt(randomid-1);
                                        randomtxtview.setText(otherside);
                                        randomtxtview.setTag("has_otherside");
                                    }
                                    else if(randomid < 7)
                                    {
                                        TextView randomtxtview = (TextView) table3Row2.getChildAt(randomid-4);
                                        randomtxtview.setText(otherside);
                                        randomtxtview.setTag("has_otherside");
                                    }
                                    else
                                    {
                                        TextView randomtxtview = (TextView) table3Row3.getChildAt(randomid-7);
                                        randomtxtview.setText(otherside);
                                        randomtxtview.setTag("has_otherside");
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
                tablerow.addView(txtview);

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
                TextView txtview = (TextView)tr.getChildAt(x);
                txtview.setText("");
                txtview.setTag(null);
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
                TextView txtview = (TextView)tr.getChildAt(x);
                txtview.setEnabled(enable);
            }

        }
    }

    private Boolean CheckRowsWinner()
    {
        for(TableRow tr : table3rows)
        {
            TextView txt = (TextView)tr.getChildAt(0);
            String whichside = (String)txt.getTag();
            if(whichside != null)
            {
                //compare with index 1 & 2
                TextView tmptxt = (TextView)tr.getChildAt(1);
                String tmpwhichside = (String)tmptxt.getTag();

                TextView tmptxt2 = (TextView)tr.getChildAt(2);
                String tmpwhichside2 = (String)tmptxt2.getTag();

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
        TextView txt = (TextView)table3Row2.getChildAt(1);
        String whichside = (String)txt.getTag();

        if(whichside != null)
        {
            int b = 0, c=2;
            for(int k=1;k<=2;k++)
            {
                TextView tmptxt = (TextView)table3Row1.getChildAt(b);
                String tmpwhichside = (String)tmptxt.getTag();

                TextView tmptxt2 = (TextView)table3Row3.getChildAt(c);
                String tmpwhichside2 = (String)tmptxt2.getTag();

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
            TextView img = (TextView)table3Row1.getChildAt(y);
            String whichside = (String)img.getTag();

            if(whichside != null)
            {
                TextView tmptxt = (TextView)table3Row2.getChildAt(y);
                String tmpwhichside = (String)tmptxt.getTag();

                TextView tmptxt2 = (TextView)table3Row3.getChildAt(y);
                String tmpwhichside2 = (String)tmptxt2.getTag();

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

        alertrl_newgame = view.findViewById(R.id.alertrl_newgame);
        alertrl_menu = view.findViewById(R.id.alertrl_menu);
        alertrl_close = view.findViewById(R.id.alertrl_close);

        otheruser.setText(player2name+"'s Score: ");

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

        alertrl_newgame.setOnTouchListener(MyTouchListener(alertrl_newgame));
        alertrl_menu.setOnTouchListener(MyTouchListener(alertrl_menu));
        alertrl_close.setOnTouchListener(MyTouchListener(alertrl_close));

        dialog.show();
    }

    private View.OnTouchListener MyTouchListener(final RelativeLayout rl)
    {
        return (new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        rl.setBackgroundResource(R.drawable.alertrl_style_clikd);
                        break;
                    case MotionEvent.ACTION_UP:
                        rl.setBackgroundResource(0);
                        if(rl == alertrl_newgame)
                        {
                            dialog.dismiss();
                            RefreshTable();
                        }
                        else if(rl == alertrl_menu)
                        {
                            dialog.dismiss();
                            Intent main = new Intent(context, MainActivity.class);
                            startActivity(main);
                        }
                        else
                        {
                            dialog.dismiss();
                            TableSetEnabled(false);
                            txtnewgame.setVisibility(View.VISIBLE);
                            txtmenu.setVisibility(View.VISIBLE);

                        }
                        break;
                }
                return true;
            }
        });
    }

}
