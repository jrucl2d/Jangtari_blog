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
  const formData = new FormData();
  formData.append(
    "post",
    JSON.stringify({
      categoryId: newInfo.categoryId,
      title: newInfo.title,
      post: newInfo.post,
      template: newInfo.template,
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
export const updatePost = async (newInfo, pictures) => {
  const formData = new FormData();
  formData.append(
    "post",
    JSON.stringify({
      id: newInfo.id,
      title: newInfo.title,
      post: newInfo.post,
      template: newInfo.template,
      hashtags: newInfo.hashtags,
    })
  );
  if (pictures.length > 0) {
    pictures.forEach((picture) => {
      formData.append("images", picture);
    });
    await axios.put("/admin/post", formData, {
      headers: {
        "Content-Type": "multipart/form-data; charset=UTF-8",
      },
    });
  } else {
    await axios.put("/admin/post/nimg", formData, {
      headers: {
        "Content-Type": "multipart/form-data; charset=UTF-8",
      },
    });
  }
};

export const deletePost = async (id) => {
  await axios.delete(`/admin/post/${id}`);
};
