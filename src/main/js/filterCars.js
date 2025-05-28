import { csrfHeaderName, csrfToken } from './util/csrf.js'

const results = document.querySelector('#search-results')
const allCarsContainer = document.querySelector('#all-cars')

document.querySelector('#brand').addEventListener('keyup', async (e) => {
    const searchField = e.target
    const brand = searchField.value.trim() // Trim spaces

    if (brand === '') {
        results.innerHTML = '' // Clear search results
        allCarsContainer.style.display = 'flex' // Show all cars again
        return
    }

    const response = await fetch(`/api/cars?brand=${brand}`, {
        headers: {
            [csrfHeaderName]: csrfToken,
            Accept: 'application/json'
        }
    })

    if (response.status === 200) {
        const cars = await response.json()
        results.innerHTML = '' // Clear previous results
        allCarsContainer.style.display = 'none' // Hide all cars

        if (cars.length === 0) {
            results.innerHTML =
                '<p class="d-flex justify-content-center align-items-center vh-80">No results found!</p>'
        } else {
            let cardContainer = document.createElement('div')
            cardContainer.classList.add('row', 'row-cols-1', 'row-cols-sm-2', 'row-cols-lg-4')

            cars.forEach(car => {
                let card = document.createElement('div')
                card.classList.add('col')
                card.innerHTML = `
                    <div class="card mb-4">
                        <img src="/images/${car.image}" class="card-img-top" alt="Car Image">
                        <div class="card-body">
                            <h5 class="card-title">${car.brand}</h5>
                            <p class="card-text">
                                <strong>Model:</strong> ${car.model}
                            </p>
                            <div class="card-footer text-center mt-auto">
                                <a href="/car/${car.id}" class="btn btn-primary">View Details</a>
                            </div>
                        </div>
                    </div>
                `
                cardContainer.appendChild(card)
            })

            results.appendChild(cardContainer)
        }
    } else {
        alert('Something went wrong')
    }
})
