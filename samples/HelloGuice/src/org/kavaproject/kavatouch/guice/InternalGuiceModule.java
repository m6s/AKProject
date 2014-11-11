package org.kavaproject.kavatouch.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import info.mschmitt.ak.DeviceHandle;
import info.mschmitt.ak.internal.InternalModule;
import info.mschmitt.ak.internal.MotionEventHandler;
import info.mschmitt.ak.internal.ScreenRedrawObserver;
import info.mschmitt.ak.internal.SystemEventSource;
import info.mschmitt.ak.ui.UIApplication;
import info.mschmitt.ak.ui.UIEventFactory;
import info.mschmitt.ak.ui.UIScreen;
import info.mschmitt.ak.ui.UITouchFactory;

import javax.inject.Singleton;

public class InternalGuiceModule extends InternalModule implements Module {
    @Override
    public void configure(Binder binder) {
    }

    @Override
    @Provides
    @Singleton
    public SystemEventSource provideSystemEventSource(MotionEventHandler motionEventHandler, DeviceHandle
            deviceHandle) {
        return super.provideSystemEventSource(motionEventHandler, deviceHandle);
    }

    @Override
    @Provides
    @Singleton
    public ScreenRedrawObserver provideScreenRedrawObserver(UIApplication sharedUIApplication, DeviceHandle
            deviceHandle) {
        return super.provideScreenRedrawObserver(sharedUIApplication, deviceHandle);
    }

    @Override
    @Provides
    @Singleton
    public MotionEventHandler provideMotionEventHandler(UITouchFactory uiTouchFactory, UIEventFactory uiEventFactory, UIApplication sharedApplication, UIScreen mainScreen, DeviceHandle deviceHandle) {
        return super.provideMotionEventHandler(uiTouchFactory, uiEventFactory, sharedApplication, mainScreen, deviceHandle);
    }
}
