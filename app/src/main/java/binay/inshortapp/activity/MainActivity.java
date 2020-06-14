package binay.inshortapp.activity;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import binay.inshortapp.InshortApplication;
import binay.inshortapp.R;
import binay.inshortapp.fragment.ArticleCardsFragment;
import binay.inshortapp.fragment.BottomSheetFragment;
import binay.inshortapp.util.NetworkUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements BottomSheetFragment.FilterListener {

    //Bottomsheet for filter options
    private BottomSheetDialogFragment bottomSheetDialogFragment;

    //Listener to handler filter, sort and offline and online view
    private MenuOptionListener fragmentRefreshListener;

    //Method for offline listener to handle getter
    public MenuOptionListener getOfflineListener() {
        return fragmentRefreshListener;
    }

    //Method for offline listener to handle setter
    public void setOfflineListener(MenuOptionListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }


    public interface MenuOptionListener {
        void onOffline();

        void onSortOldNew();

        void onSortNewOld();

        void onFilterCategory(int pos);

        void onFilterPublisher(int pos);

        void onOnline();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * Checking internet is connected or not if not show setting dialog
        * */
        if (NetworkUtil.isOnline(MainActivity.this)) {
            //Attaching fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, ArticleCardsFragment.newInstance()).commit();
        } else {
            if (!InshortApplication.getInstance().getNoInternetDialogVisibility())
                NetworkUtil.showNoInternetAlertDialog(MainActivity.this);
        }

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
            //Show the Bottom Sheet Fragment
            bottomSheetDialogFragment = new BottomSheetFragment(1);
            bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
            return true;
        } else if (id == R.id.action_filter_category) {
            //Show the Bottom Sheet Fragment
            bottomSheetDialogFragment = new BottomSheetFragment(2);
            bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
            return true;

        } else if (id == R.id.action_sort_old_new) {
            if (getOfflineListener() != null) {
                getOfflineListener().onSortOldNew();
            }
            return true;

        } else if (id == R.id.action_sort_new_old) {
            if (getOfflineListener() != null) {
                getOfflineListener().onSortNewOld();
            }
            return true;

        } else if (id == R.id.action_offline) {
            if (getOfflineListener() != null) {
                getOfflineListener().onOffline();
            }
            return true;

        } else if (id == R.id.action_online) {
            if (getOfflineListener() != null) {
                getOfflineListener().onOnline();
            }
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onApplyFilter(int filterType, int position) {
        bottomSheetDialogFragment.dismiss();
        if (filterType == 1) {
            if (getOfflineListener() != null) {
                getOfflineListener().onFilterPublisher(position);
            }
        } else if (filterType == 2) {
            if (getOfflineListener() != null) {
                getOfflineListener().onFilterCategory(position);
            }
        }

    }


}
