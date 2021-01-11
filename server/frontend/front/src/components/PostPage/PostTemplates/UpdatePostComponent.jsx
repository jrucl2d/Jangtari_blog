import React, { useRef, useEffect, useState } from "react";
import { Button } from "react-bootstrap";
import queryString from "query-string";
import "../PostStyle.css";
import { v4 as uuid } from "uuid";
import { useDispatch, useSelector } from "react-redux";
import { getOnePost, updatePost } from "../../../modules/postReducer";
import LoadingComponent from "../../MainPage/LoadingComponent";

function UpdatePostComponent({ location, history }) {
  const dispatch = useDispatch();
  const { success, error, loading, post } = useSelector(
    (state) => state.postReducer
  );
  const [info, setInfo] = useState({
    id: null,
    hashtags: "",
    title: "",
    post: "",
    template: null,
  });
  const [pictures, setPictures] = useState([]);
  const [thumbnails, setThumbnails] = useState([]);
  const mainRef = useRef();

  useEffect(() => {
    if (post) return;
    const id = queryString.parse(location.search).id;
    dispatch(getOnePost(id));
    mainRef.current.focus();
    // eslint-disable-next-line
  }, []);

  useEffect(() => {
    if (!post) return;
    const tmpHashtags = post.hashtags.map((v) => "#" + v.hashtag);
    if (post.pictures && post.pictures[0] && post.pictures[0].picture) {
      setPictures(post.pictures.map((v) => v.picture));
      setThumbnails(post.pictures.map((v) => v.picture));
    }
    setInfo({
      ...info,
      id: queryString.parse(location.search).id,
      title: post.title,
      post: post.content,
      template: queryString.parse(location.search).template,
      hashtags: tmpHashtags.join(" "),
    });
    // eslint-disable-next-line
  }, [post]);

  useEffect(() => {
    if (!success) return;
    history.goBack();
  }, [success, history]);

  useEffect(() => {
    if (!error) return;
    alert("문제가 발생했습니다. 잠시 뒤에 다시 시도해주세요.");
  }, [error]);

  const onChangeInfo = (e) => {
    setInfo({
      ...info,
      [e.target.name]: e.target.value,
    });
  };

  const onClickSave = () => {
    if (info.title === "" || info.post === "") {
      alert("제목과 본문을 작성해주세요.");
      return;
    }
    const sendingInfo = { ...info };
    if (info.hashtags === "") {
      sendingInfo.hashtags = null;
    } else {
      let sendingHahstags = info.hashtags.split(" ");
      let nope = false;
      sendingHahstags = sendingHahstags.filter((v) => {
        if (v !== "" && v[0] !== "#") {
          nope = true;
        }
        return v[0] === "#";
      });
      if (nope) {
        alert("해시태그 형식을 제대로 맞춰주세요.");
        return;
      }
      if (sendingHahstags.length > 5) {
        alert("해시태그는 5개까지만 허용됩니다.");
        return;
      }
      sendingInfo.hashtags = Array.from(new Set(sendingHahstags)).map((v) =>
        v.substring(1, v.length)
      );
    }
    dispatch(updatePost(sendingInfo, pictures));
  };

  const onChangePictures = (e) => {
    const fileArr = e.target.files;

    let fileURLs = [];
    let nope = false;
    Object.values(fileArr).forEach((file) => {
      if (file.size > 1024 * 1024) {
        nope = true;
      }
    });
    setPictures([...pictures, ...Object.values(fileArr)]);

    if (nope) {
      alert("파일 크기가 1mb보다 큽니다.");
      return;
    }

    let file;
    let filesLength = fileArr.length > 10 ? 10 : fileArr.length;

    for (let i = 0; i < filesLength; i++) {
      file = fileArr[i];
      let reader = new FileReader();
      reader.onload = () => {
        fileURLs[i] = reader.result;
        setThumbnails([...thumbnails, ...fileURLs]);
      };
      reader.readAsDataURL(file);
    }
  };

  const onClickCancelPicture = (index) => {
    setThumbnails(thumbnails.filter((v, i) => i !== index));
    setPictures(pictures.filter((v, i) => i !== index));
  };

  return (
    <>
      {loading ? (
        <div className="main-loading">
          <LoadingComponent />
        </div>
      ) : (
        <div className="new-post-body">
          <div className="new-post-images">
            <label
              htmlFor="category_img_upload"
              className="about-label post-image-label"
            >
              <i className="far fa-file-image" />
              &nbsp;파일 선택
            </label>
            <input
              type="file"
              accept="image/jpg"
              className="category-upload"
              id="category_img_upload"
              multiple="multiple"
              onChange={onChangePictures}
            />
            <div className="new-post-thumbnails">
              {thumbnails &&
                thumbnails.map((t, i) => (
                  <img
                    key={uuid()}
                    onClick={() => onClickCancelPicture(i)}
                    src={t}
                    alt="썸네일"
                  />
                ))}
            </div>
          </div>
          <div className="new-post-main">
            <div>
              <input
                type="text"
                placeholder="#해시태그 #해시태그 #해시태그 #해시태그 #해시태그"
                onChange={onChangeInfo}
                name="hashtags"
                value={info.hashtags}
              />
              <Button variant="outline-primary" onClick={onClickSave}>
                저장
              </Button>
            </div>
            <input
              className="new-post-title"
              name="title"
              type="text"
              placeholder="제목"
              value={info.title}
              onChange={onChangeInfo}
            />
            <textarea
              placeholder="게시글 본문"
              cols="30"
              rows="20"
              name="post"
              value={info.post}
              ref={mainRef}
              onChange={onChangeInfo}
            ></textarea>
          </div>
        </div>
      )}
    </>
  );
}

export default UpdatePostComponent;
