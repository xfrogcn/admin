import DataScopeDialog from '@/components/DataScopeDialog';
import LinkButton from '@/components/LinkButton';
import ListBox from '@/components/ListBox';
import Panel from '@/components/Panel';
import { getDataScopes, grantDataScope } from '@/services/swagger/dataScopeApi';
import {
  listPermissionItems,
  listPermissionItemsFormPlatform,
} from '@/services/swagger/permissionItemApi';
import {
  createRole,
  deleteRole,
  enableRole,
  getRolePermissionItems,
  grantPermissionItems,
  listRoles,
  updateRole,
} from '@/services/swagger/roleApi';
import { stopEvent } from '@/utils/commonUtils';
import { confirmAction, useMessageBox } from '@/utils/messageUtils';
import { DataNodeEx, generateDataNodes, getAllFolderNodes } from '@/utils/treeItemUtils';
import {
  DiffOutlined,
  FolderAddOutlined,
  FolderOutlined,
  PlusOutlined,
  QuestionCircleOutlined,
  SaveOutlined,
} from '@ant-design/icons';
import { PageContainer } from '@ant-design/pro-components';
import { Access, FormattedMessage, useAccess, useIntl } from '@umijs/max';
import {
  Button,
  Checkbox,
  Col,
  Flex,
  Popconfirm,
  Row,
  Space,
  Tooltip,
  Tree,
  Typography,
  theme,
} from 'antd';
import React, { useCallback, useEffect, useState } from 'react';
import EditForm from './components/EditForm';

const handleAdd = useMessageBox<API.CreateRoleRequestDTO, number>(createRole);
const handleUpdate = useMessageBox<{ id: number; body: API.UpdateRoleRequestDTO }, void>((args) =>
  updateRole({ roleId: args.id }, args.body),
);
const handleGrantPermission = useMessageBox<{ roleId: number; body: number[] }, void>((args) =>
  grantPermissionItems({ roleId: args.roleId }, args.body),
);
const handleEnable = useMessageBox<{ id: number; enabled: boolean }, void>((args) =>
  enableRole({ roleId: args.id, enabled: args.enabled }),
);
const handleDelete = useMessageBox<number, void>((id) => deleteRole({ roleId: id }));
const handleGrantDataScope = useMessageBox<
  { roleId: number; scopeItems: API.DataScopeItem[] },
  void
>((args) =>
  grantDataScope({ targetId: args.roleId, targetType: 'ROLE', scopeItems: args.scopeItems }),
);

const unCheckedAllChildrenNodes = (
  checkedKeys: React.Key[],
  node: DataNodeEx<API.PermissionItemDTO>,
) => {
  node.children?.forEach((item) => {
    if (checkedKeys.includes(item.key)) {
      checkedKeys.splice(checkedKeys.indexOf(item.key), 1);
      unCheckedAllChildrenNodes(checkedKeys, item as any);
    }
  });
};

const checkedAllChildrenNodes = (
  checkedKeys: React.Key[],
  node: DataNodeEx<API.PermissionItemDTO>,
) => {
  node.children?.forEach((item) => {
    checkedKeys.push(item.key);
    checkedAllChildrenNodes(checkedKeys, item as any);
  });
};

const setNodeIcon = (node: DataNodeEx<API.PermissionItemDTO>) => {
  switch (node.data.type) {
    case 'F':
      node.icon = <FolderAddOutlined />;
      break;
    case 'M':
      node.icon = <FolderOutlined />;
      break;
    case 'B':
      node.icon = <DiffOutlined />;
      break;
  }
};

