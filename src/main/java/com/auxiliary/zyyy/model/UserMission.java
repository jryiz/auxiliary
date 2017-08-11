package com.auxiliary.zyyy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @auther ucmed Wenjun Choi
 * @create 2017/8/9
 */
@Entity
public class UserMission {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer userId;
    @Column(length = 10)
    private String doctorName;
    @Column(length = 10)
    private String missionDate;
    @Column(length = 4)
    private String hospitalId;
    @Column(length = 20)
    private String deptName;
    @Column(length = 2)
    private String isDelete;
    @Column(length = 15)
    private String cardNo;
    private Date createDate;
    private Date updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getMissionDate() {
        return missionDate;
    }

    public void setMissionDate(String missionDate) {
        this.missionDate = missionDate;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}
