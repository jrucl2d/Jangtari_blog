import React from "react";
import { Dropdown, DropdownButton } from "react-bootstrap";
import { useSelector } from "react-redux";

function CommentComponent() {
  const { post } = useSelector((state) => state.postReducer);

  const onClickAddComment = (e) => {
    if (!post) return;
    console.log(post);
  };

  return (
    <ul className="post-comment-box">
      <h3>
        댓글<i className="fas fa-plus" onClick={onClickAddComment}></i>
      </h3>
      {post && post.comments && post.comments.length > 0 ? (
        post.comments.map((v) => (
          <li key={v.commentId}>
            <div className="comment-content">
              {v.recomment !== null &&
                Array(v.recomment.split("-").length)
                  .fill()
                  .map((v, i) => (
                    <span key={i} className="post-comment-blank"></span>
                  ))}
              <span>
                {v.recomment !== null ? "┖ " : "-"}
                {v.nickName} : {v.comment}
              </span>
            </div>
            <DropdownButton
              key="up"
              drop="up"
              variant="outline-secondary"
              title={<i className="fas fa-ellipsis-h" name="new"></i>}
            >
              <Dropdown.Item eventKey="1">
                <div className="dropdown-inner">
                  댓글
                  <i className="far fa-comment-dots"></i>
                </div>
              </Dropdown.Item>
              <Dropdown.Item eventKey="2">
                <div className="dropdown-inner">
                  수정
                  <i className="fas fa-hammer"></i>
                </div>
              </Dropdown.Item>
              <Dropdown.Item eventKey="3">
                <div className="dropdown-inner">
                  삭제
                  <i className="far fa-trash-alt"></i>
                </div>
              </Dropdown.Item>
            </DropdownButton>
          </li>
        ))
      ) : (
        <li>
          <div className="comment-content">
            <span>댓글이 없습니다.</span>
          </div>
        </li>
      )}
    </ul>
  );
}

export default CommentComponent;
