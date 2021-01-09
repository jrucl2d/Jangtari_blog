import axios from "axios";

export const getInfo = async () => {
  const { data } = await axios.get("/jangtari");
  const result = data.result;
  return result;
};

export const setInfo = async (newInfo) => {
  const formData = new FormData();
  formData.append(
    "jangtari",
    JSON.stringify({
      nickname: newInfo.nickname,
      introduce: newInfo.introduce,
    })
  );
  if (newInfo.picture) {
    formData.append("image", newInfo.picture);
    await axios.put("/admin/jangtari", formData, {
      headers: {
        "Content-Type": "multipart/form-data; charset=UTF-8",
      },
    });
  } else {
    await axios.put("/admin/jangtari/nimg", formData, {
      headers: {
        "Content-Type": "multipart/form-data; charset=UTF-8",
      },
    });
  }
};

export const login = async (info) => {
  return await axios.post("/login", {
    username: info.username,
    password: info.password,
  });
};
export const join = async (info) => {
  await axios.post("/join", info);
};

export const logout = async () => {
  await axios.get("/signout");
};
