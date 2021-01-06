import React, { useEffect, useState, useRef } from "react";
import { useSelector } from "react-redux";

function PostBodyComponent({ foldMode }) {
  const { post } = useSelector((state) => state.postReducer);
  const theRef = useRef();
  const [showingPost, setShowingPost] = useState([]);

  useEffect(() => {
    if (post && post.content) {
      setShowingPost(
        post.content.split(/(?:\r\n|\r|\n)/g).filter((v) => v !== "")
      );
    }
    // eslint-disable-next-line
  }, []);

  return (
    <>
      <div className={`post-body ${foldMode ? "foldMode" : ""}`} ref={theRef}>
        {showingPost.map((v, i) => (
          <div className="post-content-lines" key={i}>
            {v}
          </div>
        ))}
      </div>
    </>
  );
}

export default PostBodyComponent;
