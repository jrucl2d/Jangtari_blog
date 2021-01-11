import * as jangttakAPI from "../api/JangtariAPI";

export const getJangTtak = () => async (dispatch) => {
  dispatch({ type: "GET_JANGTAK" });
  try {
    const result = await jangttakAPI.getInfo();
    dispatch({ type: "GET_SUCCESS", result });
  } catch (err) {
    dispatch({ type: "GET_ERROR", error: err });
  }
};
export const setJangTtak = (newInfo, tmpImage) => async (dispatch) => {
  dispatch({ type: "SET_JANGTAK" });
  try {
    await jangttakAPI.setInfo(newInfo);
    const reduxUpdatedInfo = {
      ...newInfo,
      picture: tmpImage,
    };
    dispatch({ type: "SET_INFO_SUCCESS", newInfo: reduxUpdatedInfo });
  } catch (err) {
    dispatch({ type: "SET_INFO_ERROR", error: err });
  }
};
export const login = (info) => async (dispatch) => {
  dispatch({ type: "LOG_IN" });
  try {
    const result = await jangttakAPI.login(info);
    dispatch({
      type: "LOG_IN_SUCCESS",
      nickname: Object.keys(result.data.result)[0],
      role: Object.values(result.data.result)[0],
      username: info.username,
    });
  } catch (err) {
    dispatch({ type: "LOG_IN_ERROR", error: err });
  }
};
export const logout = () => async (dispatch) => {
  dispatch({ type: "LOG_OUT" });
  try {
    await jangttakAPI.logout();
    dispatch({ type: "LOG_OUT_SUCCESS" });
  } catch (err) {
    dispatch({ type: "LOG_OUT_ERROR" });
  }
};

export const join = (info) => async (dispatch) => {
  dispatch({ type: "JOIN" });
  try {
    await jangttakAPI.join(info);
    dispatch({
      type: "JOIN_SUCCESS",
      success: "join",
    });
  } catch (err) {
    dispatch({ type: "JOIN_ERROR", error: err });
  }
};

export const initSuccessOfJang = () => {
  return { type: "INIT_JANG" };
};

const initialState = {
  loading: false,
  info: null,
  success: null,
  error: null,
};

export default function jangtariReducer(state = initialState, action) {
  switch (action.type) {
    case "INIT_JANG":
      return {
        ...state,
        success: null,
      };
    case "GET_JANGTAK":
      return {
        ...state,
        loading: true,
        info: null,
        success: null,
        error: null,
      };
    case "SET_JANGTAK":
      return {
        ...state,
        success: null,
        error: null,
        loading: true,
      };
    case "GET_SUCCESS":
      return {
        ...state,
        loading: false,
        info: action.result,
        success: null,
        error: null,
      };
    case "LOG_OUT":
    case "JOIN":
    case "LOG_IN":
      return {
        ...state,
        success: null,
        error: null,
        loading: true,
      };
    case "LOG_OUT_SUCCESS":
      localStorage.clear();
      return {
        ...state,
        loading: false,
        success: "logout",
        error: null,
      };
    case "JOIN_SUCCESS":
      return {
        ...state,
        loading: false,
        error: null,
        success: action.success,
      };
    case "JOIN_ERROR":
      return {
        ...state,
        loading: false,
        error: action.error,
        success: null,
      };
    case "GET_ERROR":
      return {
        ...state,
        loading: false,
        info: null,
        success: null,
        error: action.error,
      };
    case "SET_INFO_SUCCESS":
      return {
        ...state,
        loading: false,
        info: action.newInfo,
        success: action.newInfo.nickname + "으로 변경되었습니다.",
        error: null,
      };
    case "SET_INFO_ERROR":
      return {
        ...state,
        loading: false,
        info: null,
        success: null,
        error: action.error,
      };
    case "LOG_OUT_ERROR":
      return {
        ...state,
        loading: false,
        info: null,
        success: null,
        error: "logoutError",
      };
    case "LOG_IN_SUCCESS":
      localStorage.setItem("nickname", action.nickname);
      localStorage.setItem("username", action.username);
      localStorage.setItem("role", action.role);
      return {
        ...state,
        loading: false,
        error: null,
        success: "login",
      };
    case "LOG_IN_ERROR":
      return {
        ...state,
        loading: false,
        success: null,
        error: "loginError",
      };
    default:
      return state;
  }
}
