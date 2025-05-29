import {csrfHeaderName, csrfToken} from './util/csrf.js'

const results = document.querySelector('#search-results')
const allCarsContainer = document.querySelector('#all-cars')

document.querySelector('#brand').addEventListener('keyup', async (e) => {
    const searchField = e.target
    const brand = searchField.value.trim() // Trim spaces

    if (brand === '') {
        results.innerHTML = '' // Clear search results

        // Make all cars container visible but start transparent
        allCarsContainer.style.display = 'flex'
        allCarsContainer.style.opacity = '0'

        // Animate fade-in
        allCarsContainer.animate(
            [
                {opacity: 0},
                {opacity: 1}
            ],
            {
                duration: 500,
                easing: 'ease-out',
                fill: 'forwards'
            }
        )

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

        // Function to handle showing results after hiding old cars
        function showResults() {
            allCarsContainer.style.display = 'none'
            results.innerHTML = '' // Clear previous results

            if (cars.length === 0) {
                const noResults = document.createElement('p')
                noResults.className = 'd-flex justify-content-center align-items-center vh-80'
                noResults.textContent = 'No results found!'
                results.appendChild(noResults)

                requestAnimationFrame(() => {
                    noResults.animate(
                        [
                            {opacity: 0, transform: 'scale(0.95)'},
                            {opacity: 1, transform: 'scale(1)'}
                        ],
                        {
                            duration: 500,
                            easing: 'ease-out',
                            fill: 'forwards'
                        }
                    )
                })
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

                    requestAnimationFrame(() => {
                        card.animate(
                            [
                                {opacity: 0, transform: 'translateY(20px)'},
                                {opacity: 1, transform: 'translateY(0)'}
                            ],
                            {
                                duration: 400,
                                easing: 'ease-out',
                                fill: 'forwards'
                            }
                        )
                    })
                })

                results.appendChild(cardContainer)

                // Animate the entire container once appended
                cardContainer.animate(
                    [
                        {opacity: 0, transform: 'translateY(20px)'},
                        {opacity: 1, transform: 'translateY(0)'}
                    ],
                    {
                        duration: 600,
                        easing: 'ease-out',
                        fill: 'forwards'
                    }
                )
            }
        }

        if (allCarsContainer.style.display !== 'none') {
            // Animate fade-out of old cars container before showing results
            const fadeOutAnim = allCarsContainer.animate(
                [
                    {opacity: 1},
                    {opacity: 0}
                ],
                {
                    duration: 300,
                    easing: 'ease-in',
                    fill: 'forwards'
                }
            )

            fadeOutAnim.finished.then(showResults)
        } else {
            showResults()
        }
    } else {
        alert('Something went wrong')
    }
})
