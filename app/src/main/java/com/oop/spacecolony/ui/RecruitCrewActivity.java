package com.oop.spacecolony.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.oop.spacecolony.R;
import com.oop.spacecolony.logic.SaveManager;
import com.oop.spacecolony.model.Pilot;
import com.oop.spacecolony.model.Medic;
import com.oop.spacecolony.model.ColonyArchive;
import com.oop.spacecolony.model.Crew;
import com.oop.spacecolony.model.Soldier;
import com.oop.spacecolony.model.Engineer;
import com.oop.spacecolony.model.Scientist;
import com.oop.spacecolony.util.IdGenerator;

public class RecruitCrewActivity extends AppCompatActivity {

    private EditText etCrewName;
    private RadioGroup radioGroupClasses, radioGroupGender;
    private ColonyArchive archive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit_crew);

        etCrewName = findViewById(R.id.etCrewName);
        radioGroupClasses = findViewById(R.id.radioGroupClasses);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        Button btnConfirmRecruit = findViewById(R.id.btnConfirmRecruit);

        archive = SaveManager.load(this);

        btnConfirmRecruit.setOnClickListener(v -> {
            String name = etCrewName.getText().toString().trim();
            int selectedClassId = radioGroupClasses.getCheckedRadioButtonId();
            int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();

            if (name.isEmpty()) {
                Toast.makeText(this, "Enter a crew member name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedClassId == -1) {
                Toast.makeText(this, "Select a class", Toast.LENGTH_SHORT).show();
                return;
            }

            String gender = (selectedGenderId == R.id.rbMale) ? "Male" : "Female";
            int id = IdGenerator.getNextId(this);
            Crew crew = null;

            if (selectedClassId == R.id.rbSoldier) {
                crew = new Soldier(id, name, gender);
            } else if (selectedClassId == R.id.rbPilot) {
                crew = new Pilot(id, name, gender);
            } else if (selectedClassId == R.id.rbMedic) {
                crew = new Medic(id, name, gender);
            } else if (selectedClassId == R.id.rbScientist) {
                crew = new Scientist(id, name, gender);
            } else if (selectedClassId == R.id.rbEngineer) {
                crew = new Engineer(id, name, gender);
            }

            if (crew != null) {
                archive.addCrew(crew);
                SaveManager.save(this, archive);
                Toast.makeText(this, "Crew member recruited", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}