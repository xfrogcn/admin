import type { EditableProTableProps, ParamsType, ProTableProps } from '@ant-design/pro-components';
import { EditableProTable, ProTable } from '@ant-design/pro-components';
import React, { CSSProperties, useCallback, useEffect, useRef, useState } from 'react';
import useResizeObserver from '@react-hook/resize-observer'

const useSize = (target: React.MutableRefObject<any>): {width: number, height: number} => {
  const [size, setSize] = React.useState<any>()

  React.useLayoutEffect(() => {
    setSize(target.current.getBoundingClientRect())
  }, [target])

  useResizeObserver(target, (entry) => setSize(entry.contentRect))
  return size
}

export default function EditableProTablePage<DataType extends Record<string, any>, Params extends ParamsType = ParamsType, ValueType = "text">
    (props: EditableProTableProps<DataType, Params, ValueType> & {width?: string | number | boolean, style?: CSSProperties}) : JSX.Element {

  const [scrollY, setScrollY] = useState<number>(0);
  const wrapperRef = useRef<HTMLDivElement>();
  const timeRef = useRef<any>(0)

  const size = useSize(wrapperRef)

  const onSizeChange = useCallback(() => {
    if (!wrapperRef || !wrapperRef.current) {
      return;
    }
    const tables = wrapperRef.current.getElementsByClassName('ant-table');
    if (tables && tables.length > 0) {
        const table = tables[0]
        let headHeight = 0;
        const headers = table.getElementsByClassName('ant-table-thead')
        if (headers && headers.length > 0) {
            headHeight = headers[0].getBoundingClientRect().height;
        }
        const th = table.getBoundingClientRect().height;
        setScrollY(th - headHeight - 10)
    }
  }, [timeRef, size])

  useEffect(() => {
    if (timeRef.current) {
      clearTimeout(timeRef.current)
      timeRef.current = 0
    }
    timeRef.current = setTimeout(() => {
      onSizeChange();
      if (timeRef.current) {
        clearTimeout(timeRef.current)
        timeRef.current = 0
      }
    }, 50)
    
  }, [size])

  const {style, className, ...restProps} = props;

  return (
        <div className={`table-page-wrapper ${className}`} style={style} ref={wrapperRef as any}>
          <EditableProTable<DataType, ParamsType>
            {...restProps as any}
            options={{fullScreen: true, ...props.options}}
            scroll = {{y: scrollY, x: props.width}}
          />
        </div>
  );
};
