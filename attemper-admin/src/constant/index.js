export const load = (src) => {
  return import(src)
    .then((module) => {
      return module
    })
}
