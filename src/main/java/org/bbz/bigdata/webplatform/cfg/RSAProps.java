package org.bbz.bigdata.webplatform.cfg;

import java.io.*;
import java.util.Properties;

/**
 * Created by liulaoye on 16-11-14.
 * 读取配置表中的公私钥，
 */
public enum RSAProps{
    INSTANCE;

    private final String privateKey;
    private final String publicKey;

    RSAProps(){

        String filePath = this.getClass().getClassLoader().getResource( "rsa_key.properties" ).getPath();

        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = new BufferedInputStream( new FileInputStream( filePath ) );
            prop.load( in );     ///加载属性列表
            in.close();

        } catch( FileNotFoundException e ) {
            e.printStackTrace();
        } catch( IOException e ) {
            e.printStackTrace();
        }

        privateKey = prop.getProperty( "private_key" );
        publicKey = prop.getProperty( "public_key" );
    }

    public String getPrivateKey(){
        return privateKey;
    }

    public String getPublicKey(){
        return publicKey;
    }
}
