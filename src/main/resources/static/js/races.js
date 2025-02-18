const deleteButton = document.getElementById("delete-button")

deleteButton.addEventListener("click", async e => {
    if (!confirm("Are you sure you want to delete this race?")) {
        return;
    }

    // Get the race ID from a data attribute on the button
    const raceId = deleteButton.getAttribute("data-race-id");

    if (!raceId) {
        alert("Race ID is missing!");
        return;
    }

    try {
        const response = await fetch(`/api/races/${raceId}`, {method: "DELETE"});

        if (response.status === 204) {
            alert("Race deleted successfully!");
            location.href = "/races"; // Redirect to the races list page after deletion
        } else {
            alert("Race not found or could not be deleted.");
        }
    } catch (error) {
        console.error("Error deleting race:", error);
        alert("Something went wrong. Please try again.");
    }
})