import KeepAlive, { useEffectOnActive, useKeepaliveRef } from '@/components/Keepalive';
import { CloseOutlined, ReloadOutlined } from '@ant-design/icons';
import { RouteContext } from '@ant-design/pro-components';
import { FormattedMessage, history, useAppData } from '@umijs/max';
import { Button, Flex, Tabs, Tooltip } from 'antd';
import React, { useCallback, useContext, useMemo, useRef, useState } from 'react';
import { useLocation, useOutlet } from 'react-router';

type TabTo =
  | {
      pathname: string;
      search?: string;
    }
  | string;

export interface PageTabsProps {}
export interface PageTabContextObject {
  setTabLabel: (label: string | any) => void;
  close: (to?: TabTo, reload?: boolean) => void;
  closeTab: (key: string, to?: TabTo, reload?: boolean) => void;
  reloadComplete: () => void;
  tab: { defaultLabel: string };
  key?: string;
  mustReload: () => boolean;
  refresh: () => void;
}

const nilfunction = () => void {};
export const PageTabContext = React.createContext<PageTabContextObject>({
  setTabLabel: nilfunction,
  close: nilfunction,
  closeTab: nilfunction,
  tab: { defaultLabel: '' },
  mustReload: () => false,
  reloadComplete: nilfunction,
  refresh: nilfunction,
});

export const usePageTabContext = () => {
  return useContext(PageTabContext);
};

export const usePageTabReload = (reloader: (() => Promise<void>) | (() => void)): void => {
  const pageTabContext = usePageTabContext();
  useEffectOnActive(
    (active) => {
      if (active && pageTabContext.mustReload()) {
        pageTabContext.reloadComplete();
        reloader();
      }
    },
    true,
    [pageTabContext],
  );
};

const ALL_TAB_KEY = 'ALL_TAB';

function getCacheKey(location: { pathname: string; search?: string }) {
  return location.pathname + (location.search || '');
}

export const PageTabs = (props: PageTabsProps): JSX.Element => {
  const [tabs, setTabs] = useState<any[]>([]);
  const [, updateState] = useState<any>();
  const forceUpdate = useCallback(() => updateState({}), []);
  const map = useRef<Record<string, any>>({});
  const routes = useContext(RouteContext);
  const appData = useAppData();

  const outlet = useOutlet();
  const location = useLocation();

  const keepalive = useKeepaliveRef();

  const pageTabContext = useMemo(() => {
    if (location.pathname === '/') {
      return {
        setTabLabel: nilfunction,
        tab: { defaultLabel: '' },
        close: nilfunction,
        closeTab: nilfunction,
        mustReload: () => false,
        reloadComplete: nilfunction,
        refresh: nilfunction,
      };
    }

    const key = getCacheKey(location);
    let tabItem = null;
    if (!map.current[key]) {
      tabItem = {
        key: key,
        closable: tabs.length === 0 ? false : true,
        label: routes.currentMenu?.name,
        defaultLabel: routes.currentMenu?.name,
      };
      map.current[key] = { location: { ...location }, reload: false };
      if (tabs.length === 1) {
        tabs[0].closable = true;
      }
      const newTabs = [...tabs, tabItem];
      map.current[ALL_TAB_KEY] = newTabs;
      setTabs(newTabs);
    } else {
      tabItem = tabs.findLast((item) => item.key === key);
    }

    const closeTab = (key: string, to?: TabTo, reload?: boolean) => {
      const tabs: { key: string; closable: boolean }[] = map.current[ALL_TAB_KEY] || [];
      const itemIndex = tabs.findLastIndex((it) => it.key === key);
      if (itemIndex >= 0) {
        keepalive.current?.removeCache(key as string);
        const newTabs = tabs.filter((it) => it.key !== key);
        if (newTabs.length === 1) {
          newTabs[0].closable = false;
        }
        map.current[ALL_TAB_KEY] = newTabs;
        setTabs(newTabs);
        delete map.current[key as string];
        let switchLocation = to;
        if (to && typeof to === 'string') {
          switchLocation = {
            pathname: to,
            search: '',
          };
        }
        if (!switchLocation) {
          const switchTabIndex = itemIndex === 0 ? 0 : itemIndex - 1;
          switchLocation =
            switchTabIndex >= newTabs.length
              ? { pathname: '/' }
              : map.current[newTabs[switchTabIndex].key].location;
        }
        const targetKey = getCacheKey(switchLocation as any);
        if (map.current[targetKey]) {
          map.current[targetKey].reload = reload;
        }

        history.replace(switchLocation as any);
      }
    };
    return {
      key: key,
      tab: tabItem,
      mustReload: () => map.current[key].reload,
      refresh: () => {
        keepalive.current?.removeCache(key as string);
        setTimeout(() => {
          forceUpdate();
          console.log(keepalive.current?.getCaches());
        }, 2000);
        // history.replace(map.current[key].location as any);
      },
      reloadComplete: () => {
        map.current[key].reload = false;
      },
      closeTab: closeTab,
      close: (to?: TabTo, reload?: boolean) => {
        closeTab(key, to, reload);
      },
      setTabLabel: (label: string | any) => {
        const tabs = map.current[ALL_TAB_KEY] || [];
        const index = tabs.findIndex((item: any) => item.key === key);
        if (index !== -1) {
          tabs[index].label = label;

          const newTabs = [...tabs];
          map.current[ALL_TAB_KEY] = newTabs;
          setTabs(newTabs);
        }
      },
    };
  }, [routes.currentMenu, location]);

  const cacheKey = useMemo(() => {
    return getCacheKey(location);
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

      pageTabContext.closeTab(targetKey as string);
    },
    [pageTabContext],
  );

  const removeIcon = tabs.length == 1 ? false : <CloseOutlined />;

  return (
    <Flex vertical style={{ height: '100%', position: 'relative' }}>
      <Tabs
        className="page-tabs"
        hideAdd
        activeKey={cacheKey}
        type="editable-card"
        items={tabs}
        animated
        removeIcon={removeIcon}
        tabBarExtraContent={{
          left: (
            <Tooltip title={<FormattedMessage id="admin.ui.components.tab-pages.refresh-button" />}>
              <Button
                key="refresh"
                type="text"
                ghost={true}
                icon={<ReloadOutlined />}
                onClick={() => {
                  pageTabContext.refresh();
                }}
              />
            </Tooltip>
          ),
        }}
        onTabClick={(key) => {
          history.replace(map.current[key].location);
        }}
        onEdit={onEdit}
      />

      <KeepAlive
        activeName={cacheKey}
        cache={isCache}
        max={50}
        strategy={'LRU'}
        aliveRef={keepalive}
      >
        <PageTabContext.Provider value={pageTabContext}>{outlet}</PageTabContext.Provider>
      </KeepAlive>
    </Flex>
  );
};
