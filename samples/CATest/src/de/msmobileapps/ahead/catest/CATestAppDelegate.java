package de.msmobileapps.ahead.catest;

import info.mschmitt.ak.Injector;
import info.mschmitt.ak.coreanimation.AnimationLayer;
import info.mschmitt.ak.coreanimation.AnimationLayerDelegate;
import info.mschmitt.ak.coreanimation.AnimationLayerFactory;
import info.mschmitt.ak.coreanimation.staging.AnimationAction;
import info.mschmitt.ak.coregraphics.GraphicsContext;
import info.mschmitt.ak.coregraphics.GraphicsPath;
import info.mschmitt.ak.coregraphics.GraphicsPoint;
import info.mschmitt.ak.coregraphics.GraphicsRect;
import info.mschmitt.ak.runtime.Factory;
import info.mschmitt.ak.ui.*;
import info.mschmitt.jdispatch.DispatchBlock;
import info.mschmitt.jdispatch.DispatchQueue;
import info.mschmitt.jdispatch.DispatchTime;

import java.util.concurrent.TimeUnit;

/**
 * @author Matthias Schmitt
 */
public class CATestAppDelegate implements UIApplicationDelegate, UIApplicationDelegate.Storyboarding, AnimationLayerDelegate {
    private UIWindowFactory mWindowFactory = Injector.getInstance().injectUIWindowFactory();
    private UIScreen mMainScreen = Injector.getInstance().injectMainScreen();
    private UIColorFactory mColorFactory = Injector.getInstance().injectUIColorFactory();
    private AnimationLayerFactory mLayerFactory = Injector.getInstance().injectAnimationLayerFactory();
    private UIBezierPathFactory mBezierPathFactory = Injector.getInstance().injectUIBezierPathFactory();

    @Override
    public boolean onDidFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        UIWindow window = mWindowFactory.create(mMainScreen.getBounds());
        window.getLayer().setBackgroundColor(mColorFactory.lightGray().toCoreType());
        window.getLayer().setBorderColor(mColorFactory.darkGray().toCoreType());
        window.getLayer().setBorderWidth(5);
        window.getLayer().setName("window");

        final AnimationLayer blueLayer = mLayerFactory.create();
        blueLayer.setFrame(new GraphicsRect(50, 50, 100, 200));
        blueLayer.setBackgroundColor(mColorFactory.blue().toCoreType());
        blueLayer.setBorderColor(mColorFactory.purple().toCoreType());
        blueLayer.setBorderWidth(5);
        blueLayer.setDelegate(this);
        blueLayer.setNeedsDisplay();
        blueLayer.setName("blueLayer");
        window.getLayer().addSublayer(blueLayer);

        AnimationLayer yellowLayer = mLayerFactory.create();
        yellowLayer.setBounds(new GraphicsRect(0, 0, 50, 100));
        yellowLayer.setBackgroundColor(mColorFactory.yellow().toCoreType());
        yellowLayer.setBorderColor(mColorFactory.green().toCoreType());
        yellowLayer.setBorderWidth(5);
        yellowLayer.setName("yellowLayer");
        blueLayer.addSublayer(yellowLayer);

        window.makeKeyAndVisible();

        DispatchTime fiveSecondsFromNow = DispatchTime.NOW.plus(5, TimeUnit.SECONDS);
        DispatchQueue.getMainQueue().dispatchAfter(fiveSecondsFromNow, new DispatchBlock() {
            @Override
            public void execute(int index, Object context) {
                blueLayer.setPosition(new GraphicsPoint(200, 250));
            }
        });
        return true;
    }

    @Override
    public UIWindow getWindow() {
        return null;
    }

    @Override
    public void setWindow(UIWindow value) {
    }

    @Override
    public void displayLayer(AnimationLayer layer) {
        throw new UnsupportedOperationException();
//        GraphicsSize size = layer.getBounds().size;
//        int width = (int) (size.width * layer.getContentsScale());
//        int height = (int) (size.height * layer.getContentsScale());
//        GraphicsBitmapContext ctx = new GraphicsBitmapContext(null, width, height, 8, width * 4, GraphicsColorSpace.createDeviceRGB(),
//                null); //GraphicsBitmapInfo.kCGImageAlphaPremultipliedLast
//        //        CGContext.CGContextScaleCTM(ctx, contentsScale(), contentsScale()); //TODO Check direction
//        MutableGraphicsPath path = new MutableGraphicsPath();
//        path.addEllipseInRect(null, new GraphicsRect(10, 10, 80, 150));
//        ctx.beginPath();
//        ctx.addPath(path);
//        ctx.strokePath();
//        layer.setContents(ctx.toImage());
    }

    @Override
    public void drawLayer(AnimationLayer layer, GraphicsContext ctx) {
        UIBezierPath bezierPath = mBezierPathFactory.oval(new GraphicsRect(10, 10, 80, 150));
        GraphicsPath path = bezierPath.getGraphicsPath();
        ctx.beginPath();
        ctx.addPath(path);
        ctx.strokePath();
    }

    @Override
    public AnimationAction actionForLayer(AnimationLayer layer, String key) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean onWillFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onDidBecomeActive(UIApplication application) {
    }

    @Override
    public void onWillResignActive(UIApplication application) {
    }

    @Override
    public void onDidEnterBackground(UIApplication application) {
    }

    @Override
    public void onWillEnterForeground(UIApplication application) {
    }

    @Override
    public void onWillTerminate(UIApplication application) {
    }

    @Override
    public Factory getFactory() {
        return null;
    }
}
