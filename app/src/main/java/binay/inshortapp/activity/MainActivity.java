package binay.inshortapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import binay.inshortapp.R;
import binay.inshortapp.fragment.ArticleCardsFragment;
import binay.inshortapp.interfaces.FilterListener;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    //Listener to apply filter
    FilterListener mFilterListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intialize listener
        mFilterListener = (FilterListener) this;

        //Attaching fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, ArticleCardsFragment.newInstance()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter_publisher) {
            if (mFilterListener != null)
                mFilterListener.onApplyFilter(1);
            return true;
        } else if (id == R.id.action_filter_category) {
            if (mFilterListener != null)
                mFilterListener.onApplyFilter(2);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
