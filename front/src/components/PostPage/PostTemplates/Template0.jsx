import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";

function Template0() {
  const { post } = useSelector((state) => state.postReducer);
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
      <h1 className="post-title">{post && post.title}</h1>
      <div className="post-main-box template0">
        <div className="post-main-content">
          <div className="post-left">
            <div
              className="post-image template0"
              style={{
                backgroundImage:
                  "url(https://source.unsplash.com/random//1280x720)",
              }}
            >
              {post && post.pictures && post.pictures[0].picture}
            </div>
            <ul className="post-comment-box">
              <h3>댓글</h3>
              {post &&
                post.comments &&
                post.comments.map((v) => (
                  <li key={v.commentId}>
                    {v.recomment !== null &&
                      Array(v.recomment.split("-").length)
                        .fill()
                        .map((v, i) => (
                          <span key={i} className="post-comment-blank"></span>
                        ))}
                    {v.recomment !== null ? "┖ " : "-"}
                    {v.nickName} : {v.comment}
                  </li>
                ))}
            </ul>
          </div>
          <div className="post-right">
            <div className="post-body">
              {showingPost.map((v, i) => (
                <div className="post-content-lines" key={i}>
                  {v}
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default Template0;
