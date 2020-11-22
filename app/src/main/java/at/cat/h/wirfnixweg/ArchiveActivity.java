package at.cat.h.wirfnixweg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import at.cat.h.wirfnixweg.model.Article;
import at.cat.h.wirfnixweg.view.ArchiveAdapter;

public class ArchiveActivity extends AppCompatActivity implements ArchiveAdapter.SelectedPosting{ //§§§

    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private List<Article> articleList;
    private ArchiveAdapter archiveAdapter;
    private DatabaseReference reference;

    private String articleName;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        mToolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar();

        articleList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        archiveAdapter = new ArchiveAdapter(this, articleList, this);
        recyclerView.setAdapter(archiveAdapter);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("/post/article");
        query = reference.orderByChild("userEmail").equalTo(currentUser.getEmail());
        showData();
    }

    private void showData() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            private static final String TAG = "";

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Article article = snapshot.getValue(Article.class);
                        article.setId(snapshot.getKey());
                        articleList.add(article);
                        Log.d(TAG, "onDataChange" + dataSnapshot.toString());
                    }
                    archiveAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled" + databaseError.toString());
            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_return_btn :
                startActivity(new Intent(ArchiveActivity.this, MainActivity.class));
                break;
            case R.id.action_add_btn :
                startActivity(new Intent(ArchiveActivity.this, PostActivity.class));
                break;

            // Search

            case R.id.action_logout_btn :
                //logout();
        }
        return true;
    }

    //§§§
    @Override
    public void onItemClick(int position) {
        showDeleteDialog(articleList.get(position));
    }

    @Override
    public void onLongItemClick(int position) {
        showDeleteDialog(articleList.get(position));
    }

    private void showDeleteDialog(final Article article){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ArchiveActivity.this);
        dialogBuilder.setTitle("Bitte löschen Sie die abgelaufenen Beiträge!");
        dialogBuilder.setMessage("Entfernen?");
        dialogBuilder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                reference.child(article.getId()).removeValue();
            }
        });
        dialogBuilder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}