const API = "/api";

function getToken() {
    return localStorage.getItem("token");
}

function getUser() {
    const u = localStorage.getItem("user");
    return u ? JSON.parse(u) : null;
}

function saveSession(data) {
    localStorage.setItem("token", data.token);
    localStorage.setItem("user", JSON.stringify({
        id: data.id, name: data.name, email: data.email, role: data.role
    }));
}

function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}

// Makes any API call and adds the token automatically
async function apiCall(method, path, body) {
    const headers = { "Content-Type": "application/json" };
    const token = getToken();
    if (token) headers["Authorization"] = "Bearer " + token;

    const res = await fetch(API + path, {
        method: method,
        headers: headers,
        body: body ? JSON.stringify(body) : undefined
    });

    let data = null;
    try { data = await res.json(); } catch (e) { /* empty response */ }

    if (!res.ok) {
        const message = data && data.message
            ? data.message
            : "Something went wrong (status " + res.status + ")";
        throw new Error(message);
    }
    return data;
}

// Makes text safe to put inside HTML (stops script injection)
function escapeHtml(text) {
    const div = document.createElement("div");
    div.textContent = text == null ? "" : String(text);
    return div.innerHTML;
}

function showMessage(text, type) {
    const box = document.getElementById("message");
    box.innerHTML = '<div class="msg ' + type + '">' + escapeHtml(text) + '</div>';
}

function renderNav() {
    const user = getUser();
    let links = '<a href="index.html">Jobs</a>';

    if (user) {
        if (user.role === "STUDENT") {
            links += '<a href="student.html">My Applications</a>';
        }
        if (user.role === "RECRUITER" || user.role === "ADMIN") {
            links += '<a href="recruiter.html">Recruiter Dashboard</a>';
        }
        links += '<span class="user">' + escapeHtml(user.name) + ' (' + user.role + ')</span>';
        links += '<a href="#" onclick="logout(); return false;">Logout</a>';
    } else {
        links += '<a href="login.html">Login</a><a href="register.html">Register</a>';
    }

    document.getElementById("nav").innerHTML =
        '<div class="brand">JobPortal</div><div class="links">' + links + '</div>';
}