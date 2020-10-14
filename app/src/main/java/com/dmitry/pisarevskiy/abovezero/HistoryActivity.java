package com.dmitry.pisarevskiy.abovezero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dmitry.pisarevskiy.abovezero.database.App;
import com.dmitry.pisarevskiy.abovezero.database.RequestDao;
import com.dmitry.pisarevskiy.abovezero.database.RequestSource;

public class HistoryActivity extends AppCompatActivity {
    private final SingleTon singleTon = SingleTon.getInstance();
    private RVAdapterHistory rvAdapterHistory;
    private RecyclerView rvHistory;
    private RequestSource requestSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvHistory =findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        RequestDao requestDao = App
                .getInstance()
                .getRequestDao();
        requestSource = new RequestSource(requestDao);
        rvAdapterHistory = new RVAdapterHistory(requestSource.getRequests());
        rvHistory.setAdapter(rvAdapterHistory);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        menu.findItem(R.id.newCity).setVisible(false);
        final MenuItem search = menu.findItem(R.id.search);
        search.setVisible(true);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rvAdapterHistory.filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}