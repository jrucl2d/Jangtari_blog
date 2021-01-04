import React, { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { getAllPosts } from "../../modules/postReducer";
import LoadingComponent from "../MainPage/LoadingComponent";

function PostListComponent({ location }) {
  const dispatch = useDispatch();

  const { result, loading, error } = useSelector((state) => state.postReducer);
  useEffect(() => {
    if (result) return;
    const id = location.pathname.split("/")[2];
    dispatch(getAllPosts(id));
  }, []);

  useEffect(() => {
    if (result) {
      console.log(result);
    }
  }, [result]);

  useEffect(() => {
    if (error) {
      alert("에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
    }
  }, [error]);

  return (
    <div>
      {loading ? (
        <div className="main-loading">
          <LoadingComponent />
        </div>
      ) : (
        <h1>wleg</h1>
      )}
    </div>
  );
}

export default PostListComponent;
