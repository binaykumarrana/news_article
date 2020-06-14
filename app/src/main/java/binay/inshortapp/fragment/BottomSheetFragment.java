package binay.inshortapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import binay.inshortapp.R;

/**
 * Created by Binay on 09/09/17.
 */

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private int position = 0;
    private FilterListener mFilterListener;
    //Bottom Sheet Callback
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    public BottomSheetFragment() {
    }

    public BottomSheetFragment(int pos) {
        this.position = pos;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mFilterListener = (FilterListener) activity;
        } catch (ClassCastException c) {
            throw new ClassCastException(activity.toString() + " must implement BottomSheetSelectedListener");
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        //Get the content View
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
        dialog.setContentView(contentView);
        if (position == 1) {
            contentView.findViewById(R.id.llPublisher).setVisibility(View.VISIBLE);
            contentView.findViewById(R.id.llCategory).setVisibility(View.INVISIBLE);
            //Static publisher list for time being ..it can be stored locally and fetched in list
            String[] pList = new String[]{"Los Angeles Times", "Livemint", "IFA Magazine", "Moneynews", "NASDAQ", "MarketWatch",
                    "Reuters", "Businessweek", "GlobalPost", "euronews"};
            ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, pList);
            ListView listView = contentView.findViewById(R.id.lvPublisher);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mFilterListener.onApplyFilter(position, i+1);
                }
            });

        } else {
            contentView.findViewById(R.id.llPublisher).setVisibility(View.INVISIBLE);
            contentView.findViewById(R.id.llCategory).setVisibility(View.VISIBLE);
            contentView.findViewById(R.id.tvBusiness).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFilterListener.onApplyFilter(position, 1);
                }
            });
            contentView.findViewById(R.id.tvScTech).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFilterListener.onApplyFilter(position, 2);
                }
            });
            contentView.findViewById(R.id.tvEntertainment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFilterListener.onApplyFilter(position, 3);
                }
            });
            contentView.findViewById(R.id.tvHealth).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFilterListener.onApplyFilter(position, 4);
                }
            });
        }
        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        //Set callback
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    public interface FilterListener {
        void onApplyFilter(int filterType, int position);
    }
}
