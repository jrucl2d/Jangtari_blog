import React, { useState, useEffect } from "react";
import "./LoginAndJoin.css";
import { Form, Button } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { login } from "../../modules/jangtariReducer";
import LoadingComponent from "../MainPage/LoadingComponent";

function LoginFormComponent({ history }) {
  const dispatch = useDispatch();
  const { success, error, loading } = useSelector(
    (state) => state.jangtariReducer
  );
  const [info, setInfo] = useState({
    username: "",
    password: "",
  });
  const onChangeInfo = (e) => {
    setInfo({
      ...info,
      [e.target.name]: e.target.value,
    });
  };
  useEffect(() => {
    if (!error) return;
    alert("아이디 혹은 비밀번호가 잘못되었습니다.");
  }, [error]);

  useEffect(() => {
    if (!success) return;
    if (success === "logoutSuc") return;
    alert(success);
    history.goBack();
  }, [success, history]);

  const onClickSubmit = async (e) => {
    if (info.username === "" || info.nickname === "" || info.password === "") {
      alert("모든 정보를 입력해주세요.");
      return;
    }
    e.preventDefault();

    dispatch(
      login({
        username: info.username,
        password: info.password,
      })
    );
  };

  return (
    <div>
      {loading ? (
        <div className="main-loading">
          <LoadingComponent />
        </div>
      ) : (
        <>
          <h1>로그인</h1>
          <Form className="join-login-form">
            <Form.Group controlId="username">
              <Form.Label>아이디</Form.Label>
              <Form.Control
                type="text"
                placeholder="아이디 입력"
                onChange={onChangeInfo}
                name="username"
                value={info.username}
              />
            </Form.Group>
            <Form.Group controlId="password">
              <Form.Label>비밀번호</Form.Label>
              <Form.Control
                type="password"
                placeholder="비밀번호 입력"
                onChange={onChangeInfo}
                name="password"
                value={info.password}
              />
            </Form.Group>
            <Button
              variant="outline-primary"
              type="button"
              onClick={onClickSubmit}
            >
              로그인
            </Button>
            <Button
              variant="outline-success"
              type="button"
              onClick={() => {
                history.push("/joinForm");
              }}
            >
              회원가입
            </Button>
          </Form>
        </>
      )}
    </div>
  );
}

export default LoginFormComponent;
