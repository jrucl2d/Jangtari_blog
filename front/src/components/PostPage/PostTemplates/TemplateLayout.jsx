import React, { useEffect } from "react";
import { Route } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { getOnePost } from "../../../modules/postReducer";

import Template0 from "./Template0";
import Template1 from "./Template1";
import LoadingComponent from "../../MainPage/LoadingComponent";
function TemplateLayout({ location }) {
  const dispatch = useDispatch();
  const { post, loading, error } = useSelector((state) => state.postReducer);

  useEffect(() => {
    if (error) {
      alert("에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
    }
  }, [error]);

  useEffect(() => {
    if (post) return;
    const id = location.pathname.split("/")[2];
    dispatch(getOnePost(id));
    // eslint-disable-next-line
  }, []);

  return (
    <div>
      {loading ? (
        <div className="main-loading">
          <LoadingComponent />
        </div>
      ) : (
        <>
          <Route path="/post/:id/0" component={Template0} />
          <Route path="/post/:id/1" component={Template1} />
        </>
      )}
    </div>
  );
}

export default TemplateLayout;
