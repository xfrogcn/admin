import { css } from '@emotion/css';
import { theme } from 'antd';
import classNames from 'classnames';
import React, { CSSProperties, useMemo } from 'react';
import { getBorderCss, convertSpace, SpaceSize, getScrollbarColorCss, convertMarginSpaceProps, MarginSpaceProps } from '../common';

export interface PanelProps extends MarginSpaceProps {
  className?: string;
  style?: CSSProperties;
  panelClassName?: string;
  panelStyle?: CSSProperties;
  children: JSX.Element;
}

const usePanelStyles = () => {
  const { token } = theme.useToken();
  return css`
    border: ${getBorderCss(token)};
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
    scrollbar-color: ${getScrollbarColorCss(token)};
  `;
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
    return convertMarginSpaceProps(props, token)
  }, [props.spaceBottom, props.spaceLeft, props.spaceRight, props.spaceTop, token]);

  return (
    <div className={classString} style={{...spaceStyle, ...style}}>
      {children}
    </div>
  );
}
