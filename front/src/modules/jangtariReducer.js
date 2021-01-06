import * as jangttakAPI from "../api/JangtariAPI";

export const getJangTtak = () => async (dispatch) => {
  dispatch({ type: "GET" });
  try {
    const result = await jangttakAPI.getInfo();
    dispatch({ type: "GET_SUCCESS", result });
  } catch (err) {
    dispatch({ type: "GET_ERROR,", error: err });
  }
};
export const setJangTtak = (newInfo) => async (dispatch) => {
  try {
    await jangttakAPI.setInfo(newInfo);
    dispatch({ type: "SET_INFO_SUCCESS", newInfo });
  } catch (err) {
    dispatch({ type: "SET_INFO_ERROR,", error: err });
  }
};

const initialState = {
  loading: false,
  info: null,
  error: null,
};

export default function jangtariReducer(state = initialState, action) {
  switch (action.type) {
    case "GET":
      return {
        ...state,
        loading: true,
        info: null,
        error: null,
      };
    case "GET_SUCCESS":
      return {
        ...state,
        loading: false,
        info: action.result,
        error: null,
      };
    case "GET_ERROR":
      return {
        ...state,
        loading: false,
        info: null,
        error: action.error,
      };
    case "SET_INFO_SUCCESS":
      return {
        ...state,
        loading: false,
        info: action.newInfo,
        error: null,
      };
    case "SET_INFO_ERROR":
      return {
        ...state,
        loading: false,
        info: null,
        error: action.error,
      };
    default:
      return state;
  }
}