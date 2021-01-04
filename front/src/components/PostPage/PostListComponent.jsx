import React, { useEffect } from "react";
import { Button } from "react-bootstrap";
import { useSelector, useDispatch } from "react-redux";
import { Link } from "react-router-dom";
import { getAllPosts } from "../../modules/postReducer";
import LoadingComponent from "../MainPage/LoadingComponent";
import qs from "query-string";
import "./PostStyle.css";

function PostListComponent({ location, history }) {
  const dispatch = useDispatch();
  const { result, loading, error } = useSelector((state) => state.postReducer);

  useEffect(() => {
    const id = location.pathname.split("/")[2];
    const query = qs.parse(location.search);
    if (query.page) {
      if (query.type) {
        dispatch(getAllPosts(id, query.page, query.type, query.keyword));
      } else {
        dispatch(getAllPosts(id, +query.page));
      }
    } else {
      dispatch(getAllPosts(id));
    }
    // eslint-disable-next-line
  }, [location.search]);

  useEffect(() => {
    if (error) {
      alert("에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
    }
  }, [error]);

  const onClickPageNumber = (e) => {
    const id = location.pathname.split("/")[2];
    const wantNumber = e.target.innerHTML;
    const query = qs.parse(location.search);
    query.page = wantNumber;
    let theUrl = `/category/${id}/${location.pathname.split("/")[3]}?page=${
      query.page
    }`;
    if (query.type) {
      theUrl += "&type=" + query.type + "&keyword=" + query.keyword;
    }
    history.push(theUrl);
  };
  return (
    <div className="post-list">
      {loading ? (
        <div className="main-loading">
          <LoadingComponent />
        </div>
      ) : (
        <>
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
                    onClick={onClickPageNumber}
                  >
                    {Math.floor((result.currentPageNum - 1) / 10) * 10 + i + 1}
                  </Button>
                ))
              : null}
            {result && result.nextPage && (
              <Button variant="outline-success">
                <i className="fas fa-arrow-down"></i>
              </Button>
            )}
          </div>
          <ul className="post-list-box">
            <div>
              <li className="post-list-content">
                {location.pathname.split("/")[3]}
              </li>
              {Array(10)
                .fill()
                .map((v, i) => (
                  <li className="post-list-content" key={i}>
                    <Link to="#">
                      {result && result.result.content.length > 0
                        ? result.result.content[i].title
                        : null}
                    </Link>
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
