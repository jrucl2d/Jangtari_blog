import React, { useEffect, useRef, useState } from "react";
import { Button } from "react-bootstrap";
import { useSelector, useDispatch } from "react-redux";
import { deleteCategory } from "../../modules/categoryReducer";
import LoadingComponent from "../MainPage/LoadingComponent";
import CategorySettginModal from "./CategorySettginModal";
import "./CategorySettingStyle.css";

function CategorySettingComponent({ history }) {
  const dispatch = useDispatch();
  const colorRef = useRef([
    "primary",
    "secondary",
    "success",
    "warning",
    "info",
    "light",
    "dark",
  ]);
  const [modalShow, setModalShow] = useState(false);
  const [theCategory, setTheCategory] = useState({
    name: "",
    picture: "",
  });

  const { categories, error, loading } = useSelector(
    (state) => state.categoryReducer
  );
  // 카테고리 목록 불러오기
  useEffect(() => {
    if (localStorage.getItem("role") !== "ADMIN") {
      alert("오호! 그러면 안 돼요!");
      history.push("/");
    }
    // eslint-disable-next-line
  }, []);

  useEffect(() => {
    if (error) {
      alert("에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
    }
  }, [error]);

  const onClickDelete = (e) => {
    if (!window.confirm("정말 삭제하시겠습니까?")) return;
    dispatch(deleteCategory(+e.target.name));
    alert("삭제되었습니다.");
  };

  return (
    <>
      {loading ? (
        <div className="main-loading">
          <LoadingComponent />
        </div>
      ) : (
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
                      setModalShow(true);
                    }}
                    variant={`outline-${colorRef.current[i]}`}
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
      )}
      <CategorySettginModal
        setModalShow={setModalShow}
        modalShow={modalShow}
        category={theCategory}
      />
    </>
  );
}

export default CategorySettingComponent;
