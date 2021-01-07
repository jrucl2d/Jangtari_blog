import React, { useRef, useEffect, useState } from "react";
import { Button, Modal } from "react-bootstrap";
import "../PostStyle.css";

function NewPostComponent({ location }) {
  const mainRef = useRef();
  useEffect(() => {
    mainRef.current.focus();
  }, []);

  const [show, setShow] = useState(false);
  const [info, setInfo] = useState({
    categoryId: location.pathname.split("/")[1],
    hashtags: null,
    title: "",
    post: "",
    template: 0,
  });

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
            placeholder="#해시태그, #해시태그, #해시태그, #해시태그, #해시태그"
          />
          <Button variant="outline-primary">저장</Button>
        </div>

        <textarea
          placeholder="게시글 본문"
          cols="30"
          rows="20"
          ref={mainRef}
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
