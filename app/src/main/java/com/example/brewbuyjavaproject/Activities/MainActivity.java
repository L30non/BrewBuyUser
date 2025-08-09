package com.example.brewbuyjavaproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.brewbuyjavaproject.BrewBuyActivity;
import com.example.brewbuyjavaproject.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Button btnManageCoffee = findViewById(R.id.btnManageCoffee);
        btnManageCoffee.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, BrewBuyActivity.class)));
    }

}