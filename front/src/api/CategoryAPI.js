import axios from "axios";

export const getAll = async () => {
  const { data } = await axios.get("/categories");
  const result = data.result;
  return result;
};
export const add = async (newInfo) => {
  const formData = new FormData();
  formData.append("category", newInfo.name);

  if (newInfo.picture) {
    formData.append("image", newInfo.picture);
    const { data } = await axios.post("/admin/category", formData, {
      headers: {
        "Content-Type": "multipart/form-data; charset=UTF-8",
      },
    });
    return data.result;
  }
  const { data } = await axios.post("/admin/category/nimg", formData, {
    headers: {
      "Content-Type": "multipart/form-data; charset=UTF-8",
    },
  });
  return data.result;
};
export const update = async (newInfo) => {
  const formData = new FormData();
  formData.append(
    "category",
    JSON.stringify({
      id: newInfo.id,
      name: newInfo.name,
    })
  );
  if (newInfo.picture) {
    formData.append("image", newInfo.picture);
    await axios.put("/admin/category", formData, {
      headers: {
        "Content-Type": "multipart/form-data; charset=UTF-8",
      },
    });
  } else {
    await axios.put("/admin/category/nimg", formData, {
      headers: {
        "Content-Type": "multipart/form-data; charset=UTF-8",
      },
    });
  }
};

export const deleteCate = async (id) => {
  await axios.delete(`/admin/category/${id}`);
};
