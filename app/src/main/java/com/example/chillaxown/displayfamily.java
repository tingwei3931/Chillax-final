package com.example.chillaxown;



import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

public class displayfamily extends AppCompatActivity implements PostAdapter.RecycleViewClickListener {

 /*   private RecyclerView rc;
    private DatabaseReference mdb;
  //  List<familypost> list;
    FirebaseDatabase database = FirebaseDatabase.getInstance();*/

    TextView posttaskname,postTaskcategory,postTaskTime,postTaskdate,userid;
    Button save,view;
    FirebaseDatabase database;
    DatabaseReference myRef,mdb;
    RecyclerView recyclerview,rc;
    List<Post> postList;
    RecyclerView postRecyclerView ;
    PostAdapter postAdapter ;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage mStorage;
    DatabaseReference databaseReference;
    ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayfamily);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(83,36,54)));
        getSupportActionBar().setTitle("Sharing Space");

        posttaskname = (TextView) findViewById(R.id.row_task_name);
        postTaskcategory = (TextView) findViewById(R.id.row_task_category);
        postTaskTime = (TextView) findViewById(R.id.row_task_time);
        postTaskdate = (TextView) findViewById(R.id.row_task_date);

        //userid = (TextView) findViewById(R.id.row_task_user);

        recyclerview = (RecyclerView) findViewById(R.id.rview);
        spinner = (ProgressBar) findViewById(R.id.progressBar2);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("reader");

        mdb=FirebaseDatabase.getInstance().getReference().child("reader");

        mdb.keepSynced(true);
        rc=(RecyclerView) findViewById(R.id.rview);
        rc.setLayoutManager(new LinearLayoutManager(this));
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("reader");

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getApplicationContext(), postList, this);
        rc.setAdapter(postAdapter);
        showSpinner();
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        // Clear the previous data onStart
        postAdapter.clearAll();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnap: dataSnapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    post.setPostKey(postsnap.getKey());
                    postList.add(post) ;

                }
                hideSpinner();
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void recycleViewListClicked(View v, int position) {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }


    private void hideSpinner(){
        recyclerview.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);
    }

    private void showSpinner() {
        recyclerview.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
    }
}

/**
class BlogViewHolder extends  RecyclerView.ViewHolder{
    View mView;
    public BlogViewHolder(View itemView)
    {
        super(itemView);
        mView=itemView;
    }
    public void setTaskName (String title)
    {
        TextView posttaskname=(TextView)mView.findViewById(R.id.task_name);
        posttaskname.setText(title);
    }
    public void setcategory (String category)
    {
        TextView postTaskcategory=(TextView)mView.findViewById(R.id.task_category);
        postTaskcategory.setText(category);
    }
    public void settime (String TaskTime)
    {
        TextView postTaskTime=(TextView)mView.findViewById(R.id.task_time);
        postTaskTime.setText(TaskTime);
    }
    public void setdate(String Taskdate)
    {
        TextView    postTaskdate=(TextView)mView.findViewById(R.id.task_date);
        postTaskdate.setText(Taskdate);

    }
    public void setuser(String user)
    {
        TextView userid=(TextView)mView.findViewById(R.id.task_user);
        userid.setText(user);

    }

**/