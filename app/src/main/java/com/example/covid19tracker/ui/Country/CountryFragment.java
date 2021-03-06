package com.example.covid19tracker.ui.Country;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid19tracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CountryFragment extends Fragment {

    RecyclerView rvCovidCountry;
    ProgressBar progressBar;
    List<CovidCountry> covidCountries;
    TextView totalCountry;
    covidCountryAdaptor covidCountryAdaptor;
    private static final String Tag = CountryFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_country, container, false);
        setHasOptionsMenu(true);
        rvCovidCountry = root.findViewById(R.id.reCovidCountry);
        progressBar = root.findViewById(R.id.progress_circular_Country);
        rvCovidCountry.setLayoutManager(new LinearLayoutManager(getActivity()));
        covidCountries = new ArrayList<>();
        //call Volley method
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCovidCountry.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.line_devider));
        rvCovidCountry.addItemDecoration(dividerItemDecoration);
        getDataFromServerSort(true, false, false);
        return root;
    }

    private void showRecyclerView() {
        covidCountryAdaptor = new covidCountryAdaptor(covidCountries, getContext());
        rvCovidCountry.setAdapter(covidCountryAdaptor);
        ItemClickSupport.addTo(rvCovidCountry).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedCovidCountry(covidCountries.get(position));
            }
        });
    }

    private void showSelectedCovidCountry(CovidCountry covidCountry) {
        Intent intent = new Intent(getActivity(), CovidCountryDetails.class);
        intent.putExtra("EXTRA_COVID", covidCountry);
        Log.e("test**", covidCountry.getmActive());
        startActivity(intent);

    }

    private String getDate(Long milliSecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss aaa");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSecond);
        return simpleDateFormat.format(calendar.getTime());
    }

    private void getDataFromServerSort(final boolean sortingCases, final boolean sortingDeaths, final boolean sortingRecovered) {
        String url = "https://corona.lmao.ninja/v2/countries";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                if (response != null) {
                    Log.e(Tag, "OnResponse" + response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            JSONObject countryInfo = data.getJSONObject("countryInfo");
                            covidCountries.add(new CovidCountry(data.getString("country"), data.getInt("cases"),
                                    data.getString("todayCases"), data.getString("deaths"), data.getString("todayDeaths"),
                                    data.getString("recovered"), data.getString("critical"), data.getString("active"),
                                    countryInfo.getString("flag"), getDate(data.getLong("updated"))
                            ));
                        }
                        sortCases(sortingCases);
                        sortDeaths(sortingDeaths);
                        sortRecovered(sortingRecovered);
                        getActivity().setTitle(jsonArray.length() + " countries");
                        showRecyclerView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e(Tag, "onResponse" + error);
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.country_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(getActivity());
        searchView.setQueryHint("Search...");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (covidCountryAdaptor != null) {
                    covidCountryAdaptor.getFilter().filter(newText);
                }
                return true;
            }
        });
        searchItem.setActionView(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_alpha:
                Toast.makeText(getContext(), "Sort Alphabetically", Toast.LENGTH_SHORT).show();
                covidCountries.clear();
                progressBar.setVisibility(View.VISIBLE);
                getDataFromServerSort(false, false, false);
                covidCountryAdaptor.notifyDataSetChanged();
                break;
            case R.id.sort_by_Cases:
                Toast.makeText(getContext(), "Sort by Cases", Toast.LENGTH_SHORT).show();
                covidCountries.clear();
                progressBar.setVisibility(View.VISIBLE);
                getDataFromServerSort(true, false, false);
                covidCountryAdaptor.notifyDataSetChanged();
                break;
            case R.id.sort_by_deaths:
                Toast.makeText(getContext(), "Sort by Deaths", Toast.LENGTH_SHORT).show();
                covidCountries.clear();
                progressBar.setVisibility(View.VISIBLE);
                getDataFromServerSort(false, true, false);
                covidCountryAdaptor.notifyDataSetChanged();
                break;
            case R.id.sort_by_recovered:
                Toast.makeText(getContext(), "Sort by Recovered", Toast.LENGTH_SHORT).show();
                covidCountries.clear();
                progressBar.setVisibility(View.VISIBLE);
                getDataFromServerSort(false, false, true);
                covidCountryAdaptor.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortCases(boolean sorting) {
        if (sorting) {
            Collections.sort(covidCountries, new Comparator<CovidCountry>() {
                @Override
                public int compare(CovidCountry o1, CovidCountry o2) {
                    if (o1.getmCases() > o2.getmCases()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    private void sortDeaths(boolean sorting) {
        if (sorting) {
            Collections.sort(covidCountries, new Comparator<CovidCountry>() {
                @Override
                public int compare(CovidCountry o1, CovidCountry o2) {
                    if (Integer.parseInt(o1.mDeaths) >= Integer.parseInt(o2.mDeaths)) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    private void sortRecovered(boolean sorting) {
        if (sorting) {
            Collections.sort(covidCountries, new Comparator<CovidCountry>() {
                @Override
                public int compare(CovidCountry o1, CovidCountry o2) {
                    if (Integer.parseInt(o1.mRecovered) > Integer.parseInt(o2.mRecovered)) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        }
    }
}
