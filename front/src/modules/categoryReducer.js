import * as categoryAPI from "../api/CategoryAPI";

export const getAllCategories = () => async (dispatch) => {
  dispatch({ type: "GET_ALL" });
  try {
    const result = await categoryAPI.getAll();
    dispatch({ type: "GET_ALL_SUCCESS", result });
  } catch (err) {
    dispatch({ type: "GET_ALL_ERROR", error: err });
  }
};
export const addCategory = (newInfo, tmpImage) => async (dispatch) => {
  try {
    const newId = await categoryAPI.add(newInfo);
    const reduxNewInfo = {
      ...newInfo,
      id: newId,
      picture: tmpImage,
    };
    dispatch({ type: "ADD_INFO_SUCCESS", newInfo: reduxNewInfo });
  } catch (err) {
    dispatch({ type: "ADD_INFO_ERROR", error: err });
  }
};
export const updateCategory = (newInfo, tmpImage) => async (dispatch) => {
  try {
    await categoryAPI.update(newInfo);
    const reduxUpdatedInfo = {
      ...newInfo,
      picture: tmpImage,
    };
    dispatch({ type: "UPDATE_INFO_SUCCESS", newInfo: reduxUpdatedInfo });
  } catch (err) {
    dispatch({ type: "UPDATE_INFO_ERROR", error: err });
  }
};
export const deleteCategory = (id) => async (dispatch) => {
  try {
    await categoryAPI.deleteCate(id);
    dispatch({ type: "DELETE_INFO_SUCCESS", id });
  } catch (err) {
    dispatch({ type: "DELETE_INFO_ERROR", error: err });
  }
};

const initialState = {
  loading: false,
  categories: null,
  success: null,
  error: null,
};

export default function categoryReducer(state = initialState, action) {
  switch (action.type) {
    case "GET_ALL":
      return {
        ...state,
        loading: true,
        categories: null,
        success: null,
        error: null,
      };
    case "GET_ALL_SUCCESS":
      return {
        ...state,
        loading: false,
        categories: action.result,
        success: null,
        error: null,
      };
    case "ADD_INFO_SUCCESS":
      return {
        ...state,
        loading: false,
        categories: [...state.categories, action.newInfo],
        success: "성공적으로 추가되었습니다.",
        error: null,
      };
    case "UPDATE_INFO_SUCCESS":
      return {
        ...state,
        loading: false,
        categories: state.categories.map((v) =>
          v.id === action.newInfo.id ? action.newInfo : v
        ),
        success: "성공적으로 변경되었습니다.",
        error: null,
      };
    case "DELETE_INFO_SUCCESS":
      return {
        ...state,
        loading: false,
        categories: state.categories.filter((v) => v.id !== action.id),
        success: "성공적으로 삭제되었습니다.",
        error: null,
      };
    case "ADD_INFO_ERROR":
    case "UPDATE_INFO_ERROR":
    case "DELETE_INFO_ERROR":
      return {
        ...state,
        error: action.error,
        success: null,
      };
    case "GET_ALL_ERROR":
      return {
        ...state,
        loading: false,
        categories: null,
        success: null,
        error: action.error,
      };
    default:
      return state;
  }
}
