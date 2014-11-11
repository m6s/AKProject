package de.msmobileapps.threadingreference;

import info.mschmitt.ak.Injector;
import info.mschmitt.ak.coregraphics.GraphicsRect;
import info.mschmitt.ak.ui.DefaultUIViewController;
import info.mschmitt.ak.ui.UILabel;
import info.mschmitt.ak.ui.UILabelFactory;
import info.mschmitt.ak.ui.UIScreen;

/**
 * @author Matthias Schmitt
 */
public class ThreadingViewController extends DefaultUIViewController {
    UILabelFactory labelFactory = Injector.getInstance().injectUILabelFactory();
    UIScreen mainScreen = Injector.getInstance().injectMainScreen();
    UILabel textView;

    public ThreadingViewController() {
        super(Injector.getInstance().injectUIViewControllerFactory(), Injector.getInstance().injectUIViewFactory(), Injector.getInstance().injectMethodResolver());
    }

    @Override
    public void loadView() {
        GraphicsRect bounds = mainScreen.getApplicationFrame();
        super.loadView();
        textView = labelFactory.create(bounds);
        this.setView(textView);
        textView.setText("Foo");
    }
}

