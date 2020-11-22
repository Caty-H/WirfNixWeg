package at.cat.h.wirfnixweg;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;
import java.util.List;
import at.cat.h.wirfnixweg.model.Article;
import at.cat.h.wirfnixweg.model.User;

public class SelectedArticleActivity extends AppCompatActivity {

    private static final int REQUEST_PHONE_PERMISSION_CODE = 2;
    private TextView articleName, unit, price;
    private TextView userName, userAddress, userPhone;
    private ImageView back;
    private Button call;

    private DatabaseReference reference;
    private Article article;
    private List<Article> articleList = new ArrayList<>();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_article);

        articleName = findViewById(R.id.article_tv);
        unit = findViewById(R.id.unit_tv);
        price = findViewById(R.id.price_tv);

        //UserDaten in die Felder
        userName = findViewById(R.id.full_name_tv);
        userAddress = findViewById(R.id.adress_tv);
        userPhone = findViewById(R.id.phone_tv);

        back = findViewById(R.id.backIv);

        final Intent intent = getIntent();
        if (articleName != null) {

            Article article = (Article) intent.getSerializableExtra("article");
            articleName.setText(article.getArticleName());
            unit.setText(String.valueOf(article.getUnit()));
            price.setText(String.valueOf(article.getPrice()));
            userName.setText(article.getUserName());
            userAddress.setText(article.getUserAddress());
            userPhone.setText(article.getUserPhone());
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(SelectedArticleActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    public void triggerCall(View view) {
        userPhone = findViewById(R.id.phone_tv);
        String number = userPhone.getText().toString();

        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + number));  // ,Uri.parse("tel:06641234567")

        PackageManager packageManager = getPackageManager();
        if (callIntent.resolveActivity(packageManager) != null) {
            // Ja, es gibt eine Activity
            startActivity(callIntent);
        } else {
            // Nein, es gibt keine... Benutzer benachrichtigen
            Toast.makeText(this, "Leider", Toast.LENGTH_LONG).show();
        }
    }
}
















