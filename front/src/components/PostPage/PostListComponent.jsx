import React, { useEffect } from "react";
import { Button } from "react-bootstrap";
import { useSelector, useDispatch } from "react-redux";
import { getAllPosts } from "../../modules/postReducer";
import LoadingComponent from "../MainPage/LoadingComponent";
import "./PostStyle.css";

function PostListComponent({ location }) {
  const dispatch = useDispatch();
  const { categoryId, result, loading, error } = useSelector(
    (state) => state.postReducer
  );
  useEffect(() => {
    const id = location.pathname.split("/")[2];
    if (categoryId && categoryId === id) return;
    dispatch(getAllPosts(id));
    // eslint-disable-next-line
  }, []);

  useEffect(() => {
    if (error) {
      alert("에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
    }
  }, [error]);

  return (
    <div className="post-list">
      <div className="post-list-pagination">
        {result && result.prevPage && (
          <Button variant="outline-warning">
            <i className="fas fa-arrow-up"></i>
          </Button>
        )}
        {result && result.result.content.length > 0
          ? result.result.content.map((v, i) => (
              <Button
                className="post-list-numbers"
                variant="outline-info"
                key={v.id}
              >
                {Math.floor(result.currentPageNum / 10) * 10 + i + 1}
              </Button>
            ))
          : null}
        {result && result.nextPage && (
          <Button variant="outline-success">
            <i className="fas fa-arrow-down"></i>
          </Button>
        )}
      </div>
      {loading ? (
        <div className="main-loading">
          <LoadingComponent />
        </div>
      ) : (
        <>
          <ul className="post-list-box">
            <div>
              <li className="post-list-content">
                {location.pathname.split("/")[3]}
              </li>
              {Array(10)
                .fill()
                .map((v, i) => (
                  <li className="post-list-content" key={i}>
                    {result && result.result.content.length > 0
                      ? result.result.content[i].title
                      : null}
                  </li>
                ))}
            </div>
          </ul>
        </>
      )}
    </div>
  );
}

export default PostListComponent;
