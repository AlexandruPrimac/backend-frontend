import { csrfHeaderName, csrfToken } from './util/csrf.js'

const results = document.querySelector('#search-results')
const allRaces = document.querySelector('#all-races')

document.querySelector('#location').addEventListener('keyup', async (e) => {
    const searchField = e.target
    const location = searchField.value.trim()

    if (location === '') {
        results.innerHTML = ''

        // Show all races container with fade-in animation
        allRaces.style.display = 'flex'
        allRaces.style.opacity = '0'
        allRaces.animate(
            [
                { opacity: 0 },
                { opacity: 1 }
            ],
            {
                duration: 500,
                easing: 'ease-out',
                fill: 'forwards'
            }
        )
        return
    }

    const response = await fetch(`/api/races?location=${location}`, {
        headers: {
            [csrfHeaderName]: csrfToken,
            Accept: 'application/json'
        }
    })

    if (response.status === 200) {
        const races = await response.json()

        // Function to show the results after fading out old container
        function showResults() {
            allRaces.style.display = 'none'
            results.innerHTML = ''

            if (races.length === 0) {
                const noResults = document.createElement('p')
                noResults.className = 'd-flex justify-content-center align-items-center vh-80'
                noResults.textContent = 'No results found!'
                results.appendChild(noResults)

                requestAnimationFrame(() => {
                    noResults.animate(
                        [
                            { opacity: 0, transform: 'scale(0.95)' },
                            { opacity: 1, transform: 'scale(1)' }
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

                races.forEach(race => {
                    let card = document.createElement('div')
                    card.classList.add('col')
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
                    `
                    cardContainer.appendChild(card)

                    requestAnimationFrame(() => {
                        card.animate(
                            [
                                { opacity: 0, transform: 'translateY(20px)' },
                                { opacity: 1, transform: 'translateY(0)' }
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

                cardContainer.animate(
                    [
                        { opacity: 0, transform: 'translateY(20px)' },
                        { opacity: 1, transform: 'translateY(0)' }
                    ],
                    {
                        duration: 600,
                        easing: 'ease-out',
                        fill: 'forwards'
                    }
                )
            }
        }

        if (allRaces.style.display !== 'none') {
            const fadeOutAnim = allRaces.animate(
                [
                    { opacity: 1 },
                    { opacity: 0 }
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
