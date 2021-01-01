import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getAllCategories } from "../modules/categoryReducer";
import LoadingComponent from "./LoadingComponent";
import "./MainComponent.css";

function MainComponent() {
  const { categories, loading, error } = useSelector(
    (state) => state.categoryReducer
  );
  const dispatch = useDispatch();

  // 카테고리 목록 불러오기
  useEffect(() => {
    dispatch(getAllCategories());
    // eslint-disable-next-line
  }, []);
  useEffect(() => {
    if (error) {
      alert("에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
    }
  }, [error]);

  return (
    <div className="main-body">
      <header>
        <h1 className="main-title">장따리 똥글</h1>
      </header>
      {loading && (
        <div className="main-loading">
          <LoadingComponent />
        </div>
      )}
      {categories && categories.map((v) => <div key={v.id}>{v.name}</div>)}
    </div>
  );
}

export default MainComponent;
