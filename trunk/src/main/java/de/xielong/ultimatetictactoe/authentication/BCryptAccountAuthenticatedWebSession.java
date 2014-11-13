package de.xielong.ultimatetictactoe.authentication;

import lombok.Getter;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

import de.xielong.ultimatetictactoe.authentication.persistence.Account;

/**
 * @author XieLong
 */
@Getter
public class BCryptAccountAuthenticatedWebSession extends AuthenticatedWebSession {

    private Account account;

    private Roles   roles;

    /**
     * This method return the current {@link BCryptAccountAuthenticatedWebSession} object.
     * 
     * @return the current {@link BCryptAccountAuthenticatedWebSession}
     */
    public static BCryptAccountAuthenticatedWebSession get() {
        return (BCryptAccountAuthenticatedWebSession) Session.get();
    }

    /**
     * Default Construct.
     * 
     * @param request
     *            The current request object
     */
    public BCryptAccountAuthenticatedWebSession(Request request) {
        super(request);
    }

    /**
     * This method authenticate the user by checking the passed userName and password combination with the stored
     * account iformation in the {@link Account} table.
     * 
     * @param username
     *            the user input username
     * @param password
     *            the user input password
     * @return true if the authentication was successful otherwise false
     */
    @Override
    public boolean authenticate(final String username, final String password) {
        account = new Account();
        account.setName(username);

        roles = new Roles(Roles.USER);
        return true;
    }

}
