package com.example.indotrucker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyItemActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView mMyItemsList;

    private View mMainView;

    private FirebaseAuth mAuth;

    private DatabaseReference mMyItemsDatabase, mItemsDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item);

        mAuth = FirebaseAuth.getInstance();
        String current_user_id = mAuth.getCurrentUser().getUid();

        mToolbar = (Toolbar) findViewById(R.id.my_items_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Items");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMyItemsDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(current_user_id).child("user_items");
        mMyItemsDatabase.keepSynced(true);

        mItemsDatabase = FirebaseDatabase.getInstance().getReference().child("items");
        mItemsDatabase.keepSynced(true);

        mMyItemsList = (RecyclerView) findViewById(R.id.myitems_list);
        mMyItemsList.setHasFixedSize(true);
        mMyItemsList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<MyItems> options =
                new FirebaseRecyclerOptions.Builder<MyItems>()
                        .setQuery(mMyItemsDatabase, MyItems.class)
                        .setLifecycleOwner(this)
                        .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyItems, MyItemsViewHolder>(options) {

            @NonNull
            @Override
            public MyItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyItemsViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.items_single_layout, parent, false));

            }

            @Override
            protected void onBindViewHolder(@NonNull final MyItemsViewHolder myitemsViewHolder, int position, @NonNull MyItems model) {

                final String item_id = getRef(position).getKey();

                myitemsViewHolder.setName(model.getName());
                myitemsViewHolder.setLoad(model.getLoad());
                myitemsViewHolder.setAddress(model.getAddress());
                myitemsViewHolder.setFrom(model.getFrom());
                myitemsViewHolder.setTo(model.getTo());

                mMyItemsDatabase.child(item_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        myitemsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CharSequence options[] = new CharSequence[]{"Delete Item"};

                                final AlertDialog.Builder builder = new AlertDialog.Builder(MyItemActivity.this);

                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if (i == 0) {

                                            mMyItemsDatabase.child(item_id).removeValue();
                                            mItemsDatabase.child(item_id).child("shown").setValue("false");
                                        }

                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        };

        mMyItemsList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class MyItemsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public MyItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }


        public void setName(String name){
            TextView itemName = (TextView) mView.findViewById(R.id.item_single_name);
            itemName.setText(name);
        }

        public void setLoad(String load){
            TextView itemLoad = (TextView) mView.findViewById(R.id.item_single_load);
            itemLoad.setText(load);
        }

        public void setAddress(String address){
            TextView itemAddress = (TextView) mView.findViewById(R.id.item_single_address);
            itemAddress.setText(address);
        }

        public void setFrom(String from){
            TextView itemFrom = (TextView) mView.findViewById(R.id.item_single_from);
            itemFrom.setText(from);
        }

        public void setTo(String to){
            TextView itemTo = (TextView) mView.findViewById(R.id.item_single_to);
            itemTo.setText("To " + to);
        }


    }
}

