package de.xielong.ultimatetictactoe.authentication;


public abstract class AuthUtil {

    private static final int BCRYPT_LOG_ROUNDS = 12;

    // public static PrincipalAuthenticatedWebSession getSession() {
    // return PrincipalAuthenticatedWebSession.get();
    // }

    public static BCryptAccountAuthenticatedWebSession getSession() {
        return BCryptAccountAuthenticatedWebSession.get();
    }

}
