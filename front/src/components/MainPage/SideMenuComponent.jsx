import axios from "axios";
import React, { useRef, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { Link } from "react-router-dom";
import { getAllCategories } from "../../modules/categoryReducer";
import { LogOut } from "../../modules/memberReducer";

function SideMenuComponent({ isToggle, onClickMenuButton }) {
  const dispatch = useDispatch();
  const { categories } = useSelector((state) => state.categoryReducer);
  const { role } = useSelector((state) => state.memberReducer);

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

  const onClickLogout = async (e) => {
    try {
      await axios.get("/signout");
      dispatch(LogOut());
      alert("로그아웃 하였습니다.");
    } catch (err) {
      alert("문제가 발생했습니다. 잠시 뒤에 다시 시도해주세요.");
      return;
    }
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
          {role === null ? (
            <li>
              <Link onClick={() => onClickMenuButton()} to="/loginForm">
                로그인
              </Link>
            </li>
          ) : (
            <li>
              <Link onClick={onClickLogout} to="/">
                로그아웃
              </Link>
            </li>
          )}
          {role == null && (
            <li>
              <Link onClick={() => onClickMenuButton()} to="/joinForm">
                회원가입
              </Link>
            </li>
          )}
        </ul>
        <h2>
          카테고리
          {role && role === "ADMIN" && (
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
