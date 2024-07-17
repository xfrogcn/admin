import { getOrganizaitonPathName } from '@/utils/bizUtils';
import { PlusOutlined } from '@ant-design/icons';
import { Access, FormattedMessage, useAccess, useIntl } from '@umijs/max';
import {
  Divider,
  Dropdown,
  Flex,
  MenuProps,
  Table,
  TableColumnProps,
  Tabs,
  Typography,
  message,
} from 'antd';
import { useMemo, useState } from 'react';
import DraggableModal, { DraggableModalProps } from '../DraggableModal';
import LinkButton from '../LinkButton';
import OrganizationDialog from '../OrganizationDialog';

interface DataScopeDialogProps extends DraggableModalProps {
  targetType: 'USER' | 'ROLE';
  target: { id: number; name: string };
  dataScope?: API.DataScopeDTO[];
  onSave?: (dataScopes: API.DataScopeDTO[]) => Promise<void>;
}

const DataScopeDialog: React.FC<DataScopeDialogProps> = (props: DataScopeDialogProps) => {
  const [organizationDataScopes, setOrganizationDataScopes] = useState<API.DataScopeDTO[]>([]);
  const [organizationDialogOpen, setOrganizationDialogOpen] = useState<boolean>(false);
  const intl = useIntl();
  const access = useAccess();

  const addOrganizationScopeMenuItems: MenuProps['items'] = [
    {
      key: 'user-organization',
      label: <FormattedMessage id="admin.ui.components.datascope-dialog.user-organization" />,
    },
    {
      key: 'customer-organization',
      label: <FormattedMessage id="admin.ui.components.datascope-dialog.customer-organization" />,
    },
  ];

  const addDataScopeItems = (items: API.DataScopeDTO[]) => {
    if (!items || items.length == 0) {
      return;
    }
    const checkedItems: API.DataScopeDTO[] = [];
    items.forEach((item) => {
      const old = organizationDataScopes.find((i) => i.scopeId == item.scopeId);
      if (!old) {
        checkedItems.push(item);
      } else {
        message.warning(
          intl.formatMessage({ id: 'admin.ui.components.datascope-dialog.repeat-data-scope-item' }),
        );
      }
    });

    setOrganizationDataScopes([...organizationDataScopes, ...checkedItems]);
  };

  const addOrganizationScopeMenu: MenuProps = {
    items: addOrganizationScopeMenuItems,
    onClick: (e) => {
      if (e.key == 'user-organization') {
        const item: API.DataScopeDTO = {
          scopeId: 0,
          scopeType: 'USER_ORGANIZATION',
        };
        addDataScopeItems([item]);
      } else if (e.key == 'customer-organization') {
        setOrganizationDialogOpen(true);
      }
    },
  };

  const tilte = useMemo(() => {
    const targetName =
      props.targetType == 'ROLE'
        ? intl.formatMessage({ id: 'admin.ui.components.datascope-dialog.target-type-role' })
        : intl.formatMessage({ id: 'admin.ui.components.datascope-dialog.target-type-user' });
    return `${intl.formatMessage({
      id: 'admin.ui.components.datascope-dialog.title',
    })} - ${targetName} - ${props.target?.name}`;
  }, [props.targetType, props.target]);

  useMemo(async () => {
    if (props.open) {
    }
    return [];
  }, [props.open]);

  const dataScopeTableColumns: TableColumnProps<API.DataScopeDTO>[] = [
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

  if (access.hasPermission('admin:system:role:grantdatascope')) {
    dataScopeTableColumns.push({
      title: intl.formatMessage({
        id: 'admin.ui.components.datascope-dialog.data-scope-table-scope-operation',
      }),
      dataIndex: 'operation',
      key: 'operation',
      width: '6em',
      render: (value, record, index) => {
        return (
          <LinkButton
            type="primary"
            key="delete"
            onClick={() => {
              setOrganizationDataScopes(organizationDataScopes.filter((item) => item !== record));
            }}
          >
            <FormattedMessage id="admin.ui.public.delete-button" />
          </LinkButton>
        );
      },
    });
  }

  return (
    <>
      <DraggableModal
        maskClosable={false}
        {...props}
        onOk={async (e) => {
          if (props.onSave) {
            await props.onSave(organizationDataScopes);
          }
        }}
        onOpen={() => {
          if (props.dataScope) {
            setOrganizationDataScopes(
              props.dataScope.filter(
                (it) => it.scopeType === 'ORGANIZATION' || it.scopeType === 'USER_ORGANIZATION',
              ),
            );
          }
        }}
        width={800}
        title={tilte}
      >
        <Divider />
        <Tabs tabPosition="left" style={{ height: 300, marginTop: 20 }}>
          <Tabs.TabPane
            key="organization"
            tab={<FormattedMessage id="admin.ui.components.datascope-dialog.tab-organization" />}
          >
            <Flex vertical>
              <Flex justify="space-between" style={{ marginBottom: 16 }}>
                <Typography.Title level={5}>
                  <FormattedMessage id="admin.ui.components.datascope-dialog.tab-organization" />
                </Typography.Title>
                <Access accessible={access.hasPermission('admin:system:role:grantdatascope')}>
                  <Dropdown.Button
                    type="primary"
                    style={{ width: 'auto' }}
                    menu={addOrganizationScopeMenu}
                    icon={<PlusOutlined />}
                  >
                    <FormattedMessage id="admin.ui.components.datascope-dialog.button-add-organization" />
                  </Dropdown.Button>
                </Access>
              </Flex>
              <Table
                style={{ flex: 1 }}
                columns={dataScopeTableColumns}
                dataSource={organizationDataScopes}
                pagination={false}
                scroll={{ y: 200 }}
              />
            </Flex>
          </Tabs.TabPane>
        </Tabs>
        <Divider />
      </DraggableModal>
      <OrganizationDialog
        open={organizationDialogOpen}
        treeProps={{
          checkable: true,
          checkStrictly: true,
          defaultExpandAll: true,
        }}
        onBeforeSelected={(organizaiton, selected, treeItem, selectedKeys) => {
          if (!selected) {
            return true;
          }

          console.log(treeItem, selectedKeys);
          if (treeItem.parentIds.filter((it) => selectedKeys.includes(it)).length > 0) {
            message.warning(
              intl.formatMessage({
                id: 'admin.ui.components.datascope-dialog.include-chidren-data-scope-item',
              }),
            );
            // 只做提示，不做硬性限制
            return true;
          }

          return true;
        }}
        onCancel={() => setOrganizationDialogOpen(false)}
        multiple={true}
        onSelected={(organizations: API.OrganizationDTO[]) => {
          addDataScopeItems(
            organizations.map((organization) => ({
              scopeId: organization.id,
              scopeType: 'ORGANIZATION',
              scopeInfo: organization,
            })),
          );

          setOrganizationDialogOpen(false);
        }}
        showDisabledOrganization={false}
      />
    </>
  );
};

export default DataScopeDialog;
