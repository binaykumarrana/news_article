package binay.inshortapp.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import binay.inshortapp.R;
import binay.inshortapp.card.ArticleCard;
import binay.inshortapp.interfaces.FilterListener;
import binay.inshortapp.model.Article;
import binay.inshortapp.util.Constant;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

/**
 * Created by Binay on 08/09/17.
 */

public class ArticleCardsFragment extends BaseFragment implements FilterListener {

    private CardRecyclerView cardRecyclerView;
    private CardArrayRecyclerViewAdapter cardArrayRecyclerViewAdapter;
    private List<Card> cards;
    private ProgressBar progressBar;
    List<Article> articles;

    public static ArticleCardsFragment newInstance() {
        ArticleCardsFragment articleCardsFragment = new ArticleCardsFragment();
        return articleCardsFragment;
    }

    public ArticleCardsFragment() {
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_article_cards;
    }

    @Override
    protected void onViewReady(View view, Bundle savedInstanceState) {
        articles = new ArrayList<>();
        progressBar = (ProgressBar) view.findViewById(R.id.pbArticlesLoading);
        progressBar.setVisibility(View.VISIBLE);
        cardRecyclerView = (CardRecyclerView) view.findViewById(R.id.crvArticles);
        cards = new ArrayList<>();
        cardArrayRecyclerViewAdapter = new CardArrayRecyclerViewAdapter(getActivity(), cards);
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        cardRecyclerView.setAdapter(cardArrayRecyclerViewAdapter);

        updateArticles();
    }

    private void updateArticles() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Constant.API_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    Log.d("Success", "" + jsonArray.length() + jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Article article = new Article();
                        article.TITLE = jsonObject.getString("TITLE");
                        article.ID = jsonObject.getInt("ID");
                        article.URL = jsonObject.getString("URL");
                        article.PUBLISHER = jsonObject.getString("PUBLISHER");
                        article.CATEGORY = jsonObject.getString("CATEGORY");
                        article.HOSTNAME = jsonObject.getString("HOSTNAME");
                        article.TIMESTAMP = jsonObject.getLong("TIMESTAMP");
                        articles.add(article);

                    }
                    refreshList();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("OnFail", "" + error.getMessage());
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    private void refreshList() {
        for (int i = 0; i < articles.size(); i++) {
            ArticleCard articleCard = new ArticleCard(getActivity(), articles.get(i));
            cards.add(articleCard);
        }
        cardArrayRecyclerViewAdapter.addAll(cards);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Articles Fetched Successfully", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onApplyFilter(int position) {

    }
}
