import React, { useEffect, useRef, useState } from "react";
import { Button } from "react-bootstrap";
import { useSelector } from "react-redux";
import LoadingComponent from "../MainPage/LoadingComponent";
import CategorySettginModal from "./CategorySettginModal";
import "./CategorySettingStyle.css";

function CategorySettingComponent({ history }) {
  const colorRef = useRef([
    "primary",
    "secondary",
    "success",
    "warning",
    "danger",
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
