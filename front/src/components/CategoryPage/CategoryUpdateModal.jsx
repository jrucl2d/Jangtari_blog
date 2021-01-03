import React, { useState, useEffect } from "react";
import { Button, Modal } from "react-bootstrap";
import { useDispatch } from "react-redux";
import { updateCategory } from "../../modules/categoryReducer";

function CategoryUpdateModal({ setModalShow, modalShow, category }) {
  const dispatch = useDispatch();
  const [imageBase64, setImageBase64] = useState(""); // base64 정보
  const [infoChange, setInfoChange] = useState({
    id: 0,
    name: "",
    picture: "",
  });
  useEffect(() => {
    if (category !== null) {
      setInfoChange({
        id: category.id,
        name: category.name,
        picture: category.picture,
      });
    }
  }, [category]);

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

  const onChangeInfo = (e) => {
    setInfoChange({
      ...infoChange,
      name: e.target.value,
    });
  };
  const onClose = () => {
    setImageBase64("");
    setInfoChange({
      id: category.id,
      name: category.name,
      picture: category.picture,
    });
    setModalShow(false);
  };

  const onClickSubmit = async (e) => {
    if (infoChange.name === "") {
      alert("카테고리 제목을 적어주세요.");
      return;
    }
    dispatch(updateCategory(infoChange));
    setTimeout(() => {
      alert("성공적으로 변경되었습니다.");
      onClose();
    }, 500);
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
          카테고리 정보 수정
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="category-update-name">
          <label htmlFor="">카테고리 제목 :</label>
          <input
            type="text"
            name="name"
            value={infoChange.name}
            onChange={onChangeInfo}
          />
        </div>
        <div className="category-picture">
          <img
            src={imageBase64 === "" ? infoChange.picture : imageBase64}
            alt="카테고리 사진"
          />
          <label htmlFor="category_img_upload" className="about-label">
            <i className="far fa-file-image" />
            &nbsp;파일 선택
          </label>
          <input
            type="file"
            accept="image/jpg,image/png,image/jpeg,image/gif"
            className="category-upload"
            onChange={onChangeFile}
            id="category_img_upload"
          />
        </div>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="outline-primary" onClick={onClickSubmit}>
          수정
        </Button>
        <Button variant="outline-danger" onClick={onClose}>
          닫기
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default CategoryUpdateModal;
