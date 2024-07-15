import { listOrganizations } from '@/services/swagger/organizationApi';
import { findById, generateTreeItems } from '@/utils/treeItemUtils';
import { FormattedMessage } from '@umijs/max';
import { useCallback, useMemo, useState } from 'react';
import DraggableModal, { DraggableModalProps } from '../DraggableModal';
import TreeSelector, { TreeItem, TreeSelectorProps } from '../TreeSelector';

interface OrganizationDialogProps extends DraggableModalProps {
  parentId?: number;
  currentId?: number;
  selectedIds?: number[];
  showDisabledOrganization: boolean;
  multiple?: boolean;
  onSelected?: (organization: any) => void;
  onBeforeSelected?: (
    organization: API.OrganizationDTO,
    selected: boolean,
    treeItem: TreeItem<API.OrganizationDTO>,
    selectedKeys: number[],
  ) => boolean;
  treeProps?: TreeSelectorProps<API.OrganizationDTO>;
}

const OrganizationDialog: React.FC<OrganizationDialogProps> = (props: OrganizationDialogProps) => {
  const [treeData, setTreeData] = useState([] as TreeItem<API.OrganizationDTO>[]);
  const [idMap, setIdMap] = useState({} as Record<number, API.OrganizationDTO>);
  const [selectedKeys, setSelectedKeys] = useState([] as number[]);

  const queryOrganization = useCallback(async () => {
    if (!props.open) {
      return;
    }
    const request: API.QueryOrganizationRequestDTO = {
      parentId: props.parentId,
      status: props.showDisabledOrganization == true ? undefined : 'NORMAL',
    };
    const response = await listOrganizations({ arg0: request });

    return response;
  }, [props.parentId, props.showDisabledOrganization, props.open]);

  useMemo(async () => {
    if (props.open) {
      const response = await queryOrganization();
      const map = {} as Record<number, API.OrganizationDTO>;
      (response || []).forEach((item) => {
        map[item.id || 0] = item;
      });
      setIdMap(map);
      setTreeData(
        generateTreeItems<API.OrganizationDTO>(
          response || [],
          (item) => item.id || 0,
          (item) => item.parentId,
          (item) => item.name || '',
          (a, b) => (a.displayOrder ?? 0) - (b.displayOrder ?? 0),
        ),
      );
    }
    return [];
  }, [props.open]);

  const initSelectedKeys = useMemo(() => {
    if (props.currentId) {
      setSelectedKeys([props.currentId]);
      return [props.currentId];
    }
    setSelectedKeys([...(props.selectedIds || [])]);
    return [...(props.selectedIds || [])];
  }, [props.currentId, props.open, props.selectedIds, treeData]);

  const onBeforeSelected = (id: number, selected: boolean) => {
    const treeItems = findById([id], treeData);
    if (!props.onBeforeSelected || !idMap[id]) {
      return true;
    }
    return props.onBeforeSelected(idMap[id], selected, treeItems[0], selectedKeys);
  };

  return (
    <DraggableModal
      maskClosable={false}
      {...props}
      onOk={(e) => {
        const treeItems = findById(selectedKeys, treeData);
        const organizations = selectedKeys
          .map((id) => {
            const organizaiton = idMap[id];
            if (organizaiton) {
              const treeItem = treeItems.find((item) => item.id == id);
              const pathNames: string[] =
                treeItem?.parentIds?.map((id) => idMap[id]?.name || '') || [];
              organizaiton.parentNames = pathNames;
            }

            return organizaiton;
          })
          .filter((item) => !!item);
        if (props.onSelected && organizations.length > 0) {
          props.onSelected(props.multiple ? organizations : organizations[0]);
        }
        if (props.onOk) {
          props.onOk(e);
        }
      }}
      okButtonProps={{ disabled: selectedKeys.length == 0 }}
      title={<FormattedMessage id="admin.ui.components.organization-dialog.title" />}
    >
      <TreeSelector<API.OrganizationDTO>
        selectedKeys={initSelectedKeys}
        treeData={treeData}
        treeWrapperStyle={{
          height: 250,
          marginTop: 10,
          overflow: 'auto'
        }}
        {...props.treeProps}
        onBeforeSelect={onBeforeSelected}
        onSelect={(selectedKeys) => setSelectedKeys(selectedKeys)}
      />
    </DraggableModal>
  );
};

export default OrganizationDialog;
