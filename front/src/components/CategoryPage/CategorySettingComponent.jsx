import React, { useEffect, useRef, useState } from "react";
import { Button } from "react-bootstrap";
import { useSelector, useDispatch } from "react-redux";
import { deleteCategory } from "../../modules/categoryReducer";
import LoadingComponent from "../MainPage/LoadingComponent";
import CategoryUpdateModal from "./CategoryUpdateModal";
import "./CategorySettingStyle.css";
import CategoryAddModal from "./CategoryAddModal";

function CategorySettingComponent() {
  const dispatch = useDispatch();
  const colorRef = useRef([
    "primary",
    "secondary",
    "success",
    "warning",
    "info",
    "light",
  ]);
  const [updateModalShow, setUpdateModalShow] = useState(false);
  const [addModalShow, setAddModalShow] = useState(false);
  const [theCategory, setTheCategory] = useState({
    name: "",
    picture: "",
  });

  const { categories, error, loading } = useSelector(
    (state) => state.categoryReducer
  );

  useEffect(() => {
    if (error) {
      alert("에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
    }
  }, [error]);

  const onClickDelete = (e) => {
    if (!window.confirm("정말 삭제하시겠습니까?")) return;
    dispatch(deleteCategory(+e.target.name));
    setTimeout(() => {
      alert("삭제되었습니다.");
    }, 500);
  };

  return (
    <>
      {loading ? (
        <div className="main-loading">
          <LoadingComponent />
        </div>
      ) : (
        <>
          <ul className="category-list">
            {categories &&
              categories.map((v, i) => (
                <li key={v.id}>
                  <div>{v.name}</div>
                  <div>
                    <Button
                      onClick={() => {
                        setTheCategory({
                          id: v.id,
                          name: v.name,
                          picture: v.picture,
                        });
                        setUpdateModalShow(true);
                      }}
                      variant={`outline-${colorRef.current[i % 6]}`}
                    >
                      수정
                    </Button>
                    <Button
                      variant="outline-danger"
                      name={v.id}
                      onClick={onClickDelete}
                    >
                      삭제
                    </Button>
                  </div>
                </li>
              ))}
          </ul>
          <Button variant="outline-info" onClick={() => setAddModalShow(true)}>
            카테고리 추가
          </Button>
        </>
      )}
      <CategoryUpdateModal
        setModalShow={setUpdateModalShow}
        modalShow={updateModalShow}
        category={theCategory}
      />
      <CategoryAddModal
        setModalShow={setAddModalShow}
        modalShow={addModalShow}
      />
    </>
  );
}

export default CategorySettingComponent;
