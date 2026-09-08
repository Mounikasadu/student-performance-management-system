const API_BASE = 'http://localhost:8080/api';

let authHeader = null;

// ---------- LOGIN ----------

function login() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const statusEl = document.getElementById('login-status');

    if (!email || !password) {
        statusEl.textContent = 'Enter both email and password.';
        statusEl.className = 'status error';
        return;
    }

    const candidateAuth = 'Basic ' + btoa(email + ':' + password);

    fetch(`${API_BASE}/students`, {
        headers: { 'Authorization': candidateAuth }
    })
    .then(response => {
        if (response.status === 401) {
            throw new Error('Incorrect email or password.');
        }
        authHeader = candidateAuth;
        statusEl.textContent = '';

        document.getElementById('login-view').classList.add('hidden');
        document.getElementById('app-content').classList.remove('hidden');
        document.getElementById('session-info').textContent = 'Signed in as ' + email;

        loadStudents();
        loadSubjects();
        loadGrades();
    })
    .catch(err => {
        statusEl.textContent = err.message;
        statusEl.className = 'status error';
    });
}

function logout() {
    authHeader = null;
    document.getElementById('login-view').classList.remove('hidden');
    document.getElementById('app-content').classList.add('hidden');
    document.getElementById('session-info').textContent = 'Not signed in';
    document.getElementById('login-status').textContent = '';
}

// ---------- TAB SWITCHING ----------

document.querySelectorAll('.tab').forEach(tabBtn => {
    tabBtn.addEventListener('click', () => {
        document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
        document.querySelectorAll('.tab-panel').forEach(p => p.classList.add('hidden'));

        tabBtn.classList.add('active');
        document.getElementById('tab-' + tabBtn.dataset.tab).classList.remove('hidden');

        if (tabBtn.dataset.tab === 'students') loadStudents();
        if (tabBtn.dataset.tab === 'subjects') loadSubjects();
        if (tabBtn.dataset.tab === 'grades') loadGrades();
        if (tabBtn.dataset.tab === 'analytics') loadAnalytics();
    });
});

// ---------- SHARED HELPER ----------

function authFetch(path, options = {}) {
    options.headers = Object.assign({}, options.headers, {
        'Authorization': authHeader,
        'Content-Type': 'application/json'
    });
    return fetch(`${API_BASE}${path}`, options).then(res => {
        if (!res.ok) {
            return res.json().then(body => { throw new Error(body.error || 'Request failed'); });
        }
        return res.status === 204 ? null : res.json();
    });
}

// ---------- STUDENTS ----------

function loadStudents() {
    authFetch('/students').then(students => {
        document.getElementById('count-students').textContent = students.length;
        const body = document.getElementById('students-body');
        if (students.length === 0) {
            body.innerHTML = '<tr><td colspan="5" class="empty">No students yet.</td></tr>';
            return;
        }
        body.innerHTML = students.map(s => `
            <tr>
                <td>${s.id}</td>
                <td>${s.name}</td>
                <td>${s.rollNumber}</td>
                <td>${s.className}</td>
                <td><button class="link-danger" onclick="deleteStudent(${s.id})">Delete</button></td>
            </tr>
        `).join('');
    }).catch(err => alert(err.message));
}

function createStudent(event) {
    event.preventDefault();
    const student = {
        name: document.getElementById('s-name').value,
        rollNumber: document.getElementById('s-roll').value,
        className: document.getElementById('s-class').value
    };
    authFetch('/students', { method: 'POST', body: JSON.stringify(student) })
        .then(() => { event.target.reset(); loadStudents(); })
        .catch(err => alert(err.message));
}

function deleteStudent(id) {
    if (!confirm('Delete this student?')) return;
    authFetch(`/students/${id}`, { method: 'DELETE' })
        .then(() => loadStudents())
        .catch(err => alert(err.message));
}

// ---------- SUBJECTS ----------

function loadSubjects() {
    authFetch('/subjects').then(subjects => {
        document.getElementById('count-subjects').textContent = subjects.length;
        const body = document.getElementById('subjects-body');
        if (subjects.length === 0) {
            body.innerHTML = '<tr><td colspan="2" class="empty">No subjects yet.</td></tr>';
            return;
        }
        body.innerHTML = subjects.map(s => `<tr><td>${s.id}</td><td>${s.name}</td></tr>`).join('');
    }).catch(err => alert(err.message));
}

function createSubject(event) {
    event.preventDefault();
    const subject = { name: document.getElementById('sub-name').value };
    authFetch('/subjects', { method: 'POST', body: JSON.stringify(subject) })
        .then(() => { event.target.reset(); loadSubjects(); })
        .catch(err => alert(err.message));
}

// ---------- GRADES ----------

function loadGrades() {
    authFetch('/grades').then(grades => {
        document.getElementById('count-grades').textContent = grades.length;
        const body = document.getElementById('grades-body');
        if (grades.length === 0) {
            body.innerHTML = '<tr><td colspan="6" class="empty">No grades yet.</td></tr>';
            return;
        }
        body.innerHTML = grades.map(g => `
            <tr>
                <td>${g.id}</td>
                <td>${g.student.name}</td>
                <td>${g.subject.name}</td>
                <td>${g.marks}</td>
                <td>${g.examType}</td>
                <td>${g.examDate}</td>
            </tr>
        `).join('');
    }).catch(err => alert(err.message));
}

function createGrade(event) {
    event.preventDefault();
    const grade = {
        studentId: Number(document.getElementById('g-student').value),
        subjectId: Number(document.getElementById('g-subject').value),
        marks: Number(document.getElementById('g-marks').value),
        examType: document.getElementById('g-examtype').value,
        examDate: document.getElementById('g-examdate').value
    };
    authFetch('/grades', { method: 'POST', body: JSON.stringify(grade) })
        .then(() => { event.target.reset(); loadGrades(); })
        .catch(err => alert(err.message));
}

// ---------- ANALYTICS ----------

function renderBars(containerId, data) {
    const container = document.getElementById(containerId);
    const entries = Object.entries(data);
    if (entries.length === 0) {
        container.innerHTML = '<p class="empty">No data yet.</p>';
        return;
    }
    const max = Math.max(...entries.map(([, v]) => v), 100);
    container.innerHTML = entries.map(([name, avg]) => `
        <div class="bar-row">
            <div class="bar-row-top"><span>${name}</span><span>${avg.toFixed(1)}</span></div>
            <div class="bar-track"><div class="bar-fill" style="width:${(avg / max * 100).toFixed(0)}%"></div></div>
        </div>
    `).join('');
}

function loadAnalytics() {
    authFetch('/analytics/average-per-student')
        .then(data => renderBars('avg-student-bars', data))
        .catch(err => console.error(err));

    authFetch('/analytics/average-per-subject')
        .then(data => renderBars('avg-subject-bars', data))
        .catch(err => console.error(err));

    authFetch('/analytics/top-performer').then(data => {
        document.getElementById('top-performer').textContent =
            `${data.studentName} — ${data.marks} in ${data.subjectName}`;
    }).catch(() => {
        document.getElementById('top-performer').textContent = 'No data yet.';
    });

    authFetch('/analytics/pass-fail-summary').then(data => {
        document.getElementById('pass-fail-text').textContent =
            `${data.passed} passed / ${data.failed} failed`;
    }).catch(() => {
        document.getElementById('pass-fail-text').textContent = 'No data yet.';
    });
}
