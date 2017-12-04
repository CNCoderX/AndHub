
function fnResize(size) {
    var body = document.getElementsByTagName("body")[0];
    if (body) {
        body.style.fontSize = size + "px"
    }
}

window.onload = function () {
    CodeMirror.modeURL = "code_mirror/mode/%N/%N.js";
    var config = {};
    config.readOnly = "nocursor";
    config.value = ICodeReview.getContent();
    config.theme = ICodeReview.getTheme();
    config.lineNumbers = !!ICodeReview.lineNumbers();
    config.lineWrapping = !!ICodeReview.lineWrapping();
    config.autofocus = false;
    config.dragDrop = false;
    config.fixedGutter = false;
    var editor = CodeMirror(document.body, config);

    var mode, mime;
    var file = ICodeReview.getFile();
    var info = CodeMirror.findModeByFileName(file);
    if (info) {
        mode = info.mode;
        mime = info.mime;
    }
    if (mode) {
        editor.setOption("mode", mime);
        CodeMirror.autoLoadMode(editor, mode);
    }
    fnResize(ICodeReview.getFontSize());
}