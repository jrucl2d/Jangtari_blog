import React from "react";
import { useSelector } from "react-redux";

function CommentComponent() {
  const { post } = useSelector((state) => state.postReducer);

  return (
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
  );
}

export default CommentComponent;
