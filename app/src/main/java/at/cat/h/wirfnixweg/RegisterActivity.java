package at.cat.h.wirfnixweg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;
import at.cat.h.wirfnixweg.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText regEmail, regName, regAddress, regPostcode, regPhone, regPassword, regConfirmPassword;
    private Button register;
    private TextView login;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regName = findViewById(R.id.name_et);
        regEmail = findViewById(R.id.email_et);
        regAddress = findViewById(R.id.address_et);
        regPostcode = findViewById(R.id.postCode_et);
        regPhone = findViewById(R.id.phone_et);
        regPassword = findViewById(R.id.password_et);
        regConfirmPassword = findViewById(R.id.password_confirm_et);
        register = findViewById(R.id.register_button);
        login = findViewById(R.id.login_tv);

        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = regName.getText().toString();
                String email = regEmail.getText().toString();
                String address = regAddress.getText().toString();
                String postCode = regPostcode.getText().toString();
                String phone = regPhone.getText().toString();
                String password = regPassword.getText().toString();
                String confirmPassword = regConfirmPassword.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(address) || (TextUtils.isEmpty(postCode))
                    || (TextUtils.isEmpty(phone)) || (TextUtils.isEmpty(password)) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Das Feld muss ausgefüllt sein!", Toast.LENGTH_LONG).show();
                } else if (password.length() < 8) {
                    Toast.makeText(RegisterActivity.this, "Mindestens 8 Ziffern bitte!", Toast.LENGTH_LONG).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwort stimmt nicht!", Toast.LENGTH_LONG).show();
                } else {
                    registerUser(name, address, postCode, phone, email, password);
                }
            }
        });
    }

    private void registerUser(final String name, final String address, final String postCode, final String phone, final String email, final String password) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //String userId = mRef.getKey();
                                User user = new User(name, address, postCode, phone, email, password);

                                FirebaseDatabase.getInstance().getReference("user")

                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())

                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Herzlich Willkommen!" ,Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();


                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Leider ... " ,Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });

                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));


                            } else {
                                Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
    }
}




