package com.mirea.kt.ribo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class LoginDialog extends DialogFragment {
    String title,task;

    public LoginDialog(String title, String task) {
        this.title = title;
        this.task = task;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(task)
                .setIcon(R.mipmap.logo_repair_app_round)
                .setNeutralButton("Ок",(dialog, which) -> {
                    startActivity(new Intent(getContext(),MainActivity.class));
                    dialog.cancel();
                });
        return builder.create();
    }
}

