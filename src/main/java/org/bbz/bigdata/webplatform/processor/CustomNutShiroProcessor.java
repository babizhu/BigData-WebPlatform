package org.bbz.bigdata.webplatform.processor;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.bbz.bigdata.webplatform.consts.ErrorCode;
import org.nutz.integration.shiro.NutShiro;
import org.nutz.integration.shiro.NutShiroProcessor;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.view.ServerRedirectView;

/**
 * Created by liulaoye on 16-11-2.
 *
 */
public class CustomNutShiroProcessor extends NutShiroProcessor{
    @Override
    protected void whenUnauthenticated( ActionContext ac, UnauthenticatedException e ) throws Exception{
//        super.whenUnauthenticated( ac, e );
        if( NutShiro.isAjax( ac.getRequest() ) ) {
//            ac.getResponse().addHeader( "loginStatus", "accessDenied" );
            ac.getResponse().setStatus( 500 );
            NutShiro.rendAjaxResp( ac.getRequest(), ac.getResponse(), new NutMap().addv( "errId", ErrorCode.USER_NOT_LOGIN.toNum() ) );
        } else {
            new ServerRedirectView( loginUri() ).render( ac.getRequest(), ac.getResponse(), null );
        }
    }

    protected void whenUnauthorized(ActionContext ac, UnauthorizedException e) throws Exception {
        if (NutShiro.isAjax(ac.getRequest())) {
            ac.getResponse().setStatus( 500 );
            NutShiro.rendAjaxResp( ac.getRequest(), ac.getResponse(), new NutMap().addv( "errId", ErrorCode.USER_PERMISSION_DENIED.toNum() ) );
        } else {
            new ServerRedirectView(noAuthUri()).render(ac.getRequest(), ac.getResponse(), null);
        }
    }
}
