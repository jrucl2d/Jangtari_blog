import axios from "axios";
import React, { useState, useEffect } from "react";
import { Dropdown, DropdownButton } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { setNewComments } from "../../../modules/postReducer";

function CommentComponent() {
  const dispatch = useDispatch();
  const { post } = useSelector((state) => state.postReducer);
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
    if (theComment === null || localStorage.getItem("username") === null)
      return;
    try {
      await axios.post("/user/comment", {
        postId: post.id,
        commenter: localStorage.getItem("username"),
        comment: theComment,
        recommentId: null,
      });
      setNewCommentRefresh(true);
    } catch (err) {
      console.error(err);
      alert("사용자 정보가 일치하지 않습니다.");
      return;
    }
  };
  const onClickAddRecomment = async (recommentId) => {
    if (!post) return;
    const theComment = prompt("대댓글을 작성해주세요");
    if (theComment === "") {
      alert("빈 댓글을 작성할 수 없습니다.");
      return;
    }
    if (theComment === null || localStorage.getItem("username") === null)
      return;
    try {
      await axios.post("/user/comment", {
        postId: post.id,
        commenter: localStorage.getItem("username"),
        comment: theComment,
        recommentId: recommentId,
      });
      setNewCommentRefresh(true);
    } catch (err) {
      console.error(err);
      alert("사용자 정보가 일치하지 않습니다.");
      return;
    }
  };
  const onClickModify = async (commentId, comment) => {
    if (!post) return;
    const theComment = prompt("댓글을 수정해주세요", comment);
    if (theComment === "") {
      alert("빈 댓글로 수정할 수 없습니다.");
      return;
    }
    if (theComment === null || localStorage.getItem("username") === null)
      return;
    try {
      await axios.put("/user/comment", {
        commenter: localStorage.getItem("username"),
        comment: theComment,
        id: commentId,
      });
      alert("댓글을 수정했습니다.");
      setNewCommentRefresh(true);
    } catch (err) {
      console.error(err);
      alert("사용자 정보가 일치하지 않습니다.");
      return;
    }
  };
  const onClickDelete = async (commentId) => {
    if (!post) return;
    if (!window.confirm("댓글을 삭제하시겠습니까?")) return;

    if (localStorage.getItem("username") === null) return;
    try {
      await axios.delete(`/user/comment/${commentId}`);
      setNewCommentRefresh(true);
    } catch (err) {
      console.error(err);
      alert("사용자 정보가 일치하지 않습니다.");
      return;
    }
  };
  return (
    <ul className="post-comment-box">
      <h3>
        댓글
        {localStorage.getItem("username") && (
          <i className="fas fa-plus" onClick={onClickAddComment}></i>
        )}
      </h3>
      {post && post.comments && post.comments.length > 0 ? (
        post.comments.map((v) => (
          <li key={v.commentId}>
            <div className="comment-content">
              {v.recomment !== v.commentId && (
                <span className="post-comment-blank"></span>
              )}
              <span>
                {v.recomment !== v.commentId ? "┖ " : "-"}
                {v.nickname} : {v.comment}
              </span>
            </div>
            {localStorage.getItem("username") && (
              <DropdownButton
                key="up"
                drop="up"
                variant="outline-secondary"
                title={<i className="fas fa-ellipsis-h" name="new"></i>}
              >
                <Dropdown.Item
                  eventKey="1"
                  onClick={() => onClickAddRecomment(v.recomment)}
                >
                  <div className="dropdown-inner">
                    댓글
                    <i className="far fa-comment-dots"></i>
                  </div>
                </Dropdown.Item>
                {localStorage.getItem("username") &&
                  localStorage.getItem("username") === v.username && (
                    <>
                      <Dropdown.Item eventKey="2">
                        <div
                          className="dropdown-inner"
                          onClick={() => onClickModify(v.commentId, v.comment)}
                        >
                          수정
                          <i className="fas fa-hammer"></i>
                        </div>
                      </Dropdown.Item>
                      <Dropdown.Item
                        eventKey="3"
                        onClick={() => onClickDelete(v.commentId)}
                      >
                        <div className="dropdown-inner">
                          삭제
                          <i className="far fa-trash-alt"></i>
                        </div>
                      </Dropdown.Item>
                    </>
                  )}
              </DropdownButton>
            )}
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
