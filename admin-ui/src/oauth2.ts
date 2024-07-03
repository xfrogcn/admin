import { UserManager, UserManagerSettings } from 'oidc-client-ts';
import { urls } from './config';

export const signinRedirectCallbackPath = '/signin-redirect-callback';
export const signoutRedirectCallbackPath = '/signout-redirect-callback';

export const oidcConfig: UserManagerSettings = {
  authority: urls.auth(),
  response_type: 'code',
  client_id: 'admin-ui',
  redirect_uri: `${urls.ui()}${signinRedirectCallbackPath}`,
  post_logout_redirect_uri: `${urls.ui()}${signoutRedirectCallbackPath}`,
  scope: 'openid',
};

export const userManager = new UserManager(oidcConfig);
