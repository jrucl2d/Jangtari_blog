export const LogIn = (info) => {
  return { type: "LOG_IN", info };
};
export const LogOut = () => {
  return { type: "LOG_OUT" };
};
const initialState = {
  username: null,
  nickname: null,
  role: null,
};

export default function memberReducer(state = initialState, action) {
  switch (action.type) {
    case "LOG_IN":
      return {
        username: action.info.username,
        nickname: action.info.nickname,
        role: action.info.role,
      };
    case "LOG_OUT":
      return {
        username: null,
        nickname: null,
        role: null,
      };
    default:
      return state;
  }
}
