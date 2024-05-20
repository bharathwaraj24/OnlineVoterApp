package com.example.onlinevoting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VoterWaittimeCalc extends AppCompatActivity {
    private EditText etNumberOfVoters, etAverageServiceTime, etNumberOfPollingStations;
    private Button btnCalc;
    private TextView tvWaitTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_voter_waittime_calc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etNumberOfVoters = findViewById(R.id.etNumberOfVoters);
        etAverageServiceTime = findViewById(R.id.etAverageServiceTime);
        etNumberOfPollingStations = findViewById(R.id.etNumberOfPollingStations);
        btnCalc = findViewById(R.id.btnCalc);
        tvWaitTime = findViewById(R.id.tvWaitTime);
        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateWaitTime();
            }
        });
        Button bar = findViewById(R.id.barchart);
        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BarChartView.class);
                startActivity(intent);
            }
        });

    }
    private void calculateWaitTime() {
        try {
            int numberOfVoters = Integer.parseInt(etNumberOfVoters.getText().toString());
            double averageServiceTime = Double.parseDouble(etAverageServiceTime.getText().toString());
            int numberOfPollingStations = Integer.parseInt(etNumberOfPollingStations.getText().toString());

            // Calculate the average wait time
            double averageWaitTime = numberOfVoters * averageServiceTime / numberOfPollingStations;

            // Display the result
            tvWaitTime.setText(String.format("Estimated Average Wait Time: %.2f minutes", averageWaitTime));
        } catch (NumberFormatException e) {
            tvWaitTime.setText("Please enter valid numbers!");
        } catch (ArithmeticException e) {
            tvWaitTime.setText("Invalid input! Division by zero.");
        }
    }

}
