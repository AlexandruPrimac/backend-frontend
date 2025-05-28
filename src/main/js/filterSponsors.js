import { csrfHeaderName, csrfToken } from './util/csrf.js'

const results = document.querySelector('#search-results')
const allSponsors = document.querySelector('#all-sponsors')

document.querySelector('#name').addEventListener('keyup', async (e) => {
    const searchField = e.target
    const name = searchField.value.trim().toLowerCase()

    if (name === '') {
        results.innerHTML = ''

        // Fade in the allSponsors container
        allSponsors.style.display = 'flex'
        allSponsors.style.opacity = '0'
        allSponsors.animate(
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

    const response = await fetch(`/api/sponsors?name=${name}`, {
        headers: {
            [csrfHeaderName]: csrfToken,
            Accept: 'application/json'
        }
    })

    if (response.status === 200) {
        const sponsors = await response.json()

        function showResults() {
            allSponsors.style.display = 'none'
            results.innerHTML = ''

            if (sponsors.length === 0) {
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

        if (allSponsors.style.display !== 'none') {
            const fadeOutAnim = allSponsors.animate(
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
        alert('Something went wrong, please try again later.')
    }
})
