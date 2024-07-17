import { setAlpha } from '@ant-design/pro-components';
import { css } from '@emotion/css';
import { Space, theme } from 'antd';
import classNames from 'classnames';
import React, { CSSProperties, useImperativeHandle, useMemo, useState } from 'react';
import { convertMarginSpaceProps, getBorderCss, MarginSpaceProps } from '../common';

export interface FooterBarAction {
  left: (nodes: React.ReactNode | React.ReactNode[]) => void;
  right: (nodes: React.ReactNode | React.ReactNode[]) => void;
  middle: (nodes: React.ReactNode | React.ReactNode[]) => void;
}

export interface FooterBarProps extends MarginSpaceProps {
  className?: string;
  style?: CSSProperties;
  left?: React.ReactNode | React.ReactNode[];
  right?: React.ReactNode | React.ReactNode[];
  middle?: React.ReactNode | React.ReactNode[];
  containerRef?: React.RefObject<FooterBarAction>;
}

const useFooterBarStyles = () => {
  const { token } = theme.useToken();
  return css`
    height: 68px;
    .wrapper {
        position: absolute;
        left: 0;
        right: 0;
        bottom: 0;
        z-index: 99;
        display: flex;
        align-items: center;
        width: 100%;
        box-sizing: border-box;
        line-height: 64px;
        background-color: ${setAlpha(token.colorBgElevated, 0.6)};
        borderBlock-start: ${getBorderCss(token)};
        '-webkit-backdrop-filter': blur(8px);
        backdrop-filter: blur(8px);
        color: ${token.colorText};
        transition: all 0.2s ease 0s;
        .left {
            flex: 1;
            color: ${token.colorText};
        }
        .right {
            color: ${token.colorText};
            padding-right: ${token.paddingXL}px;
            '> *': {
            marginInlineEnd: 8;
            '&:last-child': {
                marginBlock: 0;
                marginInline: 0;
            }
        }
    }
  `;
};

export default function FooterBar(props: FooterBarProps) {
  const { className, style, containerRef } = props;

  const rootClassName = useFooterBarStyles();
  const classString = classNames(rootClassName, className);
  const { token } = theme.useToken();
  const [left, setLeft] = useState<React.ReactNode | React.ReactNode[]>(props.left);
  const [right, setRight] = useState<React.ReactNode | React.ReactNode[]>(props.right);
  const [middle, setMiddle] = useState<React.ReactNode | React.ReactNode[]>(props.middle);

  const spaceStyle = useMemo(() => {
    return convertMarginSpaceProps(props, token);
  }, [props.spaceBottom, props.spaceLeft, props.spaceRight, props.spaceTop, token]);

  useImperativeHandle(
    containerRef,
    () => ({
      left: setLeft,
      right: setRight,
      middle: setMiddle,
    }),
    [],
  );

  return (
    <div className={classString} style={{ ...spaceStyle, ...style }}>
      <div className="wrapper">
        <div className="left">
          <Space size="middle">{left}</Space>
        </div>
        <div className="middle">
          <Space size="middle">{middle}</Space>
        </div>
        <div className="right">
          <Space size="middle">{right}</Space>
        </div>
      </div>
    </div>
  );
}
