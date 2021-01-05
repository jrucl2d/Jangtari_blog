import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";

function Template0() {
  const { post } = useSelector((state) => state.postReducer);
  const [showingPost, setShowingPost] = useState([]);
  const [nowPicture, setNowPicture] = useState(0);
  useEffect(() => {
    if (post && post.content) {
      setShowingPost(
        post.content.split(/(?:\r\n|\r|\n)/g).filter((v) => v !== "")
      );
    }
    // eslint-disable-next-line
  }, []);

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
    <>
      <h1 className="post-title">{post && post.title}</h1>
      <div className="post-main-box template0">
        <div className="post-main-content">
          <div className="post-left">
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
              {/* {post && post.pictures && post.pictures[nowPicture].picture} */}
              <div className="image-backgound">
                {post && post.pictures && post.pictures[0].picture === null ? (
                  "이미지가 없습니다."
                ) : (
                  <>
                    {" "}
                    <div className="image-to-left" onClick={onClickLeftIamge}>
                      <i className="fas fa-arrow-left"></i>
                    </div>
                    <div className="image-to-right" onClick={onClickRightIamge}>
                      <i className="fas fa-arrow-right"></i>
                    </div>
                    <div className="image-numbering">
                      {post &&
                        post.pictures &&
                        post.pictures.map((v, i) => (
                          <div
                            onClick={() => onClickPictureButton(i)}
                            className={
                              nowPicture === i
                                ? "now-picture"
                                : "no-now-picture"
                            }
                            key={v.picture}
                          ></div>
                        ))}
                    </div>
                  </>
                )}
              </div>
            </div>
            <ul className="post-comment-box">
              <h3>댓글</h3>
              {post &&
                post.comments &&
                post.comments.map((v) => (
                  <li key={v.commentId}>
                    {v.recomment !== null &&
                      Array(v.recomment.split("-").length)
                        .fill()
                        .map((v, i) => (
                          <span key={i} className="post-comment-blank"></span>
                        ))}
                    {v.recomment !== null ? "┖ " : "-"}
                    {v.nickName} : {v.comment}
                  </li>
                ))}
            </ul>
          </div>
          <div className="post-right">
            <div className="post-body">
              {showingPost.map((v, i) => (
                <div className="post-content-lines" key={i}>
                  {v}
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default Template0;
