package org.bbz.bigdata.webplatform.bean;

import org.nutz.dao.entity.annotation.*;

/**
 * Created by liulaoye on 16-11-1.
 * Enterprise
 */
@Table("enterprise")
@View("enterprise_view")
public class Enterprise extends BaseBean{
    @Id
    protected int id;
    @Name
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    protected String name;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 150)
    protected String address;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 250)
    protected String mapAddress;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    protected String contact;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 1000)
    protected String description;
    @Column
    protected String mapX;
    @Column
    protected String mapY;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 150)
    protected String linkName;
//    @Column
//    protected long linkId;

    public String getLinkName(){
        return linkName;
    }

    public void setLinkName( String linkName ){
        this.linkName = linkName;
    }



    @Column("yearnum")	// 其实可以不用声明数据库字段名 "taskcount"，因为多数数据库忽略大小写
    @Readonly		// <- 这里声明了只读字段，即视图里增加的字段
    private int yearNum;

    @Column("yearvalue")	// 其实可以不用声明数据库字段名 "taskcount"，因为多数数据库忽略大小写
    @Readonly		// <- 这里声明了只读字段，即视图里增加的字段
    private double yearValue;

//    public long getLinkId(){
//        return linkId;
//    }
//
//    public void setLinkId( long linkId ){
//        this.linkId = linkId;
//    }


//    @Column("taskcount")	// 其实可以不用声明数据库字段名 "taskcount"，因为多数数据库忽略大小写
//    @Readonly		// <- 这里声明了只读字段，即视图里增加的字段
//    private int taskCount;

    /**
     * 所属区域
     */
    @Column
    protected byte areaType;


    public byte getAreaType(){
        return areaType;
    }

    public void setAreaType( byte areaType ){
        this.areaType = areaType;
    }



    public String getMapX(){
        return mapX;
    }

    public void setMapX( String mapX ){
        this.mapX = mapX;
    }

    public String getMapY(){
        return mapY;
    }

    public void setMapY( String mapY ){
        this.mapY = mapY;
    }

    public String getMapAddress(){
        return mapAddress;
    }

    public void setMapAddress( String mapAddress ){
        this.mapAddress = mapAddress;
    }

    public String getContact(){
        return contact;
    }

    public void setContact( String contact ){
        this.contact = contact;
    }




    public int getId(){
        return id;
    }

    public void setId( int id ){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName( String name ){
        this.name = name;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress( String address ){
        this.address = address;
    }

    public String getDescription(){
//        enterprise.setDescription( enterprise.getDescription() );//替换相对于json的非法字符
        return description;
    }

    public void setDescription( String description ){
        this.description = description;
    }

    public double getYearValue() {
        return yearValue;
    }

    public int getYearNum() {
        return yearNum;
    }
}
