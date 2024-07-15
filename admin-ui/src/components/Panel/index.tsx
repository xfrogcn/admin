import { unit } from '@ant-design/cssinjs';
import { css } from '@emotion/css';
import { GlobalToken, theme } from 'antd';
import classNames from 'classnames';
import React, { CSSProperties, useMemo } from 'react';

export type SpaceSize = 'xxs' | 'xs' | 'sm' | 'nm' | 'md' | 'lg' | 'xl' | 'xxl' | number | string;

export interface PanelProps {
  className?: string;
  style?: CSSProperties;
  panelClassName?: string;
  panelStyle?: CSSProperties;
  children: JSX.Element;
  spaceLeft?: SpaceSize;
  spaceTop?: SpaceSize;
  spaceRight?: SpaceSize;
  spaceBottom?: SpaceSize;
}

const usePanelStyles = () => {
  const { token } = theme.useToken();
  return css`
    border: ${unit(token.lineWidth)} solid ${token.colorBorder};
    background-color: ${token.colorBgContainer};
    position: relative;
    flex: 1;
    overflow: auto;
  `;
};

const usePanelBodyStyles = () => {
  const { token } = theme.useToken();
  return css`
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    overflow: auto;
    scrollbar-color: ${token.colorTextPlaceholder} ${token.colorSplit};
  `;
};

const calSpace = (size: SpaceSize, token: GlobalToken) => {
  switch (size) {
    case 'xxs':
      return token.marginXXS;
    case 'xs':
      return token.marginXS;
    case 'sm':
      return token.marginSM;
    case 'nm':
      return token.margin;
    case 'md':
      return token.marginMD;
    case 'lg':
      return token.marginLG;
    case 'xl':
      return token.marginXL;
    case 'xxl':
      return token.marginXXL;
    default:
      return size;
  }
};

export default function Panel(props: PanelProps) {
  const { className, style, panelClassName, panelStyle } = props;

  const classString = classNames(usePanelStyles(), className);
  const panelClassString = classNames(usePanelBodyStyles(), panelClassName);
  const { token } = theme.useToken();

  const children = React.cloneElement(props.children, {
    className: panelClassString,
    style: panelStyle,
  });
  const spaceStyle = useMemo(() => {
    const css: CSSProperties = {};
    
    if (props.spaceTop !== undefined) {
      css.marginTop = calSpace(props.spaceTop, token);
    }
    if (props.spaceRight !== undefined) {
      css.marginRight = calSpace(props.spaceRight, token);
    }
    if (props.spaceBottom !== undefined) {
      css.marginBottom = calSpace(props.spaceBottom, token);
    }
    if (props.spaceLeft !== undefined) {
      css.marginLeft = calSpace(props.spaceLeft, token);
    }
    return css;
  }, [props.spaceBottom, props.spaceLeft, props.spaceRight, props.spaceTop, token]);

  return (
    <div className={classString} style={{...spaceStyle, ...style}}>
      {children}
    </div>
  );
}
