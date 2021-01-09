import React, { useEffect, useState } from "react";
import "./LoginAndJoin.css";
import { useDispatch, useSelector } from "react-redux";
import { Form, Button } from "react-bootstrap";
import LoadingComponent from "../MainPage/LoadingComponent";
import { join } from "../../modules/jangtariReducer";

function JoinFormComponent({ history }) {
  const dispatch = useDispatch();
  const { success, error, loading } = useSelector(
    (state) => state.jangtariReducer
  );
  const [info, setInfo] = useState({
    username: "",
    nickname: "",
    password: "",
    password2: "",
  });
  const [showPWWarn, setShowPWWarn] = useState(false);

  useEffect(() => {
    if (localStorage.getItem("role")) {
      alert("로그인 했는데 왜 왔니");
      history.push("/");
    }
    // eslint-disable-next-line
  }, []);

  useEffect(() => {
    if (!error) return;
    alert("이미 존재하는 회원입니다.");
  }, [error]);

  useEffect(() => {
    if (!success) return;
    alert(success);
    history.push("/");
  }, [success, history]);

  useEffect(() => {
    if (info.password !== info.password2) {
      setShowPWWarn(true);
    } else {
      setShowPWWarn(false);
    }
  }, [info.password, info.password2]);

  const onChangeInfo = (e) => {
    setInfo({
      ...info,
      [e.target.name]: e.target.value,
    });
  };
  const onClickSubmit = async (e) => {
    if (
      info.username === "" ||
      info.nickname === "" ||
      info.password === "" ||
      info.password2 === ""
    ) {
      alert("모든 정보를 입력해주세요.");
      return;
    }
    dispatch(
      join({
        username: info.username,
        nickname: info.nickname,
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
          <h1>회원가입</h1>
          <Form className="join-login-form join-form">
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
            <Form.Group controlId="nickname">
              <Form.Label>닉네임</Form.Label>
              <Form.Control
                type="text"
                placeholder="닉네임 입력"
                onChange={onChangeInfo}
                name="nickname"
                value={info.nickname}
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
            <Form.Group controlId="password2">
              <Form.Label>비밀번호 확인</Form.Label>
              <Form.Control
                type="password"
                placeholder="비밀번호 재입력"
                onChange={onChangeInfo}
                name="password2"
                value={info.password2}
              />
            </Form.Group>
            {showPWWarn ? (
              <div className="join-password-check">
                비밀번호가 맞지 않습니다.
              </div>
            ) : null}
            <Button
              id="join-button"
              variant="outline-primary"
              type="button"
              onClick={onClickSubmit}
            >
              회원가입
            </Button>
          </Form>
        </>
      )}
    </div>
  );
}

export default JoinFormComponent;
