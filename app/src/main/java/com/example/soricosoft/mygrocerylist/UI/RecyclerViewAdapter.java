package com.example.soricosoft.mygrocerylist.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.soricosoft.mygrocerylist.Activities.DetailsActivity;
import com.example.soricosoft.mygrocerylist.Data.DatabaseHandler;
import com.example.soricosoft.mygrocerylist.Model.Grocery;
import com.example.soricosoft.mygrocerylist.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Paul de Mast on 29-Oct-17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Grocery> groceryList;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
        this.groceryList = new ArrayList<>();
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row,parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        Grocery grocery = groceryList.get(position);

        holder.groceryItemName.setText(grocery.getName());
        holder.quantity.setText(grocery.getQuantity());
        holder.dateAdded.setText(grocery.getDateItemAdded().toString());
    }

    public void addGrocery(Grocery grocery) {
        groceryList.add(grocery);
        Collections.sort(groceryList, (grocery1, grocery2) ->
            grocery2.getDateItemAdded().compareTo(grocery1.getDateItemAdded()));
        int index = groceryList.indexOf(grocery);
        notifyItemInserted(index);
    }

    public void setAllGroceries(Collection<Grocery> groceries) {
        groceryList.clear();
        groceryList.addAll(groceries);
        Collections.sort(groceryList, (grocery1, grocery2) ->
                grocery2.getDateItemAdded().compareTo(grocery1.getDateItemAdded()));

        notifyDataSetChanged();
    }

    public void updateGrocery(Grocery grocery) {
        DatabaseHandler db = new DatabaseHandler(context);

        db.updateGrocery(grocery);
        int index = groceryList.indexOf(grocery);
        if (index >= 0) {
            notifyItemChanged(index, grocery);
        }
    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView groceryItemName;
        public TextView quantity;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;

        public int id;


        public ViewHolder(View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            groceryItemName = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            dateAdded = itemView.findViewById(R.id.dateAdded);

            editButton = itemView.findViewById(R.id.editGroceryButton);
            deleteButton = itemView.findViewById(R.id.deleteGroceryButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            itemView.setOnClickListener((View v) -> {
                // TODO: 29-Oct-17 goto next screen
                int position = getAdapterPosition();

                Grocery grocery = groceryList.get(position);

                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("grocery", grocery);
                context.startActivity(intent);
            });
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.editGroceryButton:
                    int pos = getAdapterPosition();
                    Grocery grocery = groceryList.get(pos);
                    showChangeGroceryDialog(grocery);

                    break;
                case R.id.deleteGroceryButton:
                    pos = getAdapterPosition();
                    int groceryId = groceryList.get(pos).getId();

                    showDeleteGroceryDialog(groceryId);
                    break;
            }

        }

        private void showChangeGroceryDialog(final Grocery grocery) {

            // TODO: 22-Nov-17 : aparte class voor het deze Dialog
            AlertDialog.Builder alertDialogBuilder;
            final AlertDialog alertDialog;
            LayoutInflater layoutInflater;

            alertDialogBuilder = new AlertDialog.Builder(context);
            layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.popup, null);

            final TextView editTitle = view.findViewById(R.id.groceryTitle);
            editTitle.setText("Edit grocery:");
            final EditText groceryItem = view.findViewById(R.id.groceryItem);
            groceryItem.setText(grocery.getName());
            final EditText groceryQty = view.findViewById(R.id.groceryQty);
            groceryQty.setText(grocery.getQuantity());
            Button saveButton = view.findViewById(R.id.saveButton);

            alertDialogBuilder.setView(view);
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            saveButton.setOnClickListener((View view1) -> {
                if (!groceryItem.getText().toString().isEmpty() && !groceryQty.getText().toString().isEmpty()) {
                    // update grocery item:
                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(groceryQty.getText().toString());
                    updateGrocery(grocery);
                } else {
                    Snackbar.make(view1, "Add grocery and quantity", Snackbar.LENGTH_LONG);
                }
                alertDialog.dismiss();
            });
        }

        private void showDeleteGroceryDialog(final int id) {
            AlertDialog.Builder alertDialogBuilder;
            final AlertDialog alertDialog;
            LayoutInflater layoutInflater;

            alertDialogBuilder = new AlertDialog.Builder(context);
            layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton = view.findViewById(R.id.noButton);
            Button yesButton = view.findViewById(R.id.yesButton);

            alertDialogBuilder.setView(view);
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            noButton.setOnClickListener((View v) -> alertDialog.dismiss());

            yesButton.setOnClickListener((View v) -> {
                DatabaseHandler db = new DatabaseHandler(context);
                db.deleteGrocery(id);
                groceryList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());

                alertDialog.dismiss();
            });

        }
    }
}
