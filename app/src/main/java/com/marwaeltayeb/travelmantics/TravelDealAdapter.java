package com.marwaeltayeb.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TravelDealAdapter extends RecyclerView.Adapter<TravelDealAdapter.DealViewHolder>{

    ArrayList<TravelDeal> deals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;

    public TravelDealAdapter() {
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        this.deals = FirebaseUtil.mDeals;
        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TravelDeal td = dataSnapshot.getValue(TravelDeal.class);
                Log.d("Deal: ", td.getTitle());
                td.setId(dataSnapshot.getKey());
                deals.add(td);
                // Notify if an item is inserted
                notifyItemInserted(deals.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildListener);
    }

    @Override
    public DealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.user_list_item, parent, false);
        return new DealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DealViewHolder holder, int position) {
        TravelDeal deal = deals.get(position);
        holder.bind(deal);
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }


    public class DealViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView tvTitle;
        TextView tvDescription;
        TextView tvPrice;

        public DealViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            itemView.setOnClickListener(this);
        }

        public void bind(TravelDeal deal) {
            tvTitle.setText(deal.getTitle());
            tvDescription.setText(deal.getDescription());
            tvPrice.setText(deal.getPrice());
        }

        @Override
        public void onClick(View view) {
            // get position of the item that is clicked
            int position = getAdapterPosition();
            Log.d("Click", String.valueOf(position));
            // find the travelDeal that was selected through its position
            TravelDeal selectedDeal = deals.get(position);
            Intent intent = new Intent(view.getContext(), AdminActivity.class);
            // Pass an object of TravelDeal class
            intent.putExtra("Deal", (selectedDeal));
            view.getContext().startActivity(intent);
        }
    }
}
