package binay.inshortapp.card;

import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import binay.inshortapp.R;
import binay.inshortapp.activity.ArticleDetailsActivity;
import binay.inshortapp.model.Article;
import co.uk.rushorm.core.RushCallback;
import co.uk.rushorm.core.RushSearch;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Binay on 08/09/17.
 */

public class ArticleCard extends Card {
    private Article article;
    private Context mContext;

    //Constructor
    public ArticleCard(Context context, Article article) {
        super(context, R.layout.article_card_inner_layout);
        this.article = article;
        this.mContext = context;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView tvArticleTitle = parent.findViewById(R.id.tvArticleTitle);
        TextView tvArticleUrl = parent.findViewById(R.id.tvArticleUrl);
        TextView tvArticleHostName = parent.findViewById(R.id.tvArticleHost);
        TextView tvArticlePublisher = parent.findViewById(R.id.tvArticlePublisher);
        TextView tvArticleTime = parent.findViewById(R.id.tvArticleTime);
        LinearLayout cardItem = parent.findViewById(R.id.llItemView);
        final ImageButton favButton = parent.findViewById(R.id.ibFav);
        //Set Item to view
        tvArticleTitle.setText(article.TITLE);
        tvArticleHostName.setText(article.HOSTNAME);
        tvArticleHostName.setLinksClickable(true);
        tvArticleHostName.setMovementMethod(LinkMovementMethod.getInstance());
        tvArticlePublisher.setText(article.PUBLISHER);
        tvArticleUrl.setText(article.URL);
        tvArticleUrl.setLinksClickable(true);
        tvArticleUrl.setMovementMethod(LinkMovementMethod.getInstance());
        tvArticleTime.setText(getTime(article.TIMESTAMP));

        //Fetching particular article and checking that exist in local db
        try {
            List<Article> articleList = new RushSearch().whereEqual("ID", article.ID).find(Article.class);
            if (articleList.size() > 0)
                article.isFavArticle = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (article.isFavArticle) {
            favButton.setImageResource(R.drawable.ic_favorite_red_200_24dp);
        } else {
            favButton.setImageResource(R.drawable.ic_favorite);
        }

        /*
        * Clicklistener for opening article details page
        * */
        cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, ArticleDetailsActivity.class).putExtra("URL", article.URL).putExtra("HOST", article.PUBLISHER));
            }
        });

        /*
        * Listener for saving fav article
        * */
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (article.isFavArticle) {
                    List<Article> articleList = new RushSearch()
                            .whereEqual("ID", article.ID)
                            .find(Article.class);
                    articleList.get(0).delete(new RushCallback() {
                        @Override
                        public void complete() {
                            article.isFavArticle = false;
                            favButton.setImageResource(R.drawable.ic_favorite);
                            Log.d("BIUFHFU", "delete");
                        }
                    });
                } else {
                    article.save(new RushCallback() {
                        @Override
                        public void complete() {
                            article.isFavArticle = true;
                            favButton.setImageResource(R.drawable.ic_favorite_red_200_24dp);
                            Log.d("BIUFHFU", "save");
                        }
                    });

                }

            }
        });
    }


    private String getTime(long unixTimeStamp) {
        Date date = new Date(unixTimeStamp * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
        simpleDateFormat.setTimeZone(TimeZone.getDefault()); // give a timezone reference for formating (see comment at the bottom
        return simpleDateFormat.format(date);
    }
}
