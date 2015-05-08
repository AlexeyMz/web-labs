
function post(path, params, method) {
    method = method || "post"; // Set method to post by default if not specified.

    // The rest of this code assumes you are not using a library.
    // It can be made less wordy if you use one.
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", path);

    for(var key in params) {
        if(params.hasOwnProperty(key)) {
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", params[key]);

            form.appendChild(hiddenField);
        }
    }

    document.body.appendChild(form);
    form.submit();
}

/***
 * @param options: {
 *    method: 'get'|'post'|'put'|'delete'|...
 *    url: string;
 *    data: object|string|any;
 *    success: (response: string) => void;
 * }
 */
function ajax(options) {
    var xmlhttp;
    // compatible with IE7+, Firefox, Chrome, Opera, Safari
    xmlhttp = new XMLHttpRequest();
    if (options.success) {
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                options.success(xmlhttp.responseText);
            }
        };
    }

    var params;
    if (typeof options.data === "object") {
        params = "";
        for (var key in options.data) {
            if (options.data.hasOwnProperty(key) && typeof options.data[key] !== "undefined") {
                if (params.length > 0) { params += "&"; }
                params += key + "=" + encodeURIComponent("" + options.data[key]);
            }
        }
    } else {
        params = "" + options.data;
    }

    options.method = options.method.toLowerCase();
    var hasPayload = options.method === "post" || options.method === "put";

    if (!hasPayload && params.length > 0) {
        options.url += "?" + params;
    }
    xmlhttp.open(options.method, options.url, true);
    if (hasPayload) {
        xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    }
    xmlhttp.send(hasPayload ? params : undefined);
}

function createElement(parent, tag, textContent) {
    var elem = document.createElement(tag);
    if (textContent) { elem.textContent = textContent; }
    if (parent) { parent.appendChild(elem); }
    return elem;
}

function prepend(parent, child) {
    if (parent.firstChild) {
        parent.insertBefore(child, parent.firstChild);
    } else {
        parent.appendChild(child);
    }
}
