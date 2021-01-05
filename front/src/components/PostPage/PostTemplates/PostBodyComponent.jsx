import React, { useEffect, useState, useRef } from "react";
import { useSelector } from "react-redux";

function PostBodyComponent() {
  const { post } = useSelector((state) => state.postReducer);
  const theRef = useRef();
  const [showingPost, setShowingPost] = useState([]);
  const [boxHeight, setBoxHeight] = useState(0);

  useEffect(() => {
    if (post && post.content) {
      setShowingPost(
        post.content.split(/(?:\r\n|\r|\n)/g).filter((v) => v !== "")
      );
    }
    // eslint-disable-next-line
  }, []);
  useEffect(() => {
    if (theRef && theRef.current !== 0) {
      setBoxHeight(Math.ceil(theRef.current.clientHeight / 50));
    }
  }, [showingPost]);

  return (
    <>
      <div className="post-body" ref={theRef}>
        {showingPost.map((v, i) => (
          <div className="post-content-lines" key={i}>
            {v}
          </div>
        ))}
        {boxHeight !== 0 &&
          Array(boxHeight)
            .fill()
            .map((v, i) => (
              <div
                key={i}
                className="post-content-underline"
                style={{
                  top: `${i * 50}px`,
                }}
              ></div>
            ))}
      </div>
    </>
  );
}

export default PostBodyComponent;
