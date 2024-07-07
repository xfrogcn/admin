import { ProColumnType, ProFormColumnsType, ProRenderFieldPropsType, ValueTypeWithFieldProps } from "@ant-design/pro-components";
import { ProOrganizationSelector } from "./OrganizationSelector";
import { Tag } from "antd";
import { DicSelect } from './DicSelect'




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
            console.log(text, props)
            return <div>HELLO WORLD</div>
        },
    }
}

export type ExValueType = ProFormColumnsType["valueType"] | 'organizationSelector' | 'tags' | 'dic'


export type ExProFormColumnsType<T> = Omit<ProFormColumnsType<T>, 'valueType'> & {
    valueType?:   ExValueType,
    resetValue?: any
};

export type ExProColumnsType<T> = Omit<ProColumnType<T>, 'valueType'> & {
    valueType?:   ExValueType,
};