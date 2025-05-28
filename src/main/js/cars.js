import '../scss/cars.scss'
import { csrfToken, csrfHeaderName } from './util/csrf.js'

const deleteButton = document.getElementById("delete-button")
deleteButton.addEventListener("click", async e => {

    if (!confirm("Are you sure you want to delete this car?")) {
        return;
    }

    // Get the car ID from a data attribute on the button
    const carId = deleteButton.getAttribute("data-car-id");

    if (!carId) {
        alert("Car ID is missing!");
        return;
    }

    try {
        const response = await fetch(`/api/cars/${carId}`, {
            method: "DELETE", headers: {
                [csrfHeaderName]: csrfToken,
                'Content-Type': 'application/json'
            }
        });

        if (response.status === 204) {
            alert("Car deleted successfully!");
            location.href = "/cars"; // Redirect to the cars list page after deletion
        } else {
            alert("Car not found or could not be deleted.");
        }
    } catch (error) {
        console.error("Error deleting car:", error);
        alert("Something went wrong. Please try again.");
    }
});


document.addEventListener("DOMContentLoaded", () => {
    const editButton = document.getElementById("edit-button");
    const saveButton = document.getElementById("save-button");
    const cancelButton = document.getElementById("cancel-button");
    const carCategory = document.getElementById('car-category');
    const inputs = document.querySelectorAll("input");

    let originalValues = {}; // Store original values for cancel

    // Enable editing mode
    editButton.addEventListener("click", () => {
        // Save original values
        inputs.forEach(input => originalValues[input.id] = input.value);

        // Enable inputs
        inputs.forEach(input => input.removeAttribute("disabled"));

        carCategory.removeAttribute("disabled");

        // Show Save & Cancel, Hide Edit
        editButton.style.display = "none";
        saveButton.style.display = "inline-block";
        cancelButton.style.display = "inline-block";
    });

    // Cancel editing (restore original values)
    cancelButton.addEventListener("click", () => {
        inputs.forEach(input => input.value = originalValues[input.id]); // Restore values
        inputs.forEach(input => input.setAttribute("disabled", "true")); // Disable inputs
        carCategory.setAttribute("disabled", "true"); // Disable category input

        // Show Edit, Hide Save & Cancel
        editButton.style.display = "inline-block";
        saveButton.style.display = "none";
        cancelButton.style.display = "none";
    });

    // Save updates via PATCH
    saveButton.addEventListener("click", async () => {
        const carId = document.getElementById("delete-button").getAttribute("data-car-id");

        const updatedCar = {
            brand: document.getElementById("car-brand").value,
            model: document.getElementById("car-model").value,
            engine: parseFloat(document.getElementById("car-engine").value),
            horsepower: parseInt(document.getElementById("car-horsepower").value),
            year: parseInt(document.getElementById("car-year").value),
            category: document.getElementById("car-category").value
        };

        try {
            const response = await fetch(`/api/cars/${carId}`, {
                method: "PATCH",
                headers: {
                    'Accept': 'application/json' ,
                    [csrfHeaderName]: csrfToken,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedCar)
            });

            if (response.ok) {
                alert("Car updated successfully!");
                inputs.forEach(input => input.setAttribute("disabled", "true")); // Disable inputs
                carCategory.setAttribute("disabled", "true"); // Disable category input
                editButton.style.display = "inline-block";
                saveButton.style.display = "none";
                cancelButton.style.display = "none";
            } else {
                const errorMsg = await response.text();
                alert("Failed to update car: " + errorMsg);
            }
        } catch (error) {
            console.error("Error updating car:", error);
            alert("Something went wrong. Please try again.");
        }
    });
});

