import React, { useRef, useEffect, useState } from "react";
import { Button, Modal } from "react-bootstrap";
import queryString from "query-string";
import "../PostStyle.css";

function NewPostComponent({ location }) {
  const [show, setShow] = useState(false);
  const [info, setInfo] = useState({
    categoryId: location.pathname.split("/")[2],
    hashtags: "",
    title: "",
    post: "",
    template: queryString.parse(location.search).template,
  });
  const mainRef = useRef();
  useEffect(() => {
    mainRef.current.focus();
  }, []);

  const onChangeInfo = (e) => {
    setInfo({
      ...info,
      [e.target.name]: e.target.value,
    });
  };

  const onClickSave = () => {
    if (info.title === "" || info.post === "") {
      alert("제목과 본문을 작성해주세요.");
      return;
    }
    const sendingInfo = { ...info };
    if (info.hashtags === "") {
      sendingInfo.hashtags = null;
    } else {
      let sendingHahstags = info.hashtags.split(" ");
      let nope = false;
      sendingHahstags = sendingHahstags.filter((v) => {
        if (v !== "" && v[0] !== "#") {
          nope = true;
        }
        return v[0] === "#";
      });
      if (nope) {
        alert("해시태그 형식을 제대로 맞춰주세요.");
        return;
      }
      if (sendingHahstags.length > 5) {
        alert("해시태그는 5개까지만 허용됩니다.");
        return;
      }
      sendingInfo.hashtags = Array.from(new Set(sendingHahstags));
    }
    console.log(sendingInfo);
  };

  return (
    <div className="new-post-body">
      <PictureModal setShow={setShow} show={show} />
      <div className="new-post-images">
        <Button
          variant="outline-info"
          onClick={() => {
            setShow(true);
          }}
        >
          사진
        </Button>
        {/* <img src="https://source.unsplash.com/random//1280x720" alt="이미지" /> */}
      </div>
      <div className="new-post-main">
        <div>
          <input
            type="text"
            placeholder="#해시태그 #해시태그 #해시태그 #해시태그 #해시태그"
            onChange={onChangeInfo}
            name="hashtags"
            value={info.hashtags}
          />
          <Button variant="outline-primary" onClick={onClickSave}>
            저장
          </Button>
        </div>
        <input
          className="new-post-title"
          name="title"
          type="text"
          placeholder="제목"
          value={info.title}
          onChange={onChangeInfo}
        />
        <textarea
          placeholder="게시글 본문"
          cols="30"
          rows="20"
          name="post"
          value={info.post}
          ref={mainRef}
          onChange={onChangeInfo}
        ></textarea>
      </div>
    </div>
  );
}

export default NewPostComponent;

function PictureModal({ show, setShow }) {
  return (
    <Modal
      className="category-modal"
      show={show}
      size="lg"
      onHide={() => setShow(false)}
      aria-labelledby="contained-modal-title-vcenter"
      centered
    >
      <Modal.Header closeButton>
        <Modal.Title id="contained-modal-title-vcenter">사진 설정</Modal.Title>
      </Modal.Header>
      <Modal.Body>{}</Modal.Body>
      <Modal.Footer>
        <Button variant="outline-primary">추가</Button>
        <Button
          variant="outline-danger"
          onClick={() => {
            setShow(false);
          }}
        >
          닫기
        </Button>
      </Modal.Footer>
    </Modal>
  );
}
