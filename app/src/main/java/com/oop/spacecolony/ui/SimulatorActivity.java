package com.oop.spacecolony.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.oop.spacecolony.R;
import com.oop.spacecolony.logic.SaveManager;
import com.oop.spacecolony.model.ColonyArchive;
import com.oop.spacecolony.model.Crew;

import java.util.ArrayList;
import java.util.List;

public class SimulatorActivity extends AppCompatActivity {

    private Spinner spinnerCrew;
    private ColonyArchive archive;
    private List<Crew> crewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulator);

        spinnerCrew = findViewById(R.id.spinnerCrew);
        Button btnTrainCrew = findViewById(R.id.btnTrainCrew);

        archive = SaveManager.load(this);
        crewList = archive.getAllCrew();

        List<String> crewNames = new ArrayList<>();
        for (Crew crew : crewList) {
            crewNames.add(crew.getName() + " (" + crew.getCrewClass() + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                crewNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCrew.setAdapter(adapter);

        btnTrainCrew.setOnClickListener(v -> {
            if (crewList.isEmpty()) {
                Toast.makeText(this, "No crew members available", Toast.LENGTH_SHORT).show();
                return;
            }

            int position = spinnerCrew.getSelectedItemPosition();
            Crew selectedCrew = crewList.get(position);
            selectedCrew.gainExperience(50);
            selectedCrew.recordTrainingSession();

            SaveManager.save(this, archive);
            Toast.makeText(this,
                    selectedCrew.getName() + " trained and gained XP",
                    Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}