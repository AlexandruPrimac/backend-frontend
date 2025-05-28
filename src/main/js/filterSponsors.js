import { csrfHeaderName, csrfToken } from './util/csrf.js'

const results = document.querySelector('#search-results')
const allSponsors = document.querySelector('#all-sponsors')

document.querySelector('#name').addEventListener('keyup', async (e) => {
    const searchField = e.target
    const name = searchField.value.trim().toLowerCase() // Convert to lowercase for case-insensitive search

    // Reset the search results if no input
    if (name === '') {
        results.innerHTML = ''
        allSponsors.style.display = 'flex' // Show all sponsors
        return
    }

    // Fetch filtered sponsors from the API
    const response = await fetch(`/api/sponsors?name=${name}`, {
        headers: {
            [csrfHeaderName]: csrfToken,
            Accept: 'application/json'
        }
    })

    // Check if the response is successful
    if (response.status === 200) {
        const sponsors = await response.json()
        results.innerHTML = '' // Clear previous results
        allSponsors.style.display = 'none' // Hide the original sponsors

        if (sponsors.length === 0) {
            results.innerHTML =
                '<p class="d-flex justify-content-center align-items-center vh-80">No results found!</p>'
        } else {
            let cardContainer = document.createElement('div')
            cardContainer.classList.add('row', 'row-cols-1', 'row-cols-sm-2', 'row-cols-lg-4')

            // Create sponsor cards dynamically
            sponsors.forEach(sponsor => {
                let card = document.createElement('div')
                card.classList.add('col')
                card.innerHTML = `
                    <div class="card mb-4">
                        <img src="/images/${sponsor.image}" class="card-img-top" alt="Sponsor Image">
                        <div class="card-body">
                            <h5 class="card-title">${sponsor.name}</h5>
                            <p class="card-text">
                                <strong>Industry:</strong> ${sponsor.industry}
                            </p>
                            <p class="card-text">
                                <strong>Founded:</strong> ${sponsor.foundingYear}
                            </p>
                        </div>
                    </div>
                `
                cardContainer.appendChild(card)
            })

            // Append the new sponsor cards to the results container
            results.appendChild(cardContainer)
        }
    } else {
        alert('Something went wrong, please try again later.')
    }
})
