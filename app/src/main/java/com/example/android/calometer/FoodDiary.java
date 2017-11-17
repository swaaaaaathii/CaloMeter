package com.example.android.calometer;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class FoodDiary extends AppCompatActivity{

    String food=new String();
    //Create DatabaseHelper object to connect to the database
    DatabaseHelper helper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_diary2);

        //Get the user id
        String sid = getIntent().getStringExtra("sessionid");
        final int id = Integer.parseInt(sid);

        //Create dropdown menu
        Spinner spinner= (Spinner)findViewById(R.id.spinner);
        String[] spinnerArray=new String[]{};
        Cursor curfood = helper.getDropDownFood();
        List<String> FoodSpinner = new ArrayList<String>();
        if(curfood!=null && curfood.moveToFirst()) {
            do {
                FoodSpinner.add(curfood.getString(curfood.getColumnIndex("food_name")));
            }while (curfood.moveToNext());
        }
        ArrayAdapter<String> FoodAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,FoodSpinner);
        FoodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(FoodAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        food=parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        //Insert food information on button click
        Button b1 = (Button) findViewById(R.id.addToDiary);
        b1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        EditText b = (EditText) findViewById(R.id.qty);
                        int qty = Integer.parseInt(b.getText().toString());

                        int cal = helper.getCalories(food);
                        FoodItem foo = new FoodItem();
                        foo.setFoodItem(id, food, qty, (cal * qty));
                        int check = helper.insertFoodItem(foo);
                    }
                });

        //Display food consumed by user on button click
        Button b2 = (Button) findViewById(R.id.displayView);
        b2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Cursor cur = helper.getAllRows(id);
                        String[] foodList = new String[]{DatabaseHelper.COLUMN_USER_FOOD_FOOD_NAME, DatabaseHelper.COLUMN_USER_FOOD_AMOUNT};
                        int[] toView = new int[]{R.id.item_name, R.id.item_amount};
                        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.food_item, cur, foodList, toView, 0);
                        ListView myList = (ListView) findViewById(R.id.foodlist);
                        myList.setAdapter(myCursorAdapter);
                    }
                });

    }


}
