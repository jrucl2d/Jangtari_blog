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
    case "GET_ALL_ERROR":
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
