package de.msmobileapps.threadingreference.cfportsources.multiprocessingservices;

public class MPTaskID {
    public Thread jThread;

    public MPTaskID(Thread jThread) {
        this.jThread = jThread;
    }

    @Override
    public String toString() {
        return String.valueOf(jThread.getId());
    }
}
