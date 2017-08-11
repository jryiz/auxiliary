package com.auxiliary.zyyy.mapper;

import com.auxiliary.zyyy.model.UserMission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/8/11
 */
@Mapper
public interface ZyyyMapper {
    @Select({
            "select * from user_mission"
    })
    List<UserMission> findAll();
}
