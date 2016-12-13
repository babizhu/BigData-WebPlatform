package org.bbz.bigdata.webplatform.bean.hdfs;

import lombok.Data;

/**
 * Created by liulaoye on 16-12-7.
 */

@Data
public class HdfsDirInfo{
    private long accessTime;
    private long blockSize;
    private String fileId;//是不是应该转换为ｎａｍｅ
    private String group;
    private long length;
    private long modificationTime;
    private String owner;
    private String pathSuffix;
    private String permission;
    private short replication;
    private boolean isFile;

}
