package org.kavaproject.kavatouch.helloworld;

import android.app.Activity;
import android.os.Bundle;
import info.mschmitt.ak.DeviceHandle;
import info.mschmitt.ak.Injector;
import info.mschmitt.ak.coreanimation.SurfaceViewAnimationEngine;
import info.mschmitt.ak.ui.Session;
import info.mschmitt.ak.ui.UIApplicationDelegate;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SurfaceViewAnimationEngine animationEngine = new SurfaceViewAnimationEngine(this) {
            @Override
            public Session getSession(DeviceHandle deviceHandle) {
                Injector.setInstance(new Injector(deviceHandle));
                return Injector.getInstance().injectSession();
            }

            @Override
            protected UIApplicationDelegate getDelegate() {
                return new HelloWorldDelegate();
            }
        };
        setContentView(animationEngine);
    }
}
