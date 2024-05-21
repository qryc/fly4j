var check = false;

function dis() {
    check = true;
}


function AllFiles() {
    for (var x = 0; x < document.FileList.elements.length; x++) {
        var y = document.FileList.elements[x];
        var ytr = y.parentNode.parentNode;
        var check = document.FileList.selall.checked;
        if (y.name == 'selfile' && ytr.style.display != 'none') {
            if (y.disabled != true) {
                y.checked = check;
                if (y.checked == true) ytr.className = 'checked';
                else ytr.className = 'mouseout';
            }
        }
    }
}
