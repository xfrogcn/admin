import { TreeItem } from '@/components/TreeSelector';
import { DataNode } from 'antd/es/tree';

export function generateTreeItems<T>(
  listData: T[],
  idGetter: (item: T) => number,
  parentIdGetter: (item: T) => number | undefined | null,
  nameGetter: (item: T) => string,
  sorter?: (a: T, b: T) => number,
): TreeItem<T>[] {
  const idMap: { [key: number]: T } = {};
  listData.forEach((item) => {
    const id = idGetter(item);
    idMap[id] = item;
  });

  const converter = (item: T, parent: TreeItem<T> | null): TreeItem<T> => {
    return {
      id: idGetter(item),
      name: nameGetter(item),
      children: [],
      data: item,
      parentIds: parent ? [...parent.parentIds, parent.id] : [],
    };
  };

  const rootItems: TreeItem<T>[] = [];
  listData.forEach((item) => {
    const parentId = parentIdGetter(item);
    if (parentId === undefined || parentId === null || !idMap[parentId]) {
      rootItems.push(converter(item, null));
    }
  });

  const getAllChildren = (parentId: number, parent: TreeItem<T>): TreeItem<T>[] => {
    const children = listData
      .filter((it) => parentIdGetter(it) == parentId)
      .map((it) => converter(it, parent));
    children.forEach((child) => {
      child.children = getAllChildren(child.id, child);
    });
    return sorter ? children.sort((a, b) => sorter(a.data as any, b.data as any)) : children;
  };

  rootItems.forEach((item) => {
    const children = getAllChildren(item.id, item);
    item.children = children;
  });

  console.log(rootItems);

  return sorter ? rootItems.sort((a, b) => sorter(a.data as any, b.data as any)) : rootItems;
}

export type DataNodeEx<T> = DataNode & {
  data: T;
  children?: DataNodeEx<T>[];
  parentIds: React.Key[];
};

export function generateDataNodes<T>(
  listData: T[],
  idGetter: (item: T) => number,
  parentIdGetter: (item: T) => number | undefined | null,
  nameGetter: (item: T) => string,
  cusomter?: (item: DataNodeEx<T>) => void,
  sorter?: (a: T, b: T) => number,
): DataNodeEx<T>[] {
  const idMap: { [key: number]: T } = {};
  listData.forEach((item) => {
    const id = idGetter(item);
    idMap[id] = item;
  });

  const converter = (item: T, parent: DataNodeEx<T> | null): DataNodeEx<T> => {
    const node = {
      key: idGetter(item),
      title: nameGetter(item),
      children: [],
      data: item,
      parentIds: parent ? [...parent.parentIds, parent.key] : [],
    };
    if (cusomter) {
      cusomter(node);
    }
    
    return node;
  };

  const rootItems: DataNodeEx<T>[] = [];
  listData.forEach((item) => {
    const parentId = parentIdGetter(item);
    if (parentId === undefined || parentId === null || !idMap[parentId]) {
      rootItems.push(converter(item, null));
    }
  });

  const getAllChildren = (parentId: React.Key, parent: DataNodeEx<T>): DataNodeEx<T>[] => {
    const children = listData
      .filter((it) => parentIdGetter(it) == parentId)
      .map((it) => converter(it, parent));
    children.forEach((child) => {
      child.children = getAllChildren(child.key, child);
    });
    return sorter ? children.sort((a, b) => sorter(a.data as any, b.data as any)) : children;
  };

  rootItems.forEach((item) => {
    const children = getAllChildren(item.key, item);
    item.children = children;
  });

  console.log(rootItems);

  return sorter ? rootItems.sort((a, b) => sorter(a.data as any, b.data as any)) : rootItems;
}

export function findById<T>(id: number[], listData: TreeItem<T>[]): TreeItem<T>[] {
  if (!id || id.length === 0 || !listData) {
    return [];
  }

  const items: TreeItem<T>[] = [];

  const findAllChildren = (parent: TreeItem<T>) => {
    if (id.includes(parent.id)) {
      items.push(parent);
    }
    if (parent.children) {
      parent.children.forEach((child) => {
        findAllChildren(child);
      });
    }
  };

  listData.forEach((item) => {
    findAllChildren(item);
  });

  return items;
}

export type withChildrenType<T> = T & {
  children: T[];
};

export function generateTree<T>(
  listData: T[],
  idGetter: (item: T) => number,
  parentIdGetter: (item: T) => number | undefined | null,
  sorter?: (a: T, b: T) => number,
): withChildrenType<T>[] {
  type resultType = withChildrenType<T>;

  const idMap: { [key: number]: T } = {};
  listData.forEach((item) => {
    const id = idGetter(item);
    idMap[id] = item;
  });

  const rootItems: resultType[] = [];
  listData.forEach((item) => {
    const parentId = parentIdGetter(item);
    if (parentId === undefined || parentId === null || !idMap[parentId]) {
      rootItems.push(item as resultType);
    }
  });

  const getAllChildren = (parentId: number, parent: T): T[] => {
    const children: resultType[] = listData
      .filter((it) => parentIdGetter(it) == parentId)
      .map((it) => it as resultType);
    children.forEach((child) => {
      child.children = getAllChildren(idGetter(child), child);
    });
    return sorter ? children.sort(sorter) : children;
  };

  rootItems.forEach((item) => {
    const children = getAllChildren(idGetter(item), item);
    item.children = children;
  });

  return sorter ? rootItems.sort(sorter) : rootItems;
}

export function getAllFolderNodes<T extends { children: any[] }>(
  nodes: T[] | any[],
  keyGetter: (item: T) => React.Key,
): React.Key[] {
  const keys: React.Key[] = [];

  const getAllChildren = (node: T) => {
    if (node.children && node.children.length > 0) {
      keys.push(keyGetter(node));
      node.children.forEach((child) => {
        getAllChildren(child);
      });
    }
  };

  nodes.forEach((node) => {
    getAllChildren(node);
  });

  return keys;
}
