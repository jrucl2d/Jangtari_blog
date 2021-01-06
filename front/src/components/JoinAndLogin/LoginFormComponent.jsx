import React, { useState } from "react";
import "./LoginAndJoin.css";
import { Form, Button } from "react-bootstrap";
import axios from "axios";
import { useDispatch } from "react-redux";
import { LogIn } from "../../modules/memberReducer";

function LoginFormComponent({ history }) {
  const dispatch = useDispatch();
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
  const onClickSubmit = async (e) => {
    if (info.username === "" || info.nickname === "" || info.password === "") {
      alert("모든 정보를 입력해주세요.");
      return;
    }
    e.preventDefault();
    try {
      const result = await axios.post("/login", {
        username: info.username,
        password: info.password,
      });
      console.log(result);
      const nickname = Object.keys(result.data.result)[0];
      const role = Object.values(result.data.result)[0];
      console.log(nickname);
      dispatch(
        LogIn({
          username: info.username,
          nickname,
          role,
        })
      );
      alert(`${nickname}님 환영합니다.`);
      history.goBack();
    } catch (err) {
      console.log(err);
      alert("아이디 혹은 비밀번호가 잘못되었습니다.");
      return;
    }
  };

  return (
    <div>
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
        <Button variant="outline-primary" type="button" onClick={onClickSubmit}>
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
    </div>
  );
}

export default LoginFormComponent;
