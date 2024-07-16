import { QuestionCircleOutlined } from '@ant-design/icons';
import { getLocale, setLocale, useIntl } from '@umijs/max';
import { useState } from 'react';
import { updateUserParameters } from '@/services/swagger/userParameterApi';
import { applicationCode } from '@/config';
import HeaderDropdown from '../HeaderDropdown';
import { useMessageBox } from '@/utils/messageUtils';


const handleUpdateLanguage = useMessageBox<string, void>((lang) =>
  updateUserParameters({ application: applicationCode() }, {parameters: {language: lang}}),
);

export type SiderTheme = 'light' | 'dark';

interface LocalData {
  lang: string;
  label?: string;
  icon?: string;
  title?: string;
}

interface SelectLangProps {
  globalIconClassName?: string;
  postLocalesData?: (locales: LocalData[]) => LocalData[];
  onItemClick?: (params: any) => void;
  className?: string;
  reload?: boolean;
  icon?: React.ReactNode;
  style?: React.CSSProperties;
  langs?: API.LangDTO[];
}

const defaultLangUConfigMap = {
  'ar-EG': {
    icon: '🇪🇬',
  },
  'az-AZ': {
    icon: '🇦🇿',
  },
  'bg-BG': {
    icon: '🇧🇬',
  },
  'bn-BD': {
    icon: '🇧🇩',
  },
  'ca-ES': {
    icon: '🇨🇦',
  },
  'cs-CZ': {
    icon: '🇨🇿',
  },
  'da-DK': {
    icon: '🇩🇰',
  },
  'de-DE': {
    icon: '🇩🇪',
  },
  'el-GR': {
    icon: '🇬🇷',
  },
  'en-GB': {
    icon: '🇬🇧',
  },
  'en-US': {
    icon: '🇺🇸',
  },
  'es-ES': {
    icon: '🇪🇸',
  },
  'et-EE': {
    icon: '🇪🇪',
  },
  'fa-IR': {
    icon: '🇮🇷',
  },
  'fi-FI': {
    icon: '🇫🇮',
  },
  'fr-BE': {
    icon: '🇧🇪',
  },
  'fr-FR': {
    icon: '🇫🇷',
  },
  'ga-IE': {
    icon: '🇮🇪',
  },
  'he-IL': {
    icon: '🇮🇱',
  },
  'hi-IN': {
    icon: '🇮🇳',
  },
  'hr-HR': {
    icon: '🇭🇷',
  },
  'hu-HU': {
    icon: '🇭🇺',
  },
  'hy-AM': {
    icon: '🇦🇲',
  },
  'id-ID': {
    icon: '🇮🇩',
  },
  'it-IT': {
    icon: '🇮🇹',
  },
  'is-IS': {
    icon: '🇮🇸',
  },
  'ja-JP': {
    icon: '🇯🇵',
  },
  'ku-IQ': {
    icon: '🇮🇶',
  },
  'kn-IN': {
    icon: '🇮🇳',
  },
  'ko-KR': {
    icon: '🇰🇷',
  },
  'lv-LV': {
    icon: '🇱🇮',
  },
  'mk-MK': {
    icon: '🇲🇰',
  },
  'mn-MN': {
    icon: '🇲🇳',
  },
  'ms-MY': {
    icon: '🇲🇾',
  },
  'nb-NO': {
    icon: '🇳🇴',
  },
  'ne-NP': {
    icon: '🇳🇵',
  },
  'nl-BE': {
    icon: '🇧🇪',
  },
  'nl-NL': {
    icon: '🇳🇱',
  },
  'pl-PL': {
    icon: '🇵🇱',
  },
  'pt-BR': {
    icon: '🇧🇷',
  },
  'pt-PT': {
    icon: '🇵🇹',
  },
  'ro-RO': {
    icon: '🇷🇴',
  },
  'ru-RU': {
    icon: '🇷🇺',
  },
  'sk-SK': {
    icon: '🇸🇰',
  },
  'sr-RS': {
    icon: '🇸🇷',
  },
  'sl-SI': {
    icon: '🇸🇱',
  },
  'sv-SE': {
    icon: '🇸🇪',
  },
  'ta-IN': {
    icon: '🇮🇳',
  },
  'th-TH': {
    icon: '🇹🇭',
  },
  'tr-TR': {
    icon: '🇹🇷',
  },
  'uk-UA': {
    icon: '🇺🇰',
  },
  'vi-VN': {
    icon: '🇻🇳',
  },
  'zh-CN': {
    icon: '🇨🇳',
  },
  'zh-TW': {
    icon: '🇭🇰',
  },
};

