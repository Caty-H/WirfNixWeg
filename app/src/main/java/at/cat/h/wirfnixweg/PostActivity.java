package at.cat.h.wirfnixweg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import at.cat.h.wirfnixweg.model.Article;
import at.cat.h.wirfnixweg.model.User;

public class PostActivity extends AppCompatActivity {

    private EditText articleName, unit, price;
    private TextView name, phone, address, postcode;
    private TextView noPost;
    private FloatingActionButton done;
    private DatabaseReference reference;
    private FirebaseUser currentUser;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        articleName = findViewById(R.id.article_et);
        unit = findViewById(R.id.unit_et);
        price = findViewById(R.id.price_et);

        // UserDaten in die Felder
        name = findViewById(R.id.full_name_tv);
        address = findViewById(R.id.adress_tv);
        phone = findViewById(R.id.phone_tv);
        postcode = findViewById(R.id.postCode_tv);

        done = findViewById(R.id.done_btn);
        noPost = findViewById(R.id.not_post_tv);

        noPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Artikeln in die DB
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String articleNameSave = articleName.getText().toString();
                double unitSave = Double.parseDouble(unit.getText().toString());
                double priceSave = Double.parseDouble(price.getText().toString());

                if (TextUtils.isEmpty(articleNameSave) || TextUtils.isEmpty(String.valueOf(unitSave))
                        || TextUtils.isEmpty(String.valueOf(priceSave))) {
                    Toast.makeText(PostActivity.this, "Die Felder müssen ausgefüllt sein", Toast.LENGTH_LONG).show();
                } else {

                }

                Article article = new Article(articleNameSave, unitSave, priceSave);
                article.setUserInformation(user);
                DatabaseReference articlesReference = reference.child("post").child("article").push();

                articlesReference
                        .setValue(article)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });
                sendToMain();
            }
        });

        //UserDaten aus der DB zurück
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        reference.child("user")

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
    }

    public void sendToMain() {
        startActivity(new Intent(PostActivity.this, MainActivity.class));
        finish();
    }
}