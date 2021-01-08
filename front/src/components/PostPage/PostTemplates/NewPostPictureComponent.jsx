// import React from 'react'
// import { Button, Modal } from 'react-bootstrap';

// function NewPostPictureComponent({
//     setShow={}
//     show,
//     pictures,    setPictures
// }) {
//     const onChangePictures = (e) => {
//         const files = e.target.files;
//         let nope = false;
//         Object.values(files).forEach((file) => {
//           if (file.size > 1024 * 1024) {
//             nope = true;
//           }
//         });
//         if (nope) {
//           alert("파일 크기가 1mb보다 큽니다.");
//           return;
//         }
//         try {
//           const forPictures = [];
//           Object.values(files).forEach((file) => {
//             const reader = new FileReader();
//             reader.onload = () => {
//               forPictures.push({
//                 file,
//                 base64: reader.result,
//               });
//             };
//             reader.readAsDataURL(file);
//           });
//           setPictures(forPictures);
//           // console.log(files);
//           setShow(false);
//           setShow(true);
//         } catch (err) {
//           console.error(err);
//         }
//       };
//       return (
//         <Modal
//           className="category-modal"
//           show={show}
//           size="lg"
//           onHide={() => setShow(false)}
//           aria-labelledby="contained-modal-title-vcenter"
//           centered
//         >
//           <Modal.Header closeButton>
//             <Modal.Title id="contained-modal-title-vcenter">사진 설정</Modal.Title>
//           </Modal.Header>
//           <Modal.Body>
//             <label htmlFor="category_img_upload" className="about-label">
//               <i className="far fa-file-image" />
//               &nbsp;파일 선택
//             </label>
//             <input
//               type="file"
//               accept="image/jpg"
//               className="category-upload"
//               id="category_img_upload"
//               multiple="multiple"
//               onChange={onChangePictures}
//             />
//             <div className="new-post-modal-thumbnails">
//               {pictures &&
//                 pictures.length > 0 &&
//                 pictures.map((picture) => (
//                   <img key={UUID()} src={picture.base64} alt="썸네일" />
//                 ))}
//             </div>
//           </Modal.Body>
//           <Modal.Footer>
//             <Button variant="outline-primary">추가</Button>
//             <Button
//               variant="outline-danger"
//               onClick={() => {
//                 setShow(false);
//               }}
//             >
//               닫기
//             </Button>
//           </Modal.Footer>
//         </Modal>
//       );
// }

// export default NewPostPictureComponent
