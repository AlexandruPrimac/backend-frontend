const form = document.getElementById("add-race-form")

form.addEventListener("submit", async e => {
    e.preventDefault();

    const name = document.querySelector("#name").value;
    const date = document.querySelector("#date").value;
    const track = document.querySelector("#track").value;
    const location = document.querySelector("#location").value;
    const distance = document.querySelector("#distance").value;

    const jsonBody = JSON.stringify({ name, date, track, location, distance});

    console.log("Sending data:", jsonBody);

    try {
        const response = await fetch('http://localhost:8080/api/races', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: jsonBody
        });

        console.log("Response status:", response.status);

        if (response.status === 201) {
            const race = await response.json();
            alert(`Congrats, your race got created. It has ID #${race.id}`);
            window.location = `/race/${race.id}`;
        } else {
            const errorMsg = await response.text();
            console.error("Server error:", errorMsg);
            alert('Something went wrong: ' + errorMsg);
        }
    } catch (error) {
        console.error("Error during fetch:", error);
        alert("Failed to connect to the server");
    }
});