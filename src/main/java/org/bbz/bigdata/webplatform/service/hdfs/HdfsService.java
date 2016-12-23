package org.bbz.bigdata.webplatform.service.hdfs;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.bbz.bigdata.webplatform.consts.ErrorCode;
import org.nutz.ioc.loader.annotation.IocBean;

import java.io.IOException;

/**
 * Created by liulaoye on 16-12-7.
 * HdfsService
 */

@IocBean
public class HdfsService{


    /**
     * 获取目录列表
     *
     * @param dirPath 目录的路径
     * @return ｌｉｓｔ
     */
    public FileStatus[] getHdfsDirList( String dirPath, FileSystem fs ) throws IOException{

        return fs.listStatus( new Path( dirPath ) );
    }

    /**
     * 获取文件内容
     *
     * @param filePath  文件路径
     * @param readBlock 要读取的文件快下标，从０开始计数
     * @return
     */
    public byte[] getFileContent( FileSystem fs, String filePath, long readBlock, int blockSize ){
        FSDataInputStream in = null;
        byte[] contents = null;
        try {
            FileStatus status = fs.getFileStatus( new Path( filePath ) );
            in = fs.open( new Path( filePath ) );

            long fileLen = status.getLen();
            long totalBlock = fileLen / blockSize;
            readBlock = Math.min( readBlock, totalBlock );//不能获取超过文件总快数的内容
            int realLen;
            boolean isLastBlock = totalBlock <= readBlock;

            if( !isLastBlock ) {
                realLen = blockSize;
            } else {
                realLen = (int) (fileLen - readBlock * blockSize);//可以放心的转，不会超过BLOCK_SIZE
            }

            contents = new byte[realLen];//一次最多仅允许读BLOCK_SIZE字节

            long fileOffset = readBlock * blockSize;

            in.readFully( fileOffset, contents );

        } catch( IOException e ) {
            e.printStackTrace();
        }
        return contents;
    }


    public ErrorCode mkdir( FileSystem fs, String path ) throws IOException{

        Path dst = new Path( path );

        if( fs.exists( dst ) ) {
//            return buildErrorResponse( response, 202, dst.toString() );
            return ErrorCode.HDFS_DIR_HAS_EXIST;
        }
        fs.mkdirs( dst );
        return ErrorCode.SUCCESS;
    }

    public ErrorCode rm( FileSystem fs,String path,boolean recursiveDelete ){
        try {
            fs.delete( new Path( path ), recursiveDelete );//false 为是否递归删除
        }catch( Exception e ){
            return ErrorCode.HDFS_NOT_RECURSIVE;
        }
        return ErrorCode.SUCCESS;
    }

    public ErrorCode rename( FileSystem fs, String srcPath, String destPath ) throws IOException{
        fs.rename( new Path( srcPath ), new Path( destPath ) );
        return ErrorCode.SUCCESS;

    }



}
