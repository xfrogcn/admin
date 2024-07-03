import { request } from '@umijs/max';
import { message } from 'antd';
import { userManager } from './oauth2';

const enhanceRequest = async <T = any>(url: string, opts: any = { method: 'GET' }): Promise<T> => {
  const options = opts || {};
  options.getResponse = true;
  let response = null;
  try {
    response = await request<T>(url, options);
    return response.data;
  } catch (e: any) {
    const errorResponse: any = e?.response;
    if (errorResponse && errorResponse.status === 401) {
      const userInfo = await userManager.getUser();
      if (userInfo && userInfo.refresh_token) {
        try {
          await userManager.signinSilent();
        } catch (ex) {
          console.log('refresh token error: ', ex);
          await userManager.removeUser();
          await userManager.signinRedirect();
          return null as any;
        }
        response = await request<T>(url, options);
      } else {
        await userManager.signinRedirect();
      }
    } else {
      if (errorResponse && errorResponse.data && errorResponse.data.code) {
        message.error(errorResponse.data.message || errorResponse.data.errorCode);
        throw new Error(errorResponse.data);
      } else {
        message.error('Unknown error');
        console.error("Unknown request error", e);
      }
    }
  }

  return response?.data as any;
};

export default enhanceRequest;
