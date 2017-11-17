package com.example.android.calometer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DailyActivity extends AppCompatActivity {
    //Create DatabaseHelper object to connect to database
    DatabaseHelper helper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        //Get user ID of the user
        String sid = getIntent().getStringExtra("sessionid");
        final int id = Integer.parseInt(sid);

        //Get calorie values and calculate carry-over
        int food_cals = helper.foodCalsBurnt(id);
        int ex_cals = helper.exerciseCalsBurnt(id);
        int target = helper.returnTarget(id);
        final int carry_over = target + (food_cals - ex_cals);

        //Set the value of text fields
        final TextView fTextView = (TextView) findViewById(R.id.foodCals);
        fTextView.setText(String.valueOf(food_cals));
        final TextView eTextView = (TextView) findViewById(R.id.exCals);
        eTextView.setText(String.valueOf(ex_cals));
        final TextView tTextView = (TextView) findViewById(R.id.target);
        tTextView.setText(String.valueOf(target));
        final TextView cTextView = (TextView) findViewById(R.id.carry);
        cTextView.setText(String.valueOf(carry_over));
        final int updatedTarget = target + carry_over;

        Button b = (Button) findViewById(R.id.reset);
        //Update the database on button click
        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helper.deleteUserFood(id);
                        helper.deleteExercise(id);
                        helper.updateTarget(id,updatedTarget);
                        helper.insertUserTrack(id,carry_over);
                        //Check if the target date has been reached
                        int flag = helper.checkLastDate(id);
                        if(flag == 1)
                        {
                            Toast.makeText(getApplicationContext(),"Diet Completed!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
