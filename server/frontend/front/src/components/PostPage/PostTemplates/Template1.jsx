import React from "react";
import { useSelector } from "react-redux";
import CommentComponent from "./CommentComponent";
import PostBodyComponent from "./PostBodyComponent";
import PostImageComponent from "./PostImageComponent";

function Template1() {
  const { post } = useSelector((state) => state.postReducer);

  return (
    <>
      <h1 className="post-title">{post && post.title}</h1>
      <div className="post-main-box template0">
        <div className="post-main-content">
          <div className="post-left">
            <PostBodyComponent />
          </div>
          <div className="post-right">
            <PostImageComponent />
            <CommentComponent />
          </div>
        </div>
      </div>
    </>
  );
}

export default Template1;
