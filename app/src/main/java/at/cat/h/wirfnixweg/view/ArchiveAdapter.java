package at.cat.h.wirfnixweg.view;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import at.cat.h.wirfnixweg.R;
import at.cat.h.wirfnixweg.model.Article;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ArchiveViewHolder> {
    private Context ctx;
    private List<Article> articleList;
    private SelectedPosting selectedPosting;

    public ArchiveAdapter(Context ctx, List<Article> articleList, SelectedPosting selectedPosting) {
        this.ctx = ctx;
        this.articleList = articleList;
        this.selectedPosting = selectedPosting;
    }

    @NonNull
    @Override
    public ArchiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.archive_item, null);
        return new ArchiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArchiveViewHolder holder, final int position) {
        Article article = articleList.get(position);
        holder.articleN.setText(article.getArticleName());
        holder.unitA.setText(String.valueOf(article.getUnit()));
        holder.priceA.setText(String.valueOf(article.getPrice()));
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public interface SelectedPosting {
        void onItemClick(int position);
        void onLongItemClick(int position);
    }

    // ViewHolder
    public class ArchiveViewHolder extends RecyclerView.ViewHolder{
        TextView articleN, unitA, priceA;
        ImageView delete;

     public ArchiveViewHolder(@NonNull final View itemView) {
         super(itemView);
         articleN = itemView.findViewById(R.id.article_name_prefixTv);
         unitA = itemView.findViewById(R.id.unit_articleTv);
         priceA = itemView.findViewById(R.id.price_articleTv);
         delete = itemView.findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosting.onItemClick(getAdapterPosition());
            }
        });

        delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //selectedPosting = (SelectedPosting) articleList;
                selectedPosting.onLongItemClick(getAdapterPosition());
                return true;
            }
        });
     }
 }
}
