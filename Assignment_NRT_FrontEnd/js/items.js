const baseUrl = 'http://localhost:8080/api/items';

//  Get Auth Header
function getAuthHeader() {
  const auth = localStorage.getItem('auth');
  if (!auth) {
    alert("Please login first");
    window.location.href = 'index.html';
    return;
  }
  return {
    'Authorization': 'Basic ' + auth,
    'Content-Type': 'application/json'
  };
}

//  Load Items


let allItems = [];
let currentPage = 1;
const itemsPerPage = 10;

function getAuthHeader() {
  const auth = localStorage.getItem('auth');
  if (!auth) {
    alert("Please login first");
    window.location.href = 'index.html';
    return;
  }
  return {
    'Authorization': 'Basic ' + auth,
    'Content-Type': 'application/json'
  };
}

async function loadItems() {
  const res = await fetch(`${baseUrl}/`, {
    method: 'GET',
    headers: getAuthHeader()
  });

  if (!res.ok) {
    alert("Failed to load items.");
    return;
  }

  allItems = await res.json();
  currentPage = 1;
  renderPaginatedItems();
  renderPaginationControls();
}

function renderPaginatedItems() {
  const tbody = document.getElementById('itemsTableBody');
  tbody.innerHTML = '';

  const start = (currentPage - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  const items = allItems.slice(start, end);

  items.forEach(item => {
    tbody.innerHTML += `
      <tr>
        <td>${item.itemId}</td>
        <td>${item.itemName}</td>
        <td>${item.itemCategory}</td>
        <td>${item.itemLocation}</td>
        <td>${item.itemCondition}</td>
        <td>
         
          <button class="btn btn-success btn-sm ms-1" onclick="increaseQuantity(${item.itemId})">+</button>
           ${item.itemQuantity}
          <button class="btn btn-warning btn-sm ms-1" onclick="decreaseQuantity(${item.itemId})">-</button>
        </td>
        <td>
          <button class="btn btn-primary btn-sm me-2" onclick="editItem(${item.itemId})">Edit</button>
          <button class="btn btn-danger btn-sm" onclick="deleteItem(${item.itemId})">Delete</button>
        </td>
      </tr>
    `;
  });
}
function renderPaginationControls() {
  const pagination = document.getElementById('paginationControls');
  if (!pagination) return;

  pagination.innerHTML = '';
  const totalPages = Math.ceil(allItems.length / itemsPerPage);

  for (let i = 1; i <= totalPages; i++) {
    const btn = document.createElement('button');
    btn.className = `btn btn-sm ${i === currentPage ? 'btn-primary' : 'btn-outline-primary'} mx-1`;
    btn.textContent = i;
    btn.onclick = () => {
      currentPage = i;
      renderPaginatedItems();
      renderPaginationControls();
    };
    pagination.appendChild(btn);
  }
}

//  Add or Update Item
document.getElementById('addItemForm').addEventListener('submit', async function (e) {
  e.preventDefault();

  const item = {
    itemName: document.getElementById('itemName').value,
    itemCategory: document.getElementById('itemCategory').value,
    itemLocation: document.getElementById('itemLocation').value,
    itemCondition: document.getElementById('itemCondition').value,
    itemQuantity: document.getElementById('itemQuantity').value,
  };

  const id = window.editingItemId;
  const url = id ? `${baseUrl}/${id}` : `${baseUrl}/`;
  const method = id ? 'PUT' : 'POST';

  const res = await fetch(url, {
    method: method,
    headers: getAuthHeader(),
    body: JSON.stringify(item)
  });

  if (res.ok) {
    loadItems();
    document.getElementById('addItemForm').reset();
    document.querySelector('#addItemForm button').innerText = 'Add Item';
    window.editingItemId = null;
  } else {
    alert("Failed to " + (id ? "update" : "add") + " item.");
  }
});

//  Edit Item
async function editItem(id) {
  const res = await fetch(`${baseUrl}/`, {
    method: 'GET',
    headers: getAuthHeader()
  });

  if (!res.ok) {
    alert("Failed to fetch items.");
    return;
  }

  const items = await res.json();
  const item = items.find(i => i.itemId === id);
  if (!item) return;

  document.getElementById('itemName').value = item.itemName;
  document.getElementById('itemCategory').value = item.itemCategory;
  document.getElementById('itemLocation').value = item.itemLocation;
  document.getElementById('itemCondition').value = item.itemCondition;
  document.getElementById('itemQuantity').value = item.itemQuantity;

  window.editingItemId = id;
  document.querySelector('#addItemForm button').innerText = 'Update Item';
}

//  Delete Item
async function deleteItem(id) {
  if (confirm("Are you sure you want to delete this item?")) {
    const res = await fetch(`${baseUrl}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeader()
    });

    if (res.ok) {
      loadItems();
    } else if (res.status === 409) {
      alert("Item is linked to issued record and cannot be deleted.");
    } else {
      alert("Delete failed.");
    }
  }
}

//  Increase Quantity
async function increaseQuantity(id) {
  const res = await fetch(`${baseUrl}/increase/${id}`, {
    method: 'POST',
    headers: getAuthHeader()
  });

  if (res.ok) {
    loadItems();
  } else {
    alert("Failed to increase quantity.");
  }
}

//  Decrease Quantity
async function decreaseQuantity(id) {
  const res = await fetch(`${baseUrl}/decrease/${id}`, {
    method: 'POST',
    headers: getAuthHeader()
  });

  if (res.ok) {
    loadItems();
  } else {
    alert("Failed to decrease quantity.");
  }
}

//  Filter by Category

async function filterByCategory() {
  const category = document.getElementById('filterCategory').value;

  const res = await fetch(`${baseUrl}/category/${category}`, {
    headers: getAuthHeader()
  });

  if (!res.ok) {
    alert("Failed to filter.");
    return;
  }

  allItems = await res.json();
  currentPage = 1;
  renderPaginatedItems();
  renderPaginationControls();
}


//  Import Excel
async function importExcel() {
  const fileInput = document.getElementById('excelFile');
  if (!fileInput.files.length) {
    alert("Select an Excel file first.");
    return;
  }

  const formData = new FormData();
  formData.append('file', fileInput.files[0]);

  const res = await fetch(`${baseUrl}/import`, {
    method: 'POST',
    headers: {
      'Authorization': 'Basic ' + localStorage.getItem('auth') // exclude content-type for FormData
    },
    body: formData
  });

  if (res.ok) {
    alert("Import successful!");
    loadItems();
  } else {
    const errorText = await res.text();
    alert("Import failed: " + errorText);
  }
}

//  Export to PDF
async function downloadPdfReport() {
  const res = await fetch(`${baseUrl}/export/pdf`, {
    headers: getAuthHeader()
  });

  if (res.ok) {
    const blob = await res.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'inventory_report.pdf';
    a.click();
    a.remove();
  } else {
    alert("PDF export failed.");
  }
}
//  Download PDF by Category
async function downloadPdfByCategory() {
  const category = document.getElementById('pdfCategory').value.trim();
  if (!category) {
    alert("Please enter a category.");
    return;
  }

  const res = await fetch(`${baseUrl}/export/pdf/category/${category}`, {
    headers: getAuthHeader()
  });

  if (res.ok) {
    const blob = await res.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `inventory_category_${category}.pdf`;
    a.click();
  } else {
    alert("Failed to export PDF for category.");
  }
}

//  Download PDF by Location
async function downloadPdfByLocation() {
  const location = document.getElementById('pdfLocation').value.trim();
  if (!location) {
    alert("Please enter a location.");
    return;
  }

  const res = await fetch(`${baseUrl}/export/pdf/location/${location}`, {
    headers: getAuthHeader()
  });

  if (res.ok) {
    const blob = await res.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `inventory_location_${location}.pdf`;
    a.click();
  } else {
    alert("Failed to export PDF for location.");
  }
}

//  Filter Low Stock Items

async function filterLowStock() {
  const threshold = document.getElementById('lowStockThreshold').value.trim();
  if (!threshold) {
    alert("Please enter a threshold.");
    return;
  }

  const res = await fetch(`${baseUrl}/report/low-stock?threshold=${threshold}`, {
    headers: getAuthHeader()
  });

  if (!res.ok) {
    alert("Failed to fetch low stock items.");
    return;
  }

  allItems = await res.json();
  currentPage = 1;
  renderPaginatedItems();
  renderPaginationControls();
}


//  Download Low Stock PDF
async function downloadLowStockPdf() {
  const threshold = document.getElementById('lowStockThreshold').value.trim() || 5;

  const res = await fetch(`${baseUrl}/report/low-stock?threshold=${threshold}`, {
    headers: getAuthHeader()
  });

  if (!res.ok) {
    alert("Failed to fetch low stock data for PDF.");
    return;
  }

  const items = await res.json();
  if (!items.length) {
    alert("No low stock items found.");
    return;
  }

  const resPdf = await fetch(`${baseUrl}/export/pdf/low-stock?threshold=${threshold}`, {
    headers: getAuthHeader()
  });

  if (resPdf.ok) {
    const blob = await resPdf.blob();
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `low_stock_report_threshold_${threshold}.pdf`;
    a.click();
  } else {
    alert("Failed to export low stock PDF.");
  }
}


//  On Page Load
window.onload = loadItems;
