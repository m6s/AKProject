package de.msmobileapps.threadingreference.runloops;

import android.util.Log;
import info.mschmitt.ak.Injector;
import info.mschmitt.ak.corefoundation.CoreRunLoop;
import info.mschmitt.ak.corefoundation.CoreRunLoopExitReason;
import info.mschmitt.ak.foundation.DefaultFoundationThread;
import info.mschmitt.ak.foundation.DefaultFoundationThreadFactory;

public class RunLoopThread extends DefaultFoundationThread {
    protected RunLoopThread() {
        super((DefaultFoundationThreadFactory) Injector.getInstance().injectFoundationThreadFactory(), Injector.getInstance().injectMethodPerformer());
    }

    @Override
    public void main() {
        // Set up an autorelease pool here if not using garbage collection.
        boolean done = false;

        // Add your sources or timers to the run loop and do any other setup.
        int i = 0;
        do {
            Log.d("FOO", "loop: " + i++);
            // Start the run loop but return after each source is handled.
            CoreRunLoopExitReason result = CoreRunLoop.runInMode(CoreRunLoop.MODE_DEFAULT, 3, true);

            // If a source explicitly stopped the run loop, or if there are no
            // sources or timers, go ahead and exit.
            if ((result == CoreRunLoopExitReason.STOPPED) || (result == CoreRunLoopExitReason.FINISHED)) {
                done = true;
            }

            // Check for any other exit conditions here and set the
            // done variable as needed.
        } while (!done);

        // Clean up code here. Be sure to release any allocated autorelease pools.
    }
}
