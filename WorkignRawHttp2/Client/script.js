document.getElementById("fetchButton").addEventListener("click", fetchEmployees);
document.getElementById("addButton").addEventListener("click", addEmployee);
document.getElementById("fetchByIdButton").addEventListener("click", fetchEmployeeById);
document.getElementById("deleteButton").addEventListener("click", deleteEmployee);
document.getElementById("updateButton").addEventListener("click", updateEmployee);
const sendMessageButton = document.getElementById('sendMessageButton');
sendMessageButton.addEventListener('click', sendMessageToServer);

let webSocket;


document.getElementById("connectButton").addEventListener("click", connectWebSocket);




function fetchEmployees() {
    const employeeList = document.getElementById("employeeList");
    employeeList.innerHTML = "";

    fetch("https://localhost:8443/api/rest/employees")
        .then(response => response.json())
        .then(data => {
            data.forEach(employee => {
                const employeeItem = createEmployeeListItem(employee);
				console.log(employeeItem);
                employeeList.appendChild(employeeItem);
            });
        })
        .catch(error => {
            console.error("REST Error:", error);
            employeeList.innerHTML = "<li>Error fetching employees.</li>";
        });
}

function createEmployeeListItem(employee) {
    const employeeItem = document.createElement("li");
    employeeItem.innerHTML = `
        <strong>ID:</strong> ${employee.id}<br>
        <strong>Name:</strong> ${employee.name}<br>
        <strong>Age:</strong> ${employee.age}<br>
        <strong>Location:</strong> ${employee.location}<br>
        <strong>Email:</strong> ${employee.email}<br>
        <strong>Department:</strong> ${employee.department}
    `;
    return employeeItem;
}


function addEmployee() {
    const name = document.getElementById("nameInput").value;
    const age = document.getElementById("ageInput").value;
    const location = document.getElementById("locationInput").value;
    const email = document.getElementById("emailInput").value;
    const department = document.getElementById("departmentInput").value;

    const employee = {
        name,
        age: age ? parseInt(age) : null,
        location,
        email,
        department
    };

    fetch("https://localhost:8443/api/rest/employees", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(employee)
    })
    .then(response => response.json())
    .then(data => {
        console.log("Added employee:", data);
        // You can choose to display the added employee in the list or update the UI as needed
    })
    .catch(error => {
        console.error("REST Error:", error);
    });
}

function fetchEmployeeById() {
    const employeeId = document.getElementById("employeeIdInput").value;

    fetch(`https://localhost:8443/api/rest/employees/${employeeId}`)
        .then(response => response.json())
        .then(data => {
            console.log("Fetched employee:", data);
            displayFetchedEmployee(data);
        })
        .catch(error => {
            console.error("REST Error:", error);
            const fetchedEmployeeContainer = document.getElementById("fetchedEmployee");
            fetchedEmployeeContainer.innerHTML = "Employee not found.";
        });
}

function displayFetchedEmployee(employee) {
    const fetchedEmployeeContainer = document.getElementById("fetchedEmployee");
    fetchedEmployeeContainer.innerHTML = `
        <strong>ID:</strong> ${employee.id}<br>
        <strong>Name:</strong> ${employee.name}<br>
        <strong>Age:</strong> ${employee.age}<br>
        <strong>Location:</strong> ${employee.location}<br>
        <strong>Email:</strong> ${employee.email}<br>
        <strong>Department:</strong> ${employee.department}
    `;
}




function deleteEmployee() {
    const employeeId = document.getElementById("deleteEmployeeId").value;

    fetch(`https://localhost:8443/api/rest/employees?id=${employeeId}`, {
        method: "DELETE"
    })
    .then(response => {
        if (response.ok) {
            console.log("Employee deleted successfully.");
            const deleteStatus = document.getElementById("deleteStatus");
            deleteStatus.textContent = "Employee deleted successfully.";
        } else {
            console.error("Failed to delete employee.");
            const deleteStatus = document.getElementById("deleteStatus");
            deleteStatus.textContent = "Failed to delete employee.";
        }
    })
    .catch(error => {
        console.error("REST Error:", error);
    });
}

// ... (existing code)

function updateEmployee() {
    const employeeId = document.getElementById("updateEmployeeId").value;
    const name = document.getElementById("updateNameInput").value;
    const age = document.getElementById("updateAgeInput").value;
    const location = document.getElementById("updateLocationInput").value;
    const email = document.getElementById("updateEmailInput").value;
    const department = document.getElementById("updateDepartmentInput").value;

    fetch(`https://localhost:8443/api/rest/employees/${employeeId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Employee not found.");
            }
            return response.json();
        })
        .then(existingEmployee => {
            const updatedEmployee = {
                id: existingEmployee.id,
                name: name || existingEmployee.name,
                age: age ? parseInt(age) : existingEmployee.age,
                location: location || existingEmployee.location,
                email: email || existingEmployee.email,
                department: department || existingEmployee.department
            };

            return fetch(`https://localhost:8443/api/rest/employees/${employeeId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(updatedEmployee)
            });
        })
        .then(response => {
            if (response.ok) {
                console.log("Employee updated successfully.");
                const updateStatus = document.getElementById("updateStatus");
                updateStatus.textContent = "Employee updated successfully.";
            } else {
                console.error("Failed to update employee.");
                const updateStatus = document.getElementById("updateStatus");
                updateStatus.textContent = "Failed to update employee.";
            }
        })
        .catch(error => {
            console.error("REST Error:", error);
            const updateStatus = document.getElementById("updateStatus");
            updateStatus.textContent = error.message || "An error occurred.";
        });
}

function connectWebSocket() {
    webSocket = new WebSocket("wss://localhost:8443/api/websocket");

    webSocket.onopen = event => {
        console.log("WebSocket connection opened:", event);
    };

    webSocket.onmessage = event => {
    const messageContainer = document.getElementById("websocketMessage");
    const message = event.data;

    try {
        const parsedMessage = JSON.parse(message);
        if (parsedMessage.message !== undefined && parsedMessage.value !== undefined) {
            messageContainer.textContent = `Message: ${parsedMessage.message}, Value: ${parsedMessage.value}`;
        } else {
            messageContainer.textContent = message;
        }
    } catch (error) {
        messageContainer.textContent = message;
    }
};
	
    webSocket.onclose = event => {
        console.log("WebSocket connection closed:", event);
    };
}

function sendMessageToServer() {
	

	
	const messageInput = document.getElementById("messageInput").value;
    const message = messageInput || '';
    const value = message ? 0 : 0;

    const data = {
        message: message,
        value: value
    };

    if (webSocket && webSocket.readyState === WebSocket.OPEN) {
        webSocket.send(JSON.stringify(data));
        document.getElementById("messageInput").value = '';
    }
	

    
}
