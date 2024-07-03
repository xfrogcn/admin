package com.xfrog.platform.domain.permission.aggregate;


import com.xfrog.framework.domain.AuditEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Getter
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User extends AuditEntity {
    private String userName;
    private String password;
    private Long organizationId;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private String name;
    private String sex;
    private String mobilePhone;
    private String email;
    private Timestamp lastLoginTime;
    private String tenantId;

    public void update(Long organizationId, String name, String sex, String mobilePhone, String email) {
        this.organizationId = organizationId;
        this.name = name;
        this.sex = sex;
        this.mobilePhone = mobilePhone;
        this.email = email;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
