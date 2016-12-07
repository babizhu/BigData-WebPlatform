package org.bbz.bigdata.webplatform;

import org.bbz.bigdata.webplatform.bean.User;
import org.bbz.bigdata.webplatform.service.UserService;
import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

/**
 * Created by liulaoye on 16-10-25.
 * MainSetup
 */
public class MainSetup implements Setup{
    public void init( NutConfig conf ){
        System.out.println("MainSetup.init()");
        Ioc ioc = conf.getIoc();
        Dao dao = ioc.get( Dao.class );
        Daos.createTablesInPackage( dao, "org.bbz.bigdata.webplatform", false );

        // 初始化默认根用户
        if (dao.count(User.class) == 0) {
            UserService us = ioc.get(UserService.class);
            us.add("admin", "123456");
        }

    }

    public void destroy( NutConfig nc ){

    }
}
