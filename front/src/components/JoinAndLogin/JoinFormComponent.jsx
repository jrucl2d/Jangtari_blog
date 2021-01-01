import React from 'react'
import './LoginAndJoin.css'
import { Form, Button } from 'react-bootstrap'

function JoinFormComponent() {
    return (
        <div>
            <h1>회원가입</h1>
            <Form className="join-login-form">
                <Form.Group controlId="username">
                    <Form.Label>아이디</Form.Label>
                    <Form.Control type="text" placeholder="아이디 입력" />
                </Form.Group>
                <Form.Group controlId="nickname">
                    <Form.Label>닉네임</Form.Label>
                    <Form.Control type="text" placeholder="닉네임 입력" />
                </Form.Group>
                <Form.Group controlId="password">
                    <Form.Label>비밀번호</Form.Label>
                    <Form.Control type="password" placeholder="비밀번호 입력" />
                </Form.Group>
                <Form.Group controlId="password2">
                    <Form.Label>비밀번호 확인</Form.Label>
                    <Form.Control type="password" placeholder="비밀번호 재입력" />
                </Form.Group>
                <div className="join-password-check">
                    비밀번호가 맞지 않습니다.
                    </div>
                <Button id="join-button" variant="outline-primary" type="submit">
                    회원가입
                </Button>
            </Form>
        </div>
    )
}

export default JoinFormComponent
