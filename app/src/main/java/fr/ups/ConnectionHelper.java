package fr.ups;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.provider.Settings;

import java.net.NetworkInterface;

public class ConnectionHelper {

    Context context;

    public ConnectionHelper(Context ctx) {
        this.context = ctx;
    }

    public boolean isConnected() {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo wifiConn = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifiConn != null && wifiConn.isConnected() || mobileConn != null && mobileConn.isConnected()) {
            return true;
        }else {
            return false;
        }
    }

    public void ShowNoConnectionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Please connect to the internet to proceed")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        context.startActivity(new Intent(context, WelcomeActivity.class));
                    }
                });

    }
}
