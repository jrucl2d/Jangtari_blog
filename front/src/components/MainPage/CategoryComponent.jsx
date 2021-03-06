import React from "react";
import { Link } from "react-router-dom";
import "./CategoryComponent.css";

function CategoryComponent({ category }) {
  return (
    <Link to={`/category/${category.id}/${category.name}`}>
      <div
        className="category-box"
        style={{
          backgroundImage: `url(${category.picture})`,
        }}
      >
        <h2 className="category-name">{category.name}</h2>
      </div>
    </Link>
  );
}

export default CategoryComponent;
