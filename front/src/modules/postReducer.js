import * as postAPI from "../api/PostAPI";

export const getAllPosts = (id) => async (dispatch) => {
  dispatch({ type: "GET_ALL_POSTS" });
  try {
    const result = await postAPI.getAll(id);
    dispatch({ type: "GET_ALL_POSTS_SUCCESS", result });
  } catch (err) {
    dispatch({ type: "GET_ALL_POSTS_ERROR,", error: err });
  }
};
// export const addCategory = (newInfo) => async (dispatch) => {
//   try {
//     const newId = await categoryAPI.add(newInfo);
//     newInfo["id"] = newId;
//     dispatch({ type: "ADD_INFO_SUCCESS", newInfo });
//   } catch (err) {
//     dispatch({ type: "ADD_INFO_ERROR,", error: err });
//   }
// };
// export const updateCategory = (newInfo) => async (dispatch) => {
//   try {
//     await categoryAPI.update(newInfo);
//     dispatch({ type: "UPDATE_INFO_SUCCESS", newInfo });
//   } catch (err) {
//     dispatch({ type: "UPDATE_INFO_ERROR,", error: err });
//   }
// };
// export const deleteCategory = (id) => async (dispatch) => {
//   try {
//     await categoryAPI.deleteCate(id);
//     dispatch({ type: "DELETE_INFO_SUCCESS", id });
//   } catch (err) {
//     dispatch({ type: "DELETE_INFO_ERROR,", error: err });
//   }
// };

const initialState = {
  loading: false,
  result: null,
  error: null,
};

export default function postReducer(state = initialState, action) {
  switch (action.type) {
    case "GET_ALL_POSTS":
      return {
        ...state,
        loading: true,
        result: null,
        error: null,
      };
    case "GET_ALL_POSTS_SUCCESS":
      return {
        ...state,
        loading: false,
        result: action.result,
        error: null,
      };
    // case "ADD_INFO_SUCCESS":
    //   return {
    //     ...state,
    //     loading: false,
    //     result: [...state.result, action.newInfo],
    //     error: null,
    //   };
    // case "UPDATE_INFO_SUCCESS":
    //   return {
    //     ...state,
    //     loading: false,
    //     result: state.result.map((v) =>
    //       v.id === action.newInfo.id ? action.newInfo : v
    //     ),
    //     error: null,
    //   };
    // case "DELETE_INFO_SUCCESS":
    //   return {
    //     ...state,
    //     loading: false,
    //     result: state.result.filter((v) => v.id !== action.id),
    //     error: null,
    //   };
    // case "ADD_INFO_ERROR":
    case "GET_ALL_POSTS_ERROR":
      // case "UPDATE_INFO_ERROR":
      // case "DELETE_INFO_ERROR":
      return {
        ...state,
        loading: false,
        result: null,
        error: action.error,
      };
    default:
      return state;
  }
}
