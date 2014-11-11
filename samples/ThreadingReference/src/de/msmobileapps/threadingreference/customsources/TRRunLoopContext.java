package de.msmobileapps.threadingreference.customsources;

import info.mschmitt.ak.corefoundation.CoreRunLoop;

public class TRRunLoopContext {
    private CoreRunLoop runLoop;
    private TRRunLoopSource source;

    public CoreRunLoop runLoop() {
        return runLoop;
    }

    public TRRunLoopSource source() {
        return source;
    }

    public TRRunLoopContext initWithSourceAndLoop(TRRunLoopSource src, CoreRunLoop loop) {
        source = src;
        runLoop = loop;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof TRRunLoopContext)) {
            return false;
        }
        TRRunLoopContext other = (TRRunLoopContext) o;
        return other.runLoop().equals(runLoop()) && other.source().equals(source());
    }
}
