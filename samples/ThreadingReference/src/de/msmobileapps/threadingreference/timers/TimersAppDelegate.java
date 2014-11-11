package de.msmobileapps.threadingreference.timers;

import android.util.Log;
import de.msmobileapps.threadingreference.ThreadingViewController;
import info.mschmitt.ak.Injector;
import info.mschmitt.ak.corefoundation.CoreRunLoop;
import info.mschmitt.ak.corefoundation.CoreRunLoopTimer;
import info.mschmitt.ak.coregraphics.GraphicsRect;
import info.mschmitt.ak.foundation.*;
import info.mschmitt.ak.ui.*;
import info.mschmitt.ak.ui.staging.IBOutlet;

/**
 * @author Matthias Schmitt
 */
public class TimersAppDelegate implements UIApplicationDelegate, UIApplicationDelegate.Storyboarding {
    private UIWindowFactory mWindowFactory = Injector.getInstance().injectUIWindowFactory();
    private RunLoopFactory mRunLoopFactory = Injector.getInstance().injectRunLoopFactory();
    private FoundationThreadFactory mThreadFactory = Injector.getInstance().injectFoundationThreadFactory();
    private FoundationDateFactory mDateFactory = Injector.getInstance().injectFoundationDateFactory();
    private TimerFactory mTimerFactory = Injector.getInstance().injectTimerFactory();
    private UIScreen mMainScreen = Injector.getInstance().injectMainScreen();
    private UIWindow mWindow;
    private ThreadingViewController mViewController;

    @Override
    public boolean onDidFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        GraphicsRect screenBounds = mMainScreen.getBounds();
        mWindow = mWindowFactory.create(screenBounds);
        mViewController = new ThreadingViewController();
        mWindow.addSubview(mViewController.getView());
        mWindow.makeKeyAndVisible();

        listingWithOwnThread();
        return true;
    }

    /**
     * @return
     * @property (nonatomic, retain)
     */
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
     * @return
     * @property (nonatomic, retain)
     */
    @IBOutlet
    public ThreadingViewController viewController() {
        return mViewController;
    }

    public void setViewController(ThreadingViewController value) {
        mViewController = value;
    }


    private void listingWithOwnThread() {
        mThreadFactory.create(this, TimersAppDelegateFactory.SEL_LISTING_310, null).start();
    }

    public void listing311() {
        CoreRunLoop runLoop = CoreRunLoop.getCurrent();
        CoreRunLoopTimer timer = new CoreRunLoopTimer(0.1, 0.3, 0, new CoreRunLoopTimer.Handler() {
            @Override
            public void execute(CoreRunLoopTimer timer) {
                Log.d("FOO", "CF timer fired");
            }
        });
        runLoop.addTimer(timer, CoreRunLoop.MODES_COMMON);
        mRunLoopFactory.currentRunLoop().run();
    }

    public void listing310() {
        RunLoop myRunLoop = mRunLoopFactory.currentRunLoop();
        ///Create and schedule the first timer.///
        FoundationDate futureDate = mDateFactory.createWithTimeIntervalSinceNow(1.0);
        Timer myTimer = mTimerFactory.create(
                futureDate,
                0.1,
                this,
                TimersAppDelegateFactory.SEL_MY_DO_FIRE_TIMER_1,
                null,
                true);
        myRunLoop.addTimer(myTimer, RunLoop.MODE_DEFAULT);
        ///Create and schedule the second timer.///
        mTimerFactory.schedule(
                0.2,
                this,
                TimersAppDelegateFactory.SEL_MY_DO_FIRE_TIMER_2,
                null,
                true);
        mRunLoopFactory.currentRunLoop().run();
    }

    public void myDoFireTimer2(Timer anArgument) {
        Log.d("FOO", "Timer 2 fired");
    }

    public void myDoFireTimer1(Timer anArgument) {
        Log.d("FOO", "Timer 1 fired");
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
