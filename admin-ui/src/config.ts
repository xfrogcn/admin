const apiUrlMap = {
  dev: {
    auth: 'http://192.168.3.244:8080',
    api: 'http://192.168.3.244:8080',
    ui: 'http://localhost:8000',
  },
  prod: {
    auth: 'http://localhost:8080',
    api: 'http://localhost:8080',
    ui: 'http://localhost:8000',
  },
};

const isDev = process.env.NODE_ENV === 'development';

declare global {
  interface Window {
    authUrl?: string;
    apiUrl?: string;
    uiUrl?: string;
    applicationCode?: string;
  }
}

export const urls = {
  auth: () => (window.authUrl || isDev ? apiUrlMap.dev.auth : apiUrlMap.prod.auth),
  api: () => (window.apiUrl || isDev ? apiUrlMap.dev.api : apiUrlMap.prod.api),
  ui: () => (window.uiUrl || isDev ? apiUrlMap.dev.ui : apiUrlMap.prod.ui),
};

export function applicationCode() {
  return window.applicationCode || 'admin-ui';
}