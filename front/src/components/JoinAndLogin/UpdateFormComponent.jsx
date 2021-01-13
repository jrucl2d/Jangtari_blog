import React, { useEffect, useState } from "react";
import "./LoginAndJoin.css";
import { useDispatch, useSelector } from "react-redux";
import { Form, Button } from "react-bootstrap";
import LoadingComponent from "../MainPage/LoadingComponent";
import {
  initCheckSucOfJang,
  updateMember,
} from "../../modules/jangtariReducer";

function UpdateFormComponent({ history }) {
  const dispatch = useDispatch();
  const { success, checkSuc, error, loading } = useSelector(
    (state) => state.jangtariReducer
  );
  const [info, setInfo] = useState({
    username: localStorage.getItem("username"),
    nickname: localStorage.getItem("nickname"),
    password: "",
  });

  useEffect(() => {
    if (!checkSuc) {
      alert("접근 불가");
      history.push("/");
    }
    dispatch(initCheckSucOfJang());
    // eslint-disable-next-line
  }, []);

  useEffect(() => {
    if (!error) return;
    alert("에러가 발생했습니다.");
  }, [error]);

  useEffect(() => {
    if (!success) return;
    if (success === "memberChange") {
      history.push("/");
    }
    // eslint-disable-next-line
  }, [success, history]);

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
    dispatch(
      updateMember({
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
          <h1>정보 변경</h1>
          <Form className="join-login-form join-form">
            <Form.Group controlId="username">
              <Form.Label>아이디</Form.Label>
              <Form.Control
                type="text"
                style={{ backgroundColor: "grey", color: "white" }}
                onChange={onChangeInfo}
                name="username"
                value={info.username}
                readOnly
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

            <Button
              id="join-button"
              variant="outline-primary"
              type="button"
              onClick={onClickSubmit}
            >
              변경
            </Button>
          </Form>
        </>
      )}
    </div>
  );
}

export default UpdateFormComponent;
