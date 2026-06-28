package com.oop.spacecolony;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oop.spacecolony.logic.SaveManager;
import com.oop.spacecolony.model.ColonyArchive;
import com.oop.spacecolony.ui.MissionControlActivity;
import com.oop.spacecolony.ui.RecruitCrewActivity;
import com.oop.spacecolony.ui.SelectDuoActivity;
import com.oop.spacecolony.ui.StatisticsActivity;
import com.oop.spacecolony.ui.SimulatorActivity;
import com.oop.spacecolony.ui.adapter.CrewAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerCrew;
    private ColonyArchive archive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerCrew = findViewById(R.id.recyclerCrew);
        Button btnRecruit = findViewById(R.id.btnRecruit);
        Button btnSimulator = findViewById(R.id.btnSimulator);
        Button btnMissionControl = findViewById(R.id.btnMissionControl);
        Button btnStats = findViewById(R.id.btnStats);
        Button btnSaveFile = findViewById(R.id.btnSaveFile);
        Button btnLoadFile = findViewById(R.id.btnLoadFile);

        archive = SaveManager.load(this);

        recyclerCrew.setLayoutManager(new LinearLayoutManager(this));
        recyclerCrew.setAdapter(new CrewAdapter(archive.getAllCrew()));

        btnRecruit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecruitCrewActivity.class);
            startActivity(intent);
        });

        btnSimulator.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SimulatorActivity.class);
            startActivity(intent);
        });

        btnMissionControl.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SelectDuoActivity.class);
            startActivity(intent);
        });

        btnStats.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });

        btnSaveFile.setOnClickListener(v -> {
            SaveManager.saveToFile(this, archive);
            Toast.makeText(this, "Crew saved to file", Toast.LENGTH_SHORT).show();
        });

        btnLoadFile.setOnClickListener(v -> {
            archive = SaveManager.loadFromFile(this);
            SaveManager.save(this, archive); // keep auto-save in sync
            recyclerCrew.setAdapter(new CrewAdapter(archive.getAllCrew()));
            Toast.makeText(this, "Crew loaded from file", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        archive = SaveManager.load(this);
        recyclerCrew.setAdapter(new CrewAdapter(archive.getAllCrew()));
    }
}