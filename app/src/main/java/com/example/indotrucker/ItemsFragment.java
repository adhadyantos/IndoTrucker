package com.example.indotrucker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemsFragment extends Fragment {

    private ImageButton mImageButton;
    private EditText mEditText;

    private RecyclerView mItemsList;

    private View mMainView;

    private FirebaseAuth mAuth;

    private DatabaseReference mItemsDatabase;

    public ItemsFragment(){
        //Empty Constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_items, container, false);
        mItemsList = (RecyclerView) mMainView.findViewById(R.id.items_list);
        mAuth = FirebaseAuth.getInstance();

        String current_user_id = mAuth.getCurrentUser().getUid();

        mImageButton = (ImageButton) mMainView.findViewById(R.id.items_search_btn);
        mEditText = (EditText) mMainView.findViewById(R.id.items_search_edittext);

        mItemsDatabase = FirebaseDatabase.getInstance().getReference().child("items");
        mItemsDatabase.keepSynced(true);

        mItemsList.setHasFixedSize(true);
        mItemsList.setLayoutManager(new LinearLayoutManager(getContext()));

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text_search = mEditText.getText().toString();
                searchItem(text_search);

            }
        });


        // Inflate the layout for this fragment
        return mMainView;

    }

    private void searchItem(String text_search) {

        Query firebaseSearchQuery = mItemsDatabase.orderByChild("from").startAt(text_search).endAt(text_search + "\uf8ff");

        FirebaseRecyclerOptions<Items> options=
                new FirebaseRecyclerOptions.Builder<Items>()
                        .setQuery(firebaseSearchQuery,Items.class)
                        .setLifecycleOwner(this)
                        .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Items, ItemsViewHolder>(options) {

            @NonNull
            @Override
            public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ItemsFragment.ItemsViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.items_single_layout, parent, false));

            }


            @Override
            protected void onBindViewHolder(@NonNull final ItemsViewHolder itemsViewHolder, int position, @NonNull Items model) {

                String item_id = getRef(position).getKey();

                itemsViewHolder.setName(model.getName());
                itemsViewHolder.setLoad(model.getLoad());
                itemsViewHolder.setAddress(model.getAddress());
                itemsViewHolder.setFrom(model.getFrom());
                itemsViewHolder.setTo(model.getTo());

                mItemsDatabase.child(item_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        final String user_id = dataSnapshot.child("owner").getValue().toString();

                        itemsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CharSequence options[] = new CharSequence[]{"Contact Owner"};

                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if(i == 0){

                                            Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                            profileIntent.putExtra("user_id", user_id);
                                            startActivity(profileIntent);

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
        mItemsList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        Query firebaseshownSearchQuery = mItemsDatabase.orderByChild("shown").startAt("true").endAt("true" + "\uf8ff");

        FirebaseRecyclerOptions<Items> options=
                new FirebaseRecyclerOptions.Builder<Items>()
                        .setQuery(firebaseshownSearchQuery,Items.class)
                        .setLifecycleOwner(this)
                        .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Items, ItemsViewHolder>(options) {

            @NonNull
            @Override
            public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ItemsFragment.ItemsViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.items_single_layout, parent, false));

            }


            @Override
            protected void onBindViewHolder(@NonNull final ItemsViewHolder itemsViewHolder, int position, @NonNull Items model) {

                String item_id = getRef(position).getKey();

                itemsViewHolder.setName(model.getName());
                itemsViewHolder.setLoad(model.getLoad());
                itemsViewHolder.setAddress(model.getAddress());
                itemsViewHolder.setFrom(model.getFrom());
                itemsViewHolder.setTo(model.getTo());

                mItemsDatabase.child(item_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        final String user_id = dataSnapshot.child("owner").getValue().toString();

                        itemsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CharSequence options[] = new CharSequence[]{"Contact Owner"};

                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if(i == 0){

                                            Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                            profileIntent.putExtra("user_id", user_id);
                                            startActivity(profileIntent);

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
        mItemsList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ItemsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ItemsViewHolder(@NonNull View itemView) {
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

