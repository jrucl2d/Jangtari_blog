import React, { useEffect, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getAllCategories } from "../../modules/categoryReducer";
import CategoryComponent from "./CategoryComponent";
import LoadingComponent from "./LoadingComponent";
import ScrollContainer from "react-indiana-drag-scroll";
import "./MainComponent.css";

function MainComponent() {
  const { categories, loading, error } = useSelector(
    (state) => state.categoryReducer
  );
  const dispatch = useDispatch();
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
    menuRef.current.classList.toggle("active-1");
  };

  return (
    <div className="main-body">
      <header>
        <h1 className="main-title">장따리 똥글</h1>
        <button
          className="menu-trigger"
          ref={menuRef}
          onClick={onClickMenuButton}
        >
          <span></span>
          <span></span>
          <span></span>
        </button>
      </header>
      {loading ? (
        <div className="main-loading">
          <LoadingComponent />
        </div>
      ) : (
        <ScrollContainer className="scroll-container category-container">
          {categories &&
            categories.map((v) => <CategoryComponent key={v.id} />)}
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
