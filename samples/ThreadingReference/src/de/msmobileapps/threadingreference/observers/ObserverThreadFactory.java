package de.msmobileapps.threadingreference.observers;


import info.mschmitt.ak.Injector;
import info.mschmitt.ak.foundation.DefaultFoundationThreadFactory;
import info.mschmitt.ak.foundation.Timer;
import info.mschmitt.ak.runtime.IMP1;
import info.mschmitt.ak.runtime.SEL;

public class ObserverThreadFactory extends DefaultFoundationThreadFactory {
    public static final SEL SEL_DO_FIRE_TIMER = SEL.getInstance("doFireTimer:");
    public static final ObserverThreadFactory INSTANCE = new ObserverThreadFactory();
    private static final IMP1 IMP_DO_FIRE_TIMER = new IMP1<ObserverThread, Timer>() {
        @Override
        public void invoke(ObserverThread receiver, SEL sel, Timer nsTimer) {
            receiver.doFireTimer(nsTimer);
        }
    };

    protected ObserverThreadFactory() {
        super(Injector.getInstance().injectMethodPerformer());
    }
}
