package at.cat.h.wirfnixweg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import at.cat.h.wirfnixweg.model.Article;
import at.cat.h.wirfnixweg.view.PostAdapter;

public class MainActivity extends AppCompatActivity implements PostAdapter.SelectedArticle{

    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private List<Article> articleList;
    private PostAdapter postAdapter;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar();

        articleList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        postAdapter = new PostAdapter(this, articleList, this);
        recyclerView.setAdapter(postAdapter);

        reference = FirebaseDatabase.getInstance().getReference("/post/article");

        ValueEventListener valueEventListener = new ValueEventListener() {
            private static final String TAG = "";

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Article article = snapshot.getValue(Article.class);
                        articleList.add(article);
                        Log.d(TAG, "onDataChange" + dataSnapshot.toString());
                    }
                    postAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled" + databaseError.toString());
            }
        };
        reference.addListenerForSingleValueEvent(valueEventListener);
    }


    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search_view);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                postAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.action_search_view :
                int id = item.getItemId();
                if (id == R.id.action_search_view){
                    return true;
                }
                break;
            case R.id.action_add_button :
                startActivity(new Intent(MainActivity.this,PostActivity.class));
                break;

            case R.id.action_profile_button :
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;

            case R.id.action_archiv_button :
                startActivity(new Intent(MainActivity.this, ArchiveActivity.class));
                break;

            case R.id.action_logout_button :
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
        }
        return true;
    }


    @Override
    public void selectedArticle(Article article) {
        startActivity(new Intent(MainActivity.this, SelectedArticleActivity.class)
               .putExtra("article", article));
    }
}
