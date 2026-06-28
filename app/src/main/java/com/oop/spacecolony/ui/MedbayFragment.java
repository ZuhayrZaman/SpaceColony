package com.oop.spacecolony.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oop.spacecolony.R;
import com.oop.spacecolony.logic.SaveManager;
import com.oop.spacecolony.model.ColonyArchive;
import com.oop.spacecolony.ui.adapter.CrewAdapter;

public class MedbayFragment extends Fragment {

    public MedbayFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medbay, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerMedbay);
        ColonyArchive archive = SaveManager.load(requireContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new CrewAdapter(archive.getMedbayCrew()));

        return view;
    }
}