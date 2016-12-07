package org.bbz.bigdata.webplatform;

/**
 * Created by liulaoye on 16-10-25.
 */

//import org.nutz.integration.shiro.ShiroSessionProvider;

import org.bbz.bigdata.webplatform.mvc.CustomShiroSessionProvider;
import org.nutz.mvc.annotation.*;

@SetupBy(MainSetup.class)
@IocBy(args = {"*js", "ioc/",
        "*anno", "org.bbz.bigdata.webplatform",
        "*tx",
        "*async"})
@Modules(scanPackage = true)
@ChainBy(args="mvc/chain.js")

@Ok("json:full")
@Fail("http:500")
//@Filters({@By(type = CrossOriginFilter.class)})
@SessionBy(CustomShiroSessionProvider.class)

public class MainModule {


}