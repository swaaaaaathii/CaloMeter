package com.example.android.calometer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphActivity extends AppCompatActivity {

    //Create DatabaseHelper object to connect with the database
    DatabaseHelper helper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //Get the user id
        String sid = getIntent().getStringExtra("sessionid");
        final int id = Integer.parseInt(sid);

        int[] calories = helper.getDailyCalories(id);
        int count = helper.getRecordCount(id);
        int i =0;
        DataPoint[] points = new DataPoint[count];

        //Create a graph which plots Day vs. Calorie Carryover
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(count+5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-10000);
        graph.getViewport().setMaxY(10000);
        graph.setTitle("Calorie Loss Trend" );
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Day");
        gridLabel.setVerticalAxisTitle("Carryover");
        for(i=0;i<count;i++)
        {
            points[i] = new DataPoint(i,calories[i]);

        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        graph.addSeries(series);
    }
}
