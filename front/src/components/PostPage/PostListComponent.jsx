import React, { useEffect, useState, useCallback } from "react";
import { Button, Dropdown, DropdownButton, Modal } from "react-bootstrap";
import { useSelector, useDispatch } from "react-redux";
import { Link } from "react-router-dom";
import { deletePost, getAllPosts } from "../../modules/postReducer";
import LoadingComponent from "../MainPage/LoadingComponent";
import qs from "query-string";
import "./PostStyle.css";

function PostListComponent({ location, history }) {
  const dispatch = useDispatch();
  const [searchInfo, setSearchInfo] = useState({
    type: "title",
    keyword: "",
  });
  const [show, setShow] = useState(false);
  const [updateMode, setUpdateMode] = useState(null); // 포스트 id와 템플릿이 들어가면 updatemode
  const { result, success, loading, error } = useSelector(
    (state) => state.postReducer
  );

  const getPostsFunc = useCallback(() => {
    const id = location.pathname.split("/")[2];
    const query = qs.parse(location.search);
    let theUrl = `/category/${id}/posts`;
    if (query.page) {
      if (query.type) {
        dispatch(
          getAllPosts(
            theUrl +
              "?page=" +
              query.page +
              "&type=" +
              query.type +
              "&keyword=" +
              query.keyword
          )
        );
      } else {
        dispatch(getAllPosts(theUrl + "?page=" + query.page));
      }
    } else {
      if (query.type) {
        dispatch(
          getAllPosts(
            theUrl + "?type=" + query.type + "&keyword=" + query.keyword
          )
        );
      } else {
        dispatch(getAllPosts(theUrl));
      }
    }
    // eslint-disable-next-line
  }, [location]);

  useEffect(() => {
    getPostsFunc();
    // eslint-disable-next-line
  }, [location]);

  useEffect(() => {
    if (error) {
      alert("에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
    }
  }, [error]);

  useEffect(() => {
    if (!success) return;
    getPostsFunc();
    // eslint-disable-next-line
  }, [success]);

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
  const onClickAnotherPage = (e) => {
    const id = location.pathname.split("/")[2];
    const wantNumber =
      e.currentTarget.name === "next"
        ? result && result.nextPage && +result.nextPage.pageNumber + 1
        : result && result.prevPage && +result.prevPage.pageNumber + 1;
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

  const onClickSelect = (e) => {
    setSearchInfo({
      ...searchInfo,
      type: e.currentTarget.name,
    });
  };
  const onChangeSearch = (e) => {
    setSearchInfo({
      ...searchInfo,
      keyword: e.target.value,
    });
  };
  const onClickSubmit = (e) => {
    e.preventDefault();
    if (searchInfo.keyword.split(" ").length > 1) {
      alert("공백 없는 단어를 입력하세요.");
      return;
    }
    const id = location.pathname.split("/")[2];
    const theUrl = `/category/${id}/${location.pathname.split("/")[3]}?type=${
      searchInfo.type[0]
    }&keyword=${searchInfo.keyword}`;
    history.push(theUrl);
  };

  const onClickUpdate = (id, template) => {
    setShow(true);
    setUpdateMode({ id, template });
  };
  const onClickDelete = (id) => {
    if (window.confirm("정말로 삭제하시겠습니까?")) {
      dispatch(deletePost(id));
    } else {
      return;
    }
  };

  return (
    <>
      {loading ? (
        <div className="main-loading">
          <LoadingComponent />
        </div>
      ) : (
        <div className="post-list">
          <div className="post-list-pagination">
            {result && result.prevPage && (
              <Button
                variant="outline-warning"
                name="prev"
                onClick={onClickAnotherPage}
              >
                <i className="fas fa-arrow-up"></i>
              </Button>
            )}
            {result && result.result.content.length > 0
              ? Array(
                  result.totalPageNum -
                    Math.floor((result.currentPageNum - 1) / 10) * 10 <
                    10
                    ? result.totalPageNum -
                        Math.floor((result.currentPageNum - 1) / 10) * 10
                    : 10
                )
                  .fill()
                  .map((v, i) => (
                    <Button
                      className="post-list-numbers"
                      variant={`${
                        Math.floor((result.currentPageNum - 1) / 10) * 10 +
                          i +
                          1 !==
                        result.currentPageNum
                          ? "outline-"
                          : ""
                      }info`}
                      key={i}
                      onClick={onClickPageNumber}
                    >
                      {Math.floor((result.currentPageNum - 1) / 10) * 10 +
                        i +
                        1}
                    </Button>
                  ))
              : null}
            {result && result.nextPage && (
              <Button
                variant="outline-success"
                name="next"
                onClick={onClickAnotherPage}
              >
                <i className="fas fa-arrow-down"></i>
              </Button>
            )}
          </div>
          <ul className="post-list-box">
            <div>
              {localStorage.getItem("role") &&
                localStorage.getItem("role") === "ADMIN" && (
                  <>
                    <Button
                      className="post-list-add"
                      variant="outline-light"
                      onClick={() => {
                        setShow(true);
                        setUpdateMode(null);
                      }}
                    >
                      <i className="fas fa-plus"></i>
                    </Button>
                    <TemplateModal
                      show={show}
                      setShow={setShow}
                      loc={location.pathname.split("/")[2]}
                      updateMode={updateMode}
                    />
                  </>
                )}

              <li className="post-list-content post-list-title">
                {location.pathname.split("/")[3]}
              </li>
              {Array(10)
                .fill()
                .map((v, i) => (
                  <li className="post-list-content" key={i}>
                    <Link
                      to={
                        result &&
                        result.result.content.length > 0 &&
                        i < result.result.content.length
                          ? `/post/${result.result.content[i].id}/${result.result.content[i].template}`
                          : "#"
                      }
                    >
                      {result &&
                      result.result.content.length > 0 &&
                      i < result.result.content.length
                        ? result.result.content[i].title
                        : null}
                    </Link>
                    {localStorage.getItem("role") &&
                      localStorage.getItem("role") === "ADMIN" &&
                      result &&
                      result.result.content.length > 0 &&
                      i < result.result.content.length && (
                        <div className="post-list-setting">
                          <Button
                            variant="outline-primary"
                            onClick={() =>
                              onClickUpdate(
                                result.result.content[i].id,
                                result.result.content[i].template
                              )
                            }
                          >
                            수정
                          </Button>
                          <Button
                            variant="outline-danger"
                            onClick={() => {
                              onClickDelete(result.result.content[i].id);
                            }}
                          >
                            삭제
                          </Button>
                        </div>
                      )}
                  </li>
                ))}
            </div>
            <div className="post-list-search">
              <DropdownButton
                id="dropdown-basic-button"
                title={
                  searchInfo.type === "title"
                    ? "제목"
                    : searchInfo.type === "content"
                    ? "내용"
                    : "해시태그"
                }
                variant="outline-warning"
              >
                <Dropdown.Item name="title" onClick={onClickSelect}>
                  제목
                </Dropdown.Item>
                <Dropdown.Item name="content" onClick={onClickSelect}>
                  내용
                </Dropdown.Item>
                <Dropdown.Item name="hashtag" onClick={onClickSelect}>
                  해시태그
                </Dropdown.Item>
              </DropdownButton>
              <form onSubmit={onClickSubmit}>
                <input
                  type="text"
                  value={searchInfo.keyword}
                  onChange={onChangeSearch}
                />
                <Button type="submit" variant="outline-primary">
                  검색
                </Button>
              </form>
            </div>
          </ul>
        </div>
      )}
    </>
  );
}

export default PostListComponent;

function TemplateModal({ show, setShow, loc, updateMode }) {
  return (
    <Modal
      className="category-modal"
      show={show}
      size="lg"
      onHide={() => setShow(false)}
      aria-labelledby="contained-modal-title-vcenter"
      centered
    >
      <Modal.Header closeButton>
        <Modal.Title id="contained-modal-title-vcenter">
          {updateMode === null
            ? "템플릿을 선택하세요."
            : `기존 템플릿은 ${updateMode.template}입니다. 바꾸시겠습니까?`}
        </Modal.Title>
      </Modal.Header>
      <Modal.Body className="template-select-modal">
        <Link
          to={
            updateMode === null
              ? `/category/${loc}/add/post?template=${0}`
              : `/category/${loc}/update/post?id=${updateMode.id}&template=${0}`
          }
        >
          <img
            className={`template-select ${
              updateMode && +updateMode.template === +0
                ? "before-selected-template"
                : ""
            }`}
            src="/template0.PNG"
            alt="템플릿0"
          />
        </Link>
        <Link
          to={
            updateMode === null
              ? `/category/${loc}/add/post?template=${1}`
              : `/category/${loc}/update/post?id=${updateMode.id}&template=${1}`
          }
        >
          <img
            className={`template-select ${
              updateMode && +updateMode.template === +1
                ? "before-selected-template"
                : ""
            }`}
            src="/template1.PNG"
            alt="템플릿1"
          />
        </Link>
      </Modal.Body>
      <Modal.Footer>
        <Button
          variant="outline-danger"
          onClick={() => {
            setShow(false);
          }}
        >
          닫기
        </Button>
      </Modal.Footer>
    </Modal>
  );
}
