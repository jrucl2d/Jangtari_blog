import axios from "axios";

export const getAll = async (theUrl) => {
  const { data } = await axios.get(theUrl);
  const result = data.result;
  return result;
};
export const getOne = async (id) => {
  const { data } = await axios.get(`/post/${id}`);
  const result = data.result;
  return result;
};
export const addPost = async (newInfo, pictures) => {
  console.log(pictures);
  const formData = new FormData();
  formData.append(
    "post",
    JSON.stringify({
      categoryId: newInfo.categoryId,
      title: newInfo.title,
      post: newInfo.post,
      hashtags: newInfo.hashtags,
    })
  );
  if (pictures.length > 0) {
    pictures.forEach((picture) => {
      formData.append("images", picture);
    });
    await axios.post("/admin/post", formData, {
      headers: {
        "Content-Type": "multipart/form-data; charset=UTF-8",
      },
    });
  } else {
    await axios.post("/admin/post/nimg", formData, {
      headers: {
        "Content-Type": "multipart/form-data; charset=UTF-8",
      },
    });
  }
};
// export const update = async (newInfo) => {
//   await axios.put("/admin/updateCategory", newInfo);
// };

// export const deleteCate = async (id) => {
//   await axios.delete(`/admin/deleteCategory/${id}`);
// };
