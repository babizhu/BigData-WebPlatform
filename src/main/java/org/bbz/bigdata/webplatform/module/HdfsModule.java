package org.bbz.bigdata.webplatform.module;

import org.apache.commons.codec.binary.Base64;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.bbz.bigdata.webplatform.bean.hdfs.HdfsDirInfo;
import org.bbz.bigdata.webplatform.consts.ErrorCode;
import org.bbz.bigdata.webplatform.service.hdfs.HdfsService;
import org.bbz.bigdata.webplatform.service.hdfs.FileReadType;
import org.bbz.bigdata.webplatform.service.hdfs.OperationType;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liulaoye on 16-12-7.
 * HdfsModule
 */

@IocBean
@At("/api/hdfs")
public class HdfsModule extends BaseModule{
    private static final int BLOCK_SIZE = 4096;
    @Inject
    private HdfsService hdfsService;


    @At
    public NutMap operation( @Param("path") String path,
                             @Param("op") int op,
                             @Param("args") String args,
                             HttpServletRequest req,
                             HttpServletResponse response ){

        ErrorCode ret;
        final OperationType operationType = OperationType.fromNum( op );
        switch( operationType ) {
            case MKDIR:

                ret = hdfsService.mkdir( args );
                break;
            default:
                return this.buildErrorResponse( response, ErrorCode.OPERATION_NOT_FOUND, op + "" );
        }

        if( ret != ErrorCode.SUCCESS){
            this.buildErrorResponse( response, ret );
        }
        return this.buildSuccessResponse();
    }

    /**
     * 获取指定路径下的文件信息，如果指定路径为文件夹，则返回文件夹的内容，如果指定路径为文件，则返回文件内容
     *
     * @param   path     指定路径
     * @param   response response
     * @return
     *          json
     */
    @At
    @GET
    public Object getFilePathInfo( @Param("path") String path, @Param("readType") int readType,
                                   @Param("currentBlock") int readBlock,
                                   HttpServletResponse response ){
        FileSystem fs = null;
        try {
            fs = FileSystem.get( new Configuration() );
            Boolean isFile = fs.isFile( new Path( path ) );
            if( isFile ) {
                return this.getFileContent( path, fs, readType, readBlock );
            } else {
                return getDirList( path, fs );
            }

        } catch( org.apache.hadoop.security.AccessControlException e ) {
            return buildErrorResponse( response, ErrorCode.HDFS_ACCESS_ERROR );
        } catch( Exception e ) {
            return buildErrorResponse( response, ErrorCode.UNKNOW_ERROR, e.getLocalizedMessage() );

        } finally {
            if( fs != null ) {
                try {
                    fs.close();
                } catch( IOException e ) {
                    return buildErrorResponse( response, ErrorCode.UNKNOW_ERROR, e.getLocalizedMessage() );
                }
            }
        }
    }

    /**
     * 获取文件内容
     *
     * @param filePath 目录的路径
     * @param fs       fs
     * @param readType readType
     * @return 要查看的文件内容
     */
    private Object getFileContent( String filePath, FileSystem fs, int readType, long readBlock ){
        final byte[] fileContent = hdfsService.getFileContent( fs, filePath, readBlock, BLOCK_SIZE );
        return buildFileContent( readType, fileContent );
    }

    private Object getDirList( String dirPath, FileSystem fs ) throws IOException{
        return buildDirList( hdfsService.getHdfsDirList( dirPath, fs ) );
    }

    private String buildFileContent( int readType, byte[] contents ){
        if( FileReadType.fromNum( readType ) == FileReadType.BINARY ) {
            return buildFileContentAsBinary( contents );
        } else {
            return buildFileContentAsText( contents );
        }
    }

    private String buildFileContentAsText( byte[] fileContent ){
        String json = Base64.encodeBase64String( fileContent );
        return json.replace( "\n", "" ).replace( "\r", "" ).replace( "\t", "" );
    }

