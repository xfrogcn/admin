
const dicCache: Record<string, API.DicDTO> = {

}

export function getDicFromCache(type: string): API.DicDTO {
    return dicCache[type];
}

export function setDicCache(type: string, dic: API.DicDTO) {
    dicCache[type] = dic;
}

export function removeDicCache(type: string) {
    delete dicCache[type];
}