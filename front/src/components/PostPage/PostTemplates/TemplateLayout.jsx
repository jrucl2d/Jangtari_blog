import React, { useEffect } from "react";
import { Route } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { getOnePost } from "../../../modules/postReducer";
import "../PostStyle.css";

import Template0 from "./Template0";
import Template1 from "./Template1";
import LoadingComponent from "../../MainPage/LoadingComponent";
import TemplateMobile from "./TemplateMobile";
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
    console.log(window.innerHeight);
    // eslint-disable-next-line
  }, []);

  return (
    <div className="post-outer-box">
      {loading ? (
        <div className="main-loading">
          <LoadingComponent />
        </div>
      ) : (
        <>
          {window.innerWidth > 767 ? (
            <>
              <Route path="/post/:id/0" component={Template0} />
              <Route path="/post/:id/1" component={Template1} />
            </>
          ) : (
            <Route path="/post/:id" component={TemplateMobile} />
          )}
        </>
      )}
    </div>
  );
}

export default TemplateLayout;
