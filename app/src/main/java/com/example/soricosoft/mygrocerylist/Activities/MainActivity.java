package com.example.soricosoft.mygrocerylist.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.soricosoft.mygrocerylist.Data.DatabaseHandler;
import com.example.soricosoft.mygrocerylist.Model.Grocery;
import com.example.soricosoft.mygrocerylist.R;
import com.example.soricosoft.mygrocerylist.UI.RecyclerViewAdapter;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import static java.time.LocalDateTime.now;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    private DatabaseHandler databaseHandler;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem;
    private EditText quantity;
    private Button saveButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> {
            createPopupDialog(MainActivity.this);

        });


        databaseHandler = new DatabaseHandler(this);
        recyclerView = findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        List<Grocery> groceries = databaseHandler.getGroceries();
        Log.d(TAG, "onCreate: nr of groceries " + groceries.size());
        if (groceries.size() == 0) {
            Toast.makeText(this, R.string.addItems, Toast.LENGTH_LONG).show();
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerViewAdapter.setAllGroceries(databaseHandler.getGroceries());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void createPopupDialog(Context context) {
        dialogBuilder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        groceryItem = view.findViewById(R.id.groceryItem);
        quantity = view.findViewById(R.id.groceryQty);
        saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this::saveGroceryToDB);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void saveGroceryToDB(View view) {
        Grocery grocery = new Grocery();

        String item = groceryItem.getText().toString();
        String qty = quantity.getText().toString();

        if (!item.isEmpty() && !qty.isEmpty()) {
            grocery.setName(item);
            grocery.setQuantity(qty);
            grocery.setDateItemAdded(LocalDateTime.now());
            databaseHandler.addGroceryItem(grocery);
            recyclerViewAdapter.addGrocery(grocery);
        }
        Snackbar.make(view, "grocery item saved!!", Snackbar.LENGTH_LONG).show();
        dialog.dismiss();
//        Log.d(TAG, "saveGroceryToDB: item added:" + databaseHandler.getGroceriesCount());
    }
}
