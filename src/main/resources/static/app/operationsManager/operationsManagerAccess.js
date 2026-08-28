// Startpoint per la Registrazione dell'Operations Manager
document.getElementById("registerButton").addEventListener("click", async function (e) {
    e.preventDefault();

    const fullName = document.getElementById("regName").value;
    const email = document.getElementById("regEmail").value;
    const password = document.getElementById("regPassword").value;

    const customer = { 
        fullName, 
        email, 
        password 
    };

    try {
        const response = await fetch("/api/hotelManagementSystem/registerOperationsManager", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(customer),
            credentials: 'include'
        });

        const result = await response.text();
        alert(result);

        if (response.ok) {
            window.location.href = "/operationsManager/operationsManagerAccess";
        }
    } catch (error) {
        alert("Error: " + error.message);
    }
});
