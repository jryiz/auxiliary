package com.auxiliary.zyyy.mapper;

import com.auxiliary.zyyy.model.UserMission;
import com.auxiliary.zyyy.model.ZyyyUser;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/8/11
 */
@Mapper
public interface ZyyyMapper {
    @Select({
            "SELECT * FROM zyyy_user WHERE is_delete = 0",
            "AND id IN ( SELECT user_id FROM user_mission",
            "WHERE is_delete = 0 AND mission_date = #{missionDate,jdbcType=VARCHAR}",
            "GROUP BY user_id )"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "login_name", property = "loginName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_name", property = "userName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "id_card", property = "idCard", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sex", property = "sex", jdbcType = JdbcType.VARCHAR),
            @Result(column = "session_id", property = "sessionId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "is_delete", property = "isDelete", jdbcType = JdbcType.VARCHAR) }
    )
    List<ZyyyUser> findMissionUserByDate(@Param("missionDate")String missionDate);
}
