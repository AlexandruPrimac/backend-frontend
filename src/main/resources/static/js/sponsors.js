const deleteButton = document.querySelectorAll(".remove-sponsor-button")

deleteButton.forEach(deleteButton => {

    deleteButton.addEventListener("click", async e => {
        if (!confirm("Are you sure you want to delete this sponsor?")) {
            return;
        }

        // Get the sponsor ID from a data attribute on the button
        const sponsorId = deleteButton.getAttribute("data-sponsor-id");

        if (!sponsorId) {
            alert("Sponsor ID is missing!");
            return;
        }

        try {
            const response = await fetch(`/api/sponsors/${sponsorId}`, {method: "DELETE"});

            if (response.status === 204) {
                alert("Sponsor deleted successfully!");
                location.reload();
            } else {
                alert("Sponsor not found or could not be deleted.");
            }
        } catch (error) {
            console.error("Error deleting sponsor:", error);
            alert("Something went wrong. Please try again.");
        }
    })

})
