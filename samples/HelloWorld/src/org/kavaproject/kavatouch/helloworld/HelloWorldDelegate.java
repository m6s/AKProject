package org.kavaproject.kavatouch.helloworld;

import info.mschmitt.ak.Injector;
import info.mschmitt.ak.coregraphics.GraphicsRect;
import info.mschmitt.ak.foundation.FoundationTextAlignment;
import info.mschmitt.ak.ui.*;

import java.util.EnumSet;

/**
 * @author Matthias Schmitt
 */
public class HelloWorldDelegate implements UIApplicationDelegate, UIApplicationDelegate.Storyboarding {
    private UILabelFactory mLabelFactory = Injector.getInstance().injectUILabelFactory();
    private UIWindowFactory mWindowFactory = Injector.getInstance().injectUIWindowFactory();
    private UIScreen mMainScreen = Injector.getInstance().injectMainScreen();
    private UIColorFactory mColorFactory = Injector.getInstance().injectUIColorFactory();
    private UIWindow mWindow;

    @Override
    public UIWindow getWindow() {
        return mWindow;
    }

    @Override
    public void setWindow(UIWindow value) {
        mWindow = value;
    }

    @Override
    public boolean onWillFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean onDidFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        UILabel label = mLabelFactory.create(new GraphicsRect(100, 20, 120, 21));
        label.setText("Hello, World!");
        label.setTextAlignment(FoundationTextAlignment.CENTER);
        label.setAutoresizingMask(EnumSet.of(UIViewAutoresizing.FLEXIBLE_LEFT_MARGIN,
                UIViewAutoresizing.FLEXIBLE_RIGHT_MARGIN));
        mWindow = mWindowFactory.create(new GraphicsRect(0, 0, 320, 480));
        mWindow.addSubview(label);
        GraphicsRect frame = mMainScreen.getApplicationFrame();
        mWindow.setFrame(frame);
        mWindow.setBackgroundColor(mColorFactory.lightGray());
        mWindow.makeKeyAndVisible();
        return true;
    }

    @Override
    public void onDidBecomeActive(UIApplication application) {
    }

    @Override
    public void onWillResignActive(UIApplication application) {
    }

    @Override
    public void onDidEnterBackground(UIApplication application) {
    }

    @Override
    public void onWillEnterForeground(UIApplication application) {
    }

    @Override
    public void onWillTerminate(UIApplication application) {
    }
}
