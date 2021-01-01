import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getAllCategories } from "../modules/categoryReducer";

function MainComponent() {
  const { categories, loading, error } = useSelector(
    (state) => state.categoryReducer
  );
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(getAllCategories());
  }, []);
  return (
    <>
      <h1>장따리 똥글</h1>
      <div>메뉴</div>

      {loading && <div>로딩</div>}
      {categories && <div>카테고리들</div>}
      {error && <div>무언가 오류가 났누!</div>}
    </>
  );
}

export default MainComponent;
