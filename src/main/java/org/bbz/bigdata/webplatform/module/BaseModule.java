package org.bbz.bigdata.webplatform.module;

import org.bbz.bigdata.webplatform.consts.ErrorCode;
import org.nutz.lang.util.NutMap;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by liulaoye on 16-10-31.
 * 所有Module的父类
 */
public class BaseModule{

    /**
     * 根据错误id，构建相应的错误提示信息
     *
     * @param response
     * @param errId
     * @return
     */
    protected NutMap buildErrorResponse( HttpServletResponse response, ErrorCode errId ){

        return this.buildErrorResponse( response, errId, "" );
    }

    protected NutMap buildErrorResponse( HttpServletResponse response, ErrorCode errId, String args ){
        response.setStatus( 500 );
        NutMap re = new NutMap();
        re.addv( "errId", errId.toNum() ).addv( "args", args );
//        return "{\"errId\":" + errId.toNum() + ",\"args\":\"" + args + "\"}";
        return re;
    }
}
