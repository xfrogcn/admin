import { ProColumnType } from "@ant-design/pro-components";

export const stopEvent = (e: React.MouseEvent) => {
    e.stopPropagation();
    e.preventDefault();
}

export function isNil(value: any): value is null | undefined {
    return value === null || value === undefined
}

export function isRegExp(value: any): value is RegExp {
    return Object.prototype.toString.call(value) === "[object RegExp]"
}

export function isArr(value: any): value is Array<any> {
    return Array.isArray(value)
}

export function isFn(value: any): value is Function {
    return typeof value === "function"
}

export const defaultWidth = {
    dateTime: '14em',
    userName: '8em',
    code: '16em',
    status2: '8em',
}

export const columnProperties: Record<string, ProColumnType<any>> = {
    userName: {
        width: defaultWidth.userName,
        align: 'center'
    },
    dateTime: {
        hideInSearch: true,
        width: defaultWidth.dateTime,
        align: 'center',
        valueType: 'dateTime',
    },
    center: {
        align: 'center',
        ellipsis: true
    }
}

