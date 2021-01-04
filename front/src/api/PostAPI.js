import axios from "axios";

export const getAll = async (id, page, type, keyword) => {
  let theUrl = `/category/${id}/posts`;
  if (page) {
    theUrl += "?page=" + page;
    if (type) {
      theUrl += "&type=" + type + "&keyword=" + keyword;
    }
  }
  if (type) {
    theUrl += "?type=" + type + "&keyword=" + keyword;
  }
  console.log(theUrl);
  const { data } = await axios.get(theUrl);
  const result = data.result;
  //   console.log(result);
  return result;
};
// export const add = async (newInfo) => {
//   const { data } = await axios.post("/admin/addCategory", newInfo);
//   const result = data.result;
//   return result;
// };
// export const update = async (newInfo) => {
//   await axios.put("/admin/updateCategory", newInfo);
// };

// export const deleteCate = async (id) => {
//   await axios.delete(`/admin/deleteCategory/${id}`);
// };
