package com.example.food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Register1 extends AppCompatActivity {
    EditText mName;
    Spinner mDiet;
    Spinner mGoal;
    Button mNextBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);


        mName = findViewById(R.id.Namer);
        mGoal = (Spinner) findViewById(R.id.spinner3);
        mDiet = (Spinner) findViewById(R.id.spinner2);
        mNextBtn = findViewById(R.id.button3);


        List<String> Diet = Arrays.asList("None","Vegan","Vegetarian","Halal","Ketogenic","Lacto-Vegetarian","Ovo-Vegetarian","Pescetarian","Paleo","Primal","Whole30");
        Spinner spinnerDiet = findViewById(R.id.spinner2);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,Diet);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiet.setAdapter(adapter);

        List<String> goal = Arrays.asList("Prefer not to say","Lose weight","Gain weight","Track my calories");
        Spinner spinnerGoal = findViewById(R.id.spinner3);
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,goal);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGoal.setAdapter(adapter2);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = mName.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    mName.setError("Name is required");
                    return;
                }




                String diet = mDiet.getSelectedItem().toString();
                String goal = mGoal.getSelectedItem().toString();
                startActivity(new Intent(Register1.this,Register.class)
                        .putExtra("name",name)
                        .putExtra("goal",goal)
                        .putExtra("diet",diet.toLowerCase(Locale.ROOT)));


//                newIntent.putExtra("name",name);
//                newIntent.putExtra("goal",goal);
//                newIntent.putExtra("diet",diet);
//
//                startActivity(newIntent);
            }
        });



    }
}