import React from 'react'
import './LoginAndJoin.css'
import { Form, Button } from 'react-bootstrap'

function LoginFormComponent() {
    return (
        <div>
            <h1>로그인</h1>
            <Form className="join-login-form">
                <Form.Group controlId="formBasicEmail">
                    <Form.Label>아이디</Form.Label>
                    <Form.Control type="text" placeholder="아이디 입력" />
                </Form.Group>

                <Form.Group controlId="formBasicPassword">
                    <Form.Label>비밀번호</Form.Label>
                    <Form.Control type="password" placeholder="비밀번호 입력" />
                </Form.Group>
                <Button variant="outline-primary" type="submit">
                    로그인
                </Button>
                <Button variant="outline-success" type="submit">
                    회원가입
                </Button>
            </Form>
        </div>
    )
}

export default LoginFormComponent
