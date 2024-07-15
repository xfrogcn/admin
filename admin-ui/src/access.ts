import React from 'react';

/**
 * @see https://umijs.org/docs/max/access#access
 * */
export default function access(
  initialState: { currentUser?: API.UserDTO; permissions?: Record<string, true> } | undefined,
) {
  return {
    canRoute: (route: any) => {
      return !!(initialState?.permissions || {})[route.permissionCode];
    },
    hasPermission: (permissionCode: string) => {
      return !!(initialState?.permissions || {})[permissionCode];
    },
  };
}

export function accessOperate<T>(
  operations: Record<string, T>,
  accessCheck: {
    canRoute: (route: any) => boolean;
    hasPermission: (permissionCode: string) => boolean;
  },
): T[] {
  const buttons: T[] = [];

  for (const key in operations) {
    if (operations.hasOwnProperty(key)) {
      if (accessCheck.hasPermission(key)) {
        buttons.push(operations[key]);
      }
    }
  }

  return buttons;
}

export function withAccessRender<T>(
  operations: Record<string, (dom: React.ReactNode, record: T) => JSX.Element>,
  accessCheck: {
    canRoute: (route: any) => boolean;
    hasPermission: (permissionCode: string) => boolean;
  },
): {
  hasPermisions: boolean;
  permissionCodes: string[];
  columnWidth: string;
  render: (dom: React.ReactNode, record: T) => JSX.Element[];
} {
  const renderList: ((dom: React.ReactNode, record: T) => JSX.Element)[] = [];
  const permissionCodes: string[] = [];

  for (const key in operations) {
    if (operations.hasOwnProperty(key)) {
      if (accessCheck.hasPermission(key)) {
        renderList.push(operations[key]);
        permissionCodes.push(key);
      }
    }
  }

  const result= {
    hasPermisions: renderList.length> 0,
    permissionCodes: permissionCodes,
    columnWidth: permissionCodes.length * 2.5 + 1 + 'em',
    render: (dom: React.ReactNode, record: T) => {
      const buttons: JSX.Element[] = [];
      for (let i = 0; i < renderList.length; i++) {
        const button = renderList[i](dom, record);
        if (button) {
          buttons.push(button);
        }
      }
      return buttons;
    },
  }

  return result;
}
