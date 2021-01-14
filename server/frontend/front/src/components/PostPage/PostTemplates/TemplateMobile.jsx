import React, { useState } from "react";
import { Button } from "react-bootstrap";
import { useSelector } from "react-redux";
import CommentComponent from "./CommentComponent";
import PostBodyComponent from "./PostBodyComponent";
import PostImageComponent from "./PostImageComponent";

function TemplateMobile({ history }) {
  const { post } = useSelector((state) => state.postReducer);
  const [foldMode, setFoldMode] = useState(false);
  const onClickFold = (e) => {
    setFoldMode(!foldMode);
  };

  return (
    <>
      <h1 className="post-title">{post && post.title}</h1>
      <div className="post-main-box template0">
        <div className="post-main-content">
          <PostImageComponent />
          <Button
            className="post-fold-button"
            variant={`outline-${foldMode ? "info" : "warning"}`}
            onClick={onClickFold}
          >
            {foldMode ? "펴기" : "접기"}
          </Button>
          <div className="post-content-notice">게시글</div>
          <PostBodyComponent foldMode={foldMode} />
          <CommentComponent />
        </div>
      </div>
    </>
  );
}

export default TemplateMobile;
