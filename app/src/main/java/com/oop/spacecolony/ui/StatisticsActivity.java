package com.oop.spacecolony.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.oop.spacecolony.R;
import com.oop.spacecolony.logic.SaveManager;
import com.oop.spacecolony.model.ColonyArchive;
import com.oop.spacecolony.model.Crew;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        TextView tvStatsSummary = findViewById(R.id.tvStatsSummary);

        ColonyArchive archive = SaveManager.load(this);

        StringBuilder stats = new StringBuilder();

        for (Crew crew : archive.getAllCrew()) {
            stats.append(crew.getName())
                    .append(" | Missions: ").append(crew.getMissionsPlayed())
                    .append(" | Wins: ").append(crew.getVictories())
                    .append(" | Lost: ").append(crew.getLostMissions())
                    .append(" | Simulator: ").append(crew.getTrainingSessions())
                    .append("\n");
        }

        tvStatsSummary.setText(stats.toString());

        // Prevent crash if no crew members exist
        if (archive.getAllCrew().isEmpty()) {
            tvStatsSummary.setText("No statistics available yet.");
            return;
        }

        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();

        for (Crew crew : archive.getAllCrew()) {
            data.add(new ValueDataEntry(
                    crew.getName(),
                    crew.getTrainingSessions()
            ));
        }

        pie.data(data);
        pie.title("Simulator Sessions");

        anyChartView.setChart(pie);
    }
}