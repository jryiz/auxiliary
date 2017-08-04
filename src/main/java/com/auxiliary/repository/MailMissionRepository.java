package com.auxiliary.repository;

import com.auxiliary.model.MailMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jryiz on 2017/7/17 0017.
 */
public interface MailMissionRepository extends JpaRepository<MailMission,Integer> {
    public List<MailMission> findAllByIsDelete(char isDelete);
}
