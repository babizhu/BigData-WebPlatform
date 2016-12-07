var ioc = {
	conf : {
		type : "org.nutz.ioc.impl.PropertiesProxy",
		fields: {
			paths : ["custom/"]
		}
	},	
	dataSource : {
		factory : "$conf#make",
        args : ["com.alibaba.druid.pool.DruidDataSource", "db."],
        type : "com.alibaba.druid.pool.DruidDataSource"
	},
	dao : {
		type : "org.nutz.dao.impl.NutDao",
		args : [{refer:"dataSource"}]
	}
};