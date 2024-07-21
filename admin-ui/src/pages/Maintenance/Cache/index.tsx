import { withAccessRender } from '@/access';
import LinkButton from '@/components/LinkButton';
import ProTablePage from '@/components/ProTablePage';
import { clearCaches, listCaches } from '@/services/swagger/maintenanceApi';
import { useMessageBox } from '@/utils/messageUtils';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer } from '@ant-design/pro-components';
import { FormattedMessage, useAccess, useIntl } from '@umijs/max';
import { Flex, Popconfirm } from 'antd';
import React, { useMemo, useRef } from 'react';

const handleClearCache = useMessageBox<API.clearCachesParams, void>((args) =>
  clearCaches({ ...args }),
);

const queryCaches = async (): Promise<{ data: API.CacheDTO[]; success: boolean; total: number }> => {
  const response = await listCaches();

  return {
    data: response ?? [],
    success: true,
    total: response.length,
  };
};

const CacheList: React.FC = () => {

  const actionRef = useRef<ActionType>();
  const intl = useIntl();
  const access = useAccess();

  const operationRender = useMemo(() => {
    return withAccessRender<API.CacheDTO>(
      {
        'admin:maintenance:cache:clear': (_, record) => (
          <Popconfirm
            key="clear"
            title={<FormattedMessage id="admin.ui.public.confirm-ok-button" />}
            description={<FormattedMessage id="admin.ui.pages.cache.clear-confirm-desc" />}
            onConfirm={async () => {
              await handleClearCache({ cacheName: record.cacheName || '' });
            }}
          >
            <LinkButton type="primary" key="delete">
              <FormattedMessage id="admin.ui.pages.cache.clear-button" />
            </LinkButton>
          </Popconfirm>
        ),
      },
      access,
    );
  }, [access]);

  const columns: ProColumns<API.CacheDTO>[] = [
    {
      title: <FormattedMessage id="admin.ui.pages.cache.label-display-text" />,
      dataIndex: 'displayText',
      valueType: 'text',
      sorter: true,
      hideInSearch: true,
      width: '12em',
      renderText: (_, record) => {
        return intl.formatMessage({id: record.displayCorpusCode, defaultMessage: record.displayText})
      }
    },
    {
      title: <FormattedMessage id="admin.ui.pages.cache.label-cache-name" />,
      dataIndex: 'cacheName',
      valueType: 'text',
      sorter: true,
      hideInSearch: true,
      width: '12em',
      copyable: true
    },
  ];

  if (operationRender.hasPermisions) {
    columns.push({
      title: <FormattedMessage id="admin.ui.public.option-button" defaultMessage="Operating" />,
      dataIndex: 'option',
      valueType: 'option',
      width: operationRender.columnWidth,
      fixed: 'right',
      align: 'center',
      render: (dom, record) => (
        <Flex justify="space-around">{operationRender.render(dom, record)}</Flex>
      ),
    });
  }

  return (
    <PageContainer pageHeaderRender={false}>
      <ProTablePage<API.CacheDTO>
        headerTitle={intl.formatMessage({
          id: 'admin.ui.pages.cache.cache-list',
        })}
        actionRef={actionRef}
        rowKey="id"
        toolBarRender={() => []}
        request={queryCaches}
        columns={columns as any}
        rowSelection={false}
        pagination={false}
        search={false}
      />
    </PageContainer>
  );
};

export default CacheList;
