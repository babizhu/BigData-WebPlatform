package org.bbz.bigdata.webplatform.misc.tools;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by liulaoye on 16-12-7.
 * 读取后缀名为Properties的配置文件
 */
public class ReadProperties{
    public Properties init( String cfgPath ){
        Properties prop = null;
        try {
            prop = new Properties();
            InputStream in = new BufferedInputStream( new FileInputStream( cfgPath ) );
            prop.load( in );

        } catch( IOException e ) {
            e.printStackTrace();
        }
        return prop;
    }
}