export const SelectLang: React.FC<SelectLangProps> = (props) => {
  const {
    globalIconClassName,
    postLocalesData,
    onItemClick,
    icon,
    style,
    reload,
    langs,
    ...restProps
  } = props;

  const [selectedLang, setSelectedLang] = useState(() => getLocale());
  const intl = useIntl();

  const changeLang = async ({ key }: any): Promise<void> => {
    const result = await handleUpdateLanguage(key);
    if (result.success) {
      setLocale(key, reload);
      setSelectedLang(getLocale());
    }
  };

  const defaultLangUConfig = (langs || []).map((key) => ({
    lang: key.code,
    label: key.localName,
    // @ts-ignore
    icon: defaultLangUConfigMap[key.code]?.icon || '🌐',
  }));

  const allLangUIConfig = defaultLangUConfig;
  const handleClick = onItemClick ? (params: any) => onItemClick(params) : changeLang;

  const menuItemStyle = { minWidth: '160px' };
  const menuItemIconStyle = { marginRight: '8px' };

  const langMenu = {
    selectedKeys: [selectedLang],
    onClick: handleClick,
    items: allLangUIConfig.map((localeObj) => ({
      key: localeObj.lang,
      style: menuItemStyle,
      label: (
        <>
          <span role="img" aria-label={localeObj?.label || 'en-US'} style={menuItemIconStyle}>
            {localeObj?.icon || '🌐'}
          </span>
          {localeObj?.label || 'en-US'}
        </>
      ),
    })),
  };

  let dropdownProps = { menu: langMenu };

  const inlineStyle = {
    cursor: 'pointer',
    padding: '12px',
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: 18,
    verticalAlign: 'middle',
    ...style,
  };

  return (
    // @ts-ignore
    <HeaderDropdown {...dropdownProps} placement="bottomRight" {...restProps}>
      <span className={globalIconClassName} style={inlineStyle}>
        <i className="anticon">
          {icon ? (
            icon
          ) : (
            <svg
              viewBox="0 0 24 24"
              focusable="false"
              width="1em"
              height="1em"
              fill="currentColor"
              aria-hidden="true"
            >
              <path d="M0 0h24v24H0z" fill="none" />
              <path
                d="M12.87 15.07l-2.54-2.51.03-.03c1.74-1.94 2.98-4.17 3.71-6.53H17V4h-7V2H8v2H1v1.99h11.17C11.5 7.92 10.44 9.75 9 11.35 8.07 10.32 7.3 9.19 6.69 8h-2c.73 1.63 1.73 3.17 2.98 4.56l-5.09 5.02L4 19l5-5 3.11 3.11.76-2.04zM18.5 10h-2L12 22h2l1.12-3h4.75L21 22h2l-4.5-12zm-2.62 7l1.62-4.33L19.12 17h-3.24z "
                className="css-c4d79v"
              />
            </svg>
          )}
        </i>
      </span>
    </HeaderDropdown>
  );
};

export const Question = () => {
  return (
    <div
      style={{
        display: 'flex',
        height: 26,
      }}
      onClick={() => {
        window.open('https://pro.ant.design/docs/getting-started');
      }}
    >
      <QuestionCircleOutlined />
    </div>
  );
};
