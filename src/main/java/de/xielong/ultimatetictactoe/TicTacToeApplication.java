package de.xielong.ultimatetictactoe;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.pages.SignInPage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.settings.IMarkupSettings;

import de.xielong.ultimatetictactoe.authentication.BCryptAccountAuthenticatedWebSession;
import de.xielong.ultimatetictactoe.pages.GamePage;
import de.xielong.wicket.less.LessUtil;

public class TicTacToeApplication extends AuthenticatedWebApplication {

    private static final Class<? extends WebPage> HOMEPAGE = GamePage.class;

    public static TicTacToeApplication get() {
        Application application = Application.get();

        if (application instanceof TicTacToeApplication == false) {
            throw new WicketRuntimeException("The application attached to the current thread is not a "
                    + TicTacToeApplication.class.getSimpleName());
        }

        return (TicTacToeApplication) application;
    }

    @Override
    protected void init() {
        super.init();

        /*
         * Mount pages.
         */

        IMarkupSettings markupSettings = getMarkupSettings();
        /*
         * If no <?xml version="1.0" encoding="UTF-8"?> is provided, the default encoding UTF-8 should be used.
         */
        markupSettings.setDefaultMarkupEncoding("UTF-8");
        /*
         * Wicket tags should always be stripped, because they corrupt CSS rules
         */
        markupSettings.setStripWicketTags(true);
        /* Different configurations for configuration types */
        switch (getConfigurationType()) {
            case DEVELOPMENT:
                markupSettings.setStripComments(false);

                // Init Debug Logging
                Logger root = Logger.getRootLogger();
                root.removeAllAppenders();
                root.addAppender(new ConsoleAppender(new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN)));
                root.setLevel(Level.INFO);
                break;
            case DEPLOYMENT:
                markupSettings.setStripComments(true);
                break;
            default:
                break;
        }

        LessUtil.initApplication(this);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HOMEPAGE;
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return BCryptAccountAuthenticatedWebSession.class;
    }

    @Override
    public Class<? extends WebPage> getSignInPageClass() {
        return SignInPage.class;
    }

}
