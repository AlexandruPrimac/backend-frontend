const form = document.querySelector("#add-car-form");

form.addEventListener("submit", async e => {
    e.preventDefault();

    const brand = document.querySelector("#brand").value;
    const model = document.querySelector("#model").value;
    const engine = document.querySelector("#engine").value;
    const horsePower = Number(document.querySelector("#horsepower").value)
    const year = document.querySelector("#year").value;
    const category = document.querySelector("#category").value;

    const jsonBody = JSON.stringify({ brand, model, engine, horsePower, year, category });

    console.log("Sending data:", jsonBody);

    try {
        const response = await fetch('http://localhost:8080/api/cars', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: jsonBody
        });

        console.log("Response status:", response.status);

        if (response.status === 201) {
            const car = await response.json();
            alert(`Congrats, your car got created. It has ID #${car.id}`);
            window.location = `/car/${car.id}`;
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
