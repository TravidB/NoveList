(function () {
    'use strict';

    var _onReady = null;

    function injectDOM() {
        var corner = document.createElement('div');
        corner.id = 'userCorner';
        corner.innerHTML =
            '<button class="user-corner-btn" id="signInBtn">Sign In</button>' +
            '<div class="user-corner-menu" id="userMenu" style="display:none;">' +
            '<button class="user-corner-btn" id="userBtn"></button>' +
            '<div class="user-dropdown" id="userDropdown" style="display:none;">' +
            '<button class="user-dropdown-item" id="logoutBtn">Logout</button>' +
            '</div></div>';
        document.body.appendChild(corner);

        var overlay = document.createElement('div');
        overlay.id = 'authOverlay';
        overlay.className = 'auth-overlay';
        overlay.style.display = 'none';
        overlay.innerHTML =
            '<div class="auth-modal">' +
            '<div class="auth-tabs">' +
            '<button class="auth-tab active" id="tabLogin">Login</button>' +
            '<button class="auth-tab" id="tabSignup">Sign Up</button>' +
            '</div>' +
            '<div id="authLoginPanel">' +
            '<div class="form-group"><label>Username</label><input type="text" id="authLoginUser" placeholder="Username" autocomplete="username"></div>' +
            '<div class="form-group"><label>Password</label><input type="password" id="authLoginPass" placeholder="Password" autocomplete="current-password"></div>' +
            '<p class="auth-error" id="authLoginErr"></p>' +
            '<button class="btn" id="authLoginBtn">Login</button>' +
            '</div>' +
            '<div id="authSignupPanel" style="display:none;">' +
            '<div class="form-group"><label>Username</label><input type="text" id="authSignupUser" placeholder="Choose a username" autocomplete="username"></div>' +
            '<div class="form-group"><label>Password</label><input type="password" id="authSignupPass" placeholder="Choose a password" autocomplete="new-password"></div>' +
            '<p class="auth-error" id="authSignupErr"></p>' +
            '<button class="btn" id="authSignupBtn">Create Account</button>' +
            '</div>' +
            '</div>';
        document.body.appendChild(overlay);

        document.getElementById('signInBtn').addEventListener('click', showModal);
        document.getElementById('userBtn').addEventListener('click', toggleDropdown);
        document.getElementById('logoutBtn').addEventListener('click', logout);
        overlay.addEventListener('click', function (e) { if (e.target === overlay) hideModal(); });
        document.getElementById('tabLogin').addEventListener('click', function () { switchTab('login'); });
        document.getElementById('tabSignup').addEventListener('click', function () { switchTab('signup'); });
        document.getElementById('authLoginBtn').addEventListener('click', submitLogin);
        document.getElementById('authSignupBtn').addEventListener('click', submitSignup);
        document.getElementById('authLoginPass').addEventListener('keydown', function (e) { if (e.key === 'Enter') submitLogin(); });
        document.getElementById('authSignupPass').addEventListener('keydown', function (e) { if (e.key === 'Enter') submitSignup(); });
        document.addEventListener('click', function (e) {
            var dd = document.getElementById('userDropdown');
            var btn = document.getElementById('userBtn');
            if (dd && e.target !== btn && !dd.contains(e.target)) dd.style.display = 'none';
        });
    }

    function showLoggedIn(username) {
        document.getElementById('signInBtn').style.display = 'none';
        document.getElementById('userMenu').style.display = 'block';
        document.getElementById('userBtn').textContent = username + ' ▾';
    }

    function showLoggedOut() {
        document.getElementById('signInBtn').style.display = 'block';
        document.getElementById('userMenu').style.display = 'none';
    }

    function showModal() {
        document.getElementById('authOverlay').style.display = 'flex';
        setTimeout(function () { document.getElementById('authLoginUser').focus(); }, 50);
    }

    function hideModal() {
        document.getElementById('authOverlay').style.display = 'none';
        document.getElementById('authLoginErr').textContent = '';
        document.getElementById('authSignupErr').textContent = '';
        document.getElementById('authLoginUser').value = '';
        document.getElementById('authLoginPass').value = '';
        document.getElementById('authSignupUser').value = '';
        document.getElementById('authSignupPass').value = '';
    }

    function switchTab(tab) {
        var isLogin = tab === 'login';
        document.getElementById('tabLogin').classList.toggle('active', isLogin);
        document.getElementById('tabSignup').classList.toggle('active', !isLogin);
        document.getElementById('authLoginPanel').style.display = isLogin ? '' : 'none';
        document.getElementById('authSignupPanel').style.display = isLogin ? 'none' : '';
        setTimeout(function () {
            document.getElementById(isLogin ? 'authLoginUser' : 'authSignupUser').focus();
        }, 50);
    }

    function toggleDropdown() {
        var dd = document.getElementById('userDropdown');
        dd.style.display = dd.style.display === 'none' ? 'block' : 'none';
    }

    async function submitLogin() {
        var username = document.getElementById('authLoginUser').value.trim();
        var password = document.getElementById('authLoginPass').value;
        var errEl = document.getElementById('authLoginErr');
        if (!username || !password) { errEl.textContent = 'Enter username and password.'; return; }
        errEl.textContent = '';
        var res = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username: username, password: password })
        });
        if (res.ok) {
            var data = await res.json();
            window.currentUser = data;
            hideModal();
            showLoggedIn(data.username);
            if (_onReady) _onReady();
        } else {
            errEl.textContent = 'Invalid username or password.';
        }
    }

    async function submitSignup() {
        var username = document.getElementById('authSignupUser').value.trim();
        var password = document.getElementById('authSignupPass').value;
        var errEl = document.getElementById('authSignupErr');
        if (!username || !password) { errEl.textContent = 'Enter username and password.'; return; }
        if (password.length < 4) { errEl.textContent = 'Password must be at least 4 characters.'; return; }
        errEl.textContent = '';
        var res = await fetch('/api/auth/signup', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username: username, password: password })
        });
        if (res.ok) {
            var data = await res.json();
            window.currentUser = data;
            hideModal();
            showLoggedIn(data.username);
            if (_onReady) _onReady();
        } else if (res.status === 409) {
            errEl.textContent = 'Username already taken.';
        } else {
            errEl.textContent = 'Sign up failed. Try again.';
        }
    }

    async function logout() {
        await fetch('/api/auth/logout', { method: 'POST' });
        window.currentUser = null;
        window.location.href = '/index.html';
    }

    window.initAuth = async function (options) {
        options = options || {};
        _onReady = options.onReady || null;
        var res = await fetch('/api/auth/me');
        if (res.ok) {
            var data = await res.json();
            window.currentUser = data;
            showLoggedIn(data.username);
            if (options.redirectIfLoggedIn) { window.location.href = '/browse.html'; return; }
            if (_onReady) _onReady();
        } else {
            window.currentUser = null;
            showLoggedOut();
            if (options.requireAuth) { window.location.href = '/index.html'; return; }
        }
    };

    document.addEventListener('DOMContentLoaded', injectDOM);
})();
