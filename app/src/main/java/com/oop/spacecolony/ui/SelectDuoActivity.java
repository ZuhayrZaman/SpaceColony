package com.oop.spacecolony.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.oop.spacecolony.R;
import com.oop.spacecolony.logic.MissionType;
import com.oop.spacecolony.logic.SaveManager;
import com.oop.spacecolony.model.ColonyArchive;
import com.oop.spacecolony.model.Crew;

import java.util.ArrayList;
import java.util.List;

public class SelectDuoActivity extends AppCompatActivity {

    private Spinner spinnerLead, spinnerReserve1, spinnerReserve2, spinnerMissionType;
    private ColonyArchive archive;
    private List<Crew> crewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_duo);

        spinnerLead = findViewById(R.id.spinnerLead);
        spinnerReserve1 = findViewById(R.id.spinnerPartner);
        spinnerReserve2 = findViewById(R.id.spinnerReserve2);
        spinnerMissionType = findViewById(R.id.spinnerMissionType);
        Button btnStartMission = findViewById(R.id.btnStartMission);

        archive = SaveManager.load(this);
        crewList = archive.getAvailableCrew();

        if (crewList.size() < 2) {
            Toast.makeText(this, "Need at least 2 available crew members", Toast.LENGTH_SHORT).show();
        }

        List<String> crewNames = new ArrayList<>();
        for (Crew crew : crewList) {
            crewNames.add(crew.getName() + " (" + crew.getCrewClass() + ")");
        }

        ArrayAdapter<String> crewAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                crewNames
        );
        crewAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLead.setAdapter(crewAdapter);
        spinnerReserve1.setAdapter(crewAdapter);

        List<String> reserve2Options = new ArrayList<>();
        reserve2Options.add("None");
        reserve2Options.addAll(crewNames);

        ArrayAdapter<String> reserve2Adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                reserve2Options
        );
        reserve2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReserve2.setAdapter(reserve2Adapter);

        ArrayAdapter<MissionType> missionAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                MissionType.values()
        );
        missionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMissionType.setAdapter(missionAdapter);

        btnStartMission.setOnClickListener(v -> {
            if (crewList.size() < 2) {
                Toast.makeText(this, "Need at least 2 available crew members", Toast.LENGTH_SHORT).show();
                return;
            }

            int leadPos = spinnerLead.getSelectedItemPosition();
            int reserve1Pos = spinnerReserve1.getSelectedItemPosition();
            int reserve2SpinnerPos = spinnerReserve2.getSelectedItemPosition();

            if (leadPos == reserve1Pos) {
                Toast.makeText(this, "Choose two different crew members", Toast.LENGTH_SHORT).show();
                return;
            }

            Crew leadCrew = crewList.get(leadPos);
            Crew reserve1Crew = crewList.get(reserve1Pos);
            Crew reserve2Crew = null;

            if (reserve2SpinnerPos > 0) {
                int reserve2Pos = reserve2SpinnerPos - 1;
                if (reserve2Pos == leadPos || reserve2Pos == reserve1Pos) {
                    Toast.makeText(this, "Third crew member must be different", Toast.LENGTH_SHORT).show();
                    return;
                }
                reserve2Crew = crewList.get(reserve2Pos);
            }

            leadCrew.recordMission();
            reserve1Crew.recordMission();
            if (reserve2Crew != null) {
                reserve2Crew.recordMission();
            }
            archive.progressMedbayRecoveryForAll();
            SaveManager.save(this, archive);

            MissionType missionType = (MissionType) spinnerMissionType.getSelectedItem();

            Intent intent = new Intent(SelectDuoActivity.this, MissionControlActivity.class);
            intent.putExtra("lead_id", leadCrew.getId());
            intent.putExtra("reserve1_id", reserve1Crew.getId());

            if (reserve2Crew != null) {
                intent.putExtra("reserve2_id", reserve2Crew.getId());
            }

            intent.putExtra("mission_type", missionType.name());
            startActivity(intent);
        });
    }
}