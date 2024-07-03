import { DraggableModalProps } from '@/components/DraggableModal';
import FormDraggableModal from '@/components/DraggableModal/FormDraggableModal';
import { ExProFormColumnsType } from '@/components/ValueTypes';
import { patterns } from '@/utils/bizUtils';
import { ProFormProps } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';

interface EditPermissionItemDialogProps extends DraggableModalProps {
  mode: 'edit' | 'create';
  permissionItem?: API.PermissionItemDTO;
  onFinish?: ProFormProps['onFinish'];
}

const EditPermissionItemDialog: React.FC<EditPermissionItemDialogProps> = (
  props: EditPermissionItemDialogProps,
) => {
  const intl = useIntl();

  const columns: ExProFormColumnsType<API.PermissionItemDTO>[] = [
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.permissionitem.label-parent-name' }),
      dataIndex: 'parentName',
      valueType: 'text',
      readonly: true,
    },
    {
      dataIndex: 'parentId',
      valueType: 'text',
      readonly: true,
      formItemProps: {
        hidden: true,
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.permissionitem.label-type' }),
      dataIndex: 'type',
      valueType: 'radio',
      valueEnum: {
        F: {
          text: intl.formatMessage({ id: 'admin.ui.pages.permissionitem.label-type-folder' }),
        },
        M: {
          text: intl.formatMessage({ id: 'admin.ui.pages.permissionitem.label-type-menu' }),
        },
        B: {
          text: intl.formatMessage({ id: 'admin.ui.pages.permissionitem.label-type-button' }),
        },
      },
      formItemProps: {
        rules: [],
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.permissionitem.label-name' }),
      dataIndex: 'name',
      valueType: 'text',
      fieldProps: {
        maxLength: 64,
      },
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            message: intl.formatMessage({ id: 'admin.ui.pages.permissionitem.rule-require-name' }),
          },
        ],
      },
    },
  ];

  if (props.mode === 'create') {
    columns.push(
      {
        title: intl.formatMessage({ id: 'admin.ui.pages.permissionitem.label-code' }),
        dataIndex: 'code',
        valueType: 'text',
        tooltip: intl.formatMessage({ id: 'admin.ui.pages.permissionitem.tooltip-code' }),
        fieldProps: {
          maxLength: 128,
        },
        formItemProps: {
          rules: [
            {
              required: true,
              type: 'string',
              message: intl.formatMessage({
                id: 'admin.ui.pages.permissionitem.rule-require-code',
              }),
            },
            {
              pattern: patterns.permissionCode,
              type: 'string',
              message: intl.formatMessage({ id: 'admin.ui.pages.permissionitem.tooltip-code' }),
            },
          ],
        },
      },
      {
        title: intl.formatMessage({ id: 'admin.ui.pages.permissionitem.label-platform' }),
        dataIndex: 'platform',
        valueType: 'radio',
        valueEnum: {
          true: {
            text: intl.formatMessage({ id: 'admin.ui.public.label-yes' }),
          },
          false: {
            text: intl.formatMessage({ id: 'admin.ui.public.label-no' }),
          },
        },
        formItemProps: {
          rules: [],
        },
      },
    );
  }

  return (
    <FormDraggableModal<API.PermissionItemDTO>
      {...props}
      onInit={(form) => {
        if (props.permissionItem) {
          form.resetFields();
          form.setFieldsValue({
            ...props.permissionItem,
            platform: props.permissionItem.platform + ''
          });
        }
      }}
      columns={columns}
    />
  );
};

export default EditPermissionItemDialog;
