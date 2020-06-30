package com.example.indotrucker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextInputLayout mItemName, mItemLoad, mItemAddress, mItemFrom, mItemTo;
    private Button mUploadItemBtn;
    private ProgressDialog mProgress;

    private FirebaseUser mCurrentUser;

    private DatabaseReference mUserItems, mAllItemsDatabase, mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserItems = FirebaseDatabase.getInstance().getReference().child("users").child(current_uid).child("user_items");
        mAllItemsDatabase = FirebaseDatabase.getInstance().getReference().child("items");

        mToolbar = (Toolbar) findViewById(R.id.upload_toolbar);
        mItemName = (TextInputLayout) findViewById(R.id.upload_item_name);
        mItemLoad = (TextInputLayout) findViewById(R.id.upload_item_load);
        mItemAddress = (TextInputLayout) findViewById(R.id.upload_item_address);
        mItemFrom = (TextInputLayout) findViewById(R.id.upload_item_from);
        mItemTo = (TextInputLayout) findViewById(R.id.upload_item_to);
        mUploadItemBtn = (Button) findViewById(R.id.upload_item_btn);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Upload Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUploadItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String name = mItemName.getEditText().getText().toString();
                String load = mItemLoad.getEditText().getText().toString();
                String address = mItemAddress.getEditText().getText().toString();
                String from = mItemFrom.getEditText().getText().toString();
                String to = mItemTo.getEditText().getText().toString();

                if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(load) || !TextUtils.isEmpty(address) || !TextUtils.isEmpty(from) || !TextUtils.isEmpty(to)){

                    mProgress = new ProgressDialog(UploadActivity.this);
                    mProgress.setTitle("Uploading Item");
                    mProgress.setMessage("Please wait while we upload your item");
                    mProgress.show();

                    upload_item(name, load, address, from, to);
                }
            }
        });
    }

    private void upload_item(String name, String load, String address, String from, String to) {

        String user_item_ref = "users/" + mCurrentUser + "/" + "user_items";
        String all_item_ref = "items";

        String current_uid = mCurrentUser.getUid();

        DatabaseReference user_item_push = mAllItemsDatabase.push();
        final String item_push_id = user_item_push.getKey();

        final Map userItemsMap = new HashMap();

        userItemsMap.put("name", name);
        userItemsMap.put("load", load);
        userItemsMap.put("address", address);
        userItemsMap.put("from", from);
        userItemsMap.put("to", to);
        userItemsMap.put("owner", current_uid);
        userItemsMap.put("shown", "true");

        mUserItems.child(item_push_id).setValue(userItemsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                mAllItemsDatabase.child(item_push_id).setValue(userItemsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        mProgress.dismiss();

                        Intent mainIntent = new Intent(UploadActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();

                    }
                });

            }
        });

    }
}
