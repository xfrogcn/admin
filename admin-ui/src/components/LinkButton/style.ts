import { GenerateStyle, ProAliasToken } from '@ant-design/pro-components';
import { useStyle as useAntdStyle } from '@ant-design/pro-provider';

interface LinkButtonToken extends ProAliasToken {
  componentCls: string;
}

const genSelectBoxStyle: GenerateStyle<LinkButtonToken> = (token: LinkButtonToken) => {
  const { componentCls } = token;

  return {
    [componentCls]: {
      textDecoration: 'none',

      '&:disabled > *': {
        pointerEvents: 'none',
      },

      [`&${componentCls}-primary`]: {
        color: token.colorPrimary,
        '&:hover': {
          color: token.colorPrimaryTextHover,
        },
        '&:active': {
          color: token.colorPrimaryTextHover,
        },
        '&[disabled]': {
          color: token.colorTextDisabled,
        },
      },

      [`&${componentCls}-success`]: {
        color: token.colorSuccess,
        '&:hover': {
          color: token.colorSuccessTextHover,
        },
        '&:active': {
          color: token.colorSuccess,
        },
        '&[disabled]': {
          color: token.colorTextDisabled,
        },
      },

      [`&${componentCls}-warning`]: {
        color: token.colorWarning,
        '&:hover': {
          color: token.colorWarningTextHover,
        },
        '&:active': {
          color: token.colorWarningTextHover,
        },
        '&[disabled]': {
          color: token.colorTextDisabled,
        },
      },
      [`&${componentCls}-danger`]: {
        color: token.colorError,
        '&:hover': {
          color: token.colorErrorTextHover,
        },
        '&:active': {
          color: token.colorErrorTextHover,
        },
        '&[disabled]': {
          color: token.colorTextDisabled,
        },
      },
    },
  };
};

export default function useStyle(prefixCls: string) {
  return useAntdStyle('Panel', (token) => {
    const linkButtonToken: LinkButtonToken = {
      ...token,
      componentCls: `.${prefixCls}`,
    };

    return [genSelectBoxStyle(linkButtonToken)];
  });
}
