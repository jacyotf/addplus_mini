package com.addplus.server.web.serviceimpl.rest.demomodule;

import com.addplus.server.api.annotation.NotToken;
import com.addplus.server.api.annotation.SysLogRecord;
import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.mapper.demo.SysDemoUserMapper;
import com.addplus.server.api.model.demo.SysDemoUser;
import com.addplus.server.api.service.rest.demomodule.HelloService;
import com.addplus.server.api.utils.DataUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service

public class HelloServiceImpl implements HelloService {

    @Autowired
    private SysDemoUserMapper sysDemoUserMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    @NotToken
    public String sayHello(String name) {
        SysDemoUser sysDemoUser = new SysDemoUser();
        sysDemoUser.setId(1);
        sysDemoUser.setVersion(1);
        sysDemoUser.setNickName("test111");
        sysDemoUser.setCreateDate(new Date());
        sysDemoUserMapper.updateById(sysDemoUser);
        sysDemoUserMapper.deleteById(1);
        return "Hello" + name;
    }

    @Override
    @SysLogRecord
    @NotToken
    public SysDemoUser addUser(SysDemoUser user) {
        user.setDeleted(0);
        user.setVersion(1);
        user.setCreateDate(new Date());
        sysDemoUserMapper.insert(user);
        return user;
    }


    @Override
    public String demoSendMessageByPhone(String code, String phone) throws Exception {

        return "SUCCESS";
    }


    @Override
    @SysLogRecord
    /**
     * 个人财产
     */

    public SysDemoUser getDemoOneProperty(Integer demoUserId) throws Exception {
        if (DataUtils.EmptyOrNegativeOrZero(demoUserId)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysDemoUser sysDemoUser = sysDemoUserMapper.findUserWithDemoAddress(demoUserId);
        if (sysDemoUser == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
        }
        return sysDemoUser;
    }

    @Override
    @NotToken
    public SysDemoUser getDenoManyProperty(Integer demoUserId) throws Exception {
        if (DataUtils.EmptyOrNegativeOrZero(demoUserId)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysDemoUser sysDemoUser = sysDemoUserMapper.getDemoUserWithDemoCar(demoUserId);
        if (sysDemoUser == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
        }
        return sysDemoUser;
    }

    @Override
    public SysDemoUser getDenoOneManyProperty(Integer demoUserId,String aaaa) throws Exception {
        if (DataUtils.EmptyOrNegativeOrZero(demoUserId)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysDemoUser sysDemoUser = sysDemoUserMapper.getDemoUserWithDemoCarAndDemoAddress(demoUserId);
        if (sysDemoUser == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
        }
        return sysDemoUser;
    }

    @NotToken
    @Override
    public List<SysDemoUser> getAllDemoUser() throws Exception {
        return sysDemoUserMapper.testXmlDemoUser();
    }

    @Override
    public List<SysDemoUser> getAllDemoUserByList(List<Integer> list) throws Exception {
        if (list == null || list.isEmpty()) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        List<SysDemoUser> list1 = sysDemoUserMapper.selectBatchIds(list);
        return list1;
    }

    @NotToken
    @Override
    @SysLogRecord
   public Boolean insertUser(SysDemoUser user)throws Exception{
        user.setDeleted(0);
        user.setVersion(1);
        user.setCreateDate(new Date());
        int s=sysDemoUserMapper.insert(user);
        if(s==0){
            throw  new ErrorException(ErrorCode.SYS_ERROR_DATABASEFAIL);
        }
        return true;
    }

    @NotToken
    @Override
    @SysLogRecord
    public  Boolean deleteUser (Integer demoUserId)throws Exception {
       int i=sysDemoUserMapper.deleteById(demoUserId);
        if (i==0){
            return false;
        }else {
            return true;
        }
    }

    @NotToken
    @Override
    @SysLogRecord
    public  SysDemoUser selectUser (Integer demoUserId)throws Exception {
        /*获取key为bbb的缓存，如果有则返回去true*/
        boolean b= redisTemplate.hasKey("bbb");
        if(!b){
        /*QueryWrapper->条件构造器*/
        QueryWrapper<SysDemoUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",demoUserId);
        queryWrapper.select("id","nick_name","demo_address_id","deleted","version","create_date");
        SysDemoUser sysDemoUser = sysDemoUserMapper.selectOne(queryWrapper);
        /*SysDemoUser sysDemoUser=sysDemoUserMapper.selectById(demoUserId);*/
        if(sysDemoUser==null){
           throw  new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
        }
        redisTemplate.opsForValue().set("bbb",sysDemoUser,10000, TimeUnit.SECONDS);
            System.out.println("数据库："+sysDemoUser);
            return sysDemoUser;
        }
        System.out.println("缓存："+redisTemplate.opsForValue().get("bbb"));
       return (SysDemoUser)redisTemplate.opsForValue().get("bbb");
    }



    @NotToken
    @Override
    @SysLogRecord
    public Boolean updateUser(Integer demoUserId)throws Exception{
        QueryWrapper<SysDemoUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",demoUserId);
        SysDemoUser sysDemoUser = new SysDemoUser();
        sysDemoUser.setNickName("1312134546");
        sysDemoUser.setCreateDate(new Date());
        int update = sysDemoUserMapper.update(sysDemoUser, queryWrapper);
        if(update==0){
            throw  new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        return true;
    }

    @NotToken
    @Override
    public IPage<SysDemoUser> getAllUsers(Integer pageNo, Integer pageSize) throws Exception {
        if (DataUtils.EmptyOrNegative(pageNo, pageSize)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        Page<SysDemoUser> page = new Page<SysDemoUser>(pageNo, pageSize);
        QueryWrapper<SysDemoUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        queryWrapper.select("id","nick_name","demo_address_id","deleted","version","create_date");
        IPage<SysDemoUser> sysDemoUserIPage = sysDemoUserMapper.selectPage(page, queryWrapper);
        if (sysDemoUserIPage == null || sysDemoUserIPage.getRecords() == null || sysDemoUserIPage.getRecords().isEmpty()) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATA);
        }
        return sysDemoUserIPage;
    }


 /*   @NotToken
    @Override
    *//*    @Scheduled(fixedRate = 5000*3)*//*
    public void  timeo() throws Exception{
 *//*       SysDemoUser sysDemoUser = selectUser(id);
        redisTemplate.opsForValue().set("aaaa",sysDemoUser);*//*
        System.out.println("测试数据是："+redisTemplate.opsForValue().get("bbb"));
        System.out.println(redisTemplate.hasKey("bbb"));
        if(!(redisTemplate.hasKey("bbb"))){
            selectUser(8);
            System.out.println("oooooooooooooooo++++++++++++++++");
        }

*//*        boolean a= redisTemplate.expire("aaaa",5, TimeUnit.SECONDS);
        System.out.println(a);*//*

    }*/
    /*        QueryWrapper<SysDemoUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",1);
        SysDemoUser sysDemoUser = new SysDemoUser();
        sysDemoUser.setNickName("sasasa");
        int update = sysDemoUserMapper.update(sysDemoUser, queryWrapper);
        if (update==0){
            throw new ErrorException(ErrorCode.SYS_ERROR_DATABASEFAIL);
        }
        return update>0;*/
}
