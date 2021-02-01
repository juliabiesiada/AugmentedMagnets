package fr.ups;

import android.util.Log;
import fr.ups.wikitude.rendering.CustomSurfaceView;
import fr.ups.wikitude.rendering.Driver;
import fr.ups.wikitude.rendering.GLRenderer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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
import fr.ups.wikitude.rendering.StrokedRectangle;

public class MainActivity extends AppCompatActivity implements ImageTrackerListener, ExternalRendering {

    private static final String TAG = "MainActivity";
    private WikitudeSDK wikitudeSDK;
    private CustomSurfaceView customSurfaceView;
    private Driver driver;
    private GLRenderer glRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wikitudeSDK = new WikitudeSDK(this);
        NativeStartupConfiguration startupConfiguration = new NativeStartupConfiguration();
        startupConfiguration.setLicenseKey(getString(R.string.LICENSE_KEY));
        startupConfiguration.setCameraPosition(CameraSettings.CameraPosition.BACK);
        startupConfiguration.setCameraResolution(CameraSettings.CameraResolution.AUTO);

        wikitudeSDK.onCreate(getApplicationContext(), this, startupConfiguration);

        final TargetCollectionResource targetCollectionResource = wikitudeSDK.getTrackerManager().createTargetCollectionResource("../../res/target/target.wtc");
        wikitudeSDK.getTrackerManager().createImageTracker(targetCollectionResource, this, null);
    }

    @Override
    public void onRenderExtensionCreated(RenderExtension renderExtension) {
        glRenderer = new GLRenderer(renderExtension);
        customSurfaceView = new CustomSurfaceView(getApplicationContext(), glRenderer);
        Driver driver = new Driver(customSurfaceView, 30);
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
}
