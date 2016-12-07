package org.bbz.bigdata.webplatform.cfg;

import org.bbz.bigdata.webplatform.misc.tools.ReadProperties;

import java.util.Properties;

/**
 * Created by liulaoye on 16-12-7.
 * oozie配置类
 */
public enum OozieCfg{
    INSTANCE;
    /**
     * 监听ip
     */
    public final String URL;

    OozieCfg(){
        final Properties prop = new ReadProperties().init( "xx" );
        URL = prop.getProperty( "url" );

    }

}
