package com.emranhss.blooddonation;


import com.emranhss.blooddonation.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.emranhss.blooddonation.Adapter.UserAdapter;
import com.emranhss.blooddonation.Model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout appDrawerLayout;
    private Toolbar appToolbar;
    private NavigationView appNavigationView;

    private CircleImageView nav_user_image;
    private TextView nav_user_name, nav_user_email, nav_user_pnumber, nav_user_bloodgroup, nav_user_type;

    private DatabaseReference userRef;

    private RecyclerView appRecyclerView;
    private ProgressBar appProgressbar;
    private List<User> userList;
    private UserAdapter userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        appToolbar = findViewById(R.id.toobar);
        setSupportActionBar(appToolbar);
        getSupportActionBar().setTitle("Blood and Buddies App");

        appDrawerLayout = findViewById(R.id.drawerLayout);
        appNavigationView = findViewById(R.id.appNavigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(DashboardActivity.this, appDrawerLayout, appToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        appDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        appNavigationView.setNavigationItemSelectedListener(this);

        appProgressbar = findViewById(R.id.progressbar);
        appRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        appRecyclerView.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(DashboardActivity.this, userList);
        appRecyclerView.setAdapter(userAdapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type = snapshot.child("type").getValue().toString();
                if(type.equals("donor")){
                    readRecipients();
                }else{
                    readDonor();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        nav_user_image = appNavigationView.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_user_name = appNavigationView.getHeaderView(0).findViewById(R.id.nav_user_name);
        nav_user_pnumber = appNavigationView.getHeaderView(0).findViewById(R.id.nav_user_pnumber);
        nav_user_email = appNavigationView.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_user_bloodgroup = appNavigationView.getHeaderView(0).findViewById(R.id.nav_user_bloodgroup);
        nav_user_type = appNavigationView.getHeaderView(0).findViewById(R.id.nav_user_type);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();
                    nav_user_name.setText(name);

                    String email = snapshot.child("email").getValue().toString();
                    nav_user_email.setText(email);

                    String phonenumber = snapshot.child("phonenumber").getValue().toString();
                    nav_user_pnumber.setText(phonenumber);

                    String bloodgroup = snapshot.child("bloodgroup").getValue().toString();
                    nav_user_bloodgroup.setText(bloodgroup);

                    String type = snapshot.child("type").getValue().toString();
                    nav_user_type.setText(type);

                    if(snapshot.hasChild("profilepictureurl")){
                        String imageUrl = snapshot.child("profilepictureurl").getValue().toString();
                        Glide.with(getApplicationContext()).load(imageUrl).into(nav_user_image);
                    }else{
                        nav_user_image.setImageResource(R.drawable.profile);
                    }

                    Menu nav_menu = appNavigationView.getMenu();
                    if (type.equals("donor")){
                        nav_menu.findItem(R.id.notifications).setVisible(true);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void readDonor() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = reference.orderByChild("type").equalTo("donor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                appProgressbar.setVisibility(View.GONE);

                if(userList.isEmpty()){
                    Toast.makeText(DashboardActivity.this, "No Donors", Toast.LENGTH_SHORT).show();
                    appProgressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readRecipients() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = reference.orderByChild("type").equalTo("recipient");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                appProgressbar.setVisibility(View.GONE);

                if(userList.isEmpty()){
                    Toast.makeText(DashboardActivity.this, "No Recipient", Toast.LENGTH_SHORT).show();
                    appProgressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sent_mail) {
            startActivity(new Intent(this, SentEmailActivity.class));

        } else if (id == R.id.compatible) {
            openCategory("Compatible with me");

        } else if (id == R.id.aplus) {
            openCategory("A+");

        } else if (id == R.id.aminus) {
            openCategory("A-");

        } else if (id == R.id.abplus) {
            openCategory("AB+");

        } else if (id == R.id.abminus) {
            openCategory("AB-");

        } else if (id == R.id.bplus) {
            openCategory("B+");

        } else if (id == R.id.bminus) {
            openCategory("B-");

        } else if (id == R.id.oplus) {
            openCategory("O+");

        } else if (id == R.id.ominus) {
            openCategory("O-");

        } else if (id == R.id.profile) {
            startActivity(new Intent(this, ProfileActivity.class));

        } else if (id == R.id.notifications) {
            startActivity(new Intent(this, NotificationActivity.class));

        } else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();

        } else if (id == R.id.about) {
            startActivity(new Intent(this, AboutActivity.class));
        }

        appDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void openCategory(String groupName) {
        Intent intent = new Intent(this, CatogarySelectedActivity.class);
        intent.putExtra("group", groupName);
        startActivity(intent);
    }




//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_dashboard);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
}