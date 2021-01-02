import axios from "axios";

export const getInfo = async () => {
  const { data } = await axios.get("/informationOfJangTtari");
  const result = data.result;
  return result;
};
