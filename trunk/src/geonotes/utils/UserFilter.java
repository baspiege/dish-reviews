package geonotes.utils;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
* Grab the user from dishRevUser cookie and sets as user principal.
*/
public class UserFilter implements Filter {

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // Cast to http servlet
        HttpServletRequest httpServletRequest=(HttpServletRequest)servletRequest;

        String user=null;
        if (httpServletRequest.getCookies()!=null) {
          for (Cookie cookie: httpServletRequest.getCookies()) {
              if (cookie.getName().equals("dishRevUser")) {
                  user=cookie.getValue();
                  break;
              }
          }
        }
        
        filterChain.doFilter(new UserRoleRequestWrapper(user, httpServletRequest), servletResponse);
    }
    
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing for now    
    }
    
    public void destroy() {
        // Nothing for now
    }
}