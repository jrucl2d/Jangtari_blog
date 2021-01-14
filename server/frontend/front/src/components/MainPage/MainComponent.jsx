import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import CategoryComponent from "./CategoryComponent";
import LoadingComponent from "./LoadingComponent";
import ScrollContainer from "react-indiana-drag-scroll";

function MainComponent({ history }) {
  const { categories, error, loading } = useSelector(
    (state) => state.categoryReducer
  );
  const { error: jError, loading: jLoading, checkSuc } = useSelector(
    (state) => state.jangtariReducer
  );

  useEffect(() => {
    if (error || jError === "logoutError") {
      alert("에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
    }
    if (jError === "checkErr") {
      alert("비밀번호가 맞지 않습니다.");
    }
  }, [error, jError]);

  useEffect(() => {
    if (checkSuc) {
      history.push("/updateForm");
    }
    // eslint-disable-next-line
  }, [checkSuc]);

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
