import { withAccessRender } from '@/access';
import LinkButton from '@/components/LinkButton';
import ProTablePage from '@/components/ProTablePage';
import { ExProColumnsType } from '@/components/ValueTypes';
import { listOpLogs } from '@/services/swagger/opLogApi';
import { convertCommonQueryParams, convertDateRange } from '@/utils/bizUtils';
import { columnProperties, defaultWidth } from '@/utils/commonUtils';
import type { ActionType } from '@ant-design/pro-components';
import { PageContainer, ProDescriptions } from '@ant-design/pro-components';
import { FormattedMessage, useAccess, useIntl } from '@umijs/max';
import { Drawer, Flex } from 'antd';
import React, { useMemo, useRef, useState } from 'react';

const queryOpLogs = async (
  params: API.OpLogDTO & {
    pageSize?: number | undefined;
    current?: number | undefined;
    keyword?: string,
    timeRange: [string, string]
  },
  sorter: any,
): Promise<{ data: API.OpLogDTO[]; success: boolean; total: number }> => {
  const response = await listOpLogs({
    keyword: params.keyword,
    bizActions: params.bizAction,
    bizTypes: params.bizType,
    tags: params.tag,
    timeRange: convertDateRange(params.timeRange as any),
    ...convertCommonQueryParams(params, sorter),
  } as API.QueryDicRequestDTO);

  return {
    data: response.data ?? [],
    success: true,
    total: response.total ?? 0,
  };
};

const OpLogList: React.FC = () => {
  const [detailOpen, handleDetailOpen] = useState<boolean>(false);
  const [currentLog, setCurrentLog] = useState<API.OpLogDTO>();
  const actionRef = useRef<ActionType>();
  const intl = useIntl();
  const access = useAccess();

  const operationRender = useMemo(() => {
    return withAccessRender<API.OpLogDTO>(
      {
        'admin:system:oplog:view': (_, record) => (
          <LinkButton
            type="primary"
            key="items"
            onClick={() => {
              setCurrentLog(record);
              handleDetailOpen(true);
            }}
          >
            <FormattedMessage id="admin.ui.pages.oplog.button-view" />
          </LinkButton>
        ),
      },
      access,
    );
  }, [access]);

  const columns: ExProColumnsType<API.OpLogDTO>[] = [
    {
      title: <FormattedMessage id="admin.ui.pages.oplog.label-operatorName" />,
      dataIndex: 'operatorName',
      valueType: 'text',
      sorter: true,
      hideInSearch: true,
      ...columnProperties.userName,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.oplog.operate-time" />,
      dataIndex: 'createdTime',
      sorter: true,
      hideInForm: true,
      ...columnProperties.dateTime,
    },
    {
      title: <FormattedMessage id="admin.ui.public.keyword" />,
      dataIndex: 'keyword',
      sorter: false,
      valueType: 'text',
      hideInTable: true,
      hideInSearch: false,
      hideInDescriptions: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.oplog.operate-time" />,
      dataIndex: 'timeRange',
      sorter: false,
      hideInTable: true,
      hideInSearch: false,
      hideInDescriptions: true,
      align: 'center',
      valueType: 'dateRange',
    },

    {
      title: <FormattedMessage id="admin.ui.pages.oplog.label-tag" />,
      dataIndex: 'tag',
      valueType: 'dic',
      sorter: true,
      hideInSearch: false,
      ...columnProperties.userName,
      fieldProps: {
        dictype: 'oplog-type',
        mode: 'tags'
      },
    },
    {
      title: <FormattedMessage id="admin.ui.pages.oplog.label-biz-type" />,
      dataIndex: 'bizType',
      valueType: 'dic',
      sorter: true,
      hideInSearch: false,
      ...columnProperties.userName,
      fieldProps: {
        dictype: 'oplog-biz-type',
        mode: 'tags'
      },
    },
    {
      title: <FormattedMessage id="admin.ui.pages.oplog.label-biz-action" />,
      dataIndex: 'bizAction',
      valueType: 'dic',
      sorter: true,
      hideInSearch: false,
      ...columnProperties.userName,
      fieldProps: {
        dictype: 'oplog-biz-action',
        mode: 'tags'
      },
    },
    {
      title: <FormattedMessage id="admin.ui.pages.oplog.label-biz-code" />,
      dataIndex: 'bizCode',
      valueType: 'text',
      sorter: true,
      hideInSearch: true,
      ...columnProperties.center,
      width: defaultWidth.code,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.oplog.label-success" />,
      dataIndex: 'success',
      valueType: 'text',
      valueEnum: {
        true: {
          text: intl.formatMessage({ id: 'admin.ui.pages.oplog.label-success-true' }),
          status: 'Success',
        },
        false: {
          text: intl.formatMessage({ id: 'admin.ui.pages.oplog.label-success-false' }),
          status: 'Error',
        },
      },
      hideInSearch: true,
      hideInDescriptions: true,
      ...columnProperties.center,
      width: defaultWidth.status2,
    },
    {
        title: <FormattedMessage id="admin.ui.pages.oplog.label-success" />,
        dataIndex: 'success',
        valueType: 'text',
        hideInTable: true,
        hideInSearch: true,
        hideInDescriptions: false,
        render: (text) => {
            return text ? <FormattedMessage id="admin.ui.pages.oplog.label-success-true" /> : <FormattedMessage id="admin.ui.pages.oplog.label-success-false" />
        }
      },
      {
        title: <FormattedMessage id="admin.ui.pages.oplog.label-requestId" />,
        dataIndex: 'requestId',
        valueType: 'text',
        hideInTable: true,
        hideInSearch: true,
        hideInDescriptions: false,
      },
      {
        title: <FormattedMessage id="admin.ui.pages.oplog.label-message" />,
        dataIndex: 'message',
        valueType: 'text',
        hideInTable: true,
        hideInSearch: true,
        hideInDescriptions: false,
      },
      {
        title: <FormattedMessage id="admin.ui.pages.oplog.label-extra" />,
        dataIndex: 'extra',
        valueType: 'text',
        hideInTable: true,
        hideInSearch: true,
        hideInDescriptions: false,
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
      <ProTablePage<API.OpLogDTO, any>
        headerTitle={intl.formatMessage({
          id: 'admin.ui.pages.oplog.logs-list',
        })}
        actionRef={actionRef}
        rowKey="id"
        toolBarRender={() => []}
        request={queryOpLogs}
        columns={columns as any}
        rowSelection={false}
      />
      <Drawer title={<FormattedMessage id="admin.ui.pages.oplog.detail-title" />} open={detailOpen} size='large' maskClosable={true} destroyOnClose={true} onClose={() => handleDetailOpen(false)}>
        <ProDescriptions column={2} columns={columns as any} dataSource={currentLog} />
      </Drawer>
    </PageContainer>
  );
};

export default OpLogList;
