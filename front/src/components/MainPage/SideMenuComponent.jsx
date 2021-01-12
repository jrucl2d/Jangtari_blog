import React, { useRef, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { Link } from "react-router-dom";
import { getAllCategories } from "../../modules/categoryReducer";
import { logout } from "../../modules/jangtariReducer";

function SideMenuComponent({ isToggle, onClickMenuButton }) {
  const dispatch = useDispatch();
  const { categories } = useSelector((state) => state.categoryReducer);

  const menuRef = useRef(null);
  useEffect(() => {
    if (categories) return;
    dispatch(getAllCategories());
    // eslint-disable-next-line
  }, []);
  useEffect(() => {
    if (isToggle) {
      menuRef.current.classList.add("show");
    } else {
      menuRef.current.classList.remove("show");
    }
  }, [isToggle]);

  const onClickLogout = async () => {
    dispatch(logout());
    onClickMenuButton();
  };

  return (
    <>
      <div className="side-bar" ref={menuRef}>
        <h2>장따리의 똥글</h2>
        <ul className="side-bar-user">
          <li>
            <Link onClick={() => onClickMenuButton()} to="/">
              메인으로
            </Link>
          </li>
          <li>
            <Link onClick={() => onClickMenuButton()} to="/about">
              Who is '장딱'
            </Link>
          </li>
          {localStorage.getItem("role") === null ? (
            <>
              <li>
                <Link onClick={() => onClickMenuButton()} to="/loginForm">
                  로그인
                </Link>
              </li>
              <li>
                <Link onClick={() => onClickMenuButton()} to="/joinForm">
                  회원가입
                </Link>
              </li>
            </>
          ) : (
            <>
              <li>
                <Link onClick={onClickLogout} to="/">
                  로그아웃
                </Link>
              </li>
              <li>
                <Link to="/">정보변경</Link>
              </li>
            </>
          )}
        </ul>
        <h2>
          카테고리
          {localStorage.getItem("role") &&
            localStorage.getItem("role") === "ADMIN" && (
              <>
                &nbsp;
                <Link
                  className="category-setting"
                  onClick={() => onClickMenuButton()}
                  to="/update/category"
                >
                  설정
                </Link>
              </>
            )}
        </h2>
        <ul className="side-bar-list">
          {categories &&
            categories.map((v) => (
              <li key={v.id}>
                <Link
                  onClick={() => onClickMenuButton()}
                  to={`/category/${v.id}/${v.name}`}
                >
                  {v.name}
                </Link>
              </li>
            ))}
        </ul>
      </div>
      <div className="side-bar-curtain" onClick={() => onClickMenuButton()} />
    </>
  );
}

export default SideMenuComponent;
