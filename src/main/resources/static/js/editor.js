import TipTapEditorController from './tiptap_editor_controller.js';

const tiptapEditorController = new TipTapEditorController("editor", "Hello World from TipTap!");

document.querySelector('#editor-submit-button').addEventListener('click', () => {
    document.querySelector('#form-title').value = document.querySelector('#title').value;
    document.querySelector('#form-content').value = tiptapEditorController.editor.getHTML();
    document.querySelector('#form-author').value = "defaultAuthor";
    //document.querySelector('#editor-form').submit();

    const form = document.querySelector('#editor-form');
    const data = new FormData(form);

    fetch(form.action, {
        method: form.method,
        body: data
    }).then(response => {
        if (response.ok) {
            alert('저장 성공');
        } else {
            alert('저장 실패');
        }

        for (const [key, value] of data.entries()) {
            console.log(key, ": ", value);
        }
    });
});