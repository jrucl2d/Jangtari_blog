import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import CategoryComponent from "./CategoryComponent";
import LoadingComponent from "./LoadingComponent";
import ScrollContainer from "react-indiana-drag-scroll";

function MainComponent() {
  const { categories, error, loading } = useSelector(
    (state) => state.categoryReducer
  );
  const { error: jError, success, loading: jLoading } = useSelector(
    (state) => state.jangtariReducer
  );

  useEffect(() => {
    if (error || jError === "logoutError") {
      alert("에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
    }
  }, [error, jError]);

  useEffect(() => {
    if (!success) return;
    if (success === "logoutSuc") {
      alert("로그아웃 했습니다.");
    }
  }, [success]);

  return (
    <>
      {loading || jLoading ? (
        <div className="main-loading">
          <LoadingComponent />
        </div>
      ) : (
        <ScrollContainer className="scroll-container category-container">
          {categories &&
            categories.map((v) => (
              <CategoryComponent key={v.id} category={v} />
            ))}
        </ScrollContainer>
      )}
    </>
  );
}

export default MainComponent;
