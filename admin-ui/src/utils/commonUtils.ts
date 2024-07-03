export const stopEvent = (e: React.MouseEvent) => {
    e.stopPropagation();
    e.preventDefault();
}