const RoleList: React.FC = () => {
  const [createModalOpen, handleCreateModalOpen] = useState<boolean>(false);
  const [newRole, setNewRole] = useState<API.CreateRoleRequestDTO>({
    name: '',
    memo: '',
    enabled: true,
  } as API.CreateRoleRequestDTO);

  const [editModalOpen, handleEditModalOpen] = useState<boolean>(false);
  const [editRole, setEditRole] = useState<API.RoleDTO | undefined>();
  const [roles, setRoles] = useState<API.RoleDTO[]>([]);
  const [currentRole, setCurrentRole] = useState<API.RoleDTO>();
  const [isChanged, setIsChanged] = useState<boolean>(false);
  const [checkedPermissionIds, setCheckedPermissionIds] = useState<number[]>([]);
  const [autoChecked, setAutoChecked] = useState<boolean>(true);
  const [autoCheckAllChildren, setAutoCheckAllChildren] = useState<boolean>(false);
  const [expandedKeys, setExpandedKeys] = useState<React.Key[]>([]);

  const [permissionTree, setPermissionTree] = useState<DataNodeEx<API.PermissionItemDTO>[]>([]);

  const [dataScopeDialogOpen, handleDataScopeDialogOpen] = useState<boolean>(false);
  const [roleDataScope, setRoleDataScopes] = useState<API.DataScopeDTO[]>([]);

  const intl = useIntl();
  const access = useAccess();
  const { token } = theme.useToken();

  const queryRoles = useCallback(() => {
    return listRoles().then((roles) => setRoles(roles));
  }, []);

  const queryPermissionItems = useCallback(() => {
    return access.hasPermission('admin:platform:grantplatformpermission')
      ? listPermissionItemsFormPlatform()
      : listPermissionItems();
  }, [access]);

  // 初始化
  useEffect(() => {
    queryRoles()
      .then(queryPermissionItems)
      .then((permissionItems) => {
        const nodes = generateDataNodes(
          permissionItems,
          (item) => item.id || 0,
          (item) => item.parentId,
          (item) => item.name ?? '',
          setNodeIcon,
        );
        setPermissionTree(nodes);
        setExpandedKeys(
          getAllFolderNodes(nodes, (item) => (item as DataNodeEx<API.PermissionItemDTO>).key),
        );
      });
  }, []);

  useEffect(() => {
    if (roles.length > 0 && !currentRole) {
      setCurrentRole(roles[0]);
    }
  }, [roles]);

  useEffect(() => {
    if (!currentRole) {
      return;
    }
    getRolePermissionItems({ roleId: currentRole.id || 0 }).then((items) => {
      setIsChanged(false);
      setCheckedPermissionIds(items.map((it) => it.id || 0));
    });
  }, [currentRole]);

  const onChecked = useCallback(
    (
      checkedKeys: { checked: React.Key[]; halfChecked: React.Key[] },
      info: {
        checked: boolean;
        checkedNodes: DataNodeEx<API.PermissionItemDTO>[];
        node: DataNodeEx<API.PermissionItemDTO>;
      },
    ) => {
      let keys: React.Key[] = checkedKeys.checked;
      if (info && info.node) {
        // 自动勾选上级
        if (info.checked) {
          const autoKeys = [];
          if (autoChecked) {
            autoKeys.push(...info.node.parentIds);
          }
          if (autoCheckAllChildren) {
            checkedAllChildrenNodes(autoKeys, info.node);
          }
          if (autoKeys.length > 0) {
            keys = [...new Set([...keys, ...autoKeys])];
          }
        } else if (autoChecked && !info.checked) {
          const parentIds = info.node.parentIds;
          let currentCheckedKeys = checkedKeys.checked;
          // 自动处理上级节点
          if (info.node.parentIds && info.node.parentIds.length > 0) {
            for (let i = parentIds.length - 1; i >= 0; i--) {
              const currentNode = info.checkedNodes.findLast((node) => node.key === parentIds[i]);
              // 当前选中，需要查看他的所有子节点是否有选中的，如果没有，则应该取消选择
              if (currentNode) {
                if (currentNode.children?.every((item) => !currentCheckedKeys.includes(item.key))) {
                  currentCheckedKeys.splice(currentCheckedKeys.indexOf(currentNode.key), 1);
                }
              }
            }
          }
          // 子节点自动取消选择
          unCheckedAllChildrenNodes(currentCheckedKeys, info.node);
          keys = currentCheckedKeys;
        }
      }
      setCheckedPermissionIds(keys as any);
      setIsChanged(true);
    },
    [autoChecked, autoCheckAllChildren],
  );

  const onRoleChanged = async (role: API.RoleDTO): Promise<boolean> => {
    if (role.id == currentRole?.id) {
      return Promise.resolve(false);
    }
    return new Promise<boolean>((resolve) =>
      confirmAction(
        isChanged,
        () => {
          setCurrentRole(role);
          resolve(true);
        },
        'admin.ui.pages.role.unsave-tip',
        () => {
          resolve(false);
        },
      ),
    );
  };

  return (
    <PageContainer pageHeaderRender={false}>
      <Row gutter={16} style={{ flex: 1 }}>
        <Col span={10}>
          <Flex style={{ height: '100%' }} vertical>
            <Flex justify="space-between">
              <Typography.Title level={5}>
                <FormattedMessage id="admin.ui.pages.role.table-title" />
              </Typography.Title>
              <Space>
                <Access accessible={access.hasPermission('admin:system:role:create')}>
                  <Button
                    type="primary"
                    key="primary"
                    onClick={() => {
                      setNewRole({ ...newRole });
                      handleCreateModalOpen(true);
                    }}
                  >
                    <PlusOutlined /> <FormattedMessage id="admin.ui.public.new-button" />
                  </Button>
                </Access>
                <Access accessible={access.hasPermission('admin:system:role:grantdatascope')}>
                  <Button
                    type="primary"
                    key="primary"
                    disabled={currentRole ? false : true}
                    onClick={async () => {
                      const dataScopes = await getDataScopes({
                        targetId: currentRole?.id || 0,
                        targetType: 'ROLE',
                      });
                      setRoleDataScopes(dataScopes);
                      handleDataScopeDialogOpen(true);
                    }}
                  >
                    <FormattedMessage id="admin.ui.pages.role.label-data-scope" />
                  </Button>
                </Access>
              </Space>
            </Flex>

            <ListBox<API.RoleDTO>
              spaceTop="sm"
              dataSource={roles}
              rowKey="id"
              selectedKey={currentRole?.id}
              renderItem={(item) => [
                <div>
                  {item.name}
                  {item.memo ? (
                    <Tooltip title={item.memo}>
                      <QuestionCircleOutlined style={{ marginLeft: 8 }} />
                    </Tooltip>
                  ) : undefined}
                </div>,
                <Flex style={{ width: 110, minWidth: 110 }} justify="space-around">
                  <Access accessible={access.hasPermission('admin:system:role:edit')}>
                    <LinkButton
                      type="primary"
                      key="edit"
                      onClick={(e) => {
                        stopEvent(e);
                        setEditRole({ ...item });
                        handleEditModalOpen(true);
                      }}
                    >
                      <FormattedMessage id="admin.ui.public.edit-button" />
                    </LinkButton>
                  </Access>
                  <Access accessible={access.hasPermission('admin:system:role:disable')}>
                    {item.enabled ? (
                      <Popconfirm
                        key="disable"
                        onPopupClick={stopEvent}
                        title={<FormattedMessage id="admin.ui.public.label-enabled-true" />}
                        description={
                          <FormattedMessage id="admin.ui.pages.role.disable-confirm-desc" />
                        }
                        onConfirm={async () => {
                          const result = await handleEnable({ id: item.id || 0, enabled: false });
                          if (result.success) {
                            await queryRoles();
                          }
                        }}
                      >
                        <LinkButton type="primary" onClick={stopEvent}>
                          <FormattedMessage id="admin.ui.public.label-enabled-false" />
                        </LinkButton>
                      </Popconfirm>
                    ) : (
                      <Popconfirm
                        key="enable"
                        onPopupClick={stopEvent}
                        title={<FormattedMessage id="admin.ui.public.label-enabled-true" />}
                        description={
                          <FormattedMessage id="admin.ui.pages.role.enable-confirm-desc" />
                        }
                        onConfirm={async () => {
                          const result = await handleEnable({ id: item.id || 0, enabled: true });
                          if (result.success) {
                            await queryRoles();
                          }
                        }}
                      >
                        <LinkButton type="primary" onClick={stopEvent}>
                          <FormattedMessage id="admin.ui.public.label-enabled-true" />
                        </LinkButton>
                      </Popconfirm>
                    )}
                  </Access>
                  <Access accessible={access.hasPermission('admin:system:role:delete')}>
                    <Popconfirm
                      key="delete"
                      onPopupClick={stopEvent}
                      title={<FormattedMessage id="admin.ui.public.delete-confirm-title" />}
                      description={
                        <FormattedMessage id="admin.ui.pages.role.delete-confirm-desc" />
                      }
                      onConfirm={async () => {
                        const result = await handleDelete(item.id || 0);
                        if (result.success) {
                          await queryRoles();
                        }
                      }}
                    >
                      <LinkButton type="primary" onClick={stopEvent}>
                        <FormattedMessage id="admin.ui.public.delete-button" />
                      </LinkButton>
                    </Popconfirm>
                  </Access>
                </Flex>,
              ]}
              onSelect={onRoleChanged}
            />
          </Flex>
        </Col>
        <Col span={14}>
          <Flex style={{ height: '100%' }} vertical>
            <Flex justify="space-between">
              <Typography.Title level={5}>
                <FormattedMessage id="admin.ui.pages.role.permission-title" />
              </Typography.Title>
              <Access accessible={access.hasPermission('admin:system:role:grant')}>
                <div>
                  <Checkbox
                    style={{ marginRight: 10 }}
                    value={autoChecked}
                    defaultChecked={autoChecked}
                    onChange={(e) => setAutoChecked(e.target.checked)}
                  >
                    <FormattedMessage id="admin.ui.pages.role.label-auto-check" />
                  </Checkbox>
                  <Checkbox
                    style={{ marginRight: 10 }}
                    value={autoCheckAllChildren}
                    defaultChecked={autoCheckAllChildren}
                    onChange={(e) => setAutoCheckAllChildren(e.target.checked)}
                  >
                    <FormattedMessage id="admin.ui.pages.role.label-auto-check-all" />
                  </Checkbox>
                  <Button
                    disabled={!currentRole}
                    type="primary"
                    onClick={async () => {
                      const result = await handleGrantPermission({
                        roleId: currentRole?.id || 0,
                        body: checkedPermissionIds,
                      });
                      if (result.success) {
                        setIsChanged(false);
                      }
                    }}
                  >
                    <SaveOutlined />
                    <FormattedMessage id="admin.ui.pages.role.permission-save-button" />
                  </Button>
                </div>
              </Access>
            </Flex>
            <Panel panelStyle={{ padding: 10 }} spaceTop="sm">
              <Tree
                showLine={true}
                showIcon={true}
                treeData={permissionTree}
                checkable={!!currentRole}
                selectable={false}
                checkStrictly={true}
                onCheck={onChecked as any}
                expandedKeys={expandedKeys}
                onExpand={(keys) => setExpandedKeys(keys)}
                checkedKeys={{ checked: checkedPermissionIds, halfChecked: [] }}
              />
            </Panel>
          </Flex>
          {
            <EditForm
              mode="create"
              role={newRole}
              onCancel={() => handleCreateModalOpen(false)}
              title={intl.formatMessage({ id: 'admin.ui.pages.role.create-new-title' })}
              onFinish={async (values) => {
                const result = await handleAdd(values as API.CreateRoleRequestDTO);
                if (result.success) {
                  handleCreateModalOpen(false);
                  await queryRoles();
                }
              }}
              width="600px"
              open={createModalOpen}
            ></EditForm>
          }
          {
            <EditForm
              mode="edit"
              role={editRole}
              onCancel={() => handleEditModalOpen(false)}
              title={intl.formatMessage({ id: 'admin.ui.pages.role.edit-title' })}
              onFinish={async (values) => {
                const result = await handleUpdate({
                  id: editRole?.id ?? 0,
                  body: values as API.UpdateUserRequestDTO,
                });
                if (result.success) {
                  handleEditModalOpen(false);
                  await queryRoles();
                }
              }}
              width="600px"
              open={editModalOpen}
            ></EditForm>
          }
        </Col>
      </Row>
      <DataScopeDialog
        open={dataScopeDialogOpen}
        dataScope={roleDataScope}
        onCancel={() => handleDataScopeDialogOpen(false)}
        targetType="ROLE"
        target={currentRole as any}
        onSave={async (dataScope) => {
          const scopeItems: API.DataScopeItem[] = dataScope.map(
            (it) => ({ scopeType: it.scopeType, scopeId: it.scopeId } as API.DataScopeItem),
          );
          const result = await handleGrantDataScope({
            roleId: currentRole?.id ?? 0,
            scopeItems: scopeItems,
          });
          if (result.success) {
            handleDataScopeDialogOpen(false);
          }
        }}
      />
    </PageContainer>
  );
};

export default RoleList;
