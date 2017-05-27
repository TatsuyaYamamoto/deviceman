export function joinUserId(value) {
    return required(value) && regexp(value, /^[!-~]+$/);
}

export function joinUserAddress(value) {
    return required(value) && regexp(value, /^[a-z0-9\+\-_]+(\.[a-z0-9\+\-_]+)*@([a-z0-9\-]+\.)+[a-z]{2,6}$/);
}


function required(text) {
    return !!text;
}

function regexp(text, regexp) {
    return regexp.test(text);
}
