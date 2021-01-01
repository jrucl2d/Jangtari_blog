import React, { useRef, useEffect } from 'react'
import { useSelector } from 'react-redux';
import { Link } from 'react-router-dom';

function SideMenuComponent({ isToggle }) {
    const menuRef = useRef(null);
    useEffect(() => {
        if (isToggle) {
            menuRef.current.classList.add("show")
        } else {
            menuRef.current.classList.remove("show")
        }
    }, [isToggle])

    const { categories } = useSelector(
        (state) => state.categoryReducer
    );
    return (
        <div className="side-bar" ref={menuRef}>
            <h2>장따리의 똥글</h2>
            <ul className="side-bar-user">
                <li><Link to="/about">Who is '장딱'</Link></li>
                <li><Link to="/loginForm">로그인</Link></li>
                <li><Link to="/joinForm">회원가입</Link></li>
            </ul>
            <h2>카테고리</h2>
            <ul className="side-bar-list">
                {categories && categories.map((v) => (
                    <li key={v.id}><Link to="/#">{v.name}</Link></li>
                ))}
            </ul>
        </div>
    )
}

export default SideMenuComponent
