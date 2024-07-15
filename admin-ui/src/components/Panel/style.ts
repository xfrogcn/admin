import { unit } from '@ant-design/cssinjs';
import { GenerateStyle, ProAliasToken } from '@ant-design/pro-components';
import { useStyle as useAntdStyle } from '@ant-design/pro-provider';

interface PanelToken extends ProAliasToken {
  componentCls: string;
}


const genSelectBoxStyle: GenerateStyle<PanelToken> = (token: PanelToken) => {
  const { componentCls, lineWidth, colorBorder } = token;


  return {
    [componentCls]: {
      border: `${unit(lineWidth)} solid ${colorBorder}`,
      backgroundColor: token.colorBgContainer,
      position: 'relative',
      flex: 1,
      marginTop: unit(10),
      overflow: 'auto',

      '& .panel-body': {
        position: 'absolute',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        overflow: 'auto',
      },
    }
  };
};

export default function useStyle(prefixCls: string) {
  return useAntdStyle('Panel', (token) => {
    const panelToken: PanelToken = {
      ...token,
      componentCls: `.${prefixCls}`,
    };

    return [genSelectBoxStyle(panelToken)];
  });
}