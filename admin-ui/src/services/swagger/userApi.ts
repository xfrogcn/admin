// @ts-ignore
/* eslint-disable */
import request from '@/request';

/** 创建用户 POST /api/users */
export async function createUser(body: API.CreateUserRequestDTO, options?: { [key: string]: any }) {
  return request<number>('/api/users', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取用户详情 GET /api/users/${param0} */
export async function getUserDetail(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserDetailParams,
  options?: { [key: string]: any },
) {
  const { userId: param0, ...queryParams } = params;
  return request<API.UserDTO>(`/api/users/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 更新用户 PUT /api/users/${param0} */
export async function updateUser(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateUserParams,
  body: API.UpdateUserRequestDTO,
  options?: { [key: string]: any },
) {
  const { userId: param0, ...queryParams } = params;
  return request<any>(`/api/users/${param0}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 修改当前用户密码 PUT /api/users/change-password */
export async function changeCurrentUserPassword(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.changeCurrentUserPasswordParams,
  options?: { [key: string]: any },
) {
  return request<any>('/api/users/change-password', {
    method: 'PUT',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 获取当前用户详情 GET /api/users/current */
export async function getCurrentUserDetail(options?: { [key: string]: any }) {
  return request<API.CurrentUserInfoDTO>('/api/users/current', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取当前用户权限列表 GET /api/users/current/permission-codes */
export async function getCurrentUserPermissionCodes(options?: { [key: string]: any }) {
  return request<string[]>('/api/users/current/permission-codes', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 禁用用户 PUT /api/users/disable/${param0} */
export async function disableUser(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.disableUserParams,
  options?: { [key: string]: any },
) {
  const { userId: param0, ...queryParams } = params;
  return request<any>(`/api/users/disable/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 启用用户 PUT /api/users/enable/${param0} */
export async function enableUser(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.enableUserParams,
  options?: { [key: string]: any },
) {
  const { userId: param0, ...queryParams } = params;
  return request<any>(`/api/users/enable/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 为用户授予角色 PUT /api/users/grant-roles/${param0} */
export async function grantRoles(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.grantRolesParams,
  body: number[],
  options?: { [key: string]: any },
) {
  const { userId: param0, ...queryParams } = params;
  return request<any>(`/api/users/grant-roles/${param0}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 查询用户列表 POST /api/users/list */
export async function listUsers(body: API.QueryUserRequestDTO, options?: { [key: string]: any }) {
  return request<API.PageDTOUserDTO>('/api/users/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 重置用户密码 PUT /api/users/reset-password/${param0} */
export async function resetPassword(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.resetPasswordParams,
  options?: { [key: string]: any },
) {
  const { userId: param0, ...queryParams } = params;
  return request<any>(`/api/users/reset-password/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  });
}
