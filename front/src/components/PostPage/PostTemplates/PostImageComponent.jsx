import React, { useState } from "react";
import { useSelector } from "react-redux";

function PostImageComponent() {
  const { post } = useSelector((state) => state.postReducer);
  const [nowPicture, setNowPicture] = useState(0);

  const onClickPictureButton = (number) => {
    setNowPicture(+number);
  };
  const onClickLeftIamge = (e) => {
    setNowPicture(
      nowPicture - 1 < 0
        ? post && post.pictures && post.pictures.length - 1
        : nowPicture - 1
    );
  };
  const onClickRightIamge = (e) => {
    setNowPicture(
      nowPicture + 1 > (post && post.pictures && post.pictures.length - 1)
        ? 0
        : nowPicture + 1
    );
  };
  return (
    <div
      className="post-image template0"
      style={{
        backgroundImage:
          post &&
          post.pictures &&
          post.pictures[0].picture !== null &&
          `url(${post.pictures[nowPicture].picture})`,
      }}
    >
      {window.innerWidth > 767 ? (
        <div className="image-backgound">
          {post && post.pictures && post.pictures[0].picture === null ? (
            "이미지가 없습니다."
          ) : (
            <>
              <div className="image-to-left" onClick={onClickLeftIamge}>
                <i className="fas fa-arrow-left"></i>
              </div>
              <div className="image-to-right" onClick={onClickRightIamge}>
                <i className="fas fa-arrow-right"></i>
              </div>
              <Numbering
                post={post}
                onClickPictureButton={onClickPictureButton}
                nowPicture={nowPicture}
              />
            </>
          )}
        </div>
      ) : (
        <>
          <div
            className="image-to-left-mobile"
            onClick={onClickLeftIamge}
          ></div>
          <div
            className="image-to-right-mobile"
            onClick={onClickRightIamge}
          ></div>
          {post && post.pictures && post.pictures[0].picture === null ? (
            "이미지가 없습니다."
          ) : (
            <Numbering
              post={post}
              onClickPictureButton={onClickPictureButton}
              nowPicture={nowPicture}
            />
          )}
        </>
      )}
    </div>
  );
}

export default PostImageComponent;

function Numbering({ post, onClickPictureButton, nowPicture }) {
  return (
    <div className="image-numbering">
      {post &&
        post.pictures &&
        post.pictures.map((v, i) => (
          <div
            onClick={() => onClickPictureButton(i)}
            className={nowPicture === i ? "now-picture" : "no-now-picture"}
            key={v.picture}
          ></div>
        ))}
    </div>
  );
}
