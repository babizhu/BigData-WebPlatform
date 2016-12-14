package org.bbz.bigdata.webplatform.consts;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liulaoye on 16-12-14.
 * 常见的增删改操作枚举
 */
public enum OperationType{
    ADD_OR_UPDATE( 1 ),
//    UPDATE( 1 ),
    DELETE( 2 ),;

    private final int number;

    private static final Map<Integer, OperationType> numToEnum = new HashMap<Integer, OperationType>();

    static{
        for( OperationType t : values() ) {

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
