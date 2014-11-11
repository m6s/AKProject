package de.msmobileapps.threadingreference;

import android.app.Activity;
import android.os.Bundle;
import de.msmobileapps.threadingreference.observers.ObserversAppDelegate;
import info.mschmitt.ak.DeviceHandle;
import info.mschmitt.ak.Injector;
import info.mschmitt.ak.coreanimation.SurfaceViewAnimationEngine;
import info.mschmitt.ak.ui.Session;
import info.mschmitt.ak.ui.UIApplicationDelegate;

/**
 * @author Matthias Schmitt
 */
public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_NoTitleBar);
        SurfaceViewAnimationEngine animationEngine = new SurfaceViewAnimationEngine(this) {
            @Override
            public Session getSession(DeviceHandle deviceHandle) {
                Injector.setInstance(new Injector(deviceHandle));
                return Injector.getInstance().injectSession();
            }

            @Override
            protected UIApplicationDelegate getDelegate() {
//                return new RunLoopsAppDelegate();
//                return new TimersAppDelegate();
//                return new CFPortSourcesAppDelegate();
//                return new PortSourcesAppDelegate();
                return new ObserversAppDelegate();
//                return new CustomSourcesAppDelegate();
            }
        };
        setContentView(animationEngine);
    }
}
