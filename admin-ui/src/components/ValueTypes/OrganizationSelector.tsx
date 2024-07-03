import { getOrganization } from '@/services/swagger/organizationApi';
import { getOrganizaitonPathName } from '@/utils/bizUtils';
import { ProFieldFCRenderProps } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';
import { Input } from 'antd';
import { FC, useMemo, useState } from 'react';
import OrganizationDialog from '../OrganizationDialog';

const { Search } = Input;

export const ProOrganizationSelector: FC<ProFieldFCRenderProps> = (props) => {
  const [organizationModalOpen, setOrganizationModalOpen] = useState(false);
  const [organizationName, setOrganizationName] = useState('');
  const intl = useIntl();

  useMemo(async () => {
    if (props.fieldProps.value) {
      console.log(props.fieldProps)
      const orgInfo = await getOrganization({ organizationId: props.fieldProps.value });
      setOrganizationName(getOrganizaitonPathName(orgInfo));
    }
  }, [props.fieldProps.value]);

  return (
    <>
      <Search
        placeholder={intl.formatMessage({ id: 'admin.ui.components.valuetypes.os-placeholder' })}
        allowClear={false}
        enterButton={intl.formatMessage({ id: 'admin.ui.components.valuetypes.os-button' })}
        {...props.fieldProps}
        readOnly={true}
        disabled={props.readonly}
        value={organizationName}
        title={organizationName}
        onSearch={() => setOrganizationModalOpen(true)}
      />
      <OrganizationDialog
        currentId={props.fieldProps.value}
        open={organizationModalOpen}
        parentId={props.fieldProps.rootOrganizationId}
        onCancel={() => setOrganizationModalOpen(false)}
        onSelected={(selectedOrganization: API.OrganizationDTO) => {
          setOrganizationModalOpen(false);
          console.log(props);
          if (props.fieldProps.onChange) {
            props.fieldProps.onChange(selectedOrganization.id);
          }
        }}
        showDisabledOrganization={false}
      />
    </>
  );
};
