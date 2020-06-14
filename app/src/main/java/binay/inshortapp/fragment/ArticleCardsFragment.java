package binay.inshortapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

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
import binay.inshortapp.activity.MainActivity;
import binay.inshortapp.card.ArticleCard;
import binay.inshortapp.model.Article;
import binay.inshortapp.util.Constant;
import co.uk.rushorm.core.RushSearch;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

/**
 * Created by Binay on 08/09/17.
 */
public class ArticleCardsFragment extends BaseFragment {

    private ProgressDialog progressDialog;
    private CardRecyclerView cardRecyclerView;
    private CardArrayRecyclerViewAdapter cardArrayRecyclerViewAdapter;
    private List<Card> cards;

    List<String> publisher = new ArrayList<>();
    /**
     * The Articles List.
     */
    List<Article> articles;

    /**
     * New instance article cards fragment.
     *
     * @return the article cards fragment
     */
    public static ArticleCardsFragment newInstance() {
        return new ArticleCardsFragment();
    }

    /**
     * Empty ArticleCardsFragment constructor.
     */
    public ArticleCardsFragment() {
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_article_cards;
    }

    @Override
    protected void onViewReady(View view, Bundle savedInstanceState) {
        init(view);
        updateArticles();

        //Updating  view data
        ((MainActivity) getActivity()).setOfflineListener(new MainActivity.MenuOptionListener() {
            @Override
            public void onOffline() {
                //from local persistence
                try {
                    List<Article> articleList = new RushSearch().find(Article.class);
                    if (articleList.size() > 0) {
                        if (cards != null)
                            cards.clear();
                        for (int i = 0; i < articleList.size(); i++) {
                            ArticleCard articleCard = new ArticleCard(getActivity(), articleList.get(i));
                            cards.add(articleCard);
                        }
                        cardArrayRecyclerViewAdapter = new CardArrayRecyclerViewAdapter(getActivity(), cards);
                        cardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        cardRecyclerView.setAdapter(cardArrayRecyclerViewAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSortOldNew() {

            }

            @Override
            public void onSortNewOld() {

            }

            @Override
            public void onFilterCategory(int position) {
                progressDialog.show();
                String constraint = "";
                if (position == 1) {
                    //getFilter().filter("b");
                    constraint = "b";
                } else if (position == 2) {
                    //getFilter().filter("t");
                    constraint = "t";
                } else if (position == 3) {
                    // getFilter().filter("e");
                    constraint = "e";
                } else if (position == 4) {
                    // getFilter().filter("m");
                    constraint = "m";
                }
                if (cards != null)
                    cards.clear();
                for (int i = 0; i < articles.size(); i++) {
                    if (articles.get(i).CATEGORY.equalsIgnoreCase(constraint)) {
                        ArticleCard articleCard = new ArticleCard(getActivity(), articles.get(i));
                        cards.add(articleCard);
                    }
                }
                cardArrayRecyclerViewAdapter = new CardArrayRecyclerViewAdapter(getActivity(), cards);
                cardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                cardRecyclerView.setAdapter(cardArrayRecyclerViewAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFilterPublisher(int position) {
                progressDialog.show();
                String[] pList = new String[]{"Los Angeles Times", "Livemint", "IFA Magazine", "Moneynews", "NASDAQ", "MarketWatch",
                        "Reuters", "Businessweek", "GlobalPost", "euronews"};
                if (cards != null)
                    cards.clear();
                for (int i = 0; i < articles.size(); i++) {
                    if (position > 0)
                        if (articles.get(i).PUBLISHER.equalsIgnoreCase(pList[position - 1])) {
                            ArticleCard articleCard = new ArticleCard(getActivity(), articles.get(i));
                            cards.add(articleCard);
                        }
                }
                cardArrayRecyclerViewAdapter = new CardArrayRecyclerViewAdapter(getActivity(), cards);
                cardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                cardRecyclerView.setAdapter(cardArrayRecyclerViewAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onOnline() {
                if (cards != null)
                    cards.clear();
                refreshList();
            }
        });
    }

    //Fetch articles from API
    private void updateArticles() {
        progressDialog.show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Constant.API_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
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
                        if (!publisher.contains(jsonObject.getString("PUBLISHER"))) {
                            publisher.add(jsonObject.getString("PUBLISHER"));
                        }
                        //Save publisher locally
                        /*if (jsonObject.getString("PUBLISHER") != null) {
                            Publisher publisher = new Publisher();
                            publisher.setPublisherName(jsonObject.getString("PUBLISHER"));
                            try {
                                List<Publisher> publisherList = new RushSearch()
                                        .whereEqual("publisherName", jsonObject.getString("PUBLISHER"))
                                        .find(Publisher.class);
                                if (publisherList.size() == 0) {
                                    publisher.save();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }*/
                    }

                    refreshList();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });

        //submitting request to server
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    private void init(View view) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading articles...");
        articles = new ArrayList<>();
        cardRecyclerView = view.findViewById(R.id.crvArticles);
        cards = new ArrayList<>();
        cardArrayRecyclerViewAdapter = new CardArrayRecyclerViewAdapter(getActivity(), cards);
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        cardRecyclerView.setAdapter(cardArrayRecyclerViewAdapter);
    }

    private void refreshList() {
        for (int i = 0; i < articles.size(); i++) {
            ArticleCard articleCard = new ArticleCard(getActivity(), articles.get(i));
            cards.add(articleCard);
        }
        cardArrayRecyclerViewAdapter.addAll(cards);
        cardArrayRecyclerViewAdapter.notifyDataSetChanged();
        progressDialog.dismiss();


    }


}
