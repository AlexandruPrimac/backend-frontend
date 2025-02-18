const results = document.querySelector("#search-results");
const allRaces = document.querySelector("#all-races");

document.querySelector("#location").addEventListener("keyup", async (e) => {
    const searchField = e.target;
    const location = searchField.value.trim();

    if (location === "") {
        results.innerHTML = "";
        allRaces.style.display = "flex";
        return;
    }

    const response = await fetch(`/api/races?location=${location}`, {
        headers: {'Accept': 'application/json'}
    });

    if (response.status === 200) {
        const races = await response.json();
        results.innerHTML = '';
        allRaces.style.display = "none";
        if (races.length === 0) {
            results.innerHTML = '<p class="d-flex justify-content-center align-items-center vh-80" >No results found!</p>';
        } else {
            let cardContainer = document.createElement("div");
            cardContainer.classList.add("row", "row-cols-1", "row-cols-sm-2", "row-cols-lg-4");

            races.forEach(race => {
                let card = document.createElement("div");
                card.classList.add("col");
                card.innerHTML = `
                    <div class="card mb-4">
                        <img src="/images/${race.image}" class="card-img-top" alt="Race Image">
                        <div class="card-body">
                            <h5 class="card-title">${race.name}</h5>
                             <p class="card-text">
                                <strong>Track:</strong> ${race.track}
                            </p>
                            <div class="card-footer text-center mt-auto">
                                <a href="/race/${race.id}" class="btn btn-primary">View Details</a>
                            </div>
                        </div>
                    </div>
                `;
                cardContainer.appendChild(card);
            });

            results.appendChild(cardContainer);
        }
    } else {
        alert('Something went wrong');
    }
});

