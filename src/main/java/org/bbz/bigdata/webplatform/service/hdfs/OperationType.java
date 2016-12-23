package org.bbz.bigdata.webplatform.service.hdfs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liulaoye on 16-12-13.
 * ｈｄｆｓ操作类型
 */
public enum OperationType{
    MKDIR( 1 ),
    RM( 2 ),
    RENAME( 3 ),;

    private final int number;

    private static final Map<Integer, OperationType> numToEnum = new HashMap<Integer, OperationType>();

    static{
        for( OperationType t : numToEnum.values() ) {

            OperationType s = numToEnum.put( t.number, t );
            if( s != null ) {
                throw new RuntimeException( t.number + "重复了" );
            }
        }
    }

    OperationType( int number ){
        this.number = number;
    }

    public int toNum(){
        return number;
    }

    public static OperationType fromNum( int n ){
        return numToEnum.get( n );
    }
}
