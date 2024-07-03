package com.xfrog.platform.application.permission.api;

import com.xfrog.platform.application.permission.api.dto.CreateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.api.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.api.dto.QueryOrganizationRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateOrganizationRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "OrganizationApi", description = "组织管理接口")
@RequestMapping("/api/organizations")
public interface OrganizationApi {
    @PostMapping
    @Operation(summary = "创建组织")
    Long createOrganization(@Valid @RequestBody CreateOrganizationRequestDTO organization);

    @GetMapping("/list")
    @Operation(summary = "查询组织列表")
    List<OrganizationDTO> listOrganizations(QueryOrganizationRequestDTO queryDTO);

    @GetMapping("/{organizationId}")
    @Operation(summary = "查询组织")
    OrganizationDTO getOrganization(@PathVariable("organizationId") Long organizationId);

    @PostMapping("/{organizationId}")
    @Operation(summary = "更新组织")
    void updateOrganization(@PathVariable("organizationId") Long organizationId, @Valid @RequestBody UpdateOrganizationRequestDTO organization);

    @DeleteMapping("/{organizationId}")
    @Operation(summary = "删除组织")
    void deleteOrganization(@Valid @PathVariable("organizationId") Long organizationId);
}
