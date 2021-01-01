import React from "react";
import "./CategoryComponent.css";

function CategoryComponent({ category }) {
  const onMouseOverPicture = (e) => {};
  const onMouseLeavePicture = (e) => {};
  return (
    <div
      className="category-box"
      onMouseOver={onMouseOverPicture}
      onMouseLeave={onMouseLeavePicture}
    >
      <h2 className="category-name">{category.name}</h2>
    </div>
  );
}

export default CategoryComponent;
