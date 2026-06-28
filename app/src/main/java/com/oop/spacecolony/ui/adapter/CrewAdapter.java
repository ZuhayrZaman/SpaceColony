package com.oop.spacecolony.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oop.spacecolony.R;
import com.oop.spacecolony.model.Crew;

import java.util.List;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewViewHolder> {

    private final List<Crew> crewList;

    public CrewAdapter(List<Crew> crewList) {
        this.crewList = crewList;
    }

    @NonNull
    @Override
    public CrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_crew, parent, false);
        return new CrewViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CrewViewHolder holder, int position) {
        Crew crew = crewList.get(position);

        holder.imgCrew.setImageResource(crew.getImageResId());
        holder.tvCrewName.setText(crew.getName());
        holder.tvCrewClass.setText(crew.getCrewClass());
        String statusText;
        if (crew.isInMedbay()) {
            statusText = "Status: In Medbay (" + crew.getMedbayMissionsRemaining() + " mission(s) remaining)";
        } else {
            statusText = "Status: Ready";
        }

        holder.tvCrewStats.setText(
                "HP: " + crew.getEnergy() + "/" + crew.getMaxEnergy()
                        + " | Skill: " + crew.getSkill()
                        + " | XP: " + crew.getExperience()
                        + "\nMissions: " + crew.getMissionsPlayed()
                        + " | Wins: " + crew.getVictories()
                        + " | Simulator: " + crew.getTrainingSessions()
                        + "\n" + statusText
        );
    }

    @Override
    public int getItemCount() {
        return crewList.size();
    }

    static class CrewViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCrew;
        TextView tvCrewName, tvCrewClass, tvCrewStats;

        public CrewViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCrew = itemView.findViewById(R.id.imgCrew);
            tvCrewName = itemView.findViewById(R.id.tvCrewName);
            tvCrewClass = itemView.findViewById(R.id.tvCrewClass);
            tvCrewStats = itemView.findViewById(R.id.tvCrewStats);
        }
    }
}