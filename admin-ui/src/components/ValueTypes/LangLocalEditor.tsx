import { EditOutlined, EyeOutlined } from '@ant-design/icons';
import { ProFieldFCRenderProps } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';
import { Button, Tag, Tooltip } from 'antd';
import { isFunction } from 'lodash';
import { FC, useEffect, useState } from 'react';
import LangLocalDialog from '../LangLocalDialog';


export const LangLocalEditor: FC<ProFieldFCRenderProps> = (props) => {
  const [value, setValue] = useState<any>(props.fieldProps.value);
  const [open, setOpen] = useState<boolean>(false);
  const [corpusInfo, setCorpusInfo] = useState<API.LangCorpusDTO>();
  const intl = useIntl();

  const { langs, corpus, ...restProps } = props.fieldProps;

  useEffect(() => {
    const values: Record<string, string|null> = {};
    langs?.forEach((lang: API.LangDTO) => {
        if (lang.code) {
      values[lang.code] = null;
        }
    });
    const originValue = props.readonly ? props.text : props.fieldProps.value;
    const actualValue = {...values, ...originValue}
    setValue(actualValue)
    if (props.readonly) {
        props.fieldProps.onChange(actualValue)
    }
  }, [props.fieldProps.value, langs, props.text]);

  return (
    <>
      <div>
        {langs.length > 0 ? (
          <Button
            key="edit"
            type="link"
            onClick={() => {
              let corpusInfo = undefined;
              if (corpus && isFunction(corpus)) {
                corpusInfo = corpus();
              }
              setCorpusInfo(corpusInfo);
              setOpen(true);
            }}
          >
            {props.readonly ?  <EyeOutlined /> : <EditOutlined />}
          </Button>
        ) : null}
        {langs.map((lang: API.LangDTO) => (
          <Tooltip key={lang.code} title={`${lang.name} - (${lang.localName})`}>
            <Tag color={(value||{})[lang.code || ''] ? 'success' : 'error'}>{lang.code}</Tag>
          </Tooltip>
        ))}
      </div>
      <LangLocalDialog
        langs={langs}
        onCancel={() => setOpen(false)}
        langCorpus={corpusInfo}
        title={intl.formatMessage({ id: 'admin.ui.pages.lang.label_lang_local_value' })}
        langLocal={value}
        // @ts-ignore
        readonly={props.readonly}
        onFinish={async (values) => {
          setValue(values);
          props.fieldProps.onChange(values);
          setOpen(false);
        }}
        width="600px"
        open={open}
      />
    </>
  );
};
