package de.msmobileapps.threadingreference.cfportsources;

import android.util.Log;
import de.msmobileapps.threadingreference.ThreadingViewController;
import de.msmobileapps.threadingreference.cfportsources.multiprocessingservices.MPTaskID;
import de.msmobileapps.threadingreference.cfportsources.multiprocessingservices.OSStatus;
import de.msmobileapps.threadingreference.cfportsources.multiprocessingservices.TaskProc;
import info.mschmitt.ak.Injector;
import info.mschmitt.ak.corefoundation.*;
import info.mschmitt.ak.coregraphics.GraphicsRect;
import info.mschmitt.ak.ui.UIApplication;
import info.mschmitt.ak.ui.UIApplicationDelegate;
import info.mschmitt.ak.ui.UIApplicationLaunchOptions;
import info.mschmitt.ak.ui.UIWindow;
import info.mschmitt.ak.ui.staging.IBOutlet;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Matthias Schmitt
 */
public class CFPortSourcesAppDelegate implements UIApplicationDelegate, UIApplicationDelegate.Storyboarding {
    public static final int K_CHECKIN_MESSAGE = 100;
    private TaskProc mServerThreadEntryPoint = new TaskProc() {

        @Override
        public OSStatus execute(Object param) {
            // Create the remote port to the main thread.
            String portName = (String) param;
            CoreMessagePort mainThreadPort = CoreMessagePort.createRemote(portName);
            // Store the port in this thread’s context info for later reference.
            // Create a port for the worker thread.
            String myPortName = "com.MyApp.Thread-" + new MPTaskID(Thread.currentThread());
            CoreMessagePort myPort = CoreMessagePort.createLocal(
                    myPortName,
                    new CoreMessagePort.ReceiveHandler() {
                        @Override
                        public ByteBuffer execute(CoreMessagePort local, int msgid, ByteBuffer data) {
                            //This is the port of the main thread, because we copied it into our context earlier
                            Log.d("FOO", "Server thread received.");
                            return null;
                        }
                    });
//            if (shouldFreeInfo[0]) {
//                // Couldn't create a local port, so kill the thread.
//                Multiprocessing.MPExit(0);
//            }
            CoreRunLoopSource rlSource = myPort.createRunLoopSource(0);
            if (rlSource == null) {
                // Couldn't create a local port, so kill the thread.
                Thread.currentThread().stop();
            }
            // Add the source to the current run loop.
            CoreRunLoop.getCurrent().addSource(rlSource, CoreRunLoop.MODE_DEFAULT);
            // Package up the port name and send the check-in message.
            int stringLength = myPortName.length();
            byte[] buffer = new byte[stringLength];
            CoreString.getBytes(
                    myPortName,
                    new CoreRange(0, stringLength),
                    CoreStringBuiltInEncodings.ASCII,
                    0,
                    false,
                    buffer,
                    null);
            ByteBuffer outData = ByteBuffer.wrap(buffer);
            mainThreadPort.sendRequest(K_CHECKIN_MESSAGE, outData, 0.1, 0.0, null, null);
            // Clean up thread data structures.
            // Enter the run loop.
            CoreRunLoop.run();
            return null;
        }
    };
    public static final int kThreadStackSize = (8 * 4096);
    public static Injector DI;
    UIWindow window;
    ThreadingViewController viewController;
    private Set<CoreMessagePort> mActiveThreads = new HashSet<CoreMessagePort>();

    @Override
    public boolean onDidFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        GraphicsRect screenBounds = Injector.getInstance().injectMainScreen().getBounds();
        window = Injector.getInstance().injectUIWindowFactory().create(screenBounds);
        viewController = new ThreadingViewController();
        window.addSubview(viewController.getView());
        window.makeKeyAndVisible();
        MySpawnThread();
        return true;
    }

    /**
     * @return
     * @property (nonatomic, retain)
     */
    @IBOutlet
    @Override
    public UIWindow getWindow() {
        return window;
    }

    @Override
    public void setWindow(UIWindow value) {
        window = value;
    }

    /**
     * @return
     * @property (nonatomic, retain)
     */
    @IBOutlet
    public ThreadingViewController viewController() {
        return viewController;
    }

    public void setViewController(ThreadingViewController value) {
        viewController = value;
    }

    private OSStatus MySpawnThread() {
        // Create a local port for receiving responses.
        // Create a string with the port name.
        final String myPortName = "com.myapp.MainThread";
        // Create the port.
        CoreMessagePort myPort = CoreMessagePort.createLocal(
                myPortName,
                new CoreMessagePort.ReceiveHandler() {
                    @Override
                    public ByteBuffer execute(CoreMessagePort local, int msgid, ByteBuffer data) {
                        Log.d("FOO", "App thread received.");
                        if (msgid == K_CHECKIN_MESSAGE) {
//                            int bufferLength = data.getLength();
//                            byte[] buffer = new byte[bufferLength];
//                            data.getBytes(new CFRange(0, bufferLength), buffer);
                            byte[] buffer = data.array();
                            String threadPortName = CoreString.create(buffer,
                                    buffer.length,
                                    CoreStringBuiltInEncodings.ASCII,
                                    false).toString();
                            // You must obtain a remote message port by name.
                            CoreMessagePort messagePort = CoreMessagePort.createRemote(threadPortName);
                            if (messagePort != null) {
                                // Retain and save the thread’s comm port for future reference.
                                AddPortToListOfActiveThreads(messagePort);
                            }
                        } else {
                            // Process other messages.
                        }
                        return null;
                    }

                    private void AddPortToListOfActiveThreads(CoreMessagePort messagePort) {
                        mActiveThreads.add(messagePort); //This is the port of the worker thread
                    }
                });
        if (myPort != null) {
            // The port was successfully created.
            // Now create a run loop source for it.
            CoreRunLoopSource rlSource = myPort.createRunLoopSource(0);
            if (rlSource != null) {
                // Add the source to the current run loop.
                CoreRunLoop.getCurrent().addSource(rlSource, CoreRunLoop.MODE_DEFAULT);
            }
        }
        // Create the thread and continue processing.
        MPTaskID[] taskID = new MPTaskID[1];
        Thread jThread = new Thread(new Runnable() {
            @Override
            public void run() {
                OSStatus result = mServerThreadEntryPoint.execute(myPortName);
            }
        });
        jThread.start();
        taskID[0] = new MPTaskID(jThread);
        OSStatus osStatus = null;
        return osStatus;
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
