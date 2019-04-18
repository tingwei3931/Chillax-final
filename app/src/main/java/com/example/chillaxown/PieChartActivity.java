package com.example.chillaxown;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import me.itangqi.waveloadingview.WaveLoadingView;


public class PieChartActivity extends AppCompatActivity {
    private static String TAG = "activity_piechart";

    // float sp1= 10.6f+10.8f;
    //private float[] yData = {sp, 10, 66.76f, 44.32f, 46.01f, 16.89f, 23.9f};
    private String[] xData = {"Trip", "Study" , "Chores" , "Family", "Relaxation", "Urgent"};


    PieChart pieChart;
    long totalTimeSpent, stressTime, relaxTime;
    WaveLoadingView wa,wa1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Task Visualisation");

        Log.d(TAG, "onCreate: starting to create chart");
        float sp=0,sp1=0,sp2=0,sp3=0,sp4=0,sp5=0,sp6=0;

        TaskDetailsSQL task = new TaskDetailsSQL(getApplicationContext());

        sp = task.category("Trip");
        sp1= task.category("Study");
        sp2= task.category("Chores");
        sp3= task.category("Family");
        sp4= task.category("Relaxation");
        sp5= task.category("Urgent");


        final float[] yData = {sp, sp1, sp2, sp3, sp4, sp5};
        pieChart = (PieChart) findViewById(R.id.idPieChart);

        pieChart.setDescription("Percentage per Task");
        pieChart.setRotationEnabled(true);
        //pieChart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Super Cool Chart");
        pieChart.setCenterTextSize(10);
        //pieChart.setDrawEntryLabels(true);
        //pieChart.setEntryLabelTextSize(20);
        //More options just check out the documentation!
        Log.d(TAG, "addDataSet started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            if (yData[i] > 0)
                yEntrys.add(new PieEntry(yData[i] , xData[i]));
        }

        for(int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "category");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        HashMap<String, Integer> categoryDict = new HashMap<String, Integer>();
        categoryDict.put("Trip", Color.BLUE);
        categoryDict.put("Study", Color.GRAY);
        categoryDict.put("Chores", Color.GREEN);
        categoryDict.put("Family", Color.YELLOW);
        categoryDict.put("Relaxation", Color.MAGENTA);
        categoryDict.put("Urgent", Color.RED);

        ArrayList<Integer> usedColors = new ArrayList<>();
        for(int i = 0; i < yEntrys.size(); i++) {
            usedColors.add(categoryDict.get(yEntrys.get(i).getLabel()));
        }

       /* categoryDict.put("Trip", Color.BLUE);
        categoryDict.put("Study", Color.GRAY);
        categoryDict.put("Chores", Color.GREEN);
        categoryDict.put("Family", Color.YELLOW);
        categoryDict.put("Relaxation", Color.MAGENTA);
        categoryDict.put("Urgent", Color.RED);*/
        pieDataSet.setColors(usedColors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        //legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setEntryLabelColor(Color.GRAY);
        pieChart.invalidate();
        // addDataSet();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());
                Log.d(TAG, "onValueSelected: " + h.toString());

                //Log.i("Entry", e.getX());
                PieEntry pe = (PieEntry) e;
                Log.e("LABEL",pe.getLabel());
                int pos1 = e.toString().indexOf("(sum): ");
                String category = e.toString().substring(pos1 + 7);
                Snackbar.make(getWindow().getDecorView().getRootView(), pe.getLabel(), Snackbar.LENGTH_LONG)
                        .show();

                for(int i = 0; i < yData.length; i++){
                    if(yData[i] == Float.parseFloat(category)){
                        pos1 = i;
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        calculateWavePercentage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void calculateWavePercentage() {
        TaskDetailsSQL sql = new TaskDetailsSQL(this);
        /**
        ArrayList<Duration> totalDuration = sql.getAllDuration();
        if(totalDuration.size() > 0) {
            for(Duration d: totalDuration) {
                totalTimeSpent += d.getTaskTime();
            }
        }
**/

        String[] stressCategory = {"Trip", "Family", "Relaxation"};
        String[] relaxationCategory = {"Chores", "Urgent", "Study"};

        for(String category: stressCategory) {
            ArrayList<Duration> stressDuration = sql.getDurationByCategory(category);
            if(stressDuration.size() > 0) {
                for(Duration d: stressDuration)
                    stressTime += d.getTaskTime();
            }
        }

        for(String category: relaxationCategory) {
            ArrayList<Duration> relaxDuration = sql.getDurationByCategory(category);
            if(relaxDuration.size() > 0) {
                for(Duration d: relaxDuration)
                    relaxTime += d.getTaskTime();
            }
        }

        totalTimeSpent = relaxTime + stressTime;
        Log.i("PERCENT_RELAX", String.valueOf(relaxTime));
        Log.i("PERCENT_STRESS", String.valueOf(stressTime));
        Log.i("PERCENT_TIMESPENT", String.valueOf(totalTimeSpent));

        int relaxPercentage = (totalTimeSpent == 0) ?  50 : Math.round(relaxTime * 100 / totalTimeSpent);
        int stressPercentage = (totalTimeSpent == 0) ? 50 : 100 - relaxPercentage;

        Log.i("RELAX_PERCENT", String.valueOf(relaxPercentage));
        Log.i("STRESS_PERCENT", String.valueOf(stressPercentage));

        wa = (WaveLoadingView)findViewById(R.id.waveLoadingview);
        wa1 = (WaveLoadingView)findViewById(R.id.waveLoadingview1);
        wa1.setProgressValue((int)stressPercentage);
        wa.setProgressValue((int)relaxPercentage);
        wa1.setCenterTitle(String.format("%d%%", (int)stressPercentage));
        wa1.setTopTitle(String.format("Relaxation"));
        //wa.setTopTitleSize(10.0f);
        wa.setCenterTitle(String.format("%d%%", (int)relaxPercentage));
        //wa.setTopTitleSize(10.0f);
        wa.setTopTitle(String.format("Stress"));

        if (stressPercentage >= 80) {
            sendPushNotification("Uh Oh! Seems like you had been relaxing too much lately. Hurry and get some work done!");

        }

        if (relaxPercentage >= 80) {
            sendPushNotification("Watch out! You are stressing out lately. Take a deep breath and relax.");
        }
    }

    private void sendPushNotification(String message) {
        Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
        intent.putExtra("test", "I am a String");
        Calendar now = Calendar.getInstance();

        NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                .title("Be careful!")
                .content(message)
                .color(255, 0, 0, 255)
                .led_color(255, 255, 255, 255)
                .time(now)
                .addAction(intent, "Snooze", false)
                .key("test")
                .addAction(new Intent(), "Dismiss", true, false)
                .addAction(intent, "Done")
                .large_icon(R.mipmap.ic_launcher_round)
                .build();

        //Snackbar.make(getWindow().getDecorView().getRootView(), "Remainder has been saved!", Snackbar.LENGTH_LONG)
        //        .show();
    }

    private void sendWarningNotification() {

    }
}
