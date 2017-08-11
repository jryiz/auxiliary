package com.auxiliary.zyyy.repository;

import com.auxiliary.zyyy.model.ZyyyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by ucmed on 2017/8/9.
 */
public interface ZyyyUserRepository extends JpaRepository<ZyyyUser,Integer> {
    public List<ZyyyUser> findAllByIsDelete(String isDelete);
    ZyyyUser findByLoginNameAndIsDelete(String loginName,String isDelete);
}
