package com.mirea.kt.ribo.fragments.update;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.mirea.kt.ribo.R;
import com.mirea.kt.ribo.databinding.FragmentCarRepairUpdateBinding;
import com.mirea.kt.ribo.room.CarRepair;
import com.mirea.kt.ribo.room.CarRepairViewModel;

import java.util.Objects;

public class CarRepairUpdateFragment extends Fragment {
    private FragmentCarRepairUpdateBinding binding;

    private CarRepairViewModel mCarRepairViewModel;

    private CarRepair args;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCarRepairUpdateBinding.inflate(inflater);

        mCarRepairViewModel = new ViewModelProvider(this).get(CarRepairViewModel.class);

        if (getArguments() != null) {
            args = CarRepairUpdateFragmentArgs.fromBundle(getArguments()).getCurrentCarRepair();
        }

        binding.repairTitleNumberU.setText(String.valueOf(args.id));
        binding.carBrandTextFieldU.setText(args.carBrand);
        binding.carModelTextFieldU.setText(args.carModel);
        binding.carNumberTextFieldU.setText(args.carNumber);
        binding.repairTypeTextFieldU.setText(args.repairType);
        binding.masterNameTextFieldU.setText(args.masterName);

        binding.updateCarRepairButton.setOnClickListener(this::updateItem);
        binding.backTextButtonU.setOnClickListener(this::back);
        binding.deleteCarRepairFloatingActionButtonU.setOnClickListener(this::deleteCarRepair);

        return binding.getRoot();
    }

    private void updateItem(View v) {
        String carBrand = Objects.requireNonNull(binding.carBrandTextFieldU.getText()).toString();
        String carModel = Objects.requireNonNull(binding.carModelTextFieldU.getText()).toString();
        String carNumber = Objects.requireNonNull(binding.carNumberTextFieldU.getText()).toString();
        String repairType = Objects.requireNonNull(binding.repairTypeTextFieldU.getText()).toString();
        String masterName = Objects.requireNonNull(binding.masterNameTextFieldU.getText()).toString();

        if (inputCheck(carBrand, carModel, carNumber, repairType, masterName)) {
            CarRepair updatedCarRepair = new CarRepair(args.id, carBrand, carModel, carNumber, repairType, masterName);
            mCarRepairViewModel.updateCarRepair(updatedCarRepair);
            Toast.makeText(requireContext(), R.string.update_repair_success, Toast.LENGTH_LONG).show();
            Navigation.findNavController(v).navigate(R.id.action_carRepairUpdateFragment_to_carRepairListFragment);
        } else Toast.makeText(requireContext(), R.string.add_failed, Toast.LENGTH_LONG).show();
    }

    private Boolean inputCheck(String carBrand, String carModel, String carNumber, String repairType, String masterName) {
        return !(TextUtils.isEmpty(carBrand) || TextUtils.isEmpty(carModel) || TextUtils.isEmpty(carNumber) || TextUtils.isEmpty(repairType) || TextUtils.isEmpty(masterName));
    }

    private void back(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.back_title);
        builder.setMessage(R.string.back_confirm_not_to_add);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> Navigation.findNavController(v).navigate(R.id.action_carRepairUpdateFragment_to_carRepairListFragment));
        builder.setNegativeButton(R.string.no, (dialog, which) -> {});

        builder.create().show();
    }

    private void deleteCarRepair(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.delete_title);
        builder.setMessage(R.string.back_confirm_to_delete);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            mCarRepairViewModel.deleteCarRepair(args);
            Navigation.findNavController(v).navigate(R.id.action_carRepairUpdateFragment_to_carRepairListFragment);
            Toast.makeText(requireContext(), R.string.update_delete_success, Toast.LENGTH_LONG).show();
        });

        builder.setNegativeButton(R.string.no, (dialog, which) -> {});
        builder.create().show();
    }
}