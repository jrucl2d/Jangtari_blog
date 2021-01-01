import React, { useRef, useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux';
import { Link } from 'react-router-dom';
import { getAllCategories } from '../../modules/categoryReducer';

function SideMenuComponent({ isToggle, onClickMenuButton }) {
    const dispatch = useDispatch();
    const { categories } = useSelector(
        (state) => state.categoryReducer
    );
    const menuRef = useRef(null);
    useEffect(() => {
        if (categories === null) {
            dispatch(getAllCategories());
        }
        // eslint-disable-next-line
    }, [])
    useEffect(() => {
        if (isToggle) {
            menuRef.current.classList.add("show")
        } else {
            menuRef.current.classList.remove("show")
        }
    }, [isToggle])


    return (
        <>
            <div className="side-bar" ref={menuRef}>
                <h2>장따리의 똥글</h2>
                <ul className="side-bar-user">
                    <li><Link onClick={() => onClickMenuButton()} to="/">메인으로</Link></li>
                    <li><Link onClick={() => onClickMenuButton()} to="/about">Who is '장딱'</Link></li>
                    <li><Link onClick={() => onClickMenuButton()} to="/loginForm">로그인</Link></li>
                    <li><Link onClick={() => onClickMenuButton()} to="/joinForm">회원가입</Link></li>
                </ul>
                <h2>카테고리</h2>
                <ul className="side-bar-list">
                    {categories && categories.map((v) => (
                        <li key={v.id}><Link to="/#">
                            {v.name}
                        </Link></li>
                    ))}
                </ul>
            </div>
            <div className="side-bar-curtain" onClick={() => onClickMenuButton()} />
        </>
    )
}

export default SideMenuComponent
