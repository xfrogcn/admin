import { CloseOutlined } from '@ant-design/icons';
import { RouteContext } from '@ant-design/pro-components';
import { history, useAppData } from '@umijs/max';
import { Flex, Tabs } from 'antd';
import KeepAlive, { useKeepaliveRef } from 'keepalive-for-react';
import { useCallback, useContext, useEffect, useMemo, useRef, useState } from 'react';
import { useLocation, useOutlet } from 'react-router';

export interface PageTabsProps {}

export const PageTabs = (props: PageTabsProps): JSX.Element => {
  const [tabs, setTabs] = useState<any[]>([]);
  const map = useRef<Record<string, any>>({});
  const routes = useContext(RouteContext);
  const appData = useAppData();

  const outlet = useOutlet();
  const location = useLocation();

  const keepalive = useKeepaliveRef();

  console.log(keepalive)

  useEffect(() => {
    if (location.pathname === '/') {
      return;
    }
    const key = location.pathname + location.search;
    let tabItem = map.current[key];
    if (!tabItem) {
      tabItem = {
        key: key,
        closable: tabs.length === 0 ? false : true,
        label: routes.currentMenu?.name,
      };
      map.current[key] = { ...location };
      if (tabs.length === 1) {
        tabs[0].closable = true;
      }
      setTabs([...tabs, tabItem]);
    }
  }, [routes.currentMenu, location]);

  const cacheKey = useMemo(() => {
    return location.pathname + location.search;
  }, [location]);

  const isCache = useMemo(() => {
    if (!routes.currentMenu) {
      return false;
    }
    const routeInfo = appData.routes[routes.currentMenu.id];
    return routeInfo && !!!routeInfo.redirect;
  }, [routes.currentMenu, appData]);

  const onEdit = useCallback(
    (targetKey: React.MouseEvent | React.KeyboardEvent | string, action: 'add' | 'remove') => {
      if (action === 'add') {
        return;
      }

      const itemIndex = tabs.findLastIndex((it) => it.key === targetKey);
      if (itemIndex >= 0) {
        keepalive.current?.removeCache(targetKey as string);
        const newTabs = tabs.filter((it) => it.key !== targetKey);
        if (newTabs.length === 1) {
          newTabs[0].closable = false;
        }
        setTabs(newTabs);
        delete map.current[targetKey as string];
        const switchTabIndex = itemIndex === 0 ? 0 : itemIndex - 1;
        const switchLocation =
          switchTabIndex >= newTabs.length ? { pathname: '/' } : map.current[newTabs[switchTabIndex].key];
        history.push(switchLocation);
      }
    },
    [tabs, setTabs, keepalive],
  );

  const removeIcon = tabs.length == 1 ? false : <CloseOutlined />

  return (
    <Flex vertical style={{ height: '100%', position: 'relative' }}>
      <Tabs
        className="page-tabs"
        hideAdd
        activeKey={cacheKey}
        type="editable-card"
        items={tabs}
        animated
        removeIcon = {removeIcon}
        onTabClick={(key) => {
          history.push(map.current[key]);
        }}
        onEdit={onEdit}
      />
      <KeepAlive
        children={outlet}
        activeName={cacheKey}
        cache={isCache}
        max={50}
        strategy={'LRU'}
        aliveRef={keepalive}
      />
    </Flex>
  );
};
