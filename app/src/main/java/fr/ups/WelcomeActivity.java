package fr.ups;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.wikitude.WikitudeSDK;
import com.wikitude.common.permission.PermissionManager;
import fr.ups.MainActivity;
import fr.ups.R;

import java.util.Arrays;

public class WelcomeActivity extends AppCompatActivity {

    private Button startButton;
    private final PermissionManager permissionManager = WikitudeSDK.getPermissionManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        startButton = findViewById(R.id.startBtn);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] permissions = new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.INTERNET,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                permissionManager.checkPermissions(WelcomeActivity.this, permissions,
                        PermissionManager.WIKITUDE_PERMISSION_REQUEST,
                        new PermissionManager.PermissionManagerCallback() {
                            @Override
                            public void permissionsGranted(int requestCode) {
                                final Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void permissionsDenied(@NonNull String[] deniedPermissions) {
                                Toast.makeText(WelcomeActivity.this,
                                        getString(R.string.permissions_denied) + Arrays.toString(deniedPermissions),
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void showPermissionRationale(final int requestCode, @NonNull String[] strings) {
                                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(WelcomeActivity.this);
                                alertBuilder.setCancelable(true);
                                alertBuilder.setTitle(R.string.permission_rationale_title);
                                alertBuilder.setMessage(getString(R.string.permission_rationale_text) + Arrays.toString(permissions));
                                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        permissionManager.positiveRationaleResult(requestCode, permissions);
                                    }
                                });
                                AlertDialog alert = alertBuilder.create();
                                alert.show();
                            }
                        });

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        WikitudeSDK.getPermissionManager().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
