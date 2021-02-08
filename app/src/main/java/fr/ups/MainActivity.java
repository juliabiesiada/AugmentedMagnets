package fr.ups;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wikitude.NativeStartupConfiguration;
import com.wikitude.WikitudeSDK;
import com.wikitude.common.WikitudeError;
import com.wikitude.common.camera.CameraSettings;
import com.wikitude.common.rendering.RenderExtension;
import com.wikitude.rendering.ExternalRendering;
import com.wikitude.tracker.ImageTarget;
import com.wikitude.tracker.ImageTracker;
import com.wikitude.tracker.ImageTrackerListener;
import com.wikitude.tracker.TargetCollectionResource;
import fr.ups.wikitude.rendering.CustomSurfaceView;
import fr.ups.wikitude.rendering.Driver;
import fr.ups.wikitude.rendering.GLRenderer;
import fr.ups.wikitude.rendering.StrokedRectangle;
import fr.ups.wikitude.samples.WikitudeSDKConstants;

public class MainActivity extends Activity implements ImageTrackerListener, ExternalRendering {

    private static final String TAG = "MainActivity";
    private WikitudeSDK wikitudeSDK;
    private CustomSurfaceView customSurfaceView;
    private Driver driver;
    private GLRenderer glRenderer;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("notes");

        wikitudeSDK = new WikitudeSDK(this);
        NativeStartupConfiguration startupConfiguration = new NativeStartupConfiguration();
        startupConfiguration.setLicenseKey(WikitudeSDKConstants.WIKITUDE_SDK_KEY);
        startupConfiguration.setCameraPosition(CameraSettings.CameraPosition.BACK);
        startupConfiguration.setCameraResolution(CameraSettings.CameraResolution.AUTO);

        wikitudeSDK.onCreate(getApplicationContext(), this, startupConfiguration);

        final TargetCollectionResource targetCollectionResource = wikitudeSDK.getTrackerManager().createTargetCollectionResource("file:///android_asset/tracker.wtc");
        wikitudeSDK.getTrackerManager().createImageTracker(targetCollectionResource, this, null);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    protected void onResume() {
        super.onResume();
        wikitudeSDK.onResume();
        customSurfaceView.onResume();
        driver.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        customSurfaceView.onPause();
        driver.stop();
        wikitudeSDK.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wikitudeSDK.onDestroy();
    }

    @Override
    public void onRenderExtensionCreated(RenderExtension renderExtension) {
        glRenderer = new GLRenderer(renderExtension);
        wikitudeSDK.getCameraManager().setRenderingCorrectedFovChangedListener(glRenderer);
        customSurfaceView = new CustomSurfaceView(getApplicationContext(), glRenderer);
        driver = new Driver(customSurfaceView, 30);
        setContentView(customSurfaceView);
    }

    @Override
    public void onTargetsLoaded(ImageTracker imageTracker) {
        Log.v(TAG, "Image tracker loaded");
    }

    @Override
    public void onErrorLoadingTargets(ImageTracker imageTracker, WikitudeError error) {
        Log.v(TAG, "Unable to load image tracker. Reason: " + error.getMessage());
    }

    @Override
    public void onImageRecognized(ImageTracker imageTracker, ImageTarget target) {
        Log.v(TAG, "Recognized target " + target.getName());
        StrokedRectangle strokedRectangle = new StrokedRectangle(StrokedRectangle.Type.STANDARD);
        glRenderer.setRenderablesForKey(target.getName() + target.getUniqueId(), strokedRectangle, null);
        sendData();
    }

    @Override
    public void onImageTracked(ImageTracker imageTracker, ImageTarget target) {
        StrokedRectangle strokedRectangle = (StrokedRectangle) glRenderer.getRenderableForKey(target.getName() + target.getUniqueId());

        if (strokedRectangle != null) {
            strokedRectangle.viewMatrix = target.getViewMatrix();

            strokedRectangle.setXScale(target.getTargetScale().x);
            strokedRectangle.setYScale(target.getTargetScale().y);
        }
    }

    @Override
    public void onImageLost(ImageTracker imageTracker, ImageTarget target) {
        Log.v(TAG, "Lost target " + target.getName());
        glRenderer.removeRenderablesForKey(target.getName() + target.getUniqueId());
    }

    @Override
    public void onExtendedTrackingQualityChanged(ImageTracker imageTracker, ImageTarget imageTarget, int i, int i1) {

    }

    public void sendData() {

        final int[] curr_id = {0};

        ConnectionHelper connHelper = new ConnectionHelper(this);
        if(connHelper.isConnected()) {
            NotesHelper notesHelper = new NotesHelper(2,1,"text", "Welcome", null);
            reference.child("nid").setValue(notesHelper);
        }else {
            connHelper.ShowNoConnectionDialog();
        }


    }
}
