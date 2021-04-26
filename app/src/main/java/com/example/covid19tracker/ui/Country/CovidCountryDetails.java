package com.example.covid19tracker.ui.Country;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.covid19tracker.R;

public class CovidCountryDetails extends AppCompatActivity {
    private TextView tvDetailsCountryName, tvDetailsTotalCases, tvDetailsTodayCases, tvDetailsTotalDeaths, tvDetailsTodayDeaths, tvDetailsTotalRecovery, tvDetailsTotalActive, tvDetailsTotalCritical, lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_country_details);
        tvDetailsCountryName = findViewById(R.id.tvDetailsCountryName);
        tvDetailsTotalCases = findViewById(R.id.tvDetailsTotalCases);
        tvDetailsTodayCases = findViewById(R.id.tvDetailsTodayCases);
        tvDetailsTotalDeaths = findViewById(R.id.tvDetailsTotalDeaths);
        tvDetailsTodayDeaths = findViewById(R.id.tvDetailsTodayDeaths);
        tvDetailsTotalRecovery = findViewById(R.id.tvDetailsTotalRecovery);
        tvDetailsTotalActive = findViewById(R.id.tvDetailsTotalActive);
        tvDetailsTotalCritical = findViewById(R.id.tvDetailsTotalCritical);
        lastUpdate = findViewById(R.id.tvDetailsLastUpdate);

        CovidCountry covidCountry = getIntent().getParcelableExtra("EXTRA_COVID");

        tvDetailsCountryName.setText(covidCountry.getmCovidCountry());
        tvDetailsTotalCases.setText(Integer.toString(covidCountry.getmCases()));
        tvDetailsTodayCases.setText(covidCountry.getmTodayCases());
        tvDetailsTotalDeaths.setText(covidCountry.getmDeaths());
        tvDetailsTodayDeaths.setText(covidCountry.getmTodayDeaths());
        tvDetailsTotalRecovery.setText(covidCountry.getmRecovered());
        tvDetailsTotalActive.setText(covidCountry.getmActive());
        tvDetailsTotalCritical.setText(covidCountry.getmCritical());
        lastUpdate.setText("Last updated\n"+covidCountry.getLastUpdate());
    }
}
