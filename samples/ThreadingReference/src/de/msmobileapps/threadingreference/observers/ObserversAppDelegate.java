package de.msmobileapps.threadingreference.observers;

import de.msmobileapps.threadingreference.ThreadingViewController;
import info.mschmitt.ak.Injector;
import info.mschmitt.ak.coregraphics.GraphicsRect;
import info.mschmitt.ak.ui.*;
import info.mschmitt.ak.ui.staging.IBOutlet;

/**
 * @author Matthias Schmitt
 */
public class ObserversAppDelegate implements UIApplicationDelegate, UIApplicationDelegate.Storyboarding {
    private UIWindowFactory mWindowFactory = Injector.getInstance().injectUIWindowFactory();
    private UIScreen mMainScreen = Injector.getInstance().injectMainScreen();
    private UIWindow mWindow;
    private ThreadingViewController mViewController;

    @IBOutlet
    @Override
    public UIWindow getWindow() {
        return mWindow;
    }

    @Override
    public void setWindow(UIWindow value) {
        mWindow = value;
    }

    /**
     * @property (nonatomic, retain)
     */
    @IBOutlet
    public ThreadingViewController viewController() {
        return mViewController;
    }

    public void setViewController(ThreadingViewController value) {
        mViewController = value;
    }

    @Override
    public boolean onDidFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        GraphicsRect screenBounds = mMainScreen.getBounds();
        mWindow = mWindowFactory.create(screenBounds);
        mViewController = new ThreadingViewController();
        mWindow.addSubview(mViewController.getView());
        mWindow.makeKeyAndVisible();
        new ObserverThread().start();
        return true;
    }


    @Override
    public boolean onWillFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        throw new UnsupportedOperationException();
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
