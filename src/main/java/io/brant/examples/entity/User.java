package io.brant.examples.entity;

import io.brant.examples.annotation.ColumnPosition;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@DynamicInsert
@DynamicUpdate
@Entity
public class User {

    @Id
    @Column(name = "USER_CD", length = 20)
    @ColumnPosition(1)
    private String userCd;

    @Column(name = "USER_NM", length = 30)
    @ColumnPosition(2)
    private String userNm;

    @Column(name = "USER_PS", length = 128)
    @ColumnPosition(3)
    private String userPs;

    @Column(name = "USER_TYPE", length = 15)
    @ColumnPosition(4)
    private String userType;

    @Column(name = "EMAIL", length = 30)
    @ColumnPosition(5)
    private String email;

    @Column(name = "HP_NO", length = 15)
    @ColumnPosition(6)
    private String hpNo;

    @Column(name = "LAST_LOGIN_AT")
    @ColumnPosition(8)
    private LocalDateTime lastLoginAt;

    @Column(name = "PASSWORD_UPDATED_AT")
    @ColumnPosition(9)
    private LocalDateTime passwordUpdatedAt;

    @Column(name = "USE_YN", length = 1)
    @ColumnPosition(10)
    private String useYn;

    @Column(name = "REMARK", length = 200)
    @ColumnPosition(11)
    private String remark;
}
