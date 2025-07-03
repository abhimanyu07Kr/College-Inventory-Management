function login() {
  const username = document.getElementById("username").value.trim();
  const password = document.getElementById("password").value.trim();

  if (!username || !password) {
    alert("Please enter username and password.");
    return;
  }

  const credentials = btoa(username + ":" + password);

  // Call backend to get user role
  fetch("http://localhost:8080/api/user-role", {
    method: "GET",
    headers: {
      "Authorization": "Basic " + credentials
    }
  })
    .then(res => {
      if (!res.ok) throw new Error("Invalid username or password");
      return res.json();
    })
    .then(data => {
      const role = data.role;
      localStorage.setItem("auth", credentials); // save for later API calls

      if (role === "ROLE_ADMIN") {
        window.location.href = "admin-dashboard.html"; // redirect to admin page
      } else if (role === "ROLE_SUBADMIN") {
        document.getElementById("loginForm").style.display = "none";
        document.getElementById("dashboardLinks").style.display = "block";
      } else {
        alert("Unknown role: " + role);
      }
    })
    .catch(err => {
      alert("Login failed: " + err.message);
    });
}

// Auto-login check
// window.onload = function () {
//   const auth = localStorage.getItem("auth");
//   if (!auth) return;

//   fetch("http://localhost:8080/api/user-role", {
//     method: "GET",
//     headers: {
//       "Authorization": "Basic " + auth
//     }
//   })
//     .then(res => {
//       if (!res.ok) throw new Error("Invalid session");
//       return res.json();
//     })
//     .then(data => {
//       const role = data.role;
//       if (role === "ROLE_ADMIN") {
//         window.location.href = "admin-dashboard.html";
//       } else if (role === "ROLE_SUBADMIN") {
//         document.getElementById("loginForm").style.display = "none";
//         document.getElementById("dashboardLinks").style.display = "block";
//       }
//     })
//     .catch(() => {
//       localStorage.removeItem("auth");
//     });
// };
