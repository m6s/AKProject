package de.msmobileapps.threadingreference.customsources;

import de.msmobileapps.threadingreference.ThreadingViewController;
import info.mschmitt.ak.Injector;
import info.mschmitt.ak.coregraphics.GraphicsRect;
import info.mschmitt.ak.foundation.FoundationThreadFactory;
import info.mschmitt.ak.foundation.RunLoopFactory;
import info.mschmitt.ak.foundation.Timer;
import info.mschmitt.ak.foundation.TimerFactory;
import info.mschmitt.ak.runtime.Creatable;
import info.mschmitt.ak.runtime.Factory;
import info.mschmitt.ak.ui.*;
import info.mschmitt.ak.ui.staging.IBOutlet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Matthias Schmitt
 */
public class CustomSourcesAppDelegate implements UIApplicationDelegate, UIApplicationDelegate.Storyboarding, Creatable {
    private static Factory sFactory = CustomSourcesAppDelegateFactory.INSTANCE;
    private UIWindowFactory mWindowFactory = Injector.getInstance().injectUIWindowFactory();
    private UIScreen mMainScreen = Injector.getInstance().injectMainScreen();
    private FoundationThreadFactory mThreadFactory = Injector.getInstance().injectFoundationThreadFactory();
    private TimerFactory mTimerFactory = Injector.getInstance().injectTimerFactory();
    private RunLoopFactory mRunLoopFactory = Injector.getInstance().injectRunLoopFactory();
    private UIApplication mSharedApplication = Injector.getInstance().injectSharedApplication();
    private UIWindow mWindow;
    private ThreadingViewController mViewController;
    private List<TRRunLoopContext> mSourcesToPing = new ArrayList<TRRunLoopContext>();

    @Override
    public boolean onDidFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        GraphicsRect screenBounds = mMainScreen.getBounds();
        mWindow = mWindowFactory.create(screenBounds);
        mViewController = new ThreadingViewController();
        mWindow.addSubview(mViewController.getView());
        mWindow.makeKeyAndVisible();

        //Worker Thread
        mThreadFactory.create(this, CustomSourcesAppDelegateFactory.SEL_WORKER_THREAD_MAIN, null).start();

        //Main Thread
        mTimerFactory.schedule(2, this, CustomSourcesAppDelegateFactory.SEL_MAIN_THREAD_MAIN, null, true);
        return true;
    }

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
     * # viewController
     */
    @IBOutlet
    public ThreadingViewController viewController() {
        return mViewController;
    }

    public void setViewController(ThreadingViewController value) {
        mViewController = value;
    }

    public void workerThreadMain() {
        TRRunLoopSource runLoopSource = new TRRunLoopSource().init();
        runLoopSource.addToCurrentRunLoop();
        mRunLoopFactory.currentRunLoop().run();
    }

    public void mainThreadMain(Timer arg) {
        for (TRRunLoopContext runLoopContext : mSourcesToPing) {
            runLoopContext.source().addCommandWithData(1, "Foo");
            runLoopContext.source().fireAllCommandsOnRunLoop(runLoopContext.runLoop());
            runLoopContext.runLoop().wakeUp();
        }
    }

    public CustomSourcesAppDelegate sharedAppDelegate() {
        return (CustomSourcesAppDelegate) mSharedApplication.getDelegate();
    }

    public void registerSource(TRRunLoopContext sourceInfo) {
        mSourcesToPing.add(sourceInfo);
    }

    public void removeSource(TRRunLoopContext sourceInfo) {
        Iterator<TRRunLoopContext> it = mSourcesToPing.iterator();
        while (it.hasNext()) {
            if (sourceInfo.equals(it.next())) {
                it.remove();
            }
        }
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

    @Override
    public Factory getFactory() {
        return sFactory;
    }
}
