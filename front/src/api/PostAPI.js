import axios from "axios";

export const getAll = async (id) => {
  const { data } = await axios.get(`/category/${id}/posts`);
  const result = data.result;
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
