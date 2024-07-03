import { findById, getAllFolderNodes } from '@/utils/treeItemUtils';
import { useIntl } from '@umijs/max';
import { Input, Tree, TreeDataNode } from 'antd';
import React, { Key, useCallback, useEffect, useMemo, useState } from 'react';

const { Search } = Input;

export interface TreeItem<TData> {
  id: number;
  name: string;
  data?: TData;
  children?: TreeItem<TData>[];
  parentIds: number[];
}

export interface TreeSelectorProps<TData> {
  treeData?: TreeItem<TData>[];
  selectable?: boolean;
  multiple?: boolean;
  checkable?: boolean;
  checkStrictly?: boolean;
  selectedKeys?: number[];
  onSelect?: (selectedKeys: number[]) => void;
  onCheck?: (selectedKeys: number[]) => void;
  className?: string;
  treeClassName?: string;
  style?: React.CSSProperties;
  treeStyle?: React.CSSProperties;
  defaultExpandAll?: boolean;
  onBeforeSelect?: (id: number, selected: boolean) => boolean
}

function TreeSelector<T>(props: TreeSelectorProps<T>): JSX.Element {
  const [searchValue, setSearchValue] = useState('');
  const [expandedKeys, setExpandedKeys] = useState<React.Key[]>([]);
  const [autoExpandParent, setAutoExpandParent] = useState(true);
  const [selectedKeys, setSelectedKeys] = useState<React.Key[]>([]);
  const [checkedKeys, setCheckedKeys] = useState<React.Key[]>([]);

  useEffect(() => {
    if (props.selectedKeys) {
      const parentIds: React.Key[] = [];
      const selectedItems = findById(props.selectedKeys, props.treeData as any);
      selectedItems.forEach((item) => parentIds.push(...item.parentIds));
      parentIds.push(...props.selectedKeys);
      if (!props.defaultExpandAll) {
        setExpandedKeys([...new Set(parentIds)]);
        setAutoExpandParent(true);
      }
      if (props.checkable) {
        setCheckedKeys([...props.selectedKeys]);
      } else {
        setSelectedKeys([...props.selectedKeys]);
      }
      setSearchValue('');
    }
  }, [props.selectedKeys]);

  const onExpand = (newExpandedKeys: React.Key[]) => {
    setExpandedKeys(newExpandedKeys);
    setAutoExpandParent(false);
  };

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    setSearchValue(value);
    setAutoExpandParent(true);
  };

  const treeData = useMemo(() => {
    let parentIds: number[] = [];
    const loop = (data: TreeItem<T>[]): TreeDataNode[] => {
      return data.map((item) => {
        const strTitle = item.name as string;
        const index = strTitle.indexOf(searchValue);
        const beforeStr = strTitle.substring(0, index);
        const afterStr = strTitle.slice(index + searchValue.length);
        if (index >= 0) {
          parentIds.push(...item.parentIds);
        }
        const title =
          index > -1 ? (
            <span>
              {beforeStr}
              <span className="site-tree-search-value">{searchValue}</span>
              {afterStr}
            </span>
          ) : (
            <span>{strTitle}</span>
          );
        if (item.children) {
          return { title, key: item.id.toString(), children: loop(item.children) };
        }

        return {
          title,
          key: item.id.toString(),
        };
      });
    };
    const items = loop(props.treeData as any);
    if (parentIds.length > 0) {
      setExpandedKeys([...new Set(parentIds)]);
    }

    return items;
  }, [props.treeData, searchValue]);

  useEffect(() => {
    if (props.defaultExpandAll) {
      const keys = getAllFolderNodes(props.treeData as any, (item: any) => item.id);
      setExpandedKeys(keys);
    }
  }, [props.defaultExpandAll, props.treeData]);

  const onSelect = useCallback(
    (selectedKeys: React.Key[], info: any) => {
      if (props.checkable) {
        return;
      }
      setSelectedKeys(selectedKeys);
      if (props.onSelect) {
        props.onSelect(selectedKeys as number[]);
      }
    },
    [props.onSelect, props.onBeforeSelect],
  );
  const onCheck = useCallback(
    (checked: { checked: Key[]; halfChecked: Key[] }, info: any) => {
      if (!props.checkable) {
        return;
      }
      let isIgnore = false;
      if (props.onBeforeSelect) {
        isIgnore = !props.onBeforeSelect(info.node.key, info.checked);
      }
      if (isIgnore) {
        return;
      }
      setCheckedKeys(checked.checked);
      if (props.onSelect) {
        props.onSelect(checked.checked as number[]);
      }
    },
    [props.onCheck, props.onBeforeSelect],
  );

  const intl = useIntl();

  return (
    <div style={props.style}>
      <Search
        placeholder={intl.formatMessage({ id: 'admin.ui.components.treeselector.placeholder' })}
        onChange={onChange}
        value={searchValue}
        enterButton
      />
      <div className={props.className} style={props.treeStyle}>
        <Tree
          className={props.treeClassName}
          treeData={treeData}
          onExpand={onExpand}
          expandedKeys={expandedKeys}
          autoExpandParent={autoExpandParent}
          selectable={props.selectable}
          multiple={props.multiple}
          checkable={props.checkable}
          checkStrictly={props.checkStrictly}
          selectedKeys={selectedKeys}
          checkedKeys={{ checked: checkedKeys, halfChecked: [] }}
          onSelect={onSelect}
          onCheck={onCheck as any}
        />
      </div>
    </div>
  );
}

export default TreeSelector;
