package de.msmobileapps.threadingreference.observers;

import android.util.Log;
import info.mschmitt.ak.Injector;
import info.mschmitt.ak.corefoundation.CoreRunLoop;
import info.mschmitt.ak.corefoundation.CoreRunLoopActivity;
import info.mschmitt.ak.corefoundation.CoreRunLoopObserver;
import info.mschmitt.ak.foundation.*;

/**
 * Timer fired
 * Observer firedk CFRunLoopBeforeTimers
 * Observer firedk CFRunLoopBeforeSources
 * Observer firedk CFRunLoopBeforeWaiting
 * Observer fired kCFRunLoopAfterWaiting
 * <p/>
 * for 10 seconds
 */
public class ObserverThread extends DefaultFoundationThread {
    FoundationDateFactory foundationDateFactory = Injector.getInstance().injectFoundationDateFactory();
    TimerFactory timerFactory = Injector.getInstance().injectTimerFactory();
    RunLoopFactory runLoopFactory = Injector.getInstance().injectRunLoopFactory();

    public ObserverThread() {
        super((DefaultFoundationThreadFactory) Injector.getInstance().injectFoundationThreadFactory(), Injector.getInstance().injectMethodPerformer());
    }

    @Override
    public void main() {
        setName("ObserverThread");
        RunLoop myRunLoop = runLoopFactory.currentRunLoop();
        // Create a run loop observer and attach it to the run loop.
        CoreRunLoopObserver observer = new CoreRunLoopObserver(CoreRunLoopActivity.ALL_ACTIVITIES, true, 0,
                new CoreRunLoopObserver.Handler() {
                    @Override
                    public void execute(CoreRunLoopObserver observer, CoreRunLoopActivity activity) {
                        Log.d("FOO", "Observer fired " + activity);
                    }
                });
        if (observer != null) {
            CoreRunLoop cfLoop = myRunLoop.toCoreType();
            cfLoop.addObserver(observer, CoreRunLoop.MODE_DEFAULT);
        }
        // Create and schedule the timer.
        timerFactory.schedule(0.1,
                this,
                ObserverThreadFactory.SEL_DO_FIRE_TIMER,
                null,
                true);

        int loopCount = 10;
        do {
            // Run the run loop 10 times to let the timer fire.
            myRunLoop.runUntil(foundationDateFactory.createWithTimeIntervalSinceNow(1));
            loopCount--;
        } while (loopCount != 0);
    }

    public void doFireTimer(Timer theTimer) {
        Log.d("FOO", "Timer fired");
    }
}
