import { unit } from '@ant-design/cssinjs';
import { GenerateStyle, ProAliasToken } from '@ant-design/pro-components';
import { useStyle as useAntdStyle } from '@ant-design/pro-provider';

interface ListItemToken extends ProAliasToken {
  componentCls: string;
}


const genSelectListItemStyle: GenerateStyle<ListItemToken> = (token: ListItemToken) => {
  const { componentCls, lineWidth, colorBorder, colorPrimary, colorFillContentHover, colorPrimaryBg } = token;


  return {
    [componentCls]: {
      position: 'relative',
      padding: '10px 15px 10px 15px',
      borderBottom: 'solid 0px transparent',
      cursor: 'pointer',
      marginBottom: unit(1),
      '> .bar': {
        position: 'absolute',
        top: 0,
        bottom: 0,
        left: 0,
        width: unit(4),
        background: 'transparent',
      },
      '&:hover': {
        backgroundColor: token.colorFillSecondary,
        borderBottomColor: token.colorFillSecondary,
      },
      '&.selected': {
        backgroundColor: token.colorBorderSecondary,
        '> .bar': {
          background: colorPrimary,
        },
      },
    },
  };
};

export default function useStyle(prefixCls: string) {
  return useAntdStyle('ListBox', (token) => {
    const listItemTokenToken: ListItemToken = {
      ...token,
      componentCls: `.${prefixCls}`,
    };

    return [genSelectListItemStyle(listItemTokenToken)];
  });
}