package de.msmobileapps.threadingreference.customsources;

import info.mschmitt.ak.foundation.Timer;
import info.mschmitt.ak.runtime.Factory;
import info.mschmitt.ak.runtime.IMP0;
import info.mschmitt.ak.runtime.IMP1;
import info.mschmitt.ak.runtime.SEL;

/**
 * @author Matthias Schmitt
 */
public class CustomSourcesAppDelegateFactory implements Factory {
    public static final SEL SEL_WORKER_THREAD_MAIN = SEL.getInstance("workerThreadMain");
    public static final SEL SEL_MAIN_THREAD_MAIN = SEL.getInstance("mainThreadMain:");
    public static final CustomSourcesAppDelegateFactory INSTANCE = new CustomSourcesAppDelegateFactory();

    static {
    }

    private static final IMP0 IMP_WORKER_THREAD_MAIN = new IMP0<CustomSourcesAppDelegate>() {
        @Override
        public void invoke(CustomSourcesAppDelegate receiver, SEL sel) {
            receiver.workerThreadMain();
        }
    };
    private static final IMP1 IMP_MAIN_THREAD_MAIN = new IMP1<CustomSourcesAppDelegate, Timer>() {
        @Override
        public void invoke(CustomSourcesAppDelegate receiver, SEL sel, Timer nsTimer) {
            receiver.mainThreadMain(nsTimer);
        }
    };
    public static SEL SEL_REGISTER_SOURCE = SEL.getInstance("registerSource:");
    public static SEL SEL_REMOVE_SOURCE = SEL.getInstance("removeSource:");
    private static IMP1 IMP_REGISTER_SOURCE = new IMP1<CustomSourcesAppDelegate, TRRunLoopContext>() {
        @Override
        public void invoke(CustomSourcesAppDelegate receiver, SEL sel, TRRunLoopContext trRunLoopContext) {
            receiver.registerSource(trRunLoopContext);
        }
    };
    private static IMP1 IMP_REMOVE_SOURCE = new IMP1<CustomSourcesAppDelegate, TRRunLoopContext>() {
        @Override
        public void invoke(CustomSourcesAppDelegate receiver, SEL sel, TRRunLoopContext trRunLoopContext) {
            receiver.removeSource(trRunLoopContext);
        }
    };
}
