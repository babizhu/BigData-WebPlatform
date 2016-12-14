package org.bbz.bigdata.webplatform.module;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.bbz.bigdata.webplatform.bean.User;
import org.bbz.bigdata.webplatform.cfg.RSAProps;
import org.bbz.bigdata.webplatform.consts.ErrorCode;
import org.bbz.bigdata.webplatform.misc.Base64Utils;
import org.bbz.bigdata.webplatform.misc.RSAUtils;
import org.bbz.bigdata.webplatform.service.UserService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.integration.shiro.SimpleShiroToken;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by liulaoye on 16-10-25.
 * UserModule
 */

@IocBean
@At("/api/user")

public class UserModule extends BaseModule{
    @Inject
    protected UserService userService;
    @Inject
    protected Dao dao;



//    @Filters // 覆盖UserModule类的@Filter设置,因为登陆可不能要求是个已经登陆的Session
    @At
    @GET
    public Object login( @Param("username") String username,
                         @Param("password") String password,
                         @Param("rememberMe") boolean rememberMe,
                         @Param("captcha") String captcha,
//                        @Attr(scope = Scope.SESSION, value = "nutz_captcha") String _captcha,
                         HttpSession session,
                         HttpServletRequest req,
                         HttpServletResponse response ){
        NutMap re = new NutMap();
//        if (!Toolkit.checkCaptcha(_captcha, captcha)) {
//            return re.setv("ok", false).setv("msg", "验证码错误");
//        }

        if( Strings.isBlank( username ) || Strings.isBlank( password ) ) {
            return buildErrorResponse( response, ErrorCode.USER_LOGIN_ERROR );
        }
        String p = decodeRsaPassword( password );

        int userId = userService.fetch( username, p );
        if( userId < 0 ) {

            return buildErrorResponse( response, ErrorCode.USER_LOGIN_ERROR );
        } else {
            session.setAttribute( "me", userId );
            // 完成nutdao_realm后启用.
            final SimpleShiroToken token = new SimpleShiroToken( userId );
            token.setRememberMe( rememberMe );
            SecurityUtils.getSubject().login( token );

//            SecurityUtils.getSubject().getSession().setTimeout(10000);//设置ｓｅｓｓｉｏｎ超时时间
            return re.setv( "ok", true ).setv( "name", username );
        }
    }

    private String decodeRsaPassword( String password ){
        try {
            final byte[] decode = Base64Utils.decode( password );
            return new String( RSAUtils.decryptByPrivateKey( decode, RSAProps.INSTANCE.getPrivateKey() ) );
        } catch( Exception e ) {
            e.printStackTrace();
        }
        return null;
    }

    @At
    @GET
    @RequiresRoles("admin")
    public Object count(){
        NutMap re = new NutMap();
        final int count = dao.count( User.class );
        return re.setv( "ok", true ).setv( "count", count );
    }

    @At
    @GET
    @RequiresAuthentication
    @Ok("json:{locked:'password|salt',ignoreNull:true}")
    public Object query(){

        return userService.getAll();
    }

    /**
     * 数据操作（增删改）统一到这里处理
     *
     * @param op   操作类型1:增 改（通过id是否等于-1区分） 2:删除
     * @param user 当前要操作的元素
     */
    @At
    @GET
    @RequiresUser
    public Object operation( @Param("op") int op, @Param("..") User user,
                             HttpServletResponse response ){
        Object result;

        switch( op ) {
            case 1:
                if( user.getId() == -1 ) {
                    result = add( user, response );
                } else {
                    userService.updateIgnoreNull( user );
                    result = user;
                }
                break;
            case 2:
                userService.delete( user.getId() );
                result = new NutMap( "id", user.getId() );
                break;

            default:
                result = this.buildErrorResponse( response, ErrorCode.OPERATION_NOT_FOUND, op + "" );
        }

        return result;
    }


    private Object add( User user, HttpServletResponse response ){ // 两个点号是按对象属性一一设置

        ErrorCode errorCode = checkUser( user, true );
        if( errorCode != ErrorCode.SUCCESS ) {
            return this.buildErrorResponse( response, errorCode );
        }
        user = userService.add( user );
        return user;
    }


    @At
    public Object update( @Param("password") String password, @Attr("me") int me ){
        if( Strings.isBlank( password ) || password.length() < 6 )
            return new NutMap().setv( "ok", false ).setv( "msg", "密码不符合要求" );
        userService.updatePassword( me, password );
        return new NutMap().setv( "ok", true );
    }

    private ErrorCode checkUser( User user, boolean create ){
        if( user == null ) {
            return ErrorCode.ILLEGAL_ARGUMENT;
        }
        if( create ) {
            if( Strings.isBlank( user.getName() ) || Strings.isBlank( user.getPassword() ) )
                return ErrorCode.ILLEGAL_ARGUMENT;
        } else {
            if( Strings.isBlank( user.getPassword() ) )
                return ErrorCode.ILLEGAL_ARGUMENT;
        }
        String passwd = user.getPassword().trim();
        if( 6 > passwd.length() || passwd.length() > 20 ) {
            return ErrorCode.USER_PASSWD_LEN_TOO_SHORT;
        }
        user.setPassword( passwd );
        if( create ) {
            int count = dao.count( User.class, Cnd.where( "name", "=", user.getName() ) );
            if( count != 0 ) {
                return ErrorCode.USER_HAS_EXIST;
            }
        } else {
            if( user.getId() < 1 ) {
                return ErrorCode.ILLEGAL_ARGUMENT;
            }
        }
        if( user.getName() != null )
            user.setName( user.getName().trim() );
        return ErrorCode.SUCCESS;
    }

}