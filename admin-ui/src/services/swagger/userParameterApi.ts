// @ts-ignore
/* eslint-disable */
import request from '@/request';

/** 获取用户当前所有配置参数 GET /api/user-parameters/${param0} */
export async function getUserSettings(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserSettingsParams,
  options?: { [key: string]: any },
) {
  const { application: param0, ...queryParams } = params;
  return request<API.UserSettingsDTO>(`/api/user-parameters/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 更新用户参数 PUT /api/user-parameters/${param0} */
export async function updateUserParameters(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateUserParametersParams,
  body: API.UpdateUserParameterRequestDTO,
  options?: { [key: string]: any },
) {
  const { application: param0, ...queryParams } = params;
  return request<any>(`/api/user-parameters/${param0}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}
