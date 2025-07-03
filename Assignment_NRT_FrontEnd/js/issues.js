const issueBaseUrl = 'http://localhost:8080/api/issues';

function getAuthHeader() {
  const auth = localStorage.getItem('auth');
  if (!auth) {
    alert("Please login first.");
    window.location.href = 'index.html';
    return;
  }
  return {
    'Authorization': 'Basic ' + auth,
    'Content-Type': 'application/json'
  };
}

//  Issue Item
document.getElementById('issueForm').addEventListener('submit', async function(e) {
  e.preventDefault();

  const record = {
    item: { itemId: document.getElementById('issueItemId').value },
    issuedTo: document.getElementById('issuedTo').value,
    issueDate: document.getElementById('issueDate').value
  };

  const res = await fetch(`${issueBaseUrl}/issue`, {
    method: 'POST',
    headers: getAuthHeader(),
    body: JSON.stringify(record)
  });

  if (res.ok) {
    loadIssues();
    document.getElementById('issueForm').reset();
  } else {
    alert("Failed to issue item. Check item ID or login.");
  }
});

//  Return Item
document.getElementById('returnForm').addEventListener('submit', async function(e) {
  e.preventDefault();

  const id = document.getElementById('returnId').value;
  const condition = document.getElementById('returnCondition').value;

  const res = await fetch(`${issueBaseUrl}/return/${id}?condition=${encodeURIComponent(condition)}`, {
    method: 'POST',
    headers: getAuthHeader()
  });

  if (res.ok) {
    loadIssues();
    document.getElementById('returnForm').reset();
  } else {
    alert("Failed to return item. Check ID or login.");
  }
});

//  Load all issued records
async function loadIssues() {
  const res = await fetch(`${issueBaseUrl}/`, {
    headers: getAuthHeader()
  });

  if (!res.ok) {
    alert("Failed to load issue records. Please login again.");
    return;
  }

  const data = await res.json();
  const tbody = document.getElementById('issueTableBody');
  tbody.innerHTML = '';

  data.forEach(r => {
    tbody.innerHTML += `
      <tr>
        <td>${r.id}</td>
        <td>${r.item?.itemName || '-'}</td>
        <td>${r.issuedTo}</td>
        <td>${r.issueDate || ''}</td>
        <td>${r.returnDate || ''}</td>
        <td>${r.returnCondition || ''}</td>
      </tr>
    `;
  });
}

window.onload = loadIssues;
