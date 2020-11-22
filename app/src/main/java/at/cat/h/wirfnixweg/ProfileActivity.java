package at.cat.h.wirfnixweg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import at.cat.h.wirfnixweg.model.User;


public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "";
    private Toolbar mToolbar;
    private EditText name, phone, address, postcode;
    private Button save;
    private User user;

    private FirebaseUser currentUser;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mToolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar();

        name = findViewById(R.id.full_name_et);
        phone = findViewById(R.id.phone_et);
        address = findViewById(R.id.address_et);
        postcode = findViewById(R.id.postCode_et);
        save = findViewById(R.id.btn_save);

        getUserObjects();
    }

    private void getUserObjects() {

        //UserDaten aus der DB zurück
        reference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        reference.child("/user")
                .child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            name.setText(user.getName());
                            phone.setText(user.getPhone());
                            address.setText(user.getAddress());
                            postcode.setText(user.getPostCode());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameUpdate = name.getText().toString();
                String phoneUpdate = phone.getText().toString();
                String addressUpdate = address.getText().toString();
                String postCodeUpdate = postcode.getText().toString();

                // UserDaten in die DB speichern
                 final User user = new User(nameUpdate, addressUpdate, postCodeUpdate, phoneUpdate);

                reference.child("user")
                        .child(currentUser.getUid())
                        .setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ProfileActivity.this, "Daten wurden geändert", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "userDaten: " + user.getName()+ user.getPhone() +
                                        user.getAddress() + user.getPostCode());
                            }
                        });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_return_btn :
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                break;
            case R.id.action_add_btn :
                startActivity(new Intent(ProfileActivity.this, PostActivity.class));
                break;
            case R.id.action_logout_btn :
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                break;
        }
        return true;
    }
}
