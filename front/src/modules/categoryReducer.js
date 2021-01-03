import * as categoryAPI from "../api/CategoryAPI";

export const getAllCategories = () => async (dispatch) => {
  dispatch({ type: "GET_ALL" });
  try {
    const result = await categoryAPI.getAll();
    dispatch({ type: "GET_ALL_SUCCESS", result });
  } catch (err) {
    dispatch({ type: "GET_ALL_ERROR,", error: err });
  }
};
export const updateCategory = (newInfo) => async (dispatch) => {
  try {
    await categoryAPI.update(newInfo);
    dispatch({ type: "UPDATE_INFO_SUCCESS", newInfo });
  } catch (err) {
    dispatch({ type: "UPDATE_INFO_ERROR,", error: err });
  }
};
export const deleteCategory = (id) => async (dispatch) => {
  try {
    await categoryAPI.deleteCate(id);
    dispatch({ type: "DELETE_INFO_SUCCESS", id });
  } catch (err) {
    dispatch({ type: "DELETE_INFO_ERROR,", error: err });
  }
};

const initialState = {
  loading: false,
  categories: null,
  error: null,
};

export default function categoryReducer(state = initialState, action) {
  switch (action.type) {
    case "GET_ALL":
      return {
        ...state,
        loading: true,
        categories: null,
        error: null,
      };
    case "GET_ALL_SUCCESS":
      return {
        ...state,
        loading: false,
        categories: action.result,
        error: null,
      };
    case "UPDATE_INFO_SUCCESS":
      return {
        ...state,
        loading: false,
        categories: state.categories.map((v) =>
          v.id === action.newInfo.id ? action.newInfo : v
        ),
        error: null,
      };
    case "DELETE_INFO_SUCCESS":
      return {
        ...state,
        loading: false,
        categories: state.categories.filter((v) => v.id !== action.id),
        error: null,
      };
    case "GET_ALL_ERROR":
    case "UPDATE_INFO_ERROR":
    case "DELETE_INFO_ERROR":
      return {
        ...state,
        loading: false,
        categories: null,
        error: action.error,
      };
    default:
      return state;
  }
}
