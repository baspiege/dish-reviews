package geonotes.utils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Set user principal.
 */
public class UserRoleRequestWrapper extends HttpServletRequestWrapper {

    String user=null;
    HttpServletRequest realRequest;

    public UserRoleRequestWrapper(String user, HttpServletRequest request) {
        super(request);
        this.user = user;
        this.realRequest = request;
    }

    @Override
    public Principal getUserPrincipal() {

        // anonymous implementation to return user
        return new Principal() {
            @Override
            public String getName() {
                return user;
            }
        };
    }
}
