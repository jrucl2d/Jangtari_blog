import axios from "axios";
import React, { useState, useEffect } from "react";
import { Dropdown, DropdownButton } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { setNewComments } from "../../../modules/postReducer";

function CommentComponent() {
  const dispatch = useDispatch();
  const { post } = useSelector((state) => state.postReducer);
  const { username } = useSelector((state) => state.memberReducer);
  const [newCommentRefresh, setNewCommentRefresh] = useState(false);

  useEffect(() => {
    if (!newCommentRefresh) return;
    setNewCommentRefresh(false);
    (async () => {
      try {
        const newComments = await axios.get(`/post/${post.id}/comments`);
        dispatch(setNewComments(newComments.data.result));
      } catch (err) {
        console.error(err);
        alert("댓글을 불러오는 중에 오류가 발생했습니다.");
        return;
      }
    })();
  }, [newCommentRefresh, post, dispatch]);

  const onClickAddComment = async (e) => {
    if (!post) return;
    const theComment = prompt("댓글을 작성해주세요");
    if (theComment === "") {
      alert("빈 댓글을 작성할 수 없습니다.");
      return;
    }
    if (theComment === null) return;
    try {
      await axios.post("/comment", {
        postId: post.id,
        comment: theComment,
        recommentId: null,
      });
      setNewCommentRefresh(true);
    } catch (err) {
      console.error(err);
      alert("오류가 발생했습니다. 잠시 뒤에 다시 시도해주세요.");
      return;
    }
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
                {v.nickname} : {v.comment}
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
              {username && username === v.username && (
                <>
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
                </>
              )}
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
