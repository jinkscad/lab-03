package com.example.listycitylab3;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity
        implements AddCityFragment.AddCityDialogListener {

    private ArrayList<City> dataList;
    private ListView cityList;
    private CityArrayAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] cities = {"Edmonton", "Vancouver", "Toronto"};
        String[] provinces = {"AB", "BC", "ON"};

        dataList = new ArrayList<>();
        for (int i = 0; i < cities.length; i++) {
            dataList.add(new City(cities[i], provinces[i]));
        }

        cityList = findViewById(R.id.city_list);
        cityAdapter = new CityArrayAdapter(this, dataList);
        cityList.setAdapter(cityAdapter);

        // ADD new city
        FloatingActionButton addBtn = findViewById(R.id.button_add_city);
        addBtn.setOnClickListener(v ->
                AddCityFragment.newInstance(null, -1)
                        .show(getSupportFragmentManager(), "addCity"));

        // EDIT existing city on item click
        cityList.setOnItemClickListener((parent, view, position, id) -> {
            City clicked = dataList.get(position);
            AddCityFragment.newInstance(clicked, position)
                    .show(getSupportFragmentManager(), "editCity");
        });
    }

    // ----- Callbacks from AddCityFragment -----
    @Override
    public void onCityAdded(City city) {
        dataList.add(0, city);                 // add to top (or use add(city))
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCityEdited(int position, City updatedCity) {
        // Option A: replace the item
        dataList.set(position, updatedCity);

        // Option B: mutate original object instead (if you prefer)
        // City original = dataList.get(position);
        // original.setName(updatedCity.getName());
        // original.setProvince(updatedCity.getProvince());

        cityAdapter.notifyDataSetChanged();
    }
}
