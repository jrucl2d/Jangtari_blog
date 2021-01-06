import * as jangttakAPI from "../api/JangtariAPI";

export const getJangTtak = () => async (dispatch) => {
  dispatch({ type: "GET" });
  try {
    const result = await jangttakAPI.getInfo();
    dispatch({ type: "GET_SUCCESS", result });
  } catch (err) {
    dispatch({ type: "GET_ERROR", error: err });
  }
};
export const setJangTtak = (newInfo) => async (dispatch) => {
  try {
    await jangttakAPI.setInfo(newInfo);
    dispatch({ type: "SET_INFO_SUCCESS", newInfo });
  } catch (err) {
    dispatch({ type: "SET_INFO_ERROR", error: err });
  }
};

const initialState = {
  loading: false,
  info: null,
  success: null,
  error: null,
};

export default function jangtariReducer(state = initialState, action) {
  switch (action.type) {
    case "GET":
      return {
        ...state,
        loading: true,
        info: null,
        success: null,
        error: null,
      };
    case "GET_SUCCESS":
      return {
        ...state,
        loading: false,
        info: action.result,
        success: null,
        error: null,
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
        success: "성공적으로 변경되었습니다.",
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
    default:
      return state;
  }
}
