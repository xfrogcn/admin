// @ts-ignore
/* eslint-disable */
import request from '@/request';

/** 创建语料库 POST /api/langcorpus */
export async function createLangCorpus(
  body: API.CreateLangCorpusRequestDTO,
  options?: { [key: string]: any },
) {
  return request<number[]>('/api/langcorpus', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 查询语料库 GET /api/langcorpus/${param0} */
export async function getLangCorpus(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getLangCorpusParams,
  options?: { [key: string]: any },
) {
  const { langCorpusId: param0, ...queryParams } = params;
  return request<API.LangCorpusDTO>(`/api/langcorpus/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 更新语料库 POST /api/langcorpus/${param0} */
export async function updateLangCorpus(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateLangCorpusParams,
  body: API.UpdateLangCorpusRequestDTO,
  options?: { [key: string]: any },
) {
  const { langCorpusId: param0, ...queryParams } = params;
  return request<any>(`/api/langcorpus/${param0}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 删除语料库 DELETE /api/langcorpus/${param0} */
export async function deleteLangCorpus(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteLangCorpusParams,
  options?: { [key: string]: any },
) {
  const { langCorpusId: param0, ...queryParams } = params;
  return request<any>(`/api/langcorpus/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 启用或禁用语种 POST /api/langcorpus/${param0}/${param1} */
export async function enableLangCorpus(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.enableLangCorpusParams,
  options?: { [key: string]: any },
) {
  const { langCorpusId: param0, enabled: param1, ...queryParams } = params;
  return request<any>(`/api/langcorpus/${param0}/${param1}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 配置语料本地化 PUT /api/langcorpus/${param0}/local */
export async function configLangLocal(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.configLangLocalParams,
  body: Record<string, any>,
  options?: { [key: string]: any },
) {
  const { langCorpusId: param0, ...queryParams } = params;
  return request<any>(`/api/langcorpus/${param0}/local`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 查询语料库列表 POST /api/langcorpus/list */
export async function listLangCorpus(
  body: API.QueryLangCorpusRequestDTO,
  options?: { [key: string]: any },
) {
  return request<API.PageDTOLangCorpusDTO>('/api/langcorpus/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取指定语种的本地化语言 GET /api/langcorpus/local/${param0}/${param1} */
export async function getLangLocal(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getLangLocalParams,
  options?: { [key: string]: any },
) {
  const { application: param0, langCode: param1, ...queryParams } = params;
  return request<Record<string, any>>(`/api/langcorpus/local/${param0}/${param1}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}
