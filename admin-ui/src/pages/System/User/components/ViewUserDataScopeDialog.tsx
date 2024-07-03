import DraggableModal, { DraggableModalProps } from '@/components/DraggableModal';
import { getUserDataScopes } from '@/services/swagger/dataScopeApi';
import { getOrganizaitonPathName } from '@/utils/bizUtils';
import { FormattedMessage, useAccess, useIntl } from '@umijs/max';
import { Divider, Flex, Table, TableColumnProps } from 'antd';
import { useMemo, useState } from 'react';

interface ViewUserDataScopeDialogProps extends DraggableModalProps {
  target: { id: number; name: string };
}

const ViewUserDataScopeDialog: React.FC<ViewUserDataScopeDialogProps> = (
  props: ViewUserDataScopeDialogProps,
) => {
  const [loading, setLoading] = useState<boolean>(false);
  const [dataScopes, setDataScopes] = useState<API.DataScopeDTO[]>([]);
  const intl = useIntl();
  const access = useAccess();

  const tilte = useMemo(() => {
    const targetName = intl.formatMessage({
      id: 'admin.ui.components.datascope-dialog.target-type-user',
    });
    return `${intl.formatMessage({
      id: 'admin.ui.components.datascope-dialog.title',
    })} - ${targetName} - ${props.target?.name}`;
  }, [props.target]);

  useMemo(async () => {
    if (props.open) {
    }
    return [];
  }, [props.open]);

  const dataScopeTableColumns: TableColumnProps<API.DataScopeDTO>[] = [
    {
      title: (
        <FormattedMessage id="admin.ui.components.datascope-dialog.grant-source" />
      ),
      dataIndex: 'targetType',
      key: 'targetType',
      width: 100,
      align: 'center',
      render: (text: string) => {
        switch (text) {
          case 'USER':
            return <FormattedMessage id="admin.ui.components.datascope-dialog.target-type-user" />;
          case 'ROLE':
            return <FormattedMessage id="admin.ui.components.datascope-dialog.target-type-role" />;
        }
      },
    },
    {
      title: (
        <FormattedMessage id="admin.ui.components.datascope-dialog.data-scope-table-scope-type" />
      ),
      dataIndex: 'scopeType',
      key: 'scopeType',
      width: 150,
      align: 'center',
      render: (text: string) => {
        switch (text) {
          case 'USER_ORGANIZATION':
            return (
              <FormattedMessage id="admin.ui.components.datascope-dialog.data-scope-type-user-organization" />
            );
          case 'ORGANIZATION':
            return (
              <FormattedMessage id="admin.ui.components.datascope-dialog.data-scope-type-organization" />
            );
        }
      },
    },
    {
      title: (
        <FormattedMessage id="admin.ui.components.datascope-dialog.data-scope-table-scope-name" />
      ),
      dataIndex: 'scopeName',
      render: (text: string, record: API.DataScopeDTO) => {
        if (record.scopeType === 'USER_ORGANIZATION') {
          return '-';
        }
        if (record.scopeType === 'ORGANIZATION') {
          return getOrganizaitonPathName(record.scopeInfo as API.OrganizationDTO, 3);
        }
        return '-';
      },
      key: 'scopeName',
    },
  ];

  return (
    <DraggableModal
      maskClosable={false}
      {...props}
      onOpen={async () => {
        setLoading(true);
        try {
          const result = await getUserDataScopes({ userId: props.target.id });
          setDataScopes(result.sort((a, b) => (a.targetType||'').localeCompare(b.targetType || '')));
        } finally {
          setLoading(false);
        }
      }}
      width={800}
      title={tilte}
      cancelButtonProps={{hidden: true}}
    >
      <Divider />
      <Flex vertical style={{ height: 270 }}>
        <Table
          style={{ flex: 1 }}
          columns={dataScopeTableColumns}
          pagination={false}
          loading={loading}
          scroll={{ y: 200 }}
          dataSource={dataScopes}
        />
      </Flex>
      <Divider />
    </DraggableModal>
  );
};

export default ViewUserDataScopeDialog;
