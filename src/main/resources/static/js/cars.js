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
        const response = await fetch(`/api/cars/${carId}`, { method: "DELETE" });

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
