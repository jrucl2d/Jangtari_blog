import React, { useRef, useEffect, useState } from "react";
import { Button, Form, Modal } from "react-bootstrap";
import { useSelector, useDispatch } from "react-redux";
import { Link } from "react-router-dom";
import { getAllCategories } from "../../modules/categoryReducer";
import { checkPW, logout } from "../../modules/jangtariReducer";

function SideMenuComponent({ isToggle, onClickMenuButton }) {
  const dispatch = useDispatch();
  const { categories } = useSelector((state) => state.categoryReducer);
  const [show, setShow] = useState(false);

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

  const onClickChange = async () => {
    onClickMenuButton();
    setShow(true);
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
                <Link onClick={onClickChange} to="#">
                  정보변경
                </Link>
                <PasswordModal show={show} setShow={setShow} />
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

function PasswordModal({ show, setShow }) {
  const theRef = useRef();
  const dispatch = useDispatch();
  const [password, setPassword] = useState("");
  useEffect(() => {
    if (show) {
      theRef.current.focus();
    }
  }, [show]);
  const onClickSubmit = () => {
    dispatch(checkPW(password));
    onClickClose();
  };
  const onClickClose = () => {
    setShow(false);
    setPassword("");
  };
  return (
    <Modal
      className="category-modal"
      show={show}
      size="lg"
      onHide={onClickClose}
      aria-labelledby="contained-modal-title-vcenter"
      centered
    >
      <Modal.Header closeButton>
        <Modal.Title id="contained-modal-title-vcenter">
          비밀번호를 다시 입력해주세요
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form.Group controlId="password">
          <Form.Label>비밀번호</Form.Label>
          <Form.Control
            type="password"
            placeholder="비밀번호 입력"
            onChange={(e) => setPassword(e.target.value)}
            name="password"
            value={password}
            ref={theRef}
          />
        </Form.Group>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="outline-primary" onClick={onClickSubmit}>
          확인
        </Button>
        <Button variant="outline-danger" onClick={onClickClose}>
          닫기
        </Button>
      </Modal.Footer>
    </Modal>
  );
}
