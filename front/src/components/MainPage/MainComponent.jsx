import React, { useEffect, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getAllCategories } from "../../modules/categoryReducer";
import CategoryComponent from "./CategoryComponent";
import LoadingComponent from "./LoadingComponent";
import ScrollContainer from "react-indiana-drag-scroll";
import { Link } from 'react-router-dom'
import "./MainComponent.css";

function MainComponent() {
  const { categories, loading, error } = useSelector(
    (state) => state.categoryReducer
  );
  const dispatch = useDispatch();
  const menuButtonRef = useRef(null);
  const menuRef = useRef(null);


  // 카테고리 목록 불러오기
  useEffect(() => {
    dispatch(getAllCategories());
    // eslint-disable-next-line
  }, []);
  useEffect(() => {
    if (error) {
      alert("에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
    }
  }, [error]);

  const onClickMenuButton = (e) => {
    menuButtonRef.current.classList.toggle("active-1");
    menuRef.current.classList.toggle("show");
  };

  return (
    <div
      className="main-body"
    >
      <header>
        <h1 className="main-title">장따리 똥글</h1>
        <button
          className="menu-trigger"
          ref={menuButtonRef}
          onClick={onClickMenuButton}
        >
          <span></span>
          <span></span>
          <span></span>
        </button>
        <div className="side-bar" ref={menuRef}>
          <h2>장따리의 똥글</h2>
          <ul className="side-bar-user">
            <li><Link to="/about">Who is '장딱'</Link></li>
            <li><Link to="/loginForm">로그인</Link></li>
            <li><Link to="/joinForm">회원가입</Link></li>
          </ul>
          <h2>카테고리</h2>
          <ul className="side-bar-list">
            {categories && categories.map((v) => (
              <li key={v.id}><Link>{v.name}</Link></li>
            ))}
          </ul>
        </div>
      </header>
      {loading ? (
        <div className="main-loading">
          <LoadingComponent />
        </div>
      ) : (
          <ScrollContainer className="scroll-container category-container">
            {categories &&
              categories.map((v) => (
                <CategoryComponent key={v.id} category={v} />
              ))}
          </ScrollContainer>
        )}
      <footer>
        <div className="footer-icons">
          <a
            target="_blank"
            rel="noreferrer"
            href="https://www.facebook.com/profile.php?id=100005432100050"
          >
            <img src="./fb.png" alt="페이스북" />
          </a>
          <a target="_blank" rel="noreferrer" href="https://www.instagram.com/">
            <img src="./ig.png" alt="인스타" />
          </a>
          <a target="_blank" rel="noreferrer" href="https://naver.com">
            <img src="./kakao.png" alt="카카오톡" />
          </a>
          <a
            target="_blank"
            rel="noreferrer"
            href="https://www.github.com/jrucl2d"
          >
            <img src="./github.png" alt="깃헙" />
          </a>
        </div>
        <div className="footer-introduce">
          <div>
            Made by &nbsp;
            <a
              target="_blank"
              rel="noreferrer"
              href="https://www.github.com/jrucl2d"
            >
              Yusegonggeun
            </a>
          </div>
          <div>010-2578-4068</div>
        </div>
      </footer>
    </div>
  );
}

export default MainComponent;
