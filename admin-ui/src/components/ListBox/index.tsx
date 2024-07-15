import { ConfigProvider, Flex, List, ListProps } from 'antd';
import classNames from 'classnames';
import React, { CSSProperties, useContext } from 'react';
import Panel from '../Panel';
import useStyle from './style';

interface ListBoxProps<T> {
  className?: string;
  style?: CSSProperties;
  lsitClassName?: string;
  listStyle?: CSSProperties;
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
    className,
    style,
    lsitClassName,
    listStyle,
    selectedKey,
    renderItem,
    onSelect,
  } = props;

  const { getPrefixCls } = useContext(ConfigProvider.ConfigContext);
  const itemPrefixCls = getPrefixCls('selectbox-item');
  const { wrapSSR, hashId } = useStyle(itemPrefixCls);

  const classString = classNames(itemPrefixCls, className, hashId);

  return (
    <Panel>
      <List
        renderItem={(item) =>
          wrapSSR(
            <Flex
              justify="space-between"
              className={`${classString} ${item[rowKey] == selectedKey ? 'selected' : ''}`}
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
