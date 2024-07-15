import { ConfigProvider, Flex, List, ListProps, theme } from 'antd';
import classNames from 'classnames';
import React, { CSSProperties, useContext } from 'react';
import useStyle from './style';

interface PanelProps<T> {
  className?: string;
  style?: CSSProperties;
  panelClassName?: string;
  panelStyle?: CSSProperties;
  children: JSX.Element;
}

export default function Panel<T>(props: PanelProps<T>) {

  const { className, style, panelClassName, panelStyle } = props;

  const { getPrefixCls } = useContext(ConfigProvider.ConfigContext);
  const prefixCls = getPrefixCls('panel');
  const { wrapSSR, hashId } = useStyle(prefixCls);

  const classString = classNames(prefixCls, className, hashId);
  const panelClassString = classNames('panel-body', panelClassName)

  const { token } = theme.useToken();

  const children = React.cloneElement(props.children, {className: panelClassString, style: panelStyle});


  return wrapSSR(
    <div className={classString} style={style}>
      {children}
    </div>,
  );
}
