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

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class Game5by5 extends AppCompatActivity
{

    private Context context;
    private TableLayout tableLayout;
    private TableRow table5Row1, table5Row2, table5Row3, table5Row4, table5Row5 ;
    private String sideselected, player2name="Comp", playvs;

    private TextView mypoints, otherusername, otheruserpoints;
    private Integer mypoints_int = 0, otheruserpoints_int = 0;

    private ArrayList<TableRow> table5rows = new ArrayList<TableRow>();
    private ArrayList<String> mylist, ai_list;

    private AlertDialog dialog;
    private TextView txtnewgame, txtmenu;
    private RelativeLayout alertrl_newgame, alertrl_menu, alertrl_close;

    ArrayList<TableRow> rows = new ArrayList<>();
    ArrayList<TableRow> rowsfor1 = new ArrayList<>();
    ArrayList<TableRow> rowsfor2 = new ArrayList<>();
    ArrayList<TableRow> rowsto_loop1 = new ArrayList<>();
    ArrayList<TableRow> rowsto_loop2 = new ArrayList<>();

    private String side, myside, otherside;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = Game5by5.this;
        setContentView(R.layout.game5by5);

        mypoints = findViewById(R.id.mypoints);
        otherusername = findViewById(R.id.otherusername);
        otheruserpoints = findViewById(R.id.otheruserpoints);

        tableLayout = findViewById(R.id.tablelayout);

        table5Row1 = tableLayout.findViewById(R.id.table5row1);
        table5Row2 = tableLayout.findViewById(R.id.table5row2);
        table5Row3 = tableLayout.findViewById(R.id.table5row3);
        table5Row4 = tableLayout.findViewById(R.id.table5row4);
        table5Row5 = tableLayout.findViewById(R.id.table5row5);

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

        PopulateALists();

        playvs = getIntent().getStringExtra("playvs");
        if(playvs.equals("human"))
        {
            player2name = getIntent().getStringExtra("player2name");
            otherusername.setText(player2name);
        }
        else { otherusername.setText("Comp"); }


        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) tableLayout.getLayoutParams();

        TableRow.LayoutParams rowparams = new TableRow.LayoutParams();
        rowparams.height = lParams.height / 5;
        rowparams.width = lParams.width / 5;

        RefreshMyList();

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

        Typeface tf = ResourcesCompat.getFont(context, R.font.mvboli);

        int x = 1;
        for(final TableRow tablerow : table5rows)
        {
            while(x <= 25)
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
                            if(side.equals(myside))
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

                            if(mylist.size() >0)
                            {
                                if(!playvs.equals("human"))
                                {
                                    int ran_no = ThreadLocalRandom.current().nextInt(2);

                                    if(ran_no == 0) { PickRandomBox(); }
                                    else{ AI(); }

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

                    private void PickRandomBox()
                    {
                        int randomindex = ThreadLocalRandom.current().nextInt(0, mylist.size());

                        //id of img to place otherside
                        int randomid = Integer.parseInt(mylist.get(randomindex));
                        mylist.remove(Integer.toString(randomid));

                        TextView randomtxtview;
                        if(randomid < 6)
                        {
                            randomtxtview = (TextView) table5Row1.getChildAt(randomid-1);
                        }
                        else if(randomid < 11)
                        {
                            randomtxtview = (TextView) table5Row2.getChildAt(randomid-6);
                        }
                        else if(randomid < 16)
                        {
                            randomtxtview = (TextView) table5Row3.getChildAt(randomid-11);
                        }
                        else if(randomid < 21)
                        {
                            randomtxtview = (TextView) table5Row4.getChildAt(randomid-16);
                        }
                        else
                        {
                            randomtxtview = (TextView) table5Row5.getChildAt(randomid-21);
                        }

                        randomtxtview.setText(otherside);
                        randomtxtview.setTag("has_otherside");
                    }

                    private void AI()
                    {
                        //rows
                        for(TableRow tableRow : table5rows)
                        {
                            if(AIRows(tableRow, 0, 4) == true)
                            {
                                return;
                            }

                            if(AIRows(tableRow, 1,5) == true)
                            {
                                return;
                            }
                        }

                        //columns
                        for(int colno=0;colno<5;colno++)
                        {
                            if(AICols(colno, rowsto_loop1) == true)
                            {
                                return;
                            }

                            if(AICols(colno, rowsto_loop2) == true)
                            {
                                return;
                            }
                        }

                        //diagonals
                        for(int q=0;q<5;q++)
                        {
                            if(q==2){  continue; }

                            if(AIDiags(rowsto_loop1, q)==true)
                            {
                                return;
                            }

                            if(AIDiags(rowsto_loop2, q)==true)
                            {
                                return;
                            }
                        }

                        PickRandomBox();
                    }
                });
                tablerow.addView(txtview);

                x++;
                if(x == 6 || x == 11 || x == 16 || x == 21){ break; }

            }
        }


    }

    private void PopulateALists()
    {
        table5rows.add(table5Row1);
        table5rows.add(table5Row2);
        table5rows.add(table5Row3);
        table5rows.add(table5Row4);
        table5rows.add(table5Row5);

        rows.add(table5Row1);
        rows.add(table5Row2);

        rowsfor1.add(table5Row2);
        rowsfor1.add(table5Row3);
        rowsfor1.add(table5Row4);

        rowsfor2.add(table5Row3);
        rowsfor2.add(table5Row4);
        rowsfor2.add(table5Row5);

        rowsto_loop1.add(table5Row1);
        rowsto_loop1.add(table5Row2);
        rowsto_loop1.add(table5Row3);
        rowsto_loop1.add(table5Row4);

        rowsto_loop2.add(table5Row2);
        rowsto_loop2.add(table5Row3);
        rowsto_loop2.add(table5Row4);
        rowsto_loop2.add(table5Row5);
    }

    private Boolean CheckForWinner()
    {
        for(TableRow tmptablerow : table5rows)      //rows
        {
            if(CheckRowWinner(tmptablerow) == true)
            {
                return true;
            }
        }
        for(int index=0;index<5;index++)            //columns
        {
            if(CheckColWinner(index) == true)
            {
                return true;
            }
        }
        for(TableRow tmptablerow : rows)
        {
            if(CheckDiagWinner(tmptablerow) == true)
            {
                return true;
            }
        }
        return false;
    }

    private int CheckAIList()
    {
        int nohas_myside = Collections.frequency(ai_list, "has_myside");
        int nohas_otherside = Collections.frequency(ai_list, "has_otherside");

        if(nohas_myside >2)
        {
            int indexofnull = ai_list.indexOf("null");
            return indexofnull;
        }

        if(nohas_otherside >2)
        {
            int indexofnull = ai_list.indexOf("null");
            return indexofnull;
        }

        return -1;
    }

    private Boolean AIRows(TableRow tablerow, int start, int end)
    {
        ai_list = new ArrayList<>();

        for(int x=start;x<end;x++)
        {
            TextView txtview = (TextView)tablerow.getChildAt(x);
            String txttag = (String)txtview.getTag();

            if(txttag == null){ ai_list.add("null"); }
            else{ ai_list.add(txttag); }
        }

        int a = CheckAIList();
        if(a != -1)
        {
            TextView txt;
            if(start == 0)
            {
                txt = (TextView)tablerow.getChildAt(a);
            }
            else
            {
                txt = (TextView)tablerow.getChildAt(a+1);
            }

            txt.setText(otherside);
            txt.setTag("has_otherside");
            int id = txt.getId();
            mylist.remove(Integer.toString(id));
            return true;
        }

        return false;
    }

    private Boolean AICols(int colno, ArrayList<TableRow> rowsto_loop)
    {
        ai_list = new ArrayList<>();
        for(TableRow tr : rowsto_loop)
        {
            TextView txtview = (TextView)tr.getChildAt(colno);
            String txttag = (String)txtview.getTag();

            if(txttag == null){ ai_list.add("null"); }
            else{ ai_list.add(txttag); }
        }

        int a = CheckAIList();

        if(a != -1)
        {
            TableRow tr = rowsto_loop.get(a);

            TextView txtview = (TextView)tr.getChildAt(colno);
            txtview.setText(otherside);
            txtview.setTag("has_otherside");
            int id = txtview.getId();
            mylist.remove(Integer.toString(id));
            return true;
        }
        return false;
    }

    private Boolean AIDiags(ArrayList<TableRow> rowstoloop, int startindex)
    {
        ai_list = new ArrayList<>();
        int index = startindex;
        for(TableRow tr : rowstoloop)
        {
            TextView txtview = (TextView)tr.getChildAt(index);
            String txttag = (String)txtview.getTag();

            if(txttag != null){ ai_list.add(txttag); }
            else{ ai_list.add("null"); }

            if(startindex ==3 || startindex==4 ){ index--; }
            else{ index++; }

        }

        int a = CheckAIList();
        if(a != -1)
        {
            TableRow tableRow = rowstoloop.get(a);

            TextView txtview;
            if(startindex == 3 || startindex==4)
            {
                txtview = (TextView)tableRow.getChildAt(startindex-a);
            }
            else
            {
                txtview = (TextView)tableRow.getChildAt(a+startindex);
            }
            txtview.setText(otherside);
            txtview.setTag("has_otherside");
            int id = txtview.getId();
            mylist.remove(Integer.toString(id));
            return true;
        }

        return false;
    }

    private void RefreshMyList()
    {
        mylist = new ArrayList<>();
        for(int no=1;no<=25;no++)
        {
            mylist.add(Integer.toString(no));
        }
    }

    private void RefreshTable()
    {
        RefreshMyList();
        for(TableRow tr : table5rows)
        {
            for(int x=0;x<5;x++)
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
        for(TableRow tr : table5rows)
        {
            for(int x=0;x<5;x++)
            {
                TextView txtview = (TextView)tr.getChildAt(x);
                txtview.setEnabled(enable);
            }

        }
    }

    private Boolean CheckRowWinner(TableRow tableRow)
    {
        for(int x=0;x<2;x++)
        {
            Boolean winning = true;
            TextView txt = (TextView)tableRow.getChildAt(x);
            String whichside = (String)txt.getTag();

            int b;
            if(x == 0){ b = 4; }
            else{ b = 5; }
            if(whichside != null)
            {
                for(int y=x+1;y<b;y++)
                {
                    TextView tmptxt = (TextView)tableRow.getChildAt(y);
                    String tmpwhichside = (String)tmptxt.getTag();

                    if(tmpwhichside == null || !tmpwhichside.equals(whichside))
                    {
                        winning = false;
                        break;
                    }

                }
                if(winning == true)
                {
                    ShowAlertWinner(whichside);
                    return true;
                }
            }

        }

        return false;

    }

    private Boolean CheckColWinner(int columnindex)
    {
        for(TableRow tr : rows)
        {
            Boolean winning = true;
            TextView txt = (TextView)tr.getChildAt(columnindex);
            String whichside = (String)txt.getTag();

            if(whichside != null)
            {
                ArrayList<TableRow> rows_to_use;
                if(tr == table5Row1)
                {
                    rows_to_use = rowsfor1;
                }
                else{ rows_to_use = rowsfor2; }

                for(TableRow tmptr : rows_to_use)
                {
                    TextView tmptxt = (TextView)tmptr.getChildAt(columnindex);
                    String tmpwhichside = (String)tmptxt.getTag();

                    if(tmpwhichside == null || !tmpwhichside.equals(whichside))
                    {
                        winning = false;
                        break;
                    }
                }
                if(winning == true)
                {
                    ShowAlertWinner(whichside);
                    return true;
                }
            }
        }
        return false;
    }

    private Boolean CheckDiagWinner(TableRow tr)
    {
        ArrayList<TableRow> rows_to_use;
        if(tr == table5Row1){ rows_to_use = rowsfor1; }
        else{ rows_to_use = rowsfor2; }

        for(int x=0;x<5;x++)
        {
            Boolean winning = true;
            if(x == 2){ continue; }

            TextView txt = (TextView) tr.getChildAt(x);
            String whichside = (String)txt.getTag();

            int b = x;
            for(TableRow tmptr : rows_to_use)
            {
                if(x<2)
                {
                    b++;
                }
                else{ b--; }

                TextView tmptxt = (TextView)tmptr.getChildAt(b);
                String tmpwhichside = (String)tmptxt.getTag();

                if(tmpwhichside == null || !tmpwhichside.equals(whichside))
                {
                    winning = false;
                    break;
                }


            }
            if(winning == true)
            {
                ShowAlertWinner(whichside);
                return true;
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
