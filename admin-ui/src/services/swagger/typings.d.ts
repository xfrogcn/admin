declare namespace API {
  type changeCurrentUserPasswordParams = {
    arg0: string;
  };

  type createDicItemParams = {
    dicId: number;
  };

  type CreateDicItemRequestDTO = {
    /** 关联的字典 ID */
    dicId: number;
    /** 是否启用 */
    enabled: boolean;
    /** 显示名称 */
    displayText?: string;
    /** 多语言语料编码 */
    langCode?: string;
    /** 显示顺序 */
    displayOrder: number;
    /** 对应值 */
    value?: string;
    /** 扩展值 1 */
    extValue1?: string;
    /** 扩展值 2 */
    extValue2?: string;
    /** 说明 */
    memo?: string;
  };

  type CreateDicRequestDTO = {
    /** 字典类型 */
    type: string;
    /** 字典名称 */
    name: string;
    /** 字典说明 */
    memo?: string;
  };

  type CreateOrganizationRequestDTO = {
    /** 组织名称 */
    name: string;
    /** 父组织ID */
    parentId?: number;
    /** 组织状态 */
    status: 'NORMAL' | 'DISABLED';
    /** 显示顺序 */
    displayOrder: number;
    /** 电话 */
    telephone?: string;
    /** 负责人 */
    principal?: string;
  };

  type CreatePermissionItemRequestDTO = {
    /** 权限项编码 */
    code: string;
    /** 权限项名称 */
    name: string;
    /** 权限项类型 */
    type: string;
    /** 父权限项ID */
    parentId?: number;
    /** 是否为平台权限项 */
    platform: boolean;
  };

  type CreateRoleRequestDTO = {
    /** 角色名称 */
    name: string;
    /** 角色说明 */
    memo?: string;
    /** 是否启用 */
    enabled: boolean;
  };

  type CreateTenantRequestDTO = {
    /** 租户编码 */
    code?: string;
    /** 租户名称 */
    name?: string;
    /** 是否启用 */
    enabled: boolean;
    /** 租户说明 */
    memo?: string;
    /** 租户管理员登录账号 */
    adminUserName: string;
  };

  type CreateUserRequestDTO = {
    /** 用户账户名 */
    userName: string;
    /** 用户密码 */
    password?: string;
    /** 所属组织机构ID */
    organizationId?: number;
    /** 用户姓名 */
    name: string;
    /** 性别 */
    sex?: string;
    /** 电话 */
    mobilePhone?: string;
    /** 邮件地址 */
    email?: string;
    /** 是否启用 */
    enabled: boolean;
  };

  type CurrentUserInfoDTO = {
    /** 用户ID */
    id?: number;
    /** 用户账户名 */
    userName?: string;
    /** 所属组织机构ID */
    organizationId?: number;
    /** 组织名称 */
    organizationName?: string;
    /** 是否过期 */
    accountNonExpired?: boolean;
    /** 是否锁定 */
    accountNonLocked?: boolean;
    /** 是否过期 */
    credentialsNonExpired?: boolean;
    /** 是否启用 */
    enabled?: boolean;
    /** 用户姓名 */
    name?: string;
    /** 性别 */
    sex?: string;
    /** 电话 */
    mobilePhone?: string;
    /** 邮件地址 */
    email?: string;
    /** 最近登录时间 */
    lastLoginTime?: string;
    /** 创建时间 */
    createdTime?: string;
    /** 角色列表 */
    roles?: RoleDTO[];
    /** 根组织ID */
    rootOrganizationId?: number;
    /** 所属租户 */
    tenantId?: string;
  };

  type DataScopeDTO = {
    /** 目标ID */
    targetId?: number;
    /** 目标类型 */
    targetType?: 'USER' | 'ROLE';
    /** 数据范围类型 */
    scopeType?: 'ORGANIZATION' | 'USER_ORGANIZATION';
    /** 数据范围ID */
    scopeId?: number;
    /** 数据范围信息 */
    scopeInfo?: Record<string, any>;
  };

  type DataScopeItem = {
    /** 数据范围类型 */
    scopeType: 'ORGANIZATION' | 'USER_ORGANIZATION';
    /** 数据范围ID */
    scopeId: number;
  };

  type DateTimeRange = {
    begin?: string;
    end?: string;
  };

  type deleteDicParams = {
    dicId: number;
  };

  type deleteOrganizationParams = {
    organizationId: number;
  };

  type deletePermissionItemParams = {
    permissionItemId: number;
  };

  type deleteRoleParams = {
    roleId: number;
  };

  type DicDTO = {
    /** 字典ID */
    id?: number;
    /** 字典类型 */
    type?: string;
    /** 字典名称 */
    name?: string;
    /** 字典说明 */
    memo?: string;
    /** 创建时间 */
    createdTime?: string;
  };

  type disableUserParams = {
    userId: number;
  };

  type enableRoleParams = {
    roleId: number;
    enabled: boolean;
  };

  type enableTenantParams = {
    tenantId: number;
    enabled: boolean;
  };

  type enableUserParams = {
    userId: number;
  };

  type ExceptionResponseExtendValidationResult = {
    errorCode?: string;
    code?: number;
    message?: string;
    details?: string;
    data?: ValidationResultItem[];
  };

  type getDataScopesParams = {
    targetType: 'USER' | 'ROLE';
    targetId: number;
  };

  type getDicParams = {
    dicId: number;
  };

  type getOrganizationParams = {
    organizationId: number;
  };

  type getRolePermissionItemsParams = {
    roleId: number;
  };

  type getUserDataScopesParams = {
    userId: number;
  };

  type getUserDetailParams = {
    userId: number;
  };

  type GrantDataScopeRequestDTO = {
    /** 目标ID */
    targetId: number;
    /** 目标类型 */
    targetType: 'USER' | 'ROLE';
    /** 数据权限项 */
    scopeItems?: DataScopeItem[];
  };

  type grantPermissionItemsParams = {
    roleId: number;
  };

  type grantRolesParams = {
    userId: number;
  };

  type listOrganizationsParams = {
    arg0: QueryOrganizationRequestDTO;
  };

  type OrganizationDTO = {
    /** 组织ID */
    id?: number;
    /** 组织名称 */
    name?: string;
    /** 父组织ID */
    parentId?: number;
    /** 父组织ID列表 */
    parentIds?: number[];
    /** 父组织名称列表 */
    parentNames?: string[];
    /** 组织编码 */
    code?: string;
    /** 组织层级 */
    level?: number;
    /** 组织状态 */
    status?: 'NORMAL' | 'DISABLED';
    /** 显示顺序 */
    displayOrder?: number;
    /** 电话 */
    telephone?: string;
    /** 负责人 */
    principal?: string;
    /** 创建时间 */
    createdTime?: string;
  };

  type PageDTODicDTO = {
    pageNum?: number;
    pageSize?: number;
    total?: number;
    pages?: number;
    data?: DicDTO[];
  };

  type PageDTOTenantDTO = {
    pageNum?: number;
    pageSize?: number;
    total?: number;
    pages?: number;
    data?: TenantDTO[];
  };

  type PageDTOUserDTO = {
    pageNum?: number;
    pageSize?: number;
    total?: number;
    pages?: number;
    data?: UserDTO[];
  };

  type PermissionItemDTO = {
    /** 权限项ID */
    id?: number;
    /** 权限项编码 */
    code?: string;
    /** 权限项名称 */
    name?: string;
    /** 权限项类型 */
    type?: string;
    /** 父权限项ID */
    parentId?: number;
    /** 是否为平台权限项 */
    platform?: boolean;
  };

  type QueryDicRequestDTO = {
    pageNum: number;
    pageSize: number;
    sortItems?: SortItem[];
    /** 关键字 */
    keyword?: string;
  };

  type QueryOrganizationRequestDTO = {
    /** 关键字 */
    keyword?: string;
    /** 上级组织ID */
    parentId?: number;
    /** 组织状态 */
    status?: 'NORMAL' | 'DISABLED';
  };

  type QueryTenantRequestDTO = {
    pageNum: number;
    pageSize: number;
    sortItems?: SortItem[];
    /** 关键字 */
    keyword?: string;
    /** 是否启用 */
    enabled?: boolean;
    createdTime?: DateTimeRange;
  };

  type QueryUserRequestDTO = {
    pageNum: number;
    pageSize: number;
    sortItems?: SortItem[];
    /** 关键字 */
    keyword?: string;
    /** 组织ID */
    organizationId?: number;
    /** 是否启用 */
    enabled?: boolean;
    createdTime?: DateTimeRange;
    lastLoginTime?: DateTimeRange;
  };

  type resetPasswordParams = {
    userId: number;
  };

  type RoleDTO = {
    /** 角色ID */
    id?: number;
    /** 角色名称 */
    name?: string;
    /** 角色说明 */
    memo?: string;
    /** 是否启用 */
    enabled?: boolean;
    /** 创建时间 */
    createdTime?: string;
  };

  type SortItem = {
    field: string;
    order: 'ASC' | 'DESC';
  };

  type TenantDTO = {
    /** 租户ID */
    id?: number;
    /** 租户编码 */
    code?: string;
    /** 租户名称 */
    name?: string;
    /** 是否启用 */
    enabled?: boolean;
    /** 租户说明 */
    memo?: string;
    /** 创建时间 */
    createdTime?: string;
    /** 租户管理员用户ID */
    adminUserId?: number;
    /** 租户管理员登录账号 */
    adminUserName?: string;
  };

  type updateDicItemParams = {
    dicId: number;
    itemId: number;
  };

  type UpdateDicItemRequestDTO = {
    /** 是否启用 */
    enabled: boolean;
    /** 显示名称 */
    displayText?: string;
    /** 多语言语料编码 */
    langCode?: string;
    /** 显示顺序 */
    displayOrder: number;
    /** 对应值 */
    value?: string;
    /** 扩展值 1 */
    extValue1?: string;
    /** 扩展值 2 */
    extValue2?: string;
    /** 说明 */
    memo?: string;
  };

  type updateDicParams = {
    dicId: number;
  };

  type UpdateDicRequestDTO = {
    /** 字典类型 */
    type: string;
    /** 字典名称 */
    name: string;
    /** 字典说明 */
    memo?: string;
  };

  type updateOrganizationParams = {
    organizationId: number;
  };

  type UpdateOrganizationRequestDTO = {
    /** 组织名称 */
    name: string;
    /** 组织状态 */
    status: 'NORMAL' | 'DISABLED';
    /** 显示顺序 */
    displayOrder: number;
    /** 电话 */
    telephone?: string;
    /** 负责人 */
    principal?: string;
  };

  type updatePermissionItemParams = {
    permissionItemId: number;
  };

  type UpdatePermissionItemRequestDTO = {
    /** 权限项名称 */
    name?: string;
    /** 权限项类型 */
    type?: string;
  };

  type updateRoleParams = {
    roleId: number;
  };

  type UpdateRoleRequestDTO = {
    /** 角色名称 */
    name?: string;
    /** 角色说明 */
    memo?: string;
  };

  type updateTenantParams = {
    tenantId: number;
  };

  type UpdateTenantRequestDTO = {
    /** 租户名称 */
    name: string;
    /** 租户说明 */
    memo?: string;
  };

  type updateUserParams = {
    userId: number;
  };

  type UpdateUserRequestDTO = {
    /** 用户所属组织 */
    organizationId?: number;
    /** 用户姓名 */
    name: string;
    /** 性别 */
    sex?: string;
    /** 电话 */
    mobilePhone?: string;
    /** 邮件地址 */
    email?: string;
  };

  type UserDTO = {
    /** 用户ID */
    id?: number;
    /** 用户账户名 */
    userName?: string;
    /** 所属组织机构ID */
    organizationId?: number;
    /** 组织名称 */
    organizationName?: string;
    /** 是否过期 */
    accountNonExpired?: boolean;
    /** 是否锁定 */
    accountNonLocked?: boolean;
    /** 是否过期 */
    credentialsNonExpired?: boolean;
    /** 是否启用 */
    enabled?: boolean;
    /** 用户姓名 */
    name?: string;
    /** 性别 */
    sex?: string;
    /** 电话 */
    mobilePhone?: string;
    /** 邮件地址 */
    email?: string;
    /** 最近登录时间 */
    lastLoginTime?: string;
    /** 创建时间 */
    createdTime?: string;
    /** 角色列表 */
    roles?: RoleDTO[];
  };

  type ValidationResultItem = {
    member?: string;
    messages?: string[];
  };
}
