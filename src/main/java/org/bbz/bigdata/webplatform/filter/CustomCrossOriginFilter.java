package org.bbz.bigdata.webplatform.filter;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.View;
import org.nutz.mvc.filter.CrossOriginFilter;

/**
 * Created by liu_k on 2016/4/15.
 * CustomCrossOriginFilter
 * 暂时未使用
 */
public class CustomCrossOriginFilter extends CrossOriginFilter{

    public CustomCrossOriginFilter( String origin, String methods, String headers, String credentials ){
        this.origin = origin;
        this.methods = methods;
        this.headers = headers;
        this.credentials = credentials;
    }

    public View match( ActionContext ac ){

            return null;

    }
}
