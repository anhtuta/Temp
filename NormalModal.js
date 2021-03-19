import React from "react";
import Modal from "react-bootstrap/Modal";
import ButtonDefault from "../Buttons/ButtonDefault";
import "../../css/Modal.css";

const NormalModal = props => {
  const {
    show,
    customClass = "",
    size = "",
    onClose,
    onCancel,
    onSave,
    modalTitle = "",
    cancelButtonText = "",
    saveButtonText = "",
    children,
    isDelete = false,
    disabledSaveBtn = false,
    buttons
  } = props;

  return (
    <Modal
      show={show}
      onHide={onClose}
      backdrop="static"
      centered={true}
      dialogClassName={`custom-modal-wrapper ${size} ${customClass}`}
    >
      <Modal.Header closeButton>
        <Modal.Title>{modalTitle}</Modal.Title>
      </Modal.Header>
      <Modal.Body>{children}</Modal.Body>
      <Modal.Footer>
        {cancelButtonText && (
          <ButtonDefault
            type="clear-button"
            onClick={onCancel}
            text={cancelButtonText}
          />
        )}
        {buttons &&
          buttons.map(button => {
            return (
              <ButtonDefault
                key={button.key}
                type={button.type}
                onClick={button.onClick}
                text={button.text}
                disabled={button.disabled}
              />
            );
          })}
        {saveButtonText && (
          <ButtonDefault
            type={isDelete ? "delete-button" : "solid-button"}
            onClick={onSave}
            text={saveButtonText}
            disabled={disabledSaveBtn}
          />
        )}
      </Modal.Footer>
    </Modal>
  );
};

export default NormalModal;

/////
import React from "react";
import Modal from "react-bootstrap/Modal";
import ButtonDefault from "../Buttons/ButtonDefault";
import "../../css/Modal.css";

const ConfirmModal = props => {
  const {
    show,
    customClass = "",
    onClose,
    onCancel,
    onSave,
    modalTitle = "",
    bodyMessage = "",
    cancelButtonText = "",
    saveButtonText = "",
    isDelete = false,
    children
  } = props;

  return (
    <Modal
      show={show}
      onHide={onClose}
      backdrop="static"
      centered={true}
      dialogClassName={`custom-modal-wrapper ${customClass}`}
    >
      <Modal.Header closeButton>
        <Modal.Title>{modalTitle}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {bodyMessage}
        {children}
      </Modal.Body>
      <Modal.Footer>
        {cancelButtonText && (
          <ButtonDefault
            type="clear-button"
            onClick={onCancel}
            text={cancelButtonText}
          />
        )}
        {saveButtonText && (
          <ButtonDefault
            type={isDelete ? "delete-button" : "solid-button"}
            onClick={onSave}
            text={saveButtonText}
          />
        )}
      </Modal.Footer>
    </Modal>
  );
};

export default ConfirmModal;

