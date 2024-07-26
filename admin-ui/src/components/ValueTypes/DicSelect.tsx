import { getDicFromCache, setDicCache } from '@/services/DicCache';
import { getDicByTypes } from '@/services/swagger/dicApi';
import { ProFieldFCRenderProps } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';
import { Select } from 'antd';
import { FC, useEffect, useRef, useState } from 'react';
import { FormattedMessage, IntlShape } from 'react-intl';

const defaultLabelGetter = (item: API.DicItemDTO, intl: IntlShape) => {
  return item.langCode ? intl.formatMessage({ id: item.langCode }) : item.displayText;
};
const defaultValueGetter = (item: API.DicItemDTO) => {
  return item.value;
};

export const DicText: FC<ProFieldFCRenderProps> = (props) => {
  const [text, setText] = useState<string>(props.text as any);
  const intl = useIntl();

  const { dictype, labelGetter, valueGetter, readonly, autoSelectFirst, ...restProps } =
    props.fieldProps;

  useEffect(() => {
    const fetchDic = async () => {
      if (!dictype) {
        return [];
      }

      let dic: API.DicDTO = getDicFromCache(dictype);
      if (!dic) {
        const dics = await getDicByTypes([dictype]);
        if (dics.length > 0) {
          dic = dics[0];
          setDicCache(dictype, dic);
        }
      }
      const dicItems = dic?.dicItems || [];

      const labelProvider = labelGetter || defaultLabelGetter;
      const valueProvider = valueGetter || defaultValueGetter;

      const item = dicItems.find((item) => {
        return props.text === valueProvider(item);
      });

      if (item) {
        setText(labelProvider(item, intl));
      }
    };

    fetchDic();
  }, [dictype, intl, props.text]);

  return <span>{text}</span>;
};

export const DicSelect: FC<ProFieldFCRenderProps> = (props) => {
  const [loading, setLoading] = useState<boolean>(false);
  const [options, setOptions] = useState<{ label: string; value: any }[]>([]);
  const [value, setValue] = useState<any>(props.fieldProps.value);
  const dicItemMap = useRef<Record<string, API.DicItemDTO>>({});
  const intl = useIntl();

  const { dictype, labelGetter, valueGetter, readonly, autoSelectFirst, filter, ...restProps } =
    props.fieldProps;

  useEffect(() => {
    const fetchDic = async () => {
      if (!dictype) {
        return [];
      }
      setLoading(true);

      let dic: API.DicDTO = getDicFromCache(dictype);
      if (!dic) {
        const dics = await getDicByTypes([dictype]);
        if (dics.length > 0) {
          dic = dics[0];
          setDicCache(dictype, dic);
        }
      }
      let dicItems = dic?.dicItems || [];
      dicItems = dicItems.filter(it=> it.enabled);
      if (filter) {
        dicItems = filter(dicItems);
      }

      setLoading(false);
      const labelProvider = labelGetter || defaultLabelGetter;
      const valueProvider = valueGetter || defaultValueGetter;
      const items = dicItems.map((item: API.DicItemDTO) => {
        const optionItem = {
          label: labelProvider(item, intl),
          value: valueProvider(item),
        };
        dicItemMap.current[optionItem.value] = item;
        return optionItem;
      });

      setOptions(items as any);
      if (!readonly && !value && autoSelectFirst && items.length > 0) {
        const selectValue = props.fieldProps.mode ? [items[0].value] : items[0].value;
        setValue(selectValue);
        props.fieldProps.onChange(selectValue);
        if (props.fieldProps.onSelect) {
          props.fieldProps.onSelect(dicItemMap.current[items[0].value]);
        }
      } else if (value) {
        if (props.fieldProps.mode) {
          // 多选模式
          if (Array.isArray(value)) {
            const validValues = items
              .filter((it) => value.includes(it.value))
              .map((it) => it.value);
            if (validValues.length != value.length) {
              setValue(validValues);
              props.fieldProps.onChange(validValues);
            }
          }
        } else {
          if (items.findLastIndex((it) => it.value === value) === -1) {
            setValue(undefined);
            props.fieldProps.onChange(undefined);
          }
        }
      }
    };

    fetchDic();
  }, [dictype, intl, filter]);

  const onSelect = (value: any) => {
    if (props.fieldProps.onSelect) {
      const dicItem = dicItemMap.current[value];
      props.fieldProps.onSelect(dicItem);
    }
  };

  return (
    <Select
      {...restProps}
      placeholder={<FormattedMessage id="admin.ui.public.default-select-placeholder" />}
      loading={loading}
      options={options}
      disabled={readonly}
      value={value}
      onChange={(value) => {
        setValue(value);
        props.fieldProps.onChange(value);
      }}
      onSelect={onSelect}
    />
  );
};
