package org.bbz.bigdata.webplatform.service.hdfs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liulaoye on 16-12-12.
 * 读取hdfs文件内容的方式
 */
public enum FileReadType{

    TEXT(1),   //文本类型
    BINARY(2);  //二进制


            private final int number;

            private static final Map<Integer, FileReadType> numToEnum = new HashMap<Integer, FileReadType>();
            static{
                for( FileReadType t : values() ){

                    FileReadType s = numToEnum.put( t.number, t );
                    if( s != null ){
                        throw new RuntimeException( t.number + "重复了" );
                    }
                }
            }

            FileReadType( int number ){
                this.number = number;
            }

            public int toNum() {
                return number;
            }
            public static FileReadType fromNum( int n ){
                return numToEnum.get( n );
            }
}
