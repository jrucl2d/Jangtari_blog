import * as postAPI from "../api/PostAPI";

export const getAllPosts = (theUrl) => async (dispatch) => {
  dispatch({ type: "GET_ALL_POSTS" });
  try {
    const result = await postAPI.getAll(theUrl);
    dispatch({ type: "GET_ALL_POSTS_SUCCESS", result });
  } catch (err) {
    dispatch({ type: "GET_ALL_POSTS_ERROR", error: err });
  }
};
export const getOnePost = (id) => async (dispatch) => {
  dispatch({ type: "GET_ONE_POST" });
  try {
    const result = await postAPI.getOne(id);
    dispatch({ type: "GET_ONE_POST_SUCCESS", result });
  } catch (err) {
    dispatch({ type: "GET_ONE_POST_ERROR", error: err });
  }
};
export const setNewComments = (comments) => {
  return {
    type: "SET_NEW_COMMENTS",
    comments,
  };
};
export const addPost = (newInfo, pictures) => async (dispatch) => {
  dispatch({ type: "ADD_POST" });
  try {
    await postAPI.addPost(newInfo, pictures);
    dispatch({
      type: "ADD_POST_SUCCESS",
      success: "새로운 게시글이 추가되었습니다.",
    });
  } catch (err) {
    dispatch({ type: "ADD_POST_ERROR", error: err });
  }
};
export const updatePost = (newInfo, pictures) => async (dispatch) => {
  dispatch({ type: "UPDATE_POST" });
  try {
    await postAPI.updatePost(newInfo, pictures);
    dispatch({
      type: "UPDATE_POST_SUCCESS",
      success: "게시글을 수정하였습니다.",
    });
  } catch (err) {
    dispatch({ type: "UPDATE_POST_ERROR", error: err });
  }
};
export const deletePost = (id) => async (dispatch) => {
  dispatch({ type: "DELETE_POST" });
  try {
    await postAPI.deletePost(id);
    dispatch({ type: "DELETE_POST_SUCCESS", id });
  } catch (err) {
    dispatch({ type: "DELETE_POST_ERROR", error: err });
  }
};

const initialState = {
  loading: false,
  result: null,
  post: null,
  success: null,
  error: null,
};

export default function postReducer(state = initialState, action) {
  switch (action.type) {
    case "GET_ALL_POSTS":
      return {
        ...state,
        loading: true,
        result: null,
        post: null,
        success: null,
        error: null,
      };
    case "GET_ALL_POSTS_SUCCESS":
      return {
        ...state,
        loading: false,
        result: action.result,
        success: null,
        post: null,
        error: null,
      };
    case "ADD_POST":
    case "UPDATE_POST":
      return {
        ...state,
        loading: true,
        success: null,
        error: null,
        post: null,
      };
    case "GET_ONE_POST":
      return {
        ...state,
        loading: true,
        result: null,
        post: null,
        success: null,
        error: null,
      };
    case "DELETE_POST":
      return {
        ...state,
        loading: true,
        success: null,
        error: null,
      };
    case "DELETE_POST_SUCCESS":
      return {
        ...state,
        loading: false,
        success: true,
        error: null,
      };
    case "DELETE_POST_ERROR":
      return {
        ...state,
        loading: false,
        success: action.id,
        error: action.error,
      };
    case "GET_ONE_POST_SUCCESS":
      return {
        ...state,
        loading: false,
        result: null,
        post: action.result,
        success: null,
        error: null,
      };
    case "SET_NEW_COMMENTS":
      return {
        ...state,
        loading: false,
        result: null,
        success: null,
        post: {
          ...state.post,
          comments: action.comments,
        },
        error: null,
      };
    case "UPDATE_POST_SUCCESS":
    case "ADD_POST_SUCCESS":
      return {
        ...state,
        loading: false,
        success: action.success,
        error: null,
      };
    case "UPDATE_POST_ERROR":
    case "ADD_POST_ERROR":
      return {
        ...state,
        loading: false,
        error: action.error,
        success: null,
      };

    case "GET_ALL_POSTS_ERROR":
      return {
        ...state,
        loading: false,
        result: null,
        success: null,
        error: action.error,
      };
    default:
      return state;
  }
}
