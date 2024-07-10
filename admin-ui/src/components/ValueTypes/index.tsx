import { ProColumnType, ProFormColumnsType, ProRenderFieldPropsType, ValueTypeWithFieldProps } from "@ant-design/pro-components";
import { ProOrganizationSelector } from "./OrganizationSelector";
import { Tag } from "antd";
import { DicSelect, DicText } from './DicSelect'
import { LangLocalEditor } from "./LangLocalEditor";




export const valueTypeMap: Record<string, ProRenderFieldPropsType> = {
    organizationSelector: {
        renderFormItem: (text, props) => {
            return <ProOrganizationSelector {...props} />
        },
        render: (text, props) => {
            return <ProOrganizationSelector {...props} readonly={true}/>
        }
    },
    tags : {
        render: (text: any[], props) => {
            return <>
                {text.map((item) => <Tag key={item[props.fieldProps.key]}>{item[props.fieldProps.title]}</Tag>)}
            </>
        }
    },
    dic: {
        renderFormItem: (text, props) => {
            return <DicSelect {...props}/>
        },
        render: (text, props, dom) => {
            
            return <DicText {...props}/>
        },
    },
    "lang-local": {
        renderFormItem: (text, props) => {
            return <LangLocalEditor {...props} readonly={false}/>
        },
        render: (text, props, dom) => {
            
            return <LangLocalEditor {...props} readonly={true}/>
        },
    }
}

export type ExValueType = ProFormColumnsType["valueType"] | 'organizationSelector' | 'tags' | 'dic' | 'lang-local'


export type ExProFormColumnsType<T> = Omit<ProFormColumnsType<T>, 'valueType'> & {
    valueType?:   ExValueType,
    resetValue?: any
};

export type ExProColumnsType<T> = Omit<ProColumnType<T>, 'valueType'> & {
    valueType?:   ExValueType,
};