package de.msmobileapps.threadingreference.portsources;

import info.mschmitt.ak.Injector;
import info.mschmitt.ak.foundation.FoundationDateFactory;
import info.mschmitt.ak.foundation.Port;
import info.mschmitt.ak.foundation.RunLoop;
import info.mschmitt.ak.foundation.RunLoopFactory;
import info.mschmitt.ak.runtime.Factory;
import info.mschmitt.ak.runtime.IMP;
import info.mschmitt.ak.runtime.IMP1;
import info.mschmitt.ak.runtime.SEL;

public class MyWorkerClassFactory implements Factory {
    public static final SEL SEL_LAUNCH_THREAD_WITH_PORT = SEL.getInstance("LaunchThreadWithPort:");
    public static final MyWorkerClassFactory INSTANCE = new MyWorkerClassFactory();
    private static final IMP META_IMP_LAUNCH_THREAD_WITH_PORT = new IMP1<MyWorkerClassFactory, Port>() {
        @Override
        public void invoke(MyWorkerClassFactory receiver, SEL sel, Port nsPort) {
            receiver.LaunchThreadWithPort(nsPort);
        }
    };
    RunLoopFactory runLoopFactory = Injector.getInstance().injectRunLoopFactory();
    FoundationDateFactory foundationDateFactory = Injector.getInstance().injectFoundationDateFactory();

    static {
    }

    public void LaunchThreadWithPort(Object inData) {
        // Set up the connection between this thread and the main thread.
        Port distantPort = (Port) inData;
        MyWorkerClass workerObj = new MyWorkerClass();
        workerObj.sendCheckinMessage(distantPort);
        // Let the run loop process things.
        do {
            runLoopFactory.currentRunLoop().runUntil(RunLoop.MODE_DEFAULT, foundationDateFactory.distantFuture());
        } while (!workerObj.shouldExit());

    }
}
