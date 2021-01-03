import axios from "axios";

export const getAll = async () => {
  const { data } = await axios.get("/getAllCategories");
  const result = data.result;
  return result;
};

export const update = async (newInfo) => {
  await axios.put("/admin/updateCategory", newInfo);
};
