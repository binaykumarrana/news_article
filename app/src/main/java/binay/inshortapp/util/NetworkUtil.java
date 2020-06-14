package binay.inshortapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import binay.inshortapp.InshortApplication;
import binay.inshortapp.R;

/**
 * Created by Binay on 09/09/17.
 */

public class NetworkUtil {
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    public static void showNoInternetAlertDialog(final Activity activity) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity, R.style.MyAlertDialogStyle);

        builder.setTitle("No internet connection");
        builder.setMessage("There seems to be no internet\nconnection. Check your\nconnection and try again.");

        builder.setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                dialog.dismiss();
                InshortApplication.getInstance().setNoInternetDialogVisibility(false);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                InshortApplication.getInstance().setNoInternetDialogVisibility(false);
            }
        });

        final androidx.appcompat.app.AlertDialog dialog;

        dialog = builder.create();
        InshortApplication.getInstance().setNoInternetDialogVisibility(true);

        dialog.setCancelable(true);
        dialog.show();

    }
}
