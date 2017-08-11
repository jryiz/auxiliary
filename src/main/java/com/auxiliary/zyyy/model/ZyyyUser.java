package com.auxiliary.zyyy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 用户
 *
 * @auther ucmed Wenjun Choi
 * @create 2017/8/9
 */
@Entity
public class ZyyyUser {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(length = 20)
    private String loginName;
    @Column(length = 50)
    private String password;
    @Column(length = 20)
    private String idCard;
    @Column(length = 2)
    private String sex;
    @Column(length = 10)
    private String userName;
    @Column(length = 50)
    private String sessionId;
    @Column(length = 2)
    private String isDelete;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Column(columnDefinition = "0")
    public String getIsDelete() {
        return isDelete;
    }
    @Column(columnDefinition = "0")
    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}
