import React, { useEffect, useState } from "react";
import { Button } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { getJangTtak, setJangTtak } from "../../modules/jangtariReducer";
import LoadingComponent from "../MainPage/LoadingComponent";
import "./LoginAndJoin.css";

function AboutComponent() {
  const dispatch = useDispatch();
  const { info, error, loading } = useSelector(
    (state) => state.jangtariReducer
  );
  const [imageBase64, setImageBase64] = useState(""); // base64 정보
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
    setChangeMode(false);
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
    if (changeMode) {
      setImageBase64("");
      setInfoChange({
        nickname: info.nickname,
        introduce: info.introduce,
        picture: info.picture,
      });
    }
    setChangeMode(!changeMode);
  };

  const onChangeInfo = (e) => {
    setInfoChange({
      ...infoChange,
      [e.target.name]: e.target.value,
    });
  };

  const onChangeFile = (e) => {
    e.preventDefault();
    let reader = new FileReader();
    const file = e.target.files[0];
    try {
      reader.onloadend = () => {
        setInfoChange({
          ...infoChange,
          picture: file.name,
        });
        setImageBase64(reader.result);
      };
      reader.readAsDataURL(file);
    } catch (err) {
      console.error(err);
    }
  };

  const onClickSubmit = async (e) => {
    if (infoChange.nickname === "") {
      alert("닉네임을 적어주세요.");
      return;
    }
    dispatch(setJangTtak(infoChange));
    setTimeout(() => {
      alert("성공적으로 변경되었습니다.");
    }, 500);
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
              장딱, 그는 누구인가...
              {changeMode ? (
                <span className="change-buttons">
                  <Button variant="outline-primary" onClick={onClickSubmit}>
                    적용
                  </Button>
                  <Button variant="outline-danger" onClick={onClickChangeMode}>
                    취소
                  </Button>
                </span>
              ) : (
                <Button variant="outline-danger" onClick={onClickChangeMode}>
                  변경
                </Button>
              )}
            </h1>
          ) : (
            <h1>장딱, 그는 누구인가...</h1>
          )}

          <div className="about-content">
            <div className="about-picture">
              {info &&
                (imageBase64 === "" ? (
                  <img alt="장딱의 사진이 없어요" src={info.picture} />
                ) : (
                  <img alt="미리보기" src={imageBase64} />
                ))}
              {changeMode && (
                <div className="about-file">
                  <label htmlFor="profile_img_upload" className="about-label">
                    <i className="far fa-file-image" />
                    &nbsp;파일 선택
                  </label>
                  <input
                    type="file"
                    accept="image/jpg,image/png,image/jpeg,image/gif"
                    onChange={onChangeFile}
                    className="about-upload"
                    id="profile_img_upload"
                  />
                </div>
              )}
            </div>

            <div className="infos">
              <h2>장성균 A.K.A {info && info.nickname}</h2>
              {!changeMode ? (
                <textarea
                  className="infos-show-introduce"
                  cols="30"
                  rows="5"
                  readOnly
                  value={infoChange.introduce}
                ></textarea>
              ) : (
                <>
                  <div className="change-nick">
                    <label htmlFor="nickname">별명 :</label>
                    <input
                      onChange={onChangeInfo}
                      name="nickname"
                      type="text"
                      value={infoChange.nickname}
                    />
                  </div>
                  <h2 className="no-use">소개</h2>
                  <div className="change-intro">
                    <textarea
                      onChange={onChangeInfo}
                      name="introduce"
                      id="introduce"
                      cols="30"
                      rows="5"
                      value={infoChange.introduce}
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