    private String buildFileContentAsBinary( byte[] fileContent ){
        StringBuilder stringBuilder = new StringBuilder( "" );
        if( fileContent == null || fileContent.length <= 0 ) {
            return "";
        }
        for( byte b : fileContent ) {
            int v = b & 0xFF;
            String hv = Integer.toHexString( v );
            if( hv.length() < 2 ) {
                stringBuilder.append( 0 );
            }
            stringBuilder.append( hv );
        }
        return stringBuilder.toString();
    }

    private List<HdfsDirInfo> buildDirList( FileStatus[] status ){
        List<HdfsDirInfo> list = new ArrayList<>();
        for( FileStatus fileStatus : status ) {
            HdfsDirInfo info = new HdfsDirInfo();
            String name = fileStatus.getPath().getName();
            info.setAccessTime( fileStatus.getAccessTime() );
            info.setBlockSize( fileStatus.getBlockSize() );
            info.setFile( fileStatus.isFile() );
            info.setFileId( name );
            info.setGroup( fileStatus.getGroup() );
//            info.setFileId( fileStatus.get );
            info.setLength( fileStatus.getLen() );
            info.setModificationTime( fileStatus.getModificationTime() );
            info.setOwner( fileStatus.getOwner() );
            info.setPathSuffix( name );
            info.setPermission( fileStatus.getPermission().toString() );
            info.setReplication( fileStatus.getReplication() );

            list.add( info );
        }
        return list;
    }

//
//
//
//    /**
//     * 获取输入路径所在的文件夹的信息
//     *
//     * @param path     目录的路径
//     * @param req      req
//     * @param response response
//     * @return 文件夹列表的ｊｓｏｎ
//     */
//    @At
//    @GET
//    public Object getDirList( @Param("path") String path,
//                              HttpServletRequest req,
//                              HttpServletResponse response ) throws IOException{
//        FileSystem fs = null;
//        try {
//            fs = FileSystem.get( new Configuration() );
//            Boolean isFile = fs.isFile( new Path( path ) );
//            if( isFile ) {
//                return buildErrorResponse( response, ErrorCode.ILLEGAL_ARGUMENT );
//            }
//
//            return buildDirList( hdfsService.getHdfsDirList( fs, path ) );
//        } catch( org.apache.hadoop.security.AccessControlException e ) {
//            return buildErrorResponse( response, ErrorCode.HDFS_ACCESS_ERROR );
//        } catch( Exception e ) {
//            return buildErrorResponse( response, ErrorCode.UNKNOW_ERROR, e.getLocalizedMessage() );
//
//        } finally {
//            if( fs != null ) {
//                fs.close();
//            }
//        }
//    }
//
//    /**
//     * 获取文件内容
//     *
//     * @param filePath 目录的路径
//     * @param req      req
//     * @param response response
//     * @return 要查看的文件内容
//     */
//    @At
//    @GET
//    public Object getFileContent( @Param("path") String filePath,
//                                  HttpServletRequest req,
//                                  HttpServletResponse response ) throws IOException{
//        FileSystem fs = null;
//        try {
//            fs = FileSystem.get( new Configuration() );
//            Boolean isFile = fs.isFile( new Path( filePath ) );
//            if( !isFile ) {
//                return buildErrorResponse( response, ErrorCode.ILLEGAL_ARGUMENT );
//            }
//
//            return hdfsService.getFileContent( fs, filePath, FileReadType.BINARY, FILE_BLOCK );
//
//        } catch( org.apache.hadoop.security.AccessControlException e ) {
//            return buildErrorResponse( response, ErrorCode.HDFS_ACCESS_ERROR );
//        } catch( Exception e ) {
//            return buildErrorResponse( response, ErrorCode.UNKNOW_ERROR, e.getLocalizedMessage() );
//
//        } finally {
//            if( fs != null ) {
//                fs.close();
//            }
//        }
//    }
}
