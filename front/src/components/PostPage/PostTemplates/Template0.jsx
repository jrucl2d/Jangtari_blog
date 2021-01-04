import React from "react";
import { useSelector } from "react-redux";

function Template0() {
  const { post } = useSelector((state) => state.postReducer);

  return (
    <div>
      <h1>{post && post.title}</h1>
      <h2>{post && post.content}</h2>
    </div>
  );
}

export default Template0;
