import React from "react";
import { useSelector } from "react-redux";

function Template1() {
  const { post } = useSelector((state) => state.postReducer);

  return (
    <>
      <h1>{post && post.title}</h1>
      <h2>{post && post.content}</h2>
    </>
  );
}

export default Template1;
