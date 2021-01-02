import React, { useEffect, useState } from "react";
import { Button } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { getJangTtak } from "../../modules/jangtariReducer";
import LoadingComponent from "../MainPage/LoadingComponent";
import "./LoginAndJoin.css";

function AboutComponent() {
  const dispatch = useDispatch();
  const { info, error, loading } = useSelector(
    (state) => state.jangtariReducer
  );
  const [changeMode, setChangeMode] = useState(false);
  const [infoChange, setInfoChange] = useState({
    nickname: "",
    introduce: "",
    picture: "",
  });
  useEffect(() => {
    if (info === null) {
      dispatch(getJangTtak());
    }
    // eslint-disable-next-line
  }, []);
  useEffect(() => {
    if (info !== null) {
      setInfoChange({
        nickname: info.nickname,
        introduce: info.introduce,
        picture: info.picture,
      });
    }
  }, [info]);
  useEffect(() => {
    if (error) {
      alert("에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
    }
  }, [error]);

  const onClickChangeMode = (e) => {
    setChangeMode(!changeMode);
  };

  return (
    <div className="about-page">
      {loading ? (
        <div className="main-loading">
          <LoadingComponent />
        </div>
      ) : (
        <>
          {localStorage.getItem("role") &&
          localStorage.getItem("role") === "ADMIN" ? (
            <h1>
              정보를 변경하겠는가...
              {changeMode ? (
                <span className="change-buttons">
                  <Button variant="outline-primary" onClick={onClickChangeMode}>
                    바꿈잼
                  </Button>
                  <Button variant="outline-danger" onClick={onClickChangeMode}>
                    안바꿈잼
                  </Button>
                </span>
              ) : (
                <Button variant="outline-danger" onClick={onClickChangeMode}>
                  변경잼
                </Button>
              )}
            </h1>
          ) : (
            <h1>장딱, 그는 누구인가...</h1>
          )}

          <div className="about-content">
            {info && <img alt="장딱의 사진이 없어요" src={info.picture}></img>}

            <div className="infos">
              <h2>이름 : 장성균</h2>
              {!changeMode ? (
                <>
                  <h2>별명 : {info && info.nickname}</h2>
                  <h2>소개 : {info && info.introduce}</h2>
                </>
              ) : (
                <>
                  <div className="change-nick">
                    <label htmlFor="nickname">별명 :</label>
                    <input name="nickname" type="text" />
                  </div>
                  <h2>소개</h2>
                  <div className="change-intro">
                    <textarea
                      name="introduce"
                      id="introduce"
                      cols="30"
                      rows="10"
                    ></textarea>
                  </div>
                </>
              )}
            </div>
          </div>
        </>
      )}
    </div>
  );
}

export default AboutComponent;
