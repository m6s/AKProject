package de.msmobileapps.threadingreference.timers;

import info.mschmitt.ak.foundation.Timer;
import info.mschmitt.ak.runtime.*;

/**
 * TODO
 */
public class TimersAppDelegateFactory implements Factory {
    public static final SEL SEL_LISTING_311 = SEL.getInstance("listing311");
    public static final SEL SEL_LISTING_310 = SEL.getInstance("listing310");
    public static final SEL SEL_MY_DO_FIRE_TIMER_1 = SEL.getInstance("myDoFireTimer1");
    public static final SEL SEL_MY_DO_FIRE_TIMER_2 = SEL.getInstance("myDoFireTimer2");
    public static final TimersAppDelegateFactory INSTANCE = new TimersAppDelegateFactory();

    static {
    }

    private static IMP IMP_LISTING_311 = new IMP0<TimersAppDelegate>() {
        @Override
        public void invoke(TimersAppDelegate receiver, SEL sel) {
            receiver.listing311();
        }
    };
    private static IMP IMP_LISTING_310 = new IMP0<TimersAppDelegate>() {
        @Override
        public void invoke(TimersAppDelegate receiver, SEL sel) {
            receiver.listing310();
        }
    };
    private static IMP IMP_MY_DO_FIRE_TIMER_1 = new IMP1<TimersAppDelegate, Timer>() {
        @Override
        public void invoke(TimersAppDelegate receiver, SEL sel, Timer nsTimer) {
            receiver.myDoFireTimer1(nsTimer);
        }
    };
    private static IMP IMP_MY_DO_FIRE_TIMER_2 = new IMP1<TimersAppDelegate, Timer>() {
        @Override
        public void invoke(TimersAppDelegate receiver, SEL sel, Timer nsTimer) {
            receiver.myDoFireTimer2(nsTimer);
        }
    };
}
