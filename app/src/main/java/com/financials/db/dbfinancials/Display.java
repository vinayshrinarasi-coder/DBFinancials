package com.financials.db.dbfinancials;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Display extends AppCompatActivity {
    private SearchView searchView;
    Spinner filter;
    RecyclerView bondList;
    String orderBy = "dateOfMaturity";
    String asc_dsc = "asc";
    private PoliciesAdapter mAdapter;
    private final List<Policy> policiesList = new ArrayList<>();
    android.widget.ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        init();
    }

    private void init() {
        policiesList.clear();
        progressBar = findViewById(R.id.progressBar);
        mAdapter = new PoliciesAdapter(policiesList);
        runSQL();
        bondList = findViewById(R.id.bondList);
        bondList.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        bondList.setLayoutManager(mLayoutManager);
        bondList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        bondList.setItemAnimator(new DefaultItemAnimator());
        bondList.setAdapter(mAdapter);
        bondList.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), bondList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Policy poly = mAdapter.getList().get(position);
                Toast.makeText(getApplicationContext(), poly.getCertificateNumber() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, final int position) {
                final Policy poly = mAdapter.getList().get(position);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Display.this);
                alertDialogBuilder.setMessage("Select an option..");
                alertDialogBuilder.setPositiveButton("Delete",
                        (arg0, arg1) -> {
                            arg0.dismiss();
                            DatabaseHandler db = new DatabaseHandler(Display.this);
                            db.deletePolicy(poly);
                            runSQL();
                        });

                alertDialogBuilder.setNegativeButton("Edit",
                        (dialog, which) -> {
                            dialog.dismiss();
                            Intent in = new Intent(getApplicationContext(), EditPolicy.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            in.putExtra("poly", new Gson().toJson(poly));
                            startActivity(in);
                            finish();
                        });

                alertDialogBuilder.setNeutralButton("View",
                        (dialog, which) -> {
                            dialog.dismiss();
                            Intent in = new Intent(getApplicationContext(), ViewPolicy.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            in.putExtra("poly", new Gson().toJson(poly));
                            startActivity(in);
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }));
    }

    private void runSQL() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        DatabaseHandler db = new DatabaseHandler(this);
        List<Policy> contacts = db.getPoliciesSQL("select * from " + DatabaseHandler.TABLE_CONTACTS + " order by " + orderBy + " " + asc_dsc);
        policiesList.clear();
        policiesList.addAll(contacts);
        Log.e("sql = ", "select * from " + DatabaseHandler.TABLE_CONTACTS + " order by " + orderBy + " " + asc_dsc);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mAdapter.getFilter().filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    mAdapter.getFilter().filter(query);
                    return false;
                }
            });
        }

        filter = (Spinner) menu.findItem(R.id.filter).getActionView();
        if (filter != null) {
            final List<String> filterList = new ArrayList<>();
            final List<String> filterListMap = new ArrayList<>();
            filterList.add("--Filter--");
            filterListMap.add("");
            filterList.add("Deposit Date");
            filterListMap.add("dateOfDeposit");
            filterList.add("Deposit Amt");
            filterListMap.add("depositAmount");
            filterList.add("Maturity Date");
            filterListMap.add("dateOfMaturity");
            filterList.add("Maturity Amt");
            filterListMap.add("maturityAmount");
            filterList.add("Interest");
            filterListMap.add("interest");
            filterList.add("Interest Rate");
            filterListMap.add("rateOfInterest");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filterList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            filter.setAdapter(adapter);
            filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i != 0) {
                        orderBy = filterListMap.get(i);
                        runSQL();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort) {
            asc_dsc = asc_dsc.equalsIgnoreCase("asc") ? "desc" : "asc";
            runSQL();
            return true;
        } else if (id == R.id.add) {
            startActivity(new Intent(getApplicationContext(), AddNewPolicy.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}