package com.example.chillaxown;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        InsertAsyncTask.AsyncResponse,
        GetAllAsyncTask.AsyncResponse,
        DeleteAsyncTask.AsyncResponse,
        TabLayout.OnTabSelectedListener,
        TaskAdapter.RecycleViewClickListener
{

    FirebaseUser user;
    ArrayList<TaskDetails> tasks = new ArrayList<TaskDetails>();
    public static TaskAdapter adapter;
    public static TabLayout tabs;
    private static final int ADD_ACT = 1;
    private static final int EDIT_ACT = 2;
    private static final String TAG = "TAG";
    private String[] categoryList = {"In progress", "Completed"};
    private static final int VIEW_ACT = 3;
  final TaskDetailsSQL sq=new TaskDetailsSQL(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabs = (TabLayout) findViewById(R.id.topHeader);
        tabs.addOnTabSelectedListener(this);
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        adapter = new TaskAdapter(tasks, this, this);
        rv.setAdapter(adapter);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            String displayName = (name == null) ? email : name;
            Snackbar.make(getWindow().getDecorView().getRootView(), "Sign in successfully! Welcome " +
                    displayName, Snackbar.LENGTH_LONG)
                    .show();
            // Check if user's email is verified
            // boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent AddActivity = new Intent(getApplicationContext(), AddActivity.class);
                startActivityForResult(AddActivity, ADD_ACT);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        GetAllAsyncTask getAllAsync = new GetAllAsyncTask(getApplicationContext(), this);
           getAllAsync.execute();

        updateNavHeader();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    TaskDetailsSQL sqlsearch = new TaskDetailsSQL(this);
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       final GetAllAsyncTask getAllAsync1 = new GetAllAsyncTask(getApplicationContext(), this);
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        MenuItem searchItem=menu.findItem(R.id.menuSearch);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }



            @Override
            public boolean onQueryTextChange(String newText) {
              //  getAllAsync1.getlist(newText);
                getAllAsync1.getlist( newText);
             //   sqlsearch.taskDetailsList.contains()
               // sq.getTaskDetailsByCategory(newText);
              //  sq.getTaskDetailsByCategory(newText);
              //  getlist(newText);
                return false;
            }
        });
     //   getMenuInflater().inflate(R.menu.main, menu);
       // return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            Snackbar.make(getWindow().getDecorView().getRootView(), "Successfully Signed Out!", Snackbar.LENGTH_LONG)
                    .show();
            startActivity(loginActivity);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void updateNavHeader(){

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        TextView navUsername = headerview.findViewById(R.id.navHeaderTitle);
        TextView navUserMail = headerview.findViewById(R.id.navHeaderSubtitle);

        navUserMail.setText(user.getEmail());
        navUsername.setText(user.getDisplayName());
    }

    /**
     * Handles when the database returns the list of items
     * @param result
     */
    @Override
    public void processFinish(ArrayList<TaskDetails> result) {
        //clear the data in existing listView
        adapter.clear();
        //add the new data
        Log.i("INIT", result.toString());
        adapter.addAll(result);
        //update view
        adapter.notifyDataSetChanged();
        tabs.getTabAt(0).select();
    } //end of processFinish

    @Override
    public void processFinish(int result) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        switch(position) {
            case 0:
                GetAllAsyncTask getAllAsync = new GetAllAsyncTask(getApplicationContext(), this);
                getAllAsync.execute();
                Log.i(TAG,"ALL LOCATION");
                break;
            case 1:
                adapter.filter(categoryList[position]);
                adapter.notifyDataSetChanged();
                Log.i(TAG,"IN PROGRESS LOCATION");
                break;
            case 2:
                adapter.filter(categoryList[position]);
                adapter.notifyDataSetChanged();
                Log.i(TAG,"COMPLETED LOCATION");
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    //override onActivityResult method to listen for callbacks from confirmationActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_ACT) {
            if(resultCode == RESULT_OK) {
                //inform user activity is added
                Snackbar.make(getWindow().getDecorView().getRootView(), "Task Added!", Snackbar.LENGTH_LONG)
                        .show();
            }
        } else if(requestCode == EDIT_ACT) {
            //if user clicked yes
            if(resultCode == Activity.RESULT_OK) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "Task successfully updated!", Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    } //end onActivityResult

    @Override
    public void processFinish(boolean result) {
        if (result) {
            Log.i("RESULT", String.valueOf(result));
            Log.i("DELETE", "Delete successfully!");
            Snackbar.make(getWindow().getDecorView().getRootView(), "Task Deleted!", Snackbar.LENGTH_LONG)
                    .show();
            //Retrieve new set of data after deletion
            GetAllAsyncTask getAllAsync = new GetAllAsyncTask(this, this);
            getAllAsync.execute();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.i("MAIN", "ContextItemSelected!");
        if(item.getTitle().equals("Delete")) {
            //ContextMenuRecyclerView.RecyclerViewContextMenuInfo info = (ContextMenuRecyclerView.RecyclerViewContextMenuInfo) item.getMenuInfo();

            //Log.i("OBJECT", task.toString());
            int itemPos = item.getOrder();
            TaskDetails task  = adapter.getItem(itemPos);
            Log.i("MAIN", "Delete pressed!");
            Log.i("MAIN", itemPos+"");
            showConfirmationDialog(task);
        } else if(item.getTitle().equals("Edit")) {
            //ContextMenuRecyclerView.RecyclerViewContextMenuInfo info = (ContextMenuRecyclerView.RecyclerViewContextMenuInfo) item.getMenuInfo();
            //TaskDetails task = adapter.getItem(info.position);
            int itemPos = item.getOrder();
            TaskDetails task  = adapter.getItem(itemPos);
            Log.i("OBJECT", task.toString());
            Log.i("MAIN", "Edit pressed!");
            goToEditActivity(task);
        }
        return super.onContextItemSelected(item);
    }

    private void showConfirmationDialog(TaskDetails task) {
        final TaskDetails innerTask = task;
        AlertDialog.Builder cfmBuilder = new AlertDialog.Builder(this);
        StringBuilder confirmationText =  new StringBuilder();
        confirmationText.append("Are you sure to delete ")
                .append(task.getTaskName())
                .append("?");
        cfmBuilder.setMessage(confirmationText)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteAsyncTask deleteAsync = new DeleteAsyncTask(MainActivity.this, MainActivity.this);
                        deleteAsync.execute(innerTask.getTaskID());
                        Log.i("DELETE", innerTask.toString());
                        Log.i("DELETE", "Task deleted!");
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("DELETE", "Task not deleted");
                    }
                });
        cfmBuilder.show();
    } //end of showConfirmationDialog

    private void goToEditActivity(TaskDetails task) {
        Intent intent = new Intent(this, EditActivity.class);
        //Log.i("ADD", "Entering AddActivity");
        //start confirmationActivity
        intent.putExtra("taskObj", task);
        startActivityForResult(intent, EDIT_ACT);
    } //end of goToEditActivity

    @Override
    public void recycleViewListClicked(View v, int position) {
        Log.i("POSITION", position+"");
        Intent intent = new Intent(this, ViewActivity.class);
        TaskDetails task = adapter.getItem(position);
        intent.putExtra("taskObj", task);
        startActivityForResult(intent, VIEW_ACT);
    }
}
