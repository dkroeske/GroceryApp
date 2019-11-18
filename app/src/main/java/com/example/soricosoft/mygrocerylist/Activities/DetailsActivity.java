package com.example.soricosoft.mygrocerylist.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.soricosoft.mygrocerylist.Model.Grocery;
import com.example.soricosoft.mygrocerylist.R;

public class DetailsActivity extends AppCompatActivity {
    private TextView itemName;
    private TextView itemQuantity;
    private TextView itemDateAdded;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemName = findViewById(R.id.itemNameDetails);
        itemQuantity = findViewById(R.id.itemQuatityDetails);
        itemDateAdded = findViewById(R.id.dateAddedDetails);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            Grocery grocery = bundle.getParcelable("grocery");

            itemName.setText(grocery.getName());
            itemQuantity.setText(grocery.getQuantity());
            itemDateAdded.setText(grocery.getDateItemAdded().toString());
            itemId = grocery.getId();
        }

    }
}
