import { DraggableModalProps } from '@/components/DraggableModal';
import FormDraggableModal from '@/components/DraggableModal/FormDraggableModal';
import { ExProFormColumnsType } from '@/components/ValueTypes';
import { ProFormProps } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';
import { patterns } from '@/utils/bizUtils';

interface EditOrganizationDialogProps extends DraggableModalProps {
  mode: 'edit' | 'create';
  organization?: API.OrganizationDTO;
  onFinish?: ProFormProps['onFinish'];
}

const EditOrganizationDialog: React.FC<EditOrganizationDialogProps> = (
  props: EditOrganizationDialogProps,
) => {
  const intl = useIntl();

  const columns: ExProFormColumnsType<API.OrganizationDTO>[] = [
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.organization.label-parent-id' }),
      dataIndex: 'parentId',
      valueType: 'organizationSelector',
      readonly: props.mode === 'edit',
      formItemProps: {
        rules:
          props.mode === 'create'
            ? [
                {
                  required: true,
                  message: intl.formatMessage({
                    id: 'admin.ui.pages.organization.rule-require-parent-id',
                  }),
                },
              ]
            : [],
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.organization.label-name' }),
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
            message: intl.formatMessage({ id: 'admin.ui.pages.organization.rule-require-name' }),
          },
        ],
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.organization.label-display-order' }),
      dataIndex: 'displayOrder',
      valueType: 'digit',
      resetValue: 1,
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'integer',
            message: intl.formatMessage({
              id: 'admin.ui.pages.organization.rule-require-display-order',
            }),
          },
        ],
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.organization.label-telephone' }),
      dataIndex: 'telephone',
      valueType: 'text',
      fieldProps: {
        maxLength: 32,
      },
      formItemProps: {
        rules: [
          {
            required: false,
            pattern: patterns.telphone,
            type: 'string',
            message: intl.formatMessage({
              id: 'admin.ui.pages.organization.rule-pattern-telephone',
            }),
          },
        ],
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.organization.label-principal' }),
      dataIndex: 'principal',
      valueType: 'text',
      fieldProps: {
        maxLength: 32,
      },
      formItemProps: {
        rules: [],
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.organization.label-status' }),
      dataIndex: 'status',
      valueType: 'radio',
      resetValue: 'NORMAL',
      valueEnum: {
        NORMAL: {
          text: intl.formatMessage({ id: 'admin.ui.pages.organization.label-status-normal' }),
        },
        DISABLED: {
          text: intl.formatMessage({ id: 'admin.ui.pages.organization.label-status-disabled' }),
        },
      },
      formItemProps: {
        rules: [],
      },
    },
  ];

  return (
    <FormDraggableModal<API.OrganizationDTO>
      {...props}
      onInit={(form) => {
        if (props.organization) {
          form.resetFields();
          form.setFieldsValue({
            ...props.organization,
            parentId: props.organization.parentId ? props.organization.parentId + '' : undefined,
          });
        }
      }}
      columns={columns}
    />
  );
};

export default EditOrganizationDialog;
