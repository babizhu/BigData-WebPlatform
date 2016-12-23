package org.bbz.bigdata.webplatform.service;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.bbz.bigdata.webplatform.bean.User;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.random.R;
import org.nutz.service.IdNameEntityService;

import java.util.Date;
import java.util.List;

/**
 * Created by liulaoye on 16-10-25.
 * UserService
 */
@IocBean(fields = "dao")
public class UserService extends IdNameEntityService<User>{

    public List<User> getAll(){
        return dao().query( User.class, null, null );
    }

    public User add( String name, String password ){
        User user = new User();
        user.setName( name.trim() );
        user.setSalt( R.UU16() );
        user.setPassword( new Sha256Hash( password, user.getSalt() ).toHex() );
        user.setCreateTime( new Date() );
        user.setUpdateTime( new Date() );
        return dao().insert( user );
    }

    public User add( User user ){


        user.setSalt( R.UU16() );
        user.setPassword( new Sha256Hash( user.getPassword(), user.getSalt() ).toHex() );
        user.setCreateTime( new Date() );
        user.setUpdateTime( new Date() );
        return dao().insert( user );
    }

    public int fetch( String username, String password ){
        User user = fetch( username );
        if( user == null ) {
            return -1;
        }
        String tempPass = new Sha256Hash( password, user.getSalt() ).toHex();
        if( tempPass.equalsIgnoreCase( user.getPassword() ) ) {
            return user.getId();
        }
        return -1;
    }

    public void updatePassword( int userId, String password ){
        User user = fetch( userId );
        if( user == null ) {
            return;
        }
        user.setSalt( R.UU16() );
        user.setPassword( new Sha256Hash( password, user.getSalt() ).toHex() );
        user.setUpdateTime( new Date() );
        dao().update( user, "^(password|salt|updateTime)$" );
    }

    public void updateIgnoreNull( User user ){
    }
}