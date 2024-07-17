import { applicationCode } from '@/config';
import { updateUserParameters } from '@/services/swagger/userParameterApi';
import { useMessageBox } from '@/utils/messageUtils';
import { SettingOutlined } from '@ant-design/icons';
import { ProSettings } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';
import { Popover } from 'antd';
import { useMergedState } from 'rc-util';
import { BlockCheckbox } from './BlockCheckbox';
import { ThemeColor } from './ThemeColor';
import { useStyle } from './style';

const colorList = [
  { key: 'techBlue', color: '#1677FF' },
  { key: 'daybreak', color: '#1890ff' },
  { key: 'dust', color: '#F5222D' },
  { key: 'volcano', color: '#FA541C' },
  { key: 'sunset', color: '#FAAD14' },
  { key: 'cyan', color: '#13C2C2' },
  { key: 'green', color: '#52C41A' },
  { key: 'geekblue', color: '#2F54EB' },
  { key: 'purple', color: '#722ED1' },
];

const themeConfig: Record<string, string> = {};
colorList.forEach(({ key, color }) => {
  themeConfig[key] = color;
});

function genStringToTheme(val?: string): string {
  return val && themeConfig[val] ? themeConfig[val] : val || '';
}

const handleUpdateUserPameter = useMessageBox<{ parName: string; value: string }, void>((args) =>
  updateUserParameters(
    {
      application: applicationCode(),
    },
    {
      parameters: {
        [args.parName]: args.value,
      },
    },
  ),
);

type BodyProps = {
  title: string;
  prefixCls: string;
  children?: React.ReactNode;
  hashId: string;
};

const Body: React.FC<BodyProps> = ({ children, hashId, prefixCls, title }) => (
  <div style={{ marginBlockEnd: 12 }}>
    <h3 className={`${prefixCls}-body-title ${hashId}`.trim()}>{title}</h3>
    {children}
  </div>
);

const PopoverContent = (props: {
  hashId: string;
  baseClassName: string;
  navTheme?: string;
  onChange: (key: string, value: string) => Promise<void>;
  colorPrimary?: string;
}) => {
  const { hashId, baseClassName, navTheme, onChange, colorPrimary } = props;

  const intl = useIntl();

  return (
    <>
      <Body
        title={intl.formatMessage({
          id: 'admin.ui.public.theme',
        })}
        hashId={hashId}
        prefixCls={baseClassName}
      >
        <BlockCheckbox
          hashId={hashId}
          prefixCls={baseClassName}
          list={[
            {
              key: 'light',
              title: intl.formatMessage({
                id: 'admin.ui.public.theme-light',
              }),
            },
            {
              key: 'realDark',
              title: intl.formatMessage({
                id: 'admin.ui.public.theme-dark',
              }),
            },
          ]}
          value={navTheme!}
          configType="theme"
          key="navTheme"
          onChange={async (value) => await onChange('navTheme', value)}
        />
      </Body>

      <Body
        hashId={hashId}
        title={intl.formatMessage({
          id: 'admin.ui.public.theme-color',
        })}
        prefixCls={baseClassName}
      >
        <ThemeColor
          hashId={hashId}
          prefixCls={baseClassName}
          colorList={colorList}
          value={genStringToTheme(colorPrimary)!}
          formatMessage={intl.formatMessage}
          onChange={async (color) => await onChange('colorPrimary', color)}
        />
      </Body>
    </>
  );
};

type MergerSettingsType<T> = Partial<T> & {
  colorPrimary?: string;
  colorWeak?: boolean;
};

const defaultSettings: ProSettings = {
  navTheme: 'light',
  layout: 'side',
  contentWidth: 'Fluid',
  fixedHeader: false,
  fixSiderbar: true,
  iconfontUrl: '',
  colorPrimary: '#1677FF',
  splitMenus: false,
};

const SettingDropdown: React.FC<{
  settings?: MergerSettingsType<ProSettings>;
  onSettingChange?: (settings: any) => void;
}> = (props) => {
  const baseClassName = `ant-setting-dropdown-pop`;
  const { wrapSSR, hashId } = useStyle(baseClassName);

  const [settingState, setSettingState] = useMergedState<Partial<ProSettings>>(
    () => ({ ...defaultSettings }),
    {
      value: props.settings,
      onChange: props.onSettingChange,
    },
  );

  const { navTheme, colorPrimary, siderMenuType, layout, colorWeak } = settingState || {};

  const changeSetting = (key: string, value: string | boolean) => {
    const nextState = {} as Record<string, any> as any;
    nextState[key] = value;

    delete nextState.menu;
    delete nextState.title;
    delete nextState.iconfontUrl;
    delete nextState.logo;
    delete nextState.pwa;

    setSettingState({ ...settingState, ...nextState });
  };

  const content = wrapSSR(
    <PopoverContent
      hashId={hashId}
      baseClassName={baseClassName}
      navTheme={navTheme}
      colorPrimary={colorPrimary}
      onChange={async (key, value) => {
        const result = await handleUpdateUserPameter({ parName: key, value: value });
        if (result.success) {
          changeSetting(key, value);
        }
      }}
    />,
  );

  return (
    <Popover content={content} overlayStyle={{ maxWidth: 200 }}>
      <div
        style={{
          display: 'flex',
          height: 26,
        }}
        onClick={() => {}}
      >
        <SettingOutlined />
      </div>
    </Popover>
  );
};
export default SettingDropdown;
