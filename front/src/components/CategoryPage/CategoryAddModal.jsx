import React, { useState } from "react";
import { Button, Modal } from "react-bootstrap";
import { useDispatch } from "react-redux";
import { addCategory } from "../../modules/categoryReducer";

function CategoryAddModal({ setModalShow, modalShow }) {
  const dispatch = useDispatch();
  const [imageBase64, setImageBase64] = useState(""); // base64 정보
  const [infoChange, setInfoChange] = useState({
    name: "",
    picture: null,
  });

  const onChangeFile = (e) => {
    e.preventDefault();
    let reader = new FileReader();
    const file = e.target.files[0];
    if (file.size > 1024 * 1024) {
      alert("파일 사이즈가 1mb보다 큽니다.");
      return;
    }
    try {
      reader.onloadend = () => {
        setInfoChange({
          ...infoChange,
          picture: file,
        });
        setImageBase64(reader.result);
      };
      reader.readAsDataURL(file);
    } catch (err) {
      console.error(err);
    }
  };

  const onChangeInfo = (e) => {
    setInfoChange({
      ...infoChange,
      name: e.target.value,
    });
  };
  const onClose = () => {
    setImageBase64("");
    setInfoChange({
      name: "",
      picture: "",
    });
    setModalShow(false);
  };

  const onClickSubmit = async (e) => {
    if (infoChange.name === "") {
      alert("카테고리 제목을 적어주세요.");
      return;
    }
    dispatch(addCategory(infoChange, imageBase64));
    setModalShow(false);
  };

  return (
    <Modal
      className="category-modal"
      show={modalShow}
      size="lg"
      onHide={onClose}
      aria-labelledby="contained-modal-title-vcenter"
      centered
    >
      <Modal.Header closeButton>
        <Modal.Title id="contained-modal-title-vcenter">
          카테고리 추가
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="category-update-name">
          <label htmlFor="">카테고리 이름 :</label>
          <input
            type="text"
            name="name"
            placeholder="제목 입력"
            value={infoChange.name}
            onChange={onChangeInfo}
          />
        </div>
        <div className="category-picture">
          <img
            src={imageBase64 === "" ? infoChange.picture : imageBase64}
            alt="카테고리 사진 추가"
          />
          <label htmlFor="category_img_upload" className="about-label">
            <i className="far fa-file-image" />
            &nbsp;파일 선택
          </label>
          <input
            type="file"
            accept="image/jpg,image/jpeg"
            className="category-upload"
            onChange={onChangeFile}
            id="category_img_upload"
          />
        </div>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="outline-primary" onClick={onClickSubmit}>
          추가
        </Button>
        <Button variant="outline-danger" onClick={onClose}>
          닫기
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default CategoryAddModal;
