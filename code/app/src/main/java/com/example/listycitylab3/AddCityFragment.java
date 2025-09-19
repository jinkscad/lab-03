package com.example.listycitylab3;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    public interface AddCityDialogListener {
        void onCityAdded(City city);
        void onCityEdited(int position, City updatedCity);
    }

    private AddCityDialogListener listener;

    // ----- Preferred way: factory with Bundle -----
    public static AddCityFragment newInstance(@Nullable City city, int position) {
        AddCityFragment f = new AddCityFragment();
        Bundle args = new Bundle();
        if (city != null) {
            args.putSerializable("city", city);
            args.putInt("position", position);
        }
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_add_city, null);

        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        // If we were given a City, we’re in EDIT mode → prefill
        Bundle args = getArguments();
        City original = null;
        int position = -1;
        if (args != null && args.containsKey("city")) {
            original = (City) args.getSerializable("city");
            position = args.getInt("position", -1);
            if (original != null) {
                editCityName.setText(original.getName());
                editProvinceName.setText(original.getProvince());
            }
        }

        final City cityToEdit = original; // effectively final for lambda
        final int pos = position;
        String title = (cityToEdit == null) ? "Add city" : "Edit city";
        String positive = (cityToEdit == null) ? "Add" : "Save";

        return new AlertDialog.Builder(requireContext())
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(positive, (dialog, which) -> {
                    String name = editCityName.getText().toString().trim();
                    String prov = editProvinceName.getText().toString().trim();

                    if (cityToEdit == null) {
                        // ADD mode: hand a new City back
                        listener.onCityAdded(new City(name, prov));
                    } else {
                        // EDIT mode: hand updated values back with position
                        listener.onCityEdited(pos, new City(name, prov));
                    }
                })
                .create();
    }
}
