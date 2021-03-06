import React, { useRef, useState } from "react";
import "./MainPage/MainComponent.css";
import MainComponent from "./MainPage/MainComponent";
import SideMenuComponent from "./MainPage/SideMenuComponent";
import { Route, Link } from "react-router-dom";
import LoginFormComponent from "./JoinAndLogin/LoginFormComponent";
import JoinFormComponent from "./JoinAndLogin/JoinFormComponent";
import AboutComponent from "./JoinAndLogin/AboutComponent";
import CategorySettingComponent from "./CategoryPage/CategorySettingComponent";
import PostListComponent from "./PostPage/PostListComponent";
import TemplateLayout from "./PostPage/PostTemplates/TemplateLayout";
import NewPostComponent from "./PostPage/PostTemplates/NewPostComponent";
import UpdatePostComponent from "./PostPage/PostTemplates/UpdatePostComponent";
import UpdateFormComponent from "./JoinAndLogin/UpdateFormComponent";

function LayoutComponent() {
  const menuButtonRef = useRef(null);
  const [isToggle, setIsToggle] = useState(false);

  const onClickMenuButton = (e) => {
    menuButtonRef.current.classList.toggle("active-1");
    setIsToggle(!isToggle);
  };

  return (
    <div className="main-body">
      <header>
        <h1 className="main-title">
          <Link to="/">장따리 똥글</Link>
        </h1>
        <button
          className="menu-trigger"
          ref={menuButtonRef}
          onClick={onClickMenuButton}
        >
          <span></span>
          <span></span>
          <span></span>
        </button>
        <SideMenuComponent
          isToggle={isToggle}
          onClickMenuButton={onClickMenuButton}
        />
      </header>
      <Route exact path="/" component={MainComponent} />
      <Route exact path="/about" component={AboutComponent} />
      <Route exact path="/loginForm" component={LoginFormComponent} />
      <Route exact path="/joinForm" component={JoinFormComponent} />
      <Route exact path="/updateForm" component={UpdateFormComponent} />
      <Route
        exact
        path="/update/category"
        component={CategorySettingComponent}
      />
      <Route exact path="/category/:id/:name" component={PostListComponent} />
      <Route path="/post/:id" component={TemplateLayout} />
      <Route
        path="/category/:categoryId/add/post"
        component={NewPostComponent}
      />
      <Route
        path="/category/:categoryId/update/post"
        component={UpdatePostComponent}
      />
      <footer>
        <div className="footer-icons">
          <a
            target="_blank"
            rel="noreferrer"
            href="https://www.facebook.com/profile.php?id=100005432100050"
          >
            <img src="/fb.png" alt="페이스북" />
          </a>
          <a target="_blank" rel="noreferrer" href="https://www.instagram.com/">
            <img src="/ig.png" alt="인스타" />
          </a>
          <a target="_blank" rel="noreferrer" href="https://naver.com">
            <img src="/kakao.png" alt="카카오톡" />
          </a>
          <a
            target="_blank"
            rel="noreferrer"
            href="https://www.github.com/jrucl2d"
          >
            <img src="/github.png" alt="깃헙" />
          </a>
        </div>
        <div className="footer-introduce">
          <div>
            Made by &nbsp;
            <a
              target="_blank"
              rel="noreferrer"
              href="https://www.github.com/jrucl2d"
            >
              Yusegonggeun
            </a>
          </div>
          <div>Contact to Yu : 010-2578-4068</div>
        </div>
      </footer>
    </div>
  );
}

export default LayoutComponent;
