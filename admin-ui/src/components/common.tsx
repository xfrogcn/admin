import { unit } from '@ant-design/cssinjs';
import { CSSProperties } from 'react';

export type SpaceSize = 'xxs' | 'xs' | 'sm' | 'nm' | 'md' | 'lg' | 'xl' | 'xxl' | number | string;

export interface MarginSpaceProps {
    spaceLeft?: SpaceSize;
    spaceTop?: SpaceSize;
    spaceRight?: SpaceSize;
    spaceBottom?: SpaceSize;
  }

export function getBorderCss(token: any) {
    return `${unit(token.lineWidth)} solid ${token.colorBorderSecondary}`;
}

export function getScrollbarColorCss(token: any) {
    return `${token.colorTextPlaceholder} ${token.colorSplit}`;
}

export function convertSpace(size: SpaceSize, token: any) {
    switch (size) {
      case 'xxs':
        return token.marginXXS;
      case 'xs':
        return token.marginXS;
      case 'sm':
        return token.marginSM;
      case 'nm':
        return token.margin;
      case 'md':
        return token.marginMD;
      case 'lg':
        return token.marginLG;
      case 'xl':
        return token.marginXL;
      case 'xxl':
        return token.marginXXL;
      default:
        return size;
    }
  };

  export function convertMarginSpaceProps(props: MarginSpaceProps, token: any) {
    const css: CSSProperties = {};

    if (props.spaceTop !== undefined) {
        css.marginTop = convertSpace(props.spaceTop, token);
      }
      if (props.spaceRight !== undefined) {
        css.marginRight = convertSpace(props.spaceRight, token);
      }
      if (props.spaceBottom !== undefined) {
        css.marginBottom = convertSpace(props.spaceBottom, token);
      }
      if (props.spaceLeft !== undefined) {
        css.marginLeft = convertSpace(props.spaceLeft, token);
      }

      return css;
  }