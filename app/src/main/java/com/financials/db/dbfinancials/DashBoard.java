package com.financials.db.dbfinancials;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "DashBoard";
    Button display;
    double cashAtBank, intrst, bankIntrst, licIntrst;
    TextView cashAtBankView, intrstView, totalView, bankIntrstView, licIntrstView;
    DrawerLayout drawer;
    android.widget.ProgressBar progressBar;

    private final ActivityResultLauncher<Intent> importLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        handleRestore(uri);
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> exportLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        handleBackup(uri);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_dash_board);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.coordinator_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appBarLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                    setEnabled(true);
                }
            }
        });

        init();
    }

    private void init() {
        cashAtBankView = findViewById(R.id.cashAtBank);
        intrstView = findViewById(R.id.intrst);
        bankIntrstView = findViewById(R.id.bankInterest);
        licIntrstView = findViewById(R.id.licInterest);
        totalView = findViewById(R.id.total);
        progressBar = findViewById(R.id.progressBar);

        display = findViewById(R.id.display);
        display.setOnClickListener(view -> UiUtils.animateClick(view, () -> 
                startActivity(new Intent(getApplicationContext(), Display.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))));
        
        // Hide maturity rows initially
        findViewById(R.id.row1).setVisibility(View.GONE);
        findViewById(R.id.row2).setVisibility(View.GONE);
        findViewById(R.id.row3).setVisibility(View.GONE);
        findViewById(R.id.row4).setVisibility(View.GONE);
        
        runSQL();
    }

    private void runSQL() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        Locale locale = new Locale("en", "IN");
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(locale);
        dfs.setCurrencySymbol("\u20B9");
        decimalFormat.setDecimalFormatSymbols(dfs);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        List<Policy> contacts = db.getPoliciesSQL("select * from " + DatabaseHandler.TABLE_CONTACTS + " order by dateOfMaturity asc");
        
        cashAtBank = 0;
        intrst = 0;
        bankIntrst = 0;
        licIntrst = 0;
        for (int i = 0; i < contacts.size(); i++) {
            Policy p = contacts.get(i);
            cashAtBank += p.getDepositAmount();
            intrst += p.getInterest();
            if ("LIC".equalsIgnoreCase(p.getCategory())) {
                licIntrst += p.getInterest();
            } else {
                bankIntrst += p.getInterest();
            }
            if (i < 4) {
                int rowId = getResources().getIdentifier("row" + (i + 1), "id", getPackageName());
                int dateId = getResources().getIdentifier("date" + (i + 1), "id", getPackageName());
                int holderId = getResources().getIdentifier("h" + (i + 1), "id", getPackageName());
                int intId = getResources().getIdentifier("int" + (i + 1), "id", getPackageName());
                int totalId = getResources().getIdentifier("total" + (i + 1), "id", getPackageName());

                if (rowId != 0) findViewById(rowId).setVisibility(View.VISIBLE);
                if (dateId != 0) ((TextView) findViewById(dateId)).setText(p.getReadableDateOfMaturity());
                if (holderId != 0) ((TextView) findViewById(holderId)).setText(p.getHolder());
                
                String intStr = decimalFormat.format(p.getInterest());
                if (intId != 0) ((TextView) findViewById(intId)).setText(intStr.substring(0, intStr.length() - 3));
                
                String totalStr = decimalFormat.format(p.getMaturityAmount());
                if (totalId != 0) ((TextView) findViewById(totalId)).setText(totalStr.substring(0, totalStr.length() - 3));
            }
        }

        String cashStr = decimalFormat.format(cashAtBank);
        cashAtBankView.setText(" " + cashStr.substring(0, cashStr.length() - 3));
        String intrstStr = decimalFormat.format(intrst);
        intrstView.setText(" " + intrstStr.substring(0, intrstStr.length() - 3));
        
        String bankIntStr = decimalFormat.format(bankIntrst);
        bankIntrstView.setText(" " + bankIntStr.substring(0, bankIntStr.length() - 3));
        
        String licIntStr = decimalFormat.format(licIntrst);
        licIntrstView.setText(" " + licIntStr.substring(0, licIntStr.length() - 3));

        String totalFinalStr = decimalFormat.format(cashAtBank + intrst);
        totalView.setText(" " + totalFinalStr.substring(0, totalFinalStr.length() - 3));
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        runSQL();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_backup) {
            exportBackup();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_analysis) {
            startActivity(new Intent(this, AnalysisActivity.class));
        } else if (id == R.id.nav_backup) {
            exportBackup();
        } else if (id == R.id.nav_restore) {
            importRestore();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void exportBackup() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "PoliciesBackup.txt");
        exportLauncher.launch(intent);
    }

    private void importRestore() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        importLauncher.launch(intent);
    }

    private void handleBackup(Uri uri) {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        try (OutputStream os = getContentResolver().openOutputStream(uri);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            DatabaseHandler db = new DatabaseHandler(this);
            List<Policy> policies = db.getAllPolicies();
            Type listType = new TypeToken<List<Policy>>() {}.getType();
            String json = new Gson().toJson(policies, listType);
            writer.write(json);
            writer.flush();
            if (progressBar != null) progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Backup saved successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            if (progressBar != null) progressBar.setVisibility(View.GONE);
            Log.e(TAG, "Backup failed", e);
            Toast.makeText(this, "Backup failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void handleRestore(Uri uri) {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        try (InputStream is = getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Type listType = new TypeToken<List<Policy>>() {}.getType();
            List<Policy> policies = new Gson().fromJson(sb.toString(), listType);
            if (policies != null) {
                DatabaseHandler db = new DatabaseHandler(this);
                db.deleteTable();
                for (Policy p : policies) {
                    db.addPolicy(p);
                }
                runSQL();
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Policies restored successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            if (progressBar != null) progressBar.setVisibility(View.GONE);
            Log.e(TAG, "Restore failed", e);
            Toast.makeText(this, "Restore failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}