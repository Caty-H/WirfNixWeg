package at.cat.h.wirfnixweg.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import at.cat.h.wirfnixweg.R;
import at.cat.h.wirfnixweg.model.Article;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> implements Filterable{

    private Context ctx;
    private List<Article> articleList;
    private List<Article> getArticleListFiltered;
    private SelectedArticle selectedArticle;

    public PostAdapter(Context ctx, List<Article> articleList, SelectedArticle selectedArticle) {
        this.ctx = ctx;
        this.articleList = articleList;
        this.getArticleListFiltered = articleList;
        this.selectedArticle = selectedArticle;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.article_post,null);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.articleN.setText(article.getArticleName());
        holder.unitA.setText(String.valueOf(article.getUnit()));
        holder.priceA.setText(String.valueOf(article.getPrice()));
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    // for Search
    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                FilterResults filterResults = new FilterResults();
                if (charSequence == null | charSequence.length() == 0){
                    filterResults.count = getArticleListFiltered.size();
                    filterResults.values = getArticleListFiltered;
                } else {
                    String searchChar = charSequence.toString().toLowerCase();

                    List<Article> resultData = new ArrayList<>();
                    for (Article article : getArticleListFiltered){
                        if (article.getArticleName().toLowerCase().contains(searchChar)){
                            resultData.add(article);
                        }
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {

                articleList = (List<Article>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public interface SelectedArticle {
        void selectedArticle(Article article);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        TextView articleN, unitA, priceA;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        articleN = itemView.findViewById(R.id.article_name_prefixTv);
        unitA = itemView.findViewById(R.id.unit_articleTv);
        priceA = itemView.findViewById(R.id.price_articleTv);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedArticle.selectedArticle(articleList.get(getAdapterPosition()));
            }
        });
     }
    }
}
