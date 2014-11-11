package de.msmobileapps.threadingreference.portsources;

import android.util.Log;
import de.msmobileapps.threadingreference.ThreadingViewController;
import info.mschmitt.ak.Injector;
import info.mschmitt.ak.coregraphics.GraphicsRect;
import info.mschmitt.ak.foundation.*;
import info.mschmitt.ak.runtime.Factory;
import info.mschmitt.ak.ui.*;
import info.mschmitt.ak.ui.staging.IBOutlet;

/**
 * "Received message from worker thread"
 *
 * @author Matthias Schmitt
 */
public class PortSourcesAppDelegate implements UIApplicationDelegate, UIApplicationDelegate.Storyboarding, PortDelegate {
    public static final int K_CHECKIN_MESSAGE = 100;
    private UIWindowFactory mWindowFactory = Injector.getInstance().injectUIWindowFactory();
    private UIScreen mMainScreen = Injector.getInstance().injectMainScreen();
    private RunLoopFactory mRunLoopFactory = Injector.getInstance().injectRunLoopFactory();
    private MessagePortFactory mMessagePortFactory = Injector.getInstance().injectMessagePortFactory();
    private MachPortFactory mMachPortFactory = Injector.getInstance().injectMachPortFactory();
    private MessagePortNameServer mMessagePortNameServer = Injector.getInstance().injectSharedMessagePortNameServer();
    private FoundationThreadFactory mFoundationThreadFactory = Injector.getInstance().injectFoundationThreadFactory();
    private UIWindow mWindow;
    private ThreadingViewController mViewController;
    private Port mDistantPort;

    /**
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
        launchThread312To315();
//        launchThread316();
        return true;
    }

    private void launchThread316() {
        Port localPort = mMessagePortFactory.create();
        // Configure the object and add it to the current run loop.
        localPort.setDelegate(this);
        mRunLoopFactory.currentRunLoop().addPort(localPort, RunLoop.MODE_DEFAULT);
        // Register the port using a specific name. The name must be unique.
        String localPortName = "MyPortName";
        mMessagePortNameServer.registerPortName(localPort, localPortName);
    }

    private void launchThread312To315() {
        Port myPort = mMachPortFactory.createPort();
        if (myPort != null) {
            // This class handles incoming port messages.
            myPort.setDelegate(this);
            // Install the port as an input source on the current run loop.
            mRunLoopFactory.currentRunLoop().addPort(myPort, RunLoop.MODE_DEFAULT);
            // Detach the thread. Let the worker release the port.
            mFoundationThreadFactory.detachNewThread(MyWorkerClassFactory.SEL_LAUNCH_THREAD_WITH_PORT,
                    MyWorkerClassFactory.INSTANCE,
                    myPort);
        }
    }

    // Handle responses from the worker thread.
    @Override
    public void handlePortMessage(PortMessage portMessage) {
        int message = portMessage.msgid();
        Port distantPort = null;
        if (message == K_CHECKIN_MESSAGE) {
            // Get the worker thread’s communications port.
            distantPort = portMessage.sendPort();
            // Retain and save the worker port for later use.
            storeDistantPort(distantPort);
        } else {
            // Handle other messages.
        }
    }

    private void storeDistantPort(Port distantPort) {
        mDistantPort = distantPort;
        Log.d("FOO", "Received message from worker thread");
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
        return null;
    }
}
