package de.msmobileapps.threadingreference.customsources;

import android.util.Log;
import info.mschmitt.ak.Injector;
import info.mschmitt.ak.corefoundation.CoreRunLoop;
import info.mschmitt.ak.corefoundation.CoreRunLoopSource;
import info.mschmitt.ak.corefoundation.CoreRunLoopSourceCustom;
import info.mschmitt.ak.runtime.MethodPerformer;
import info.mschmitt.ak.ui.UIApplication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TRRunLoopSource {
    UIApplication sharedApplication = Injector.getInstance().injectSharedApplication();
    MethodPerformer methodPerformer = Injector.getInstance().injectMethodPerformer();
    CoreRunLoopSource runLoopSource;
    List commands;
//    private CFRunLoopScheduleCallBack RunLoopSourceScheduleRoutine = new CFRunLoopScheduleCallBack() {
//        @Override
//        public void execute(Object info, CFRunLoop rl, String mode) {
//            TRRunLoopSource obj = (TRRunLoopSource) info;
//            CustomSourcesAppDelegate del = CustomSourcesAppDelegate.sharedAppDelegate();
//            TRRunLoopContext theContext = new TRRunLoopContext().initWithSourceAndLoop(obj, rl);
//            del.performSelectorOnMainThreadWithObjectWaitUntilDone(CustomSourcesAppDelegateSelectors.SEL_REGISTER_SOURCE, theContext, false);
//        }
//    };

//    private CFRunLoopCancelCallBack RunLoopSourceCancelRoutine = new CFRunLoopCancelCallBack() {
//        @Override
//        public void execute(Object info, CFRunLoop rl, String mode) {
//            TRRunLoopSource obj = (TRRunLoopSource) info;
//            CustomSourcesAppDelegate del = CustomSourcesAppDelegate.sharedAppDelegate();
//            TRRunLoopContext theContext = new TRRunLoopContext().initWithSourceAndLoop(obj, rl);
//            del.performSelectorOnMainThreadWithObjectWaitUntilDone(CustomSourcesAppDelegateSelectors.SEL_REMOVE_SOURCE, theContext, true);
////            del.performSelectorOnMainThreadWithObjectWaitUntilDone(
////                    new SelectorOneArg<AppDelegate, RunLoopContext>() {
////                        @Override
////                        public void invoke(AppDelegate aTarget, RunLoopContext anArgument) {
////                            aTarget.removeSource(anArgument);
////                        }
////                    },
////                    theContext,
////                    true);
//        }
//    };

//    private CFRunLoopPerformCallBack RunLoopSourcePerformRoutine = new CFRunLoopPerformCallBack() {
//        @Override
//        public void execute(Object info) {
//            TRRunLoopSource obj = (TRRunLoopSource) info;
//            obj.sourceFired();
//        }
//    };

    public TRRunLoopSource init() {
//        CFRunLoopSourceContext context = new CFRunLoopSourceContext(0, this, null, null,
//                RunLoopSourceScheduleRoutine,
//                RunLoopSourceCancelRoutine,
//                RunLoopSourcePerformRoutine);
//        runLoopSource = new CFRunLoopSource(0, context);
        runLoopSource = new CoreRunLoopSourceCustom(0) {
            @Override
            public void onCancel(CoreRunLoop rl, String mode) {
                CustomSourcesAppDelegate del = (CustomSourcesAppDelegate) sharedApplication.getDelegate();
                TRRunLoopContext theContext = new TRRunLoopContext().initWithSourceAndLoop(TRRunLoopSource.this, rl);
                methodPerformer.performOnMainThread(del, CustomSourcesAppDelegateFactory.SEL_REMOVE_SOURCE,
                        theContext, true);
            }

            @Override
            public void onPerform() {
                sourceFired();
            }

            @Override
            public void onSchedule(CoreRunLoop rl, String mode) {
                CustomSourcesAppDelegate del = (CustomSourcesAppDelegate) sharedApplication.getDelegate();
                TRRunLoopContext theContext = new TRRunLoopContext().initWithSourceAndLoop(TRRunLoopSource.this, rl);
                methodPerformer.performOnMainThread(del, CustomSourcesAppDelegateFactory.SEL_REGISTER_SOURCE,
                        theContext, false);
            }
        };
        commands = new ArrayList();
        return this;
    }

    public void addToCurrentRunLoop() {
        CoreRunLoop runLoop = CoreRunLoop.getCurrent();
        runLoop.addSource(runLoopSource, CoreRunLoop.MODE_DEFAULT);
    }

    public void invalidate() {
        runLoopSource.invalidate();
    }


    ///Handler method///

    public void sourceFired() {
        synchronized (commands) {
            Iterator it = commands.iterator();
            while (it.hasNext()) {
                CommandDataPair pair = (CommandDataPair) it.next();
                Log.d("FOO", (String) pair.data);
                it.remove();
            }
        }
    }


    ///Client interface for registering commands to process///

    public void addCommandWithData(int command, Object data) {
        synchronized (commands) {
            commands.add(new CommandDataPair(command, data));
        }
    }

    public void fireAllCommandsOnRunLoop(CoreRunLoop runloop) {
        runLoopSource.signal();
        runloop.wakeUp();
    }

    private class CommandDataPair {
        public final int command;
        public final Object data;

        public CommandDataPair(int command, Object data) {
            this.command = command;
            this.data = data;
        }
    }
}
