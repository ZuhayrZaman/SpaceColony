package com.oop.spacecolony.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.oop.spacecolony.R;
import com.oop.spacecolony.logic.CombatManager;
import com.oop.spacecolony.logic.MissionType;
import com.oop.spacecolony.logic.SaveManager;
import com.oop.spacecolony.model.ColonyArchive;
import com.oop.spacecolony.model.Crew;

public class MissionControlActivity extends AppCompatActivity {

    private TextView tvFloor, tvAlienName, tvLeadCrew, tvPartnerCrew, tvCombatLog;
    private TextView tvMedKitCount;
    private ProgressBar progressAlien, progressLead;
    private ImageView imgAlien, imgLead;
    private ColonyArchive archive;
    private CombatManager combatManager;
    private MissionType missionType;
    private int floor = 1;
    private int medKits = 1;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_control);

        tvFloor = findViewById(R.id.tvFloor);
        tvAlienName = findViewById(R.id.tvAlienName);
        tvLeadCrew = findViewById(R.id.tvLeadCrew);
        tvPartnerCrew = findViewById(R.id.tvPartnerCrew);
        tvCombatLog = findViewById(R.id.tvCombatLog);
        tvMedKitCount = findViewById(R.id.tvMedKitCount);
        progressAlien = findViewById(R.id.progressAlien);
        progressLead = findViewById(R.id.progressLead);
        Button btnSwap1 = findViewById(R.id.btnSwap1);
        Button btnSwap2 = findViewById(R.id.btnSwap2);
        Button btnAttack = findViewById(R.id.btnAttack);
        Button btnUseMedKit = findViewById(R.id.btnUseMedKit);
        Button btnDefend = findViewById(R.id.btnDefend);
        Button btnSpecial = findViewById(R.id.btnSpecial);
        TextView tvMissionDescription = findViewById(R.id.tvMissionDescription);
        imgAlien = findViewById(R.id.imgAlien);
        imgLead = findViewById(R.id.imgLead);

        archive = SaveManager.load(this);

        int leadId = getIntent().getIntExtra("lead_id", -1);
        int reserve1Id = getIntent().getIntExtra("reserve1_id", -1);
        int reserve2Id = getIntent().getIntExtra("reserve2_id", -1);

        String missionTypeString = getIntent().getStringExtra("mission_type");
        if (missionTypeString == null) {
            missionTypeString = "COMBAT";
        }
        missionType = MissionType.valueOf(missionTypeString);

        Crew lead = archive.getCrew(leadId);
        Crew reserve1 = archive.getCrew(reserve1Id);
        Crew reserve2 = null;

        if (reserve2Id != -1) {
            reserve2 = archive.getCrew(reserve2Id);
        }

        if (lead == null || reserve1 == null) {
            Toast.makeText(this, "Crew not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        combatManager = new CombatManager(lead, reserve1, reserve2, floor, missionType);
        updateUI();

        btnAttack.setOnClickListener(v -> handleAttack());
        btnSwap1.setOnClickListener(v -> handleSwap1());
        btnSwap2.setOnClickListener(v -> handleSwap2());
        btnUseMedKit.setOnClickListener(v -> handleMedKit());
        btnDefend.setOnClickListener(v -> handleDefend());
        btnSpecial.setOnClickListener(v -> handleSpecial());

        tvMissionDescription.setText(
                "Mission: " + missionType.name() +
                        "\n" + missionType.getBonusDescription()
        );
    }

    @SuppressLint("SetTextI18n")
    private void handleAttack() {
        StringBuilder log = new StringBuilder();

        log.append(combatManager.attack()).append("\n");
        animateHit(imgAlien);

        if (combatManager.isVictory()) {
            rewardSurvivors();

            String medKitRewardMessage = rewardMedKitIfNeeded();
            if (!medKitRewardMessage.isEmpty()) {
                log.append(medKitRewardMessage).append("\n");
            }

            SaveManager.save(this, archive);
            tvCombatLog.setText(log.toString());
            updateUI();
            showVictoryDialog();
            return;
        }

        log.append(combatManager.enemyTurn()).append("\n");
        animateHit(imgLead);
        flashScreen();

        String deathLog = combatManager.checkDeaths();
        if (!deathLog.isEmpty()) {
            log.append(deathLog);
        }

        sendDefeatedCrewToMedbay();

        if (combatManager.isGameOver()) {
            SaveManager.save(this, archive);
            tvCombatLog.setText(log + "\nGame Over.");
            showGameOverDialog();
            return;
        }

        SaveManager.save(this, archive);
        tvCombatLog.setText(log.toString());
        updateUI();
    }

    private void handleSwap1() {
        StringBuilder log = new StringBuilder();

        log.append(combatManager.swapToReserve1()).append("\n");
        log.append(combatManager.enemyTurn()).append("\n");

        animateHit(imgLead);
        flashScreen();

        String deathLog = combatManager.checkDeaths();
        if (!deathLog.isEmpty()) {
            log.append(deathLog);
        }

        sendDefeatedCrewToMedbay();

        if (combatManager.isGameOver()) {
            SaveManager.save(this, archive);
            tvCombatLog.setText(log + "\nGame Over.");
            showGameOverDialog();
            return;
        }

        SaveManager.save(this, archive);
        tvCombatLog.setText(log.toString());
        updateUI();
    }

    private void handleSwap2() {
        StringBuilder log = new StringBuilder();

        log.append(combatManager.swapToReserve2()).append("\n");
        log.append(combatManager.enemyTurn()).append("\n");

        animateHit(imgLead);
        flashScreen();

        String deathLog = combatManager.checkDeaths();
        if (!deathLog.isEmpty()) {
            log.append(deathLog);
        }

        sendDefeatedCrewToMedbay();

        if (combatManager.isGameOver()) {
            SaveManager.save(this, archive);
            tvCombatLog.setText(log + "\nGame Over.");
            showGameOverDialog();
            return;
        }

        SaveManager.save(this, archive);
        tvCombatLog.setText(log.toString());
        updateUI();
    }

    @SuppressLint("SetTextI18n")
    private void handleMedKit() {
        Crew lead = combatManager.getLead();

        if (medKits <= 0) {
            Toast.makeText(this, "No MedKits left", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lead != null) {
            lead.heal(30);
            medKits--;
            SaveManager.save(this, archive);
            tvCombatLog.setText(lead.getName() + " used a MedKit and recovered energy.");
            updateUI();
        }
    }

    @SuppressLint("SetTextI18n")
    private void handleDefend() {
        StringBuilder log = new StringBuilder();

        log.append(combatManager.defend()).append("\n");
        log.append(combatManager.enemyTurn()).append("\n");
        flashScreen();

        String deathLog = combatManager.checkDeaths();
        if (!deathLog.isEmpty()) {
            log.append(deathLog);
        }

        sendDefeatedCrewToMedbay();

        if (combatManager.isGameOver()) {
            SaveManager.save(this, archive);
            tvCombatLog.setText(log + "\nGame Over.");
            showGameOverDialog();
            return;
        }

        SaveManager.save(this, archive);
        tvCombatLog.setText(log.toString());
        updateUI();
    }

    @SuppressLint("SetTextI18n")
    private void handleSpecial() {
        StringBuilder log = new StringBuilder();

        log.append(combatManager.useSpecial()).append("\n");
        animateHit(imgAlien);

        if (combatManager.isVictory()) {
            rewardSurvivors();

            String medKitRewardMessage = rewardMedKitIfNeeded();
            if (!medKitRewardMessage.isEmpty()) {
                log.append(medKitRewardMessage).append("\n");
            }

            SaveManager.save(this, archive);
            tvCombatLog.setText(log.toString());
            updateUI();
            showVictoryDialog();
            return;
        }

        log.append(combatManager.enemyTurn()).append("\n");
        animateHit(imgLead);
        flashScreen();

        String deathLog = combatManager.checkDeaths();
        if (!deathLog.isEmpty()) {
            log.append(deathLog);
        }

        sendDefeatedCrewToMedbay();

        if (combatManager.isGameOver()) {
            SaveManager.save(this, archive);
            tvCombatLog.setText(log.toString() + "\nGame Over.");
            showGameOverDialog();
            return;
        }

        SaveManager.save(this, archive);
        tvCombatLog.setText(log.toString());
        updateUI();
    }

    private void rewardSurvivors() {
        Crew lead = combatManager.getLead();
        Crew reserve1 = combatManager.getReserve1();
        Crew reserve2 = combatManager.getReserve2();

        if (lead != null && lead.isAlive()) {
            lead.incrementKillCount();
            lead.gainExperience(50);
            lead.recordVictory();
        }

        if (reserve1 != null && reserve1.isAlive()) {
            reserve1.gainExperience(50);
            reserve1.recordVictory();
        }

        if (reserve2 != null && reserve2.isAlive()) {
            reserve2.gainExperience(50);
            reserve2.recordVictory();
        }
    }

    private void sendDefeatedCrewToMedbay() {
        for (Crew defeated : combatManager.getDefeatedCrew()) {
            defeated.recordDefeat();
            defeated.recordLostMission();
            defeated.sendToMedbay();
        }
        combatManager.clearDefeatedCrew();
    }

    private void returnToQuartersAndRestoreEnergy() {
        archive.restoreAllAvailableCrewEnergy();
        SaveManager.save(this, archive);
        finish();
    }

    private void showGameOverDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("All active crew members have fallen and were sent to Medbay.")
                .setPositiveButton("Return to Quarters", (dialog, which) -> {
                    archive.restoreAllAvailableCrewEnergy();
                    SaveManager.save(this, archive);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    @SuppressLint("SetTextI18n")
    private void showVictoryDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Victory")
                .setMessage("Alien defeated. Continue mission or return to Quarters?")
                .setPositiveButton("Continue", (dialog, which) -> {
                    floor++;

                    Crew lead = combatManager.getLead();
                    Crew reserve1 = combatManager.getReserve1();
                    Crew reserve2 = combatManager.getReserve2();

                    combatManager = new CombatManager(
                            lead,
                            reserve1,
                            reserve2,
                            floor,
                            missionType
                    );

                    tvCombatLog.setText("A new alien appears.");
                    updateUI();
                })
                .setNegativeButton("Return to Quarters", (dialog, which) -> returnToQuartersAndRestoreEnergy())
                .show();
    }

    @SuppressLint("SetTextI18n")
    private void updateUI() {
        tvFloor.setText("Mission Floor " + floor);

        if (combatManager.getAlien() != null) {
            imgAlien.setImageResource(combatManager.getAlien().getImageResId());
            tvAlienName.setText(
                    combatManager.getAlien().getName() + " HP: "
                            + combatManager.getAlien().getEnergy() + "/"
                            + combatManager.getAlien().getMaxEnergy()
            );
            progressAlien.setMax(combatManager.getAlien().getMaxEnergy());
            progressAlien.setProgress(combatManager.getAlien().getEnergy());
        }

        Crew lead = combatManager.getLead();
        if (lead != null) {
            tvLeadCrew.setText(
                    "Lead: " + lead.getName() + " (" + lead.getCrewClass() + ") HP: "
                            + lead.getEnergy() + "/" + lead.getMaxEnergy()
            );
            progressLead.setMax(lead.getMaxEnergy());
            progressLead.setProgress(lead.getEnergy());
            imgLead.setImageResource(lead.getImageResId());
        } else {
            tvLeadCrew.setText("Lead: None");
            progressLead.setMax(100);
            progressLead.setProgress(0);
        }

        Crew reserve1 = combatManager.getReserve1();
        Crew reserve2 = combatManager.getReserve2();

        StringBuilder reserveText = new StringBuilder();

        if (reserve1 != null) {
            reserveText.append("Reserve 1: ").append(reserve1.getName()).append(" (").append(reserve1.getCrewClass()).append(") HP: ")
                    .append(reserve1.getEnergy()).append("/").append(reserve1.getMaxEnergy());
        } else {
            reserveText.append("Reserve 1: None");
        }

        reserveText.append("\n");

        if (reserve2 != null) {
            reserveText.append("Reserve 2: ").append(reserve2.getName()).append(" (").append(reserve2.getCrewClass()).append(") HP: ")
                    .append(reserve2.getEnergy()).append("/").append(reserve2.getMaxEnergy());
        } else {
            reserveText.append("Reserve 2: None");
        }

        tvPartnerCrew.setText(reserveText.toString());
        tvMedKitCount.setText("MedKits: " + medKits);}

    private String rewardMedKitIfNeeded() {
        if (floor % 3 == 0) {
            medKits++;
            return "You found a MedKit for clearing floor " + floor + "!";
        }
        return "";
    }


    private void flashScreen() {
        View root = findViewById(R.id.rootMission);
        root.setBackgroundColor(Color.RED);
        root.postDelayed(() -> root.setBackgroundColor(Color.TRANSPARENT), 150);
    }

    private void animateHit(View view) {
        view.animate()
                .translationX(20)
                .setDuration(50)
                .withEndAction(() -> view.animate().translationX(0).setDuration(50))
                .start();
    }
}