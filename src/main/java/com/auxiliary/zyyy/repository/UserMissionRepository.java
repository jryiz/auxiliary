package com.auxiliary.zyyy.repository;

import com.auxiliary.zyyy.model.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by ucmed on 2017/8/9.
 */
public interface UserMissionRepository extends JpaRepository<UserMission,Integer> {
    List<UserMission> findListByMissionDateAndIsDeleteAndMissionStatus(
            String missionDate, String isDelete,String missionStatus);
}
