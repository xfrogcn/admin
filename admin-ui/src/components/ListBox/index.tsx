import { ConfigProvider, Flex, List, ListProps } from 'antd';
import classNames from 'classnames';
import React, { CSSProperties, useContext } from 'react';
import Panel, { PanelProps } from '../Panel';
import useStyle from './style';

interface ListBoxProps<T> extends Omit<PanelProps, 'children'> {
  className?: string;
  style?: CSSProperties;
  listClassName?: string;
  listStyle?: CSSProperties;
  listItemClassName?: string;
  listItemStyle?: CSSProperties;
  dataSource?: ListProps<T>['dataSource'];
  rowKey: keyof T;
  selectedKey: any;
  onSelect: (item: T) => Promise<boolean>;
  renderItem: (item: T) => React.ReactNode | React.ReactNode[];
}

export default function ListBox<T>(props: ListBoxProps<T>) {
  const {
    dataSource,
    rowKey,
    listItemClassName,
    listItemStyle,
    listClassName,
    listStyle,
    selectedKey,
    renderItem,
    onSelect,
    ...restProps
  } = props;

  const { getPrefixCls } = useContext(ConfigProvider.ConfigContext);
  const itemPrefixCls = getPrefixCls('selectbox-item');
  const { wrapSSR, hashId } = useStyle(itemPrefixCls);

  const classString = classNames(itemPrefixCls, listItemClassName, hashId);

  return (
    <Panel panelClassName={listClassName} panelStyle={listStyle} {...restProps}>
      <List
        renderItem={(item) =>
          wrapSSR(
            <Flex
              justify="space-between"
              className={`${classString} ${item[rowKey] == selectedKey ? 'selected' : ''}`}
              style={listItemStyle}
              onClick={async () => {
                let isSelect = true;
                if (onSelect) {
                  isSelect = await onSelect(item);
                }
                if (!isSelect) {
                  return;
                }
              }}
            >
              <div className="bar"></div>
              {renderItem(item)}
            </Flex>,
          )
        }
        dataSource={dataSource}
        split={true}
        rowKey={rowKey}
      />
    </Panel>
  );
}
