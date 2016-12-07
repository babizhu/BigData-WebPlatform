package org.bbz.bigdata.webplatform.mvc;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.nutz.mvc.SessionProvider;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liulaoye on 16-10-28.
 *
 */
public class CustomShiroSessionProvider implements SessionProvider{

    public HttpServletRequest filter( HttpServletRequest req, HttpServletResponse resp, ServletContext servletContext) {
//        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
//            resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
//            resp.addHeader("Access-Control-Allow-Credentials", "true");
//            resp.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Key");
//        }

        final Subject subject = SecurityUtils.getSubject();
        if (  !subject.isAuthenticated() && subject.isRemembered() ) {//自动登录
            Object principal = subject.getPrincipal();
            if (null != principal) {
                String  userName = String.valueOf(principal);
//                String password = user.getPassword();
//                UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), password);
//                token.setRememberMe(true);
//                subject.login(token);//登录
            }
        }
        resp.addHeader("Access-Control-Allow-Origin", req.getHeader( "Origin" ));
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        resp.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Key");

        if (req instanceof ShiroHttpServletRequest ){
            return req;
        }
        final ShiroHttpServletRequest shiroHttpServletRequest = new ShiroHttpServletRequest( req, servletContext, true );
        return shiroHttpServletRequest;
    }

    public void notifyStop() {
    }

}