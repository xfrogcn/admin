package com.xfrog.platform.application.permission.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "创建用户信息请求")
public class CreateUserRequestDTO {
    @Schema(description = "用户账户名")
    @NotEmpty()
    @Size(min = 3, max = 64)
    private String userName;
    @Schema(description = "用户密码")
    private String password;
    @Schema(description = "所属组织机构ID")
    private Long organizationId;
    @Schema(description = "用户姓名")
    @NotEmpty()
    @Size(min = 0, max = 64)
    private String name;
    @Schema(description = "性别")
    private String sex;
    @Schema(description = "电话")
    @Size(min = 0, max = 32)
    private String mobilePhone;
    @Schema(description = "邮件地址")
    @Size(min = 0, max = 128)
    private String email;
    @Schema(description = "是否启用")
    @NotNull
    public Boolean enabled;
}
