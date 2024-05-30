package com.mirea.kt.ribo.fragments.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mirea.kt.ribo.R;
import com.mirea.kt.ribo.databinding.FragmentCarRepairListBinding;
import com.mirea.kt.ribo.room.CarRepair;
import com.mirea.kt.ribo.room.CarRepairViewModel;

public class CarRepairListFragment extends Fragment {

    SharedPreferences sharedPreferences;
    private static final String KEY = "on_boarding_completed";

    CarRepairListAdapter carRepairListAdapter;

    private CarRepairViewModel mCarRepairViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.mirea.kt.ribo.databinding.FragmentCarRepairListBinding binding = FragmentCarRepairListBinding.inflate(inflater);

        carRepairListAdapter = new CarRepairListAdapter();
        binding.carRepairList.setAdapter(carRepairListAdapter);
        binding.carRepairList.setLayoutManager(new LinearLayoutManager(requireContext()));

        mCarRepairViewModel = new ViewModelProvider(this).get(CarRepairViewModel.class);
        mCarRepairViewModel.readAllData.observe(getViewLifecycleOwner(), carRepairListAdapter::setData);

        firstTimeCheck();

        binding.searchCarRepair.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) searchDatabase(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query != null) searchDatabase(query);
                return true;
            }
        });

        binding.addCarRepairFloatingActionButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_carRepairListFragment_to_carRepairAddFragment));

        return binding.getRoot();
    }

    void firstTimeCheck() {
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        boolean completed = sharedPreferences.getBoolean(KEY, false);
        if (!completed) {
            CarRepair defaultCarRepair = new CarRepair(0, "BMW", "M3 E46", "11N2005Y", "Tech. Inspection", "Gurovskiy I.D.");
            mCarRepairViewModel.addCarRepair(defaultCarRepair);
            completeOnBoardingProcess();
        }
    }

    void completeOnBoardingProcess() {
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY, true).apply();
    }

    private void searchDatabase(String query) {
        String searchQuery = "%" + query + "%";
        mCarRepairViewModel.searchDatabase(searchQuery).observe(this, terms -> carRepairListAdapter.setData(terms));
    }
}