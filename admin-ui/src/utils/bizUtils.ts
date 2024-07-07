import moment from "moment";

export const patterns = {
    telphone: /^(\+\d{1,3}[-\s]?)?(\(\d{1,4}\)|\d{1,4}[-\s]?)[\d\s-]{5,15}$/,
    email: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
    userName: /^[a-zA-Z0-9_]+$/,
    password: /^.{6,}$/,
    permissionCode: /^[a-zA-Z0-9_\-:\.]+$/,
    noneWhiteSpace: /^\S+$/,
    url: /^(https?|ftp):\/\/[^\s/$.?#].[^\s]*$/i,
}

export function getOrganizaitonPathName(organization: API.OrganizationDTO | undefined, maxLevel: number = 0): string {
  if (!organization) {
    return '';
  }
  if (!organization.parentNames || organization.parentNames.length === 0) {
    return organization.name || '';
  }
  let pn = organization.parentNames;
  if (maxLevel > 0 && maxLevel < pn.length) {
    pn = pn.slice(pn.length - maxLevel);
  } 
  return pn.join(' / ') + ' / ' + organization.name;
}

export function stringToBoolean(value: string | undefined): boolean | undefined {
  if (value === undefined) {
    return undefined;
  }
  if (value === 'false') {
    return false;
  }
  if (value === 'true') {
    return true;
  }
  return undefined;
}

type commonQuery = {
  pageNum?: number;
  pageSize?: number;
  sortItems?: API.SortItem[];
};

export function convertCommonQueryParams(
  params: {
    pageSize?: number;
    current?: number;
  },
  sort: any,
): commonQuery {
  const result: commonQuery = {
    pageNum: params.current,
    pageSize: params.pageSize,
    sortItems: [],
  };

  if (sort) {
    for (const key in sort) {
      if (sort.hasOwnProperty(key)) {
        const value = sort[key];
        result.sortItems?.push({
          field: key,
          order: value === 'descend' ? 'DESC' : 'ASC',
        });
      }
    }
  }

  return result;
}

const dateTimeFormatString = "YYYY-MM-DD HH:mm:ss"

export function convertDateRange(dateRange: [string?, string?] | null | undefined) : API.DateTimeRange | undefined {
    if (!dateRange) {
        return undefined;
    }

    const result: API.DateTimeRange = {

    }

    if (dateRange[0]) {
        result.begin = moment(dateRange[0]).utcOffset(0).format(dateTimeFormatString);
    }

    if (dateRange[1]) {
        result.end = moment(dateRange[1]).add(1,'d').subtract(1,'s').utcOffset(0).format(dateTimeFormatString);
    }

    return result;

}