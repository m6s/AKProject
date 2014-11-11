package de.msmobileapps.threadingreference.portsources;

import android.util.Log;
import info.mschmitt.ak.Injector;
import info.mschmitt.ak.foundation.*;
import info.mschmitt.ak.runtime.Factory;

public class MyWorkerClass implements PortDelegate {
    RunLoopFactory runLoopFactory = Injector.getInstance().injectRunLoopFactory();
    MachPortFactory machPortFactory = Injector.getInstance().injectMachPortFactory();
    PortMessageFactory portMessageFactory = Injector.getInstance().injectPortMessageFactory();
    FoundationDateFactory foundationDateFactory = Injector.getInstance().injectFoundationDateFactory();
    private Port mOutPort;

    protected boolean shouldExit() {
        return false;
    }

    // Worker thread check-in method
    public void sendCheckinMessage(Port outPort) {
        // Retain and save the remote port for future use.
        setRemotePort(outPort);
        // Create and configure the worker thread port.
        Port myPort = machPortFactory.createPort();
        myPort.setDelegate(this);
        runLoopFactory.currentRunLoop().addPort(myPort, RunLoop.MODE_DEFAULT);
        // Create the check-in message.
        PortMessage messageObj = portMessageFactory.create(outPort, myPort, null);
        if (messageObj != null) {
            // Finish configuring the message and send it immediately.
            messageObj.setMsgid(PortSourcesAppDelegate.K_CHECKIN_MESSAGE);
            messageObj.sendBefore(foundationDateFactory.create());
        }
    }

    private void setRemotePort(Port outPort) {
        mOutPort = outPort;
    }

    @Override
    public void handlePortMessage(PortMessage portMessage) {
        Log.d("FOO", "Received reply message from main thread");
    }

    @Override
    public Factory getFactory() {
        return MyWorkerClassFactory.INSTANCE;
    }
}
