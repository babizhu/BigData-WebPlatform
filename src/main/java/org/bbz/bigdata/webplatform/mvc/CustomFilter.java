package org.bbz.bigdata.webplatform.mvc;

import org.nutz.mvc.NutFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CustomFilter extends NutFilter {

    protected Set<String> prefixs = new HashSet<>();


    public void init(FilterConfig conf) throws ServletException {
        super.init(conf);
        prefixs.add(conf.getServletContext().getContextPath() + "/druid/");
        prefixs.add(conf.getServletContext().getContextPath() + "/rs/");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {


        if (req instanceof HttpServletRequest) {
            String uri = ((HttpServletRequest) req).getRequestURI();
            for (String prefix : prefixs) {
                if (uri.startsWith(prefix)) {
                    chain.doFilter(req, resp);
                    return;
                }
                if( !uri.startsWith( "/api" ) && uri.indexOf( '.' ) == -1 ){//专门处理客户端(react js  )的请求
                    req.getRequestDispatcher( "/" ).forward( req,resp );
                    return;
                }
            }
        }
//        resp.setCharacterEncoding( "utf-8" );

        super.doFilter(req, resp, chain);
    }
}