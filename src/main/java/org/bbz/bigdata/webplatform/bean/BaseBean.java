package org.bbz.bigdata.webplatform.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;

import java.util.Date;

/**
 * Created by liulaoye on 16-10-25.
 * BaseBean
 */
public abstract class BaseBean{

    @Column("ct")
    protected Date createTime;
    @Column("ut")
    protected Date updateTime;

    public String toString() {
        return String.format("/*%s*/%s", super.toString(), Json.toJson(this, JsonFormat.compact()));
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}